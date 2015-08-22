package util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PswdTests {
	/*
	 * Testing isValid password - length of 0
	 */
	@Test
	public void isValidtest1() {
		String input = "";
		boolean result = Pswd.isValidPassword(input);
		assertFalse(result);
	}
	/*
	 * Testing isValid password - length of 8, only alphabetic 
	 */
	@Test
	public void isValidtest2() {
		String input = "aaaaaaaa";
		boolean result = Pswd.isValidPassword(input);
		assertFalse(result);
	}
	/*
	 * Testing isValid password - length of 8, no digit
	 */
	@Test
	public void isValidtest3() {
		String input = "aaaaaa!!";
		boolean result = Pswd.isValidPassword(input);
		assertFalse(result);
	}
	/*
	 * Testing isValid password - length of 8, no non-alpha/digit
	 */
	@Test
	public void isValidtest4() {
		String input = "aaaaaa87";
		boolean result = Pswd.isValidPassword(input);
		assertFalse(result);
	}
	/*
	 * Testing isValid password - length of 8, no alpha
	 */
	@Test
	public void isValidtest5() {
		String input = "1!2!3!4!";
		boolean result = Pswd.isValidPassword(input);
		assertTrue(result);
	}
	/*
	 * Testing isValid password - only one digit
	 */
	@Test
	public void isValidtest6() {
		String input = "ahello1!";
		boolean result = Pswd.isValidPassword(input);
		assertFalse(result);
	}
	/*
	 * Testing isValid password - valid only 2 digits and 1 symbol
	 */
	@Test
	public void isValidtest7() {
		String input = "ahell01!";
		boolean result = Pswd.isValidPassword(input);
		assertTrue(result);
	}
	/*
	 * Testing isValid password - not valid - length
	 */
	@Test
	public void isValidtest8() {
		String input = "hell01!";
		boolean result = Pswd.isValidPassword(input);
		assertFalse(result);
	}
	/*
	 * Test hashPassword using online hash code.
	 */
	@Test
	public void hashPasswordTest1(){
		String input = "123456";
		String result = Pswd.hashPassword(input);
		assertEquals(result, "e10adc3949ba59abbe56e057f20f883e");
	}
	/*
	 * Test hashPassword returns same result twice.
	 */
	@Test
	public void hashPasswordTest2(){
		String input = "Hell01!";
		String result = Pswd.hashPassword(input);
		String input2 = "Hell01!";
		String result2 = Pswd.hashPassword(input2);
		assertEquals(result, result2);
	}
	/*
	 * Test hashPassword does not always produce same results.
	 */
	@Test
	public void hashPasswordTest3(){
		String input = "Hell012!";
		String result = Pswd.hashPassword(input);
		String input2 = "CUL8T3R!";
		String result2 = Pswd.hashPassword(input2);
		System.out.println(result + "\n" + result2);
		assertFalse(result.equals(result2));
	}
}
