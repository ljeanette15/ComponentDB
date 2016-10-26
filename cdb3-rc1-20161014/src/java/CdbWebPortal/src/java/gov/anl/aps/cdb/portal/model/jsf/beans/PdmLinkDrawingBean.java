/*
 * Copyright (c) 2014-2015, Argonne National Laboratory.
 *
 * SVN Information:
 *   $HeadURL$
 *   $Date$
 *   $Revision$
 *   $Author$
 */
package gov.anl.aps.cdb.portal.model.jsf.beans;

import gov.anl.aps.cdb.api.PdmLinkApi;
import gov.anl.aps.cdb.common.constants.CdbProperty;
import gov.anl.aps.cdb.common.exceptions.CdbException;
import gov.anl.aps.cdb.common.exceptions.ConfigurationError;
import gov.anl.aps.cdb.common.objects.PdmLinkDrawing;
import gov.anl.aps.cdb.common.objects.PdmLinkDrawingRevision;
import gov.anl.aps.cdb.common.objects.PdmLinkSearchResults;
import gov.anl.aps.cdb.common.objects.PdmLinkSearchResult;
import gov.anl.aps.cdb.common.objects.Image;
import gov.anl.aps.cdb.common.objects.PdmLinkComponent;
import gov.anl.aps.cdb.portal.controllers.ItemController;
import gov.anl.aps.cdb.portal.controllers.ItemDomainCatalogController;
import gov.anl.aps.cdb.portal.utilities.ConfigurationUtility;
import gov.anl.aps.cdb.portal.utilities.SessionUtility;
import gov.anl.aps.cdb.portal.controllers.PropertyTypeController;
import gov.anl.aps.cdb.portal.model.db.entities.AllowedPropertyValue;
import gov.anl.aps.cdb.portal.model.db.entities.EntityType;
import gov.anl.aps.cdb.portal.model.db.entities.Item;
import gov.anl.aps.cdb.portal.model.db.entities.PropertyType;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.apache.log4j.Logger;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import javax.faces.model.ListDataModel;
import javax.imageio.ImageIO;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * JSF bean for handling PDMLink drawings.
 *
 * This bean uses CDB web service.
 */
@Named("pdmLinkDrawingBean")
@SessionScoped
public class PdmLinkDrawingBean implements Serializable {

    private static final Logger logger = Logger.getLogger(PdmLinkDrawingBean.class.getName());

    private String drawingNumber;
    private String searchKeywords;
    private String prevSearchKeywords;
    private PdmLinkDrawing drawing;
    private PdmLinkApi pdmLinkApi;
    private PdmLinkSearchResults searchResults;
    private PdmLinkComponent pdmLinkComponent;
    //Getting image from server 
    private StreamedContent pdmLinkImage;
    private byte[] imageByteArray;
    //Add component form 
    private boolean exposeSuggestedComponentType;
    private ListDataModel suggestedComponentTypeListDataModel;
    private final String PDMLINK_PROPERTY_NAME = "PDMLink Drawing";
    private final String WBS_PROPERTY_NAME = "WBS-DCC";
    private final String ITEM_DOMAIN_CATALOG_CONTROLER_NAME = "itemDomainCatalogController";
    private final String PROPERTY_TYPE_CONTROLLER_NAME = "propertyTypeController";
    private final String DEFAULT_ENTITY_TYPE_NAME_FOR_NEW_ITEMS = "Component"; 

    private PropertyType pdmPropertyType;
    private PropertyType wbsPropertyType;
    private String dialogErrorMessage;

    @PostConstruct
    public void init() {
        String webServiceUrl = ConfigurationUtility.getPortalProperty(CdbProperty.WEB_SERVICE_URL_PROPERTY_NAME);
        try {
            pdmLinkApi = new PdmLinkApi(webServiceUrl);
        } catch (ConfigurationError ex) {
            String error = "PDMLink Service is not accessible:  " + ex.getErrorMessage();
            showErrorMessage(error);
            logger.error(error);
        }
    }

    public PdmLinkDrawing getDrawing() {
        return drawing;
    }

    public void setDrawingNumber(String drawingName) {
        this.drawingNumber = drawingName;
    }

    public String getDrawingNumber() {
        return drawingNumber;
    }

    public List<PdmLinkSearchResult> getSearchResults() {
        if (searchResults == null) {
            return null;
        }
        return searchResults.getSearchResults();
    }

    public List<PdmLinkDrawingRevision> getDrawingRevisionList() {
        if (drawing == null) {
            return null;
        }
        return drawing.getRevisionList();
    }

    public void setSearchKeywords(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    public String getSearchKeywords() {
        return searchKeywords;
    }

    public StreamedContent getPdmLinkImage() {
        return pdmLinkImage;
    }

    private PropertyType getPdmPropertyType() {
        if (pdmPropertyType == null) {
            PropertyTypeController propertyTypeController = getPropertyTypeController(); 
            pdmPropertyType = propertyTypeController.findByName(PDMLINK_PROPERTY_NAME);
        }
        return pdmPropertyType;
    }

    private PropertyType getWbsPropertyType() {
        // The wbs property may have new allowed values added so updating the property type is essencial.
        PropertyTypeController propertyTypeController = getPropertyTypeController(); 
        wbsPropertyType = propertyTypeController.findByName(WBS_PROPERTY_NAME);
        return wbsPropertyType;
    }

    public void setExposeSuggestedComponentType(boolean exposeSuggestedComponentType) {
        this.exposeSuggestedComponentType = exposeSuggestedComponentType;
    }

    public boolean isExposeSuggestedComponentType() {
        return exposeSuggestedComponentType;
    }

    public PdmLinkComponent getPdmLinkComponent() {
        return pdmLinkComponent;
    }

    public String getDialogErrorMessage() {
        return dialogErrorMessage;
    }

    private void showErrorMessage(String error) {
        dialogErrorMessage = "Error: " + error;
        SessionUtility.addErrorMessage("Error", error);
    }

    private void showWarningMessage(String warning) {
        dialogErrorMessage = "Warning: " + warning;
        SessionUtility.addWarningMessage("Warning", warning);
    }

    /**
     * Check if the pdmLinkApi was successfully initialized.
     *
     * @return boolean that specifies if error occurred
     */
    private boolean checkAPIStatus() {
        if (pdmLinkApi == null) {
            showErrorMessage("PDMLink Service is not accessible.");
            return false;
        }
        return true;
    }

    /**
     * Using the currently loaded drawing. Load up information required to
     * create a component.
     *
     * @param itemController allows clearing a componentObject. The component
     * object is binded to UI fields on the pdmLink add component dialog.
     */
    public void generateComponentInfo(ItemController itemController) {
        //Create a new empty cdb component. 
        itemController.prepareCreate();
        //Generate a pdmLinkComponent information object 
        pdmLinkComponent = null;
        suggestedComponentTypeListDataModel = null;

        if (!checkAPIStatus()) {
            return;
        }
        if (drawing == null) {
            showErrorMessage("No drawing is loaded");
        }

        String drawingNumber = drawing.getNumber();
        if (drawingNumber != null && !drawingNumber.isEmpty()) {
            try {
                pdmLinkComponent = pdmLinkApi.generateComponentInformation(drawingNumber);
                // Add generated description to current component 
                itemController.getSelected().setDescription(pdmLinkComponent.getCdbDescription());
            } catch (CdbException ex) {
                showErrorMessage(ex.getErrorMessage());
                logger.error(ex);
            }
        } else {
            showWarningMessage("Drawing Number is empty");
        }
    }

    /**
     * Create a catalog item using the component db entity. Once created add
     * pdmLink properties and WBS description if possible.
     *
     * @param onSuccessExecute command on UI to be executed on success. 
     */
    public void createCatalogItemFromDrawingNumber(String onSuccessExecute) {
        ItemDomainCatalogController itemController = getItemDomainCatalogController();
        generateComponentInfo(itemController);
        
        if (pdmLinkComponent != null) {            
            Item newCatalogItem = itemController.getSelected();
            newCatalogItem.setName(pdmLinkComponent.getName());
            newCatalogItem.setItemIdentifier1(pdmLinkComponent.getModelNumber());                        
            newCatalogItem.setPropertyValueList(new ArrayList<>());
            List<EntityType> entityTypeList = new ArrayList<>();
            
            // Assign component 
            List<EntityType> allowedEntityTypeList = newCatalogItem.getDomain().getAllowedEntityTypeList();
            for (EntityType entityType: allowedEntityTypeList) {
                if (entityType.getName().equals(DEFAULT_ENTITY_TYPE_NAME_FOR_NEW_ITEMS)) {
                    entityTypeList.add(entityType); 
                    try {
                        newCatalogItem.setEntityTypeList(entityTypeList);
                    } catch (CdbException ex) {
                        SessionUtility.addErrorMessage("Error", "Could not set entity type to: " + entityType);
                        return;
                    }
                    break;
                }                
            }
            
            try {
                itemController.checkCurrentItem(true);
            } catch (CdbException ex) {
                SessionUtility.addErrorMessage("PDMLink drawing exists", 
                        "An item with model number of " + pdmLinkComponent.getModelNumber() + " already exists.");
                return;
            }                                               

            getPdmPropertyType();
            if (pdmPropertyType != null) {
                for (String pdmPropertyValue : pdmLinkComponent.getPdmPropertyValues()) {
                    //Add tags if needed 
                    if (pdmPropertyValue.equalsIgnoreCase(pdmLinkComponent.getModelNumber())) {
                        itemController.preparePropertyTypeValueAdd(pdmPropertyType, pdmPropertyValue, "Model Ref");
                    } else {
                        itemController.preparePropertyTypeValueAdd(pdmPropertyType, pdmPropertyValue);
                    }
                }
            } else {
                showErrorMessage("Couldn't find " + PDMLINK_PROPERTY_NAME + " property type");
            }
            //Attempt to add WBS property
            if (pdmLinkComponent.getWbsDescription() != null && !pdmLinkComponent.getWbsDescription().equals("")) {
                getWbsPropertyType();
                if (wbsPropertyType != null) {
                    List<AllowedPropertyValue> wbsPropertyAllowedValueList = wbsPropertyType.getAllowedPropertyValueList();
                    String wbsNumber = pdmLinkComponent.getWbsDescription();
                    boolean foundAllowedValue = false;
                    for (AllowedPropertyValue wbsAllowedPropertyValue : wbsPropertyAllowedValueList) {
                        if (wbsAllowedPropertyValue.getValue().equalsIgnoreCase(wbsNumber)) {
                            wbsNumber = wbsAllowedPropertyValue.getValue();
                            foundAllowedValue = true;
                            break;
                        }
                    }
                    if (foundAllowedValue) {
                        itemController.preparePropertyTypeValueAdd(wbsPropertyType, wbsNumber);
                    } else {
                        showWarningMessage("WBS number is not in the allowed value list for WBS property.");
                    }
                } else {
                    showErrorMessage("Couldn't find " + WBS_PROPERTY_NAME + " property type");
                }
            }
            itemController.setCurrent(newCatalogItem);
            RequestContext.getCurrentInstance().execute(onSuccessExecute);
        } else {
            showErrorMessage("No pdmLink drawing information was generated");
        }
    }

    public ItemDomainCatalogController getItemDomainCatalogController() {
        return (ItemDomainCatalogController) SessionUtility.findBean(ITEM_DOMAIN_CATALOG_CONTROLER_NAME);
    }
    
    public PropertyTypeController getPropertyTypeController() {
        return (PropertyTypeController) SessionUtility.findBean(PROPERTY_TYPE_CONTROLLER_NAME);
    }

    /**
     * Create a dataModel using information in the the pdmLinkComponent from web
     * service. returning null will result the UI to use a dataModel with all
     * component types.
     *
     * @param componentTypeController passed from the UI. The standard
     * componentTypeController bean that UI would use.
     * @return dataModel that could be used with dataTable and passed to
     * auto-complete drop-down of add component on pdmLink interface.
     */
    /*
    public DataModel createSuggestedTypesDataModel(ComponentTypeController componentTypeController) {
        // When exposeSuggesteedComponentType is false the user will get a different datatable with all component types. 
        if (pdmLinkComponent != null && exposeSuggestedComponentType == true) {
            if (suggestedComponentTypeListDataModel == null) {
                componentTypeController.setSelectedComponentType(null);
                componentTypeController.setFilteredObjectList(null);
                List<ComponentType> componentTypeEntityList = new ArrayList<>();

                for (gov.anl.aps.cdb.common.objects.ComponentType suggestedComponentType : pdmLinkComponent.getSuggestedComponentTypes()) {
                    ComponentType componentType = componentTypeController.findById(suggestedComponentType.getId().intValue());
                    componentTypeEntityList.add(componentType);
                }

                suggestedComponentTypeListDataModel = new ListDataModel(componentTypeEntityList);
            }
            return suggestedComponentTypeListDataModel;
        } else {
            return null;
        }
    }*/
    /**
     * Determine whether the user wants to search for a particular drawing or a
     * list of drawing names, perform correct action based on criteria.
     */
    public void searchDrawings() {

        //Check if the required search result is already loaded. 
        if (prevSearchKeywords == null) {
            prevSearchKeywords = "";
        } else if (prevSearchKeywords.equals(searchKeywords) && drawing != null) {
            //Show only loaded search results 
            if (searchResults != null) {
                drawing = null;
                return;
            }
        } else if (searchKeywords.equals(drawingNumber) && searchResults != null) {
            //Show only loaded drawing 
            if (drawing != null) {
                searchResults = null;
                //The image needs to be reloaded. 
                loadImageForDrawing();
                return;
            }
        }

        prevSearchKeywords = searchKeywords;
        searchResults = null;
        drawing = null;

        if (searchKeywords != null && !searchKeywords.isEmpty()) {
            logger.debug("Search keywords: " + searchKeywords);
            if (searchKeywords.contains("*") || searchKeywords.contains("?")
                    || !PdmLinkDrawing.isExtensionValid(searchKeywords)) {
                searchPdmLink();
            } else {
                drawingNumber = searchKeywords;
                findDrawing();
            }
        } else {
            showWarningMessage("Search pattern cannot be empty.");
        }
    }

    /**
     * Use drawing name variable to attempt to get information for the
     * particular drawing. Set drawing variable.
     */
    public void findDrawing() {

        if (!checkAPIStatus()) {
            return;
        }

        resetDrawingInfo();

        if (drawingNumber != null && !drawingNumber.isEmpty()) {
            if (!PdmLinkDrawing.isExtensionValid(drawingNumber)) {
                showWarningMessage("Valid drawing number extensions are: " + PdmLinkDrawing.VALID_EXTENSION_LIST);
                return;
            }

            try {
                logger.debug("Searching for drawing: " + drawingNumber);
                drawing = pdmLinkApi.getDrawing(drawingNumber);
                loadImageForDrawing();
                logger.debug("Found drawing, windchill URL: " + drawing.getWindchillUrl());
            } catch (CdbException ex) {
                logger.error(ex);
                showErrorMessage(ex.getErrorMessage());
            }
        } else {
            showWarningMessage("Drawing number cannot be empty.");
        }
    }

    /**
     * Polymorphic function that call the function after setting drawingName
     * from its parameter.
     *
     * @param drawingNumber drawing number/name to search for
     */
    public void findDrawing(String drawingNumber) {
        this.drawingNumber = drawingNumber;
        findDrawing();
    }

    /**
     * Search the PDMLink for a list of drawings resulting from the keywords
     * entered by user. Set the searchResults variable
     */
    public void searchPdmLink() {
        if (!checkAPIStatus()) {
            return;
        }

        try {
            searchResults = pdmLinkApi.search(searchKeywords);
            logger.debug("Found " + searchResults.getSearchResults().size() + " drwaing(s)");
        } catch (CdbException ex) {
            logger.error(ex);
            showErrorMessage(ex.getErrorMessage());
        }
    }

    /**
     * Sets search results to a list of related drawings.
     *
     * @param drawingNumberBase drawing number with or without extension.
     * @param onSuccessCommand command to be executed upon successful completion
     * of request.
     */
    public void getRelatedDrawings(String drawingNumberBase, String onSuccessCommand) {
        searchResults = null;
        searchKeywords = "";

        if (!checkAPIStatus()) {
            return;
        }

        try {
            searchResults = pdmLinkApi.searchRelatedDrawings(drawingNumberBase);
            logger.debug("Found " + searchResults.getSearchResults().size() + " drwaing(s)");

            //See if drawing details should be loaded 
            if (searchResults != null) {
                for (PdmLinkSearchResult searchResult : searchResults.getSearchResults()) {
                    if (searchResult.getNumber().equalsIgnoreCase(drawingNumberBase)) {
                        //load the details for the drawing
                        completeDrawing(searchResult.getUfid(), searchResult.getOid());
                    }
                }
            }
            RequestContext.getCurrentInstance().execute(onSuccessCommand);
        } catch (CdbException ex) {
            logger.error(ex);
            showErrorMessage(ex.getErrorMessage());
        }
    }

    /**
     * Loads an image of a particular drawing for the latest revision. Set
     * pdmLinkImage variable
     */
    public void loadImageForDrawing() {
        imageByteArray = null;

        if (!checkAPIStatus()) {
            return;
        }

        try {
            //Check if image already has something for this version. 
            String ufid = (drawing.getRevisionList().get(0).getUfid());
            Image imageInfo = pdmLinkApi.getOneTimeImageUrl(ufid);
            String link = imageInfo.getImageUrl();
            URL url = new URL(link);

            BufferedImage buffImg;
            buffImg = ImageIO.read(url.openStream());
            ByteArrayOutputStream os;
            os = new ByteArrayOutputStream();
            ImageIO.write(buffImg, "png", os);
            imageByteArray = os.toByteArray();

            reloadPdmLinkImageStreamedContent();

        } catch (CdbException ex) {
            logger.error(ex);
            //No need to notify user since many drawings do not include image
            logger.error(ex);
            pdmLinkImage = null;
        } catch (Exception ex) {
            logger.error(ex);
            //The extension of the image is not in standard format cannot be loaded
            if (ex.getMessage().equals("image == null!")) {
                //Don't inform user of this exception. No valid image exits. 
                return;
            }
            showErrorMessage(ex.getMessage());
        }
    }

    /**
     * Using the byte array containing the image, reload the streamed content to
     * show user the image. Prime faces allows use of streamed content only once
     * per request. It needs to be reloaded.
     */
    public void reloadPdmLinkImageStreamedContent() {
        pdmLinkImage = null;

        if (imageByteArray != null) {
            pdmLinkImage = new DefaultStreamedContent(new ByteArrayInputStream(imageByteArray), "image/jpg");
        }
    }

    /**
     * Gets information about a particular drawing using information from search
     * result. Set the drawing variable.
     *
     * @param ufid attribute of a search result
     * @param oid attribute of a search result
     */
    public void completeDrawing(String ufid, String oid) {
        if (!checkAPIStatus()) {
            return;
        }

        drawing = null;
        pdmLinkImage = null;

        if (ufid != null && !ufid.isEmpty() && oid != null && !oid.isEmpty()) {
            try {
                logger.debug("Completing drawing with oid: " + oid);
                drawing = pdmLinkApi.completeDrawingInfo(ufid, oid);
                drawingNumber = drawing.getName();
                loadImageForDrawing();
                logger.debug("Found drawing, windchill URL: " + drawing.getWindchillUrl());
            } catch (CdbException ex) {
                logger.error(ex);
                showErrorMessage(ex.getErrorMessage());
            }
        } else {
            showWarningMessage("UFID and or OID were not provided by search result");
        }

    }

    /**
     * Reset the variables related to the loaded drawing information.
     */
    public void resetDrawingInfo() {
        drawing = null;
        pdmLinkImage = null;
    }

}