#!/usr/bin/env python

from cdbObject import CdbObject

class ItemEntityType(CdbObject):

    DEFAULT_KEY_LIST = [ ]

    def __init__(self, dict):
        CdbObject.__init__(self, dict)
