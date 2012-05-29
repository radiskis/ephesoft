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

package com.ephesoft.dcma.gwt.admin.bm.client.view.kvextraction.AdvancedKVExtraction;

public class KeyValueCoordinates {

	private final AdvancedKVExtractionView advancedKVExtractionView;

	private Coordinates keyCoordinates = null;

	private Coordinates valueCoordinates = null;

	private boolean keyFinalized = false;

	private boolean valueFinalized = false;

	private boolean mouseStatus = false;

	public KeyValueCoordinates(AdvancedKVExtractionView advancedKVExtractionView) {
		this.advancedKVExtractionView = advancedKVExtractionView;
		keyCoordinates = new Coordinates(advancedKVExtractionView);
		valueCoordinates = new Coordinates(advancedKVExtractionView);
	}

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
		mouseStatus = !mouseStatus;
	}

	public void finalizeKey() {
		if (this.keyFinalized) {
			if (!valueCoordinates.isEmpty()) {
				keyCoordinates.setCoordinates(valueCoordinates);
				valueCoordinates.clear();
			}
		}
		this.keyFinalized = true;
	}

	public Coordinates getKeyCoordinates() {
		return keyCoordinates;
	}

	public void finalizeValue() {
		this.valueFinalized = true;
		advancedKVExtractionView.findLocation(keyCoordinates, valueCoordinates);
	}

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

	public void createNewOverlay() {
		valueCoordinates.doOverlay(keyCoordinates, this.valueFinalized);
	}

	public boolean isMouseStatus() {
		return mouseStatus;
	}

	public boolean isKeyFinalized() {
		return keyFinalized;
	}

	public void initialize() {
		this.keyCoordinates.clear();
		this.valueCoordinates.clear();
	}

	public void clearFinalizeValues() {
		this.keyFinalized = false;
		this.valueFinalized = false;
	}

	public void createKeyValueOverlay(Coordinates keyCoordinates, Coordinates valueCoordinates) {
		this.keyCoordinates.setCoordinates(keyCoordinates);
		this.valueCoordinates.setCoordinates(valueCoordinates);
		this.keyFinalized = true;
		this.valueFinalized = true;
		createNewOverlay();
	}

	public Coordinates getValueCoordinates() {
		return valueCoordinates;
	}
}
