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

package com.ephesoft.dcma.monitor;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.da.domain.BatchClass;

public final class PrioritizedFolderContainer {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private long waitTime;

	public Map<BatchClass, UncFolder> uncFolders = new TreeMap<BatchClass, UncFolder>(new Comparator<BatchClass>() {
		@Override
		public int compare(BatchClass o1, BatchClass o2) {
			int result = 0;
			if(o1.getId() != o2.getId()) {
				Integer o1Priority = Integer.valueOf(o1.getPriority());
				Integer o2Priority = Integer.valueOf(o2.getPriority());

				result = o1Priority.compareTo(o2Priority);
				if(result == 0) result = +1;
			}
			return result;
		}
	});
	
	public PrioritizedFolderContainer(long waitTime) {
		this.waitTime = waitTime;
	}
	
	public void addBatchClasss(BatchClass batchClass) {
		uncFolders.put(batchClass, new UncFolder(batchClass.getUncFolder()));
	}
	
	public synchronized void addFolderDetail(FolderDetail folderDetail) {
		Collection<UncFolder> folders = uncFolders.values();
		for (UncFolder uncFolder : folders) {
			if(uncFolder.getUncFolderPath().equals(folderDetail.getParentPath())) {
				uncFolder.addFolderDetail(folderDetail);
				break;
			}
		}
	}
	
	public synchronized FolderDetail next() {
		FolderDetail detail = null;
		Set<Map.Entry<BatchClass, UncFolder>> uncFolderSet = uncFolders.entrySet();
		for (Map.Entry<BatchClass, UncFolder> entry : uncFolderSet) {
			detail = entry.getValue().poll();
			if(detail != null) break;
		}
		return detail;
	}
	
	private class UncFolder {
		private String uncFolderPath;
		private TreeSet<FolderDetail> folderDetails = new TreeSet<FolderDetail>();
		
		public UncFolder(String uncFolderPath) {
			this.uncFolderPath = uncFolderPath;
		}
		
		public void addFolderDetail(FolderDetail folderDetail) {
			folderDetails.add(folderDetail);
			logger.trace("PUSH::" + folderDetail.getFullPath());
		}
		
		public FolderDetail poll() {
			try {
				FolderDetail folderDetail = this.folderDetails.first();
				if (folderDetail != null) {
					if ((System.currentTimeMillis() - folderDetail.getCreationTime()) >= waitTime) {
						logger.trace("PULL::" + folderDetail.getFullPath());
						return this.folderDetails.pollFirst();
					}
				}
			} catch (NoSuchElementException e) { }
			return null;
		}
		
		public String getUncFolderPath() {
			return uncFolderPath;
		}
	}
}
