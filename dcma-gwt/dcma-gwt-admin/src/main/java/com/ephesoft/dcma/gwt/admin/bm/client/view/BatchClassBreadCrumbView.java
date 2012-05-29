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

package com.ephesoft.dcma.gwt.admin.bm.client.view;

import java.util.LinkedList;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.ViewType;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.BatchClassBreadCrumbPresenter;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class BatchClassBreadCrumbView extends View<BatchClassBreadCrumbPresenter> {

	interface Binder extends UiBinder<HorizontalPanel, BatchClassBreadCrumbView> {
	}

	@UiField
	protected Label breadCrumbs;

	@UiField
	protected Button previousButton;

	@UiField
	protected HorizontalPanel breadCrumbPanel;

	private static final Binder BINDER = GWT.create(Binder.class);

	public BatchClassBreadCrumbView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		previousButton.setText(AdminConstants.BACK_BUTTON);
		previousButton.setEnabled(false);
		previousButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (breadCrumbViews.size() != 2) {
					breadCrumbViews.removeLast();
				} else {
					boolean isClassChanged = false;
					isClassChanged = presenter.getController().getBatchClass().isDirty();
					if (isClassChanged) {
						final ConfirmationDialog backConfirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(
								LocaleDictionary.get().getMessageValue(BatchClassManagementMessages.UNSAVED_DATA_WILL_LOST),
								LocaleDictionary.get().getConstantValue(BatchClassManagementConstants.WARNING_TITLE), false);
						backConfirmationDialog.addDialogListener(new DialogListener() {

							@Override
							public void onOkClick() {
								breadCrumbViews.removeLast();
								Window.Location.reload();
							}

							@Override
							public void onCancelClick() {
							}
						});

					} else {
						breadCrumbViews.removeLast();
					}
				}
				presenter.getController().setAdd(false);
				BreadCrumbView breadCrumbView = breadCrumbViews.getLast();
				switch (breadCrumbView.viewType) {
					case BATCH_CLASS_LISTING:
						Window.Location.reload();
						break;
					case BATCH_CLASS:
						presenter.getController().getMainPresenter().showBatchClassView();
						break;
					case MODULE:
						presenter.getController().getMainPresenter().showModuleView();
						break;
					case PLUGIN:
						presenter.getController().getMainPresenter().showPluginView();
						break;
					case KV_PP_PLUGIN:
						presenter.getController().getMainPresenter().showKVppPluginView();
						break;
					case DOCUMENT_TYPE:
						presenter.getController().getMainPresenter().showDocumentTypeView(false);
						break;
					case TABLE_MAPPING:
						presenter.getController().getMainPresenter().showDocTypeFieldMappingView();
						break;
					case DATABASE_MAPPING:
						presenter.getController().getMainPresenter().showDocTypeMappingView();
						break;
					case FUZZY_DB:
						presenter.getController().getMainPresenter().showFuzzyDBPluginView();
						break;
					case DOCUMENT_LEVEL_FIELD:
						presenter.getController().getMainPresenter().getAdvancedKVExtractionPresenter().getView().removeAllOverlays();
						presenter.getController().getMainPresenter().getView().toggleBottomPanelShowHide(true);
						presenter.getController().getMainPresenter().showFieldTypeView(false);
						break;
					case EMAIL:
						presenter.getController().getMainPresenter().showEmailView(false);
						break;
					case BATCH_CLASS_FIELD:
						presenter.getController().getMainPresenter().showBatchClassFieldView(false);
						break;
					case KV_PP_PLUGIN_CONFIG_ADD_EDIT:
						presenter.getController().getMainPresenter().showKVppPluginConfigAddEditView();
						break;
					case TABLE_INFO:
						presenter.getController().getMainPresenter().showTableInfoView(false);
						break;
					case TABLE_COLUMN_INFO:
						presenter.getController().getMainPresenter().showTableColumnInfoView(false);
						break;
					case FUNCTION_KEY:
						presenter.getController().getMainPresenter().showFunctionKeyView(false);
						break;
					case KV_PP_PLUGIN_CONFIG:
						presenter.getController().getMainPresenter().showKVppPluginConfigView();
						break;
					case CONFIGURE_MODULE:
						presenter.getController().getMainPresenter().showAddModuleView();
						break;
					case CONFIGURE_PLUGIN:
						presenter.getController().getMainPresenter().showAddPluginView();
						break;
					default:
						break;
				}
				createBreadCrumbView();

			}
		});

	}

	private LinkedList<BreadCrumbView> breadCrumbViews = new LinkedList<BreadCrumbView>();

	private void createBreadCrumbView() {
		if (breadCrumbViews.size() == 1) {
			previousButton.setVisible(false);
			previousButton.removeStyleName(AdminConstants.BUTTON_STYLE);
			previousButton.addStyleName(AdminConstants.DISABLED_BUTTON_STYLE);
		} else {
			previousButton.setVisible(true);
			previousButton.setEnabled(true);
			previousButton.removeStyleName(AdminConstants.DISABLED_BUTTON_STYLE);
			previousButton.addStyleName(AdminConstants.BUTTON_STYLE);
		}
		StringBuilder breadCrumbString = new StringBuilder();
		boolean isFirst = false;
		for (BreadCrumbView breadCrumbView : breadCrumbViews) {
			if (isFirst) {
				breadCrumbString.append(AdminConstants.DOUBLE_ARROW);
			} else {
				isFirst = true;
			}
			breadCrumbString.append(breadCrumbView.breadCrumbName);
		}
		breadCrumbs.setText(breadCrumbString.toString());
		breadCrumbs.setStyleName(AdminConstants.BOLD_TEXT_STYLE);
	}

	public void create(BreadCrumbView... breadCrumbArray) {
		breadCrumbViews = new LinkedList<BreadCrumbView>();
		for (BreadCrumbView breadCrumbView : breadCrumbArray) {
			breadCrumbViews.add(breadCrumbView);
		}
		createBreadCrumbView();
	}

	public BreadCrumbView getCurrentView() {
		return breadCrumbViews.getLast();
	}

	public static class BreadCrumbView {

		private ViewType viewType;
		private String breadCrumbName;
		String identifier;

		public BreadCrumbView(ViewType viewType, String breadCrumbName, String identifier) {
			this.viewType = viewType;
			this.breadCrumbName = breadCrumbName;
			this.identifier = identifier;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((viewType == null) ? 0 : viewType.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			BreadCrumbView other = (BreadCrumbView) obj;
			if (viewType == null) {
				if (other.viewType != null) {
					return false;
				}
			} else if (!viewType.equals(other.viewType)) {
				return false;
			}
			return true;
		}
	}

	public HorizontalPanel getBreadCrumbPanel() {
		return breadCrumbPanel;
	}
}
