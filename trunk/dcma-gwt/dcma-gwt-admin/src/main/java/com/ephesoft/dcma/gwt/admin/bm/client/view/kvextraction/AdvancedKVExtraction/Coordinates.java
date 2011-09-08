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

public class Coordinates {

	private int x0;
	private int y0;
	private int x1;
	private int y1;
	private AdvancedKVExtractionView advancedKVExtractionView;
	int crosshairReductionFactor = 5;

	public Coordinates(AdvancedKVExtractionView advancedKVExtractionView) {
		this.advancedKVExtractionView = advancedKVExtractionView;
	}

	public Coordinates() {
		super();
	}

	public void setInitialCoordinates(int x0, int y0) {
		this.x0 = x0;
		this.y0 = y0;
	}

	public void setOtherCoordinates(int x1, int y1, boolean isFinal) {
		this.x1 = x1;
		this.y1 = y1;
		if (isFinal) {
			setCoordinates(getQuadrantValues());
		}
	}

	public Coordinates getQuadrantValues() {
		Coordinates returnVal;
		int initialX = this.x0;
		int initialY = this.y0;
		int finalX = this.x1;
		int finalY = this.y1;
		if (initialX <= finalX && initialY <= finalY) {
			// it is in the III quadrant.
			returnVal = getCoordinates(initialX, initialY, finalX, finalY);
		} else if (initialX >= finalX && initialY <= finalY) {
			// it is in IV quadrant.
			returnVal = getCoordinates(finalX, initialY, initialX, finalY);
		} else if (initialX <= finalX && initialY >= finalY) {
			// it is in II quadrant.
			returnVal = getCoordinates(initialX, finalY, finalX, initialY);
		} else if (initialX >= finalX && initialY >= finalY) {
			// it is in I quadrant.
			returnVal = getCoordinates(finalX+crosshairReductionFactor, finalY+crosshairReductionFactor, initialX, initialY);
		} else {
			returnVal = getCoordinates(initialX, initialY, finalX, finalY);
		}
		return returnVal;
	}

	private Coordinates getCoordinates(int initialX, int initialY, int finalX, int finalY) {
		Coordinates coordinates = new Coordinates();
		coordinates.set(initialX, initialY, finalX, finalY);
		return coordinates;
	}

	public void set(int initialX, int initialY, int finalX, int finalY) {
		this.x0 = initialX;
		this.x1 = finalX;
		this.y0 = initialY;
		this.y1 = finalY;
	}

	public void doOverlay() {
		this.advancedKVExtractionView.removeOverlay();
		Coordinates coordinates = this.getQuadrantValues();
		this.advancedKVExtractionView.createOverlay(coordinates.getX0(), coordinates.getX1(), coordinates.getY0(),
				coordinates.getY1(), 1);
	}

	public void doOverlay(Coordinates coordinates, boolean forValue) {
		this.advancedKVExtractionView.removeOverlay();
		this.advancedKVExtractionView.createOverlay(coordinates.getX0(), coordinates.getX1(), coordinates.getY0(),
				coordinates.getY1(), 1, true, forValue);
		Coordinates coordinates2 = this.getQuadrantValues();
		this.advancedKVExtractionView.createOverlay(coordinates2.getX0(), coordinates2.getX1(), coordinates2.getY0(), coordinates2
				.getY1(), 1, false, forValue);
	}

	public int getX0() {
		return x0;
	}

	public void setX0(int x0) {
		this.x0 = x0;
	}

	public int getY0() {
		return y0;
	}

	public void setY0(int y0) {
		this.y0 = y0;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public void setCoordinates(Coordinates valueCoordinates) {
		this.x0 = valueCoordinates.getX0();
		this.x1 = valueCoordinates.getX1();
		this.y0 = valueCoordinates.getY0();
		this.y1 = valueCoordinates.getY1();
	}

	public void clear() {
		this.x0 = 0;
		this.x1 = 0;
		this.y0 = 0;
		this.y1 = 0;

	}

	public boolean isEmpty() {
		boolean returnVal = false;
		if (this.x0 == 0 && this.x1 == 0 && this.y0 == 0 && this.y1 == 0) {
			returnVal = true;
		}
		return returnVal;
	}
}
