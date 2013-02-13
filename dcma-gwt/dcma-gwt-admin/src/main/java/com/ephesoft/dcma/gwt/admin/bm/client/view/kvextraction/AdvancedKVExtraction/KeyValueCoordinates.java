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

package com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction.advancedkvextraction;

/**
 * This is class to handle key value coordinates.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public class KeyValueCoordinates {

	/**
	 * advancedKVExtractionView AdvancedKVExtractionView.
	 */
	private final AdvancedKVExtractionView advancedKVExtractionView;

	/**
	 * keyCoordinates Coordinates.
	 */
	private Coordinates keyCoordinates = null;// NOPMD

	/**
	 * valueCoordinates Coordinates.
	 */
	private Coordinates valueCoordinates = null;// NOPMD

	/**
	 * keyFinalized boolean.
	 */
	private boolean keyFinalized = false;

	/**
	 * valueFinalized boolean.
	 */
	private boolean valueFinalized = false;

	/**
	 * mouseStatus boolean.
	 */
	private boolean mouseStatus = false;

	/**
	 * Constructor.
	 * 
	 * @param advancedKVExtractionView AdvancedKVExtractionView
	 */
	public KeyValueCoordinates(AdvancedKVExtractionView advancedKVExtractionView) {
		this.advancedKVExtractionView = advancedKVExtractionView;
		keyCoordinates = new Coordinates(advancedKVExtractionView);
		valueCoordinates = new Coordinates(advancedKVExtractionView);
	}

	/**
	 * To set coordinates on mouse click.
	 * 
	 * @param xCoordinate int
	 * @param yCoordinate int
	 */
	public void mouseDownat(int xCoordinate, int yCoordinate) {
		if (!mouseStatus) {
			if (!this.keyFinalized) {
				keyCoordinates.setInitialCoordinates(xCoordinate, yCoordinate);
			} else {
				valueCoordinates.setInitialCoordinates(xCoordinate, yCoordinate);
				this.valueFinalized = false;
			}
		} else {
			if (!this.keyFinalized) {
				keyCoordinates.setOtherCoordinates(xCoordinate, yCoordinate, true);
			} else {
				valueCoordinates.setOtherCoordinates(xCoordinate, yCoordinate, true);
				this.valueFinalized = false;
			}
		}
		mouseStatus = !mouseStatus;// NOPMD
	}

	/**
	 * To set coordinates on key capture.
	 */
	public void onKeyCapture() {
		if (this.keyFinalized && !valueCoordinates.isEmpty()) {
			keyCoordinates.setCoordinates(valueCoordinates);
			valueCoordinates.clear();
		}
	}

	/**
	 * To get Key Coordinates.
	 * 
	 * @return Coordinates
	 */
	public Coordinates getKeyCoordinates() {
		return keyCoordinates;
	}

	/**
	 * To finalize Value.
	 */
	public void finalizeValue() {
		this.valueFinalized = true;
		advancedKVExtractionView.setValues(keyCoordinates, valueCoordinates);
	}

	/**
	 * To set other coordinates on mouse click.
	 * 
	 * @param xCoordinate int
	 * @param yCordinate int
	 */
	public void mouseMoveat(int xCoordinate, int yCordinate) {
		if (mouseStatus) {
			if (!this.keyFinalized) {
				keyCoordinates.setOtherCoordinates(xCoordinate, yCordinate, false);
				keyCoordinates.doOverlay();
			} else {
				valueCoordinates.setOtherCoordinates(xCoordinate, yCordinate, false);
				valueCoordinates.doOverlay(keyCoordinates, this.valueFinalized);
			}
		}
	}

	/**
	 * To create New Overlay.
	 */
	public void createNewOverlay() {
		valueCoordinates.doOverlay(keyCoordinates, this.valueFinalized);
	}

	/**
	 * To get mouse status.
	 * 
	 * @return boolean
	 */
	public boolean isMouseStatus() {
		return mouseStatus;
	}

	/**
	 * To get value of field key Finalized.
	 * 
	 * @return boolean
	 */
	public boolean isKeyFinalized() {
		return keyFinalized;
	}

	/**
	 * To initialize key & value coordinates.
	 */
	public void initialize() {
		this.keyCoordinates.clear();
		this.valueCoordinates.clear();
	}

	/**
	 * To clear Finalize Values.
	 */
	public void clearFinalizeValues() {
		this.keyFinalized = false;
		this.valueFinalized = false;
	}

	/**
	 * To create Key Value Overlay.
	 * 
	 * @param keyCoordinates Coordinates
	 * @param valueCoordinates Coordinates
	 */
	public void createKeyValueOverlay(Coordinates keyCoordinates, Coordinates valueCoordinates) {
		this.keyCoordinates.setCoordinates(keyCoordinates);
		this.valueCoordinates.setCoordinates(valueCoordinates);
		this.keyFinalized = true;
		this.valueFinalized = true;
		createNewOverlay();
	}

	/**
	 * To get Value Coordinates.
	 * 
	 * @return Coordinates
	 */
	public Coordinates getValueCoordinates() {
		return valueCoordinates;
	}

	/**
	 * To set Key Finalized.
	 * 
	 * @param keyFinalized boolean
	 */
	public void setKeyFinalized(boolean keyFinalized) {
		this.keyFinalized = keyFinalized;
	}

	/**
	 * To set Value Finalized.
	 * 
	 * @param valueFinalized boolean
	 */
	public void setValueFinalized(boolean valueFinalized) {
		this.valueFinalized = valueFinalized;
	}

	/**
	 * To get value of field is Value Finalized.
	 * 
	 * @return boolean
	 */
	public boolean isValueFinalized() {
		return this.valueFinalized;
	}
}
