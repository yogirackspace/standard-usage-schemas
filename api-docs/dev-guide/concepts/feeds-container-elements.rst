.. _container-elements: 

Container elements
~~~~~~~~~~~~~~~~~~~~~~

An Atom feed consists of a series of container elements, which contain
metadata or actual content related to the feed.

The Atom Publishing Protocol supports the following container elements:

-  :ref:`cf-atom-feed-element`

-  :ref:`cf-atom-entry-element`

-  :ref:`cf-atom-content-element`


.. _cf-atom-feed-element:

Atom feed element
^^^^^^^^^^^^^^^^^^^^

An Atom ``feed`` element is a representation of an Atom feed, including
metadata about the feed, and some or all of the entries associated with
it.

The Atom Feed element represents the top-level element of an Atom Feed
Document. It functions as a container for metadata and data associated
with the feed. Its element children consist of metadata elements that
are followed by zero or more Atom Entry child elements.

The following example shows an entire Atom ``feed`` element in XML format.

 
**Atom feed element - XML example**

.. code::  

    <feed xmlns="http://www.w3.org/2005/Atom">
        <link href="https://ord.feeds.api.rackspacecloud.com/functest1/events/1234" rel="current" />
        <link href="https://ord.feeds.api.rackspacecloud.com/functest1/events/1234" rel="self" />
      <id>urn:uuidc9807298-fec2-4a39-bd8c-dfe4a6421757</id>
      <title type="text">functest1/events</title>
      <link href="https://ord.feeds.api.rackspacecloud.com/functest1/events/1234?marker=urn:uuid:e53d007a-fc23-1131-975c-cfa6b29bb814&amp;limit=2&amp;search=&amp;direction=forward" rel="previous"/>
      <link href="https://ord.feeds.api.rackspacecloud.com/functest1/events/1234?marker=urn:uuid:6fa234aea93f38c26fa234aea93f38c2&amp;limit=2&amp;search=&amp;direction=backward" rel="next"/>
      <link href="https://ord.feeds.api.rackspacecloud.com/functest1/events/1234?marker=last&amp;limit=2&amp;search=&amp;direction=backward" rel="last"/>
      <updated>2015-05-07T15:10:59.333Z</updated>
      <atom:entry xmlns:atom="http://www.w3.org/2005/Atom" xmlns="http://www.w3.org/2001/XMLSchema" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
        <atom:id>urn:uuid:e53d007a-fc23-1131-975c-cfa6b29bb814</atom:id>
        <atom:category term="tid:1234"/>
        <atom:category term="rgn:DFW"/>
        <atom:category term="dc:DFW1"/>
        <atom:category term="rid:4a2b42f4-6c63-11e2-815b-7fcbcf67f549"/>
        <atom:category term="widget.explicit.widget.usage"/>
        <atom:category term="type:widget.explicit.widget.usage"/>
        <atom:title type="text">Widget</atom:title>
        <atom:content type="application/xml">
          <event xmlns="http://docs.rackspace.com/core/event" xmlns:sample="http://docs.rackspace.com/usage/widget/explicit" dataCenter="DFW1" endTime="2013-03-15T23:59:59Z" environment="PROD" id="e53d007a-fc23-1131-975c-cfa6b29bb814" region="DFW" resourceId="4a2b42f4-6c63-11e2-815b-7fcbcf67f549" startTime="2013-03-15T13:51:11Z" tenantId="1234" type="USAGE" version="1">
            <sample:product dateTime="2013-09-26T15:32:00Z" disabled="false" enumList="BEST BEST" label="sampleString" mid="6e8bc430-9c3a-11d9-9669-0800200c9a66" num_checks="1" resourceType="WIDGET" serviceCode="Widget" stringEnum="3.0.1" time="15:32:00Z" version="1"/>
          </event>
        </atom:content>
        <atom:link href="https://ord.feeds.api.rackspacecloud.com/functest1/events/1234/entries/urn:uuid:e53d007a-fc23-1131-975c-cfa6b29bb814" rel="self"/>
        <atom:updated>2015-05-07T15:10:39.991Z</atom:updated>
        <atom:published>2015-05-07T15:10:39.991Z</atom:published>
      </atom:entry>
      <atom:entry xmlns:atom="http://www.w3.org/2005/Atom" xmlns="http://www.w3.org/2001/XMLSchema" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
        <atom:id>urn:uuid:e53d007a-fc23-11e1-975c-cfa6b29bb814</atom:id>
        <atom:category term="tid:1234"/>
        <atom:category term="rgn:DFW"/>
        <atom:category term="dc:DFW1"/>
        <atom:category term="rid:4a2b42f4-6c63-11e1-815b-7fcbcf67f549"/>
        <atom:category term="widget.explicit.widget.usage"/>
        <atom:category term="type:widget.explicit.widget.usage"/>
        <atom:title type="text">Widget</atom:title>
        <atom:content type="application/xml">
          <event xmlns="http://docs.rackspace.com/core/event" xmlns:sample="http://docs.rackspace.com/usage/widget/explicit" dataCenter="DFW1" endTime="2013-03-15T23:59:59Z" environment="PROD" id="e53d007a-fc23-11e1-975c-cfa6b29bb814" region="DFW" resourceId="4a2b42f4-6c63-11e1-815b-7fcbcf67f549" startTime="2013-03-15T11:51:11Z" tenantId="1234" type="USAGE" version="1">
            <sample:product dateTime="2013-09-26T15:32:00Z" disabled="false" enumList="BEST BEST" label="sampleString" mid="6e8bc430-9c3a-11d9-9669-0800200c9a66" num_checks="1" resourceType="WIDGET" serviceCode="Widget" stringEnum="3.0.1" time="15:32:00Z" version="1"/>
          </event>
        </atom:content>
        <atom:link href="https://ord.feeds.api.rackspacecloud.com/functest1/events/1234/entries/urn:uuid:e53d007a-fc23-11e1-975c-cfa6b29bb814" rel="self"/>
        <atom:updated>2015-05-07T15:09:41.060Z</atom:updated>
        <atom:published>2015-05-07T15:09:41.060Z</atom:published>
      </atom:entry>
    </feed>

The following example shows an entire Atom ``feed`` element in JSON format.

 
**Atom feed element - JSON example**

.. code::  

    {
        "feed": {
            "@type": "http://www.w3.org/2005/Atom",
            "entry": [
                {
                    "category": [
                        {
                            "term": "tid:1234"
                        },
                        {
                            "term": "rgn:DFW"
                        },
                        {
                            "term": "dc:DFW1"
                        },
                        {
                            "term": "rid:4a2b42f4-6c63-11e2-815b-7fcbcf67f549"
                        },
                        {
                            "term": "widget.explicit.widget.usage"
                        },
                        {
                            "term": "type:widget.explicit.widget.usage"
                        }
                    ],
                    "content": {
                        "event": {
                            "@type": "http://docs.rackspace.com/core/event",
                            "dataCenter": "DFW1",
                            "endTime": "2013-03-15T23:59:59Z",
                            "environment": "PROD",
                            "id": "e53d007a-fc23-1131-975c-cfa6b29bb814",
                            "product": {
                                "@type": "http://docs.rackspace.com/usage/widget/explicit",
                                "dateTime": "2013-09-26T15:32:00Z",
                                "disabled": false,
                                "enumList": "BEST BEST",
                                "label": "sampleString",
                                "mid": "6e8bc430-9c3a-11d9-9669-0800200c9a66",
                                "num_checks": 1,
                                "resourceType": "WIDGET",
                                "serviceCode": "Widget",
                                "stringEnum": "3.0.1",
                                "time": "15:32:00Z",
                                "version": "1"
                            },
                            "region": "DFW",
                            "resourceId": "4a2b42f4-6c63-11e2-815b-7fcbcf67f549",
                            "startTime": "2013-03-15T13:51:11Z",
                            "tenantId": "1234",
                            "type": "USAGE",
                            "version": "1"
                        }
                    },
                    "id": "urn:uuid:e53d007a-fc23-1131-975c-cfa6b29bb814",
                    "link": [
                        {
                            "href": "https://ord.feeds.api.rackspacecloud.com/functest1/events/1234/entries/urn:uuid:e53d007a-fc23-1131-975c-cfa6b29bb814",
                            "rel": "self"
                        }
                    ],
                    "published": "2015-05-07T15:10:39.991Z",
                    "title": {
                        "@text": "Widget",
                        "type": "text"
                    },
                    "updated": "2015-05-07T15:10:39.991Z"
                },
                {
                    "category": [
                        {
                            "term": "tid:1234"
                        },
                        {
                            "term": "rgn:DFW"
                        },
                        {
                            "term": "dc:DFW1"
                        },
                        {
                            "term": "rid:4a2b42f4-6c63-11e1-815b-7fcbcf67f549"
                        },
                        {
                            "term": "widget.explicit.widget.usage"
                        },
                        {
                            "term": "type:widget.explicit.widget.usage"
                        }
                    ],
                    "content": {
                        "event": {
                            "@type": "http://docs.rackspace.com/core/event",
                            "dataCenter": "DFW1",
                            "endTime": "2013-03-15T23:59:59Z",
                            "environment": "PROD",
                            "id": "e53d007a-fc23-11e1-975c-cfa6b29bb814",
                            "product": {
                                "@type": "http://docs.rackspace.com/usage/widget/explicit",
                                "dateTime": "2013-09-26T15:32:00Z",
                                "disabled": false,
                                "enumList": "BEST BEST",
                                "label": "sampleString",
                                "mid": "6e8bc430-9c3a-11d9-9669-0800200c9a66",
                                "num_checks": 1,
                                "resourceType": "WIDGET",
                                "serviceCode": "Widget",
                                "stringEnum": "3.0.1",
                                "time": "15:32:00Z",
                                "version": "1"
                            },
                            "region": "DFW",
                            "resourceId": "4a2b42f4-6c63-11e1-815b-7fcbcf67f549",
                            "startTime": "2013-03-15T11:51:11Z",
                            "tenantId": "1234",
                            "type": "USAGE",
                            "version": "1"
                        }
                    },
                    "id": "urn:uuid:e53d007a-fc23-11e1-975c-cfa6b29bb814",
                    "link": [
                        {
                            "href": "https://ord.feeds.api.rackspacecloud.com/functest1/events/1234/entries/urn:uuid:e53d007a-fc23-11e1-975c-cfa6b29bb814",
                            "rel": "self"
                        }
                    ],
                    "published": "2015-05-07T15:09:41.060Z",
                    "title": {
                        "@text": "Widget",
                        "type": "text"
                    },
                    "updated": "2015-05-07T15:09:41.060Z"
                }
            ],
            "id": "urn:uuidc9807298-fec2-4a39-bd8c-dfe4a6421757",
            "link": [
                {
                    "href": "https://ord.feeds.api.rackspacecloud.com/functest1/events/1234",
                    "rel": "current"
                },
                {
                    "href": "https://ord.feeds.api.rackspacecloud.com/functest1/events/1234",
                    "rel": "self"
                },
                {
                    "href": "https://ord.feeds.api.rackspacecloud.com/functest1/events/1234?marker=urn:uuid:e53d007a-fc23-1131-975c-cfa6b29bb814&limit=2&search=&direction=forward",
                    "rel": "previous"
                },
                {
                    "href": "https://ord.feeds.api.rackspacecloud.com/functest1/events/1234?marker=urn:uuid:6fa234aea93f38c26fa234aea93f38c2&limit=2&search=&direction=backward",
                    "rel": "next"
                },
                {
                    "href": "https://ord.feeds.api.rackspacecloud.com/functest1/events/1234?marker=last&limit=2&search=&direction=backward",
                    "rel": "last"
                }
            ],
            "title": {
                "@text": "functest1/events",
                "type": "text"
            },
            "updated": "2015-05-07T15:10:59.333Z
        }
    }


.. _cf-atom-entry-element:

Atom entry element
^^^^^^^^^^^^^^^^^^^^^

The Atom ``entry`` element represents exactly one Atom entry, outside of
the context of an Atom feed. It functions as a container for metadata
and data associated with the entry. This element can appear as a child
of the Atom ``feed`` element, or it can appear as the top-level element
of a stand-alone Atom Entry Document.

The following example shows an Atom ``entry`` element in XML format.

 
**Atom entry element - XML example**

.. code::  

    <atom:entry xmlns:atom="http://www.w3.org/2005/Atom" xmlns="http://www.w3.org/2001/XMLSchema" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
        <atom:id>urn:uuid:e53d007a-fc23-1131-975c-cfa6b29bb814</atom:id>
        <atom:category term="tid:1234"/>
        <atom:category term="rgn:DFW"/>
        <atom:category term="dc:DFW1"/>
        <atom:category term="rid:4a2b42f4-6c63-11e2-815b-7fcbcf67f549"/>
        <atom:category term="widget.explicit.widget.usage"/>
        <atom:category term="type:widget.explicit.widget.usage"/>
        <atom:title type="text">Widget</atom:title>
        <atom:content type="application/xml">
            <event xmlns="http://docs.rackspace.com/core/event" xmlns:sample="http://docs.rackspace.com/usage/widget/explicit" dataCenter="DFW1" endTime="2013-03-15T23:59:59Z" environment="PROD" id="e53d007a-fc23-1131-975c-cfa6b29bb814" region="DFW" resourceId="4a2b42f4-6c63-11e2-815b-7fcbcf67f549" startTime="2013-03-15T13:51:11Z" tenantId="1234" type="USAGE" version="1">
                <sample:product dateTime="2013-09-26T15:32:00Z" disabled="false" enumList="BEST BEST" label="sampleString" mid="6e8bc430-9c3a-11d9-9669-0800200c9a66" num_checks="1" resourceType="WIDGET" serviceCode="Widget" stringEnum="3.0.1" time="15:32:00Z" version="1"/>
            </event>
        </atom:content>
        <atom:link href="https://ord.feeds.api.rackspacecloud.com/functest1/events/1234/entries/urn:uuid:e53d007a-fc23-1131-975c-cfa6b29bb814" rel="self"/>
        <atom:updated>2015-05-07T15:10:39.991Z</atom:updated>
        <atom:published>2015-05-07T15:10:39.991Z</atom:published>
    </atom:entry>
    

The following example shows an Atom ``entry`` element in JSON format.
 
**Atom entry element - JSON example**

.. code::  

    {
        "entry": {
            "@type": "http://www.w3.org/2005/Atom",
            "category": [
                {
                    "term": "tid:1234"
                },
                {
                    "term": "rgn:DFW"
                },
                {
                    "term": "dc:DFW1"
                },
                {
                    "term": "rid:4a2b42f4-6c63-11e2-815b-7fcbcf67f549"
                },
                {
                    "term": "widget.explicit.widget.usage"
                },
                {
                    "term": "type:widget.explicit.widget.usage"
                }
            ],
            "content": {
                "event": {
                    "@type": "http://docs.rackspace.com/core/event",
                    "dataCenter": "DFW1",
                    "endTime": "2013-03-15T23:59:59Z",
                    "environment": "PROD",
                    "id": "e53d007a-fc23-1131-975c-cfa6b29bb814",
                    "product": {
                        "@type": "http://docs.rackspace.com/usage/widget/explicit",
                        "dateTime": "2013-09-26T15:32:00Z",
                        "disabled": false,
                        "enumList": "BEST BEST",
                        "label": "sampleString",
                        "mid": "6e8bc430-9c3a-11d9-9669-0800200c9a66",
                        "num_checks": 1,
                        "resourceType": "WIDGET",
                        "serviceCode": "Widget",
                        "stringEnum": "3.0.1",
                        "time": "15:32:00Z",
                        "version": "1"
                    },
                    "region": "DFW",
                    "resourceId": "4a2b42f4-6c63-11e2-815b-7fcbcf67f549",
                    "startTime": "2013-03-15T13:51:11Z",
                    "tenantId": "1234",
                    "type": "USAGE",
                    "version": "1"
                }
            },
            "id": "urn:uuid:e53d007a-fc23-1131-975c-cfa6b29bb814",
            "link": [
                {
                    "href": "https://ord.feeds.api.rackspacecloud.com/functest1/events/1234/entries/urn:uuid:e53d007a-fc23-1131-975c-cfa6b29bb814",
                    "rel": "self"
                }
            ],
            "published": "2015-05-07T15:10:39.991Z",
            "title": {
                "@text": "Widget",
                "type": "text"
            },
            "updated": "2015-05-07T15:10:39.991Z"
        }
    }



.. _cf-atom-content-element:

Atom content element
^^^^^^^^^^^^^^^^^^^^^^^

The Atom ``content`` element either contains or links to the content of
an entry. The type attribute specifies the MIME media ``type``. If no
``type`` attribute is present, the content is treated as ``text``.

The following example shows an Atom ``content`` element in XML format.

 
**Atom content element - XML example**

.. code::  

    <atom:content type="application/xml"
                  xmlns:atom="http://www.w3.org/2005/Atom">
        <event xmlns="http://docs.rackspace.com/core/event" xmlns:sample="http://docs.rackspace.com/usage/widget/explicit" dataCenter="DFW1" endTime="2013-03-15T23:59:59Z" environment="PROD" id="e53d007a-fc23-1131-975c-cfa6b29bb814" region="DFW" resourceId="4a2b42f4-6c63-11e2-815b-7fcbcf67f549" startTime="2013-03-15T13:51:11Z" tenantId="1234" type="USAGE" version="1">
            <sample:product dateTime="2013-09-26T15:32:00Z" disabled="false" enumList="BEST BEST" label="sampleString" mid="6e8bc430-9c3a-11d9-9669-0800200c9a66" num_checks="1" resourceType="WIDGET" serviceCode="Widget" stringEnum="3.0.1" time="15:32:00Z" version="1"/>
        </event>
    </atom:content>



The following example shows an Atom ``content`` element in JSON format.

 
**Atom content element - JSON example**

.. code::  

    {
        "content": {
            "event": {
                "@type": "http://docs.rackspace.com/core/event",
                "dataCenter": "DFW1",
                "endTime": "2013-03-15T23:59:59Z",
                "environment": "PROD",
                "id": "e53d007a-fc23-1131-975c-cfa6b29bb814",
                "product": {
                    "@type": "http://docs.rackspace.com/usage/widget/explicit",
                    "dateTime": "2013-09-26T15:32:00Z",
                    "disabled": false,
                    "enumList": "BEST BEST",
                    "label": "sampleString",
                    "mid": "6e8bc430-9c3a-11d9-9669-0800200c9a66",
                    "num_checks": 1,
                    "resourceType": "WIDGET",
                    "serviceCode": "Widget",
                    "stringEnum": "3.0.1",
                    "time": "15:32:00Z",
                    "version": "1"
                },
                "region": "DFW",
                "resourceId": "4a2b42f4-6c63-11e2-815b-7fcbcf67f549",
                "startTime": "2013-03-15T13:51:11Z",
                "tenantId": "1234",
                "type": "USAGE",
                "version": "1"
            }
        }
    }