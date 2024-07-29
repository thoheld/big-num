import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class BigNumArithmetic {

	/*
	 * This method opens and reads from the specified input file. It converts
	 * lines from the file into expression strings to be sent to the
	 * expressionProcessor method. 
	 */
	public static void main(String[] args) {
		
		if (args.length != 1) { // no file specified
			return;
		}
		
		String fileName = args[0];
		
		String[] lines = new String[10000]; // store lines from file
		
		try { // open file
			
			FileInputStream file = new FileInputStream(fileName); // open file
			Scanner s = new Scanner(file); // open scanner on file
			
			// collect lines from file
			int i = 0;
			while (s.hasNextLine()) { // for all lines in file
				lines[i] = s.nextLine(); // store line in lines
				i++;
			}
			 
			s.close(); // close scanner
			 
		} catch (FileNotFoundException e) { // file not found
			return;
		}
		
		int i = 0;
		while (lines[i] != null) { // for every line
			
			String[] pieces = lines[i].split(" "); // split up line
			String expression = "";
			for (String str : pieces) { // for each piece of the line
				// make sure it's either a number or operator
				if (!str.equals("")) {
					
					if (isNumber(str)) {
						// add number to expression string
						expression += str + " ";
					} else {
						// handle back-to-back operators
						for (int j = 0; j < str.length(); j++) {
							expression += str.substring(j, j+1) + " ";
						}
					}
				}
			}
			
			if (expression.length() == 0) { // skip empty lines
				i++;
				continue;
			}
			expressionProcessor(expression); // send to expressionProcessor
			
			i++; // next line
		}

	}
	
	
	/*
	 * This method takes a given expression string and prints it out in proper format.
	 * If the expression is valid, it will print the answer as well, otherwise it
	 * will leave it blank. It will return true for valid expressions, and false for
	 * invalid expressions.
	 */
	public static boolean expressionProcessor(String expression) {
		
		// seperate string into numbers and operators
		String[] numsAndOps = expression.split(" ");
		LStack nums = new LStack(); // stack to store numbers
		boolean valid = true; // track whether the expression is valid
		
		for (String x : numsAndOps) { // for every number and operator
			
			if (isNumber(x)) { // if x is a number
				LList num = new LList();
				for (int i = 0; i < x.length(); i++) { // for every digit in x
					// add digit to LList (in reverse order)
					num.insert(Integer.parseInt(x.substring(i, i+1)));
				}
				// print number to output
				System.out.print(BigNumArithmetic.convertToString(num) + " ");
				nums.push(num); // push number to stack
			
			} else if (x.equals("+")) { // addition operation
				System.out.print("+ "); // print + to output
				if (!valid) { continue; } // if invalid, skip calculations
				LList a = (LList) nums.pop(); // pop two numbers off of stack
				LList b = (LList) nums.pop();
				if (a == null || b == null) { // two numbers were not popped
					valid = false;
					continue;
				}
				// push sum of a and b to stack
				nums.push(BigNumArithmetic.addition(a, b));
			
			} else if (x.equals("*")) { // multiplication operation
				System.out.print("* "); // print * to output
				if (!valid) { continue; } // if invalid, skip calculations
				LList a = (LList) nums.pop(); // pop two numbers off of stack
				LList b = (LList) nums.pop();
				if (a == null || b == null) { // two numbers were not popped
					valid = false;
					continue;
				}
				nums.push(BigNumArithmetic.multiplication(a, b));
			
			} else if (x.equals("^")) { // exponentiation operation
				System.out.print("^ "); // print ^ to output
				if (!valid) { continue; } // if invalid, skip calculations
				LList b = (LList) nums.pop(); // pop two numbers off of stack
				LList a = (LList) nums.pop();
				if (a == null || b == null) { // two numbers were not popped
					valid = false;
					continue;
				}
				nums.push(BigNumArithmetic.exponentiation(a, BigNumArithmetic.toInt(b)));

			} else { // x was neither a number nor operator
				System.out.print(x + " "); // print x to output
				valid = false;

			}
			
		}
		
		
		// more than a single solution left on the stack, invalid
		if (nums.length() != 1) { valid = false; }
		
		// print answer to output
		if (!valid) {
			System.out.println("= "); // invalid, don't print answer
		} else {
			System.out.println("= " + 
			BigNumArithmetic.convertToString((LList) nums.pop()));
		}
		
		return valid;
	}
	
	
	/*
	 * This method returns whether or not a given string is a number. It is
	 * used for differentiating between numbers and operators in expressions.
	 */
	public static boolean isNumber(String str) {
		try {
			for (int i = 0; i < str.length(); i++) {
				Integer.parseInt(str.substring(i, i+1));
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	/*
	 * This method adds two LList numbers, and returns the sum as an LList
	 * number. If one LList is empty, it will return the value of the
	 * non-empty LList. If both are empty, it will return an empty LList.
	 */
	public static LList addition(LList a, LList b) {
		
		// allow for same exact LList number to be passed in as both a and b
		if (a == b) {
			LList aClone = new LList();
			while (!a.isAtEnd()) {
				aClone.append(a.getValue());
				a.next();
			}
			b = aClone;
		}
		
		LList sum = new LList(); // sum of a and b
		int digitSum = 0;
		int carry = 0;
		a.moveToStart();
		b.moveToStart();
		
		if (b.length() > a.length()) { // if b is longer, swap a and b
			LList temp = a;
			a = b;
			b = temp;
		}
		
		if (a.length() == 0) { // both a and b were empty, return empty LList
			return new LList();
		}
			
		while (!a.isAtEnd()) { // for all digits in a
			
			try { // add a's digit, b's digit, and carry
				digitSum = (int) a.getValue() + (int) b.getValue() + carry;
				
			} catch (NoSuchElementException e) { // no more digits in b
				digitSum = (int) a.getValue() + carry;
			}
			
			carry = digitSum / 10; // update carry
			digitSum = digitSum % 10; // remove carry from digitSum
			sum.append(digitSum); // add digitSum to sum
			
			a.next(); // next digit
			b.next();
		}
		
		if (carry == 1) { // if there is a carry left over, add it to the sum
			sum.append(1);
		}
		
		return sum;
	}
	
	
	/*
	 * This method adds two LList numbers, and returns the product as an LList
	 * number. If both are empty, it will return an empty LList.
	 */
	public static LList multiplication(LList a, LList b) {
		
		// allow for same exact LList number to be passed in as both a and b
		if (a == b) {
			LList aClone = new LList();
			while (!a.isAtEnd()) {
				aClone.append(a.getValue());
				a.next();
			}
			b = aClone;
		}
		
		if (a.length() > b.length()) { // ensure a is shorter or equal to b's length
			LList temp = a;
			a = b;
			b = temp;
		}
		
		if (b.length() == 0) { // both a and b were empty, return empty LList
			return new LList();
		}
		
		a.moveToStart();
		LStack product = new LStack();
		LList subProd;
		String expression = "";
		int digitProd;
		int carry;
		int place = 0;
		
		while(!a.isAtEnd()) { // for every digit of a
			
			b.moveToStart();
			subProd = new LList();
			carry = 0;
			for (int i = 0; i < place; i++) { // adjust for place
				subProd.append(0);
			}
			
			while (!b.isAtEnd()) {
				digitProd = ((int) a.getValue() * (int) b.getValue()) + carry;
				carry = digitProd / 10; // update carry
				digitProd = digitProd % 10; // remove carry from digitProd
				subProd.append(digitProd); // add digitProd to subProd
				b.next();
			}
			
			if (carry > 0) {
				subProd.append(carry); // add final carry
			}
			product.push(subProd);
			place++;
			a.next();
		}
		
		while (product.length() > 1) { // until one sum remains on product stack
			// pop two numbers from stack, add them, and push that sum back to product stack
			product.push(BigNumArithmetic.addition((LList) product.pop(), (LList) product.pop()));
		}
		
		return (LList) product.pop(); // return remaining value on product stack
	}
	
	
	/*
	 * This method takes an LList number a and an int n, and performs the operation a^n.
	 * 
	 */
	public static LList exponentiation(LList a, int n) {
		
		a.moveToStart();
		// base cases
		if (n == 2) { // a^2
			return BigNumArithmetic.multiplication(a, a);
		}
		if (n == 1) { // a^1
			return a;
		}
		if (n == 0) { // a^0
			LList one = new LList();
			one.append(1);
			return one;
		}
		
		// recursive cases
		if (n % 2 == 0) { // even
			// (x^2)^(n/2)
			return BigNumArithmetic.exponentiation(BigNumArithmetic.exponentiation(a, 2), n/2);
			
		} else { // odd
			// x * (x^2)^((n-1)/2)
			return BigNumArithmetic.multiplication(a, BigNumArithmetic.exponentiation(BigNumArithmetic.exponentiation(a, 2), (n-1)/2));
		}
		
	}
	
	
	/*
	 * Converts a given LList number to an int. Primarily used for exponentiation.
	 */
	public static int toInt(LList a) {
		int tens = 1; // digit's tens place
		int sum = 0;
		a.moveToStart();
		for (int i = 0; i < a.length(); i++) { // for every digit in a
			sum += ((int) a.getValue() * tens); // add digit to sum
			tens *= 10; // adjust for place
			a.next();
		}
		return sum;
	}
	
	/*
	 * This method takes an LList number and returns a properly formatted
	 * String version of it.
	 */
	public static String convertToString(LList num) {
		
		if (num.isEmpty()) { return ""; } // if LList is empty, return ""
		
		String str = "";
		boolean trimmingZeros = true;
		num.moveToEnd();
		while (true) { // moving backwards through LList
			
			num.prev();
			
			if (trimmingZeros) { // don't print leading zeros
				
				// if number is only zeros, return "0"
				if (num.currPos() == 0 && (int) num.getValue() == 0) {
					return "0";
				}
				if ((int) num.getValue() == 0) { // if zero, skip
					continue;
				} else {
					// beginning of the number has been found, stop trimming
					trimmingZeros = false;
				}
			}
			
			str += num.getValue(); // add digit to str
			if (num.currPos() == 0) { // reached last digit, break
				break;
			}
		}
		
		return str;
	}

}