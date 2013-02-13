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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.tableinfo;

import java.util.List;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.MessageConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.documenttype.TableTestResultView;
import com.ephesoft.dcma.gwt.admin.bm.client.view.tableinfo.TableInfoView;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.RandomIdGenerator;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.TableColumnInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TestTableResultDTO;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.DialogBox;

/**
 * The presenter for view that shows the table info.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class TableInfoViewPresenter extends AbstractBatchClassPresenter<TableInfoView> {

	/**
	 * tableInfoDetailPresenter TableInfoDetailPresenter.
	 */
	private final TableInfoDetailPresenter tableInfoDetailPresenter;

	/**
	 * editTableInfoPresenter EditTableInfoPresenter.
	 */
	private final EditTableInfoPresenter editTableInfoPresenter;

	/**
	 * tableTestResultView TableTestResultView.
	 */
	private final TableTestResultView tableTestResultView = new TableTestResultView();

	public TableInfoViewPresenter(BatchClassManagementController controller, TableInfoView view) {
		super(controller, view);
		this.tableInfoDetailPresenter = new TableInfoDetailPresenter(controller, view.getTableInfoDetailView());
		this.editTableInfoPresenter = new EditTableInfoPresenter(controller, view.getEditTableInfoView());
	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		tableInfoDetailPresenter.bind();
		if (controller.getSelectedTableInfoField() != null) {
			view.createTableColumnInfoList(controller.getSelectedTableInfoField().getColumnInfoDTOs());

		}
		editTableInfoPresenter.bind();
	}

	/**
	 * To show table info view.
	 */
	public void showTableInfoView() {
		view.getTableInfoVerticalPanel().setVisible(Boolean.TRUE);
		view.getTableInfoConfigVerticalPanel().setVisible(Boolean.FALSE);
	}

	/**
	 * To show edit table info view.
	 */
	public void showEditTableInfoView() {
		view.getTableInfoVerticalPanel().setVisible(Boolean.FALSE);
		view.getTableInfoConfigVerticalPanel().setVisible(Boolean.TRUE);
	}

	/**
	 * To show view on add table column button click.
	 */
	public void onAddTCButtonClicked() {
		if (controller.isAdd()) {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
					BatchClassManagementMessages.ADD_TABLE_INFO));
			return;
		}
		TableColumnInfoDTO tcColumnInfoDTO = createTableColumnInfoDTOObject();

		controller.setAdd(true);
		controller.setSelectedTableColumnInfoField(tcColumnInfoDTO);
		controller.getMainPresenter().showTableColumnInfoView(true);
	}

	/**
	 * To create Table Column Info DTO Object.
	 * 
	 * @return TableColumnInfoDTO
	 */
	public TableColumnInfoDTO createTableColumnInfoDTOObject() {
		TableColumnInfoDTO tcColumnInfoDTO = new TableColumnInfoDTO();
		tcColumnInfoDTO.setNew(true);
		tcColumnInfoDTO.setTableInfoDTO(controller.getSelectedTableInfoField());
		tcColumnInfoDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		return tcColumnInfoDTO;
	}

	/**
	 * To perform binding on edit Table Info Properties Button Click.
	 */
	public void onEditTableInfoPropertiesButtonClicked() {
		controller.setAdd(false);
		showEditTableInfoView();
		controller.getBatchClass().setDirty(Boolean.TRUE);
		editTableInfoPresenter.bind();
	}

	/**
	 * To show view on edit TC Button Click.
	 * 
	 * @param identifier String
	 */
	public void onEditTCButtonClicked(String identifier) {
		controller.setSelectedTableColumnInfoField(controller.getSelectedTableInfoField().getTCInfoDTOByIdentifier(identifier));
		controller.setAdd(false);
		controller.getMainPresenter().showTableColumnInfoView(false);
	}

	/**
	 * To show view on advanced edit Button Click.
	 * 
	 * @param identifier String
	 */
	public void onAdvEditButtonClicked(String identifier) {
		controller.setSelectedTableColumnInfoField(controller.getSelectedTableInfoField().getTCInfoDTOByIdentifier(identifier));
		controller.setAdd(false);
		controller.getMainPresenter().showAdvancedTableExtractionView();
	}

	/**
	 * To show view on delete TC Button Click.
	 * 
	 * @param identifier String
	 */
	public void onDeleteTCButtonClicked(String identifier) {
		controller.getSelectedTableInfoField().getTCInfoDTOByIdentifier(identifier).setDeleted(true);
		controller.getBatchClass().setDirty(true);
		controller.getMainPresenter().showTableInfoView(false);

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

	/**
	 * To perform operations on Test Table Button Click.
	 */
	public void onTestTableButtonClicked() {
		ScreenMaskUtility.maskScreen();
		controller.setTableInfoSelectedField(controller.getSelectedTableInfoField());

		controller.getRpcService().testTablePattern(controller.getBatchClass(), controller.getSelectedTableInfoField(),
				new EphesoftAsyncCallback<List<TestTableResultDTO>>() {

					@Override
					public void customFailure(Throwable throwable) {
						ScreenMaskUtility.unmaskScreen();
						final ConfirmationDialog dialog = ConfirmationDialogUtil.showConfirmationDialog(throwable.getMessage(),
								MessageConstants.TITLE_TEST_FAILURE, Boolean.TRUE, Boolean.TRUE);
						dialog.addDialogListener(new DialogListener() {

							@Override
							public void onOkClick() {
								dialog.hide(true);
							}

							@Override
							public void onCancelClick() {
								// TODO Auto-generated method stub
							}
						});

						dialog.okButton.setStyleName(AdminConstants.BUTTON_STYLE);

					}

					@Override
					public void onSuccess(List<TestTableResultDTO> outputDtos) {
						ScreenMaskUtility.unmaskScreen();
						DialogBox dialogBox = new DialogBox();
						dialogBox.addStyleName("width500px");
						dialogBox.setHeight("200px");
						tableTestResultView.createTestTableList(outputDtos);
						tableTestResultView.setDialogBox(dialogBox);
						dialogBox.setText(MessageConstants.TEST_TABLE_RESULT_HEADER);
						dialogBox.setWidget(tableTestResultView);
						dialogBox.center();
						tableTestResultView.getBackButton().setFocus(true);
						dialogBox.show();
					}
				});
	}
}
