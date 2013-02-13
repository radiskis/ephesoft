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

package com.ephesoft.dcma.imagemagick;

import java.io.File;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;

/**
 * This class is used for rotating the images.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.imagemagick.service.ImageProcessServiceImpl 
 */
public class ImageRotator {
	/**
	 * An instance of Logger for logging using slf4j. 
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(ImageRotator.class);

	/**
	 * This method is used to rotate the image present at specified path.
	 * @param imagePath {@link String}
	 * @throws DCMAApplicationException if any exception occurs during rotation of image.
	 */
	public void rotateImage(String imagePath) throws DCMAApplicationException {
		rotateImage(imagePath, ImageMagicKConstants.DEFAULT_DEGREE_OF_ROTATION);
	}
	/**
	 * This method is used to rotate the image present at the image path given with default degree of rotation.
	 * @param imagePath {@link String}
	 * @param degreeOfRotation double
	 * @throws DCMAApplicationException if any exception occurs during rotation of image.
	 */
	public void rotateImage(String imagePath, double degreeOfRotation) throws DCMAApplicationException {
		File fImagePath = new File(imagePath);
		if(!fImagePath.exists()){
			throw new DCMABusinessException("File does not exist filename="+fImagePath);
		}
		ConvertCmd convertcmd = new ConvertCmd();
		IMOperation operation = new IMOperation();
		operation.rotate(degreeOfRotation);
		operation.addImage();
		operation.addImage();
		Object[] listOfFiles = {imagePath,imagePath};
		try {
			convertcmd.run(operation, listOfFiles);
		} catch (Exception e) {
			LOGGER.error("Problem rotating image "+imagePath);
			throw new DCMAApplicationException("Unable to rotate Image", e);
		} 
		
		
	}

	
	

}
