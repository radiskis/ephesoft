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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.PluginNameConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.plugin.EditPluginView;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class EditPluginPresenter extends AbstractBatchClassPresenter<EditPluginView> {

	private List<EditableWidgetStorage> docFieldWidgets;

	public static final int MAX_VISIBLE_ITEM_COUNT = 4;

	public EditPluginPresenter(BatchClassManagementController controller, EditPluginView view) {
		super(controller, view);
	}

	public void onCancel() {
		controller.getMainPresenter().getPluginViewPresenter().bind();
		controller.getMainPresenter().getPluginViewPresenter().showPluginViewDetail();
	}

	public void onSave() {
		boolean fieldsValid = false;
		Collection<BatchClassPluginConfigDTO> values = controller.getSelectedPlugin().getBatchClassPluginConfigs();
		for (int index = 0; index < docFieldWidgets.size(); index++) {
			if (docFieldWidgets.get(index).isValidatable()) {
				if (!docFieldWidgets.get(index).isListBox()) {
					if (!docFieldWidgets.get(index).getTextBoxWidget().validate()) {
						ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.MANDATORY_FIELDS_ERROR_MSG);
						fieldsValid = true;
						break;
					}
				} else {
					if (docFieldWidgets.get(index).getListBoxwidget().getSelectedIndex() == -1) {
						ConfirmationDialogUtil.showConfirmationDialogError(MessageConstants.MANDATORY_FIELDS_ERROR_MSG);
						fieldsValid = true;
						break;
					}

				}
			}
		}
		if (!fieldsValid) {
			int index = 0;
			for (BatchClassPluginConfigDTO batchClassPluginConfigDTO : values) {
				if (docFieldWidgets.get(index).isListBox) {
					if (docFieldWidgets.get(index).getListBoxwidget().isMultipleSelect()) {
						StringBuffer selectedItem = new StringBuffer();
						Integer numberOfItemSelected = docFieldWidgets.get(index).getListBoxwidget().getItemCount();
						for (int i = 0; i < numberOfItemSelected; i++) {
							if (docFieldWidgets.get(index).getListBoxwidget().isItemSelected(i)) {
								selectedItem.append(docFieldWidgets.get(index).getListBoxwidget().getItemText(i)).append(';');
							}
						}
						String selectedItemString = selectedItem.toString();
						selectedItemString = selectedItemString.substring(0, selectedItemString.length() - 1);
						batchClassPluginConfigDTO.setValue(selectedItemString);
					}

					else {
						batchClassPluginConfigDTO.setValue(docFieldWidgets.get(index).getListBoxwidget().getItemText(
								docFieldWidgets.get(index).getListBoxwidget().getSelectedIndex()));
					}
				} else {
					batchClassPluginConfigDTO.setValue(docFieldWidgets.get(index).getTextBoxWidget().getWidget().getValue());
				}
				index++;
			}

			controller.getMainPresenter().getPluginViewPresenter().bind();
			controller.getMainPresenter().getPluginViewPresenter().showPluginViewDetail();

		}
	}

	public void setProperties() {
		ListBox hocrToPdfListBox = null;
		docFieldWidgets = new ArrayList<EditableWidgetStorage>();
		int row = 0;
		Collection<BatchClassPluginConfigDTO> values = controller.getSelectedPlugin().getBatchClassPluginConfigs();
		if (values != null) {
			for (final BatchClassPluginConfigDTO batchClassPluginConfig : values) {
				view.formatRow(row);
				view.addWidget(row, 0, new Label(batchClassPluginConfig.getDescription() + ":"));

				if (!batchClassPluginConfig.getName().equals(PluginNameConstants.URL1)
						&& !batchClassPluginConfig.getName().equals(PluginNameConstants.URL2)
						&& !batchClassPluginConfig.getName().equals(PluginNameConstants.URL3)
						&& !batchClassPluginConfig.getName().equals(PluginNameConstants.URL4)
						&& !batchClassPluginConfig.getName().equals(PluginNameConstants.CONVERT_INPUT_IMAGE_PARAMETERS)
						&& !batchClassPluginConfig.getName().equals(PluginNameConstants.CONVERT_OUTPUT_IMAGE_PARAMETERS)
						&& !batchClassPluginConfig.getName().equals(PluginNameConstants.FILEBOUND_DIVIVSION)
						&& !batchClassPluginConfig.getName().equals(PluginNameConstants.FILEBOUND_SEPARATOR)) {
					view.addWidgetStar(row, 1);
				}
				if (batchClassPluginConfig.getSampleValue() != null && !batchClassPluginConfig.getSampleValue().isEmpty()) {
					if (batchClassPluginConfig.getSampleValue().size() > 1) {
						// Create a listBox
						if (batchClassPluginConfig.isMultivalue()) {
							// Create a multiple select list box
							List<String> sampleValueList = batchClassPluginConfig.getSampleValue();
							int max_visible_item_count = MAX_VISIBLE_ITEM_COUNT;
							if (sampleValueList.size() < MAX_VISIBLE_ITEM_COUNT) {
								max_visible_item_count = sampleValueList.size();
							}
							ListBox fieldValue = view.addMultipleSelectListBox(row, sampleValueList, max_visible_item_count,
									batchClassPluginConfig.getValue());
							view.addWidget(row, 2, fieldValue);
							docFieldWidgets.add(new EditableWidgetStorage(fieldValue));
						} else {
							// Create a drop down
							final ListBox fieldValue = view.addDropDown(row, batchClassPluginConfig.getSampleValue(),
									batchClassPluginConfig.getValue());
							fieldValue.setName(batchClassPluginConfig.getPluginConfig().getFieldName());
							if (batchClassPluginConfig.getPluginConfig().getFieldName().equalsIgnoreCase(
									PluginNameConstants.EXPORT_PLUGIN_TYPE_DESC)) {
								hocrToPdfListBox = fieldValue;
								fieldValue.addChangeHandler(new ChangeHandler() {

									@Override
									public void onChange(ChangeEvent arg0) {
										enableHocrToPdfProps(fieldValue);
									}
								});

							}
							view.addWidget(row, 2, fieldValue);
							EditableWidgetStorage editableWidgetStorage = new EditableWidgetStorage(fieldValue);
							editableWidgetStorage.setValidatable(Boolean.FALSE);
							docFieldWidgets.add(editableWidgetStorage);
						}

					} else {
						// Create a read only text box
						ValidatableWidget<TextBox> validatableTextBox = view.addTextBox(row, batchClassPluginConfig, true);
						view.addWidget(row, 2, validatableTextBox.getWidget());
						EditableWidgetStorage editableWidgetStorage = new EditableWidgetStorage(validatableTextBox);
						editableWidgetStorage.setValidatable(Boolean.FALSE);
						docFieldWidgets.add(editableWidgetStorage);
					}
				} else {
					// Create a text box
					ValidatableWidget<TextBox> validatableTextBox = view.addTextBox(row, batchClassPluginConfig, false);
					view.addWidget(row, 2, validatableTextBox.getWidget());
					docFieldWidgets.add(new EditableWidgetStorage(validatableTextBox));
				}
				row++;
			}
			row++;
			view.addButtons(row);
		}
		enableHocrToPdfProps(hocrToPdfListBox);
	}

	private static class EditableWidgetStorage {

		private ValidatableWidget<TextBox> widget;
		private  boolean isListBox;
		private ListBox listBoxwidget;
		private boolean isValidatable;

		public EditableWidgetStorage(ValidatableWidget<TextBox> widget) {
			this.widget = widget;
			this.isListBox = false;
			this.isValidatable = true;
		}

		public EditableWidgetStorage(ListBox listBoxwidget) {
			this.listBoxwidget = listBoxwidget;
			this.isListBox = true;
			this.isValidatable = true;
		}

		public ValidatableWidget<TextBox> getTextBoxWidget() {
			return widget;
		}

		public ListBox getListBoxwidget() {
			return listBoxwidget;
		}

		public boolean isListBox() {
			return isListBox;
		}

		public boolean isValidatable() {
			return isValidatable;
		}

		public void setValidatable(boolean isValidatable) {
			this.isValidatable = isValidatable;
		}
	}

	@Override
	public void bind() {
		if (controller.getSelectedPlugin() != null) {
			view.setView();
			setProperties();
		}
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		// to be used in case of event handling
	}

	private void enableHocrToPdfProps(final ListBox fieldValue) {
		if (fieldValue != null) {
			for (EditableWidgetStorage editableWidgetStorage : docFieldWidgets) {
				if (fieldValue.getItemText(fieldValue.getSelectedIndex()).equalsIgnoreCase(
						PluginNameConstants.IMAGE_MAGIC_PLUGIN_CONF_DESC)) {
					if (editableWidgetStorage != null
							&& editableWidgetStorage.getListBoxwidget() != null
							&& editableWidgetStorage.getListBoxwidget().getName() != null
							&& editableWidgetStorage.getListBoxwidget().getName()
									.equals(PluginNameConstants.EXPORT_COLORED_OUTPUT_PDF)) {
						editableWidgetStorage.listBoxwidget.setEnabled(false);
					}
					if (editableWidgetStorage != null
							&& editableWidgetStorage.getListBoxwidget() != null
							&& editableWidgetStorage.getListBoxwidget().getName() != null
							&& editableWidgetStorage.getListBoxwidget().getName().equals(
									PluginNameConstants.EXPORT_SEARCHABLE_OUTPUT_PDF)) {
						editableWidgetStorage.listBoxwidget.setEnabled(false);
					}
				} else {
					if (editableWidgetStorage != null
							&& editableWidgetStorage.getListBoxwidget() != null
							&& editableWidgetStorage.getListBoxwidget().getName() != null
							&& editableWidgetStorage.getListBoxwidget().getName()
									.equals(PluginNameConstants.EXPORT_COLORED_OUTPUT_PDF)) {
						editableWidgetStorage.listBoxwidget.setEnabled(true);
					}
					if (editableWidgetStorage != null
							&& editableWidgetStorage.getListBoxwidget() != null
							&& editableWidgetStorage.getListBoxwidget().getName() != null
							&& editableWidgetStorage.getListBoxwidget().getName().equals(
									PluginNameConstants.EXPORT_SEARCHABLE_OUTPUT_PDF)) {
						editableWidgetStorage.listBoxwidget.setEnabled(true);
					}
				}
			}
		}
	}
}
