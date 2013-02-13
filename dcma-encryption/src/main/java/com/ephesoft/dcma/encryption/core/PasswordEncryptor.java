/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2012 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.dcma.encryption.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import com.ephesoft.dcma.encryption.exception.CryptographyException;

/**
 * This class is used to encrypt the passwords.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.encryption.core.EncryptorDecryptor
 */
@SuppressWarnings("PMD")
public class PasswordEncryptor {

	/**
	 * To encrypt the string.
	 * @param decryptedString {@link String}
	 * @return {@link String}
	 * @throws CryptographyException {@link CryptographyException}
	 */
	public String encrypt(String decryptedString) throws CryptographyException {
		String encryptedString = "";
		if (!(decryptedString == null || decryptedString.length() == 0)) {
			encryptedString = EncryptorDecryptor.getEncryptorDecryptor().encryptString(decryptedString);
		}
		return encryptedString;
	}

	/**
	 * This method is used to load the class path for encryption and decryption process.
	 * @param libDir {@link File}
	 */
	@SuppressWarnings("deprecation")
	public static void loadClasspath(File libDir) {
		try {
			URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Class<?> urlClass = URLClassLoader.class;
			Method method = urlClass.getDeclaredMethod("addURL", new Class[] {URL.class});
			method.setAccessible(true);

			String webInfPath = libDir.getParent();
			File classesDir = new File(webInfPath + File.separator + "classes");
			method.invoke(urlClassLoader, new Object[] {classesDir.toURL()});

			File[] jarFiles = libDir.listFiles();
			for (File file : jarFiles) {
				if (file.getName().endsWith(".jar")) {
					method.invoke(urlClassLoader, new Object[] {file.toURL()});
				}
			}
			Class<?> passwordEncryptorClass = urlClassLoader.loadClass("com.ephesoft.dcma.encryption.core.PasswordEncryptor");
			Object passwordEncryptorInstance = passwordEncryptorClass.newInstance();
			Method encryptMethod = passwordEncryptorClass.getDeclaredMethod("encrypt");
			encryptMethod.setAccessible(true);
			encryptMethod.invoke(passwordEncryptorInstance);
			System.out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * The main method to run this plug in.
	 * @param args {@link String}
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("One Argument expected.");
			System.exit(1);
		}

		File libDir = new File(args[0]);
		if (!libDir.exists() && !libDir.isDirectory()) {
			System.err.println("Argument should be library location.");
			System.exit(1);
		}
		loadClasspath(libDir);
	}

	/**
	 * This method is used to encrypt passwords.
	 */
	public void encrypt() {
		boolean wantToContinue = true;
		PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
		BufferedReader bufferReader = null;
		InputStreamReader inputStreamReader = null;
		do {
			inputStreamReader = new InputStreamReader(System.in);
			bufferReader = new BufferedReader(inputStreamReader);
			String inputString = null;
			boolean encryptionSuccessful = true;
			System.out.println("********Enter the password*******");
			try {
				inputString = bufferReader.readLine();
			} catch (IOException e) {
				System.out.println("Could not read the input. Exiting the application.");
				System.exit(1);
			}
			String outputString = null;
			if (inputString != null) {
				try {
					outputString = passwordEncryptor.encrypt(inputString);
				} catch (CryptographyException e) {
					System.out.println("Error in encrypting the string." + e.getMessage());
					e.printStackTrace();
					encryptionSuccessful = false;
				} catch (Exception e) {
					System.out.println("Error in encrypting the string." + e.getMessage());
					e.printStackTrace();
					encryptionSuccessful = false;
				}
			}
			if (encryptionSuccessful) {
				System.out.println("*******The encrypted password is*******");
				System.out.println(outputString + "\n");
				System.out.println("Do you want to continue y/n");
			} else {
				System.out.println("Do you want to try again y/n");
			}
			String wantToContinueString = "n";
			wantToContinue = false;
			try {
				wantToContinueString = bufferReader.readLine();
			} catch (IOException e) {
				System.out.println("Could not read the input. Exiting the application.");
				System.exit(1);
			}
			if (wantToContinueString.equalsIgnoreCase("y")) {
				wantToContinue = true;
			}
		} while (wantToContinue);
		try {
			if (bufferReader != null) {
				bufferReader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
		} catch (IOException e) {
			System.out.println("Problem in closing buffer reader. " + e.getMessage());
		}
	}
}
