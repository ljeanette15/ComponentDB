/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.anl.aps.cdb.portal.model.beans;

import gov.anl.aps.cdb.portal.model.entities.PropertyTypeHandler;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author sveseli
 */
@Stateless
public class PropertyTypeHandlerFacade extends AbstractFacade<PropertyTypeHandler>
{

    @PersistenceContext(unitName = "CdbWebPortalPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PropertyTypeHandlerFacade() {
        super(PropertyTypeHandler.class);
    }

    public PropertyTypeHandler findByName(String name) {
        try {
            return (PropertyTypeHandler) em.createNamedQuery("PropertyTypeHandler.findByName")
                    .setParameter("name", name)
                    .getSingleResult();
        }
        catch (NoResultException ex) {
        }
        return null;
    }

}
