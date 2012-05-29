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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.ViewType;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.view.BatchClassBreadCrumbView;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDynamicPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginDTO;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.EmailConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.FunctionKeyDTO;
import com.ephesoft.dcma.gwt.core.shared.KVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.RegexDTO;
import com.ephesoft.dcma.gwt.core.shared.TableColumnInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.google.gwt.event.shared.HandlerManager;

public class BatchClassBreadCrumbPresenter extends AbstractBatchClassPresenter<BatchClassBreadCrumbView> {

	private static final String OPENING_BRACKET = " [";
	private static final String CLOSING_BRACKET = "] ";
	public static final String BATCH_CLASS_LISTING = ViewType.BATCH_CLASS_LISTING.getValue();
	public static final String CONFIGURE = ViewType.KV_PP_PLUGIN_CONFIG.getValue();

	public BatchClassBreadCrumbPresenter(BatchClassManagementController controller, BatchClassBreadCrumbView view) {
		super(controller, view);
	}

	@Override
	public void bind() {
		// Processing to be done when this presenter loads.
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// event handling should be done here.
	}

	public void createBreadCrumb() {
		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null));
	}

	public void createBreadCrumb(BatchClassDTO batchClassDTO) {

		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, batchClassDTO.getDescription() + OPENING_BRACKET
						+ batchClassDTO.getIdentifier() + CLOSING_BRACKET, batchClassDTO.getIdentifier()));
	}

	public void createBreadCrumb(FunctionKeyDTO functionKeyDTO) {
		DocumentTypeDTO documentTypeDTO = functionKeyDTO.getDocTypeDTO();
		BatchClassDTO batchClass = documentTypeDTO.getBatchClass();

		String functionKeyName = functionKeyDTO.getShortcutKeyName();
		if (functionKeyName == null || functionKeyName.isEmpty()) {
			functionKeyName = "";
		}
		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, batchClass.getDescription() + OPENING_BRACKET
						+ batchClass.getIdentifier() + CLOSING_BRACKET, batchClass.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.DOCUMENT_TYPE, documentTypeDTO.getName(), documentTypeDTO
						.getIdentifier()), new BatchClassBreadCrumbView.BreadCrumbView(ViewType.FUNCTION_KEY, functionKeyName,
						documentTypeDTO.getIdentifier()));

	}

	public void createBreadCrumb(KVExtractionDTO kvExtractionDTO) {
		BatchClassDTO batchClassDTO = kvExtractionDTO.getFieldTypeDTO().getDocTypeDTO().getBatchClass();
		DocumentTypeDTO documentTypeDTO = kvExtractionDTO.getFieldTypeDTO().getDocTypeDTO();
		String KVName = AdminConstants.KV_EXTRACTION;
		if (kvExtractionDTO.isNew()) {
			KVName = AdminConstants.NEW_KV_EXTRACTION;
		}
		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, batchClassDTO.getDescription() + OPENING_BRACKET
						+ batchClassDTO.getIdentifier() + CLOSING_BRACKET, batchClassDTO.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.DOCUMENT_TYPE, documentTypeDTO.getName(), documentTypeDTO
						.getIdentifier()), new BatchClassBreadCrumbView.BreadCrumbView(ViewType.DOCUMENT_LEVEL_FIELD, kvExtractionDTO
						.getFieldTypeDTO().getName(), kvExtractionDTO.getFieldTypeDTO().getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.KV_EXTRACTION, KVName, kvExtractionDTO.getIdentifier()));
	}

	public void createBreadCrumb(RegexDTO regexDTO) {
		BatchClassDTO batchClassDTO = regexDTO.getFieldTypeDTO().getDocTypeDTO().getBatchClass();
		DocumentTypeDTO documentTypeDTO = regexDTO.getFieldTypeDTO().getDocTypeDTO();
		String name = AdminConstants.REGEX;
		if (regexDTO.isNew()) {
			name = AdminConstants.NEW_REGEX;
		}
		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, batchClassDTO.getDescription() + OPENING_BRACKET
						+ batchClassDTO.getIdentifier() + CLOSING_BRACKET, batchClassDTO.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.DOCUMENT_TYPE, documentTypeDTO.getName(), documentTypeDTO
						.getIdentifier()), new BatchClassBreadCrumbView.BreadCrumbView(ViewType.DOCUMENT_LEVEL_FIELD, regexDTO
						.getFieldTypeDTO().getName(), regexDTO.getFieldTypeDTO().getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.REGEX, name, regexDTO.getIdentifier()));
	}

	public void createBreadCrumb(BatchClassModuleDTO moduleDTO) {
		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, moduleDTO.getBatchClass().getDescription()
						+ OPENING_BRACKET + moduleDTO.getBatchClass().getIdentifier() + CLOSING_BRACKET, moduleDTO.getBatchClass()
						.getIdentifier()), new BatchClassBreadCrumbView.BreadCrumbView(ViewType.MODULE, moduleDTO.getModule()
						.getName(), moduleDTO.getIdentifier()));
	}

	public void createBreadCrumb(DocumentTypeDTO documentTypeDTO) {
		String documentTypeName = documentTypeDTO.getName();
		if (documentTypeName == null || documentTypeName.length() == 0) {
			documentTypeName = AdminConstants.NEW_DOCUMENT;
		}
		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, documentTypeDTO.getBatchClass().getDescription()
						+ OPENING_BRACKET + documentTypeDTO.getBatchClass().getIdentifier() + CLOSING_BRACKET, documentTypeDTO
						.getBatchClass().getIdentifier()), new BatchClassBreadCrumbView.BreadCrumbView(ViewType.DOCUMENT_TYPE,
						documentTypeName, documentTypeDTO.getIdentifier()));
	}

	public void createBreadCrumb(EmailConfigurationDTO emailConfigurationDTO) {
		String emailName = emailConfigurationDTO.getUserName();
		if (emailName == null || emailName.length() == 0) {
			emailName = AdminConstants.NEW_EMAIL;
		}
		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, emailConfigurationDTO.getBatchClass()
						.getDescription()
						+ OPENING_BRACKET + emailConfigurationDTO.getBatchClass().getIdentifier() + CLOSING_BRACKET,
						emailConfigurationDTO.getBatchClass().getIdentifier()), new BatchClassBreadCrumbView.BreadCrumbView(
						ViewType.EMAIL, emailName, emailConfigurationDTO.getIdentifier()));
	}

	public void createBreadCrumb(BatchClassFieldDTO batchClassFieldDTO) {
		String name = batchClassFieldDTO.getName();
		if (name == null || name.length() == 0) {
			name = LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.ADD_BATCH_CLASS_FIELD_TITLE);
		}
		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, batchClassFieldDTO.getBatchClass().getDescription()
						+ OPENING_BRACKET + batchClassFieldDTO.getBatchClass().getIdentifier() + CLOSING_BRACKET, batchClassFieldDTO
						.getBatchClass().getIdentifier()), new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_FIELD,
						name, batchClassFieldDTO.getIdentifier()));
	}

	public void createBreadCrumb(BatchClassPluginDTO batchClassPluginDTO) {
		BatchClassModuleDTO module = batchClassPluginDTO.getBatchClassModule();
		BatchClassDTO batchClass = module.getBatchClass();

		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, batchClass.getDescription() + OPENING_BRACKET
						+ batchClass.getIdentifier() + CLOSING_BRACKET, batchClass.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.MODULE, module.getModule().getName(), module.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.PLUGIN, batchClassPluginDTO.getPlugin().getPluginName(),
						batchClassPluginDTO.getIdentifier()));
	}

	public void createBreadCrumb(BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO) {
		BatchClassPluginDTO batchClassPluginDTO = batchClassDynamicPluginConfigDTO.getBatchClassPlugin();
		BatchClassModuleDTO module = batchClassPluginDTO.getBatchClassModule();
		BatchClassDTO batchClass = module.getBatchClass();

		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, batchClass.getDescription() + OPENING_BRACKET
						+ batchClass.getIdentifier() + CLOSING_BRACKET, batchClass.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.MODULE, module.getModule().getName(), module.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.FUZZY_DB, batchClassPluginDTO.getPlugin().getPluginName(),
						batchClassPluginDTO.getIdentifier()), new BatchClassBreadCrumbView.BreadCrumbView(ViewType.DATABASE_MAPPING,
						AdminConstants.DATABASE_MAPPING, batchClassDynamicPluginConfigDTO.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.TABLE_MAPPING, AdminConstants.TABLE_MAPPING, batchClass
						.getIdentifier()));
	}

	public void createBreadCrumbForDocumentType() {
		BatchClassPluginDTO batchClassPluginDTO = controller.getSelectedPlugin();
		BatchClassModuleDTO module = batchClassPluginDTO.getBatchClassModule();
		BatchClassDTO batchClass = module.getBatchClass();

		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, batchClass.getDescription() + OPENING_BRACKET
						+ batchClass.getIdentifier() + CLOSING_BRACKET, batchClass.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.MODULE, module.getModule().getName(), module.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.FUZZY_DB, batchClassPluginDTO.getPlugin().getPluginName(),
						batchClassPluginDTO.getIdentifier()), new BatchClassBreadCrumbView.BreadCrumbView(ViewType.DATABASE_MAPPING,
						AdminConstants.DATABASE_MAPPING, batchClass.getIdentifier()));
	}

	public void createBreadCrumb(FieldTypeDTO fieldTypeDTO) {
		DocumentTypeDTO documentTypeDTO = fieldTypeDTO.getDocTypeDTO();
		BatchClassDTO batchClass = documentTypeDTO.getBatchClass();
		String fieldTypeName = fieldTypeDTO.getName();
		if (fieldTypeName == null || fieldTypeName.length() == 0) {
			fieldTypeName = AdminConstants.NEW_FIELD_TYPE;
		}

		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, batchClass.getDescription() + OPENING_BRACKET
						+ batchClass.getIdentifier() + CLOSING_BRACKET, batchClass.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.DOCUMENT_TYPE, documentTypeDTO.getName(), documentTypeDTO
						.getIdentifier()), new BatchClassBreadCrumbView.BreadCrumbView(ViewType.DOCUMENT_LEVEL_FIELD, fieldTypeName,
						documentTypeDTO.getIdentifier()));
	}

	public void createBreadCrumb(TableInfoDTO tableInfoDTO) {
		DocumentTypeDTO documentTypeDTO = tableInfoDTO.getDocTypeDTO();
		BatchClassDTO batchClass = documentTypeDTO.getBatchClass();
		String tableInfoName = tableInfoDTO.getName();
		if (tableInfoName == null || tableInfoName.length() == 0) {
			tableInfoName = AdminConstants.NEW_TABLE_INFO_TYPE;
		}

		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, batchClass.getDescription() + OPENING_BRACKET
						+ batchClass.getIdentifier() + CLOSING_BRACKET, batchClass.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.DOCUMENT_TYPE, documentTypeDTO.getName(), documentTypeDTO
						.getIdentifier()), new BatchClassBreadCrumbView.BreadCrumbView(ViewType.TABLE_INFO, tableInfoName,
						documentTypeDTO.getIdentifier()));
	}

	public void createBreadCrumb(TableColumnInfoDTO tableColumnInfoDTO) {
		TableInfoDTO tableInfoDTO = tableColumnInfoDTO.getTableInfoDTO();
		DocumentTypeDTO documentTypeDTO = tableInfoDTO.getDocTypeDTO();
		BatchClassDTO batchClass = documentTypeDTO.getBatchClass();

		String tableColumnInfoName = tableColumnInfoDTO.getColumnName();
		if (tableColumnInfoName == null || tableColumnInfoName.length() == 0) {
			tableColumnInfoName = AdminConstants.NEW_TABLE_COLUMN_INFO_TYPE;
		}

		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, batchClass.getDescription() + OPENING_BRACKET
						+ batchClass.getIdentifier() + CLOSING_BRACKET, batchClass.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.DOCUMENT_TYPE, documentTypeDTO.getName(), documentTypeDTO
						.getIdentifier()), new BatchClassBreadCrumbView.BreadCrumbView(ViewType.TABLE_INFO, tableColumnInfoDTO
						.getTableInfoDTO().getName(), tableColumnInfoDTO.getTableInfoDTO().getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.TABLE_COLUMN_INFO, tableColumnInfoName, tableColumnInfoDTO
						.getIdentifier()));
	}

	public void createBreadCrumbForKVPPPlugin(BatchClassPluginConfigDTO batchClassPluginConfigDTO) {
		BatchClassPluginDTO batchClassPluginDTO = batchClassPluginConfigDTO.getBatchClassPlugin();
		BatchClassModuleDTO module = batchClassPluginDTO.getBatchClassModule();
		BatchClassDTO batchClass = module.getBatchClass();

		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, batchClass.getDescription() + OPENING_BRACKET
						+ batchClass.getIdentifier() + CLOSING_BRACKET, batchClass.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.MODULE, module.getModule().getName(), module.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.KV_PP_PLUGIN, batchClassPluginDTO.getPlugin().getPluginName(),
						batchClassPluginDTO.getIdentifier()), new BatchClassBreadCrumbView.BreadCrumbView(
						ViewType.KV_PP_PLUGIN_CONFIG, CONFIGURE, null), new BatchClassBreadCrumbView.BreadCrumbView(
						ViewType.KV_PP_PLUGIN_CONFIG_ADD_EDIT, batchClassPluginConfigDTO.getName(), batchClassPluginConfigDTO
								.getIdentifier()));
	}

	public void createBreadCrumbForKVPPPluginConfig(BatchClassPluginDTO batchClassPluginDTO) {
		BatchClassModuleDTO module = batchClassPluginDTO.getBatchClassModule();
		BatchClassDTO batchClass = module.getBatchClass();

		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, batchClass.getDescription() + OPENING_BRACKET
						+ batchClass.getIdentifier() + CLOSING_BRACKET, batchClass.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.MODULE, module.getModule().getName(), module.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.KV_PP_PLUGIN, batchClassPluginDTO.getPlugin().getPluginName(),
						batchClassPluginDTO.getIdentifier()), new BatchClassBreadCrumbView.BreadCrumbView(
						ViewType.KV_PP_PLUGIN_CONFIG, CONFIGURE, null));
	}

	public void createBreadCrumbForModules() {
		BatchClassDTO batchClass = controller.getBatchClass();
		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, batchClass.getDescription() + OPENING_BRACKET
						+ batchClass.getIdentifier() + CLOSING_BRACKET, batchClass.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.CONFIGURE_MODULE, ViewType.CONFIGURE_MODULE.getValue(),
						batchClass.getIdentifier()));

	}

	public void createBreadCrumbForPluginsSelect(String moduleIdentifier) {
		BatchClassDTO batchClass = controller.getBatchClass();
		String moduleName = controller.getBatchClass().getModuleByIdentifier(moduleIdentifier).getModule().getName();
		view.create(new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS_LISTING, BATCH_CLASS_LISTING, null),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.BATCH_CLASS, batchClass.getDescription() + OPENING_BRACKET
						+ batchClass.getIdentifier() + CLOSING_BRACKET, batchClass.getIdentifier()),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.MODULE, moduleName, moduleIdentifier),
				new BatchClassBreadCrumbView.BreadCrumbView(ViewType.CONFIGURE_PLUGIN, ViewType.CONFIGURE_PLUGIN.getValue(),
						batchClass.getIdentifier()));

	}
}
