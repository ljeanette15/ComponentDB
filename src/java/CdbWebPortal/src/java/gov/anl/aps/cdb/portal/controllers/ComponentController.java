package gov.anl.aps.cdb.portal.controllers;

import gov.anl.aps.cdb.portal.constants.DisplayType;
import gov.anl.aps.cdb.portal.exceptions.CdbPortalException;
import gov.anl.aps.cdb.portal.exceptions.ObjectAlreadyExists;
import gov.anl.aps.cdb.portal.model.db.entities.Component;
import gov.anl.aps.cdb.portal.model.db.beans.ComponentFacade;
import gov.anl.aps.cdb.portal.model.db.beans.PropertyTypeFacade;
import gov.anl.aps.cdb.portal.model.db.entities.ComponentSource;
import gov.anl.aps.cdb.portal.model.db.entities.ComponentType;
import gov.anl.aps.cdb.portal.model.db.entities.EntityInfo;
import gov.anl.aps.cdb.portal.model.db.entities.Log;
import gov.anl.aps.cdb.portal.model.db.entities.PropertyType;
import gov.anl.aps.cdb.portal.model.db.entities.PropertyValue;
import gov.anl.aps.cdb.portal.model.db.entities.PropertyValueHistory;
import gov.anl.aps.cdb.portal.model.db.entities.SettingType;
import gov.anl.aps.cdb.portal.model.db.entities.UserGroup;
import gov.anl.aps.cdb.portal.model.db.entities.UserInfo;
import gov.anl.aps.cdb.portal.model.jsf.handlers.PropertyTypeHandlerFactory;
import gov.anl.aps.cdb.portal.model.jsf.handlers.PropertyTypeHandlerInterface;
import gov.anl.aps.cdb.portal.utilities.SessionUtility;
import gov.anl.aps.cdb.portal.utilities.StorageUtility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

@Named("componentController")
@SessionScoped
public class ComponentController extends CrudEntityController<Component, ComponentFacade> implements Serializable {

    private static final String DisplayNumberOfItemsPerPageSettingTypeKey = "Component.List.Display.NumberOfItemsPerPage";
    private static final String DisplayIdSettingTypeKey = "Component.List.Display.Id";
    private static final String DisplayDescriptionSettingTypeKey = "Component.List.Display.Description";
    private static final String DisplayTypeSettingTypeKey = "Component.List.Display.Type";
    private static final String DisplayTypeCategorySettingTypeKey = "Component.List.Display.TypeCategory";
    private static final String DisplayOwnerUserSettingTypeKey = "Component.List.Display.OwnerUser";
    private static final String DisplayOwnerGroupSettingTypeKey = "Component.List.Display.OwnerGroup";
    private static final String DisplayCreatedByUserSettingTypeKey = "Component.List.Display.CreatedByUser";
    private static final String DisplayCreatedOnDateTimeSettingTypeKey = "Component.List.Display.CreatedOnDateTime";
    private static final String DisplayPropertyTypeId1SettingTypeKey = "Component.List.Display.PropertyTypeId1";
    private static final String DisplayPropertyTypeId2SettingTypeKey = "Component.List.Display.PropertyTypeId2";
    private static final String DisplayPropertyTypeId3SettingTypeKey = "Component.List.Display.PropertyTypeId3";
    private static final String DisplayPropertyTypeId4SettingTypeKey = "Component.List.Display.PropertyTypeId4";
    private static final String DisplayPropertyTypeId5SettingTypeKey = "Component.List.Display.PropertyTypeId5";
    private static final String DisplayLastModifiedByUserSettingTypeKey = "Component.List.Display.LastModifiedByUser";
    private static final String DisplayLastModifiedOnDateTimeSettingTypeKey = "Component.List.Display.LastModifiedOnDateTime";

    private static final String FilterByNameSettingTypeKey = "Component.List.FilterBy.Name";
    private static final String FilterByDescriptionSettingTypeKey = "Component.List.FilterBy.Description";
    private static final String FilterByTypeSettingTypeKey = "Component.List.FilterBy.Type";
    private static final String FilterByTypeCategorySettingTypeKey = "Component.List.FilterBy.TypeCategory";
    private static final String FilterByOwnerUserSettingTypeKey = "Component.List.FilterBy.OwnerUser";
    private static final String FilterByOwnerGroupSettingTypeKey = "Component.List.FilterBy.OwnerGroup";
    private static final String FilterByPropertyValue1SettingTypeKey = "Component.List.FilterBy.PropertyValue1";
    private static final String FilterByPropertyValue2SettingTypeKey = "Component.List.FilterBy.PropertyValue2";
    private static final String FilterByPropertyValue3SettingTypeKey = "Component.List.FilterBy.PropertyValue3";
    private static final String FilterByPropertyValue4SettingTypeKey = "Component.List.FilterBy.PropertyValue4";
    private static final String FilterByPropertyValue5SettingTypeKey = "Component.List.FilterBy.PropertyValue5";

    private static final String FilterByCreatedByUserSettingTypeKey = "Component.List.FilterBy.CreatedByUser";
    private static final String FilterByCreatedOnDateTimeSettingTypeKey = "Component.List.FilterBy.CreatedOnDateTime";
    private static final String FilterByLastModifiedByUserSettingTypeKey = "Component.List.FilterBy.LastModifiedByUser";
    private static final String FilterByLastModifiedOnDateTimeSettingTypeKey = "Component.List.FilterBy.LastModifiedOnDateTime";

    private static final Logger logger = Logger.getLogger(ComponentController.class.getName());

    @EJB
    private ComponentFacade componentFacade;

    @EJB
    private PropertyTypeFacade propertyTypeFacade;

    private Boolean displayType = null;
    private Boolean displayTypeCategory = null;

    private String filterByType = null;
    private String filterByTypeCategory = null;

    private Boolean selectDisplayType = true;
    private Boolean selectDisplayTypeCategory = true;

    private String selectFilterByType = null;
    private String selectFilterByTypeCategory = null;

    private String selectedComponentImage = null;
    private List<PropertyValue> componentImageList = null;
    private Boolean displayComponentImages = null;

    private Integer displayPropertyTypeId1 = null;
    private Integer displayPropertyTypeId2 = null;
    private Integer displayPropertyTypeId3 = null;
    private Integer displayPropertyTypeId4 = null;
    private Integer displayPropertyTypeId5 = null;

    private String filterByPropertyValue1 = null;
    private String filterByPropertyValue2 = null;
    private String filterByPropertyValue3 = null;
    private String filterByPropertyValue4 = null;
    private String filterByPropertyValue5 = null;

    public ComponentController() {
        super();
    }

    @Override
    protected ComponentFacade getFacade() {
        return componentFacade;
    }

    @Override
    protected Component createEntityInstance() {
        Component component = new Component();
        UserInfo ownerUser = (UserInfo) SessionUtility.getUser();
        EntityInfo entityInfo = new EntityInfo();
        entityInfo.setOwnerUser(ownerUser);
        List<UserGroup> ownerUserGroupList = ownerUser.getUserGroupList();
        if (!ownerUserGroupList.isEmpty()) {
            entityInfo.setOwnerUserGroup(ownerUserGroupList.get(0));
        }
        component.setEntityInfo(entityInfo);
        return component;
    }

    @Override
    public Component cloneEntityInstance(Component component) {
        Component clonedComponent = super.cloneEntityInstance(component);
        UserInfo ownerUser = (UserInfo) SessionUtility.getUser();
        EntityInfo entityInfo = new EntityInfo();
        entityInfo.setOwnerUser(ownerUser);
        List<UserGroup> ownerUserGroupList = ownerUser.getUserGroupList();
        if (!ownerUserGroupList.isEmpty()) {
            entityInfo.setOwnerUserGroup(ownerUserGroupList.get(0));
        }
        clonedComponent.setEntityInfo(entityInfo);
        super.setLogText("Cloned from " + component.getName());
        return clonedComponent;
    }

    @Override
    public String getEntityTypeName() {
        return "component";
    }

    @Override
    public String getCurrentEntityInstanceName() {
        if (getCurrent() != null) {
            return getCurrent().getName();
        }
        return "";
    }

    @Override
    public void prepareEntityView(Component component) {
        prepareComponentImageList(component);
    }

    @Override
    public void prepareEntityInsert(Component component) throws ObjectAlreadyExists {
        Component existingComponent = componentFacade.findByName(component.getName());
        if (existingComponent != null) {
            throw new ObjectAlreadyExists("Component " + component.getName() + " already exists.");
        }
        EntityInfo entityInfo = component.getEntityInfo();
        UserInfo createdByUser = (UserInfo) SessionUtility.getUser();
        Date createdOnDateTime = new Date();
        entityInfo.setCreatedOnDateTime(createdOnDateTime);
        entityInfo.setCreatedByUser(createdByUser);
        entityInfo.setLastModifiedOnDateTime(createdOnDateTime);
        entityInfo.setLastModifiedByUser(createdByUser);
        String logText = getLogText();
        if (logText != null && !logText.isEmpty()) {
            Log logEntry = new Log();
            logEntry.setText(logText);
            logEntry.setEnteredByUser(createdByUser);
            logEntry.setEnteredOnDateTime(createdOnDateTime);
            List<Log> logList = new ArrayList<>();
            logList.add(logEntry);
            component.setLogList(logList);
            resetLogText();
        }
        logger.debug("Inserting new component " + component.getName() + " (user: " + createdByUser.getUsername() + ")");
    }

    @Override
    public void prepareEntityUpdate(Component component) throws CdbPortalException {
        EntityInfo entityInfo = component.getEntityInfo();
        UserInfo lastModifiedByUser = (UserInfo) SessionUtility.getUser();
        Date lastModifiedOnDateTime = new Date();
        entityInfo.setLastModifiedOnDateTime(lastModifiedOnDateTime);
        entityInfo.setLastModifiedByUser(lastModifiedByUser);
        String logText = getLogText();
        if (logText != null && !logText.isEmpty()) {
            Log logEntry = new Log();
            logEntry.setText(logText);
            logEntry.setEnteredByUser(lastModifiedByUser);
            logEntry.setEnteredOnDateTime(lastModifiedOnDateTime);
            component.getLogList().add(logEntry);
            resetLogText();
        }

        // Compare properties with what is in the db
        List<PropertyValue> originalPropertyValueList = componentFacade.findById(component.getId()).getPropertyValueList();
        List<PropertyValue> newPropertyValueList = component.getPropertyValueList();
        ArrayList<PropertyValue> updatedPropertyValueList = new ArrayList<>();
        logger.debug("Verifying properties for component " + component);
        for (PropertyValue newPropertyValue : newPropertyValueList) {
            int index = originalPropertyValueList.indexOf(newPropertyValue);
            if (index >= 0) {
                // Original property was there.
                PropertyValue originalPropertyValue = originalPropertyValueList.get(index);
                if (!newPropertyValue.equalsByValueAndUnitsAndDescription(originalPropertyValue)) {
                    // Property value was modified.
                    logger.debug("Property value for type " + originalPropertyValue.getPropertyType()
                            + " was modified (original value: " + originalPropertyValue + "; new value: " + newPropertyValue + ")");
                    newPropertyValue.setEnteredByUser(lastModifiedByUser);
                    newPropertyValue.setEnteredOnDateTime(lastModifiedOnDateTime);

                    // Save history
                    List<PropertyValueHistory> propertyValueHistoryList = newPropertyValue.getPropertyValueHistoryList();
                    PropertyValueHistory propertyValueHistory = new PropertyValueHistory();
                    propertyValueHistory.updateFromPropertyValue(originalPropertyValue);
                    propertyValueHistoryList.add(propertyValueHistory);
                }
            } else {
                // New property value.
                logger.debug("Adding new property value for type " + newPropertyValue.getPropertyType()
                        + ": " + newPropertyValue);
                newPropertyValue.setEnteredByUser(lastModifiedByUser);
                newPropertyValue.setEnteredOnDateTime(lastModifiedOnDateTime);
            }
        }

//        // Save updated values in history table.
//        for (PropertyValue updatedPropertyValue : updatedPropertyValueList) {
//            logger.debug("Saving property value history for type " + updatedPropertyValue.getPropertyType()
//                    + ": " + updatedPropertyValue);
//            List<PropertyValueHistory> propertyValueHistoryList = updatedPropertyValue.getPropertyValueHistoryList();
//            PropertyValueHistory propertyValueHistory = new PropertyValueHistory();
//            propertyValueHistory.updateFromPropertyValue(updatedPropertyValue);
//            propertyValueHistoryList.add(propertyValueHistory);
//        }
        componentImageList = null;
        displayComponentImages = false;
        logger.debug("Updating component " + component.getName() + " (user: " + lastModifiedByUser.getUsername() + ")");
    }

    public Component findById(Integer id) {
        return componentFacade.findById(id);
    }

    @Override
    public void selectByRequestParams() {
        if (idViewParam != null) {
            Component component = findById(idViewParam);
            setCurrent(component);
            idViewParam = null;
        }
    }

    public void prepareAddProperty() {
        Component component = getCurrent();
        List<PropertyValue> propertyList = component.getPropertyValueList();
        PropertyValue property = new PropertyValue();
        propertyList.add(property);
    }

    public void savePropertyList() {
        update();
    }

    public void selectPropertyTypes(List<PropertyType> propertyTypeList) {
        Component component = getCurrent();
        UserInfo lastModifiedByUser = (UserInfo) SessionUtility.getUser();
        Date lastModifiedOnDateTime = new Date();
        List<PropertyValue> propertyValueList = component.getPropertyValueList();
        for (PropertyType propertyType : propertyTypeList) {
            PropertyValue propertyValue = new PropertyValue();
            propertyValue.setPropertyType(propertyType);
            propertyValue.setValue(propertyType.getDefaultValue());
            propertyValue.setUnits(propertyType.getDefaultUnits());
            propertyValueList.add(propertyValue);
            propertyValue.setEnteredByUser(lastModifiedByUser);
            propertyValue.setEnteredOnDateTime(lastModifiedOnDateTime);
        }
    }

    public void deleteProperty(PropertyValue componentProperty) {
        Component component = getCurrent();
        List<PropertyValue> componentPropertyList = component.getPropertyValueList();
        componentPropertyList.remove(componentProperty);
        update();
    }

    public void prepareAddSource(Component component) {
        List<ComponentSource> sourceList = component.getComponentSourceList();
        ComponentSource source = new ComponentSource();
        source.setComponent(component);
        sourceList.add(source);
    }

    public void saveSourceList() {
        update();
    }

    public void deleteSource(ComponentSource source) {
        Component component = getCurrent();
        List<ComponentSource> sourceList = component.getComponentSourceList();
        sourceList.remove(source);
        update();
    }

    public void prepareAddLog(Component component) {
        UserInfo lastModifiedByUser = (UserInfo) SessionUtility.getUser();
        Date lastModifiedOnDateTime = new Date();
        Log logEntry = new Log();
        logEntry.setEnteredByUser(lastModifiedByUser);
        logEntry.setEnteredOnDateTime(lastModifiedOnDateTime);
        List<Log> componentLogList = component.getLogList();
        componentLogList.add(logEntry);
    }

    public void deleteLog(Log componentLog) {
        Component component = getCurrent();
        List<Log> componentLogList = component.getLogList();
        componentLogList.remove(componentLog);
        update();
    }

    public List<Log> getLogList() {
        Component component = getCurrent();
        List<Log> componentLogList = component.getLogList();
        UserInfo sessionUser = (UserInfo) SessionUtility.getUser();
        if (sessionUser != null) {
            Date settingsTimestamp = getSettingsTimestamp();
            if (settingsTimestamp == null || sessionUser.areUserSettingsModifiedAfterDate(settingsTimestamp)) {
                updateSettingsFromSessionUser(sessionUser);
                settingsTimestamp = new Date();
                setSettingsTimestamp(settingsTimestamp);
            }
        }
        return componentLogList;
    }

    public void saveLogList() {
        update();
    }

    public Integer parseSettingValueAsInteger(String settingValue) {
        try {
            return Integer.parseInt(settingValue);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Override
    public String customizeListDisplay() {
        resetComponentPropertyTypeIdIndexMappings();
        return super.customizeListDisplay();
    }
    
    private void resetComponentPropertyTypeIdIndexMappings() {
        Component.setPropertyTypeIdIndex(1, displayPropertyTypeId1);
        Component.setPropertyTypeIdIndex(2, displayPropertyTypeId2);
        Component.setPropertyTypeIdIndex(3, displayPropertyTypeId3);
        Component.setPropertyTypeIdIndex(4, displayPropertyTypeId4);
        Component.setPropertyTypeIdIndex(5, displayPropertyTypeId5);
    }
    
    @Override
    public void updateSettingsFromSettingTypeDefaults(Map<String, SettingType> settingTypeMap) {
        if (settingTypeMap == null) {
            return;
        }

        logger.debug("Updating list settings from setting type defaults");

        displayNumberOfItemsPerPage = Integer.parseInt(settingTypeMap.get(DisplayNumberOfItemsPerPageSettingTypeKey).getDefaultValue());
        displayId = Boolean.parseBoolean(settingTypeMap.get(DisplayIdSettingTypeKey).getDefaultValue());
        displayDescription = Boolean.parseBoolean(settingTypeMap.get(DisplayDescriptionSettingTypeKey).getDefaultValue());
        displayType = Boolean.parseBoolean(settingTypeMap.get(DisplayTypeSettingTypeKey).getDefaultValue());
        displayTypeCategory = Boolean.parseBoolean(settingTypeMap.get(DisplayTypeCategorySettingTypeKey).getDefaultValue());
        displayOwnerUser = Boolean.parseBoolean(settingTypeMap.get(DisplayOwnerUserSettingTypeKey).getDefaultValue());
        displayOwnerGroup = Boolean.parseBoolean(settingTypeMap.get(DisplayOwnerGroupSettingTypeKey).getDefaultValue());
        displayCreatedByUser = Boolean.parseBoolean(settingTypeMap.get(DisplayCreatedByUserSettingTypeKey).getDefaultValue());
        displayCreatedOnDateTime = Boolean.parseBoolean(settingTypeMap.get(DisplayCreatedOnDateTimeSettingTypeKey).getDefaultValue());
        displayLastModifiedByUser = Boolean.parseBoolean(settingTypeMap.get(DisplayLastModifiedByUserSettingTypeKey).getDefaultValue());
        displayLastModifiedOnDateTime = Boolean.parseBoolean(settingTypeMap.get(DisplayLastModifiedOnDateTimeSettingTypeKey).getDefaultValue());

        displayPropertyTypeId1 = parseSettingValueAsInteger(settingTypeMap.get(DisplayPropertyTypeId1SettingTypeKey).getDefaultValue());
        displayPropertyTypeId2 = parseSettingValueAsInteger(settingTypeMap.get(DisplayPropertyTypeId2SettingTypeKey).getDefaultValue());
        displayPropertyTypeId3 = parseSettingValueAsInteger(settingTypeMap.get(DisplayPropertyTypeId3SettingTypeKey).getDefaultValue());
        displayPropertyTypeId4 = parseSettingValueAsInteger(settingTypeMap.get(DisplayPropertyTypeId4SettingTypeKey).getDefaultValue());
        displayPropertyTypeId5 = parseSettingValueAsInteger(settingTypeMap.get(DisplayPropertyTypeId5SettingTypeKey).getDefaultValue());
        
        filterByName = settingTypeMap.get(FilterByNameSettingTypeKey).getDefaultValue();
        filterByDescription = settingTypeMap.get(FilterByDescriptionSettingTypeKey).getDefaultValue();
        filterByType = settingTypeMap.get(FilterByTypeSettingTypeKey).getDefaultValue();
        filterByTypeCategory = settingTypeMap.get(FilterByTypeCategorySettingTypeKey).getDefaultValue();
        filterByOwnerUser = settingTypeMap.get(FilterByOwnerUserSettingTypeKey).getDefaultValue();
        filterByOwnerGroup = settingTypeMap.get(FilterByOwnerGroupSettingTypeKey).getDefaultValue();
        filterByCreatedByUser = settingTypeMap.get(FilterByCreatedByUserSettingTypeKey).getDefaultValue();
        filterByCreatedOnDateTime = settingTypeMap.get(FilterByCreatedOnDateTimeSettingTypeKey).getDefaultValue();
        filterByLastModifiedByUser = settingTypeMap.get(FilterByLastModifiedByUserSettingTypeKey).getDefaultValue();
        filterByLastModifiedOnDateTime = settingTypeMap.get(FilterByLastModifiedOnDateTimeSettingTypeKey).getDefaultValue();

        filterByPropertyValue1 = settingTypeMap.get(FilterByPropertyValue1SettingTypeKey).getDefaultValue();
        filterByPropertyValue2 = settingTypeMap.get(FilterByPropertyValue2SettingTypeKey).getDefaultValue();
        filterByPropertyValue3 = settingTypeMap.get(FilterByPropertyValue3SettingTypeKey).getDefaultValue();
        filterByPropertyValue4 = settingTypeMap.get(FilterByPropertyValue4SettingTypeKey).getDefaultValue();
        filterByPropertyValue5 = settingTypeMap.get(FilterByPropertyValue5SettingTypeKey).getDefaultValue();

        resetComponentPropertyTypeIdIndexMappings();
    }

    @Override
    public void updateSettingsFromSessionUser(UserInfo sessionUser) {
        if (sessionUser == null) {
            return;
        }

        logger.debug("Updating list settings from session user");

        displayNumberOfItemsPerPage = sessionUser.getUserSettingValueAsInteger(DisplayNumberOfItemsPerPageSettingTypeKey, displayNumberOfItemsPerPage);
        displayId = sessionUser.getUserSettingValueAsBoolean(DisplayIdSettingTypeKey, displayId);
        displayDescription = sessionUser.getUserSettingValueAsBoolean(DisplayDescriptionSettingTypeKey, displayDescription);
        displayType = sessionUser.getUserSettingValueAsBoolean(DisplayTypeSettingTypeKey, displayType);
        displayTypeCategory = sessionUser.getUserSettingValueAsBoolean(DisplayTypeCategorySettingTypeKey, displayTypeCategory);
        displayOwnerUser = sessionUser.getUserSettingValueAsBoolean(DisplayOwnerUserSettingTypeKey, displayOwnerUser);
        displayOwnerGroup = sessionUser.getUserSettingValueAsBoolean(DisplayOwnerGroupSettingTypeKey, displayOwnerGroup);
        displayCreatedByUser = sessionUser.getUserSettingValueAsBoolean(DisplayCreatedByUserSettingTypeKey, displayCreatedByUser);
        displayCreatedOnDateTime = sessionUser.getUserSettingValueAsBoolean(DisplayCreatedOnDateTimeSettingTypeKey, displayCreatedOnDateTime);
        displayLastModifiedByUser = sessionUser.getUserSettingValueAsBoolean(DisplayLastModifiedByUserSettingTypeKey, displayLastModifiedByUser);
        displayLastModifiedOnDateTime = sessionUser.getUserSettingValueAsBoolean(DisplayLastModifiedOnDateTimeSettingTypeKey, displayLastModifiedOnDateTime);

        displayPropertyTypeId1 = sessionUser.getUserSettingValueAsInteger(DisplayPropertyTypeId1SettingTypeKey, displayPropertyTypeId1);
        displayPropertyTypeId2 = sessionUser.getUserSettingValueAsInteger(DisplayPropertyTypeId2SettingTypeKey, displayPropertyTypeId2);
        displayPropertyTypeId3 = sessionUser.getUserSettingValueAsInteger(DisplayPropertyTypeId3SettingTypeKey, displayPropertyTypeId3);
        displayPropertyTypeId4 = sessionUser.getUserSettingValueAsInteger(DisplayPropertyTypeId4SettingTypeKey, displayPropertyTypeId4);
        displayPropertyTypeId5 = sessionUser.getUserSettingValueAsInteger(DisplayPropertyTypeId5SettingTypeKey, displayPropertyTypeId5);

        filterByName = sessionUser.getUserSettingValueAsString(FilterByNameSettingTypeKey, filterByName);
        filterByDescription = sessionUser.getUserSettingValueAsString(FilterByDescriptionSettingTypeKey, filterByDescription);
        filterByType = sessionUser.getUserSettingValueAsString(FilterByTypeSettingTypeKey, filterByType);
        filterByTypeCategory = sessionUser.getUserSettingValueAsString(FilterByTypeCategorySettingTypeKey, filterByTypeCategory);
        filterByOwnerUser = sessionUser.getUserSettingValueAsString(FilterByOwnerUserSettingTypeKey, filterByOwnerUser);
        filterByOwnerGroup = sessionUser.getUserSettingValueAsString(FilterByOwnerGroupSettingTypeKey, filterByOwnerGroup);
        filterByCreatedByUser = sessionUser.getUserSettingValueAsString(FilterByCreatedByUserSettingTypeKey, filterByCreatedByUser);
        filterByCreatedOnDateTime = sessionUser.getUserSettingValueAsString(FilterByCreatedOnDateTimeSettingTypeKey, filterByCreatedOnDateTime);
        filterByLastModifiedByUser = sessionUser.getUserSettingValueAsString(FilterByLastModifiedByUserSettingTypeKey, filterByLastModifiedByUser);
        filterByLastModifiedOnDateTime = sessionUser.getUserSettingValueAsString(FilterByLastModifiedOnDateTimeSettingTypeKey, filterByLastModifiedByUser);

        filterByPropertyValue1 = sessionUser.getUserSettingValueAsString(FilterByPropertyValue1SettingTypeKey, filterByPropertyValue1);
        filterByPropertyValue2 = sessionUser.getUserSettingValueAsString(FilterByPropertyValue2SettingTypeKey, filterByPropertyValue2);
        filterByPropertyValue3 = sessionUser.getUserSettingValueAsString(FilterByPropertyValue3SettingTypeKey, filterByPropertyValue3);
        filterByPropertyValue4 = sessionUser.getUserSettingValueAsString(FilterByPropertyValue4SettingTypeKey, filterByPropertyValue4);
        filterByPropertyValue5 = sessionUser.getUserSettingValueAsString(FilterByPropertyValue5SettingTypeKey, filterByPropertyValue5);

        resetComponentPropertyTypeIdIndexMappings();
    }

    @Override
    public void updateListSettingsFromListDataTable(DataTable dataTable) {
        super.updateListSettingsFromListDataTable(dataTable);
        if (dataTable == null) {
            return;
        }

        Map<String, String> filters = dataTable.getFilters();
        filterByType = filters.get("componentType");
        filterByTypeCategory = filters.get("componentTypeCategory");

        filterByPropertyValue1 = filters.get("propertyValue1");
        filterByPropertyValue2 = filters.get("propertyValue2");
        filterByPropertyValue3 = filters.get("propertyValue3");
        filterByPropertyValue4 = filters.get("propertyValue4");
        filterByPropertyValue5 = filters.get("propertyValue5");
    }

    @Override
    public void saveSettingsForSessionUser(UserInfo sessionUser) {
        if (sessionUser == null) {
            return;
        }

        sessionUser.setUserSettingValue(DisplayNumberOfItemsPerPageSettingTypeKey, displayNumberOfItemsPerPage);
        sessionUser.setUserSettingValue(DisplayIdSettingTypeKey, displayId);
        sessionUser.setUserSettingValue(DisplayDescriptionSettingTypeKey, displayDescription);
        sessionUser.setUserSettingValue(DisplayOwnerUserSettingTypeKey, displayOwnerUser);
        sessionUser.setUserSettingValue(DisplayOwnerGroupSettingTypeKey, displayOwnerGroup);
        sessionUser.setUserSettingValue(DisplayCreatedByUserSettingTypeKey, displayCreatedByUser);
        sessionUser.setUserSettingValue(DisplayCreatedOnDateTimeSettingTypeKey, displayCreatedOnDateTime);
        sessionUser.setUserSettingValue(DisplayLastModifiedByUserSettingTypeKey, displayLastModifiedByUser);
        sessionUser.setUserSettingValue(DisplayLastModifiedOnDateTimeSettingTypeKey, displayLastModifiedOnDateTime);

        sessionUser.setUserSettingValue(DisplayTypeSettingTypeKey, displayType);
        sessionUser.setUserSettingValue(DisplayTypeCategorySettingTypeKey, displayTypeCategory);

        sessionUser.setUserSettingValue(DisplayPropertyTypeId1SettingTypeKey, displayPropertyTypeId1);
        
        sessionUser.setUserSettingValue(DisplayPropertyTypeId2SettingTypeKey, displayPropertyTypeId2);
        sessionUser.setUserSettingValue(DisplayPropertyTypeId3SettingTypeKey, displayPropertyTypeId3);
        sessionUser.setUserSettingValue(DisplayPropertyTypeId4SettingTypeKey, displayPropertyTypeId4);
        sessionUser.setUserSettingValue(DisplayPropertyTypeId5SettingTypeKey, displayPropertyTypeId5);

        sessionUser.setUserSettingValue(FilterByNameSettingTypeKey, filterByName);
        sessionUser.setUserSettingValue(FilterByDescriptionSettingTypeKey, filterByDescription);
        sessionUser.setUserSettingValue(FilterByOwnerUserSettingTypeKey, filterByOwnerUser);
        sessionUser.setUserSettingValue(FilterByOwnerGroupSettingTypeKey, filterByOwnerGroup);
        sessionUser.setUserSettingValue(FilterByCreatedByUserSettingTypeKey, filterByCreatedByUser);
        sessionUser.setUserSettingValue(FilterByCreatedOnDateTimeSettingTypeKey, filterByCreatedOnDateTime);
        sessionUser.setUserSettingValue(FilterByLastModifiedByUserSettingTypeKey, filterByLastModifiedByUser);
        sessionUser.setUserSettingValue(FilterByLastModifiedOnDateTimeSettingTypeKey, filterByLastModifiedByUser);

        sessionUser.setUserSettingValue(FilterByTypeSettingTypeKey, filterByType);
        sessionUser.setUserSettingValue(FilterByTypeCategorySettingTypeKey, filterByTypeCategory);

        sessionUser.setUserSettingValue(FilterByPropertyValue1SettingTypeKey, filterByPropertyValue1);
        sessionUser.setUserSettingValue(FilterByPropertyValue2SettingTypeKey, filterByPropertyValue2);
        sessionUser.setUserSettingValue(FilterByPropertyValue3SettingTypeKey, filterByPropertyValue3);
        sessionUser.setUserSettingValue(FilterByPropertyValue4SettingTypeKey, filterByPropertyValue4);
        sessionUser.setUserSettingValue(FilterByPropertyValue5SettingTypeKey, filterByPropertyValue5);

    }

    @Override
    public void clearListFilters() {
        super.clearListFilters();
        filterByType = null;
        filterByTypeCategory = null;

        filterByPropertyValue1 = null;
        filterByPropertyValue2 = null;
        filterByPropertyValue3 = null;
        filterByPropertyValue4 = null;
        filterByPropertyValue5 = null;
    }

    @Override
    public void clearSelectFilters() {
        super.clearSelectFilters();
        selectFilterByType = null;
        selectFilterByTypeCategory = null;
    }

    public void selectComponentType(ComponentType componentType) {
        Component component = getCurrent();
        if (componentType != null) {
            component.setComponentType(componentType);
        }
    }

    public Boolean getDisplayType() {
        return displayType;
    }

    @Override
    public boolean entityCanBeCreatedByUsers() {
        return true;
    }

    @FacesConverter(value = "componentConverter", forClass = Component.class)
    public static class ComponentControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0 || value.equals("Select")) {
                return null;
            }
            ComponentController controller = (ComponentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "componentController");
            return controller.getEntity(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Component) {
                Component o = (Component) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Component.class.getName());
            }
        }

    }

    public String getDisplayPropertyTypeName(Integer propertyTypeId) {
        if (propertyTypeId != null) {

            try {
                PropertyType propertyType = propertyTypeFacade.find(propertyTypeId);
                return propertyType.getName();
            } catch (Exception ex) {
                return "Unknown Property";
            }
        }
        return null;
    }

    public void setDisplayType(Boolean displayType) {
        this.displayType = displayType;
    }

    public Boolean getDisplayTypeCategory() {
        return displayTypeCategory;
    }

    public void setDisplayTypeCategory(Boolean displayTypeCategory) {
        this.displayTypeCategory = displayTypeCategory;
    }

    public String getFilterByType() {
        return filterByType;
    }

    public void setFilterByType(String filterByType) {
        this.filterByType = filterByType;
    }

    public String getFilterByTypeCategory() {
        return filterByTypeCategory;
    }

    public void setFilterByTypeCategory(String filterByTypeCategory) {
        this.filterByTypeCategory = filterByTypeCategory;
    }

    public Boolean getSelectDisplayType() {
        return selectDisplayType;
    }

    public void setSelectDisplayType(Boolean selectDisplayType) {
        this.selectDisplayType = selectDisplayType;
    }

    public Boolean getSelectDisplayTypeCategory() {
        return selectDisplayTypeCategory;
    }

    public void setSelectDisplayTypeCategory(Boolean selectDisplayTypeCategory) {
        this.selectDisplayTypeCategory = selectDisplayTypeCategory;
    }

    public String getSelectFilterByType() {
        return selectFilterByType;
    }

    public void setSelectFilterByType(String selectFilterByType) {
        this.selectFilterByType = selectFilterByType;
    }

    public String getSelectFilterByTypeCategory() {
        return selectFilterByTypeCategory;
    }

    public void setSelectFilterByTypeCategory(String selectFilterByTypeCategory) {
        this.selectFilterByTypeCategory = selectFilterByTypeCategory;
    }

    public Integer getDisplayPropertyTypeId1() {
        return displayPropertyTypeId1;
    }

    public void setDisplayPropertyTypeId1(Integer displayPropertyTypeId1) {
        this.displayPropertyTypeId1 = displayPropertyTypeId1;
    }

    public Integer getDisplayPropertyTypeId2() {
        return displayPropertyTypeId2;
    }

    public void setDisplayPropertyTypeId2(Integer displayPropertyTypeId2) {
        this.displayPropertyTypeId2 = displayPropertyTypeId2;
    }

    public Integer getDisplayPropertyTypeId3() {
        return displayPropertyTypeId3;
    }

    public void setDisplayPropertyTypeId3(Integer displayPropertyTypeId3) {
        this.displayPropertyTypeId3 = displayPropertyTypeId3;
    }

    public Integer getDisplayPropertyTypeId4() {
        return displayPropertyTypeId4;
    }

    public void setDisplayPropertyTypeId4(Integer displayPropertyTypeId4) {
        this.displayPropertyTypeId4 = displayPropertyTypeId4;
    }

    public Integer getDisplayPropertyTypeId5() {
        return displayPropertyTypeId5;
    }

    public void setDisplayPropertyTypeId5(Integer displayPropertyTypeId5) {
        this.displayPropertyTypeId5 = displayPropertyTypeId5;
    }

    public String getFilterByPropertyValue1() {
        return filterByPropertyValue1;
    }

    public void setFilterByPropertyValue1(String filterByPropertyValue1) {
        this.filterByPropertyValue1 = filterByPropertyValue1;
    }

    public String getFilterByPropertyValue2() {
        return filterByPropertyValue2;
    }

    public void setFilterByPropertyValue2(String filterByPropertyValue2) {
        this.filterByPropertyValue2 = filterByPropertyValue2;
    }

    public String getFilterByPropertyValue3() {
        return filterByPropertyValue3;
    }

    public void setFilterByPropertyValue3(String filterByPropertyValue3) {
        this.filterByPropertyValue3 = filterByPropertyValue3;
    }

    public String getFilterByPropertyValue4() {
        return filterByPropertyValue4;
    }

    public void setFilterByPropertyValue4(String filterByPropertyValue4) {
        this.filterByPropertyValue4 = filterByPropertyValue4;
    }

    public String getFilterByPropertyValue5() {
        return filterByPropertyValue5;
    }

    public void setFilterByPropertyValue5(String filterByPropertyValue5) {
        this.filterByPropertyValue5 = filterByPropertyValue5;
    }

    public String getSelectedComponentImage() {
        return selectedComponentImage;
    }

    public void setSelectedComponentImage(String selectedComponentImage) {
        this.selectedComponentImage = selectedComponentImage;
    }

    public Boolean getDisplayComponentImages() {
        return displayComponentImages;
    }

    public void setDisplayComponentImages(Boolean displayComponentImages) {
        this.displayComponentImages = displayComponentImages;
    }

    public void prepareComponentImageList(Component component) {
        displayComponentImages = false;
        componentImageList = new ArrayList<>();
        List<PropertyValue> propertyValueList = component.getPropertyValueList();
        for (PropertyValue propertyValue : propertyValueList) {
            PropertyTypeHandlerInterface propertyTypeHandler = PropertyTypeHandlerFactory.getHandler(propertyValue);
            DisplayType valueDisplayType = propertyTypeHandler.getValueDisplayType();
            if (valueDisplayType == DisplayType.IMAGE && !propertyValue.getValue().isEmpty()) {
                componentImageList.add(propertyValue);
            }
        }
        if (!componentImageList.isEmpty()) {
            PropertyValue propertyValue = componentImageList.get(0);
            selectedComponentImage = StorageUtility.getApplicationPropertyValueImagesDirectory() + "/"
                    + propertyValue.getValue();
            displayComponentImages = true;
        }
    }

    public List<PropertyValue> getComponentImageList() {
        if (componentImageList == null) {
            prepareComponentImageList(getCurrent());
        }
        return componentImageList;
    }

}
