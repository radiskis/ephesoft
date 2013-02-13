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

package com.ephesoft.dcma.gwt.admin.bm.client.view.tablecolumninfo.advancedtableextraction;

/**
 * This class provides functionality to handle table column coordinates.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public class TableColumnCoordinates {

	/**
	 * columnCoordinates Coordinates.
	 */
	private final Coordinates columnCoordinates;

	/**
	 * columnFinalized boolean.
	 */
	private boolean columnFinalized = false;

	/**
	 * mouseStatus boolean.
	 */
	private boolean mouseStatus = false;

	/**
	 * Constructor.
	 * 
	 * @param advancedTableExtractionView AdvancedTableExtractionView
	 */
	public TableColumnCoordinates(AdvancedTableExtractionView advancedTableExtractionView) {
		columnCoordinates = new Coordinates(advancedTableExtractionView);
	}

	/**
	 * To get the mouse status.
	 * 
	 * @param xCoordinate int
	 * @param yCoordinate int
	 */
	public void mouseDownat(final int xCoordinate, final int yCoordinate) {
		if (!mouseStatus) {
			this.columnFinalized = false;
			columnCoordinates.setInitialCoordinates(xCoordinate, yCoordinate);
		} else {
			if (!this.columnFinalized) {
				columnCoordinates.setOtherCoordinates(xCoordinate, yCoordinate, true);
				finalizeColumnCoord();
			}
		}
		mouseStatus = !mouseStatus;
	}

	/**
	 * To move mouse at particular coordinates.
	 * 
	 * @param xCoordinate int
	 * @param yCoordinate int
	 */
	public void mouseMoveat(final int xCoordinate, final int yCoordinate) {
		if (mouseStatus) {
			columnCoordinates.setOtherCoordinates(xCoordinate, yCoordinate, false);
			columnCoordinates.doOverlay(false);
		}
	}

	private void finalizeColumnCoord() {
		this.columnFinalized = true;
		this.columnCoordinates.setColumnCoordinates();
		this.columnCoordinates.doOverlay(columnFinalized);
	}

	/**
	 * To set Column Finalized.
	 * 
	 * @param columnFinalized boolean
	 */
	public void setColumnFinalized(boolean columnFinalized) {
		this.columnFinalized = columnFinalized;
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
	 * To get whether column is finalized.
	 * 
	 * @return boolean
	 */
	public boolean isColumnFinalized() {
		return columnFinalized;
	}

	/**
	 * To initialize.
	 */
	public void initialize() {
		this.columnCoordinates.clear();
	}

	/**
	 * To clear Finalize Values.
	 */
	public void clearFinalizeValues() {
		this.columnFinalized = false;
	}

	/**
	 * To create New Overlay.
	 */
	public void createNewOverlay() {
		columnCoordinates.doOverlay(columnFinalized);
	}

	/**
	 * To get Coordinates.
	 * 
	 * @return Coordinates
	 */
	public Coordinates getCoordinates() {
		return columnCoordinates;
	}

}
