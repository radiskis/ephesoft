/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2010-2011 Ephesoft Inc. 
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

package com.ephesoft.dcma.imagemagick.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.ProcessExecutor;
import com.ephesoft.dcma.imagemagick.IImageMagickCommonConstants;
import com.ephesoft.dcma.imagemagick.MultiPageExecutor;
import com.ephesoft.dcma.util.OSUtil;

/**
 * This class is used to create the pdf file using Image magick.
 * 
 * @author Ephesoft
 */
public class ImageMagicKPDFCreator implements IImageMagickCommonConstants {

	/**
	 * pdfCompression to be used for pdf compression.
	 */
	private transient String pdfCompression;

	/**
	 * PDF quality to be set while creating multipage-pdf.
	 */
	private transient String pdfQuality;

	/**
	 * colored/monochrome to be set while creating multipage-pdf.
	 */
	private transient String coloredImage;

	/**
	 * @param pdfCompression the pdfCompression to set
	 */
	public void setPdfCompression(String pdfCompression) {
		this.pdfCompression = pdfCompression;
	}

	/**
	 * @param pdfQuality the pdfQuality to set
	 */
	public void setPdfQuality(String pdfQuality) {
		this.pdfQuality = pdfQuality;
	}

	/**
	 * @param coloredImage the coloredImage to set
	 */
	public void setColoredImage(String coloredImage) {
		this.coloredImage = coloredImage;
	}

	/**
	 * @return the pdfCompression
	 */
	public String getPdfCompression() {
		return pdfCompression;
	}

	/**
	 * @return the pdfQuality
	 */
	public String getPdfQuality() {
		return pdfQuality;
	}

	/**
	 * @return the coloredImage
	 */
	public String getColoredImage() {
		return coloredImage;
	}

	/**
	 * API for creating pdf using image magicK.
	 * 
	 * @param pdfPages
	 * @param pdfBatchInstanceThread
	 * @param multiPageExecutorsPdf
	 * @param pdfOptimizationSwitch
	 */
	public void createPDFUsingImageMagick(String[] pdfPages, BatchInstanceThread pdfBatchInstanceThread,
			List<MultiPageExecutor> multiPageExecutorsPdf, String pdfOptimizationSwitch) {
		multiPageExecutorsPdf.add(new MultiPageExecutor(pdfBatchInstanceThread, pdfPages, getPdfCompression(), getPdfQuality(),
				getColoredImage(), pdfOptimizationSwitch));
	}
}
