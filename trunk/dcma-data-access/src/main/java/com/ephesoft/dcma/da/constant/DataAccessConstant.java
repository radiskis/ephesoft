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

package com.ephesoft.dcma.da.constant;

import java.io.File;

/**
 * This is a common constants file for Data Access plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public interface DataAccessConstant {
	
	/**
	 * Initializing BASE_FOLDER_LOCATION.
	 */
	String BASE_FOLDER_LOCATION = "batch.base_folder";

	/**
	 * Initializing PROPERTY_FILE.
	 */
	String PROPERTY_FILE = "db-patch";

	/**
	 * Initializing META_INF_PATH.
	 */
	String META_INF_PATH = "META-INF\\dcma-data-access";

	/**
	 * Initializing AND.
	 */
	String AND = ",";

	/**
	 * Initializing EMPTY.
	 */
	String EMPTY = "";
	
	/**
	 * Initializing OR_SYMBOL.
	 */
	String OR_SYMBOL = "/";

	/**
	 * Initializing PROBLEM_CLOSING_STREAM_FOR_FILE.
	 */
	String PROBLEM_CLOSING_STREAM_FOR_FILE = "Problem closing stream for file :";

	/**
	 * Initializing ERROR_DURING_DE_SERIALIZING_THE_PROPERTIES_FOR_DATABASE_UPGRADE.
	 */
	String ERROR_DURING_DE_SERIALIZING_THE_PROPERTIES_FOR_DATABASE_UPGRADE = "Error during de-serializing the properties for Database Upgrade: ";

	/**
	 * Initializing ERROR_DURING_READING_THE_SERIALIZED_FILE.
	 */
	String ERROR_DURING_READING_THE_SERIALIZED_FILE = "Error during reading the serialized file. ";

	/**
	 * Initializing UPDATING_BATCH_CLASSES.
	 */
	String UPDATING_BATCH_CLASSES = "Updating Batch Classes....";

	/**
	 * Initializing PROPERTY_FILE_DELIMITER.
	 */
	String PROPERTY_FILE_DELIMITER = ";";
	
	/**
	 * Initializing DEPENDENCY_UPDATE.
	 */
	String DEPENDENCY_UPDATE = "DependencyUpdate";

	/**
	 * Initializing MODULE_CONFIG_UPDATE.
	 */
	String MODULE_CONFIG_UPDATE = "ModuleConfigUpdate";

	/**
	 * Initializing PLUGIN_CONFIG_UPDATE.
	 */
	String PLUGIN_CONFIG_UPDATE = "PluginConfigUpdate";

	/**
	 * Initializing SCANNER_CONFIG_UPDATE.
	 */
	String SCANNER_CONFIG_UPDATE = "ScannerConfigUpdate";

	/**
	 * Initializing PLUGIN_UPDATE.
	 */
	String PLUGIN_UPDATE = "PluginUpdate";

	/**
	 * Initializing BATCH_CLASS_UPDATE.
	 */
	String BATCH_CLASS_UPDATE = "BatchClassUpdate";

	/**
	 * Initializing MODULE_UPDATE.
	 */
	String MODULE_UPDATE = "ModuleUpdate";

	/**
	 * Initializing PROPERTIES.
	 */
	String PROPERTIES = ".properties";
	
	/**
	 * Initializing EXECUTED.
	 */
	String EXECUTED = "-executed";

	/**
	 * Initializing DCMA_BATCH_PROPERTIES.
	 */
	String DCMA_BATCH_PROPERTIES = "META-INF/dcma-batch/dcma-batch.properties";

	/**
	 * Initializing COMMA.
	 */
	String COMMA = ",";

	/**
	 * Initializing SEMI_COLON.
	 */
	String SEMI_COLON = ";";
	
	/**
	 * Initializing UPGRADE_PATCH_ENABLE.
	 */
	String UPGRADE_PATCH_ENABLE = "upgradePatch.enable";

	/**
	 * Initializing UPGRADE_PATCH_DEFAULT_BATCH_CLASS_ROLES.
	 */
	String UPGRADE_PATCH_DEFAULT_BATCH_CLASS_ROLES = "upgradePatch.defaultBatchClassRoles";

	/**
	 * Initializing DATA_ACCESS_DCMA_DB_PROPERTIES.
	 */
	String DATA_ACCESS_DCMA_DB_PROPERTIES = "META-INF/dcma-data-access/dcma-db.properties";
	
	/**
	 * Initializing BATCHCLASS.
	 */
	String BATCHCLASS = "batchClass";
	
	/**
	 * Initializing IDENTIFIER.
	 */
	String IDENTIFIER = "identifier";
	
	/**
	 * Initializing BATCH_CLASS_ID.
	 */
	String BATCH_CLASS_ID = "batchClass.id";
	
	/**
	 * Initializing NAME.
	 */
	String NAME = "name";
	
	/**
	 * Initializing BATCH_CLASS_IDENTIFIER.
	 */
	String BATCH_CLASS_IDENTIFIER = "batchClass.identifier";
	
	/**
	 * The APPLICATION_PROPERTIES {@link String} is a constant for path to application properties file.
	 */
	String APPLICATION_PROPERTIES = "META-INF/application.properties";

	/**
	 * The SUPER_ADMIN_GROUP_UPDATE {@link String} is a constant for property 'update_super_admin_group' used to set whether the
	 * super-admin group has been updated or not.
	 */
	String UPDATE_SUPER_ADMIN_GROUP = "update_super_admin_group";
	
	/**
	 * Initializing SPACE.
	 */
	String SPACE = " ";

	/**
	 * Initializing PERCENTAGE.
	 */
	String PERCENTAGE = "%";
	
	/**
	 * Initializing DOT.
	 */
	String DOT = ".";
	
	/**
	 * DEFAULT_PRIORITY int.
	 */
	int DEFAULT_PRIORITY = 99;
	
	/**
	 * PRIORITY int.
	 */
	int PRIORITY = 100;

	/**
	 * FINAL_DROP_FOLDER String.
	 */
	String FINAL_DROP_FOLDER = "Final-drop-folder";

	/**
	 * BATCH_EXPORT_TO_FOLDER String.
	 */
	String BATCH_EXPORT_TO_FOLDER = "batch.export_to_folder";

	/**
	 * COPY_BATCH_XML String.
	 */
	String COPY_BATCH_XML = "COPY_BATCH_XML";

	/**
	 * EXPORT String.
	 */
	String EXPORT = "Export";

	/**
	 * DCMA_BATCH String.
	 */
	String DCMA_BATCH = "dcma-batch" + File.separator + "dcma-batch";

	/**
	 * BATCH_BASE_FOLDER String.
	 */
	String BATCH_BASE_FOLDER = "batch.base_folder";

	/**
	 * INVALID_PLUGIN_NAME String.
	 */
	String INVALID_PLUGIN_NAME = "Invalid Plugin name.";
	
	/**
	 * BATCH_ALREADY_LOCKED String.
	 */
	String BATCH_ALREADY_LOCKED = "Batch already locked";
	
	/**
	 * DOCUMENT_TYPE_NAME_IS_NULL_OR_EMPTY String.
	 */
	String DOCUMENT_TYPE_NAME_IS_NULL_OR_EMPTY = "Document type name is null or empty.";
	
	/**
	 * KV_PAGE_PROCESS String.
	 */
	String KV_PAGE_PROCESS = "KV_Page_Process";
	
	/**
	 * PRIME_CONST int.
	 */
	int PRIME_CONST = 31;
}
