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

package com.ephesoft.dcma.gwt.rv.client.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.view.SlidingPanel;
import com.ephesoft.dcma.gwt.rv.client.constant.ReviewProperties;
import com.ephesoft.dcma.gwt.rv.client.constant.ValidateProperties;
import com.ephesoft.dcma.gwt.rv.client.event.DocExpandEvent;
import com.ephesoft.dcma.gwt.rv.client.event.DocExpandEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.DocTypeChangeEvent;
import com.ephesoft.dcma.gwt.rv.client.event.DocTypeChangeEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyDownEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEvent;
import com.ephesoft.dcma.gwt.rv.client.event.RVKeyUpEventHandler;
import com.ephesoft.dcma.gwt.rv.client.event.ValidationFieldChangeEvent;
import com.ephesoft.dcma.gwt.rv.client.event.ValidationFieldChangeEventHandler;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateConstants;
import com.ephesoft.dcma.gwt.rv.client.i18n.ReviewValidateMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosureEvent;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReviewValidatePanel extends RVBasePanel {

	interface Binder extends UiBinder<DockLayoutPanel, ReviewValidatePanel> {
	}

	HorizontalPanel urlButtonPanel = new HorizontalPanel();
	@UiField
	Label functionKeyLabel;
	@UiField
	SlidingPanel slidingPanel;
	ReviewPanel reviewPanel = new ReviewPanel();
	ValidatePanel validatePanel = new ValidatePanel();
	Label separator = new Label();
	LayoutPanel pageImageLayoutPanel = new LayoutPanel();
	@UiField
	HorizontalPanel firstShortcutsPanel;
	@UiField
	HorizontalPanel lastShortcutsPanel;

	@UiField
	DockLayoutPanel reviewValidatePanel;

	DockLayoutPanel tempPanel;

	DisclosurePanel reviewDisclosurePanel = new DisclosurePanel(LocaleDictionary.get().getConstantValue(
			ReviewValidateConstants.REVIEW_PANEL), true);

	FocusPanel reviewFocusPanel = new FocusPanel();
	VerticalPanel reviewVerticalPanel = new VerticalPanel();
	PageImagePanel pageImagePanel = new PageImagePanel();

	LayoutPanel layoutPanel = new LayoutPanel();

	VerticalPanel centerPanel = new VerticalPanel();

	FocusPanel focusPanel = new FocusPanel();
	private static final Binder binder = GWT.create(Binder.class);

	private Boolean isDocumentError = false;

	private Map<String, String> urlAndShortcutMap;

	private Map<String, String> urlAndTitleMap;

	private Map<String, String> urlAndApplicationMap;
	private String URL_CTRL_4;
	private String URL_CTRL_7;
	private String URL_CTRL_8;
	private String URL_CTRL_9;

	public ReviewValidatePanel() {
		initWidget(binder.createAndBindUi(this));
	}

	@Override
	public void initializeWidget() {
		reviewPanel.setWidth("100%");
		validatePanel.setWidth("98%");
		validatePanel.setHeight("92%");
		removeErrorText();
		if (presenter.batchDTO.getBatch().getDocuments().getDocument() != null
				&& presenter.batchDTO.getBatch().getDocuments().getDocument().size() != 0) {

			reviewPanel.setPresenter(presenter);
			reviewPanel.initializeWidget();
			reviewPanel.addStyleName("review-style");

			validatePanel.setPresenter(presenter);
			validatePanel.initializeWidget();
			validatePanel.addStyleName("review-style");

			pageImagePanel.setPresenter(presenter);
			pageImagePanel.initializeWidget();
		}
		functionKeyLabel.setVisible(false);
		functionKeyLabel.setText(ReviewValidateConstants.FUNCTION_KEYS + ReviewValidateConstants.COLON);
		functionKeyLabel.addStyleName(ReviewValidateConstants.BOLD_TEXT);
		slidingPanel.setWidget(firstShortcutsPanel);
		reviewDisclosurePanel.setWidth("100%");
		reviewDisclosurePanel.addEventHandler(new DisclosureHandler() {

			public void onClose(DisclosureEvent event) {
				reviewDisclosurePanel.setOpen(false);
				setVisibility();
				presenter.setCustomizedShortcutPanels();
				focusPanel.setFocus(true);
			}

			public void onOpen(DisclosureEvent event) {
				reviewDisclosurePanel.setOpen(true);
				setVisibility();
				presenter.setCustomizedShortcutPanels();
				focusPanel.setFocus(true);
			}
		});
		BatchInstanceStatus batchInstanceStatus = presenter.batchDTO.getBatchInstanceStatus();
		setURLConstants(batchInstanceStatus);
		setUrlAndApplicationMap();

		if (batchInstanceStatus.equals(BatchInstanceStatus.READY_FOR_VALIDATION)
				|| batchInstanceStatus.equals(BatchInstanceStatus.READY_FOR_REVIEW)) {
			reviewDisclosurePanel.setOpen(false);
		}

		setVisibility();

		slidingPanel.setEventBus(eventBus);

		tempPanel = new DockLayoutPanel(Unit.PCT);

		focusPanel.add(reviewDisclosurePanel);
	}

	private void setURLConstants(BatchInstanceStatus batchInstanceStatus) {
		switch (batchInstanceStatus) {
			case READY_FOR_VALIDATION:
				URL_CTRL_4 = ValidateProperties.EXTERNAL_APP_URL1.getPropertyKey();
				URL_CTRL_7 = ValidateProperties.EXTERNAL_APP_URL2.getPropertyKey();

				URL_CTRL_8 = ValidateProperties.EXTERNAL_APP_URL3.getPropertyKey();

				URL_CTRL_9 = ValidateProperties.EXTERNAL_APP_URL4.getPropertyKey();
				break;
			case READY_FOR_REVIEW:
				URL_CTRL_4 = ReviewProperties.EXTERNAL_APP_URL1.getPropertyKey();
				URL_CTRL_7 = ReviewProperties.EXTERNAL_APP_URL2.getPropertyKey();

				URL_CTRL_8 = ReviewProperties.EXTERNAL_APP_URL3.getPropertyKey();

				URL_CTRL_9 = ReviewProperties.EXTERNAL_APP_URL4.getPropertyKey();
				break;

			default:
		}

	}

	private void setUrlAndApplicationMap() {

		urlAndApplicationMap = new HashMap<String, String>();
		urlAndApplicationMap.put(URL_CTRL_4, ReviewValidateConstants.APP_CTRL_4);
		urlAndApplicationMap.put(URL_CTRL_7, ReviewValidateConstants.APP_CTRL_7);
		urlAndApplicationMap.put(URL_CTRL_8, ReviewValidateConstants.APP_CTRL_8);
		urlAndApplicationMap.put(URL_CTRL_9, ReviewValidateConstants.APP_CTRL_9);
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		eventBus.addHandler(ValidationFieldChangeEvent.TYPE, new ValidationFieldChangeEventHandler() {

			@Override
			public void onValueChange(ValidationFieldChangeEvent event) {
				if (!isDocumentError) {
					if (event.getDocument() != null && !event.isValidated()) {
						setErrorText(event.getDocument().getErrorMessage());
					} else if (event.getDocument() != null && event.isValidated()) {
						removeErrorText();
					} else {
						if (!event.isValidatorsValidated() && event.getFieldName() != null && !event.getFieldName().isEmpty()
								&& event.getSampleValue() != null && !event.getSampleValue().isEmpty()) {
							setErrorText(event.getFieldName() + ReviewValidateConstants.SPACE
									+ LocaleDictionary.get().getMessageValue(ReviewValidateMessages.REGEX_PATTERN_COMPLAINT) + " : "
									+ event.getSampleValue());
						} else {
							removeErrorText();
						}
					}
				}
			}

			@Override
			public void onFieldChange(ValidationFieldChangeEvent event) {
				// TODO Auto-generated method stub

			}
		});
		eventBus.addHandler(DocExpandEvent.TYPE, new DocExpandEventHandler() {

			@Override
			public void onExpand(DocExpandEvent event) {
				if (event.getDocument().getErrorMessage() != null && !event.getDocument().getErrorMessage().isEmpty()) {
					isDocumentError = true;
					setErrorText(event.getDocument().getErrorMessage());
				} else {
					isDocumentError = false;
				}
				if (!isDocumentError) {
					setErrorText(ReviewValidateConstants.EMPTY_STRING);
				}
				presenter.document = event.getDocument();
				presenter.setCustomizedShortcutPanels();
			}

		});

		eventBus.addHandler(DocTypeChangeEvent.TYPE, new DocTypeChangeEventHandler() {

			@Override
			public void onDocumentTypeChange(DocTypeChangeEvent event) {
				presenter.document = event.getDocumentType();
				presenter.setCustomizedShortcutPanels();
			}
		});
		eventBus.addHandler(RVKeyDownEvent.TYPE, new RVKeyDownEventHandler() {

			@Override
			public void onKeyDown(RVKeyDownEvent event) {
				if (!presenter.isTableView()) {
					if (event.getEvent().isControlKeyDown()) {
						switch (event.getEvent().getNativeKeyCode()) {
							case 52:
							case 100:
							case 55:
							case 103:
							case 56:
							case 104:
							case 57:
							case 105:
								event.getEvent().preventDefault();
								break;
						}

					}
				}
			}
		});
		eventBus.addHandler(RVKeyUpEvent.TYPE, new RVKeyUpEventHandler() {

			@Override
			public void onKeyUp(RVKeyUpEvent event) {
				if (!presenter.isTableView()) {
					if (event.getEvent().isControlKeyDown()) {
						String urlToFire = null;
						switch (event.getEvent().getNativeKeyCode()) {
							case 52: // ctrl+4
							case 100:
								event.getEvent().preventDefault();
								urlToFire = URL_CTRL_4;
								break;
							case 55:
							case 103: // ctrl+7
								event.getEvent().preventDefault();
								urlToFire = URL_CTRL_7;
								break;
							case 56: // ctrl+8
							case 104:
								event.getEvent().preventDefault();
								urlToFire = URL_CTRL_8;
								break;
							case 57:
							case 105: // ctrl+9
								event.getEvent().preventDefault();
								urlToFire = URL_CTRL_9;
								break;
						}
						if (urlToFire != null) {
							if (null != urlAndShortcutMap && urlAndShortcutMap.containsKey(urlToFire)) {
								presenter.showExternalAppForHtmlPattern(urlAndShortcutMap.get(urlToFire), urlAndTitleMap
										.get(urlToFire));
							}
						}
					}
				}
			}
		});

	}

	public void setVisibility() {
		switch (presenter.batchDTO.getBatchInstanceStatus()) {
			case READY_FOR_REVIEW:
				// reviewPanel.clearPanel();
				// validatePanel.clearPanel();
				setErrorText(ReviewValidateConstants.EMPTY_STRING);
				urlButtonPanel.setVisible(true);
				slidingPanel.setVisible(false);
				functionKeyLabel.setVisible(false);
				pageImagePanel.setVisible(true);
				validatePanel.setVisible(false);
				layoutPanel.clear();
				pageImageLayoutPanel.clear();
				pageImageLayoutPanel.add(pageImagePanel);
				layoutPanel.add(pageImageLayoutPanel);
				reviewValidatePanel.clear();
				reviewFocusPanel.setHeight("100%");
				reviewFocusPanel.setWidth("100%");
				reviewVerticalPanel.setWidth("100%");
				reviewDisclosurePanel.setWidth("100%");
				reviewVerticalPanel.add(reviewPanel);
				reviewDisclosurePanel.add(reviewPanel);
				if (reviewDisclosurePanel.isOpen()) {
					reviewValidatePanel.addNorth(focusPanel, 31);
					if (presenter.batchDTO.getExternalApplicationSwitchState().equals("ON")) {
						setButtonsForUrls();
						reviewValidatePanel.addSouth(layoutPanel, 52);
						centerPanel.add(urlButtonPanel);
					} else {
						reviewValidatePanel.addSouth(layoutPanel, 60);
					}
					centerPanel.add(separator);

				} else {

					reviewValidatePanel.addNorth(focusPanel, 8.5);
					if (presenter.batchDTO.getExternalApplicationSwitchState().equals("ON")) {
						setButtonsForUrls();
						reviewValidatePanel.addSouth(layoutPanel, 74.5);
						centerPanel.add(urlButtonPanel);
					} else {
						reviewValidatePanel.addSouth(layoutPanel, 83.5);
					}

				}

				reviewValidatePanel.add(centerPanel);
				break;
			case READY_FOR_VALIDATION:
				// reviewPanel.clearPanel();
				// validatePanel.clearPanel();
				urlButtonPanel.setVisible(true);
				setErrorText(ReviewValidateConstants.EMPTY_STRING);
				slidingPanel.setVisible(false);
				functionKeyLabel.setVisible(false);
				validatePanel.setVisible(true);
				pageImagePanel.setVisible(false);
				layoutPanel.clear();
				layoutPanel.add(validatePanel);
				reviewValidatePanel.clear();
				reviewFocusPanel.setHeight("100%");
				reviewFocusPanel.setWidth("100%");
				reviewVerticalPanel.setWidth("100%");
				reviewVerticalPanel.add(reviewPanel);
				reviewDisclosurePanel.setWidth("100%");
				reviewDisclosurePanel.add(reviewPanel);
				if (reviewDisclosurePanel.isOpen()) {
					reviewValidatePanel.addNorth(focusPanel, 31);
					if (presenter.batchDTO.getExternalApplicationSwitchState().equals("ON")) {
						setButtonsForUrls();
						reviewValidatePanel.addSouth(layoutPanel, 52);
						centerPanel.add(urlButtonPanel);
					} else {
						reviewValidatePanel.addSouth(layoutPanel, 60);
					}
					centerPanel.add(separator);
					reviewValidatePanel.add(centerPanel);
				} else {
					reviewValidatePanel.addNorth(focusPanel, 8.5);
					if (presenter.batchDTO.getExternalApplicationSwitchState().equals("ON")) {
						setButtonsForUrls();
						reviewValidatePanel.addSouth(layoutPanel, 74.5);
						centerPanel.add(urlButtonPanel);
					} else {
						reviewValidatePanel.addSouth(layoutPanel, 83.5);
					}
					reviewValidatePanel.add(centerPanel);
				}
				break;
			default:
				GWT.log("Unknown Status for validate Panel.");
				break;
		}
	}

	private void setErrorText(String errorText) {
		separator.setText(errorText);
		separator.setStyleName(ReviewValidateConstants.ERROR_STYLE);
	}

	private void removeErrorText() {
		separator.setText(ReviewValidateConstants.EMPTY_STRING);
		separator.removeStyleName(ReviewValidateConstants.ERROR_STYLE);
	}

	public ValidatePanel getValidatePanel() {
		return validatePanel;
	}

	public ReviewPanel getReviewPanel() {
		return reviewPanel;
	}

	public HorizontalPanel getFirstShortcutsPanel() {
		return firstShortcutsPanel;
	}

	public HorizontalPanel getLastShortcutsPanel() {
		return lastShortcutsPanel;
	}

	public SlidingPanel getSlidingPanel() {
		return slidingPanel;
	}

	public Label getFunctionKeyLabel() {
		return functionKeyLabel;
	}

	public void setButtonsForUrls() {
		urlButtonPanel.clear();
		urlAndShortcutMap = presenter.batchDTO.getUrlAndShortcutMap();
		urlAndTitleMap = presenter.batchDTO.getUrlAndTitleMap();
		if (null != urlAndShortcutMap && !urlAndShortcutMap.isEmpty()) {

			Set<String> urlList = urlAndShortcutMap.keySet();
			for (final String url : urlList) {
				if (urlAndShortcutMap.get(url) != null && !urlAndShortcutMap.get(url).isEmpty()) {
					String buttonName = LocaleDictionary.get().getConstantValue(urlAndApplicationMap.get(url));

					Button button = new Button(buttonName, new ClickHandler() {

						@Override
						public void onClick(ClickEvent arg0) {
							presenter.showExternalAppForHtmlPattern(urlAndShortcutMap.get(url), urlAndTitleMap.get(url));
						}
					});
					button.setWidth("55px");
					button.setVisible(true);
					String title = url.substring(url.indexOf('('), url.length());
					button.setTitle(title);
					urlButtonPanel.setSpacing(5);
					urlButtonPanel.add(button);
				}
			}

		}
	}

	public DisclosurePanel getReviewDisclosurePanel() {
		return reviewDisclosurePanel;
	}
}
