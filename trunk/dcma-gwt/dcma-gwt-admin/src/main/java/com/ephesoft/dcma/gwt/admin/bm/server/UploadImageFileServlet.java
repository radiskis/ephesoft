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

package com.ephesoft.dcma.gwt.admin.bm.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.core.server.DCMAHttpServlet;
import com.ephesoft.dcma.imagemagick.service.ImageProcessService;

/**
 * This is class for uploading Image File Servlet.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.server.DCMAHttpServlet
 */
public class UploadImageFileServlet extends DCMAHttpServlet {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Overridden doGet method.
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws IOException
	 */
	@Override
	public final void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	/**
	 * Overridden doPost method.
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws IOException
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String batchClassId = null;
		String docName = null;
		String fileName = null;
		String isAdvancedTableInfo = null;
		InputStream instream = null;
		OutputStream out = null;
		PrintWriter printWriter = resp.getWriter();
		if (ServletFileUpload.isMultipartContent(req)) {
			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items;
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			String uploadPath = null;
			try {
				items = upload.parseRequest(req);
				for (FileItem item : items) {

					// process only file upload - discard other form item types
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("batchClassID")) {
							batchClassId = item.getString();
						} else if (item.getFieldName().equalsIgnoreCase("docName")) {
							docName = item.getString();
						} else if (item.getFieldName().equalsIgnoreCase("isAdvancedTableInfo")) {
							isAdvancedTableInfo = item.getString();
						}
					} else if (!item.isFormField() && "importFile".equals(item.getFieldName())) {
						fileName = item.getName();
						if (fileName != null) {
							fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
						}
						instream = item.getInputStream();
					}
				}
				if (batchClassId == null || docName == null) {
					LOG.error("Error while loading image... Either batchClassId or doc type is null. Batch Class Id :: "
							+ batchClassId + " Doc Type :: " + docName);
					printWriter.write("Error while loading image... Either batchClassId or doc type is null.");
				} else {
					StringBuilder uploadPathString = uploadPath(batchClassId, docName, isAdvancedTableInfo, batchSchemaService);
					File uploadFolder = new File(uploadPathString.toString());
					if (!uploadFolder.exists()) {
						try {
							boolean tempPath = uploadFolder.mkdirs();
							if (!tempPath) {
								LOG
										.error("Unable to create the folders in the temp directory specified. Change the path and permissions in dcma-batch.properties");
								printWriter
										.write("Unable to create the folders in the temp directory specified. Change the path and permissions in dcma-batch.properties");
								return;
							}
						} catch (Exception e) {
							LOG.error("Unable to create the folders in the temp directory.", e);
							printWriter.write("Unable to create the folders in the temp directory." + e.getMessage());
							return;
						}
					}
					uploadPathString.append(File.separator);
					uploadPathString.append(fileName);
					uploadPath = uploadPathString.toString();
					out = new FileOutputStream(uploadPath);
					byte buf[] = new byte[BatchClassManagementConstants.BUFFER_SIZE];
					int len = instream.read(buf);
					while ((len) > 0) {
						out.write(buf, 0, len);
						len = instream.read(buf);
					}
					// convert tiff to png
					ImageProcessService imageProcessService = this.getSingleBeanOfType(ImageProcessService.class);
					imageProcessService.generatePNGForImage(new File(uploadPath));
					LOG.info("Png file created successfully for file: " + uploadPath);
				}
			} catch (FileUploadException e) {
				LOG.error("Unable to read the form contents." + e, e);
				printWriter.write("Unable to read the form contents.Please try again.");
			} catch (DCMAException e) {
				LOG.error("Unable to generate PNG." + e, e);
				printWriter.write("Unable to generate PNG.Please try again.");
			} finally {
				if (out != null) {
					out.close();
				}
				if (instream != null) {
					instream.close();
				}
			}
			printWriter.write("file_seperator:" + File.separator);
			printWriter.write("|");
		}
	}

	private StringBuilder uploadPath(String batchClassId, String docName, String isAdvancedTableInfo,
			BatchSchemaService batchSchemaService) {
		String batchClassIdTemp = batchClassId;
		String docNameTemp = docName;
		String uploadPath;
		batchClassIdTemp = batchClassIdTemp.trim();
		docNameTemp = docNameTemp.trim();
		if (isAdvancedTableInfo != null && isAdvancedTableInfo.equalsIgnoreCase("true")) {
			uploadPath = batchSchemaService.getAdvancedTestTableFolderPath(batchClassIdTemp, true);
		} else {
			uploadPath = batchSchemaService.getTestAdvancedKvExtractionFolderPath(batchClassIdTemp, true);
		}
		StringBuilder uploadPathString = new StringBuilder();
		uploadPathString.append(uploadPath);
		uploadPathString.append(File.separator);
		uploadPathString.append(docNameTemp);
		uploadPathString.append(File.separator);
		return uploadPathString;
	}
}
