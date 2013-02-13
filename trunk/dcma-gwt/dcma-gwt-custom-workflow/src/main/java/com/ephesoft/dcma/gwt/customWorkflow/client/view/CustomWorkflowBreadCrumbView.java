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

package com.ephesoft.dcma.gwt.customworkflow.client.view;

import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.customworkflow.client.ViewType;
import com.ephesoft.dcma.gwt.customworkflow.client.i18n.CustomWorkflowConstants;
import com.ephesoft.dcma.gwt.customworkflow.client.presenter.CustomWorkflowBreadCrumbPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class CustomWorkflowBreadCrumbView extends View<CustomWorkflowBreadCrumbPresenter> {

	interface Binder extends UiBinder<HorizontalPanel, CustomWorkflowBreadCrumbView> {
	}

	@UiField
	protected HorizontalPanel clickablebreadCrumbPanel;

	@UiField
	protected Button previousButton;

	@UiField
	protected HorizontalPanel breadCrumbPanel;

	protected HorizontalPanel buttonsHorizontalPanel;

	private List<BreadCrumbView> breadCrumbViews = new LinkedList<BreadCrumbView>();

	private static final Binder BINDER = GWT.create(Binder.class);

	public CustomWorkflowBreadCrumbView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		previousButton.setText(LocaleDictionary.get().getConstantValue(CustomWorkflowConstants.BACK_CONSTANT));
		previousButton.setEnabled(false);

		breadCrumbPanel.setSpacing(10);

		buttonsHorizontalPanel = new HorizontalPanel();
		buttonsHorizontalPanel.setSpacing(CustomWorkflowConstants.SPACING_5);
		buttonsHorizontalPanel.add(previousButton);
		breadCrumbPanel.add(buttonsHorizontalPanel);

		breadCrumbPanel.setCellHorizontalAlignment(buttonsHorizontalPanel, HasHorizontalAlignment.ALIGN_RIGHT);

		previousButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (breadCrumbViews.size() == 0) {
					presenter.initializeBreadCrumb();
				}
				((LinkedList<BreadCrumbView>) breadCrumbViews).removeLast();
				BreadCrumbView breadCrumbView = ((LinkedList<BreadCrumbView>) breadCrumbViews).getLast();
				createGivenBreadCrumbView(breadCrumbView.viewType);
			}

		});

	}

	/**
	 * @param breadCrumbView
	 */
	public void createBreadCrumbString() {
		clickablebreadCrumbPanel.clear();
		boolean isFirst = false;
		for (final BreadCrumbView breadCrumbView2 : breadCrumbViews) {
			if (isFirst) {
				Label arrowLabel = new Label(CustomWorkflowConstants.DOUBLE_ARROW);
				clickablebreadCrumbPanel.add(arrowLabel);
			} else {
				isFirst = true;
			}
			Label breadCrumbs = new Label();
			breadCrumbs.setText(breadCrumbView2.breadCrumbName);
			clickablebreadCrumbPanel.add(breadCrumbs);
			breadCrumbs.addStyleName(CustomWorkflowConstants.BOLD_TEXT_STYLE);
			breadCrumbs.addStyleName(CustomWorkflowConstants.CURSOR_POINTER);
			breadCrumbs.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					createGivenBreadCrumbView(breadCrumbView2.viewType);
				}
			});
		}
	}

	/**
	 * @return the previousButton
	 */
	public Button getPreviousButton() {
		return previousButton;
	}

	/**
	 * @param previousButton the previousButton to set
	 */
	public void setPreviousButton(Button previousButton) {
		this.previousButton = previousButton;
	}

	public void create(BreadCrumbView... breadCrumbArray) {
		breadCrumbViews = new LinkedList<BreadCrumbView>();
		for (BreadCrumbView breadCrumbView : breadCrumbArray) {
			breadCrumbViews.add(breadCrumbView);
		}
	}

	public BreadCrumbView getCurrentView() {
		return ((LinkedList<BreadCrumbView>) breadCrumbViews).getLast();
	}

	public static class BreadCrumbView {

		private final ViewType viewType;
		private final String breadCrumbName;
		private final String identifier;

		public BreadCrumbView(ViewType viewType, String breadCrumbName, String identifier) {
			this.viewType = viewType;
			this.breadCrumbName = breadCrumbName;
			this.identifier = identifier;
		}

		@Override
		public int hashCode() {
			int prime = 31;
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

	/**
	 * @param breadCrumbView
	 */
	private void createGivenBreadCrumbView(ViewType viewType) {
		switch (viewType) {

			case ENTRY_VIEW:
				presenter.checkForDirtyPlugin();
				break;
			case DEPENDENCIES_VIEW:
				presenter.getController().getCustomWorkflowManagementPresenter().getDependencyManagementPresenter()
						.showDependenciesView();
				presenter.setBackButtonVisibility(true);
				break;

			default:
				break;
		}

		createBreadCrumbString();
	}

}
