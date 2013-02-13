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

package com.ephesoft.dcma.gwt.rv.client.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.ephesoft.dcma.batch.schema.Document;

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

	private static final String REVIEW_STYLE = "review-style";
	private static final String _92 = "92%";
	private static final String _98 = "98%";
	private static final String _100 = "100%";

	interface Binder extends UiBinder<DockLayoutPanel, ReviewValidatePanel> {
	}

	private final HorizontalPanel urlButtonPanel = new HorizontalPanel();
	@UiField
	protected Label functionKeyLabel;
	@UiField
	protected SlidingPanel slidingPanel;
	protected ReviewPanel reviewPanel = new ReviewPanel();
	protected ValidatePanel validatePanel = new ValidatePanel();
	protected Label separator = new Label();
	protected LayoutPanel pageImageLayoutPanel = new LayoutPanel();
	@UiField
	protected HorizontalPanel firstShortcutsPanel;
	@UiField
	protected HorizontalPanel lastShortcutsPanel;

	@UiField
	protected DockLayoutPanel reviewValidateDockLayoutPanel;

	protected DockLayoutPanel tempPanel;

	protected DisclosurePanel reviewDisclosurePanel = new DisclosurePanel(LocaleDictionary.get().getConstantValue(
			ReviewValidateConstants.REVIEW_PANEL), true);

	protected FocusPanel reviewFocusPanel = new FocusPanel();
	protected VerticalPanel reviewVerticalPanel = new VerticalPanel();
	protected PageImagePanel pageImagePanel = new PageImagePanel();

	protected LayoutPanel layoutPanel = new LayoutPanel();

	protected VerticalPanel centerPanel = new VerticalPanel();

	protected FocusPanel focusPanel = new FocusPanel();
	private static final Binder BINDER = GWT.create(Binder.class);

	private Boolean isDocumentError = false;

	private Map<String, String> urlAndShortcutMap;

	private Map<String, String> urlAndTitleMap;

	private Map<String, String> urlAndApplicationMap;
	private String urlCTRL4;
	private String urlCTRL7;
	private String urlCTRL8;
	private String urlCTRL9;

	public ReviewValidatePanel() {
		super();
		initWidget(BINDER.createAndBindUi(this));
	}

	@Override
	public void initializeWidget() {
		reviewPanel.setWidth(_100);
		validatePanel.setWidth(_98);
		validatePanel.setHeight(_92);
		removeErrorText();
		if (presenter.batchDTO.getBatch().getDocuments().getDocument() != null
				&& presenter.batchDTO.getBatch().getDocuments().getDocument().size() != 0) {

			reviewPanel.setPresenter(presenter);
			reviewPanel.initializeWidget();
			reviewPanel.addStyleName(REVIEW_STYLE);

			validatePanel.setPresenter(presenter);
			validatePanel.initializeWidget();
			validatePanel.addStyleName(REVIEW_STYLE);

			pageImagePanel.setPresenter(presenter);
			pageImagePanel.initializeWidget();
		}
		functionKeyLabel.setVisible(false);
		functionKeyLabel.setText(ReviewValidateConstants.FUNCTION_KEYS + ReviewValidateConstants.COLON);
		functionKeyLabel.addStyleName(ReviewValidateConstants.BOLD_TEXT);
		slidingPanel.setWidget(firstShortcutsPanel);
		reviewDisclosurePanel.setWidth(_100);
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
		reviewDisclosurePanel.setOpen(false);
		setVisibility();

		slidingPanel.setEventBus(eventBus);

		tempPanel = new DockLayoutPanel(Unit.PCT);

		focusPanel.add(reviewDisclosurePanel);
	}

	private void setURLConstants(BatchInstanceStatus batchInstanceStatus) {
		switch (batchInstanceStatus) {
			case READY_FOR_VALIDATION:
				urlCTRL4 = ValidateProperties.EXTERNAL_APP_URL1.getPropertyKey();
				urlCTRL7 = ValidateProperties.EXTERNAL_APP_URL2.getPropertyKey();

				urlCTRL8 = ValidateProperties.EXTERNAL_APP_URL3.getPropertyKey();

				urlCTRL9 = ValidateProperties.EXTERNAL_APP_URL4.getPropertyKey();
				break;
			case READY_FOR_REVIEW:
				urlCTRL4 = ReviewProperties.EXTERNAL_APP_URL1.getPropertyKey();
				urlCTRL7 = ReviewProperties.EXTERNAL_APP_URL2.getPropertyKey();

				urlCTRL8 = ReviewProperties.EXTERNAL_APP_URL3.getPropertyKey();

				urlCTRL9 = ReviewProperties.EXTERNAL_APP_URL4.getPropertyKey();
				break;

			default:
		}

	}

	private void setUrlAndApplicationMap() {

		urlAndApplicationMap = new HashMap<String, String>();
		urlAndApplicationMap.put(urlCTRL4, ReviewValidateConstants.APP_CTRL_4);
		urlAndApplicationMap.put(urlCTRL7, ReviewValidateConstants.APP_CTRL_7);
		urlAndApplicationMap.put(urlCTRL8, ReviewValidateConstants.APP_CTRL_8);
		urlAndApplicationMap.put(urlCTRL9, ReviewValidateConstants.APP_CTRL_9);
	}

	@Override
	public void injectEvents(HandlerManager eventBus) {
		addValidationFieldChangeHandler(eventBus);
		addDocExpandEventHandler(eventBus);

		addDocTypeChangeHandler(eventBus);
		addRVKeyDownEventHandler(eventBus);
		addRVKeyUpEventHandler(eventBus);

	}

	/**
	 * @param eventBus
	 */
	private void addRVKeyUpEventHandler(HandlerManager eventBus) {
		eventBus.addHandler(RVKeyUpEvent.type, new RVKeyUpEventHandler() {

			@Override
			public void onKeyUp(RVKeyUpEvent event) {
				if (!presenter.isTableView() && event.getEvent().isControlKeyDown()) {
					String urlToFire = null;
					switch (event.getEvent().getNativeKeyCode()) {
						case 52: // ctrl+4
						case 100:
							event.getEvent().preventDefault();
							urlToFire = urlCTRL4;
							break;
						case 55:
						case 103: // ctrl+7
							event.getEvent().preventDefault();
							urlToFire = urlCTRL7;
							break;
						case 56: // ctrl+8
						case 104:
							event.getEvent().preventDefault();
							urlToFire = urlCTRL8;
							break;
						case 57:
						case 105: // ctrl+9
							event.getEvent().preventDefault();
							urlToFire = urlCTRL9;
							break;
						default:
							break;
					}
					if (null != urlToFire && null != urlAndShortcutMap && urlAndShortcutMap.containsKey(urlToFire)) {
						presenter.showExternalAppForHtmlPattern(urlAndShortcutMap.get(urlToFire), urlAndTitleMap.get(urlToFire));
					}
				}
			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addRVKeyDownEventHandler(HandlerManager eventBus) {
		eventBus.addHandler(RVKeyDownEvent.type, new RVKeyDownEventHandler() {

			@Override
			public void onKeyDown(RVKeyDownEvent event) {
				if (!presenter.isTableView() && event.getEvent().isControlKeyDown()) {
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
						default:
							break;
					}
				}
			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addDocTypeChangeHandler(HandlerManager eventBus) {
		eventBus.addHandler(DocTypeChangeEvent.type, new DocTypeChangeEventHandler() {

			@Override
			public void onDocumentTypeChange(DocTypeChangeEvent event) {
				presenter.document = event.getDocumentType();
				presenter.setCustomizedShortcutPanels();
			}
		});
	}

	/**
	 * @param eventBus
	 */
	private void addDocExpandEventHandler(HandlerManager eventBus) {
		eventBus.addHandler(DocExpandEvent.type, new DocExpandEventHandler() {

			@Override
			public void onExpand(DocExpandEvent event) {
				presenter.document = event.getDocument();
				setErrorTextForDocument();
				presenter.setCustomizedShortcutPanels();
				if (event.isFireReviewPanelDefaultEvent()) {
					setReviewPanelState();
				}
			}

		});
	}

	/**
	 * @param eventBus
	 */
	private void addValidationFieldChangeHandler(HandlerManager eventBus) {
		eventBus.addHandler(ValidationFieldChangeEvent.type, new ValidationFieldChangeEventHandler() {

			@Override
			public void onValueChange(ValidationFieldChangeEvent event) {
				if (!setErrorTextForDocument()) {
					if (event.getDocument() != null && event.isValidated()) {
						removeErrorText();
					} else if (!event.isValidatorsValidated() && event.getFieldName() != null && !event.getFieldName().isEmpty()
							&& event.getSampleValue() != null && !event.getSampleValue().isEmpty()) {
						setErrorText(event.getFieldName() + ReviewValidateConstants.SPACE
								+ LocaleDictionary.get().getMessageValue(ReviewValidateMessages.REGEX_PATTERN_COMPLAINT) + " : "
								+ event.getSampleValue());
					} else {
						removeErrorText();
					}
				}
			}

			@Override
			public void onFieldChange(ValidationFieldChangeEvent event) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void setVisibility() {
		switch (presenter.batchDTO.getBatchInstanceStatus()) {
			case READY_FOR_REVIEW:
				setVisibilityForBatchInReview();
				break;
			case READY_FOR_VALIDATION:
				setVisibilityForBatchInValidation();
				break;
			default:
				GWT.log("Unknown Status for validate Panel.");
				break;
		}
	}

	/**
	 * 
	 */
	private void setVisibilityForBatchInValidation() {
		// reviewPanel.clearPanel();
		// validatePanel.clearPanel();
		urlButtonPanel.setVisible(true);
		slidingPanel.setVisible(false);
		functionKeyLabel.setVisible(false);
		validatePanel.setVisible(true);
		pageImagePanel.setVisible(false);
		layoutPanel.clear();
		layoutPanel.add(validatePanel);
		reviewValidateDockLayoutPanel.clear();
		reviewFocusPanel.setHeight(_100);
		reviewFocusPanel.setWidth(_100);
		reviewVerticalPanel.setWidth(_100);
		reviewVerticalPanel.add(reviewPanel);
		reviewDisclosurePanel.setWidth(_100);
		reviewDisclosurePanel.add(reviewPanel);
		if (reviewDisclosurePanel.isOpen()) {
			reviewValidateDockLayoutPanel.addNorth(focusPanel, 31);
			if (presenter.batchDTO.getExternalApplicationSwitchState().equals("ON")) {
				setButtonsForUrls();
				reviewValidateDockLayoutPanel.addSouth(layoutPanel, 52);
				centerPanel.add(urlButtonPanel);
			} else {
				reviewValidateDockLayoutPanel.addSouth(layoutPanel, 60);
			}
			
		} else {
			reviewValidateDockLayoutPanel.addNorth(focusPanel, 8.5);
			if (presenter.batchDTO.getExternalApplicationSwitchState().equals("ON")) {
				setButtonsForUrls();
				reviewValidateDockLayoutPanel.addSouth(layoutPanel, 74.5);
				centerPanel.add(urlButtonPanel);
			} else {
				reviewValidateDockLayoutPanel.addSouth(layoutPanel, 83.5);
			}
		}
		centerPanel.add(separator);
		reviewValidateDockLayoutPanel.add(centerPanel);
	}
	
	private boolean setErrorTextForDocument() {
		Document document = presenter.document;
		boolean errorSet = false;
		if (document != null) {
			if (document.getErrorMessage() != null && !document.getErrorMessage().isEmpty()) {
				errorSet = true;
				setErrorText(document.getErrorMessage());
			} else {
				setErrorText(ReviewValidateConstants.EMPTY_STRING);
			}
		} else {
			errorSet = true;
		}
		return errorSet;
	}

	/**
	 * 
	 */
	private void setVisibilityForBatchInReview() {
		// reviewPanel.clearPanel();
		// validatePanel.clearPanel();
		urlButtonPanel.setVisible(true);
		slidingPanel.setVisible(false);
		functionKeyLabel.setVisible(false);
		pageImagePanel.setVisible(true);
		validatePanel.setVisible(false);
		layoutPanel.clear();
		pageImageLayoutPanel.clear();
		pageImageLayoutPanel.add(pageImagePanel);
		layoutPanel.add(pageImageLayoutPanel);
		reviewValidateDockLayoutPanel.clear();
		reviewFocusPanel.setHeight(_100);
		reviewFocusPanel.setWidth(_100);
		reviewVerticalPanel.setWidth(_100);
		reviewDisclosurePanel.setWidth(_100);
		reviewVerticalPanel.add(reviewPanel);
		reviewDisclosurePanel.add(reviewPanel);
		if (reviewDisclosurePanel.isOpen()) {
			reviewValidateDockLayoutPanel.addNorth(focusPanel, 31);
			if (presenter.batchDTO.getExternalApplicationSwitchState().equals("ON")) {
				setButtonsForUrls();
				reviewValidateDockLayoutPanel.addSouth(layoutPanel, 52);
				centerPanel.add(urlButtonPanel);
			} else {
				reviewValidateDockLayoutPanel.addSouth(layoutPanel, 60);
			}

		} else {

			reviewValidateDockLayoutPanel.addNorth(focusPanel, 8.5);
			if (presenter.batchDTO.getExternalApplicationSwitchState().equals("ON")) {
				setButtonsForUrls();
				reviewValidateDockLayoutPanel.addSouth(layoutPanel, 74.5);
				centerPanel.add(urlButtonPanel);
			} else {
				reviewValidateDockLayoutPanel.addSouth(layoutPanel, 83.5);
			}

		}
		centerPanel.add(separator);
		reviewValidateDockLayoutPanel.add(centerPanel);
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

	private void setReviewPanelState() {
		if (presenter.batchDTO.getBatchInstanceStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION)) {
			reviewDisclosurePanel.setOpen(presenter.batchDTO.isDefaultReviewPanelStateOpen());
		} else {
			reviewDisclosurePanel.setOpen(true);
		}
	}

	public FocusPanel getFocusPanel() {
		return focusPanel;
	}
}
