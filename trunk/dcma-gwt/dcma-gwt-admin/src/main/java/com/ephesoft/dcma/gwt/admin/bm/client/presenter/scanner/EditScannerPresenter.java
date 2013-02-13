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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.scanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.scanner.EditScannerView;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.WebScannerConfigurationDTO;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The presenter for view that shows the edit scanner details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class EditScannerPresenter extends AbstractBatchClassPresenter<EditScannerView> {

	/**
	 * elementMap Map<String, Widget>.
	 */
	private Map<String, Widget> elementMap;

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view EditScannerView
	 */
	public EditScannerPresenter(BatchClassManagementController controller, EditScannerView view) {
		super(controller, view);
	}

	/**
	 * In case of cancel click.
	 */
	public void onCancel() {
		if (controller.isAdd()) {
			controller.getMainPresenter().showBatchClassView(controller.getBatchClass());
			controller.setAdd(false);
		} else {
			controller.getMainPresenter().getScannerViewPresenter().showScannerView();
		}
	}

	/**
	 * In case of save click.
	 */
	public void onSave() {
		boolean fieldsValid = true;

		for (int index = 0; index < docFieldWidgets.size(); index++) {
			if (docFieldWidgets.get(index).isValidatable()) {
				if (!docFieldWidgets.get(index).isListBox()) {
					String textBoxValue = docFieldWidgets.get(index).getTextBoxWidget().getWidget().getText();
					// if (!docFieldWidgets.get(index).getTextBoxWidget().validate()) {
					if (docFieldWidgets.get(index).isMandatory()) {// mandatory fields
						if ((textBoxValue != null && textBoxValue.isEmpty())) {
							ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.MANDATORY_FIELDS_ERROR_MSG);
							fieldsValid = false;
							break;
						} else if (textBoxValue != null && !docFieldWidgets.get(index).getTextBoxWidget().validate()) {
							ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.FIELD_NOT_VALID_ERROR_MSG);
							fieldsValid = false;
							break;
						}
					} else {// non-mandatory fields
						if (!(textBoxValue != null && textBoxValue.isEmpty())) {
							ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.FIELD_NOT_VALID_ERROR_MSG);
							fieldsValid = false;
							break;
						}
					}
					// }
				} else {
					if (docFieldWidgets.get(index).getListBoxwidget().getSelectedIndex() == -1) {
						ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.MANDATORY_FIELDS_ERROR_MSG);
						fieldsValid = false;
						break;
					}
				}
			}
		}

		// Check for duplicate profile name.
		TextBox tProfile = (TextBox) (elementMap.get(controller.getSelectedWebScannerConfiguration().getName()));
		String profileValue = tProfile.getValue();
		String scannerIdentifier = controller.getSelectedWebScannerConfiguration().getIdentifier();
		Collection<WebScannerConfigurationDTO> configurations = controller.getBatchClass().getScannerConfiguration();
		for (WebScannerConfigurationDTO configurationDTO : configurations) {
			if (configurationDTO.getParent() == null && !configurationDTO.getIdentifier().equals(scannerIdentifier)
					&& profileValue.equalsIgnoreCase(configurationDTO.getValue())) {
				ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.NON_UNIQUE_PROFILE_ERROR_MSG);
				fieldsValid = false;
				break;
			}
		}
		if (fieldsValid) {
			if (controller.isAdd()) {
				controller.getBatchClass().addScannerConfiguration(controller.getSelectedWebScannerConfiguration());
				controller.setAdd(false);
			}
			// iterate over the docfield widgets to populate the values
			Collection<WebScannerConfigurationDTO> children = new ArrayList<WebScannerConfigurationDTO>();
			for (int index = 0; index < docFieldWidgets.size(); index++) {

				setWebScannerConfigProperties(children, index);
			}
			controller.getMainPresenter().getScannerViewPresenter().bind();
			controller.getMainPresenter().getScannerViewPresenter().showScannerView();
			if (controller.getSelectedWebScannerConfiguration().isNew()) {
				controller.getMainPresenter().getBatchClassBreadCrumbPresenter().createBreadCrumb(
						controller.getSelectedWebScannerConfiguration());
			}
		}
	}

	private void setWebScannerConfigProperties(Collection<WebScannerConfigurationDTO> children, int index) {
		if (!docFieldWidgets.get(index).isListBox()) {
			// textbox
			TextBox textBox = (TextBox) (elementMap.get(docFieldWidgets.get(index).getFieldName()));
			if (controller.isAdd()) {
				WebScannerConfigurationDTO dto = new WebScannerConfigurationDTO();
				dto.setName(docFieldWidgets.get(index).getFieldName());
				dto.setValue(textBox.getValue());
				children.add(dto);
			} else {
				if (controller.getSelectedWebScannerConfiguration().getName().equalsIgnoreCase(
						docFieldWidgets.get(index).getFieldName())) {
					controller.getSelectedWebScannerConfiguration().setValue(textBox.getValue());
				} else {
					for (WebScannerConfigurationDTO webScannerConfigurationDTO : controller.getSelectedWebScannerConfiguration()
							.getChildren()) {
						if (webScannerConfigurationDTO.getName().equalsIgnoreCase(docFieldWidgets.get(index).getFieldName())) {
							webScannerConfigurationDTO.setValue(textBox.getValue());
							break;
						}
					}
				}
			}
		} else {
			// listbox
			ListBox listBox = (ListBox) (elementMap.get(docFieldWidgets.get(index).getFieldName()));
			if (controller.isAdd()) {
				WebScannerConfigurationDTO dto = new WebScannerConfigurationDTO();
				dto.setName(docFieldWidgets.get(index).getFieldName());
				dto.setValue(listBox.getValue(listBox.getSelectedIndex()));
				children.add(dto);

			} else {
				if (controller.getSelectedWebScannerConfiguration().getName().equalsIgnoreCase(
						docFieldWidgets.get(index).getFieldName())) {
					controller.getSelectedWebScannerConfiguration().setValue(listBox.getValue(listBox.getSelectedIndex()));
				} else {
					for (WebScannerConfigurationDTO webScannerConfigurationDTO : controller.getSelectedWebScannerConfiguration()
							.getChildren()) {
						if (webScannerConfigurationDTO.getName().equalsIgnoreCase(docFieldWidgets.get(index).getFieldName())) {
							webScannerConfigurationDTO.setValue(listBox.getValue(listBox.getSelectedIndex()));
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * To add drop down.
	 * 
	 * @param row int
	 * @param sampleValueList List<String>
	 * @param selectedValue String
	 * @return ListBox
	 */
	public ListBox addDropDown(int row, List<String> sampleValueList, String selectedValue) {
		ListBox fieldValue = new ListBox();
		fieldValue.setVisibleItemCount(1);
		for (String item : sampleValueList) {
			fieldValue.addItem(item);
		}
		fieldValue.setItemSelected(sampleValueList.indexOf(selectedValue), true);
		return fieldValue;
	}

	/**
	 * Class for edit widget.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	private static class EditableWidgetStorage {

		/**
		 * fieldName String.
		 */
		private String fieldName;

		/**
		 * widget ValidatableWidget<TextBox>.
		 */
		private final ValidatableWidget<TextBox> widget;

		/**
		 * listBox boolean.
		 */
		private final boolean listBox;

		/**
		 * listBoxwidget ListBox.
		 */
		private final ListBox listBoxwidget;

		/**
		 * validatable boolean.
		 */
		private boolean validatable;

		/**
		 * mandatory boolean.
		 */
		private boolean mandatory;

		/**
		 * Constructor.
		 * 
		 * @param widget ValidatableWidget<TextBox>
		 */
		public EditableWidgetStorage(ValidatableWidget<TextBox> widget) {
			this(widget, false, null, true);
		}

		/**
		 * Constructor.
		 * 
		 * @param listBoxwidget ListBox
		 */
		public EditableWidgetStorage(ListBox listBoxwidget) {
			this(null, true, listBoxwidget, true);
		}

		/**
		 * Constructor.
		 * 
		 * @param widget ValidatableWidget<TextBox>
		 * @param listBox boolean
		 * @param listBoxwidget ListBox
		 * @param validatable boolean
		 */
		public EditableWidgetStorage(ValidatableWidget<TextBox> widget, boolean listBox, ListBox listBoxwidget, boolean validatable) {
			super();
			this.widget = widget;
			this.listBox = listBox;
			this.listBoxwidget = listBoxwidget;
			this.validatable = validatable;
		}

		/**
		 * To get Text Box Widget.
		 * 
		 * @return ValidatableWidget<TextBox>
		 */
		public ValidatableWidget<TextBox> getTextBoxWidget() {
			return widget;
		}

		/**
		 * To get List Box widget.
		 * 
		 * @return ListBox
		 */
		public ListBox getListBoxwidget() {
			return listBoxwidget;
		}

		/**
		 * To check whether list box.
		 * 
		 * @return boolean
		 */
		public boolean isListBox() {
			return listBox;
		}

		/**
		 * To check whether validatable.
		 * 
		 * @return boolean
		 */
		public boolean isValidatable() {
			return validatable;
		}

		/**
		 * To set validatable.
		 * 
		 * @param isValidatable boolean
		 */
		public void setValidatable(boolean isValidatable) {
			this.validatable = isValidatable;
		}

		/**
		 * To set mandatory.
		 * 
		 * @param isMandatory boolean
		 */
		public void setMandatory(boolean isMandatory) {
			this.mandatory = isMandatory;
		}

		/**
		 * To check whether mandatory or not.
		 * 
		 * @return boolean
		 */
		public boolean isMandatory() {
			return mandatory;
		}

		/**
		 * To get field name.
		 * 
		 * @return String
		 */
		public String getFieldName() {
			return fieldName;
		}

		/**
		 * To set field name.
		 * 
		 * @param fieldName String
		 */
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
	}

	/**
	 * docFieldWidgets List<EditableWidgetStorage>.
	 */
	private List<EditableWidgetStorage> docFieldWidgets;

	private void setProperties() {
		docFieldWidgets = new ArrayList<EditableWidgetStorage>();
		int row = 0;
		Collection<WebScannerConfigurationDTO> values = controller.getSelectedWebScannerConfiguration().getChildren();
		if (values != null) {
			elementMap = new HashMap<String, Widget>();

			// handling for profile name field
			view.formatRow(row);
			view.addWidget(row, 0, new Label(controller.getSelectedWebScannerConfiguration().getDescription()
					+ BatchClassManagementConstants.COLON));

			// Create a text box
			ValidatableWidget<TextBox> validatableTextBox = view.addTextBox(row, controller.getSelectedWebScannerConfiguration()
					.getValue(), controller.getSelectedWebScannerConfiguration().getDataType(), false, controller
					.getSelectedWebScannerConfiguration().isMandatory(), controller.getSelectedWebScannerConfiguration().getName(),
					elementMap);
			view.addWidget(row, 2, validatableTextBox.getWidget());
			EditableWidgetStorage editableWidgetStorage = new EditableWidgetStorage(validatableTextBox);
			editableWidgetStorage.setValidatable(Boolean.TRUE);
			view.addWidgetStar(row, 1);
			if (controller.getSelectedWebScannerConfiguration().isMandatory()) {
				editableWidgetStorage.setMandatory(true);
			}
			editableWidgetStorage.setFieldName(controller.getSelectedWebScannerConfiguration().getName());
			docFieldWidgets.add(editableWidgetStorage);
			row++;

			for (WebScannerConfigurationDTO dto : values) {
				view.formatRow(row);
				view.addWidget(row, 0, new Label(dto.getDescription() + BatchClassManagementConstants.COLON));

				if (dto.isMandatory()) {
					view.addWidgetStar(row, 1);
				}

				if (dto.getSampleValue() != null && (!dto.getSampleValue().isEmpty())) {
					if (dto.getSampleValue().size() > 1) {
						List<String> sampleValues = dto.getSampleValue();
						// Create a drop down
						ListBox fieldValue = view.addDropDown(row, sampleValues, dto.getValue(), dto.getName(), elementMap);
						view.addWidget(row, 2, fieldValue);
						EditableWidgetStorage editableWidgetStorageChild = new EditableWidgetStorage(fieldValue);
						editableWidgetStorageChild.setValidatable(Boolean.FALSE);
						editableWidgetStorageChild.setFieldName(dto.getName());
						docFieldWidgets.add(editableWidgetStorageChild);
					}
				} else {

					// Create a text box
					ValidatableWidget<TextBox> validatableTextBoxChild = view.addTextBox(row, dto.getValue(), dto.getDataType(),
							false, dto.isMandatory(), dto.getName(), elementMap);
					view.addWidget(row, 2, validatableTextBoxChild.getWidget());
					EditableWidgetStorage editableWidgetStorageChild = new EditableWidgetStorage(validatableTextBoxChild);
					editableWidgetStorageChild.setValidatable(Boolean.TRUE);
					if (dto.isMandatory()) {
						editableWidgetStorageChild.setMandatory(true);
					}
					editableWidgetStorageChild.setFieldName(dto.getName());
					docFieldWidgets.add(editableWidgetStorageChild);
				}
				row++;
			}
		}
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		if (controller.getSelectedWebScannerConfiguration() == null) {
			WebScannerConfigurationDTO configDTO = controller.getMainPresenter().getBatchClassViewPresenter()
					.createScannerConfigurationDTOObject();
			// controller.setAdd(true);
			controller.setSelectedWebScannerConfiguration(configDTO);
		}

		view.setView();
		FlexTable dataTable = view.getDataTable();
		dataTable.getColumnFormatter().setWidth(0, "40%");
		dataTable.getColumnFormatter().setWidth(1, "1%");
		dataTable.getColumnFormatter().setWidth(2, "59%");
		dataTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		setProperties();
		if (docFieldWidgets.get(0) != null) {
			docFieldWidgets.get(0).getTextBoxWidget().getWidget().setFocus(true);
		}
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		// Event handling is done here.
	}

}
