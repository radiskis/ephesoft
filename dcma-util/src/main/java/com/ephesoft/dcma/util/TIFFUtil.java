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

package com.ephesoft.dcma.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;
import javax.media.jai.PlanarImage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFEncodeParam;

/**
 * This is util class for TIFF.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.itextpdf.text.pdf.codec.TiffImage
 */
public class TIFFUtil {

	/**
	 * log reference.
	 */
	private static final Log LOG = LogFactory.getLog(TIFFUtil.class);

	/**
	 * API for getting the number of pages in a tiff file.
	 * 
	 * @param filePath file path {@link String}
	 * @return numberOfPage
	 */
	public static int getTIFFPageCount(String filePath) {
		LOG.info("Counting number of pages in a tiff file = " + filePath);
		int numberOfPages = 0;
		RandomAccessFileOrArray randomAccessFile = null;
		String filePathLowerCase = filePath.toLowerCase(Locale.getDefault());
		if (!filePathLowerCase.endsWith(IUtilCommonConstants.EXTENSION_TIFF)
				&& !filePathLowerCase.endsWith(IUtilCommonConstants.EXTENSION_TIF)) {
			LOG.info("File not a tiff file." + filePath);
		} else {
			try {
				randomAccessFile = new RandomAccessFileOrArray(filePath);
				numberOfPages = TiffImage.getNumberOfPages(randomAccessFile);
				LOG.info("Number of pages found = " + numberOfPages);
			} catch (IOException ioe) {
				LOG.error("IIO exception while reading the tiff file = " + filePath);
			} finally {
				if (null != randomAccessFile) {
					try {
						randomAccessFile.close();
					} catch (IOException ioe) {
						LOG.error("Error while closing the RandomAccessFileOrArray for file  = " + filePath);
					}
				}
			}
		}
		return numberOfPages;

	}
	
	/**
	 * The <code>getSelectedTiffFile</code> method is used to limit the file
	 * to the page limit given.
	 * 
	 * @param tiffFile {@link File} tiff file from which limit has to be applied
	 * @param pageLimit int
	 * @throws IOException if file is not found
	 */
	public static void getSelectedTiffFile(final File tiffFile, final int pageLimit) throws IOException {
		OutputStream out = null;
		File newTiffFile = null;
		if (null != tiffFile && getTIFFPageCount(tiffFile.getAbsolutePath()) > pageLimit) {
			try {
				final List<BufferedImage> imageList = new ArrayList<BufferedImage>();
				final SeekableStream seekableStream = new FileSeekableStream(tiffFile);
				final ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", seekableStream, null);
				for (int i = 1; i < pageLimit; i++) {
					final PlanarImage planarImage = new NullOpImage(decoder.decodeAsRenderedImage(i), null, null, OpImage.OP_IO_BOUND);
					imageList.add(planarImage.getAsBufferedImage());
				}
				seekableStream.close();
				final TIFFEncodeParam params = new TIFFEncodeParam();
				params.setCompression(TIFFEncodeParam.COMPRESSION_GROUP4);
				String name = tiffFile.getName();
				final int indexOf = name.lastIndexOf(IUtilCommonConstants.DOT);
				name = name.substring(0, indexOf);
				final String finalPath = tiffFile.getParent() + File.separator + name
						+ System.currentTimeMillis() + IUtilCommonConstants.EXTENSION_TIF;
				newTiffFile = new File(finalPath);
				out = new FileOutputStream(finalPath); 
				final ImageEncoder encoder = ImageCodec.createImageEncoder("tiff", out, params);
	            params.setExtraImages(imageList.iterator()); 
	            encoder.encode(imageList.get(0));
			} finally {
				if (null != out) {
					out.flush();
					out.close();
				}
			}
            if (tiffFile.delete() && null != newTiffFile) {
            	newTiffFile.renameTo(tiffFile);
			} else {
				if (null != newTiffFile) {
					newTiffFile.delete();
				}
			}
		}
	}
}
