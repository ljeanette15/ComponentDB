#!/usr/bin/env python

#######################################################################

import cherrypy
import json

from cdb.common.service.cdbController import CdbController
from cdb.common.objects.cdbObject import CdbObject
from cdb.common.exceptions.cdbException import CdbException
from cdb.common.exceptions.internalError import InternalError

from cdb.cdb_web_service.impl.userInfoControllerImpl import UserInfoControllerImpl

#######################################################################

class UserInfoController(CdbController):

    def __init__(self):
        CdbController.__init__(self)
        self.userInfoControllerImpl = UserInfoControllerImpl()

    @cherrypy.expose
    def getUsers(self, **kwargs):
        try:
           response = '%s' % self.toJson(self.userInfoControllerImpl.getUsers())
        except CdbException, ex:
            self.logger.error('%s' % ex)
            self.handleException(ex)
            response = ex.getFullJsonRep()
        except Exception, ex:
            self.logger.error('%s' % ex)
            self.handleException(ex)
            response = InternalError(ex).getFullJsonRep()
        return self.formatJsonResponse(response)

    @cherrypy.expose
    def getUserById(self, id, **kwargs):
        try:
            if not id:
                raise InvalidRequest('Invalid id provided.')
            response = '%s' % self.userInfoControllerImpl.getUserById(id).getFullJsonRep()
            self.logger.debug('Returning user info for %s: %s' % (id,response))
        except CdbException, ex:
            self.logger.error('%s' % ex)
            self.handleException(ex)
            response = ex.getFullJsonRep()
        except Exception, ex:
            self.logger.error('%s' % ex)
            self.handleException(ex)
            response = InternalError(ex).getFullJsonRep()
        return self.formatJsonResponse(response)

    @cherrypy.expose
    def getUserByUsername(self, username, **kwargs):
        try:
            if not len(username):
                raise InvalidRequest('Invalid username provided.')
            response = '%s' % self.userInfoControllerImpl.getUserByUsername(username).getFullJsonRep()
            self.logger.debug('Returning user info for %s: %s' % (username,response))
        except CdbException, ex:
            self.logger.error('%s' % ex)
            self.handleException(ex)
            response = ex.getFullJsonRep()
        except Exception, ex:
            self.logger.error('%s' % ex)
            self.handleException(ex)
            response = InternalError(ex).getFullJsonRep()
        return self.formatJsonResponse(response)

