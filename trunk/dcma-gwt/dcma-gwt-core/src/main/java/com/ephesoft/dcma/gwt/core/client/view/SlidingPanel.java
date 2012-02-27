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

package com.ephesoft.dcma.gwt.core.client.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.ephesoft.dcma.gwt.core.client.event.AnimationCompleteEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.layout.client.Layout.Layer;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

public class SlidingPanel extends ResizeComposite implements HasWidgets {

	private final List<Widget> widgets = new ArrayList<Widget>();
	private final LayoutPanel layoutPanel = new LayoutPanel();
	private int currentIndex = -1;
	private HandlerManager eventBus;

	public SlidingPanel() {
		initWidget(layoutPanel);
	}

	public void add(Widget w) {
		widgets.remove(w);
		widgets.add(w);

		// Display the first widget added by default
		if (currentIndex < 0) {
			layoutPanel.add(w);
			currentIndex = 0;
		}
	}

	public void setEventBus(HandlerManager eventBus) {
		this.eventBus = eventBus;
	}

	public void clear() {
		setWidget(null);
		widgets.clear();
	}

	public Widget getWidget() {
		return widgets.get(currentIndex);
	}

	public Iterator<Widget> iterator() {
		return Collections.unmodifiableList(widgets).iterator();
	}

	public boolean remove(Widget w) {
		return widgets.remove(w);
	}

	public void setWidget(Widget widget) {
		if (widget == null) {
			return;
		}

		int newIndex = widgets.indexOf(widget);

		if (newIndex < 0) {
			newIndex = widgets.size();
			add(widget);
		}

		show(newIndex);
	}

	private void show(final int newIndex) {
		if (newIndex == currentIndex) {
			return;
		}

		boolean fromLeft = newIndex < currentIndex;
		currentIndex = newIndex;

		final Widget widget = widgets.get(newIndex);
		final Widget current = layoutPanel.getWidget(0);

		// Initialize the layout.
		layoutPanel.add(widget);
		layoutPanel.setWidgetLeftWidth(current, 0, Unit.PCT, 100, Unit.PCT);
		if (fromLeft) {
			layoutPanel.setWidgetLeftWidth(widget, -100, Unit.PCT, 100, Unit.PCT);
		} else {
			layoutPanel.setWidgetLeftWidth(widget, 100, Unit.PCT, 100, Unit.PCT);
		}
		layoutPanel.forceLayout();

		// Slide into view.
		if (fromLeft) {
			layoutPanel.setWidgetLeftWidth(current, 100, Unit.PCT, 100, Unit.PCT);
		} else {
			layoutPanel.setWidgetLeftWidth(current, -100, Unit.PCT, 100, Unit.PCT);
		}
		layoutPanel.setWidgetLeftWidth(widget, 0, Unit.PCT, 100, Unit.PCT);
		layoutPanel.animate(500, new Layout.AnimationCallback() {

			public void onAnimationComplete() {
				// Remove the old widget when the animation completes.
				layoutPanel.remove(current);
				if (eventBus != null) {
					eventBus.fireEvent(new AnimationCompleteEvent(widget, newIndex));
				}
			}

			public void onLayout(Layer layer, double progress) {
			}
		});
	}
}
