.. _events-def:

Cloud Feeds events
~~~~~~~~~~~~~~~~~~~~~~~

This section describes the event types that are supported by Cloud Feeds
and where they are placed in a feed.

Cloud Feeds supports the following event types:

-  :ref:`product-events-def`

-  :ref:`cadf-user-access-events-def` 

-  :ref:`user-access-events-cloud-feeds-extended`


.. _product-events-def:

Cloud Feeds product events
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Cloud Feeds supports product events that contain information that is
specific to a certain product.

Product events are located in the ``event`` node as shown in the
following examples.

 
**Cloud feeds product events - XML example**

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

| 

 
**Cloud feeds product events - JSON example**

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

| 

The following table shows the attributes for the product event node.

**Table: Attributes for the product event node**

+-----------------------+-----------------------------------------------------+
| Name                  | Description                                         |
+=======================+=====================================================+
| ``dataCenter``        | Optional. Specifies the data center of the event.   |
|                       | If this attribute is not specified, **GLOBAL** is   |
|                       | assumed. **GLOBAL** implies that the resource is    |
|                       | without an assigned data center.                    |
+-----------------------+-----------------------------------------------------+
| ``endTime``           | Optional. Specifies the time that the event ends.   |
|                       | The format must be ISO 8601 format:                 |
|                       | yyyy-mm-ddThh:mm:ss.SSSZ (Z designates UTC). For    |
|                       | an event of type **EXIST**, the ``startTime`` and   |
|                       | ``endTime`` reflect the event duration for the      |
|                       | resource instance. The end time is exclusive —      |
|                       | that is, the event occurred up to, but not during   |
|                       | the specified value. The end time must occur after  |
|                       | the start time.                                     |
+-----------------------+-----------------------------------------------------+
| ``environment``       | Specifies the environment from which the message    |
|                       | originated. If this attribute is not specified,     |
|                       | **PROD** is assumed. This attribute is required     |
|                       | for events of type **USAGE\_SNAPSHOT**, but is      |
|                       | optional for all other event types.                 |
+-----------------------+-----------------------------------------------------+
| ``eventTime``         | Optional. Specifies the time of the event, using    |
|                       | ISO 8601 format and UTC. Use this attribute         |
|                       | instead of ``startTime`` and ``endTime`` in cases   |
|                       | where the event does not have a range.              |
+-----------------------+-----------------------------------------------------+
| ``id``                | Required. Specifies the UUID for the event record.  |
|                       | This value should be UUID version 1, 2, or 4. For   |
|                       | more information, see `RFC 4122                     |
|                       | <http://tools.ietf.org/html/rfc4122.txt>`_.         |
+-----------------------+-----------------------------------------------------+
| ``referenceId``       | Optional. Specifies a GUID that identifies the      |
|                       | event record that this record is updating. This     |
|                       | attribute should be used if this event is           |
|                       | correcting another event.                           |
+-----------------------+-----------------------------------------------------+
| ``region``            | Specifies the region in which the event is          |
|                       | located. If this attribute is not specified,        |
|                       | **GLOBAL** is assumed. **GLOBAL** implies that the  |
|                       | resource is without an assigned region.             |
+-----------------------+-----------------------------------------------------+
| ``resourceId``        | Specifies the ID of the resource. This attribute    |
|                       | is required if the ``resourceType`` attribute is    |
|                       | specified in the product node, but is optional      |
|                       | otherwise.                                          |
+-----------------------+-----------------------------------------------------+
| ``resourceName``      | Optional. Specifies the customer-defined name of    |
|                       | the resource.                                       |
+-----------------------+-----------------------------------------------------+
| ``resourceURI``       | Optional. Specifies a URI that uniquely identifies  |
|                       | the resource.                                       |
+-----------------------+-----------------------------------------------------+
| ``rootAction``        | Optional. Specifies the action that caused the      |
|                       | event.                                              |
+-----------------------+-----------------------------------------------------+
| ``severity``          | Optional. Specifies the severity of the event.      |
|                       | Valid values are **INFO**, **WARNING**, and         |
|                       | **CRITICAL**. This is attribute is valid only for   |
|                       | system events, not for usage events.                |
+-----------------------+-----------------------------------------------------+
| ``startTime``         | Specifies the time that the event starts. The       |
|                       | format must be ISO 8601 format:                     |
|                       | yyyy-mm-ddThh:mm:ss.SSSZ (Z designates UTC). The    |
|                       | start time is inclusive, which means that the       |
|                       | event occurred starting at the start time, not      |
|                       | after. This attribute is required for events of     |
|                       | type **USAGE**, but is optional for all other       |
|                       | event types.                                        |
+-----------------------+-----------------------------------------------------+
| ``tenantId``          | Optional. Specifies the tenant Id of the feeds      |
|                       | publisher                                           |
+-----------------------+-----------------------------------------------------+
| ``type``              | Required. Specifies the type of event. If one of    |
|                       | the existing event types fails to produce any       |
|                       | feeds, set this attribute to **EXTENDED** and add   |
|                       | an ``eventType`` attribute to your product schema.  |
+-----------------------+-----------------------------------------------------+
| ``version``           | Required. Specifies the version of the event        |
|                       | record.                                             |
+-----------------------+-----------------------------------------------------+

..  note:: 
    Cloud Feeds evaluates all product events against their XML schemas.


.. _cadf-user-access-events-def:

User access events in CADF
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Cloud Feeds supports the Cloud Auditing Data Federation (CADF) standard.
CADF provides a standard for the submission and retrieval of normative
audit event data from cloud providers in the form of customized reports
and logs. 

For more information about CADF, see `Cloud Auditing Data
Federation <http://dmtf.org/sites/default/files/standards/documents/DSP0262_1.0.0.pdf>`__.

Cloud Feeds defines a set of event types, which take different
attributes.

The following examples show a user access event that is encoded as a
CADF event. The CADF event is located inside the :ref:`cf-atom-content-element`.

 
**User access events - XML example**

.. code::  

    <?xml version="1.0" encoding="UTF-8"?>
    <?atom feed="functest1/events"?>
    <atom:entry xmlns:atom="http://www.w3.org/2005/Atom"
        xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        xmlns="http://www.w3.org/2001/XMLSchema">
        <atom:id>urn:uuid:6fa234aea93f38c26fa234aea93f38c2</atom:id>
        <atom:category term="tid:123456" />
        <atom:category term="rgn:DFW" />
        <atom:category term="dc:DFW1" />
        <atom:category term="username:jackhandy" />
        <atom:title type="text">UserAccessEvent</atom:title>
        <atom:content type="application/xml">
            <cadf:event xmlns:cadf="http://schemas.dmtf.org/cloud/audit/1.0/event"
                xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
                xmlns:ua="http://feeds.api.rackspacecloud.com/cadf/user-access-event"
                id="6fa234aea93f38c26fa234aea93f38c2"
                eventType="activity"
                typeURI="http://schemas.dmtf.org/cloud/audit/1.0/event"
                eventTime="2015-03-12T13:20:00-05:00"
                action="create/post"
                outcome="success">
                <cadf:initiator id="10.1.2.3" typeURI="network/node" name="jackhandy">
                    <cadf:host address="10.1.2.3" agent="curl/7.8 (i386-redhat-linux-gnu) libcurl 7.8" />
                </cadf:initiator>
                <cadf:target id="x.x.x.x" typeURI="service" name="IDM" >
                    <cadf:host address="lon.identity.api.rackspacecloud.com" />
                </cadf:target>
                <cadf:attachments>
                    <cadf:attachment name="auditData" contentType="ua:auditData">
                        <cadf:content>
                            <ua:auditData version="1">
                                <ua:region>DFW</ua:region>
                                <ua:dataCenter>DFW1</ua:dataCenter>
                                <ua:methodLabel>createToken</ua:methodLabel>
                                <ua:requestURL>https://lon.identity.api.rackspacecloud.com/v2.0/tokens</ua:requestURL>
                                <ua:queryString></ua:queryString>
                                <ua:tenantId>123456</ua:tenantId>
                                <ua:responseMessage>OK</ua:responseMessage>
                                <ua:userName>jackhandy</ua:userName>
                                <ua:roles>xxx</ua:roles>
                            </ua:auditData>
                        </cadf:content>
                    </cadf:attachment>
                </cadf:attachments>
                <cadf:observer id="IDM-1-1" name="repose-6.1.1.1" typeURI="service/security">
                    <cadf:host address="repose" />
                </cadf:observer>
                <cadf:reason reasonCode="200"
                    reasonType="http://www.iana.org/assignments/http-status-codes/http-status-codes.xml"/>
            </cadf:event>
        </atom:content>
    </atom:entry>

 
**User access events - JSON example**

.. code::  

    {
        "entry" : {
            "@type"   : "http://www.w3.org/2005/Atom",
            "id"      : "urn:uuid:6fa234aea93f38c26fa234aea93f38c2", 
            "category": [
                {
                    "term": "tid:123456"
                },
                {
                    "term": "rgn:DFW"
                },
                {
                    "term": "dc:DFW1"
                },
                {
                    "term": "username:jackhandy"
                },
            ],
            "title"   : "Identity User Access Event",
            "content" : {
                "event" : {
                    "typeURI"   : "http://schemas.dmtf.org/cloud/audit/1.0/event",
                    "id"        : "6fa234aea93f38c26fa234aea93f38c2",
                    "eventType" : "activity",
                    "eventTime" : "2015-03-12T13:20:00-05:00",
                    "action"    : "create/post",
                    "outcome"   : "success",

                    "initiator" : {
                        "id"      : "10.1.2.3",
                        "typeURI" : "network/node",
                        "name"    : "jackhandy",
                        "host"    : {
                            "address" : "10.1.2.3",
                            "agent"   : "curl/7.8 (i386-redhat-linux-gnu) libcurl 7.8"
                        }
                    },

                    "target" : {
                        "id"      : "x.x.x.x",
                        "typeURI" : "service",
                        "name"    : "IDM",
                        "host"    : {
                            "address" : "lon.identity.api.rackspacecloud.com"
                        }
                    },

                    "attachments" : [
                        {
                            "name"        : "auditData",
                            "contentType" : "http://feeds.api.rackspacecloud.com/cadf/user-access-event/auditData",
                            "content"     :  {
                                "auditData" : {
                                    "region"          : "DFW",
                                    "dataCenter"      : "DFW1",
                                    "methodLabel"     : "createToken",
                                    "requestURL"      : "https://lon.identity.api.rackspacecloud.com/v2.0/tokens",
                                    "queryString"     : "",
                                    "tenantId"        : "123456",
                                    "responseMessage" : "OK",
                                    "userName"        : "jackhandy",
                                    "roles"           : "xxx",
                                    "version"         : "1"
                                }
                            }
                        }
                    ],

                    "observer" : {
                        "id"      : "IDM-1-1",
                        "name"    : "repose-6.1.1.1",
                        "typeURI" : "service/security",
                        "host" : {
                            "address" : "repose"
                        }
                    },

                    "reason" : {
                        "reasonCode" : 200,
                        "reasonType" : "http://www.iana.org/assignments/http-status-codes/http-status-codes.xml"
                    }
                }
            }
        }
    }

The following table shows the CADF nodes that are specified in cadf.xsd.

**Table: Elements of the CADF event node**

+-------------------------------+--------------------------------------------+
| Name                          | Description                                |
+===============================+============================================+
| ``event``                     | Specifies the CADF event node. Contains a  |
|                               | set of attributes. For a detailed          |
|                               | description of the CADF event attributes,  |
|                               | see the “Attributes for CADF event node”   |
|                               | table below.                               |
+-------------------------------+--------------------------------------------+
| ``initiator``                 | Specifies the CADF event initiator.        |
|                               | Contains a set of attributes. For a        |
|                               | detailed description of the CADF initiator |
|                               | attributes, see the “Attributes for CADF   |
|                               | initiator node” table below.               |
+-------------------------------+--------------------------------------------+
| ``target``                    | Specifies the target. Contains a set of    |
|                               | attributes. For a detailed description of  |
|                               | the CADF target attributes, see the        |
|                               | “Attributes for CADF target node” table    |
|                               | below.                                     |
+-------------------------------+--------------------------------------------+
| ``attachments``               | Specifies an array of extended or          |
|                               | domain-specific information about the      |
| - ``attachment``              | node contains one or more nodes of type    |
|                               | of the CADF event attributes, see the      |
|                               | “Attributes for CADF target node” table    |
|                               | below.                                     |
|                               |                                            |
+-------------------------------+--------------------------------------------+
| ``observer``                  | Specifies the observer. For example, this  |
|                               | can be a security provider or a service,   |
|                               | such as Repose. Contains a set of          |
|                               | attributes. For a detailed description of  |
|                               | the CADF event attributes, see the         |
|                               | “Attributes for CADF observer node” table  |
|                               | below.                                     |
+-------------------------------+--------------------------------------------+
| ``reason``                    | Contains a domain-specific reason code and |
|                               | policy data that provides an additional    |
|                               | level of detail to the outcome value.      |
|                               | Contains a set of attributes. For a        |
|                               | detailed description of the CADF event     |
|                               | attributes, see the “Attributes for CADF   |
|                               | reason node” table below.                  |
+-------------------------------+--------------------------------------------+


The CADF events are located inside the CADF event node.

The following table shows the elements of the CADF event node.

**Table: Elements of the CADF event node**

+-----------------------+----------------------------------------------------+
| Element/Attribute     | Description                                        |
+=======================+====================================================+
| ``id``                | Required. Specifies the identifier for the         |
|                       | resource.                                          |
+-----------------------+----------------------------------------------------+
| ``eventType``         | Required. Specifies the purpose for creating the   |
|                       | audit record. Must be set to the value             |
|                       | ``activity``.                                      |
+-----------------------+----------------------------------------------------+
| ``typeURI``           | Required. Specifies the type of the resource that  |
|                       | is using the CADF Resource Taxonomy. Must be set   |
|                       | to the following URI:                              |
|                       | "http://schemas.dmtf.org/cloud/audit/1.0/event"    |
+-----------------------+----------------------------------------------------+
| ``eventTime``         | Required. Specifies the time the event occurred or |
|                       | began as seen by the observer.                     |
+-----------------------+----------------------------------------------------+
| ``action``            | Required. Specifies the type of activity that is   |
|                       | described in the event record. Must be set to      |
|                       | ``read.\*\| create.\*``                            |
+-----------------------+----------------------------------------------------+
| ``outcome``           | Required. Specifies the outcome or result of the   |
|                       | attempted action. Can be either ``success`` or     |
|                       | ``failure``.                                       |
+-----------------------+----------------------------------------------------+


The following table shows the elements of the CADF initiator node.

**Table: Elements of the CADF initiator node**

+-----------------------+----------------------------------------------------+
| Element/Attribute     | Description                                        |
+=======================+====================================================+
| ``id``                | Required. Specifies the identifier for the         |
|                       | resource.                                          |
+-----------------------+----------------------------------------------------+
| ``typeURI``           | Required. Specifies the type of the resource that  |
|                       | is using the CADF Resource Taxonomy. Can have one  |
|                       | of the following values:                           |
|                       |                                                    |
|                       |                                                    |
|                       |                                                    |
|                       | -  ``service/security/account/user`` for authorized|
|                       |    requests                                        |
|                       |                                                    |
|                       | -  ``network/node`` for unauthorized requests      |
|                       |                                                    |
|                       |                                                    |
+-----------------------+----------------------------------------------------+
| ``name``              | Specifies the name of the resource.                |
+-----------------------+----------------------------------------------------+
| ``host``              | Specifies the host. Takes one of the following 2   |
|                       | attributes:                                        |
|                       |                                                    |
|                       |                                                    |
|                       |                                                    |
|                       | -  ``address``                                     |
|                       |                                                    |
|                       | -  ``agent``                                       |
|                       |                                                    |
|                       |                                                    |
+-----------------------+----------------------------------------------------+


The following table shows the elements of the CADF target node.

**Table: Elements of the CADF target node**

+-----------------------+----------------------------------------------------+
| Element/Attribute     | Description                                        |
+=======================+====================================================+
| ``id``                | Required. Specifies the identifier for the         |
|                       | resource.                                          |
+-----------------------+----------------------------------------------------+
| ``typeURI``           | Required. Specifies the type of the resource that  |
|                       | is using the CADF Resource Taxonomy. Can have one  |
|                       | of the following values:                           |
|                       |                                                    |
|                       |                                                    |
|                       |                                                    |
|                       | -  ``service/security/account/user`` for authorized|
|                       |    requests                                        |
|                       |                                                    |
|                       | -  ``network/node`` for unauthorized requests      |
|                       |                                                    |
|                       |                                                    |
+-----------------------+----------------------------------------------------+
| ``name``              | Specifies the name of the target.                  |
+-----------------------+----------------------------------------------------+
| ``host``              | Specifies the host. Takes the following attribute: |
|                       |                                                    |
|                       |                                                    |
|                       |                                                    |
|                       | -  ``address``                                     |
|                       |                                                    |
|                       |                                                    |
+-----------------------+----------------------------------------------------+


The following table shows the elements of the CADF attachment node.

**Table: Elements of the CADF attachment node**

+-----------------------+----------------------------------------------------+
| Element/Attribute     | Description                                        |
+=======================+====================================================+
| ``name``              | Specifies the name of the attachment, for example  |
|                       | ``auditData``.                                     |
+-----------------------+----------------------------------------------------+
| ``contentType``       | Specifies the content type, for example            |
|                       | ``ua:auditData``.                                  |
+-----------------------+----------------------------------------------------+
| ``content``           | Contains a set of elements that define the         |
|                       | ``auditData`` property. ``auditData`` contains     |
|                       | attributes that define the user access event       |
|                       | profile for Cloud Feeds. For a detailed            |
|                       | description of the ``auditData`` property, see the |
|                       | “Attributes for auditData property” table in       |
|                       | :ref:`user-access-events-cloud-feeds-extended`.    |
+-----------------------+----------------------------------------------------+


The following table shows the elements of the CADF observer node.

**Table: Elements of the CADF observer node**

+-----------------------+----------------------------------------------------+
| Element/Attribute     | Description                                        |
+=======================+====================================================+
| ``id``                | Required. Specifies the identifier for the         |
|                       | resource.                                          |
+-----------------------+----------------------------------------------------+
| ``typeURI``           | Required. Specifies the type of the resource that  |
|                       | is using the CADF Resource Taxonomy. Can have one  |
|                       | of the following values:                           |
|                       |                                                    |
|                       |                                                    |
|                       |                                                    |
|                       | -  ``service/security/account/user`` for authorized|
|                       |    requests                                        |
|                       |                                                    |
|                       | -  ``network/node`` for unauthorized requests      |
|                       |                                                    |
|                       |                                                    |
+-----------------------+----------------------------------------------------+
| ``name``              | Specifies the name.                                |
+-----------------------+----------------------------------------------------+
| ``host``              | Specifies the host. Takes the following attribute: |
|                       |                                                    |
|                       |                                                    |
|                       |                                                    |
|                       | -  ``address``                                     |
|                       |                                                    |
|                       |                                                    |
+-----------------------+----------------------------------------------------+


The following table shows the elements of the CADF reason node.

**Table: Elements of the CADF reason node**

+-----------------------+----------------------------------------------------+
| Element/Attribute     | Description                                        |
+=======================+====================================================+
| ``reasonType``        | Specifies the reason type. For example, this can   |
|                       | be a URL to a HTTP status code registry, such as   |
|                       | `RFC 7231`_.                                       |
|                       |                                                    |
+-----------------------+----------------------------------------------------+
| ``reasonCode``        | Required. Specfies the HTTP status code. Can be    |
|                       | one of the following values: "[1-5] [0-9][0-9]".   |
+-----------------------+----------------------------------------------------+

.. _RFC 7231: http://www.iana.org/assignments/http-status-codes/

.. _user-access-events-cloud-feeds-extended:

User access events in Cloud Feeds
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Cloud Feeds supports user access events, which use the CADF standard.
User access events provide a generic approach to capturing all API calls
that are issued against Rackspace product web services.

Cloud Feeds extends the CADF standard by providing a specific user
access event profile that captures audit data that is specific to the
user and the Rackspace cloud services they are using.

User access event data is included in the atom entry as a CADF
attachment of type ``auditData``.

The following table shows the elements of the ``auditData`` property.

**Table: Elements of the auditData property**

+-------------------------+--------------------------------------------------+
| Element/Attribute       | Description                                      |
+=========================+==================================================+
| ``version``             | Required. Specifies the version of the audit     |
|                         | data.                                            |
+-------------------------+--------------------------------------------------+
| ``region``              | Required. Specifies the region, such as DFW. If  |
|                         | this value is not specified, GLOBAL is assumed.  |
+-------------------------+--------------------------------------------------+
| ``dataCenter``          | Required. Specifies the data center of the       |
|                         | event. If this value is not specified, GLOBAL is |
|                         | assumed. If this value is set, the region value  |
|                         | must match, for example, if dataCenter is set to |
|                         | "DFW1," then region must be set to "DFW."        |
+-------------------------+--------------------------------------------------+
| ``methodLabel``         | Optional. Specifies the method that is used for  |
|                         | this request. This attribute uses the friendly   |
|                         | name of an API request, for example ``addUser``. |
+-------------------------+--------------------------------------------------+
| ``requestURL``          | Specifies the URI of the request. If the URI     |
|                         | contains any query strings, they truncated.      |
+-------------------------+--------------------------------------------------+
| ``queryString``         | Optional. Specifies the query string. The query  |
|                         | string is part of a URI and contianes the data   |
|                         | that was added to the base URI, for example      |
|                         | ``/?query_string``.                              |
+-------------------------+--------------------------------------------------+
| ``tenantId``            | Specifies the tenant id.                         |
+-------------------------+--------------------------------------------------+
| ``responseMessage``     | Specifies the response message that is sent as a |
|                         | response, to the request, for example "CREATED". |
+-------------------------+--------------------------------------------------+
| ``userName``            | Specifies the username that the initiator is     |
|                         | acting on behalf of.                             |
+-------------------------+--------------------------------------------------+
| ``roles``               | Required. Contains a space-separated list of     |
|                         | user roles.                                      |
+-------------------------+--------------------------------------------------+


