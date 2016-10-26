#!/usr/bin/env python

from cdbObject import CdbObject

class PdmLinkSearchResult(CdbObject):

    DEFAULT_KEY_LIST = ['number','name']

    def __init__(self, dict):
        CdbObject.__init__(self, dict)