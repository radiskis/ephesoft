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

package com.ephesoft.dcma.gwt.foldermanager.server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.ephesoft.dcma.gwt.core.server.DCMAHttpServlet;
import com.ephesoft.dcma.gwt.foldermanager.shared.constant.FolderManagementConstants;

public class UploadDownloadFilesServlet extends DCMAHttpServlet {

	private static final String EXCEPTION_OCCURED_WHILE_DOWNLOADING_A_FILE_FROM_THE_FILE_PATH = "Exception occured while downloading a file from the file path: ";
	private static final String UNABLE_TO_CLOSE_INPUT_STREAM_FOR_FILE_DOWNLOAD = "Unable to close input stream for file download.";
	private static final String DOWNLOADING_FILE_FROM_PATH = "Downloading file from path: ";
	private static final String DOWNLOAD_COMPLETED_FOR_FILEPATH = "Download completed for filepath:";
	private static final String UNABLE_TO_CREATE_THE_UPLOAD_FOLDER_PLEASE_TRY_AGAIN = "Unable to create the upload folder.Please try again.";
	private static final String UNABLE_TO_READ_THE_FILE_PLEASE_TRY_AGAIN = "Unable to read the file.Please try again.";
	private static final String REQUEST_CONTENTS_TYPE_IS_NOT_SUPPORTED = "Request contents type is not supported.";
	private static final String THE_NAME_OF_FILE_BEING_UPLOADED_IS = "The name of file being uploaded is:";
	private static final String THE_CURRENT_UPLOAD_FOLDER_NAME_IS = "The current upload folder name is:";
	private static final String UNABLE_TO_READ_THE_FORM_CONTENTS_PLEASE_TRY_AGAIN = "Unable to read the form contents.Please try again.";
	private static final String EMPTY_STRING = "";
	private static final String CLOSING_QUOTES = "\"";
	private static final String ATTACHMENT_FILENAME = "attachment; filename=\"";
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static final String UNABLE_TO_CLOSE_OUTPUT_STREAM_FOR_DOWNLOAD = "Unable to close output stream for download.";
	private static final String UNABLE_TO_FLUSH_OUTPUT_STREAM_FOR_DOWNLOAD = "Unable to flush output stream for download.";
	private static final long serialVersionUID = -7306215716570055774L;

	@Override
	public final void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String currentUploadFolderName = req.getParameter(FolderManagementConstants.CURRENT_UPLOAD_FOLDER_NAME);
		if (null != currentUploadFolderName) {
			uploadFile(req, currentUploadFolderName);
		} else {

			String currentFileDownloadPath = req.getParameter(FolderManagementConstants.CURRENT_FILE_DOWNLOAD_PATH);
			if (null != currentFileDownloadPath) {
				downloadFile(resp, currentFileDownloadPath);
			}
		}
	}

	private void downloadFile(HttpServletResponse response, String currentFileDownloadPath) {
		LOG.info(DOWNLOADING_FILE_FROM_PATH + currentFileDownloadPath);
		DataInputStream inputStream = null;
		ServletOutputStream outStream = null;
		try {
			outStream = response.getOutputStream();
			File file = new File(currentFileDownloadPath);
			int length = 0;
			String mimetype = APPLICATION_OCTET_STREAM;
			response.setContentType(mimetype);
			response.setContentLength((int) file.length());
			String fileName = file.getName();
			response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fileName + CLOSING_QUOTES);
			byte[] byteBuffer = new byte[1024];
			inputStream = new DataInputStream(new FileInputStream(file));
			length = inputStream.read(byteBuffer);
			while ((inputStream != null) && (length != -1)) {
				outStream.write(byteBuffer, 0, length);
				length = inputStream.read(byteBuffer);
			}
			LOG.info(DOWNLOAD_COMPLETED_FOR_FILEPATH + currentFileDownloadPath);
		} catch (IOException e) {
			LOG.error(EXCEPTION_OCCURED_WHILE_DOWNLOADING_A_FILE_FROM_THE_FILE_PATH + currentFileDownloadPath);
			LOG.error(e.getMessage());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LOG.error(UNABLE_TO_CLOSE_INPUT_STREAM_FOR_FILE_DOWNLOAD);
				}
			}
			if (outStream != null) {
				try {
					outStream.flush();
				} catch (IOException e) {
					LOG.error(UNABLE_TO_FLUSH_OUTPUT_STREAM_FOR_DOWNLOAD);
				}
				try {
					outStream.close();
				} catch (IOException e) {
					LOG.error(UNABLE_TO_CLOSE_OUTPUT_STREAM_FOR_DOWNLOAD);
				}

			}
		}
	}

	private void uploadFile(HttpServletRequest req, String currentBatchUploadFolderName) throws IOException {

		File tempFile = null;
		InputStream instream = null;
		OutputStream out = null;
		String uploadFileName = EMPTY_STRING;
		if (ServletFileUpload.isMultipartContent(req)) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			uploadFileName = EMPTY_STRING;
			String uploadFilePath = EMPTY_STRING;
			List<FileItem> items;
			try {
				items = upload.parseRequest(req);
				for (FileItem item : items) {
					if (!item.isFormField()) {
						uploadFileName = item.getName();
						if (uploadFileName != null) {
							uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf(File.separator) + 1);
						}
						uploadFilePath = currentBatchUploadFolderName + File.separator + uploadFileName;

						try {
							instream = item.getInputStream();
							tempFile = new File(uploadFilePath);

							out = new FileOutputStream(tempFile);
							byte buf[] = new byte[1024];
							int len = instream.read(buf);
							while (len > 0) {
								out.write(buf, 0, len);
								len = instream.read(buf);
							}
						} catch (FileNotFoundException e) {
							LOG.error(UNABLE_TO_CREATE_THE_UPLOAD_FOLDER_PLEASE_TRY_AGAIN);

						} catch (IOException e) {
							LOG.error(UNABLE_TO_READ_THE_FILE_PLEASE_TRY_AGAIN);
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
				LOG.error(UNABLE_TO_READ_THE_FORM_CONTENTS_PLEASE_TRY_AGAIN);
			}
			LOG.info(THE_CURRENT_UPLOAD_FOLDER_NAME_IS + currentBatchUploadFolderName);
			LOG.info(THE_NAME_OF_FILE_BEING_UPLOADED_IS + uploadFileName);
		} else {
			LOG.error(REQUEST_CONTENTS_TYPE_IS_NOT_SUPPORTED);
		}

	}
}
