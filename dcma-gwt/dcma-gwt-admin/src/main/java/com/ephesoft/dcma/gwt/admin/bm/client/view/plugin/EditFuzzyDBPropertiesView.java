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

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.plugin.EditFuzzyDBPropertiesPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.validator.ValidatableWidget;
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
 * This class provides functionality to edit fuzzy DB properties.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class EditFuzzyDBPropertiesView extends View<EditFuzzyDBPropertiesPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, EditFuzzyDBPropertiesView> {
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
	private final FlexTable flextable;

	/**
	 * cancel Button.
	 */
	@UiField
	protected Button cancel;

	/**
	 * okButton Button.
	 */
	@UiField
	protected Button okButton;

	/**
	 * mappingButton Button.
	 */
	@UiField
	protected Button mappingButton;

	/**
	 * MAX_VISIBLE_ITEM_COUNT int.
	 */
	public static final int MAX_VISIBLE_ITEM_COUNT = 4;

	/**
	 * FUZZYDB_THRESHOLD_VALUE String.
	 */
	public static final String FUZZYDB_THRESHOLD_VALUE = "fuzzydb.thresholdValue";

	/**
	 * docFieldWidgets List<EditableWidgetStorage>.
	 */
	private final List<EditableWidgetStorage> docFieldWidgets;

	/**
	 * Constructor.
	 */
	public EditFuzzyDBPropertiesView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		cancel.setText(AdminConstants.CANCEL_BUTTON);
		okButton.setText(AdminConstants.OK_BUTTON);

		mappingButton.setText(AdminConstants.MAPPING_BUTTON);

		flextable = new FlexTable();
		flextable.setWidth("100%");
		flextable.getColumnFormatter().setWidth(0, "40%");
		flextable.getColumnFormatter().setWidth(1, "1%");
		flextable.getColumnFormatter().setWidth(2, "59%");
		editTable.setWidget(0, 0, flextable);
		editTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);

		docFieldWidgets = new ArrayList<EditableWidgetStorage>();
		mappingButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				presenter.getController().getMainPresenter().showDocTypeMappingView();
			}
		});
	}

	/**
	 * To set properties.
	 * 
	 * @param pluginConfigDTO BatchClassPluginConfigDTO
	 * @param row int
	 */
	public void setProperties(BatchClassPluginConfigDTO pluginConfigDTO, int row) {
		EditPluginView editPluginView = presenter.getController().getMainPresenter().getView().getPluginView().getEditPluginView();
		if (pluginConfigDTO.getSampleValue() != null && !pluginConfigDTO.getSampleValue().isEmpty()
				&& !pluginConfigDTO.getName().equals(FUZZYDB_THRESHOLD_VALUE)) {
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
		private final BatchClassPluginConfigDTO data;

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
		 * Constructor.
		 * 
		 * @param batchClassPluginConfigDTO BatchClassPluginConfigDTO
		 * @param widget ValidatableWidget<TextBox>
		 */
		public EditableWidgetStorage(BatchClassPluginConfigDTO batchClassPluginConfigDTO, ValidatableWidget<TextBox> widget) {
			this(batchClassPluginConfigDTO, widget, false, null, true);
		}

		/**
		 * Constructor.
		 * 
		 * @param batchClassPluginConfigDTO BatchClassPluginConfigDTO
		 * @param listBoxwidget ListBox
		 */
		public EditableWidgetStorage(BatchClassPluginConfigDTO batchClassPluginConfigDTO, ListBox listBoxwidget) {
			this(batchClassPluginConfigDTO, null, true, listBoxwidget, true);
		}

		/**
		 * Constructor.
		 * 
		 * @param data BatchClassPluginConfigDTO
		 * @param widget ValidatableWidget<TextBox>
		 * @param listBox boolean
		 * @param listBoxwidget ListBox
		 * @param validatable boolean
		 */
		public EditableWidgetStorage(BatchClassPluginConfigDTO data, ValidatableWidget<TextBox> widget, boolean listBox,
				ListBox listBoxwidget, boolean validatable) {
			super();
			this.data = data;
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
		 * To check whether it is list box.
		 * 
		 * @return boolean
		 */
		public boolean isListBox() {
			return listBox;
		}

		/**
		 * To check whether it is validatable.
		 * 
		 * @return boolean
		 */
		public boolean isValidatable() {
			return validatable;
		}

		/**
		 * To set Validatable.
		 * 
		 * @param isValidatable boolean
		 */
		public void setValidatable(boolean isValidatable) {
			this.validatable = isValidatable;
		}

		/**
		 * To get Data.
		 * 
		 * @return BatchClassPluginConfigDTO
		 */
		public BatchClassPluginConfigDTO getData() {
			return data;
		}
	}

	/**
	 * To get Ok Button.
	 * 
	 * @return Button
	 */
	public Button getOkButton() {
		return okButton;
	}

	/**
	 * To get cancel Button.
	 * 
	 * @return Button
	 */
	public Button getCancelButton() {
		return cancel;
	}

	/**
	 * To get Document Field Widgets.
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
	 * To get Mapping Button.
	 * 
	 * @return Button
	 */
	public Button getMappingButton() {
		return mappingButton;
	}

}
