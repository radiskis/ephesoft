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

	private static final Logger LOGGER = LoggerFactory.getLogger(PrioritizedFolderContainer.class);

	final private long waitTime;

	public Map<BatchClass, UncFolder> uncFolders = new TreeMap<BatchClass, UncFolder>(new Comparator<BatchClass>() {

		@Override
		public int compare(final BatchClass batchClass1, final BatchClass batchClass2) {
			int result = 0;
			if (batchClass1.getId() != batchClass2.getId()) {
				final Integer o1Priority = Integer.valueOf(batchClass1.getPriority());
				final Integer o2Priority = Integer.valueOf(batchClass2.getPriority());

				result = o1Priority.compareTo(o2Priority);
				if (result == 0) {
					result = +1;
				}
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
		final Collection<UncFolder> folders = uncFolders.values();
		for (UncFolder uncFolder : folders) {
			if (uncFolder.getUncFolderPath().equals(folderDetail.getParentPath())) {
				uncFolder.addFolderDetail(folderDetail);
				break;
			}
		}
	}

	public synchronized FolderDetail next() {
		FolderDetail detail = null;
		final Set<Map.Entry<BatchClass, UncFolder>> uncFolderSet = uncFolders.entrySet();
		for (Map.Entry<BatchClass, UncFolder> entry : uncFolderSet) {
			detail = entry.getValue().poll();
			if (detail != null) {
				break;
			}
		}
		return detail;
	}

	private class UncFolder {

		final private String uncFolderPath;
		final private Set<FolderDetail> folderDetails = new TreeSet<FolderDetail>();

		public UncFolder(final String uncFolderPath) {
			this.uncFolderPath = uncFolderPath;
		}

		public void addFolderDetail(FolderDetail folderDetail) {
			folderDetails.add(folderDetail);
			LOGGER.trace("PUSH::" + folderDetail.getFullPath());
		}

		public FolderDetail poll() {
			FolderDetail folderDetail = null;
			try {
				folderDetail = ((TreeSet<FolderDetail>) this.folderDetails).first();
				if (folderDetail != null && (System.currentTimeMillis() - folderDetail.getCreationTime()) >= waitTime) {
					LOGGER.trace("PULL::" + folderDetail.getFullPath());
					folderDetail = ((TreeSet<FolderDetail>) this.folderDetails).pollFirst();
				}
			} catch (NoSuchElementException e) {
				LOGGER.error("No such element is found:" +e.getMessage(),e);
			}
			return folderDetail;
		}

		public String getUncFolderPath() {
			return uncFolderPath;
		}
	}

}
