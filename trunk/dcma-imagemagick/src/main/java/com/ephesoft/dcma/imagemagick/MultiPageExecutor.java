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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.common.HocrUtil;
import com.ephesoft.dcma.common.LineDataCarrier;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.AbstractRunnable;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.ProcessExecutor;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.util.OSUtil;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;

/**
 * This class is used for creating multipage tiff and pdf.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.imagemagick.service.ImageProcessServiceImpl 
 */
public class MultiPageExecutor implements ImageMagicKConstants {

	/**
	 * String array to store pages.
	 */
	private String[] pages;

	/**
	 * A string to store document id being processed.
	 */
	private String documentId;

	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiPageExecutor.class);

	public static String GS_PDF_EXECUTOR_PATH = "GS_PDF_EXECUTOR_PATH";

	/**
	 * Parameterized constructor.
	 * @param cmds {@link String []}
	 * @param file {@link File}
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param pages {@link String []} 
	 */
	public MultiPageExecutor(String[] cmds, File file, BatchInstanceThread batchInstanceThread, String[] pages) {
		this.pages = new String[pages.length];
		this.pages = pages.clone();
		batchInstanceThread.add(new ProcessExecutor(cmds, file));
	}

	/**
	 * This method creates multi page pdf using ghost script command.
	 * 
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param pages11 {@link String []}
	 * @param gsCmdParam {@link String}
	 * @param ghostScriptCommand {@link String}
	 * @param maxFilesProcessedPerLoop {@link Integer}
	 * @param documentIdInt {@link String}
	 */
	public MultiPageExecutor(BatchInstanceThread batchInstanceThread, final String[] pages11, final String gsCmdParam,
			final String ghostScriptCommand, final Integer maxFilesProcessedPerLoop, final String documentIdInt) {
		if (pages11 != null && pages11.length > 0) {
			this.pages = new String[pages11.length];
			this.pages = pages11.clone();
			this.documentId = documentIdInt;
			batchInstanceThread.add(new AbstractRunnable() {

				@Override
				public void run() {
					String ghostScriptPath = System.getenv(IImageMagickCommonConstants.GHOSTSCRIPT_ENV_VARIABLE);
					List<String> ghostScriptCommandList = new ArrayList<String>();
					File systemFileFolderPath = new File(pages[0]);
					String systemFolderPath = systemFileFolderPath.getParent();
					String outputFileName = "";
					String[] gsCmdParams = gsCmdParam.split(SPACE);
					int noOfPages = pages.length;
					int currPageNo = 0;
					int counter = 1;
					File fileToBeDeleted = null;
					String prevTempFilePath = null;
					String nextTempFilePath = null;
					String tempFilePath = pages[noOfPages - 1].substring(0, pages[noOfPages - 1].lastIndexOf('.') == -1
							? pages[noOfPages].length() : pages[noOfPages - 1].lastIndexOf('.'));
					boolean isLastPage = false;
					boolean isDBatchAddedToGSCmd = false;
					while (!isLastPage) {
						ghostScriptCommandList.clear();
						LOGGER.info("creating ghostscript command for multipage pdf creation.");
						int maxNoOfPages = counter * maxFilesProcessedPerLoop;
						for (String param : gsCmdParams) {
							if (!isDBatchAddedToGSCmd && param.trim().equalsIgnoreCase(GHOST_SCRIPT_COMMAND_OUTPUT_PARAMETERS)) {
								isDBatchAddedToGSCmd = true;
							}
							ghostScriptCommandList.add(param);
						}
						if (!isDBatchAddedToGSCmd) {
							ghostScriptCommandList.add(GHOST_SCRIPT_COMMAND_OUTPUT_PARAMETERS);
						}
						if (maxNoOfPages >= noOfPages - 1) {
							if (OSUtil.isWindows()) {
								outputFileName = pages[noOfPages - 1].substring(pages[noOfPages - 1].lastIndexOf(File.separator) + 1);
								ghostScriptCommandList.add(GHOST_SCRIPT_COMMAND_OUTPUT_FILE_PARAM + outputFileName);
							} else if (OSUtil.isUnix()) {
								ghostScriptCommandList.add(GHOST_SCRIPT_COMMAND_OUTPUT_FILE_PARAM + pages[noOfPages - 1]);
							}

							isLastPage = true;
						} else {
							nextTempFilePath = tempFilePath + '_' + '_' + counter + documentId + FileType.PDF.getExtensionWithDot();
							outputFileName = nextTempFilePath.substring(nextTempFilePath.lastIndexOf(File.separator) + 1);
							if (OSUtil.isWindows()) {
								ghostScriptCommandList.add(DOUBLE_QUOTES + GHOST_SCRIPT_COMMAND_OUTPUT_FILE_PARAM + outputFileName
										+ DOUBLE_QUOTES);
							} else if (OSUtil.isUnix()) {
								ghostScriptCommandList.add(GHOST_SCRIPT_COMMAND_OUTPUT_FILE_PARAM + nextTempFilePath);
							}
						}
						if (prevTempFilePath != null) {
							ghostScriptCommandList.add(prevTempFilePath);
							fileToBeDeleted = new File(prevTempFilePath);
						}
						prevTempFilePath = nextTempFilePath;
						counter++;
						for (; currPageNo < noOfPages - 1 && currPageNo < maxNoOfPages; currPageNo++) {
							if (OSUtil.isWindows()) {
								ghostScriptCommandList.add(DOUBLE_QUOTES
										+ pages[currPageNo].substring(pages[currPageNo].lastIndexOf(File.separator) + 1)
										+ DOUBLE_QUOTES);

							} else if (OSUtil.isUnix()) {
								ghostScriptCommandList.add(pages[currPageNo]);
							}
						}
						List<String> commandList = new ArrayList<String>();
						if (OSUtil.isWindows()) {
							String absoluteFilePath = systemFolderPath + File.separator + outputFileName;
							String absoluteGhostScriptParametersFilePath = absoluteFilePath.substring(0, absoluteFilePath
									.lastIndexOf('.'))
									+ FileType.SER.getExtensionWithDot();
							File ghostScriptCommandParametersFile = new File(absoluteGhostScriptParametersFilePath);
							writeGhosScriptParametersToFile(ghostScriptCommandParametersFile, ghostScriptCommandList);
							makeCommandForWindows(commandList, ghostScriptPath, systemFolderPath,
									absoluteGhostScriptParametersFilePath);
						} else if (OSUtil.isUnix()) {
							commandList.add(ghostScriptPath + File.separator + ghostScriptCommand);
							commandList.addAll(ghostScriptCommandList);
						}

						LOGGER.info("Command for multi page pdf creation : " + commandList);
						String[] cmds = (String[]) commandList.toArray(new String[commandList.size()]);
						executeCreatePdfCommand(fileToBeDeleted, cmds);
					}
					if (fileToBeDeleted != null && fileToBeDeleted.exists()) {
						LOGGER.info("Deleting temporary file : " + fileToBeDeleted.getAbsolutePath());
						fileToBeDeleted.delete();
					}
				}

				private void executeCreatePdfCommand(File fileToBeDeleted, String[] cmds) {
					try {
						Process process = Runtime.getRuntime().exec(cmds);
						InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
						BufferedReader input = new BufferedReader(inputStreamReader);
						String line = null;
						do {
							line = input.readLine();
							LOGGER.info(line);
						} while (line != null);
						int exitValue = process.exitValue();
						if (exitValue != 0) {
							LOGGER.error("Process exited with an invalid exit value : " + exitValue);
							setDcmaApplicationException(new DCMAApplicationException(MULTIPAGE_PDF_CREATION_ERROR_MSG));
						}
						if (fileToBeDeleted != null && fileToBeDeleted.exists()) {
							LOGGER.info("Deleting temporary file : " + fileToBeDeleted.getAbsolutePath());
							fileToBeDeleted.delete();
						}
					} catch (IOException e) {
						LOGGER.error(MULTIPAGE_PDF_CREATION_ERROR_MSG + e.getMessage(), e);
						setDcmaApplicationException(new DCMAApplicationException(MULTIPAGE_PDF_CREATION_ERROR_MSG + e.getMessage(), e));
					} catch (SecurityException se) {
						LOGGER.error("Cannot delete the temporary file : " + fileToBeDeleted.getAbsolutePath() + se.getMessage(), se);
					}
				}

				private void writeGhosScriptParametersToFile(File file, List<String> ghostScriptCommandList) {
					LOGGER.info("Writing ghostscript parameters to :" + file.getAbsolutePath());
					StringBuffer ghostScriptParametersBuffer = new StringBuffer();
					for (String command : ghostScriptCommandList) {
						ghostScriptParametersBuffer.append(command);
						ghostScriptParametersBuffer.append(SPACE);
					}
					try {
						FileUtils.writeStringToFile(file, ghostScriptParametersBuffer.toString());
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
					}
				}

				private void makeCommandForWindows(List<String> commandList, String ghostScriptPath, String systemFolderPath,
						String ghostScriptParametersFileAbsolutePath) {
					LOGGER.info("Forming Command for Ghostscript Executor(Windows).");
					commandList.add(System.getenv(GS_PDF_EXECUTOR_PATH));
					commandList.add(DOUBLE_QUOTES + ghostScriptPath + File.separator + ghostScriptCommand + DOUBLE_QUOTES);
					commandList.add(DOUBLE_QUOTES + systemFolderPath + DOUBLE_QUOTES);
					commandList.add(DOUBLE_QUOTES + ghostScriptParametersFileAbsolutePath + DOUBLE_QUOTES);
				}
			});
		}
	}

	/**
	 * This method creates multi-page tiff file using imagemagick.
	 * 
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param tifPages {@link String[]}
	 * @param tifCompression {@link String}
	 * @param maxFilesProcessedPerLoop {@link String}
	 * @param documentIdInt {@link String}
	 */
	public MultiPageExecutor(final BatchInstanceThread batchInstanceThread, final String[] tifPages, final String tifCompression,
			final int maxFilesProcessedPerLoop, final String documentIdInt) {
		if (tifPages != null && tifPages.length > 0) {
			this.pages = new String[tifPages.length];
			this.pages = tifPages.clone();
			this.documentId = documentIdInt;
			batchInstanceThread.add(new AbstractRunnable() {

				@Override
				public void run() {
										LOGGER.info("Adding command for multipage tiff creation using imagemagick.");
					int noOfPages = pages.length;
					String outputFileName = pages[noOfPages - 1];
					LOGGER.info("Total number of input pages : " + (noOfPages - 1));
					int indexOf = outputFileName.lastIndexOf(File.separator);
					indexOf = indexOf == -1 ? outputFileName.length() : indexOf;
					String tempFilePath = outputFileName.substring(0, indexOf);
					List<File> filesToBeDeleted = new ArrayList<File>();
					List<String> pageList = new ArrayList<String>();
					String nextFileName = null;
					for (int index = 0, counter = 1; index < noOfPages; index++) {
						if (index < counter * maxFilesProcessedPerLoop && index < noOfPages - 1) {
							LOGGER.info("Adding input file name : " + pages[index]);
							pageList.add(pages[index]);
						} else {
							if (index == noOfPages - 1) {
								pageList.add(outputFileName);
								nextFileName = outputFileName;
								LOGGER.info("Adding output file name : " + outputFileName);
							} else {
								nextFileName = tempFilePath + File.separator + ImageMagicKConstants.TEMP_FILE_NAME + UNDERSCORE
										+ counter + ImageMagicKConstants.UNDERSCORE + documentId + FileType.TIF.getExtensionWithDot();
								pageList.add(nextFileName);
								filesToBeDeleted.add(new File(nextFileName));
								LOGGER.info("Adding output file name : " + nextFileName);
							}
							ConvertCmd convert = new ConvertCmd();
							IMOperation oper = new IMOperation();
							oper.addImage(pageList.size() - 1);
							oper.adjoin();
							oper.compress(tifCompression);
							oper.addImage();
							try {
								LOGGER.info("Running command for multipage tiff creation using imagemagick. tiff file name : "
										+ nextFileName);
								convert.run(oper, (Object[]) pageList.toArray());
								LOGGER.info("Multipage tiff creation using imagemagick. tiff file name : " + nextFileName);
								pageList.clear();
								pageList.add(nextFileName);
								LOGGER.info("Adding input file name : " + nextFileName);
								pageList.add(pages[index]);
								LOGGER.info("Adding input file name : " + pages[index]);
								counter = counter + 1;
							} catch (Exception e) {
								LOGGER.error("Error occured while running command for multipage tiff creation for file : "
										+ nextFileName, e);
								setDcmaApplicationException(new DCMAApplicationException(
										"Error occured while running command for multipage tiff creation.", e));
							}
						}
					}
					if (filesToBeDeleted != null && !filesToBeDeleted.isEmpty()) {
						for (File file : filesToBeDeleted) {
							if (file.exists()) {
								LOGGER.info("Deleting temporary file : " + file.getAbsolutePath());
								file.delete();
							}
						}
					}
				}
			});
		}
	}

	/**
	 * This method creates multi page pdf using Image-Magick.
	 * 
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param pages11 {@link String []}
	 * @param pdfCompression {@link String}
	 * @param pdfQuality {@link String}
	 * @param coloredImage {@link String}
	 * @param pdfOptimizationSwitch {@link String}
	 */
	public MultiPageExecutor(BatchInstanceThread batchInstanceThread, final String[] pages11, final String pdfCompression,
			final String pdfQuality, final String coloredImage, final String pdfOptimizationSwitch) {
		this.pages = new String[pages11.length];
		this.pages = pages11.clone();
		batchInstanceThread.add(new AbstractRunnable() {

			@Override
			public void run() {
				LOGGER.info("Creating multipgae pdf using imagemagick....");
				IMOperation imOper = new IMOperation();
				imOper.addImage(pages.length - 1);
				if (pdfQuality != null) {
					LOGGER.info("Adding pdfQuality : " + pdfQuality);
					imOper.quality(new Double(pdfQuality));
				}

				if (coloredImage != null && ImageMagicKConstants.FALSE.equalsIgnoreCase(coloredImage)) {
					imOper.monochrome();
				}
				if (pdfCompression != null) {
					LOGGER.info("Adding pdfCompression : " + pdfCompression);
					imOper.compress(pdfCompression);
				}

				if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
					LOGGER.info("Adding pdfOptimnisation.");
					// As per Ike suggestion, not performing any optimization with Imagemagick using PDF
					// op.type("optimize");
				}

				imOper.addImage();
				ConvertCmd convert = new ConvertCmd();
				try {
					convert.run(imOper, (Object[]) pages);
				} catch (Exception e) {
					LOGGER.error(MULTIPAGE_PDF_CREATION_ERROR_MSG + e.getMessage(), e);
					setDcmaApplicationException(new DCMAApplicationException(MULTIPAGE_PDF_CREATION_ERROR_MSG + e.getMessage(), e));
				}
			}
		});
	}

	/**
	 * This method creates multi page pdf using IText.
	 * 
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param pages11 {@link String}
	 * @param widthOfPdfPage int
	 * @param heightOfPdfPage int
	 */
	public MultiPageExecutor(BatchInstanceThread batchInstanceThread, final String[] pages11, final int widthOfPdfPage,
			final int heightOfPdfPage) {
		if (pages11 != null && pages11.length > 0) {
			this.pages = new String[pages11.length];
			this.pages = pages11.clone();
			batchInstanceThread.add(new AbstractRunnable() {

				@Override
				public void run() {
					String pdf = pages[pages.length - 1];
					Document document = null;
					PdfWriter writer = null;
					RandomAccessFileOrArray randomAccessArray = null;
					try {
						document = new Document(PageSize.LETTER, 0, 0, 0, 0);
						writer = PdfWriter.getInstance(document, new FileOutputStream(pdf));
						document.open();
						int comps = 1;
						int totalTiffImages = pages.length - 1;
						int index = 0;
						while (index < totalTiffImages) {
							randomAccessArray = new RandomAccessFileOrArray(pages[index]);
							comps = TiffImage.getNumberOfPages(randomAccessArray);
							// Conversion statement
							for (int tiffPageNumber = 0; tiffPageNumber < comps; ++tiffPageNumber) {
								Image img = TiffImage.getTiffImage(randomAccessArray, tiffPageNumber + 1);
								img.scaleToFit(widthOfPdfPage, heightOfPdfPage);
								document.add(img);
								document.newPage();
							}
							index++;
						}
					} catch (Exception e) {
						LOGGER.error("Error while creating pdf using iText" + e.getMessage(), e);
						//pdf = null;
					} finally {
						try {
							if (document != null) {
								document.close();
							}
							if (writer != null) {
								writer.close();
							}
							if (randomAccessArray != null) {
								randomAccessArray.close();
							}
						} catch (Exception e) {
							LOGGER.error("Error while closing I/O streams for write PDF. " + e.getMessage());
						}
					}
				}
			});
		}
	}

	/**
	 * The <code>MultiPageExecutor</code> method creates multi page searchable pdf using IText.
	 * 
	 * @param batchInstanceThread {@link BatchInstanceThread} thread instance of batch
	 * @param imageHtmlMap {@link Map} map containing image url with corresponding hocr
	 * @param isColoredPDF true for colored image pdf else otherwise
	 * @param isSearchablePDF true for searchable pdf else otherwise
	 * @param pdfFilePath {@link String} path where new pdf has to be created
	 * @param widthOfLine Integer for line width to be used
	 */
	public MultiPageExecutor(BatchInstanceThread batchInstanceThread, final Map<String, HocrPage> imageHtmlMap,
			final boolean isColoredPDF, final boolean isSearchablePDF, final String pdfFilePath, final int widthOfLine,
			final String[] pages) {
		if (imageHtmlMap != null && !imageHtmlMap.isEmpty()) {
			this.pages = new String[pages.length];
			this.pages = pages.clone();
			batchInstanceThread.add(new AbstractRunnable() {

				@Override
				public void run() {
					String pdf = pdfFilePath;
					Document document = null;
					PdfWriter writer = null;
					FileOutputStream fileOutputStream = null;
					boolean isPdfSearchable = isSearchablePDF;
					boolean isColoredImage = isColoredPDF;
					LOGGER.info("is searchable pdf: " + isPdfSearchable + ", is Colored Image: " + isColoredImage);
					try {
						document = new Document(PageSize.LETTER, 0, 0, 0, 0);
						fileOutputStream = new FileOutputStream(pdf);
						writer = PdfWriter.getInstance(document, fileOutputStream);
						document.open();
						Set<String> imageSet = imageHtmlMap.keySet();
						for (String imageUrl : imageSet) {
							HocrPage hocrPage = imageHtmlMap.get(imageUrl);
							String newImageUrl = getCompressedImage(isColoredImage, imageUrl);
							LOGGER.info("New Image URL: " + newImageUrl);
							addImageToPdf(writer, hocrPage, newImageUrl, isPdfSearchable, widthOfLine);
							document.newPage();
							(new File(newImageUrl)).delete();
						}
					} catch (FileNotFoundException fileNotFoundException) {
						LOGGER.error("Error occurred while creating pdf " + pdf + " : " + fileNotFoundException.toString());
					} catch (DocumentException documentException) {
						LOGGER.error("Error occurred while creating pdf " + pdf + " : " + documentException.toString());
					} finally {
						if (document != null && document.isOpen()) {
							document.close();
						}
						// Closing pdf writer
						if (null != writer) {
							writer.close();
						}

						// Closing file output stream of pdf
						if (null != fileOutputStream) {
							try {
								fileOutputStream.close();
							} catch (IOException ioException) {
								LOGGER.error("Error occurred while closing stream for pdf " + pdf + " : " + ioException.toString());
							}
						}
					}
				}
			});
		}
	}

	/**
	 * The <code>getTextWithCoordinatesMap</code> method is used to fetch text and respective coordinates from a html file passed.
	 * 
	 * @param htmlUrl {@link String} url of html file to be parsed
	 * @return {@link Map} containing text as key and coordinates as value
	 */
	private Map<String, int[]> getTextWithCoordinatesMap(final HocrPage hocrPage, final int widthOfLine) {
		Map<String, int[]> textCoordinateMap = new HashMap<String, int[]>();
		if (null != hocrPage) {
			List<LineDataCarrier> lineDataCarriers = HocrUtil.getLineDataCarrierList(hocrPage.getSpans(), hocrPage.getPageID(),
					widthOfLine, true);
			Coordinates coordinates;
			String rowData;
			for (LineDataCarrier lineDataCarrier : lineDataCarriers) {
				rowData = lineDataCarrier.getLineRowData();
				coordinates = lineDataCarrier.getRowCoordinates();
				int[] coordArray = {coordinates.getX0().intValue(), coordinates.getY0().intValue(), coordinates.getX1().intValue(),
						coordinates.getY1().intValue()};
				textCoordinateMap.put(rowData, coordArray);
			}
		}
		return textCoordinateMap;
	}

	/**
	 * The <code>addImageToPdf</code> method is used to add image to pdf and make it searchable by adding image text in invisible mode
	 * w.r.t parameter 'isPdfSearchable' passed.
	 * 
	 * @param pdfWriter {@link PdfWriter} writer of pdf in which image has to be added
	 * @param htmlUrl {@link HocrPage} corresponding html file for fetching text and coordinates
	 * @param imageUrl {@link String} url of image to be added in pdf
	 * @param isPdfSearchable true for searchable pdf else otherwise
	 * @param widthOfLine
	 */
	private void addImageToPdf(PdfWriter pdfWriter, HocrPage hocrPage, String imageUrl, boolean isPdfSearchable, final int widthOfLine) {
		if (null != pdfWriter && null != imageUrl && imageUrl.length() > 0) {
			try {
				LOGGER.info("Adding image" + imageUrl + " to pdf using iText");
				Image pageImage = Image.getInstance(imageUrl);
				float dotsPerPointX = pageImage.getDpiX() / PDF_RESOLUTION;
				float dotsPerPointY = pageImage.getDpiY() / PDF_RESOLUTION;
				PdfContentByte pdfContentByte = pdfWriter.getDirectContent();

				pageImage.scaleToFit(pageImage.getWidth() / dotsPerPointX, pageImage.getHeight() / dotsPerPointY);

				pageImage.setAbsolutePosition(0, 0);

				// Add image to pdf
				pdfWriter.getDirectContentUnder().addImage(pageImage);
				pdfWriter.getDirectContentUnder().add(pdfContentByte);

				// If pdf is to be made searchable
				if (isPdfSearchable) {
					LOGGER.info("Adding invisible text for image: " + imageUrl);
					float pageImagePixelHeight = pageImage.getHeight();
					Font defaultFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, CMYKColor.BLACK);

					// Fetch text and coordinates for image to be added
					Map<String, int[]> textCoordinatesMap = getTextWithCoordinatesMap(hocrPage, widthOfLine);
					Set<String> ketSet = textCoordinatesMap.keySet();

					// Add text at specific location
					for (String key : ketSet) {
						int[] coordinates = textCoordinatesMap.get(key);
						float bboxWidthPt = (coordinates[2] - coordinates[0]) / dotsPerPointX;
						float bboxHeightPt = (coordinates[3] - coordinates[1]) / dotsPerPointY;
						pdfContentByte.beginText();

						// To make text added as invisible
						pdfContentByte.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_INVISIBLE);
						pdfContentByte.setLineWidth(Math.round(bboxWidthPt));

						// Ceil is used so that minimum font of any text is 1
						// For exception of unbalanced beginText() and endText()
						if (bboxHeightPt > 0.0) {
							pdfContentByte.setFontAndSize(defaultFont.getBaseFont(), (float) Math.ceil(bboxHeightPt));
						} else {
							pdfContentByte.setFontAndSize(defaultFont.getBaseFont(), 1);
						}
						float xCoordinate = (float) (coordinates[0] / dotsPerPointX);
						float yCoordinate = (float) ((pageImagePixelHeight - coordinates[3]) / dotsPerPointY);
						pdfContentByte.moveText(xCoordinate, yCoordinate);
						pdfContentByte.showText(key);
						pdfContentByte.endText();
					}
				}
				pdfContentByte.closePath();
			} catch (BadElementException badElementException) {
				LOGGER
						.error("Error occurred while adding image" + imageUrl + " to pdf using Itext: "
								+ badElementException.toString());
			} catch (DocumentException documentException) {
				LOGGER.error("Error occurred while adding image" + imageUrl + " to pdf using Itext: " + documentException.toString());
			} catch (MalformedURLException malformedURLException) {
				LOGGER.error("Error occurred while adding image" + imageUrl + " to pdf using Itext: "
						+ malformedURLException.toString());
			} catch (IOException ioException) {
				LOGGER.error("Error occurred while adding image" + imageUrl + " to pdf using Itext: " + ioException.toString());
			}
		}
	}

	/**
	 * The <code>getCompressedImage</code> method is used to compress passed image according to compression type LZW, quality 100 and
	 * convert image to monochrome i.e., black and white w.r.t isColoredImage passed.
	 * 
	 * @param isColoredImage true for colored image and false otherwise
	 * @param imageUrl image to be converted
	 * @return url of converted image from passed image
	 */
	private String getCompressedImage(boolean isColoredImage, String imageUrl) {
		String newImageUrl = imageUrl;
		if (null != imageUrl && imageUrl.length() > 0) {
			IMOperation imOperation = new IMOperation();

			// Applying default image quality
			imOperation.quality((double) IMAGE_QUALITY);
			imOperation.type(OPERATION_TYPE);
			if (!isColoredImage) {
				imOperation.monochrome();
			}

			// Apply compression
			imOperation.compress(COMPRESSION_TYPE);
			imOperation.addImage(); // place holder for input file
			imOperation.addImage(); // place holder for output file

			// Converting image
			ConvertCmd convert = new ConvertCmd();
			newImageUrl = imageUrl.substring(0, imageUrl.lastIndexOf(DOTS)) + NEW + TIF_EXTENSION;

			try {
				convert.run(imOperation, new Object[] {imageUrl, newImageUrl});
			} catch (org.im4java.core.CommandException commandException) {
				LOGGER.error("IM4JAVA_TOOLPATH is not set for converting images using image magic: " + commandException.toString());
			} catch (IOException ioException) {
				LOGGER.error("Files not found for converting operation of image magic: " + ioException.toString());
			} catch (InterruptedException interruptedException) {
				LOGGER.error("Conversion operation of image magic interrupted in between: " + interruptedException.toString());
			} catch (IM4JavaException im4JavaException) {
				LOGGER.error("Error occurred while converting images using image magic: " + im4JavaException.toString());
			}
		}
		return newImageUrl;
	}

	public String[] getPages() {
		return pages;
	}
}
