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
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.ephesoft.dcma.util.CustomValueSortedMap;

public class SearchFiles {

	private SearchFiles() {
	}

	public static Map<String, Float> generateConfidence(String indexFolder, String query, String field, int noOfPages, int maxValue)
			throws Exception {

		CustomValueSortedMap docNameScore = new CustomValueSortedMap(maxValue);
		IndexReader reader = IndexReader.open(FSDirectory.open(new File(indexFolder)), true); // only searching, so read-only=true
		Searcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		// Analyzer analyzer = new WhitespaceAnalyzer();
		QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, field, analyzer);
		if (query != null) {
			query = query.trim();
		} else {
			System.out.println("Wrong Query");
			return null;
		}

		Query searchQuery = parser.parse(query);
		ScoreDoc[] scoreDocs = doPagingSearch(searcher, searchQuery, noOfPages);
		if (scoreDocs != null && scoreDocs.length > 0) {
			for (int i = 0; i < scoreDocs.length; i++) {
				Document document = searcher.doc(scoreDocs[i].doc);
				/* Explanation exp = */searcher.explain(searchQuery, scoreDocs[i].doc);
				String docPageType = fetchDocPageType(document.get("path"));
				docNameScore.add(docPageType, Double.valueOf(scoreDocs[i].score));
			}
		}
		reader.close();
		return docNameScore.getReverseSortedMapValueInFloat();
	}

	public static String fetchDocPageType(String absolutePath) throws Exception {
		String returnValue = "";
		int index = -1;
		if (absolutePath != null && absolutePath.length() > 0) {
			String[] tokens = absolutePath.split("/");
			if (tokens != null && tokens.length > 0) {
				for (int i = 0; i < tokens.length; i++) {
					if (tokens[i].contains(".htm")) {
						index = i;
					}
				}
			} else {
				System.out.println("No file seperator in path");
				throw new Exception("No file seperator in path");
			}
			if (index >= 2) {
				returnValue = tokens[index - 1];
			} else {
				System.out.println("Path contains less than 3 tokens");
				throw new Exception("Path contains less than 3 tokens");
			}
		} else {
			System.out.println("Wrong path name found in fetchDocPageType");
			throw new Exception("Wrong path name found in fetchDocPageType");
		}
		return returnValue;
	}

	/**
	 * This method uses a custom HitCollector implementation which simply prints out the docId and score of every matching document.
	 * 
	 * This simulates the streaming search use case, where all hits are supposed to be processed, regardless of their relevance.
	 */
	public static void doStreamingSearch(final Searcher searcher, Query query, String docTitle) throws IOException {
		Collector streamingHitCollector = new Collector() {

			private Scorer scorer;
			private int docBase;

			// simply print docId and score of every matching document

			@Override
			public void collect(int doc) throws IOException {
				System.out.println("doc = " + doc + docBase + " confidence score = " + scorer.score());
			}

			@Override
			public boolean acceptsDocsOutOfOrder() {
				return true;
			}

			@Override
			public void setNextReader(IndexReader reader, int docBase) throws IOException {
				this.docBase = docBase;
			}

			@Override
			public void setScorer(Scorer scorer) throws IOException {
				this.scorer = scorer;
			}

		};
		searcher.search(query, streamingHitCollector);
	}

	/**
	 * This demonstrates a typical paging search scenario, where the search engine presents pages of size n to the user. The user can
	 * then go to the next page if interested in the next hits.
	 * 
	 * When the query is executed for the first time, then only enough results are collected to fill 5 result pages. If the user wants
	 * to page beyond this limit, then the query is executed another time and all hits are collected.
	 * 
	 */
	public static ScoreDoc[] doPagingSearch(Searcher searcher, Query query, int noOfPages) throws IOException {

		// Collect enough docs to show 5 pages
		TopScoreDocCollector collector = TopScoreDocCollector.create(noOfPages, true);
		searcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		int numTotalHits = collector.getTotalHits();
		// System.out.println("Confidence Score : : "+hits.length);
		System.out.println(numTotalHits + " total matching documents");
		return hits;
	}
}
