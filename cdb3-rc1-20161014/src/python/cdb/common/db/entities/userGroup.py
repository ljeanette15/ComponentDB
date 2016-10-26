#!/usr/bin/env python

from cdb.common.db.entities.cdbDbEntity import CdbDbEntity
from cdb.common.objects import userGroup

class UserGroup(CdbDbEntity):

    entityDisplayName = 'user group'

    cdbObjectClass = userGroup.UserGroup

    def __init__(self, **kwargs):
        CdbDbEntity.__init__(self, **kwargs)

