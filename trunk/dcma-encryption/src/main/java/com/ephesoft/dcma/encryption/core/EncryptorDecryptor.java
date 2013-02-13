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

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.encryption.constants.EncryptionConstants;
import com.ephesoft.dcma.encryption.exception.CryptographyException;

/**
 * This class is used to encrypt and decrypt.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.encryption.exception.CryptographyException
 */
public final class EncryptorDecryptor {

	/**
	 * An instance of {@link EncryptorDecryptor}.
	 */
	private static EncryptorDecryptor encryptor = new EncryptorDecryptor();

	/**
	 * An instance of Logger for logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EncryptorDecryptor.class);

	private EncryptorDecryptor() {
		super();
	}

	/**
	 * getter for encryptorDecryptor.
	 * @return {@link EncryptorDecryptor}
	 */
	public static EncryptorDecryptor getEncryptorDecryptor() {
		return encryptor;
	}

	/**
	 * This method is used to start the encryption process.
	 * 
	 * @param data byte[]
	 * @param salt byte[]
	 * @param isEncryption boolean
	 * @return byte[]
	 * @throws CryptographyException {@link CryptographyException}
	 */
	public byte[] startCrypting(byte[] data, byte[] salt, boolean isEncryption) throws CryptographyException {
		KeySpec keySpec = new PBEKeySpec(EncryptionConstants.KEY.toCharArray(), salt, EncryptionConstants.ITERATION_COUNT);
		SecretKey key;
		byte[] finalBytes = null;
		try {
			key = SecretKeyFactory.getInstance(EncryptionConstants.ALGORITHM).generateSecret(keySpec);
			Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, EncryptionConstants.ITERATION_COUNT);
			if (isEncryption) {
				ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			} else {
				ecipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			}
			finalBytes = ecipher.doFinal(data);
		} catch (InvalidKeySpecException e) {
			if (isEncryption) {
				LOGGER.error("Encryption : Key used is invalid", e);
				throw new CryptographyException("Key used is invalid", e);
			} else {
				LOGGER.error("Decryption : Key used is invalid", e);
				throw new CryptographyException("Key used is invalid", e);
			}
		} catch (NoSuchAlgorithmException e) {
			if (isEncryption) {
				LOGGER.error("Encryption : Algorithm used does not exist", e);
				throw new CryptographyException("Algorithm used does not exist", e);
			} else {
				LOGGER.error("Decryption : Algorithm used does not exist", e);
				throw new CryptographyException("Algorithm used does not exist", e);
			}
		} catch (NoSuchPaddingException e) {
			if (isEncryption) {
				LOGGER.error("Encryption : Padding used does not exist", e);
				throw new CryptographyException("Padding used does not exist", e);
			} else {
				LOGGER.error("Decryption : Padding used does not exist", e);
				throw new CryptographyException("Padding used does not exist", e);
			}
		} catch (InvalidKeyException e) {
			if (isEncryption) {
				LOGGER.error("Encryption : Key generated is invalid", e);
				throw new CryptographyException("Key generated is invalid", e);
			} else {
				LOGGER.error("Decryption : Key generated is invalid", e);
				throw new CryptographyException("Key generated is invalid", e);
			}
		} catch (InvalidAlgorithmParameterException e) {
			if (isEncryption) {
				LOGGER.error("Encryption : Algorithm parameter is invalid", e);
				throw new CryptographyException("Algorithm parameter is invalid", e);
			} else {
				LOGGER.error("Decryption : Algorithm parameter is invalid", e);
				throw new CryptographyException("Algorithm parameter is invalid", e);
			}
		} catch (IllegalBlockSizeException e) {
			if (isEncryption) {
				LOGGER.error("Encryption : Block size is illegal", e);
				throw new CryptographyException("Block size is illegal", e);
			} else {
				LOGGER.error("Decryption : Block size is illegal", e);
				throw new CryptographyException("Block size is illegal", e);
			}
		} catch (BadPaddingException e) {
			if (isEncryption) {
				LOGGER.error("Encryption : Padding done is invalid", e);
				throw new CryptographyException("Padding done is invalid", e);
			} else {
				LOGGER.error("Decryption : Padding done is invalid", e);
				throw new CryptographyException("Padding done is invalid", e);
			}
		}
		return finalBytes;
	}

	/**
	 * This method encrypts the string provided.
	 * @param decryptedString {@link String}
	 * @return {@link String}
	 * @throws CryptographyException {@link CryptographyException}
	 */
	public String encryptString(String decryptedString) throws CryptographyException {
		byte[] salt = generateSalt(EncryptionConstants.SALT_LENGTH);
		byte[] decryptedStringInBytes;
		try {
			decryptedStringInBytes = decryptedString.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Could not encode string using UTF-8", e);
			throw new CryptographyException("Could not encode string using UTF-8", e);
		}
		byte[] encryptedByte = startCrypting(decryptedStringInBytes, salt, true);
		byte[] result = ArrayUtils.addAll(salt, encryptedByte);
		result = Base64.encodeBase64(result);
		return new String(result);
	}

	private byte[] generateSalt(int saltLength) {
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		byte[] randomBytes = new byte[saltLength];
		random.nextBytes(randomBytes);
		return randomBytes;
	}

	/**
	 * This method is used to decrypt the encrypted string.
	 * @param encryptedString {@link String}
	 * @return {@link String}
	 * @throws CryptographyException {@link CryptographyException}
	 */
	public String decryptString(String encryptedString) throws CryptographyException {
		byte[] data;
		try {
			data = encryptedString.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Could not decode string using UTF-8", e);
			throw new CryptographyException("Could not decode string using UTF-8", e);
		}
		data = Base64.decodeBase64(data);
		byte[] salt = ArrayUtils.subarray(data, 0, EncryptionConstants.SALT_LENGTH);
		data = ArrayUtils.subarray(data, EncryptionConstants.SALT_LENGTH, data.length);
		byte[] result = startCrypting(data, salt, false);
		return new String(result);
	}

}
