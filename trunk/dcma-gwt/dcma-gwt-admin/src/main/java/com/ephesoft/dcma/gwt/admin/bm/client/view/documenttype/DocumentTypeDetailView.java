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

package com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.documenttype.DocumentTypeDetailPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class provides functionality to show individual document type detail.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.client.View
 */
public class DocumentTypeDetailView extends View<DocumentTypeDetailPresenter> {

	/**
	 * UI binder.
	 */
	interface Binder extends UiBinder<VerticalPanel, DocumentTypeDetailView> {
	}

	/**
	 * name Label.
	 */
	@UiField
	protected Label name;

	/**
	 * description Label.
	 */
	@UiField
	protected Label description;

	/**
	 * minConfidenceThreshold Label.
	 */
	@UiField
	protected Label minConfidenceThreshold;

	/**
	 * nameLabel Label.
	 */
	@UiField
	protected Label nameLabel;

	/**
	 * descriptionLabel Label.
	 */
	@UiField
	protected Label descriptionLabel;

	/**
	 * minConfidenceThresholdLabel Label.
	 */
	@UiField
	protected Label minConfidenceThresholdLabel;

	/**
	 * recostarExtractionLabel Label.
	 */
	@UiField
	protected Label recostarExtractionLabel;

	/**
	 * recostarExtraction Label.
	 */
	@UiField
	protected Label recostarExtraction;

	/**
	 * isHiddenLabel Label.
	 */
	@UiField
	protected Label isHiddenLabel;

	/**
	 * isHidden CheckBox.
	 */
	@UiField
	protected CheckBox isHidden;

	/**
	 * editDocumentPropertiesButton Button.
	 */
	@UiField
	protected Button editDocumentPropertiesButton;

	/**
	 * scrollPanel ScrollPanel.
	 */
	@UiField
	protected ScrollPanel scrollPanel;

	/**
	 * Instantiates a class via deferred binding.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Constructor.
	 */
	public DocumentTypeDetailView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		nameLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.NAME) + AdminConstants.COLON);
		descriptionLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.DESCRIPTION)
				+ AdminConstants.COLON);
		minConfidenceThresholdLabel.setText(LocaleDictionary.get().getConstantValue(
				BatchClassManagementConstants.MINIMUM_CONFIDENCE_THRESHOLD)
				+ AdminConstants.COLON);
		recostarExtractionLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.RECOSTAR_PROJECT_FILE)
				+ AdminConstants.COLON);
		isHiddenLabel.setText(LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.IS_HIDDEN) + AdminConstants.COLON);
		nameLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		descriptionLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		minConfidenceThresholdLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		recostarExtractionLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		isHiddenLabel.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
		isHidden.setEnabled(false);

		editDocumentPropertiesButton.setText(AdminConstants.EDIT_BUTTON);
		editDocumentPropertiesButton.setHeight(AdminConstants.BUTTON_HEIGHT);
		scrollPanel.setStyleName(AdminConstants.SCROLL_PANEL_HEIGHT);
	}

	/**
	 * To set Name.
	 * 
	 * @param name String
	 */
	public void setName(String name) {
		this.name.setText(name);
	}

	/**
	 * To set Description.
	 * 
	 * @param description String
	 */
	public void setDescription(String description) {
		this.description.setText(description);
	}

	/**
	 * To set Minimum Confidence Threshold.
	 * 
	 * @param minConfidenceThreshold String
	 */
	public void setMinConfidenceThreshold(String minConfidenceThreshold) {
		this.minConfidenceThreshold.setText(minConfidenceThreshold);
	}

	/**
	 * To set Recostar Extraction.
	 * 
	 * @param recostarExtraction String
	 */
	public void setRecostarExtraction(String recostarExtraction) {
		this.recostarExtraction.setText(recostarExtraction);
	}

	/**
	 * To set is hidden field.
	 * 
	 * @param isHidden boolean
	 */
	public void setIsHidden(boolean isHidden) {
		this.isHidden.setValue(isHidden);
	}

	/**
	 * To perform operations on edit Document Properties Button Click.
	 * 
	 * @param clickEvent ClickEvent
	 */
	@UiHandler("editDocumentPropertiesButton")
	public void onEditDocumentPropertiesButtonClick(ClickEvent clickEvent) {
		presenter.getController().getBatchClassManagementPresenter().getDocumentTypeViewPresenter()
				.onEditDocumentPropertiesButtonClicked();
	}
}
