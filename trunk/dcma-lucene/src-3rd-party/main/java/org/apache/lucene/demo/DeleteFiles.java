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

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
//import org.apache.lucene.index.Term;


/** Deletes documents from an index that do not contain a term. */
public class DeleteFiles {
  
  private DeleteFiles() {}                         // singleton

  /** Deletes documents from an index that do not contain a term. */
  public static void main(String[] args) {
    String usage = "java org.apache.lucene.demo.DeleteFiles <unique_term>";
    if (args.length == 0) {
      System.err.println("Usage: " + usage);
      System.exit(1);
    }
    try {
      Directory directory = FSDirectory.open(new File("index"));
      IndexReader reader = IndexReader.open(directory, false); // we don't want read-only because we are about to delete

      Term term = new Term("path", args[0]);
      int deleted = reader.deleteDocuments(term);

      System.out.println("deleted " + deleted +
 			 " documents containing " + term);

      // one can also delete documents by their internal id:
      /*
      for (int i = 0; i < reader.maxDoc(); i++) {
        System.out.println("Deleting document with id " + i);
        reader.delete(i);
      }*/

      reader.close();
      directory.close();

    } catch (Exception e) {
      System.out.println(" caught a " + e.getClass() +
			 "\n with message: " + e.getMessage());
    }
  }
}
