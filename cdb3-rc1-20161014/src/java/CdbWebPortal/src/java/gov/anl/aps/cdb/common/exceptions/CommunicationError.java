/*
 * Copyright (c) 2014-2015, Argonne National Laboratory.
 *
 * SVN Information:
 *   $HeadURL: https://svn.aps.anl.gov/cdb/trunk/src/java/CdbWebPortal/src/java/gov/anl/aps/cdb/common/exceptions/CommunicationError.java $
 *   $Date: 2015-05-04 13:48:28 -0500 (Mon, 04 May 2015) $
 *   $Revision: 616 $
 *   $Author: sveseli $
 */
package gov.anl.aps.cdb.common.exceptions;

import gov.anl.aps.cdb.common.constants.CdbStatus;

/**
 * Communication error exception.
 */
public class CommunicationError extends CdbException {

    /**
     * Default constructor.
     */
    public CommunicationError() {
        super();
    }

    /**
     * Constructor using error message.
     *
     * @param message error message
     */
    public CommunicationError(String message) {
        super(message);
    }

    /**
     * Constructor using throwable object.
     *
     * @param throwable throwable object
     */
    public CommunicationError(Throwable throwable) {
        super(throwable);
    }

    /**
     * Constructor using error message and throwable object.
     *
     * @param message error message
     * @param throwable throwable object
     */
    public CommunicationError(String message, Throwable throwable) {
        super(message, throwable);
    }


    @Override
    public int getErrorCode() {
        return CdbStatus.CDB_COMMUNICATION_ERROR;
    }     
}