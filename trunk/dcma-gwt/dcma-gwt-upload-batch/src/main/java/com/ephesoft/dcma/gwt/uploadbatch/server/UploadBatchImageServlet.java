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

package com.ephesoft.dcma.gwt.uploadbatch.server;

import java.io.File;
import java.io.FileNotFoundException;
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
import com.ephesoft.dcma.gwt.core.server.DCMAHttpServlet;

public class UploadBatchImageServlet extends DCMAHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7306215716570055774L;

	@Override
	public final void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String currentBatchUploadFolderName = req.getParameter("currentBatchUploadFolderName");
		String uploadBatchFolderPath = batchSchemaService.getUploadBatchFolder();
		File currentBatchUploadFolder = new File(uploadBatchFolderPath + File.separator + currentBatchUploadFolderName);
		if (!currentBatchUploadFolder.exists()) {
			currentBatchUploadFolder.mkdirs();
		}
		uploadFile(req, resp, batchSchemaService, currentBatchUploadFolderName);
	}

	private void uploadFile(HttpServletRequest req, HttpServletResponse resp, BatchSchemaService batchSchemaService,
			String currentBatchUploadFolderName) throws IOException {
		PrintWriter printWriter = resp.getWriter();
		File tempFile = null;
		InputStream instream = null;
		OutputStream out = null;
		String uploadBatchFolderPath = batchSchemaService.getUploadBatchFolder();
		String uploadFileName = "";
		if (ServletFileUpload.isMultipartContent(req)) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			uploadFileName = "";
			String uploadFilePath = "";
			List<FileItem> items;
			try {
				items = upload.parseRequest(req);

				for (FileItem item : items) {
					if (!item.isFormField()) { // && "uploadFile".equals(item.getFieldName())) {
						uploadFileName = item.getName();
						if (uploadFileName != null) {
							uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf(File.separator) + 1);
						}
						uploadFilePath = uploadBatchFolderPath + File.separator + currentBatchUploadFolderName + File.separator
								+ uploadFileName;

						try {
							instream = item.getInputStream();
							tempFile = new File(uploadFilePath);

							out = new FileOutputStream(tempFile);
							byte buf[] = new byte[1024];
							int len;
							while ((len = instream.read(buf)) > 0) {
								out.write(buf, 0, len);
							}
						} catch (FileNotFoundException e) {
							log.error("Unable to create the upload folder." + e, e);
							printWriter.write("Unable to create the upload folder.Please try again.");

						} catch (IOException e) {
							log.error("Unable to read the file." + e, e);
							printWriter.write("Unable to read the file.Please try again.");
						} finally {
							if (out != null) {
								out.close();
							}
							if (instream != null) {
								instream.close();
							}
						}
					}
				}
			} catch (FileUploadException e) {
				log.error("Unable to read the form contents." + e, e);
				printWriter.write("Unable to read the form contents.Please try again.");
			}

		} else {
			log.error("Request contents type is not supported.");
			printWriter.write("Request contents type is not supported.");
		}
		printWriter.write("currentBatchUploadFolderName:" + currentBatchUploadFolderName);
		printWriter.append("|");
	
		printWriter.append("fileName:").append(uploadFileName);
		printWriter.append("|");

		printWriter.flush();
	}
}
