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

package com.ephesoft.dcma.monitor;

import java.io.File;

import net.contentobjects.jnotify.JNotifyListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Folder listener class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.monitor.service.FolderMonitorService
 */
@Component
public class EphesoftFolderListner implements JNotifyListener, ApplicationContextAware {

	/**
	 * An instance of Logger for proper logging.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(EphesoftFolderListner.class);
	
	/**
	 * An instance of {@link ApplicationContext}.
	 */
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void fileCreated(int arg0, String arg1, String arg2) {
		LOG.trace("WATCH::" + arg1 + File.separator + arg2);
		applicationContext.publishEvent(new FolderModificationEvent(applicationContext, new FolderDetail(arg1, arg2)));
	}

	@Override
	public void fileDeleted(int arg0, String arg1, String arg2) {
		LOG.trace("We are doing nothing with file deletion.");
	}

	@Override
	public void fileModified(int arg0, String arg1, String arg2) {
		LOG.trace("We are doing nothing with file modification.");
	}

	@Override
	public void fileRenamed(int arg0, String arg1, String arg2, String arg3) {
		LOG.trace("We are doing nothing with file renaming.");
	}
}
