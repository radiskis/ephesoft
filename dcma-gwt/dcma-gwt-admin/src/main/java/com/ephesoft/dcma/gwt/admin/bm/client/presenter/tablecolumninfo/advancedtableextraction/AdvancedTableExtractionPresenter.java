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

package com.ephesoft.dcma.gwt.admin.bm.client.presenter.tablecolumninfo.advancedtableextraction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.gwt.admin.bm.client.AdminConstants;
import com.ephesoft.dcma.gwt.admin.bm.client.BatchClassManagementController;
import com.ephesoft.dcma.gwt.admin.bm.client.i18n.BatchClassManagementMessages;
import com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter;
import com.ephesoft.dcma.gwt.admin.bm.client.view.tablecolumninfo.advancedtableextraction.AdvancedTableExtractionView;
import com.ephesoft.dcma.gwt.core.client.EphesoftAsyncCallback;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.TableColumnInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.google.gwt.event.shared.HandlerManager;

/**
 * The presenter for view that shows the advanced table extraction view details.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client.presenter.AbstractBatchClassPresenter
 */
public class AdvancedTableExtractionPresenter extends AbstractBatchClassPresenter<AdvancedTableExtractionView> {

	/**
	 * columnNameToDTOMap Map<String, TableColumnInfoDTO>.
	 */
	private Map<String, TableColumnInfoDTO> columnNameToDTOMap = new HashMap<String, TableColumnInfoDTO>();

	/**
	 * displayImageName String.
	 */
	private String displayImageName;

	/**
	 * Constructor.
	 * 
	 * @param controller BatchClassManagementController
	 * @param view AdvancedTableExtractionView
	 */
	public AdvancedTableExtractionPresenter(BatchClassManagementController controller, AdvancedTableExtractionView view) {
		super(controller, view);
	}

	/**
	 * To handle events.
	 * 
	 * @param eventBus HandlerManager
	 */
	@Override
	public void injectEvents(HandlerManager eventBus) {
		// TODO Auto-generated method stub

	}

	/**
	 * Processing to be done on load of this presenter.
	 */
	@Override
	public void bind() {
		TableColumnInfoDTO selectedTableColumnInfo = controller.getSelectedTableColumnInfoField();
		TableInfoDTO selectedTableInfoField = controller.getSelectedTableInfoField();
		if (selectedTableColumnInfo != null) {
			this.displayImageName = selectedTableInfoField.getDisplayImage();
			view.setBatchClassID(controller.getBatchClass().getIdentifier());
			if (controller.getSelectedDocument() != null) {
				view.setDocName(controller.getSelectedDocument().getName());
			}
			view.setColStartCoord(selectedTableColumnInfo.getColumnStartCoordinate());
			view.setColEndCoord(selectedTableColumnInfo.getColumnEndCoordinate());
			view.setTableColumnInfoList(selectedTableInfoField);
			view.setSelectedTableColumn(selectedTableColumnInfo.getColumnName());
		}
		view.clearImageUpload();
	}

	/**
	 * To get Image URL.
	 */
	public void getImageURL() {
		if (this.displayImageName != null && !this.displayImageName.isEmpty()) {
			getPageImageUrl(view.getBatchClassID(), view.getDocName(), this.displayImageName);
		}
	}

	/**
	 * To perform operations on save button click.
	 */
	public void onSaveButtonClicked() {
		saveDataInDto();
		view.removeAllOverlays();
		controller.getView().toggleBottomPanelShowHide(true);
		controller.getMainPresenter().showTableInfoView(false);
	}

	/**
	 * To save data in DTO.
	 */
	private void saveDataInDto() {
		final TableInfoDTO tableInfoDTO = controller.getSelectedTableInfoField();
		if (tableInfoDTO != null) {
			if (this.displayImageName != null && !this.displayImageName.isEmpty()) {
				tableInfoDTO.setDisplayImage(this.displayImageName);
			}
			final List<TableColumnInfoDTO> tableColumnInfoDTOs = tableInfoDTO.getColumnInfoDTOs(false);
			if (this.columnNameToDTOMap != null && !this.columnNameToDTOMap.isEmpty() && tableColumnInfoDTOs != null) {
				for (final TableColumnInfoDTO tableColumnInfoDTO : tableColumnInfoDTOs) {
					mergTableColInfoDTOs(tableColumnInfoDTO);
				}
			}
		}
	}

	private void mergTableColInfoDTOs(final TableColumnInfoDTO tableColumnInfoDTO) {
		if (tableColumnInfoDTO != null) {
			final String columnName = tableColumnInfoDTO.getColumnName();
			if (columnName != null && !columnName.isEmpty()) {
				validateAndMergColum(tableColumnInfoDTO, columnName);
			}
		}
	}

	private void validateAndMergColum(final TableColumnInfoDTO tableColumnInfoDTO, final String columnName) {
		final TableColumnInfoDTO localTableColumnDTO = this.columnNameToDTOMap.get(columnName);
		if (localTableColumnDTO != null) {
			mergeTableColumnsInfoDTOs(tableColumnInfoDTO, localTableColumnDTO);
		}
	}

	/**
	 * To get Page Image Url.
	 * 
	 * @param batchClassId String
	 * @param documentName String
	 * @param imageName String
	 */
	public void getPageImageUrl(final String batchClassId, final String documentName, final String imageName) {
		controller.getRpcService().getAdvancedTEImageUploadPath(batchClassId, documentName, imageName,
				new EphesoftAsyncCallback<String>() {

					@Override
					public void customFailure(Throwable paramThrowable) {
						ScreenMaskUtility.unmaskScreen();
						view.removeAllOverlays();
						ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
								BatchClassManagementMessages.ERROR_RETRIEVING_PATH));
					}

					@Override
					public void onSuccess(String pathUrl) {
						if (null == pathUrl || pathUrl.isEmpty()) {
							ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(
									BatchClassManagementMessages.ERROR_RETRIEVING_PATH));
							view.removeAllOverlays();
						} else {
							view.setPageImageUrl(pathUrl);
						}
						ScreenMaskUtility.unmaskScreen();
					}
				});
	}

	/**
	 * To perform operations on cancel button click.
	 */
	public void onCancelButtonClicked() {
		if (controller.isAdd()) {
			controller.setAdd(false);
		}
		view.removeAllOverlays();
		controller.getView().toggleBottomPanelShowHide(true);
		controller.getMainPresenter().showTableInfoView(false);

	}

	/**
	 * To set selected table column info DTO.
	 * 
	 * @param selColumnIdentfier String
	 */
	public void setSelectedTableColumnInfoDTO(final String selColumnIdentfier) {
		if (selColumnIdentfier != null && !selColumnIdentfier.isEmpty()) {
			TableColumnInfoDTO tcInfoDTO = controller.getSelectedTableInfoField().getTCInfoDTOByIdentifier(selColumnIdentfier);
			controller.getBatchClassManagementPresenter().getBatchClassBreadCrumbPresenter().createBreadCrumb(tcInfoDTO);
			controller.setSelectedTableColumnInfoField(tcInfoDTO);
			setColumnCoordAndCreateOverlay();
		}
	}

	/**
	 * To set column coordinates and create overlay.
	 */
	public void setColumnCoordAndCreateOverlay() {
		TableColumnInfoDTO localTableColumnInfoDTO = this.columnNameToDTOMap.get(controller.getSelectedTableColumnInfoField()
				.getColumnName());
		if (localTableColumnInfoDTO != null) {
			String startCoord = localTableColumnInfoDTO.getColumnStartCoordinate();
			String endCoord = localTableColumnInfoDTO.getColumnEndCoordinate();
			String colCoordY0 = localTableColumnInfoDTO.getColumnCoordY0();
			String colCoordY1 = localTableColumnInfoDTO.getColumnCoordY1();
			if (startCoord != null) {
				view.setColStartCoord(startCoord);
			} else {
				view.setColStartCoord(AdminConstants.EMPTY_STRING);
			}
			if (endCoord != null) {
				view.setColEndCoord(endCoord);
			} else {
				view.setColEndCoord(AdminConstants.EMPTY_STRING);
			}
			view.removeAllOverlays();
			if (startCoord != null && endCoord != null && colCoordY0 != null && !startCoord.isEmpty() && !endCoord.isEmpty()
					&& !colCoordY0.isEmpty() && !colCoordY1.isEmpty()) {
				view.setOverlayCoordinates(Integer.parseInt(startCoord), Integer.parseInt(endCoord), Integer.parseInt(colCoordY0),
						Integer.parseInt(colCoordY1));
				view.createOverlay(Integer.parseInt(startCoord), Integer.parseInt(endCoord), Integer.parseInt(colCoordY0), Integer
						.parseInt(colCoordY1), 1);
			}
		} else {
			view.removeAllOverlays();
		}
	}

	private void clearColumnCoordinates() {
		view.setColStartCoord(AdminConstants.EMPTY_STRING);
		view.setColEndCoord(AdminConstants.EMPTY_STRING);
	}

	/**
	 * To add column to DTO map.
	 * 
	 * @param columnName String
	 * @param tableColumnInfoDTO TableColumnInfoDTO
	 */
	public void addToDtoMap(final String columnName, final TableColumnInfoDTO tableColumnInfoDTO) {
		if (this.columnNameToDTOMap == null) {
			this.columnNameToDTOMap = new HashMap<String, TableColumnInfoDTO>();
		}
		TableColumnInfoDTO localTableColDTO = createLocalDTO(tableColumnInfoDTO);
		this.columnNameToDTOMap.put(columnName, localTableColDTO);
	}

	private void mergeTableColumnsInfoDTOs(final TableColumnInfoDTO tableColumnInfoDTO, final TableColumnInfoDTO localTableColumnDTO) {
		tableColumnInfoDTO.setBetweenLeft(localTableColumnDTO.getBetweenLeft());
		tableColumnInfoDTO.setBetweenRight(localTableColumnDTO.getBetweenRight());
		tableColumnInfoDTO.setColumnName(localTableColumnDTO.getColumnName());
		tableColumnInfoDTO.setColumnPattern(localTableColumnDTO.getColumnPattern());
		tableColumnInfoDTO.setRequired(localTableColumnDTO.isRequired());
		tableColumnInfoDTO.setColumnHeaderPattern(localTableColumnDTO.getColumnHeaderPattern());
		tableColumnInfoDTO.setColumnStartCoordinate(localTableColumnDTO.getColumnStartCoordinate());
		tableColumnInfoDTO.setColumnEndCoordinate(localTableColumnDTO.getColumnEndCoordinate());
		tableColumnInfoDTO.setColumnCoordY0(localTableColumnDTO.getColumnCoordY0());
		tableColumnInfoDTO.setColumnCoordY1(localTableColumnDTO.getColumnCoordY1());
	}

	private TableColumnInfoDTO createLocalDTO(final TableColumnInfoDTO tableColumnInfoDTO) {
		final TableColumnInfoDTO columnInfoDTO = new TableColumnInfoDTO();
		mergeTableColumnsInfoDTOs(columnInfoDTO, tableColumnInfoDTO);
		return columnInfoDTO;
	}

	/**
	 * To clear column DTO Map.
	 */
	public void clearColumnDTOMap() {
		if (this.columnNameToDTOMap == null) {
			this.columnNameToDTOMap.clear();
		}
	}

	/**
	 * To set Column Coordinates in DTO.
	 * 
	 * @param xCoordinate0 String
	 * @param xCoordinate1 String
	 * @param yCoordinate0 String
	 * @param yCoordinate1 String
	 */
	public void setColumnCoordinatesInDTO(final String xCoordinate0, final String xCoordinate1, final String yCoordinate0,
			final String yCoordinate1) {
		TableColumnInfoDTO tableColumnInfoDTO = controller.getSelectedTableColumnInfoField();
		TableColumnInfoDTO localTableColumnInfoDTO = null;
		if (this.columnNameToDTOMap != null && tableColumnInfoDTO != null) {
			final String columnName = tableColumnInfoDTO.getColumnName();
			if (columnName != null) {
				localTableColumnInfoDTO = this.columnNameToDTOMap.get(columnName);
				if (localTableColumnInfoDTO != null) {
					localTableColumnInfoDTO.setColumnStartCoordinate(xCoordinate0);
					localTableColumnInfoDTO.setColumnEndCoordinate(xCoordinate1);
				}
				localTableColumnInfoDTO.setColumnCoordY0(yCoordinate0);
				localTableColumnInfoDTO.setColumnCoordY1(yCoordinate1);
			}
		}
	}

	/**
	 * To clear coordinates for selected column.
	 */
	public void clearCoordinatesForSelectedColumn() {
		TableColumnInfoDTO tableColumnInfoDTO = controller.getSelectedTableColumnInfoField();
		view.removeAllOverlays();
		if (this.columnNameToDTOMap != null && tableColumnInfoDTO != null) {
			TableColumnInfoDTO localTableColumnInfoDTO = this.columnNameToDTOMap.get(tableColumnInfoDTO.getColumnName());
			clear(localTableColumnInfoDTO);
		}
	}

	/**
	 * To clear coordinates for all columns.
	 */
	public void clearCoordinatesForAllColumns() {
		view.removeAllOverlays();
		if (this.columnNameToDTOMap != null) {
			Set<String> columnNames = this.columnNameToDTOMap.keySet();
			if (columnNames != null && !columnNames.isEmpty()) {
				for (String columnName : columnNames) {
					TableColumnInfoDTO localTableColumnInfoDTO = this.columnNameToDTOMap.get(columnName);
					clear(localTableColumnInfoDTO);
				}
			}
		}
	}

	private void clear(TableColumnInfoDTO localTableColumnInfoDTO) {
		if (localTableColumnInfoDTO != null) {
			localTableColumnInfoDTO.setColumnStartCoordinate(AdminConstants.EMPTY_STRING);
			localTableColumnInfoDTO.setColumnEndCoordinate(AdminConstants.EMPTY_STRING);
			localTableColumnInfoDTO.setColumnCoordY0(AdminConstants.EMPTY_STRING);
			localTableColumnInfoDTO.setColumnCoordY1(AdminConstants.EMPTY_STRING);
			clearColumnCoordinates();
		}
	}

	/**
	 * To set display image name.
	 * 
	 * @param displayImageName String
	 */
	public void setDisplayImageName(final String displayImageName) {
		this.displayImageName = displayImageName;
	}

}
