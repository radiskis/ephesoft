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

package org.apache.lucene.demo;

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
import java.util.Arrays;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/** Indexer for HTML files. */
public class IndexHTML {

	private IndexHTML() {
	}

	private static boolean deleting = false; // true during deletion pass
	private static IndexReader reader; // existing index
	private static IndexWriter writer; // new index being built
	private static TermEnum uidIter; // document id iterator

	/** Indexer for HTML files. */
	public static void generateIndex(String indexFolder, String learnFolder, boolean create) throws Exception {
		try {
			File index = new File(indexFolder);

			if (null == index || !index.exists()) {
				// Create multiple directories
				boolean success = (index).mkdirs();
				if (success) {
					System.out.println("Directories: " + index.getAbsolutePath() + " created.");
				} else {
					System.out.println("Directories: " + index.getAbsolutePath() + " not created.");
				}
			}

			File root = null;
			root = new File(learnFolder);

			if (root == null) {
				System.err.println("Specify directory to index");
				return;
			}
			Date start = new Date();
			writer = new IndexWriter(FSDirectory.open(index), new StandardAnalyzer(Version.LUCENE_CURRENT), create,
					new IndexWriter.MaxFieldLength(1000000));
			indexDocs(root, index, create); // add new docs
			System.out.println("Optimizing index...");
			Date end = new Date();
			System.out.print(end.getTime() - start.getTime());
			System.out.println(" total milliseconds");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage(), e);
		}
		finally
		{
			writer.optimize();
			writer.close();
		}
		
	}

	/*
	 * Walk directory hierarchy in uid order, while keeping uid iterator from /* existing index in sync. Mismatches indicate one of:
	 * (a) old documents to /* be deleted; (b) unchanged documents, to be left alone; or (c) new /* documents, to be indexed.
	 */

	private static void indexDocs(File file, File index, boolean create) throws Exception {
		if (!create) { // incrementally update

			reader = IndexReader.open(FSDirectory.open(index), false); // open existing index
			uidIter = reader.terms(new Term("summary", "")); // init uid iterator

			indexDocs(file);

			if (deleting) { // delete rest of stale docs
				while (uidIter.term() != null && uidIter.term().field() == "uid") {
					System.out.println("deleting " + HTMLDocument.uid2url(uidIter.term().text()));
					reader.deleteDocuments(uidIter.term());
					uidIter.next();
				}
				deleting = false;
			}

			uidIter.close(); // close uid iterator
			reader.close(); // close existing index

		} else {
			reader = IndexReader.open(FSDirectory.open(index), false); // open existing index
			/* TermEnum terms = */reader.terms();
			indexDocs(file);
			reader.close();
		}

	}

	private static void indexDocs(File file) throws Exception {
		if (file.isDirectory()) { // if a directory
			String[] files = file.list(); // list its files
			Arrays.sort(files); // sort the files
			for (int i = 0; i < files.length; i++)
				// recursively index them
				indexDocs(new File(file, files[i]));

		} else if (file.getPath().endsWith(".html") || // index .html files
				file.getPath().endsWith(".htm")) { // index .txt files
			try {
				if (uidIter != null) {
					String uid = HTMLDocument.uid(file); // construct uid for doc

					while (uidIter.term() != null && uidIter.term().field() == "uid" && uidIter.term().text().compareTo(uid) < 0) {
						if (deleting) { // delete stale docs
							System.out.println("deleting " + HTMLDocument.uid2url(uidIter.term().text()));
							reader.deleteDocuments(uidIter.term());
						}
						uidIter.next();
					}
					if (uidIter.term() != null && uidIter.term().field() == "uid" && uidIter.term().text().compareTo(uid) == 0) {
						uidIter.next(); // keep matching docs
					} else if (!deleting) { // add new docs
						Document doc = HTMLDocument.Document(file);
						System.out.println("adding " + doc.get("path"));
						writer.addDocument(doc);
					}
				} else {
					Document doc = HTMLDocument.Document(file);
					System.out.println("adding " + doc.get("path"));
					writer.addDocument(doc); // add docs unconditionally
				}
			} catch (Exception e) {
				throw new Exception("*****Unable to generate indexes for " + file.getName() + " *****", e);
			}
		}
	}
}
