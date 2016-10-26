/*
 * Copyright (c) 2014-2015, Argonne National Laboratory.
 *
 * SVN Information:
 *   $HeadURL: https://svn.aps.anl.gov/cdb/trunk/src/java/CdbWebPortal/src/java/gov/anl/aps/cdb/portal/model/jsf/handlers/DocumentPropertyTypeHandler.java $
 *   $Date: 2015-08-17 09:49:06 -0500 (Mon, 17 Aug 2015) $
 *   $Revision: 709 $
 *   $Author: sveseli $
 */
package gov.anl.aps.cdb.portal.model.jsf.handlers;

import gov.anl.aps.cdb.portal.constants.DisplayType;
import gov.anl.aps.cdb.portal.model.db.entities.PropertyValue;
import gov.anl.aps.cdb.portal.model.db.entities.PropertyValueHistory;
import gov.anl.aps.cdb.portal.utilities.StorageUtility;

/**
 * Document property type handler.
 */
public class DocumentPropertyTypeHandler extends AbstractPropertyTypeHandler {

    public static final String HANDLER_NAME = "Document";

    public DocumentPropertyTypeHandler() {
        super(HANDLER_NAME, DisplayType.DOCUMENT);
    }

    @Override
    public String getEditActionOncomplete() {
        return "PF('propertyValueDocumentUploadDialogWidget').show()";
    }

    @Override
    public String getEditActionIcon() {
        return "ui-icon-circle-arrow-n";
    }

    @Override
    public String getEditActionBean() {
        return "propertyValueDocumentUploadBean";
    }

    @Override
    public void setTargetValue(PropertyValue propertyValue) {
        String targetLink = propertyValue.getValue();
        if (targetLink != null && !targetLink.isEmpty()) {
            targetLink = StorageUtility.getApplicationPropertyValueDocumentPath(targetLink);
            propertyValue.setTargetValue(targetLink);
        }
    }

    @Override
    public void setTargetValue(PropertyValueHistory propertyValueHistory) {
        String targetLink = propertyValueHistory.getValue();
        if (targetLink != null && !targetLink.isEmpty()) {
            targetLink = StorageUtility.getApplicationPropertyValueDocumentPath(targetLink);
            propertyValueHistory.setTargetValue(targetLink);
        }
    }
}