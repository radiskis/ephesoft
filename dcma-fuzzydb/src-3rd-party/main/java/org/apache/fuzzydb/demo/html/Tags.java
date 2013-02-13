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

package org.apache.fuzzydb.demo.html;

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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public final class Tags {

  /**
   * contains all tags for which whitespaces have to be inserted for proper tokenization
   */
  public static final Set<String> WS_ELEMS = Collections.synchronizedSet(new HashSet<String>());

  static{
    WS_ELEMS.add("<hr");
    WS_ELEMS.add("<hr/");  // note that "<hr />" does not need to be listed explicitly
    WS_ELEMS.add("<br");
    WS_ELEMS.add("<br/");
    WS_ELEMS.add("<p");
    WS_ELEMS.add("</p");
    WS_ELEMS.add("<div");
    WS_ELEMS.add("</div");
    WS_ELEMS.add("<td");
    WS_ELEMS.add("</td");
    WS_ELEMS.add("<li");
    WS_ELEMS.add("</li");
    WS_ELEMS.add("<q");
    WS_ELEMS.add("</q");
    WS_ELEMS.add("<blockquote");
    WS_ELEMS.add("</blockquote");
    WS_ELEMS.add("<dt");
    WS_ELEMS.add("</dt");
    WS_ELEMS.add("<h1");
    WS_ELEMS.add("</h1");
    WS_ELEMS.add("<h2");
    WS_ELEMS.add("</h2");
    WS_ELEMS.add("<h3");
    WS_ELEMS.add("</h3");
    WS_ELEMS.add("<h4");
    WS_ELEMS.add("</h4");
    WS_ELEMS.add("<h5");
    WS_ELEMS.add("</h5");
    WS_ELEMS.add("<h6");
    WS_ELEMS.add("</h6");
  }
}
