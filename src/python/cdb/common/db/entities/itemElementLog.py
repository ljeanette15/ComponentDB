#!/usr/bin/env python

from cdb.common.db.entities.cdbDbEntity import CdbDbEntity
from cdb.common.objects import itemElementLog

class ItemElementLog(CdbDbEntity):

    entityDisplayName = 'item element log'

    cdbObjectClass = itemElementLog.ItemElementLog

    def __init__(self, **kwargs):
        CdbDbEntity.__init__(self, **kwargs)


