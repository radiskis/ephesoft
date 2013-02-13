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

package com.ephesoft.dcma.gwt.core.client.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.ephesoft.dcma.gwt.core.client.event.ItemsAddedEvent;
import com.ephesoft.dcma.gwt.core.client.event.ModuleItemsAddedEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MultipleSelectTwoSidedListBox extends Composite {

	private static final String OPTION = "option";

	private static final String _200PX = "200px";

	private static final String BOLD_TEXT = "bold-text-black";

	private static final String BUTTON_STYLE = "button-style";

	private static final String REMOVE = "Remove";

	private static final String ADD = "Add";

	private static final String UP_BUTTON = "UP";

	private static final String DOWN_BUTTON = "DOWN";

	private static final int SPACING_CONSTANT_30 = 30;

	private static final int VISIBLE_COUNT_13 = 13;

	private static final int SPACING_CONSTANT_5 = 5;

	private Label availableLabel;

	private Label selectedLabel;

	private ListBox leftHandSideListBox;

	private ListBox rightHandSideListBox;

	@UiField
	protected VerticalPanel leftHandSideListBoxPanel;

	@UiField
	protected VerticalPanel rightHandSideListBoxPanel;

	@UiField
	protected HorizontalPanel customHorizontalPanel;

	@UiField
	protected VerticalPanel customVerticalButtonPanel;

	@UiField
	protected VerticalPanel addRemoveButtonVerticalPanel;

	@UiField
	protected Button moveLeftButton;

	@UiField
	protected Button moveRightButton;

	@UiField
	protected Button moveUpButton;

	@UiField
	protected Button moveDownButton;

	private HandlerManager eventBus;

	public void populateListBoxWithValues(ListBox listBox, List<String> values) {

		listBox.clear();
		if (values != null) {
			int index = 0;
			for (String value : values) {
				listBox.addItem(value);
				listBox.getElement().getElementsByTagName(OPTION).getItem(index++).setTitle(value);
			}
		}
	}

	public void populateListBoxWithMap(ListBox listBox, Map<String, String> values) {
		if (values != null) {
			int index = 0;
			for (Entry<String, String> value : values.entrySet()) {
				listBox.addItem(value.getValue(), value.getKey());
				listBox.getElement().getElementsByTagName(OPTION).getItem(index++).setTitle(value.getValue());
			}
		}
	}

	public List<Integer> getAllSelectedIndexes(ListBox fromList) {
		List<Integer> selectedValuesList = new ArrayList<Integer>(0);
		final int LIST_LENGTH = fromList.getItemCount();
		if (LIST_LENGTH > 0) {
			// populate all selected indexes from the list
			for (int index = 0; index < LIST_LENGTH; index++) {
				if (fromList.isItemSelected(index)) {
					selectedValuesList.add(index);
				}
			}
		}
		return selectedValuesList;
	}

	private List<String> addToLeftButtonHandler(final ListBox fromList) {
		Map<Integer, String> selectedValues = new LinkedHashMap<Integer, String>(0);
		List<String> selectedValuesList = new ArrayList<String>(0);
		// toList.setSelectedIndex(-1);
		final int LIST_LENGTH = fromList.getItemCount();
		if (LIST_LENGTH > 0) {

			// populate all selected values from the list
			for (int index = 0; index < LIST_LENGTH; index++) {
				if (fromList.isItemSelected(index)) {
					String item = fromList.getItemText(index);
					selectedValues.put(index, item);
					selectedValuesList.add(item);
				}
			}
			int temp = 0;
			for (int index : selectedValues.keySet()) {
				// remove from the current list
				fromList.removeItem(index - temp);

				temp++;
			}

			// To enable adding to the left list, uncomment the below method call
			// addToRightList(toList, selectedValues);
		}
		return selectedValuesList;
	}

	/**
	 * @param toList
	 * @param selectedValues
	 */
	public List<String> addToRightButtonHandler(final Button button, final ListBox fromList, final ListBox toList) {
		Map<Integer, String> selectedValues = new LinkedHashMap<Integer, String>(0);
		List<String> selectedValuesList = new ArrayList<String>(0);
		// toList.setSelectedIndex(-1);
		final int LIST_LENGTH = fromList.getItemCount();
		if (LIST_LENGTH > 0) {

			// populate all selected values from the list
			for (int index = 0; index < LIST_LENGTH; index++) {
				if (fromList.isItemSelected(index)) {
					String item = fromList.getItemText(index);
					selectedValues.put(index, item);
					selectedValuesList.add(item);
				}
			}

			// To enable removing from the left list, un-comment the below method call
			// removeFromRightList(fromList, selectedValues);

			for (int index : selectedValues.keySet()) {
				// add to the other list
				String selectedValue = selectedValues.get(index);
				toList.addItem(selectedValue);
				toList.getElement().getElementsByTagName(OPTION).getItem(index).setTitle(selectedValue);
				// set focus on the transferred values
				toList.setItemSelected(toList.getItemCount() - 1, true);
			}

		}
		return selectedValuesList;
	}

	private void addUpButtonPressHandler(Button button, final ListBox list) {
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent clickEvent) {
				final int LIST_LENGTH = list.getItemCount();
				if (list != null && LIST_LENGTH > 0 && !list.isItemSelected(0)) {
						// Map<Integer, String> selectedValues = new HashMap<Integer, String>();
						String tempItemTextString;
						String tempItemValueString;
						List<Integer> selectedValuesIndex = new ArrayList<Integer>(0);

					for (int index = 0; index < LIST_LENGTH; index++) {
						if (list.isItemSelected(index)) {

							tempItemTextString = list.getItemText(index);
							tempItemValueString = list.getValue(index);
							if (index != 0) {

								// swap with the previous value in the list, exception being the the first value in the list
								list.setItemText(index, list.getItemText(index - 1));
								list.setItemText(index - 1, tempItemTextString);
								list.setValue(index, list.getValue(index - 1));
								list.setValue(index - 1, tempItemValueString);
								// populate the indexes of selected values, required to bring focus on them later
								selectedValuesIndex.add(index - 1);
							} else {
								selectedValuesIndex.add(0);
							}
						}
					}

					// remove current focus
					list.setSelectedIndex(-1);

					// set focus on selected indexes after swapping
					for (Integer index : selectedValuesIndex) {
						if (index != 0) {
							list.setItemSelected(index, true);
						} else {
							list.setItemSelected(0, true);
						}
					}

				}
			}

		});
	}

	private void addDownButtonPressHandler(Button button, final ListBox listBox) {
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent clickEvent) {
				final int LIST_LENGTH = listBox.getItemCount();
				if (listBox != null && LIST_LENGTH > 0 && !listBox.isItemSelected(listBox.getItemCount() - 1)) {
					// Map<Integer, String> selectedValues = new HashMap<Integer, String>();
					String tempString;
					String tempItemValueString;
					List<Integer> selectedValuesIndex = new ArrayList<Integer>(0);

					for (int index = LIST_LENGTH - 1; index > -1; index--) {
						if (listBox.isItemSelected(index)) {

							tempString = listBox.getItemText(index);
							tempItemValueString = listBox.getValue(index);
							if (index != LIST_LENGTH - 1) {
								// swap with the next value in the list, exception being the the last value in the list
								listBox.setItemText(index, listBox.getItemText(index + 1));
								listBox.setItemText(index + 1, tempString);
								listBox.setValue(index, listBox.getValue(index + 1));
								listBox.setValue(index + 1, tempItemValueString);
								// populate the indexes of selected values, required to bring focus on them later
								selectedValuesIndex.add(index + 1);
							}

						}
					}

					// remove current focus
					listBox.setSelectedIndex(-1);
					// set focus on selected indexes after swapping
					for (Integer index : selectedValuesIndex) {
						// boolean firstSelected = false;
						if (index != 0) {
							listBox.setItemSelected(index, true);
						} else {
							listBox.setItemSelected(0, true);
						}
					}

				}
			}

		});
	}

	public void moveValuesOnIndexFromOneListToAnother(Set<Integer> indexes, ListBox fromList, ListBox toList) {
		for (Integer index : indexes) {
			int fromListIndex = index;
			String value = fromList.getItemText(fromListIndex);
			toList.addItem(value);
			// fromList.removeItem(fromListIndex);
			int newIndex = toList.getItemCount() - 1;
			toList.getElement().getElementsByTagName(OPTION).getItem(newIndex).setTitle(value);
		}
	}

	public Map<String, String> getAllValuesMapFromList(ListBox listBox) {
		Map<String, String> selectedValues = new LinkedHashMap<String, String>(0);
		for (int index = 0; index < listBox.getItemCount(); index++) {
			selectedValues.put(listBox.getValue(index), listBox.getItemText(index));
		}
		return selectedValues;
	}

	public List<String> getAllValuesFromList(ListBox listBox) {
		List<String> selectedValues = new ArrayList<String>(0);
		for (int i = 0; i < listBox.getItemCount(); i++) {
			selectedValues.add(listBox.getItemText(i));
		}
		return selectedValues;
	}

	private void addCSSStyles() {
		moveUpButton.addStyleName(BUTTON_STYLE);
		moveDownButton.addStyleName(BUTTON_STYLE);
		moveRightButton.addStyleName(BUTTON_STYLE);
		moveLeftButton.addStyleName(BUTTON_STYLE);

		moveRightButton.setWidth("70px");
		moveLeftButton.setWidth("70px");
		moveUpButton.setWidth("55px");
		moveDownButton.setWidth("55px");
		
		availableLabel.addStyleName(BOLD_TEXT);
		selectedLabel.addStyleName(BOLD_TEXT);

		leftHandSideListBoxPanel.setSpacing(SPACING_CONSTANT_5);
		rightHandSideListBoxPanel.setSpacing(SPACING_CONSTANT_5);
		leftHandSideListBox.setWidth(_200PX);
		rightHandSideListBox.setWidth(_200PX);
		customHorizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		customHorizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);

		customHorizontalPanel.setSpacing(SPACING_CONSTANT_30);
		customVerticalButtonPanel.setSpacing(SPACING_CONSTANT_30);
		addRemoveButtonVerticalPanel.setSpacing(SPACING_CONSTANT_30);

		customVerticalButtonPanel.setCellVerticalAlignment(moveUpButton, HasVerticalAlignment.ALIGN_MIDDLE);
		customVerticalButtonPanel.setCellVerticalAlignment(moveDownButton, HasVerticalAlignment.ALIGN_MIDDLE);

		addRemoveButtonVerticalPanel.setCellVerticalAlignment(moveUpButton, HasVerticalAlignment.ALIGN_MIDDLE);
		addRemoveButtonVerticalPanel.setCellVerticalAlignment(moveDownButton, HasVerticalAlignment.ALIGN_MIDDLE);

		customHorizontalPanel.setCellVerticalAlignment(leftHandSideListBoxPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		customHorizontalPanel.setCellVerticalAlignment(rightHandSideListBoxPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		customHorizontalPanel.setCellVerticalAlignment(addRemoveButtonVerticalPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		customHorizontalPanel.setCellVerticalAlignment(customVerticalButtonPanel, HasVerticalAlignment.ALIGN_MIDDLE);

	}

	interface Binder extends UiBinder<DockLayoutPanel, MultipleSelectTwoSidedListBox> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	public MultipleSelectTwoSidedListBox(HandlerManager eventBus) {
		super();
		initWidget(BINDER.createAndBindUi(this));

		leftHandSideListBox = new ListBox(true);
		rightHandSideListBox = new ListBox(true);

		availableLabel = new Label();
		selectedLabel = new Label();

		/* Add handlers to buttons */

		addUpButtonPressHandler(moveUpButton, rightHandSideListBox);
		addDownButtonPressHandler(moveDownButton, rightHandSideListBox);

		/* Add list to their panels */
		leftHandSideListBoxPanel.add(availableLabel);
		rightHandSideListBoxPanel.add(selectedLabel);

		leftHandSideListBoxPanel.add(leftHandSideListBox);
		rightHandSideListBoxPanel.add(rightHandSideListBox);

		addCSSStyles();
		moveLeftButton.setText(REMOVE);
		moveRightButton.setText(ADD);
		
		moveUpButton.setText(UP_BUTTON);
		moveDownButton.setText(DOWN_BUTTON);

		leftHandSideListBox.setVisibleItemCount(VISIBLE_COUNT_13);
		rightHandSideListBox.setVisibleItemCount(VISIBLE_COUNT_13);
		this.setEventBus(eventBus);

	}

	public MultipleSelectTwoSidedListBox() {
		this(null);
	}

	/**
	 * @return the leftHandSideListBox
	 */
	public ListBox getLeftHandSideListBox() {
		return leftHandSideListBox;
	}

	/**
	 * @return the rightHandSideListBox
	 */
	public ListBox getRightHandSideListBox() {
		return rightHandSideListBox;
	}

	@UiHandler("moveRightButton")
	public void addToRight(ClickEvent clickEvent) {
		if (eventBus != null) {
			eventBus.fireEvent(new ItemsAddedEvent(null, getLeftHandSideListBox()));
			eventBus.fireEvent(new ModuleItemsAddedEvent(null, getLeftHandSideListBox()));
		} else {
			addToRightButtonHandler(moveRightButton, leftHandSideListBox, rightHandSideListBox);
		}
	}

	@UiHandler("moveLeftButton")
	public void addToLeft(ClickEvent clickEvent) {
		addToLeftButtonHandler(rightHandSideListBox);

	}

	public final void setEventBus(HandlerManager eventBus) {
		this.eventBus = eventBus;
	}

	/**
	 * @return the moveRightButton
	 */
	public Button getMoveRightButton() {
		return moveRightButton;
	}

	/**
	 * @return the eventBus
	 */
	public HandlerManager getEventBus() {
		return eventBus;
	}

	/**
	 * @return the leftHandSideListBoxPanel
	 */
	public VerticalPanel getLeftHandSideListBoxPanel() {
		return leftHandSideListBoxPanel;
	}

	/**
	 * @return the rightHandSideListBoxPanel
	 */
	public VerticalPanel getRightHandSideListBoxPanel() {
		return rightHandSideListBoxPanel;
	}

	/**
	 * @return the availableLabel
	 */
	public Label getAvailableLabel() {
		return availableLabel;
	}

	/**
	 * @return the selectedLabel
	 */
	public Label getSelectedLabel() {
		return selectedLabel;
	}

}
