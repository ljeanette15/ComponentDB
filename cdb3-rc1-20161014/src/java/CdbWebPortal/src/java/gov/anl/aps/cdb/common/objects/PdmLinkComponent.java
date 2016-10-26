/*
 * Copyright (c) 2014-2015, Argonne National Laboratory.
 *
 * SVN Information:
 *   $HeadURL: https://svn.aps.anl.gov/cdb/trunk/src/java/CdbWebPortal/src/java/gov/anl/aps/cdb/common/objects/PdmLinkComponent.java $
 *   $Date: 2015-08-28 14:32:32 -0500 (Fri, 28 Aug 2015) $
 *   $Revision: 744 $
 *   $Author: djarosz $
 */
package gov.anl.aps.cdb.common.objects;

import java.util.LinkedList;

/**
 * Component Type object.
 */
public class PdmLinkComponent extends CdbObject {

    private LinkedList<ComponentType> suggestedComponentTypes;
    private String[] pdmPropertyValues; 
    private String wbsDescription;  
    private String drawingNumber; 
    private String cdbDescription;
    private String modelNumber; 
    
    public PdmLinkComponent() {
    }

    public String[] getPdmPropertyValues() {
        return pdmPropertyValues;
    }

    public LinkedList<ComponentType> getSuggestedComponentTypes() {
        return suggestedComponentTypes;
    }

    public String getWbsDescription() {
        return wbsDescription;
    }

    public String getDrawingNumber() {
        return drawingNumber;
    }
    
    public String getCdbDescription() {
        return cdbDescription;
    }
    
    public void setCdbDescription(String cdbDescription) {
        this.cdbDescription = cdbDescription; 
    }
    
    public void setPdmPropertyValues(String[] pdmPropertyValues) {
        this.pdmPropertyValues = pdmPropertyValues;
    }

    public void setSuggestedComponentTypes(LinkedList<ComponentType> suggestedComponentTypes) {
        this.suggestedComponentTypes = suggestedComponentTypes;
    }

    public void setWbsDescription(String wbsDescription) {
        this.wbsDescription = wbsDescription;
    }

    public void setDrawingNumber(String drawingNumber) {
        this.drawingNumber = drawingNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getModelNumber() {
        return modelNumber;
    }
    
    public String getDisplayPdmLinkPropertiesString(){
        String result = ""; 
        for (String pdmPropertyValue : pdmPropertyValues) {
            result += pdmPropertyValue + "<br/>";
        }
        return result; 
    }
    
}