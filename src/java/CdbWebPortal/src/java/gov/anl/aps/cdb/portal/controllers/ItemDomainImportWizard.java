/*
 * Copyright (c) UChicago Argonne, LLC. All rights reserved.
 * See LICENSE file.
 */
package gov.anl.aps.cdb.portal.controllers;

import gov.anl.aps.cdb.portal.controllers.ImportHelperBase.SimpleInputHandler;
import gov.anl.aps.cdb.portal.controllers.ImportHelperBase.ImportInfo;
import gov.anl.aps.cdb.portal.controllers.ImportHelperBase.OutputColumnModel;
import gov.anl.aps.cdb.portal.controllers.ImportHelperBase.ValidInfo;
import gov.anl.aps.cdb.portal.model.db.entities.CdbEntity;
import gov.anl.aps.cdb.portal.utilities.SessionUtility;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author craig
 */
@Named(ItemDomainImportWizard.CONTROLLER_NAMED)
@SessionScoped
public class ItemDomainImportWizard implements Serializable {

    public static final String CONTROLLER_NAMED = "importWizard";

    protected static final String tabSelectFile = "SelectFileTab";
    protected static final String tabValidate = "ValidateTab";
    protected static final String tabResults = "ResultsTab";

    protected ImportHelperBase importHelper = null;

    protected String currentTab = tabSelectFile;

    private Boolean disableButtonPrev = true;
    private Boolean disableButtonNext = true;
    private Boolean disableButtonFinish = true;
    private Boolean disableButtonCancel = false;

    // models for select file tab
    private Boolean disableButtonUpload = true;
    protected UploadedFile uploadfileData = null;
    private List<String> sheetNames = new ArrayList<>();
    private String selectedSheet = null;
    private boolean renderSelectSheet = false;
    private String selectFileSummaryMessage = null;
    private String selectFileErrorMessage = null;
    private boolean isValidSheet = false;
    
    // models for validation tab
    protected boolean importSuccessful = true;
    protected String importResult = "";
    private CdbEntity selectedTableRow = null;
    private TreeNode selectedTreeNode = null;
    
    public static ItemDomainImportWizard getInstance() {
        return (ItemDomainImportWizard) SessionUtility.findBean(
                ItemDomainImportWizard.CONTROLLER_NAMED);
    }

    public void registerHelper(ImportHelperBase helper) {
        reset();
        importHelper = helper;
    }

    public Boolean getDisableButtonPrev() {
        return disableButtonPrev;
    }

    public void setDisableButtonPrev(Boolean disableButtonPrev) {
        this.disableButtonPrev = disableButtonPrev;
    }

    public Boolean getDisableButtonNext() {
        return disableButtonNext;
    }

    public Boolean getDisableButtonFinish() {
        return disableButtonFinish;
    }

    public Boolean getDisableButtonCancel() {
        return disableButtonCancel;
    }

    public UploadedFile getUploadfileData() {
        return uploadfileData;
    }

    public void setUploadfileData(UploadedFile uploadfileData) {
        this.uploadfileData = uploadfileData;
    }
    
    public Boolean getDisableButtonUpload() {
        return disableButtonUpload;
    }

    public Boolean getRenderFileuploadData() {
        return uploadfileData == null;
    }

    public Boolean getRenderOutputData() {
        return uploadfileData != null;
    }
    
    public Boolean getRenderSelectSheet() {
        return renderSelectSheet;
    }
    
    public Boolean getRenderFileSummaryMessage() {
        return selectFileSummaryMessage != null;
    }
    
    public Boolean getRenderFileErrorMessage() {
        return selectFileErrorMessage != null;
    }

    public String getUploadfileDataString() {
        if (uploadfileData == null) {
            return "";
        } else {
            return uploadfileData.getFileName();
        }
    }

    public List<String> getSheetNames() {
        return sheetNames;
    }

    public String getSelectedSheet() {
        return selectedSheet;
    }
    
    public String getSelectFileSummaryMessage() {
        return selectFileSummaryMessage;
    }
    
    public String getSelectFileErrorMessage() {
        return selectFileErrorMessage;
    }
    
    public void setSelectedSheet(String selectedSheet) {
        
        if (selectedSheet == null) {
            return;
        }
        
        this.selectedSheet = selectedSheet;
        
        ValidInfo validInfo = importHelper.validateSheet(uploadfileData, selectedSheet);
        if (validInfo.isValid()) {
            isValidSheet = true;
            selectFileSummaryMessage = 
                    "Selected sheet is valid for import. " +
                    "Click 'Next Step' to continue.";
            selectFileErrorMessage = null;
        } else {
            isValidSheet = false;
            selectFileSummaryMessage = 
                    "Selected sheet is not valid for import. " +
                    "Select a different sheet or press cancel to terminate.";
            selectFileErrorMessage = 
                    "Validation error: " + validInfo.getValidString() + ".";
        }
        
        setEnablementForCurrentTab();
    }
    
    public boolean isValidSheet() {
        return isValidSheet;
    }
    
    public void fileUploadListenerData(FileUploadEvent event) {

        uploadfileData = event.getFile();
        
        sheetNames = importHelper.getSheetNames(uploadfileData);
        
        renderSelectSheet = false;
        if (sheetNames.size() > 0) {
            renderSelectSheet = true;
        } else {
            // workbook contains no sheets so not valid for import
            selectFileErrorMessage =
                    "Selected file contains no worksheets. " +
                    "Click cancel to terminate and try again.";
        }
        
        setEnablementForCurrentTab();
    }

    public boolean hasTreeView() {
        if (importHelper != null) {
            return importHelper.hasTreeView();
        } else {
            return false;
        }
    }

    public TreeNode getRootTreeNode() {
        return importHelper.getRootTreeNode();
    }

    public TreeNode getSelectedTreeNode() {
        return selectedTreeNode;
    }
 
    public void setSelectedTreeNode(TreeNode selectedNode) {
        this.selectedTreeNode = selectedNode;
    }
    
    public void treeSelectionChanged(NodeSelectEvent event) {
        CdbEntity selectedItem = (CdbEntity)event.getTreeNode().getData();
        if (getRows().contains(selectedItem)) {
            setSelectedTableRow(selectedItem);
        }
    }
    
    public CdbEntity getSelectedTableRow() {
        return selectedTableRow;
    }
    
    public void setSelectedTableRow(CdbEntity entity) {
        selectedTableRow = entity;
    }
 
    public String getImportResultString() {
        return importResult;
    }

    public String getValidationMessage() {
        return importHelper.getValidationMessage();
    }

    public String getSummaryMessage() {
        return importHelper.getSummaryMessage();
    }

    public List<CdbEntity> getRows() {
        if (importHelper != null) {
            return importHelper.getRows();
        } else {
            return new ArrayList<>();
        }
    }

    public List<OutputColumnModel> getColumns() {
        if (importHelper != null) {
            return importHelper.getTableViewColumns();
        } else {
            return new ArrayList<>();
        }
    }
    
    public StreamedContent getTemplateExcelFile() {
        return importHelper.getTemplateExcelFile();
    }
    
    /**
     * Handles FlowEvents generated by the wizard component. Determines next tab
     * based on current tab, defaults to visiting all tabs but implements
     * special cases. Skips cableDetailsTab when the type is "unspecified" since
     * that tab is empty.
     */
    public String onFlowProcess(FlowEvent event) {

        String nextStep = event.getNewStep();
        String currStep = event.getOldStep();
        
        // parse file if moving from select file tab to validate tab
        if ((currStep.endsWith(tabSelectFile))
                && (nextStep.endsWith(tabValidate))) {
            boolean result = 
                    importHelper.readXlsxFileData(uploadfileData, selectedSheet);
            if (!result) {
                uploadfileData = null;
            }
        }

        // trigger import process if moving from validate tab to results tab
        if ((currStep.endsWith(tabValidate))
                && (nextStep.endsWith(tabResults))) {
            triggerImport();
        }

        setEnablement(nextStep);

        currentTab = nextStep;

        return nextStep;
    }

    protected void triggerImport() {

        ImportInfo info = importHelper.importData();

        importSuccessful = info.isImportSuccessful();
        importResult = info.getMessage();
    }

    /**
     * Resets models for wizard components.
     */
    protected void reset() {
        currentTab = tabSelectFile;
        uploadfileData = null;
        importHelper = null;
        importSuccessful = true;
        importResult = "";
        selectedTableRow = null;
        selectedTreeNode = null;
        sheetNames = new ArrayList<>();
        selectedSheet = null;
        renderSelectSheet = false;
        selectFileErrorMessage = null;
        selectFileSummaryMessage = null;
        isValidSheet = false;
    }

    /**
     * Implements the cancel operation, invoked by the wizard's "Cancel"
     * navigation button.
     */
    public String cancel() {
        String completionUrl = importHelper.getCompletionUrl();
        this.reset();
        return completionUrl;
    }

    /**
     * Implements the finish operation, invoked by the wizard's "Finish"
     * navigation button.
     */
    public String finish() {
        String completionUrl = importHelper.getCompletionUrl();
        this.reset();
        return completionUrl;
    }

    /**
     * Sets enable/disable state for the navigation buttons based on the current
     * tab and input elements.
     */
    public void setEnablementForCurrentTab() {
        setEnablement(currentTab);
    }

    /**
     * Sets enable/disable state for the navigation buttons based on the current
     * tab and input elements.
     */
    protected void setEnablement(String tab) {

        // default
        disableButtonPrev = true;
        disableButtonCancel = false;
        disableButtonFinish = true;
        disableButtonNext = true;

        if (tab.endsWith(tabSelectFile)) {
            disableButtonPrev = true;
            disableButtonCancel = false;
            disableButtonFinish = true;

            if (isValidSheet) {
                disableButtonNext = false;
            } else {
                disableButtonNext = true;
            }
            
        } else if (tab.endsWith(tabValidate)) {
            disableButtonPrev = true;
            disableButtonCancel = false;
            /* if (importHelper.isValidationOnly()) {
                disableButtonFinish = false;
            } else { */
            disableButtonFinish = true;
            if (importHelper.isValidInput()) {
                disableButtonNext = false;
            }
            
        } else if (tab.endsWith(tabResults)) {
            disableButtonPrev = true;
            disableButtonCancel = true;
            disableButtonFinish = false;
            disableButtonNext = true;
        }
    }
}
