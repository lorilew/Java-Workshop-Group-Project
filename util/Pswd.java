package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A Class to deal with all password methods.
 * @author lxl667
 * @version 2015-03-10
 */
public class Pswd {
	/**
	 * A username is valid if:
	 * 1. Contains only alphabetic characters and digits.
	 * @param username The username given by user.
	 * @return A boolean indicating the validation of the username.
	 */
	public static boolean isValidUsername(String username){
		char c;
		for(int i=0; i<username.length(); i++){
			c = username.charAt(i);
			if(!(Character.isLetterOrDigit(c))){
				return false;
			}
		}
		return true;
	}
	/**
	 * A password is valid if:
	 * 1. It has a length of 8 characters or more.
	 * 2. It contains at least 1 non letter or digit.
	 * 3. It contains at least 2 numbers.
	 * @param password The password given by user.
	 * @return A boolean indicating the validation of the password.
	 */
	public static boolean isValidPassword(String password){
		int d = 0; // number of digits in password
		int s = 0; // number of non alphabetic or numerical characters.
		// 1. check length is more length 7.
		if(password.length()<8){
			return false;
		}else{
			char c;
			
			// count number of digits and non-alpha/digit
			for(int i=0; i<password.length(); i++){
				c = password.charAt(i);
				if(c=='#') return false;
				if(!(Character.isLetterOrDigit(c))) s++;
				if(Character.isDigit(c)) d++;
			}
			// If has at least on non-alpha/digit and at least
			// two numbers - valid.
			if(s>0 && d>1)
				return true;
			return false;
		}
	}
	/**
	 * Uses the MD5 algorithm to generate a 128-bit (16-byte) hash value
	 * for the password.
	 * If an exception is thrown during this implementation the method will 
	 * return null.
	 * @param password The password given by the user.
	 * @return A 16-byte hash code for the password.
	 */
	public static String hashPassword(String password){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			byte[] bytedata = md.digest();
			
			// Convert the byte to the hex format
			StringBuffer hexString = new StringBuffer();
			for(int i=0; i<bytedata.length; i++){
				// use 0xff & is a mask to treat each byte as unsigned.
				String hex = Integer.toHexString(0xff & bytedata[i]);
				if(hex.length()==1)
					hexString.append('0'); // padding
				hexString.append(hex);
				
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("No such algorithm \"SHA-256\" when trying to hash password");
			e.printStackTrace();
		}
		return null;
	}
}
