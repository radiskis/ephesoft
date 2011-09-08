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

package com.ephesoft.dcma.imagemagick;

import java.io.File;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.AbstractRunnable;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.ProcessExecutor;

public class MultiPageExecutor {

	String[] pages;
	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(MultiPageExecutor.class);

	public MultiPageExecutor(String[] cmds, File file, BatchInstanceThread batchInstanceThread, String[] pages) {
		this.pages = new String[pages.length];
		this.pages = pages.clone();
		batchInstanceThread.add(new ProcessExecutor(cmds, file));
	}

	public MultiPageExecutor(BatchInstanceThread batchInstanceThread, final String[] pages11) {
		this.pages = new String[pages11.length];
		this.pages = pages11.clone();
		batchInstanceThread.add(new AbstractRunnable() {

			@Override
			public void run() {
				IMOperation op = new IMOperation();
				op.addImage(pages.length - 1);
				op.adjoin();
				op.addImage();
				ConvertCmd convert = new ConvertCmd();
				try {
					convert.run(op, (Object[]) pages);
				} catch (Exception e) {
					logger.error("Error occured while running command for multipage tiff creation.");
					setDcmaApplicationException(new DCMAApplicationException(
							"Error occured while running command for multipage tiff creation."));
				}
			}
		});
	}

	public MultiPageExecutor(BatchInstanceThread batchInstanceThread, final String[] pages11, final String pdfCompression,
			final String pdfQuality, final String coloredImage) {
		this.pages = new String[pages11.length];
		this.pages = pages11.clone();
		batchInstanceThread.add(new AbstractRunnable() {

			@Override
			public void run() {
				IMOperation op = new IMOperation();
				op.addImage(pages.length - 1);
				op.quality(new Double(pdfQuality));
				op.type("optimize");
				if (coloredImage != null && coloredImage.equalsIgnoreCase("false")) {
					op.monochrome();
				}
				op.compress(pdfCompression);
				op.addImage();
				ConvertCmd convert = new ConvertCmd();
				try {
					convert.run(op, (Object[]) pages);
				} catch (Exception e) {
					logger.error("Error occured while running command for multipage tiff creation.");
					setDcmaApplicationException(new DCMAApplicationException(
							"Error occured while running command for multipage tiff creation."));
				}
			}
		});
	}

	public String[] getPages() {
		return pages;
	}
}
