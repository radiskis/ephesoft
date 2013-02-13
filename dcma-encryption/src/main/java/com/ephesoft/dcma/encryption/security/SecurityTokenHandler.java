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

package com.ephesoft.dcma.encryption.security;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.encryption.constants.EncryptionConstants;
import com.ephesoft.dcma.encryption.core.EncryptorDecryptor;
import com.ephesoft.dcma.encryption.exception.CryptographyException;

/**
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.encryption.core.EncryptorDecryptor
 */
public class SecurityTokenHandler {

	/**
	 * A token list of type String.
	 */
	private static List<String> tokenList = new ArrayList<String>(0);
	/**
	 * An instance of Random.
	 */
	private static Random rand = new Random();
	/**
	 * An instance of Logger for logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityTokenHandler.class);

	/**
	 * This method is used for generating the tokens according to the scheduled time.
	 * @return {@link String}
	 */
	public static String generateToken() {
		final String generatedToken = getEncryptedAndDecodedString(String.valueOf(rand.nextLong()));
		synchronized (tokenList) {
			tokenList.add(generatedToken);
			TimerTask timerTask = new TimerTask() {

				@Override
				public void run() {
					tokenList.remove(generatedToken);
				}
			};
			Timer timer = new Timer();
			timer.schedule(timerTask, EncryptionConstants.MILLISECONDS_IN_ONE_HOUR);
		}
		return generatedToken;
	}

	private static String getEncryptedAndDecodedString(String toEncryptString) {
		String encryptString = null;
		try {
			encryptString = EncryptorDecryptor.getEncryptorDecryptor().encryptString(toEncryptString);
		} catch (CryptographyException e) {
			LOGGER.error("Exception in encrypting string:", e);
		}
		return getEncodedString(encryptString);
	}

	private static void removeToken(String encryptedToken) {
		tokenList.remove(encryptedToken);
	}

	/**
	 * This method is used to authorize the token and check if it is valid or not.
	 * @param encryptedToken {@link String}
	 * @return boolean
	 */
	public static boolean isTokenAuthorized(String encryptedToken) {
		boolean isTokenAuthorized = false;
		if (tokenList.contains(encryptedToken)) {
			removeToken(encryptedToken);
			isTokenAuthorized = true;
		}
		return isTokenAuthorized;
	}

	/**
	 * This method is used to convert the given string into encoded string.
	 * @param toEncodeString {@link String}
	 * @return {@link String}
	 */
	public static String getEncodedString(String toEncodeString) {
		String encodedString = null;
		try {
			encodedString = URLEncoder.encode(toEncodeString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Unsupported encoding :", e);
		}
		return encodedString;
	}

	/**
	 * This method is used to convert the given string into decoded string.
	 * @param toDecodeString {@link String}
	 * @return {@link String}
	 */
	public static String getDecodedString(String toDecodeString) {
		String decodedString = null;
		try {
			decodedString = URLDecoder.decode(toDecodeString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Unsupported encoding :", e);
		}
		return decodedString;
	}
}
