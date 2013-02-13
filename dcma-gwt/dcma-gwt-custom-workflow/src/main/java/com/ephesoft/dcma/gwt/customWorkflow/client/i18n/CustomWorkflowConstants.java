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

package com.ephesoft.dcma.gwt.customworkflow.client.i18n;

import com.ephesoft.dcma.gwt.core.client.i18n.LocaleCommonConstants;

public interface CustomWorkflowConstants extends LocaleCommonConstants {

	// Locale Constants
	String ADD_CONSTANT = "add_constant";

	String BACK_CONSTANT = "back_constant";

	String PLUGIN_NAMES_STRING = "plugin_names_string";

	String ADD_PLUGIN_STRING = "add_plugin_string";

	String SAVE_BUTTON = "save_button";

	String CANCEL_BUTTON = "cancel_button";

	String RESET_BUTTON = "reset_button";

	String IMPORT_FILE = "import_file";

	String DEPENDENCIES_CONSTANT = "dependencies_constant";

	String EDIT_CONSTANT = "edit_constant";

	String DELETE_CONSTANT = "delete_constant";

	String OK_CONSTANT = "ok_constant";

	String AND_CONSTANT = "and_constant";

	String OR_CONSTANT = "or_constant";

	String PLUGIN_LABEL_CONSTANT = "plugin_label_constant";

	String DEPENDENCY_TYPE_LABEL_CONSTANT = "dependency_type_label_constant";

	String DEPENDENCIES_LABEL_CONSTANT = "dependencies_label_constant";

	String HELP_BUTTON = "help_button";

	String PLUGIN_NAME_LABEL = "plugin_name_label";

	String APPLY_BUTTON = "apply_button";

	String NO_CONSTANT = "no";

	String YES = "yes";

	String DESCRIPTION = "description";

	String DEPENDENCY_TYPE = "dependency_type";

	String DEPENDENCIES = "dependencies";

	String PLUGINS_LIST = "plugins_list";

	String ADD_NEW_PLUGIN = "add_new_plugin";
	
	String DELETE_PLUGIN = "delete_plugin";
	
	String PLUGIN_DELETED_SUCCESSFULLY = "plugin_deleted_successfully";

	String DELETING_PLUGIN_MESSAGE = "deleting_plugin_wait";

	// Internal constants
	int SPACING_CONSTANT_10 = 10;

	int SPACING_CONSTANT_30 = 30;

	int SPACING_CONSTANT_60 = 60;

	int VISIBLE_COUNT_20 = 20;

	int INDEX_VALUE_0 = 0;

	int VISIBLE_COUNT_4 = 4;

	String FILE_SEPARATOR = "/";

	String STAR = "*";

	String ERROR_CODE_TEXT = "error";

	String DOUBLE_ARROW = " >> ";

	String BOLD_TEXT_STYLE = "bold_text";

	String XML_FILE_PATH = "xmlFilePath:";

	String JAR_FILE_PATH = "jarFilePath:";

	String PLUGIN_NAME = "pluginName:";

	String FONT_RED_STYLE = "font_red";

	String CAUSE = "cause:";

	String AND_SEPERATOR = ",";

	String OR_SEPERATOR = "/";

	String EMPTY_STRING = "";

	String NEXT_LINE = "\n";

	String NEW = "New";

	String _100 = "100%";

	String HELP_CONTENT = "helpContent";

	String TEXT_PANEL = "textPanel";

	String SUBPOINT_SPACING = "<td><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

	String ROW_END = "</p></td></tr><tr>";

	String ROW_SPACING = "<td><p><b>&nbsp;&nbsp;";

	String XML_FILE = ".Xml";

	String ZIP_FILE = ".Zip";

	String JAR_FILE = ".Jar";

	int TABLE_ROW_COUNT = 15;

	char EXT_SEPARATOR = '.';

	char SERVLET_RESULT_SEPERATOR = '|';

	String LAST_ATTACHED_ZIP_SOURCE_PATH = "lastAttachedZipSourcePath=";

	String ZIP = "zip";

	String ATTACH_FORM_ACTION = "dcma-gwt-custom-workflow/importPluginUploadServlet?";

	int SPACING_5 = 5;

	String STRONG_LABEL = "strong_label";

	String BUTTONS_PANEL_LAYOUT = "buttonsPanelLayout";

	String MAIN_PANEL_LAYOUT = "mainPanelLayout";

	String TWENTY_PIXEL = "20px";

	int VISIBLE_ITEM_10 = 10;

	int SPACING_15 = 15;

	String OPTION = "option";

	String CUSTOM_WORKFLOW_PANEL = "custom-workflow-panel";

	String _147PX = "147px";

	String _280PX = "280px";

	String _25PX = "25px";

	String _50PX = "50px";

	String HTML_LI_END = "</li>";

	String HTML_LI = " <li>";

	String SPACE = " ";

	String CURSOR_POINTER = "cursorHand";

	/**
	 * The CARRIAGE_RETURN {@link String} is a constant for carriage return appended by IE browser on next line.
	 */
	String CARRIAGE_RETURN = "\r";

	String BREAK_TAG_START = "<b>";
	
	String BREAK_TAG_END = "</b>";
	
	String AND_REPLACE_REGEX = "[,]{1}\\b|\\b[,]{1}";
	
	String OR_REPLACE_REGEX = "[/]{1}\\b|\\b[/]{1}";
	
	String BOUNDARY = "\\b";
}
