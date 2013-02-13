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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.encryption.constants.EncryptionConstants;
import com.ephesoft.dcma.encryption.exception.CryptographyException;

/**
 * This class is used to decrypt passwords.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.encryption.core.PasswordEncryptor
 */
public class PasswordDecryptor {

	/**
	 * A string to store encrypted string.
	 */
	private String encryptedString;

	/**
	 * An instance of Logger for proper logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordDecryptor.class);

	/**
	 * Constructor.
	 */
	public PasswordDecryptor() {
		super();
	}

	/**
	 * Parameterized constructor.
	 * 
	 * @param encryptedString {@link String}
	 */
	public PasswordDecryptor(String encryptedString) {
		this.encryptedString = encryptedString;
	}

	/**
	 * This method is used to decrypt.
	 * 
	 * @param encryptedString {@link String}
	 * @param checkPropertyFileValue {@link boolean}
	 * @return {@link String}
	 * @throws CryptographyException
	 */
	public String decrypt(final String encryptedString, boolean checkPropertyFileValue) throws CryptographyException {
		String decryptedString = "";
		String usingEncryption = "false";
		if (!(encryptedString == null || encryptedString.length() == 0)) {
			if (checkPropertyFileValue) {
				InputStream propertyInStream = null;
				String filePath = EncryptionConstants.META_INF + File.separator + EncryptionConstants.FOLDER_NAME + File.separator
						+ EncryptionConstants.FILE_NAME + ".properties";
				try {

					propertyInStream = new ClassPathResource(filePath).getInputStream();
					Properties properties = new Properties();
					properties.load(propertyInStream);
					usingEncryption = (String) properties.get(EncryptionConstants.USE_ENCRYPTION);
				} catch (Exception exception) {
					LOGGER.error("Could not find properties files. Not using encryption");
				} finally {
					try {
						if (propertyInStream != null) {
							propertyInStream.close();
						}
					} catch (IOException ioe) {
						LOGGER.info("Could not close property input stream in password decryptor.");
					}
				}

				if (usingEncryption.equalsIgnoreCase(Boolean.TRUE.toString())) {
					decryptedString = getDecyrptedString(encryptedString,false);
				} else {
					decryptedString = encryptedString;
				}
			} else {
				decryptedString = getDecyrptedString(encryptedString,true);
			}
		}
		return decryptedString;
	}

	/**
	 * API to get decrypted string and throw exception on the basis of throwException if decryption is unsuccessful.
	 * 
	 * @param encryptedStringValue {@link String}
	 * @param throwException {@link boolean}
	 * @return decryptedString {@link String}
	 * @throws CryptographyException {@link CryptographyException}
	 */
	private String getDecyrptedString(final String encryptedString, final boolean throwException) throws CryptographyException {
		LOGGER.info("Executing getDecyrptedString method in PasswordDecryptor. Encrypted string is " + encryptedString);
		String decryptedString = null;
		try {
			decryptedString = EncryptorDecryptor.getEncryptorDecryptor().decryptString(encryptedString);
			LOGGER.info("Decryption is performed successfully. Decrypted string is " + decryptedString);
		} catch (CryptographyException exception) {
			LOGGER.info("Exception occured while executing getDecyrptedString method.");
			if (throwException) {
				LOGGER.error("Unable to perform decryption operation on encrypted string. Encrypted string is " + encryptedString);
				throw new CryptographyException(exception.getMessage(), exception);
			} else {
				LOGGER.info("Assigning random value to decrypted string.");
				Random random = new Random();
				decryptedString = random.nextInt(EncryptionConstants.RANDOM_VALUE) + "";
			}
		}
		return decryptedString;

	}

	/**
	 * getter for encryptedString.
	 * 
	 * @return {@link String}
	 */
	public String getEncryptedString() {
		return encryptedString;
	}

	/**
	 * setter for encryptedString.
	 * 
	 * @param encryptedString {@link String}
	 */
	public void setEncryptedString(String encryptedString) {
		this.encryptedString = encryptedString;
	}

	/**
	 * getter for decryptedString.
	 * 
	 * @return {@link String}
	 * @throws CryptographyException {@link CryptographyException}
	 */
	public String getDecryptedString() throws CryptographyException {
		return decrypt(encryptedString, true);
	}

	/**
	 * @param checkPropertyFileValue {@link boolean}
	 * @return the decryptedString
	 * @throws CryptographyException {@link CryptographyException}
	 */
	public String getDecryptedString(boolean checkPropertyFileValue) throws CryptographyException {
		return decrypt(encryptedString, checkPropertyFileValue);
	}

}
