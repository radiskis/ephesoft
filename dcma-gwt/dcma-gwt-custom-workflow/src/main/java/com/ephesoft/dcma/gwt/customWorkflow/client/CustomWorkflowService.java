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

package com.ephesoft.dcma.gwt.customworkflow.client;

import java.util.List;

import com.ephesoft.dcma.gwt.core.client.DCMARemoteService;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.exception.GWTException;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("customWorkflowService")
public interface CustomWorkflowService extends DCMARemoteService {

	/**
	 * API to add new plugin given the new plugin name, plugin xml path and plugin jar path.
	 * 
	 * @param pluginName {@link String}
	 * @param xmlSourcePath {@link String}
	 * @param jarSourcePath {@link String}
	 * @return {@link Boolean} if plugin is added successfully or not
	 * @throws GWTException
	 */
	Boolean addNewPlugin(String pluginName, String xmlSourcePath, String jarSourcePath) throws GWTException;

	/**
	 * API to get the list of all plugins from the database.
	 * 
	 * @return {@link List}< {@link PluginDetailsDTO}>
	 */
	List<PluginDetailsDTO> getAllPluginDetailDTOs();

	/**
	 * API to update all the dirty plugins.
	 * 
	 * @param allPluginDetailsDTO {@link List}< {@link PluginDetailsDTO}>
	 * @return {@link List}< {@link PluginDetailsDTO}>
	 */
	List<PluginDetailsDTO> updateAllPluginDetailsDTOs(List<PluginDetailsDTO> allPluginDetailsDTO);

	/**
	 * API to delete a plugin.
	 * 
	 * @param pluginDetailsDTO {@link PluginDetailsDTO}
	 * @return {@link Boolean} whether the plugin could be deleted or not.
	 * @throws GWTException
	 */
	Boolean deletePlugin(PluginDetailsDTO pluginDetailsDTO) throws GWTException;
}
