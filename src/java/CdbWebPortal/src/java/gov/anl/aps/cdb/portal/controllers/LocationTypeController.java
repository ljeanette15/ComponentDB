package gov.anl.aps.cdb.portal.controllers;

import gov.anl.aps.cdb.portal.model.db.entities.LocationType;
import gov.anl.aps.cdb.portal.controllers.util.JsfUtil;
import gov.anl.aps.cdb.portal.controllers.util.PaginationHelper;
import gov.anl.aps.cdb.portal.exceptions.ObjectAlreadyExists;
import gov.anl.aps.cdb.portal.model.db.beans.LocationTypeFacade;
import gov.anl.aps.cdb.portal.model.db.entities.SettingType;
import gov.anl.aps.cdb.portal.model.db.entities.UserInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

@Named("locationTypeController")
@SessionScoped
public class LocationTypeController extends CrudEntityController<LocationType, LocationTypeFacade> implements Serializable {

    private static final String DisplayNumberOfItemsPerPageSettingTypeKey = "LocationType.List.Display.NumberOfItemsPerPage";
    private static final String DisplayIdSettingTypeKey = "LocationType.List.Display.Id";
    private static final String DisplayDescriptionSettingTypeKey = "LocationType.List.Display.Description";

    private static final String FilterByNameSettingTypeKey = "LocationType.List.FilterBy.Name";
    private static final String FilterByDescriptionSettingTypeKey = "LocationType.List.FilterBy.Description";

    private static final Logger logger = Logger.getLogger(LocationTypeController.class.getName());

    @EJB
    private LocationTypeFacade locationTypeFacade;

    public LocationTypeController() {
    }

    @Override
    protected LocationTypeFacade getFacade() {
        return locationTypeFacade;
    }

    @Override
    protected LocationType createEntityInstance() {
        LocationType locationType = new LocationType();
        return locationType;
    }

    @Override
    public String getEntityTypeName() {
        return "locationType";
    }

    @Override
    public String getDisplayEntityTypeName() {
        return "location type";
    }

    @Override
    public String getCurrentEntityInstanceName() {
        if (getCurrent() != null) {
            return getCurrent().getName();
        }
        return "";
    }

    public LocationType findById(Integer id) {
        return locationTypeFacade.findById(id);
    }

    @Override
    public void selectByRequestParams() {
        if (idViewParam != null) {
            LocationType locationType = findById(idViewParam);
            setCurrent(locationType);
            idViewParam = null;
        }
    }

    @Override
    public List<LocationType> getAvailableItems() {
        return super.getAvailableItems();
    }

    @Override
    public void prepareEntityInsert(LocationType locationType) throws ObjectAlreadyExists {
        LocationType existingLocationType = locationTypeFacade.findByName(locationType.getName());
        if (existingLocationType != null) {
            throw new ObjectAlreadyExists("Location type " + locationType.getName() + " already exists.");
        }
        logger.debug("Inserting new location type " + locationType.getName());
    }

    @Override
    public void prepareEntityUpdate(LocationType locationType) throws ObjectAlreadyExists {
    }

    @Override
    public void updateSettingsFromSettingTypeDefaults(Map<String, SettingType> settingTypeMap) {
        if (settingTypeMap == null) {
            return;
        }

        displayNumberOfItemsPerPage = Integer.parseInt(settingTypeMap.get(DisplayNumberOfItemsPerPageSettingTypeKey).getDefaultValue());
        displayId = Boolean.parseBoolean(settingTypeMap.get(DisplayIdSettingTypeKey).getDefaultValue());
        displayDescription = Boolean.parseBoolean(settingTypeMap.get(DisplayDescriptionSettingTypeKey).getDefaultValue());

        filterByName = settingTypeMap.get(FilterByNameSettingTypeKey).getDefaultValue();
        filterByDescription = settingTypeMap.get(FilterByDescriptionSettingTypeKey).getDefaultValue();

    }

    @Override
    public void updateSettingsFromSessionUser(UserInfo sessionUser) {
        if (sessionUser == null) {
            return;
        }

        displayNumberOfItemsPerPage = sessionUser.getUserSettingValueAsInteger(DisplayNumberOfItemsPerPageSettingTypeKey, displayNumberOfItemsPerPage);
        displayId = sessionUser.getUserSettingValueAsBoolean(DisplayIdSettingTypeKey, displayId);
        displayDescription = sessionUser.getUserSettingValueAsBoolean(DisplayDescriptionSettingTypeKey, displayDescription);

        filterByName = sessionUser.getUserSettingValueAsString(FilterByNameSettingTypeKey, filterByName);
        filterByDescription = sessionUser.getUserSettingValueAsString(FilterByDescriptionSettingTypeKey, filterByDescription);
    }


    @Override
    public void saveSettingsForSessionUser(UserInfo sessionUser) {
        if (sessionUser == null) {
            return;
        }

        sessionUser.setUserSettingValue(DisplayNumberOfItemsPerPageSettingTypeKey, displayNumberOfItemsPerPage);
        sessionUser.setUserSettingValue(DisplayIdSettingTypeKey, displayId);
        sessionUser.setUserSettingValue(DisplayDescriptionSettingTypeKey, displayDescription);

        sessionUser.setUserSettingValue(FilterByNameSettingTypeKey, filterByName);
        sessionUser.setUserSettingValue(FilterByDescriptionSettingTypeKey, filterByDescription);
    }


    @FacesConverter(forClass = LocationType.class)
    public static class LocationTypeControllerConverter implements Converter
    {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LocationTypeController controller = (LocationTypeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "locationTypeController");
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
            if (object instanceof LocationType) {
                LocationType o = (LocationType) object;
                return getStringKey(o.getId());
            }
            else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + LocationType.class.getName());
            }
        }

    }

}
