//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import org.junit.Test;

/*
 * This class tests the methods of BigNumArithmetic. Included in this class is the helper function
 * compareLLists(), which takes two LLists and checks if their contents are identical to one another.
 */
public class BigNumArithmeticTest {

	/*
	 * This method tests the addition() function.
	 */
	@Test
	public void additionTest() {
		
		// create two non-empty LLists
		LList a = new LList();
		LList b = new LList();
		a.insert(9);
		a.insert(9);
		a.insert(9);
		a.insert(9);
		a.insert(9);
		a.insert(9);
		a.insert(9);
		a.insert(9);
		
		b.insert(9);
		b.insert(9);
		b.insert(0);
		b.insert(0);
		b.insert(0);
		b.insert(1);
		
		// create expected output
		LList e1 = new LList();
		e1.append(0);
		e1.append(0);
		e1.append(0);
		e1.append(0);
		e1.append(9);
		e1.append(9);
		e1.append(0);
		e1.append(0);
		e1.append(1);
		
		// ensure expected output matches actual output
		assertEquals(compareLLists(BigNumArithmetic.addition(a, b), e1), true);
		assertEquals(compareLLists(BigNumArithmetic.addition(a, b), a), false);
		
		
		// adding with 0
		LList d1= new LList();
		LList d2= new LList();
		d1.append(0);
		d1.append(0);
		d1.append(0);
		d2.append(0);
		d2.append(0);
		d2.append(0);
		
		assertEquals(compareLLists(BigNumArithmetic.addition(a, d1), a), true); // a + 0 = a
		assertEquals(compareLLists(BigNumArithmetic.addition(d1, d2), d1), true); // 0 + 0 = 0
		
		
		// adding non-empty LList and empty LList
		LList c1 = new LList();
		assertEquals(compareLLists(BigNumArithmetic.addition(a, c1), a), true);
		
		
		// adding two empty LLists (returns empty LList)
		LList c2 = new LList();
		assertEquals(compareLLists(BigNumArithmetic.addition(c1, c2), c1), true);
		
		
		// adding same exact LList
		LList same = new LList();
		same.insert(1);
		same.insert(5);
		LList e2 = new LList(); // expected
		e2.insert(3);
		e2.insert(0);
		assertEquals(compareLLists(BigNumArithmetic.addition(same, same), e2), true);


		
	}
	
	
	/*
	 * This method tests the multiplication() function.
	 */
	@Test
	public void multiplicationTest() {
		
		// multiplying two non-empty LLists
		LList a = new LList();
		LList b = new LList();
		a.insert(9);
		a.insert(8);
		
		b.insert(3);
		b.insert(4);
		b.insert(5);
		b.insert(6);
		
		LList e1 = new LList(); // expected
		e1.insert(3);
		e1.insert(3);
		e1.insert(8);
		e1.insert(6);
		e1.insert(8);
		e1.insert(8);
		assertEquals(compareLLists(BigNumArithmetic.multiplication(a, b), e1), true);
		
		
		// multiplying two empty LLists (returns empty LList)
		LList c = new LList();
		LList d = new LList();
		LList e2 = new LList();
		assertEquals(compareLLists(BigNumArithmetic.multiplication(c, d), e2), true);
	}
	
	
	/*
	 * This method tests the exponentiation() function.
	 */
	@Test
	public void exponentiationTest() {
		
		// 12^7
		LList a = new LList();
		a.insert(1);
		a.insert(2);
		LList e1 = new LList(); // expected = 35831808
		e1.insert(3);
		e1.insert(5);
		e1.insert(8);
		e1.insert(3);
		e1.insert(1);
		e1.insert(8);
		e1.insert(0);
		e1.insert(8);
		
		assertEquals(compareLLists(BigNumArithmetic.exponentiation(a, 7), e1), true);
		
		
		// to the 0th power (returns 1)
		LList one = new LList();
		one.append(1);
		assertEquals(compareLLists(BigNumArithmetic.exponentiation(a, 0), one), true);
		
	}
	
	
	/*
	 * This method tests the expressionProcessor() function.
	 */
	@Test
	public void expressionProcessorTest() {
		
		// valid expressions (should return true)
		assertEquals(BigNumArithmetic.expressionProcessor(""), true);
		assertEquals(BigNumArithmetic.expressionProcessor("00015"), true);
		assertEquals(BigNumArithmetic.expressionProcessor("00015 201 +"), true);
		assertEquals(BigNumArithmetic.expressionProcessor("00015 201 *"), true);
		assertEquals(BigNumArithmetic.expressionProcessor("00015 201 ^"), true);
		assertEquals(BigNumArithmetic.expressionProcessor("00015 201 + 30 +"), true);
		assertEquals(BigNumArithmetic.expressionProcessor("00015 201 * 30 *"), true);
		assertEquals(BigNumArithmetic.expressionProcessor("00015 2 ^ 30 ^"), true);
		assertEquals(BigNumArithmetic.expressionProcessor("00015 2 30 ^ +"), true);
		assertEquals(BigNumArithmetic.expressionProcessor("00015 2 30 ^ *"), true);
		assertEquals(BigNumArithmetic.expressionProcessor("00015 201 30 * +"), true);
		assertEquals(BigNumArithmetic.expressionProcessor("00015 2 + 30 *"), true);
		assertEquals(BigNumArithmetic.expressionProcessor("00015 2 ^ 30 *"), true);
		
		// invalid expressions (should return false)
		assertEquals(BigNumArithmetic.expressionProcessor("-15"), false);
		assertEquals(BigNumArithmetic.expressionProcessor("Hello, World!"), false);
		assertEquals(BigNumArithmetic.expressionProcessor("15 +"), false);
		assertEquals(BigNumArithmetic.expressionProcessor("15 *"), false);
		assertEquals(BigNumArithmetic.expressionProcessor("15 ^"), false);
		assertEquals(BigNumArithmetic.expressionProcessor("15 201 30 +"), false);
		assertEquals(BigNumArithmetic.expressionProcessor("15 201 30 40 * *"), false);

	}
	
	
	/*
	 * This is a helper function used in the above tests. It takes two
	 * LLists and checks if their contents are identical to one another.
	 */
	public boolean compareLLists(LList a, LList b) {
		if (a.length() != b.length()) { return false; } // if lengths are different, return false

		a.moveToStart();
		b.moveToStart();
		while (!a.isAtEnd()) { // for every element in each
			if (a.getValue() != b.getValue()) { return false; } // if they are not matching, return false
			a.next();
			b.next();
		}
		
		return true; // all elements matched
	}

}
