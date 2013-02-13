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

package com.ephesoft.dcma.gwt.foldermanager.client.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.FileWrapper;
import com.ephesoft.dcma.gwt.core.client.View;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.view.ContextMenuPanel;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog.DialogListener;
import com.ephesoft.dcma.gwt.foldermanager.client.presenter.FolderTablePresenter;
import com.ephesoft.dcma.gwt.foldermanager.client.view.widget.FolderUploadWidget;
import com.ephesoft.dcma.gwt.foldermanager.shared.constant.FolderManagementConstants;
import com.ephesoft.dcma.gwt.foldermanager.shared.constant.FolderManagementMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FolderTableView extends View<FolderTablePresenter> {

	@UiField
	protected HorizontalPanel optionsPanel;

	@UiField
	protected FlexTable headers;

	@UiField
	protected FlexTable folderDisplayTable;

	@UiField
	protected ScrollPanel folderScrollPanel;

	interface Binder extends UiBinder<DockLayoutPanel, FolderTableView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);
	private static final String _100PX = "100px";
	private final List<FolderTableRow> folderTableRows;
	private FolderUploadWidget folderUploadWidget;
	private final Button deleteSelected;
	private final Button createNewFolder;
	private final Button refresh;
	private final Button cut;
	private final Button copy;
	private final Button paste;
	private final Button folderUpButton;
	private HTML anchorHtml;
	private SortingOption lastSortingOption = SortingOption.NAME;
	private static final String PERCENTAGE_FIVE = "5%";
	private static final String PERCENTAGE_FOURTY_EIGHT = "48%";
	private static final String PERCENTAGE_TWENTY_EIGHT = "28%";
	private static final String PERCENTAGE_ELEVEN = "11%";
	private static final String PERCENTAGE_NINE = "9%";
	private static final String PERCENTAGE_HUNDRED = "100%";

	@UiField
	protected VerticalPanel footerPanel;

	private CheckBox selectAll;

	private boolean nameOrderAscending = true;

	private boolean dateOrderAscending = true;

	private boolean typeOrderAscending = true;

	private VerticalPanel multipleSelectionPanel;
	private VerticalPanel folderPathOperationsPanel;
	private static final String OPTIONS_CSS = FolderManagementConstants.FILES_TABLE_OPTIONS + FolderManagementConstants.SPACE
			+ FolderManagementConstants.WORD_WRAP + FolderManagementConstants.SPACE + FolderManagementConstants.OPTIONS_PANEL_CSS;

	public FolderTableView(Label footer) {
		super();
		initWidget(BINDER.createAndBindUi(this));
		folderDisplayTable.addStyleName(FolderManagementConstants.TABLE_BORDER_COLLAPSE);
		headers.addStyleName(FolderManagementConstants.TABLE_BORDER_COLLAPSE);
		folderScrollPanel.setAlwaysShowScrollBars(true);
		footerPanel.addStyleName(FolderManagementConstants.FE_FOOTER);
		footerPanel.setWidth(PERCENTAGE_HUNDRED);
		footerPanel.add(footer);
		footerPanel.setCellHorizontalAlignment(footer, HasHorizontalAlignment.ALIGN_RIGHT);
		this.folderTableRows = new ArrayList<FolderTableRow>();

		this.cut = new Button(LocaleDictionary.get().getConstantValue(FolderManagementConstants.CUT), new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.onCutClicked();
			}
		});
		this.createNewFolder = new Button(LocaleDictionary.get().getConstantValue(FolderManagementConstants.CREATE_NEW_FOLDER),
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent arg0) {
						presenter.onNewFolderClicked();

					}
				});
		this.copy = new Button(LocaleDictionary.get().getConstantValue(FolderManagementConstants.COPY), new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.onCopyClicked();
			}
		});
		this.paste = new Button(LocaleDictionary.get().getConstantValue(FolderManagementConstants.PASTE), new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.onPasteClicked();
			}
		});
		this.deleteSelected = new Button(LocaleDictionary.get().getConstantValue(FolderManagementConstants.DELETE),
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						presenter.onDeleteClicked();
					}
				});
		this.refresh = new Button(LocaleDictionary.get().getConstantValue(FolderManagementConstants.REFRESH), new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.onRefreshClicked();
			}
		});

		this.folderUpButton = new Button(LocaleDictionary.get().getConstantValue(FolderManagementConstants.FOLDER_UP),
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent arg0) {
						presenter.onFolderUpClicked();
					}
				});
		createTableHeader();
		sinkEvents(Event.ONCONTEXTMENU | Event.ONDBLCLICK);
		setPasteEnabled(false);
	}

	private final void createTableHeader() {
		selectAll = new CheckBox();
		selectAll.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Boolean isSelectAllSelected = selectAll.getValue();
				for (FolderTableRow folderTableRow : folderTableRows) {
					folderTableRow.checkBox.setValue(isSelectAllSelected);
				}
			}
		});
		headers.setWidget(0, 0, selectAll);
		String name = LocaleDictionary.get().getConstantValue(FolderManagementConstants.NAME);

		String modifiedAt = LocaleDictionary.get().getConstantValue(FolderManagementConstants.MODIFIED_AT);
		String type = LocaleDictionary.get().getConstantValue(FolderManagementConstants.TYPE);

		HTML nameHTML = new HTML(name);
		HTML modifiedAtHTML = new HTML(modifiedAt);
		HTML typeHTML = new HTML(type);
		final HorizontalPanel nameHorizontalPanel = new HorizontalPanel();
		final HorizontalPanel modifiedHorizontalPanel = new HorizontalPanel();
		final HorizontalPanel typeHorizontalPanel = new HorizontalPanel();
		nameHorizontalPanel.add(nameHTML);
		modifiedHorizontalPanel.add(modifiedAtHTML);
		typeHorizontalPanel.add(typeHTML);

		nameHorizontalPanel.addStyleName(FolderManagementConstants.CURSOR_HAND);
		modifiedHorizontalPanel.addStyleName(FolderManagementConstants.CURSOR_HAND);
		typeHorizontalPanel.addStyleName(FolderManagementConstants.CURSOR_HAND);
		nameHTML.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				nameOrderAscending = toggleSortingAnchor(nameHorizontalPanel, nameOrderAscending);
				lastSortingOption = SortingOption.NAME;
				updateTableContent(presenter.getFolderPath());
			}

		});
		modifiedAtHTML.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				dateOrderAscending = toggleSortingAnchor(modifiedHorizontalPanel, dateOrderAscending);
				lastSortingOption = SortingOption.DATE;
				updateTableContent(presenter.getFolderPath());
			}
		});
		typeHTML.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				typeOrderAscending = toggleSortingAnchor(typeHorizontalPanel, typeOrderAscending);
				lastSortingOption = SortingOption.TYPE;
				updateTableContent(presenter.getFolderPath());
			}

		});

		headers.setWidget(0, 1, nameHorizontalPanel);
		headers.setWidget(0, 2, modifiedHorizontalPanel);
		headers.setWidget(0, 3, typeHorizontalPanel);
		headers.getCellFormatter().addStyleName(
				0,
				0,
				FolderManagementConstants.FILES_TABLE_SMALL_COLUMN + FolderManagementConstants.SPACE
						+ FolderManagementConstants.FILES_TABLE_HEADER_CELL);
		headers.getCellFormatter().addStyleName(0, 1, FolderManagementConstants.FILES_TABLE_HEADER_CELL);
		headers.getCellFormatter().addStyleName(0, 2, FolderManagementConstants.FILES_TABLE_HEADER_CELL);
		headers.getCellFormatter().addStyleName(0, 3, FolderManagementConstants.FILES_TABLE_HEADER_CELL);
		headers.getColumnFormatter().setWidth(0, PERCENTAGE_FIVE);
		headers.getColumnFormatter().setWidth(1, PERCENTAGE_FOURTY_EIGHT);
		headers.getColumnFormatter().setWidth(2, PERCENTAGE_TWENTY_EIGHT);
		headers.getColumnFormatter().setWidth(3, PERCENTAGE_ELEVEN);
		headers.setWidth(PERCENTAGE_HUNDRED);
	}

	private boolean toggleSortingAnchor(HorizontalPanel horizontalPanel, boolean ascending) {
		if (anchorHtml != null) {
			anchorHtml.removeFromParent();
		}
		if (ascending) {
			anchorHtml = new HTML(FolderManagementConstants.SORT_UP);
		} else {
			anchorHtml = new HTML(FolderManagementConstants.SORT_DOWN);
		}
		horizontalPanel.add(anchorHtml);
		return !ascending;
	}

	private final void updateTableContent(String folderPath) {
		if (null != folderTableRows && !folderTableRows.isEmpty()) {
			List<FileWrapper> fileWrappers = new ArrayList<FileWrapper>();
			for (FolderTableRow folderTableRow : folderTableRows) {
				fileWrappers.add(folderTableRow.file);
			}
			updateTableContent(folderPath, fileWrappers);
		}
	}

	private void showTableOptions() {
		folderUploadWidget.setVisible(true);
		if (multipleSelectionPanel == null) {
			multipleSelectionPanel = new VerticalPanel();
			HorizontalPanel labelPanel = new HorizontalPanel();
			Label label = new Label(LocaleDictionary.get().getMessageValue(FolderManagementMessages.MULTI_SELECT_OPTIONS));
			labelPanel.add(label);
			labelPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			multipleSelectionPanel.add(labelPanel);
			HorizontalPanel multipleSelectionButtonPanel = new HorizontalPanel();
			multipleSelectionPanel.add(multipleSelectionButtonPanel);
			multipleSelectionButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			multipleSelectionButtonPanel.add(cut);
			multipleSelectionButtonPanel.add(copy);
			multipleSelectionButtonPanel.add(paste);
			multipleSelectionButtonPanel.add(deleteSelected);
			multipleSelectionButtonPanel.setSpacing(2);
		}
		if (folderPathOperationsPanel == null) {
			folderPathOperationsPanel = new VerticalPanel();
			HorizontalPanel folderLabelPanel = new HorizontalPanel();
			Label label = new Label(LocaleDictionary.get().getConstantValue(FolderManagementConstants.FOLDER));
			folderLabelPanel.add(label);
			folderLabelPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			HorizontalPanel folderPathOperationButtonsPanel = new HorizontalPanel();
			folderPathOperationButtonsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			folderPathOperationButtonsPanel.setSpacing(2);
			createNewFolder.setWidth(_100PX);
			folderPathOperationButtonsPanel.add(createNewFolder);
			folderPathOperationButtonsPanel.add(folderUpButton);
			folderPathOperationButtonsPanel.add(refresh);
			folderPathOperationsPanel.add(folderLabelPanel);
			folderPathOperationsPanel.add(folderPathOperationButtonsPanel);
		}
		optionsPanel.add(folderPathOperationsPanel);
		optionsPanel.setCellHorizontalAlignment(folderPathOperationsPanel, HasHorizontalAlignment.ALIGN_LEFT);
		optionsPanel.add(folderUploadWidget);
		optionsPanel.setCellHorizontalAlignment(folderUploadWidget, HasHorizontalAlignment.ALIGN_CENTER);
		optionsPanel.add(multipleSelectionPanel);
		optionsPanel.setCellHorizontalAlignment(multipleSelectionPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		optionsPanel.addStyleName(OPTIONS_CSS);
	}

	/**
	 * This method updates table content on path change or for sorting option clicked.
	 * 
	 * @param path current selected path
	 * @param fileSortedList list of content to be sorted
	 */
	public final void updateTableContent(String path, List<FileWrapper> fileSortedList) {
		cleanupTableContent();
		List<FileWrapper> sortedFileList = sort(fileSortedList, lastSortingOption);
		folderDisplayTable.removeRow(0);
		setEnableAttributeForTableContent(true);
		folderUpButton.setEnabled(!path.equals(presenter.getParentFolderPath()));
		presenter.setFolderPath(path);
		presenter.onPathChange(path);
		for (FileWrapper file : sortedFileList) {
			insertFileRow(file);
		}
	}

	private void insertFileRow(final FileWrapper file) {
		int numRows = folderDisplayTable.getRowCount();

		CheckBox checkBox = new CheckBox();
		final String fileName = file.getName();
		FolderTableRow fileTableRow = new FolderTableRow(checkBox, file);
		folderTableRows.add(fileTableRow);
		folderDisplayTable.setWidget(numRows, 0, checkBox);
		HTML fileIconedName = getFileIconedName(fileName, file.getKind());
		fileIconedName.addStyleName(FolderManagementConstants.CURSOR_HAND);
		addHandlersToFileIconedName(file, fileName, fileIconedName);

		folderDisplayTable.setWidget(numRows, 1, fileIconedName);
		folderDisplayTable.setText(numRows, 2, file.getModified());
		folderDisplayTable.setText(numRows, 3, file.getKind().toString());
		folderDisplayTable.getCellFormatter().addStyleName(numRows, 0, FolderManagementConstants.FILES_TABLE_SMALL_COLUMN);
		folderDisplayTable.getCellFormatter().addStyleName(numRows, 1, FolderManagementConstants.TOP_LINKS);
		folderDisplayTable.getCellFormatter().addStyleName(numRows, 2, FolderManagementConstants.TOP_LINKS);
		folderDisplayTable.getCellFormatter().addStyleName(numRows, 3, FolderManagementConstants.TOP_LINKS);
		if (numRows % 2 != 0) {
			folderDisplayTable.getRowFormatter().addStyleName(numRows, FolderManagementConstants.FILES_TABLE_ODD);
		}
	}

	private void addHandlersToFileIconedName(final FileWrapper file, final String fileName, HTML fileIconedName) {
		fileIconedName.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent mouseDown) {
				presenter.setSelectedFile(file);
				com.google.gwt.user.client.Timer timer = new com.google.gwt.user.client.Timer() {

					@Override
					public void run() {
						presenter.setSelectedFile(null);
					}
				};
				timer.schedule(500);
				if (mouseDown.getNativeButton() == Event.BUTTON_RIGHT) {
					performOperationsOnRightClick(file, fileName, mouseDown);
				}
			}

		});
	}

	private void performOperationsOnRightClick(final FileWrapper file, final String fileName, MouseDownEvent mouseDown) {
		final String absoluteFilePath = file.getPath();
		final ContextMenuPanel contextMenu = new ContextMenuPanel();
		contextMenu.show();
		MenuBar menuBar = new MenuBar(true);
		MenuItem openMenuItem = new MenuItem(LocaleDictionary.get().getConstantValue(FolderManagementConstants.OPEN), new Command() {

			@Override
			public void execute() {
				openItem(file);
				contextMenu.hide();
			}
		});
		MenuItem cutMenuItem = new MenuItem(LocaleDictionary.get().getConstantValue(FolderManagementConstants.CUT), new Command() {

			@Override
			public void execute() {
				presenter.onFileCut(absoluteFilePath);
				setPasteEnabled(true);
				contextMenu.hide();
			}
		});
		MenuItem copyMenuItem = new MenuItem(LocaleDictionary.get().getConstantValue(FolderManagementConstants.COPY), new Command() {

			@Override
			public void execute() {
				presenter.onFileCopy(absoluteFilePath);
				setPasteEnabled(true);
				contextMenu.hide();
			}
		});
		MenuItem renameMenuItem = new MenuItem(LocaleDictionary.get().getConstantValue(FolderManagementConstants.RENAME),
				new Command() {

					@Override
					public void execute() {
						performOperationsOnRename(fileName, contextMenu);
					}
				});

		MenuItem deleteMenuItem = new MenuItem(LocaleDictionary.get().getConstantValue(FolderManagementConstants.DELETE),
				new Command() {

					@Override
					public void execute() {

						final ConfirmationDialog confirmationDialog = ConfirmationDialogUtil
								.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
										FolderManagementMessages.ARE_YOU_SURE_YOU_WANT_TO_DELETE_THE_FILE,
										FolderManagementConstants.QUOTES + fileName + FolderManagementConstants.QUOTES),
										LocaleDictionary.get().getMessageValue(FolderManagementMessages.CONFIRM_DELETE_OPERATION),
										false, true);
						confirmationDialog.addDialogListener(new DialogListener() {

							@Override
							public void onOkClick() {
								confirmationDialog.hide();
								presenter.onFileDelete(fileName, absoluteFilePath);
							}

							@Override
							public void onCancelClick() {
								confirmationDialog.hide();
							}
						});
						confirmationDialog.okButton.setFocus(true);
						contextMenu.hide();
					}
				});
		menuBar.addItem(openMenuItem);
		if (!file.getKind().equals(FileType.DIR)) {
			MenuItem downloadMenuItem = new MenuItem(LocaleDictionary.get().getConstantValue(FolderManagementConstants.DOWNLOAD),
					new Command() {

						@Override
						public void execute() {
							contextMenu.hide();
							presenter.onFileDownload(file);
						}
					});
			menuBar.addItem(downloadMenuItem);
		}
		menuBar.addItem(cutMenuItem);
		menuBar.addItem(copyMenuItem);
		menuBar.addItem(renameMenuItem);
		menuBar.addItem(deleteMenuItem);

		contextMenu.setWidget(menuBar);
		contextMenu.setPopupPosition(mouseDown.getNativeEvent().getClientX(), mouseDown.getNativeEvent().getClientY());
		contextMenu.show();
	}

	private void performOperationsOnRename(final String fileName, final ContextMenuPanel contextMenu) {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.addStyleName(FolderManagementConstants.CONFIGURABLE_DIALOG_BOX);
		dialogBox.setText(LocaleDictionary.get().getMessageValue(FolderManagementMessages.RENAME_TO));
		final TextBox renameTextBox = new TextBox();
		renameTextBox.setText(fileName);
		HorizontalPanel renameTextBoxPanel = new HorizontalPanel();
		renameTextBoxPanel.add(renameTextBox);
		HorizontalPanel buttonsPanel = new HorizontalPanel();
		Button okButton = new Button(LocaleDictionary.get().getConstantValue(FolderManagementConstants.OK_STRING), new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				final String newFileName = renameTextBox.getText();
				presenter.onRename(fileName, newFileName);
			}
		});
		Button cancelButton = new Button(LocaleDictionary.get().getConstantValue(FolderManagementConstants.CANCEL),
				new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						dialogBox.hide();
					}
				});
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		VerticalPanel layoutPanel = new VerticalPanel();
		layoutPanel.add(renameTextBoxPanel);
		layoutPanel.add(buttonsPanel);
		dialogBox.add(layoutPanel);
		dialogBox.center();
		dialogBox.show();
		renameTextBox.setFocus(true);
		renameTextBox.selectAll();
		contextMenu.hide();
	}

	private class FolderTableRow {

		private final CheckBox checkBox;
		private final FileWrapper file;

		public FolderTableRow(CheckBox checkBox, FileWrapper file) {
			this.checkBox = checkBox;
			this.file = file;
		}

	}

	private enum SortingOption {
		NAME, DATE, TYPE;
	}

	public List<String> getSelectedFileList() {
		List<String> selectedFileList = new ArrayList<String>();
		for (FolderTableRow folderTableRow : folderTableRows) {
			if (folderTableRow.checkBox.getValue()) {
				selectedFileList.add(folderTableRow.file.getPath());
			}
		}
		return selectedFileList;
	}

	private List<FileWrapper> sort(List<FileWrapper> fileSortedList, final SortingOption sortingOption) {
		final Set<FileWrapper> set = new TreeSet<FileWrapper>(new Comparator<FileWrapper>() {

			public int compare(final FileWrapper firstFile, final FileWrapper secondFile) {
				int returnValue = 0;
				switch (sortingOption) {
					case NAME:
						if (nameOrderAscending) {
							returnValue = firstFile.getName().compareToIgnoreCase(secondFile.getName());
						} else {
							returnValue = secondFile.getName().compareToIgnoreCase(firstFile.getName());
						}
						break;
					case DATE:
						if (dateOrderAscending) {
							returnValue = firstFile.getModifiedTimeInSeconds().compareTo(secondFile.getModifiedTimeInSeconds());
						} else {
							returnValue = secondFile.getModifiedTimeInSeconds().compareTo(firstFile.getModifiedTimeInSeconds());
						}
						if (returnValue == 0) {
							if (!nameOrderAscending) {
								returnValue = firstFile.getName().compareToIgnoreCase(secondFile.getName());
							} else {
								returnValue = secondFile.getName().compareToIgnoreCase(firstFile.getName());
							}
						}
						break;
					case TYPE:
						if (typeOrderAscending) {
							returnValue = firstFile.getKind().getExtension().compareToIgnoreCase(secondFile.getKind().getExtension());
						} else {
							returnValue = secondFile.getKind().getExtension().compareToIgnoreCase(firstFile.getKind().getExtension());
						}
						if (returnValue == 0) {
							if (!nameOrderAscending) {
								returnValue = firstFile.getName().compareToIgnoreCase(secondFile.getName());
							} else {
								returnValue = secondFile.getName().compareToIgnoreCase(firstFile.getName());
							}
						}
						break;
					default:
						break;
				}
				return returnValue;
			}
		});

		set.addAll(fileSortedList);
		final List<FileWrapper> sortedfileList = new ArrayList<FileWrapper>();
		sortedfileList.addAll(set);
		return sortedfileList;
	}

	public final void cleanupTableContent() {
		folderTableRows.clear();
		folderDisplayTable.removeAllRows();
		folderDisplayTable.getColumnFormatter().setWidth(0, PERCENTAGE_FIVE);
		folderDisplayTable.getColumnFormatter().setWidth(1, PERCENTAGE_FOURTY_EIGHT);
		folderDisplayTable.getColumnFormatter().setWidth(2, PERCENTAGE_TWENTY_EIGHT);
		folderDisplayTable.getColumnFormatter().setWidth(3, PERCENTAGE_NINE);
		folderDisplayTable.setWidth(PERCENTAGE_HUNDRED);
		
		// These empty labels are for alignment purpose only
		final Label emptyLabelCol1 = new Label();
		final Label emptyLabelCol3 = new Label();
		final Label emptyLabelCol4 = new Label();
		
		emptyLabelCol1.setWidth(PERCENTAGE_FIVE);
		emptyLabelCol3.setWidth(PERCENTAGE_TWENTY_EIGHT);
		emptyLabelCol4.setWidth(PERCENTAGE_NINE);
		folderDisplayTable.setWidget(0, 0, emptyLabelCol1);
		folderDisplayTable.setWidget(0, 2, emptyLabelCol3);
		folderDisplayTable.setWidget(0, 3, emptyLabelCol4);
		HTML html = new HTML(FolderManagementConstants.IMAGE_LOADING);
		folderDisplayTable.setWidget(0, 1, html);
		folderDisplayTable.getCellFormatter()
				.setAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		selectAll.setValue(false);
	}

	/**
	 * This method adds message to table when no content is present at selected location.
	 */
	public void addNoContentLabel() {
		Label noContentLabel = new Label(LocaleDictionary.get().getMessageValue(FolderManagementMessages.NO_CONTENT_MESSAGE));
		final Label emptyLabelCol1 = new Label();
		final Label emptyLabelCol3 = new Label();
		final Label emptyLabelCol4 = new Label();
		
		emptyLabelCol1.setWidth(PERCENTAGE_FIVE);
		emptyLabelCol3.setWidth(PERCENTAGE_TWENTY_EIGHT);
		emptyLabelCol4.setWidth(PERCENTAGE_NINE);
		folderDisplayTable.setWidget(0, 0, emptyLabelCol1);
		folderDisplayTable.setWidget(0, 2, emptyLabelCol3);
		folderDisplayTable.setWidget(0, 3, emptyLabelCol4);
		folderDisplayTable.setWidget(0, 1, noContentLabel);
		folderDisplayTable.getCellFormatter()
				.setAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		setEnableAttributeForTableContent(false);
	}

	/**
	 * This method sets buttons enable attribute according to table content.
	 * 
	 * @param enable to enable or disable
	 */
	public final void setEnableAttributeForTableContent(boolean enable) {
		createNewFolder.setEnabled(enable);
		folderUploadWidget.setEnableUploadButton(enable);
		folderUpButton.setEnabled(enable);
		if (enable) {
			List<String> cutOrCopyFileList = presenter.getCutOrCopyFileList();
			if (null != cutOrCopyFileList && !cutOrCopyFileList.isEmpty()) {
				paste.setEnabled(true);
			}
		} else {
			paste.setEnabled(false);
		}
	}

	private HTML getFileIconedName(String name, FileType type) {
		HTML html = null;
		if (type == FileType.DIR) {
			html = new HTML(FolderManagementConstants.ICON_FOLDER + FolderManagementConstants.OPEN_SPAN_TAG + name
					+ FolderManagementConstants.CLOSE_SPAN_TAG);
		} else if (type == FileType.MM) {
			html = new HTML(FolderManagementConstants.ICON_MM + FolderManagementConstants.OPEN_SPAN_TAG + name
					+ FolderManagementConstants.CLOSE_SPAN_TAG);
		} else if (type == FileType.IMG) {
			html = new HTML(FolderManagementConstants.ICON_IMG + FolderManagementConstants.OPEN_SPAN_TAG + name
					+ FolderManagementConstants.CLOSE_SPAN_TAG);
		} else if (type == FileType.DOC) {
			html = new HTML(FolderManagementConstants.ICON_DOC + FolderManagementConstants.OPEN_SPAN_TAG + name
					+ FolderManagementConstants.CLOSE_SPAN_TAG);
		} else {
			html = new HTML(FolderManagementConstants.ICON_OTHER + FolderManagementConstants.OPEN_SPAN_TAG + name
					+ FolderManagementConstants.CLOSE_SPAN_TAG);
		}
		return html;
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
			case Event.ONCONTEXTMENU:
				event.cancelBubble(true);
				event.preventDefault();
				break;
			case Event.ONDBLCLICK:
				presenter.openItem();
			default:
				break;
		}
	}

	private void openItem(final FileWrapper file) {
		presenter.openItem(file);
	}

	public final void setPasteEnabled(boolean isEnabled) {
		paste.setEnabled(isEnabled);

	}

	public void setFileUploadWidget(FolderUploadWidget folderUploadWidget) {
		this.folderUploadWidget = folderUploadWidget;
		showTableOptions();

	}

}
