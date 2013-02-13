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

package com.ephesoft.dcma.gwt.admin.bm.client.view.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.EditBoxExporterPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
import com.ephesoft.dcma.gwt.core.client.view.ExternalAppDialogBox;
import com.ephesoft.dcma.gwt.core.client.view.ExternalAppDialogBox.DialogBoxListener;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginConfigDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to edit box exporter.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class EditBoxExporterView extends View<EditBoxExporterPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, EditBoxExporterView> {
	}

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * editTable FlexTable.
	 */
	@UiField
	protected FlexTable editTable;

	/**
	 * flextable FlexTable.
	 */
	private FlexTable flextable;

	/**
	 * cancel Button.
	 */
	@UiField
	protected Button cancel;

	/**
	 * ok Button.
	 */
	@UiField
	protected Button ok;

	/**
	 * authenticationTokenButton Button.
	 */
	@UiField
	protected Button authenticationTokenButton;

	/**
	 * MAX_VISIBLE_ITEM_COUNT int.
	 */
	public static final int MAX_VISIBLE_ITEM_COUNT = 4;

	/**
	 * TOKEN String.
	 */
	private static final String TOKEN = "boxExportPlugin.authenticationToken";

	/**
	 * docFieldWidgets List<EditableWidgetStorage>.
	 */
	List<EditableWidgetStorage> docFieldWidgets;

	/**
	 * Constructor.
	 */
	public EditBoxExporterView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		cancel.setText(AdminConstants.CANCEL_BUTTON);

		ok.setText(AdminConstants.OK_BUTTON);

		authenticationTokenButton.setText(AdminConstants.AUTHENTICATION_TOKEN_BUTTON);
		flextable = new FlexTable();
		flextable.setWidth("100%");
		flextable.getColumnFormatter().setWidth(0, "40%");
		flextable.getColumnFormatter().setWidth(1, "1%");
		flextable.getColumnFormatter().setWidth(2, "59%");
		editTable.setWidget(0, 0, flextable);
		editTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		docFieldWidgets = new ArrayList<EditableWidgetStorage>();

		authenticationTokenButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				Map<String, String> boxExportPropertyMap = presenter.boxExportPropertyMap;
				if (boxExportPropertyMap != null) {
					presenter.getNewTicket(true);
				} else {
					presenter.getBoxExporterProperties(true);
				}
			}
		});
	}

	/**
	 * To set Properties.
	 * 
	 * @param pluginConfigDTO BatchClassPluginConfigDTO
	 * @param row int
	 */
	public void setProperties(BatchClassPluginConfigDTO pluginConfigDTO, int row) {
		EditPluginView editPluginView = presenter.getController().getMainPresenter().getView().getPluginView().getEditPluginView();
		if (pluginConfigDTO.getSampleValue() != null && !pluginConfigDTO.getSampleValue().isEmpty()) {
			if (pluginConfigDTO.getSampleValue().size() > 1) {
				// Create a listBox
				if (pluginConfigDTO.isMultivalue()) {
					// Create a multiple select list box
					Label propertyName = new Label(pluginConfigDTO.getDescription() + BatchClassManagementConstants.COLON);
					List<String> sampleValueList = pluginConfigDTO.getSampleValue();
					int max_visible_item_count = MAX_VISIBLE_ITEM_COUNT;
					if (sampleValueList.size() < MAX_VISIBLE_ITEM_COUNT) {
						max_visible_item_count = sampleValueList.size();
					}
					ListBox fieldValue = editPluginView.addMultipleSelectListBox(row, sampleValueList, max_visible_item_count,
							pluginConfigDTO.getValue());
					flextable.setWidget(row, 0, propertyName);
					if (pluginConfigDTO.isMandatory()) {
						addWidgetStar(row, 1);
					}
					flextable.setWidget(row, 2, fieldValue);
					docFieldWidgets.add(new EditableWidgetStorage(pluginConfigDTO, fieldValue));
				} else {
					// Create a drop down
					Label propertyName = new Label(pluginConfigDTO.getDescription() + BatchClassManagementConstants.COLON);
					ListBox fieldValue = editPluginView.addDropDown(row, pluginConfigDTO.getSampleValue(), pluginConfigDTO.getValue());
					EditableWidgetStorage editableWidgetStorage = new EditableWidgetStorage(pluginConfigDTO, fieldValue);
					editableWidgetStorage.setValidatable(Boolean.FALSE);
					flextable.setWidget(row, 0, propertyName);
					if (pluginConfigDTO.isMandatory()) {

						addWidgetStar(row, 1);
					}
					flextable.setWidget(row, 2, fieldValue);
					docFieldWidgets.add(editableWidgetStorage);
				}

			} else {
				// Create a read only text box
				Label propertyName = new Label(pluginConfigDTO.getDescription() + BatchClassManagementConstants.COLON);
				TextBox fieldValue = new TextBox();
				fieldValue.setText(pluginConfigDTO.getValue());
				final ValidatableWidget<TextBox> validatableTextBox = editPluginView.addTextBox(row, pluginConfigDTO, Boolean.TRUE);
				EditableWidgetStorage editableWidgetStorage = new EditableWidgetStorage(pluginConfigDTO, validatableTextBox);
				editableWidgetStorage.setValidatable(Boolean.FALSE);
				flextable.setWidget(row, 0, propertyName);
				if (pluginConfigDTO.isMandatory()) {
					addWidgetStar(row, 1);
				}
				flextable.setWidget(row, 2, validatableTextBox.getWidget());
				docFieldWidgets.add(editableWidgetStorage);
			}
		} else {
			// Create a text box
			Label propertyName = new Label(pluginConfigDTO.getDescription() + BatchClassManagementConstants.COLON);
			final ValidatableWidget<TextBox> validatableTextBox = editPluginView.addTextBox(row, pluginConfigDTO, Boolean.FALSE);
			flextable.setWidget(row, 0, propertyName);
			if (pluginConfigDTO.isMandatory()) {
				addWidgetStar(row, 1);
			}
			flextable.setWidget(row, 2, validatableTextBox.getWidget());
			docFieldWidgets.add(new EditableWidgetStorage(pluginConfigDTO, validatableTextBox));
		}
		flextable.getFlexCellFormatter().addStyleName(row, 0, AdminConstants.BOLD_TEXT_STYLE);
		flextable.getFlexCellFormatter().setAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
	}

	/**
	 * To add buttons.
	 * 
	 * @param row int
	 */
	public void addButtons(int row) {
	}

	/**
	 * Class for Editable Widget Storage.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	public static class EditableWidgetStorage {

		/**
		 * data BatchClassPluginConfigDTO.
		 */
		private BatchClassPluginConfigDTO data;

		/**
		 * widget ValidatableWidget<TextBox>.
		 */
		private ValidatableWidget<TextBox> widget;

		/**
		 * isListBox boolean.
		 */
		private boolean isListBox;

		/**
		 * listBoxwidget ListBox.
		 */
		private ListBox listBoxwidget;

		/**
		 * isValidatable boolean.
		 */
		private boolean isValidatable;

		/**
		 * Constructor.
		 * 
		 * @param batchClassPluginConfigDTO BatchClassPluginConfigDTO
		 * @param widget ValidatableWidget<TextBox>
		 */
		public EditableWidgetStorage(BatchClassPluginConfigDTO batchClassPluginConfigDTO, ValidatableWidget<TextBox> widget) {
			this.data = batchClassPluginConfigDTO;
			this.widget = widget;
			this.isListBox = false;
			this.isValidatable = true;
		}

		/**
		 * Constructor.
		 * 
		 * @param batchClassPluginConfigDTO
		 * @param listBoxwidget
		 */
		public EditableWidgetStorage(BatchClassPluginConfigDTO batchClassPluginConfigDTO, ListBox listBoxwidget) {
			this.data = batchClassPluginConfigDTO;
			this.listBoxwidget = listBoxwidget;
			this.isListBox = true;
			this.isValidatable = true;
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
		 * To check whether it is list box or not.
		 * 
		 * @return boolean
		 */
		public boolean isListBox() {
			return isListBox;
		}

		/**
		 * To check whether it is validatable or not.
		 * 
		 * @return boolean
		 */
		public boolean isValidatable() {
			return isValidatable;
		}

		/**
		 * To set Validatable.
		 * 
		 * @param isValidatable boolean
		 */
		public void setValidatable(boolean isValidatable) {
			this.isValidatable = isValidatable;
		}

		/**
		 * To get data.
		 * 
		 * @return BatchClassPluginConfigDTO
		 */
		public BatchClassPluginConfigDTO getData() {
			return data;
		}
	}

	/**
	 * To get OK button.
	 * 
	 * @return Button
	 */
	public Button getOkButton() {
		return ok;
	}

	/**
	 * To get cancel button.
	 * 
	 * @return Button
	 */
	public Button getCancelButton() {
		return cancel;
	}

	/**
	 * To get Doc Field Widgets.
	 * 
	 * @return List<EditableWidgetStorage>
	 */
	public List<EditableWidgetStorage> getDocFieldWidgets() {
		return docFieldWidgets;
	}

	/**
	 * To add Widget Star.
	 * 
	 * @param row int
	 * @param column int
	 */
	public void addWidgetStar(int row, int column) {
		Label star = new Label(BatchClassManagementConstants.STAR);
		flextable.setWidget(row, column, star);
		star.setStyleName(BatchClassManagementConstants.FONT_RED);
	}

	/**
	 * To display URL for getting Authorized Token.
	 * 
	 * @param ticket String
	 */
	public void displayURLForGettingAuthToken(final String ticket) {
		Map<String, String> boxExportPropertyMap = presenter.boxExportPropertyMap;
		if (null != boxExportPropertyMap) {
			String url = presenter.boxExportPropertyMap.get(BatchClassManagementConstants.AUTHENTICATION_URL);
			String finalURL = url.replace(BatchClassManagementConstants.TICKET, ticket);
			final ExternalAppDialogBox externalAppDialogBox = new ExternalAppDialogBox(finalURL, 640, 480, false, true, true);
			externalAppDialogBox.addDialogBoxListener(new DialogBoxListener() {

				@Override
				public void onOkClick() {
					presenter.getAuthenticationToken(getValueOfDocFieldWidget(BatchClassManagementConstants.API_KEY_PROPERTY_NAME),
							ticket);
					externalAppDialogBox.hide();
				}

				@Override
				public void onCloseClick() {
					presenter.getAuthenticationToken(getValueOfDocFieldWidget(BatchClassManagementConstants.API_KEY_PROPERTY_NAME),
							ticket);
					externalAppDialogBox.hide();
				}
			});
		}

	}

	/**
	 * To set Authentication Token.
	 * 
	 * @param token String
	 */
	public void setAuthenticationToken(String token) {
		setValueOfDocFieldWidget(TOKEN, token);
	}

	private void setValueOfDocFieldWidget(String name, String value) {
		for (EditableWidgetStorage editableWidgetStorage : docFieldWidgets) {
			if (editableWidgetStorage.getData().getName().equals(name)) {
				if (editableWidgetStorage.isListBox) {
					ListBox listBoxwidget = editableWidgetStorage.getListBoxwidget();
					if (listBoxwidget != null) {
						for (int i = 0; i < listBoxwidget.getItemCount(); i++) {
							String listBoxValue = listBoxwidget.getValue(i);
							if (listBoxValue.equals(value)) {
								listBoxwidget.setSelectedIndex(i);
								break;
							}
						}
					}
				} else {
					ValidatableWidget<TextBox> textBoxWidget = editableWidgetStorage.getTextBoxWidget();
					if (textBoxWidget != null) {
						textBoxWidget.getWidget().setValue(value);
					}
				}
				break;
			}
		}
	}

	/**
	 * To get Value Of Document Field Widget.
	 * 
	 * @param name String
	 * @return String
	 */
	public String getValueOfDocFieldWidget(String name) {
		String value = null;
		for (EditableWidgetStorage editableWidgetStorage : docFieldWidgets) {
			String nameOfWidget = editableWidgetStorage.getData().getName();
			if (nameOfWidget.equals(name)) {
				if (editableWidgetStorage.isListBox) {
					ListBox listBoxwidget = editableWidgetStorage.getListBoxwidget();
					if (listBoxwidget != null) {
						value = listBoxwidget.getValue(listBoxwidget.getSelectedIndex());
					}
				} else {
					ValidatableWidget<TextBox> textBoxWidget = editableWidgetStorage.getTextBoxWidget();
					if (textBoxWidget != null) {
						value = textBoxWidget.getWidget().getValue();
					}
				}
				break;
			}
		}
		return value;
	}
}
