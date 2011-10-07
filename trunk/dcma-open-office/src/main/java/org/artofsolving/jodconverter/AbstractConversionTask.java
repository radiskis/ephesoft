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

//
// JODConverter - Java OpenDocument Converter
// Copyright 2009 Art of Solving Ltd
// Copyright 2004-2009 Mirko Nasato
//
// JODConverter is free software: you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation, either version 3 of
// the License, or (at your option) any later version.
//
// JODConverter is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General
// Public License along with JODConverter.  If not, see
// <http://www.gnu.org/licenses/>.
//
package org.artofsolving.jodconverter;

import static org.artofsolving.jodconverter.office.OfficeUtils.SERVICE_DESKTOP;
import static org.artofsolving.jodconverter.office.OfficeUtils.cast;
import static org.artofsolving.jodconverter.office.OfficeUtils.toUnoProperties;
import static org.artofsolving.jodconverter.office.OfficeUtils.toUrl;

import java.io.File;
import java.util.Map;

import org.artofsolving.jodconverter.office.OfficeContext;
import org.artofsolving.jodconverter.office.OfficeException;
import org.artofsolving.jodconverter.office.OfficeTask;

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.XComponent;
import com.sun.star.task.ErrorCodeIOException;
import com.sun.star.util.CloseVetoException;
import com.sun.star.util.XCloseable;
import com.sun.star.util.XRefreshable;

/**
 * 
 * @author Ephesoft
 * 
 */
public abstract class AbstractConversionTask implements OfficeTask {

	private final File inputFile;
	private final File outputFile;
	private final String inputFileURL;

	public AbstractConversionTask(File inputFile, File outputFile) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
		this.inputFileURL = null;
	}

	public AbstractConversionTask(String inputFileURL, File outputFile) {
		this.inputFile = null;
		this.outputFile = outputFile;
		this.inputFileURL = inputFileURL;
	}

	protected abstract Map<String, ?> getLoadProperties(File inputFile);

	protected abstract Map<String, ?> getStoreProperties(File outputFile, XComponent document);

	public void execute(OfficeContext context) throws OfficeException {
		XComponent document = null;
		try {
			if (inputFile != null) {
				document = loadDocument(context, inputFile);
			} else {
				document = loadDocument(context, inputFileURL);
			}
			storeDocument(document, outputFile);
		} catch (OfficeException officeException) {
			throw officeException;
		} catch (Exception exception) {
			throw new OfficeException("conversion failed", exception);
		} finally {
			if (document != null) {
				XCloseable closeable = cast(XCloseable.class, document);
				if (closeable != null) {
					try {
						closeable.close(true);
					} catch (CloseVetoException closeVetoException) {
						// whoever raised the veto should close the document
					}
				} else {
					document.dispose();
				}
			}
		}
	}

	private XComponent loadDocument(OfficeContext context, File inputFile) throws OfficeException {
		if (!inputFile.exists()) {
			throw new OfficeException("input document not found");
		}
		XComponentLoader loader = cast(XComponentLoader.class, context.getService(SERVICE_DESKTOP));
		Map<String, ?> loadProperties = getLoadProperties(inputFile);
		XComponent document = null;
		try {
			document = loader.loadComponentFromURL(toUrl(inputFile), "_blank", 0, toUnoProperties(loadProperties));
		} catch (IllegalArgumentException illegalArgumentException) {
			throw new OfficeException("could not load document: " + inputFile.getName(), illegalArgumentException);
		} catch (ErrorCodeIOException errorCodeIOException) {
			throw new OfficeException("could not load document: " + inputFile.getName() + "; errorCode: "
					+ errorCodeIOException.ErrCode, errorCodeIOException);
		} catch (IOException ioException) {
			throw new OfficeException("could not load document: " + inputFile.getName(), ioException);
		}
		if (document == null) {
			throw new OfficeException("could not load document: " + inputFile.getName());
		}
		XRefreshable refreshable = cast(XRefreshable.class, document);
		if (refreshable != null) {
			refreshable.refresh();
		}
		return document;
	}

	private XComponent loadDocument(OfficeContext context, String inputFileURL) throws OfficeException {
		XComponentLoader loader = cast(XComponentLoader.class, context.getService(SERVICE_DESKTOP));
		XComponent document = null;
		try {
			PropertyValue[] loadProps = new PropertyValue[1];
			loadProps[0] = new PropertyValue();
			loadProps[0].Name = "Hidden";
			loadProps[0].Value = true;
			document = loader.loadComponentFromURL(inputFileURL, "_blank", 0, loadProps);
		} catch (IllegalArgumentException illegalArgumentException) {
			throw new OfficeException("could not load document: " + inputFileURL, illegalArgumentException);
		} catch (ErrorCodeIOException errorCodeIOException) {
			throw new OfficeException("could not load document: " + inputFileURL + "; errorCode: " + errorCodeIOException.ErrCode,
					errorCodeIOException);
		} catch (IOException ioException) {
			throw new OfficeException("could not load document: " + inputFileURL, ioException);
		}
		if (document == null) {
			throw new OfficeException("could not load document: " + inputFileURL);
		}
		XRefreshable refreshable = cast(XRefreshable.class, document);
		if (refreshable != null) {
			refreshable.refresh();
		}
		return document;
	}

	private void storeDocument(XComponent document, File outputFile) throws OfficeException {
		Map<String, ?> storeProperties = getStoreProperties(outputFile, document);
		if (storeProperties == null) {
			throw new OfficeException("unsupported conversion");
		}
		try {
			PropertyValue[] storeProps = new PropertyValue[1];
			storeProps[0] = new PropertyValue();
			storeProps[0].Name = "FilterName";
			storeProps[0].Value = "writer_pdf_Export";
			cast(XStorable.class, document).storeToURL(toUrl(outputFile), storeProps);
		} catch (ErrorCodeIOException errorCodeIOException) {
			throw new OfficeException("could not store document: " + outputFile.getName() + "; errorCode: "
					+ errorCodeIOException.ErrCode, errorCodeIOException);
		} catch (IOException ioException) {
			throw new OfficeException("could not store document: " + outputFile.getName(), ioException);
		}
	}

}
