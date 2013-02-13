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

package org.apache.fuzzydb.demo;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/** A utility for making Lucene Documents for HTML documents. */

public class HTMLDocument {

	static char dirSep = System.getProperty("file.separator").charAt(0);

	public static String uid(File f) {
		// Append path and date into a string in such a way that lexicographic
		// sorting gives the same results as a walk of the file hierarchy. Thus
		// null (\u0000) is used both to separate directory components and to
		// separate the path from the date.
		return f.getPath().replace(dirSep, '\u0000') + "\u0000"
				+ DateTools.timeToString(f.lastModified(), DateTools.Resolution.SECOND);
	}

	public static String uid2url(String uid) {
		String url = uid.replace('\u0000', '/'); // replace nulls with slashes
		return url.substring(0, url.lastIndexOf('/')); // remove date from end
	}

	public static Document Document(String eachRecord, int indexRowId) throws IOException, InterruptedException {
		if (null == eachRecord) {
			String err = "Invalid row for Index generation";
			System.err.println(err);
		}
		String rowId = "";
		String[] allRecords = eachRecord.split(";;;");
		if(allRecords != null && allRecords.length > 0){
			rowId = allRecords[indexRowId];
		}
		//String rowId = eachRecord.substring(0, eachRecord.indexOf(" "));
		// make a new, empty document
		if(rowId.length() > 0){
			Document doc = new Document();
			doc.add(new Field("rowId", rowId, Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("rowData", eachRecord, Field.Store.NO, Field.Index.ANALYZED));
			return doc;
		}else{
			return null;
		}
		
	}

	private HTMLDocument() {
	}
}
