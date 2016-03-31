/*
 * Copyright (c) 2014-2015, Argonne National Laboratory.
 *
 * SVN Information:
 *   $HeadURL$
 *   $Date$
 *   $Revision$
 *   $Author$
 */
package gov.anl.aps.cdb.portal.model.db.beans;

import gov.anl.aps.cdb.portal.model.db.entities.EntityInfo;
import javax.ejb.Stateless;

/**
 * DB facade for entity info objects.
 */
@Stateless
public class EntityInfoDbFacade extends CdbEntityDbFacade<EntityInfo>
{
    public EntityInfoDbFacade() {
        super(EntityInfo.class);
    }
    
}
