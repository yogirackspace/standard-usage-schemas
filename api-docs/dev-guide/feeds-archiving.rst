..  _cf-archiving-api:

Cloud Feeds Archiving
~~~~~~~~~~~~~~~~~~~~~~~

Cloud Feeds supports archiving of events. Normally, an event remains in
the database for three days. Archiving enables customers to permanently
store events in a Cloud Files container, and to retrieve them from that
location.

..  note:: 
    To utilize Cloud Feeds archiving, you must have a valid Rackspace Cloud
    Files account. For more information about Rackspace Cloud Files, read
    http://www.rackspace.com/cloud/files/.

Cloud Feeds provides two ways to configure archiving:

-  Configuration via the Cloud Control panel. For details, see `Configure Cloud Feeds Archiving in the
   Cloud Control
   Panel <http://www.rackspace.com/knowledge_center/article/configure-cloud-feeds-archiving-in-the-cloud-control-panel>`__.

-  Configuration via the **Archiving Configuration API**. For more
   information, see :ref:cf-archiving-api-ops`.

Feeds archiving is described in RFC 5005: `Feed Paging and
Archiving <https://tools.ietf.org/html/rfc5005>`__.


Archiving overview
^^^^^^^^^^^^^^^^^^^

Cloud Feeds archives a user's events based on region by storing the data
in the Cloud Files container in a particular region.

Users can elect to have their events stored in one or more of their
Cloud Files containers.

During the archiving process, events are written to files and are
organized by date, feed and region.

Files are formatted as a static feed page for a single day for a single
feed in a region. Users can walk through their archived feeds similarly
to regular feeds by using ``next-archive`` and ``prev-archive`` links
that point to the next and previous days.

Cloud Feeds provides **three** options for routing the archiving data to
be stored:

-  Store all data at a default location, which is specified as one
   default container URL.

-  Store the data dependent on a specified region. For example, an event
   originating from the LON region is stored in a Cloud Files container
   in LON, an event originating from the SYD region is stored in a Cloud
   Files container in SYD, etc. This option requires the user to specify
   an archive container URL for each region. Events can also be
   configured to be stored in any arbitrary region.

-  Store some of the data at the default location, and some of the data
   at a specific, region-based container URL.

You can configure the following settings for Cloud Feeds archiving:

-  Enable or disable archiving.

-  Define the default container for events.

-  Specify specific containers per region. Any events from an
   unspecified region are archived in the default container.

-  Define format to use for archiving the events: An array of XML, JSON,
   or both.

..  note:: 
    The daily feed archive contains the events that were posted during that
    day. The daily feed archive might also list events that contain data
    pertaining to days other than the day when the event was posted.



.. _cf-archiving-api-ops:

Archiving Configuration API operations
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Use the Archiving Configuration API to retrieve and upload archive settings for Cloud 
Feeds. This section describes the elements, service endpoint, and RBAC roles for the 
Archiving Configuration API.  See the Cloud Feeds API operations reference for details.
about the Archive Configuration API operations.  

.. _elements-archiving-config-api: 

Elements of the Archiving Configuration API
...............................................

Cloud Feeds enables users to configure their Cloud Feeds archiving
settings by using the Archiving Configuration API.

**Archiving Configuration API elements**

``enabled`` 
     
     Required. If this value is set to true, archiving is enabled. If set to false, archiving
     is disabled.
     
``data_format``   
   
     Required. Specifies the data format, in which the events are archived. Valid formats are XML 
     and JSON. Users can select both formats. This parameter is an array.
     
``default_archive_container_url``

     Optional. Specifies the container URL that is used for archiving Cloud Feeds events.
     If this value is set, all Cloud Feeds events from all regions will be archived to this 
     container.
     
     If this value is not set, one or more archive container URLs must be specified. This 
     value can be overridden by ``archive_container_URLs``.
     
``archive_container_urls`` 

     Optional. Specifies a list of Cloud Files URLs for each region. Each event is archived to the 
     container that corresponds to its region. You can define container URLs for the following 
     regions: ``iad``, ``ord``, ``dfw``, ``lon``, ``hkg``, ``syd``. Any events from a region 
     that do not have a container URL specified will be archived to the default container 
     URL. 
     
     If this value is not specified, then the default container URL must be specified. If 
     this value is set, you must define at least one region-specific container URL. 


.. _archiving-config-api-service-endpoint:

Archiving Configuration API service endpoint
..............................................

Use the following endpoint to make REST calls against the Archiving Configuration API:

``https://preferences.feeds.api.rackspacecloud.com``


.. _archiving-config-api-rbac-roles:

RBAC Roles for the Archiving Configuration API
.................................................

The RBAC roles for the Archiving Configuration API differ from the RBAC
roles for the Cloud Feeds API. The main difference is in the
cloudfeeds:service-admin role. Users who are assigned the
cloudfeeds:service-admin role cannot issue **GET** or **POST** requests
on **multiple** tenants but only on a **single** tenant.

The following table shows the RBAC role matrix for Cloud Feeds:

**Table RBAC Role Permissions Matrix**

+--------------------------+-------------------------+-------------------------+
| Role                     | GET method              | POST method             |
+==========================+=========================+=========================+
| admin                    | Yes                     | No                      |
+--------------------------+-------------------------+-------------------------+
| identity:user-admin      | Yes                     | No                      |
+--------------------------+-------------------------+-------------------------+
| observer                 | Yes                     | No                      |
+--------------------------+-------------------------+-------------------------+
| cloudfeeds:observer      | Yes                     | No                      |
+--------------------------+-------------------------+-------------------------+
| cloudfeeds:service-admin | Yes on single tenants;  | Yes on single tenants;  |
|                          | No on multiple tenants  | No on multiple tenants  |
+--------------------------+-------------------------+-------------------------+
| any other roles          | No                      | No                      |
+--------------------------+-------------------------+-------------------------+



Configuring Cloud Feeds archiving settings
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

To configure the Cloud Feeds archiving settings, you need to do the
following:

-  Make a **POST** request against the
   ``https://preferences.feeds.api.rackspacecloud.com/archive/<tenantid``>
   endpoint and provide the configuration information in the request
   body.

-  Be sure to set the ``enabled`` parameter to "true," and the
   ``data_format`` parameter to and array of values (JSON, XML, or
   both).


.. Important:: 
    The Archiving Configuration API only supports token-based
    authentication. It does not support basic authentication.

If you want to specify **one single container URL** to store all events,
regardless of which region they originate from, set the
``default_container_URL`` parameter to a valid URL from your Cloud Files
account, as shown in the following example:

.. code::  

    curl -X POST -H "Content-Type: application/json" -H "X-Auth-Token: my_auth_token" https://preferences.feeds.api.rackspacecloud.com/archive/147587 -i -d '{
      "data_format": [
        "JSON"
      ],
      "default_archive_container_url": "https://storage.stg.swift.racklabs.com/v1/StagingUS_6b881249-b992-44ef-9ad1-2b9f5107d2f9/FeedsArchives",
      "enabled": true
    };'

If you want to specify a **specific container URL** for each region, so
that Cloud Feeds routes all the events to be archived to a container
that corresponds with the region of the event, use the
``archive_container_urls`` parameter. For each region, point to a valid
URL from your Cloud Files account that you want the events to be routed
to, as shown in the following example:

.. code::  

     curl -X POST -H "Content-Type: application/json" -H "X-Auth-Token: my_auth_token" https://preferences.feeds.api.rackspacecloud.com/archive/147587 -i -d '{
      "data_format": [
        "JSON", "XML"
      ],
      "archive_container_urls": {
        "iad": "https://storage.stg.swift.racklabs.com/v1/StagingUS_6b881249-b992-44ef-9ad1-2b9f5107d2f9/USArchives",
        "dfw": "https://storage.stg.swift.racklabs.com/v1/StagingUS_6b881249-b992-44ef-9ad1-2b9f5107d2f9/USArchives",
        "ord": "https://storage.stg.swift.racklabs.com/v1/StagingUS_6b881249-b992-44ef-9ad1-2b9f5107d2f9/USArchives",
        "lon": "https://storage.stg.swift.racklabs.com/v1/StagingUS_6b881249-b992-44ef-9ad1-2b9f5107d2f9/UKArchives",
        "syd": "https://storage.stg.swift.racklabs.com/v1/StagingUS_6b881249-b992-44ef-9ad1-2b9f5107d2f9/APACArchives",
        "hkg": "https://storage.stg.swift.racklabs.com/v1/StagingUS_6b881249-b992-44ef-9ad1-2b9f5107d2f9/APACArchives   
      },  
      "enabled": true
    };'

Cloud Files also provides the option to specify a default container URL
and one or more archive container URLs. In this configuration, all feeds
that are configured for a region-specific container URL are routed to
that URL. All other feeds are routed to the default container URL. The
following example shows a configuration that routes the feeds from
``lon``, ``syd``, and ``hkg`` to a region-specific URL. All other feeds
are routed to the default container URL.

.. code::  

     curl -X POST -H "Content-Type: application/json" -H "X-Auth-Token: ****" https://preferences.feeds.api.rackspacecloud.com/archive/147587 -i -d '{
      "data_format": [
        "JSON", "XML"
      ],
      "default_archive_container_url": "https://storage.stg.swift.racklabs.com/v1/StagingUS_6b881249-b992-44ef-9ad1-2b9f5107d2f9/FeedsArchives",
      "archive_container_urls": {
        "lon": "https://storage.stg.swift.racklabs.com/v1/StagingUS_6b881249-b992-44ef-9ad1-2b9f5107d2f9/UKArchives",
        "syd": "https://storage.stg.swift.racklabs.com/v1/StagingUS_6b881249-b992-44ef-9ad1-2b9f5107d2f9/APACArchives",
        "hkg": "https://storage.stg.swift.racklabs.com/v1/StagingUS_6b881249-b992-44ef-9ad1-2b9f5107d2f9/APACArchives   
      },  
      "enabled": true
    };'


Working with archived feeds
^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Review the following sections to learn about managing archived feeds:

-  :ref:`cf-download-archived-feeds`

-  :ref:`cf-navigating-archived-feeds`

-  :ref:`cf-rbac-roles-archive-config`

-  :ref:`cf-format-archived-feeds`


.. _cf-download-archived-feeds:

Downloading archived feeds
............................

You can access your archived feeds by submitting a **GET** request
against the external feeds endpoint. The **GET** request downloads your
archived feeds directly from Cloud Files.

Cloud Feeds provides the following endpoint for accessing external
feeds:

``https://external.feeds.endpoint/``.

To submit a **GET** request, use the following syntax:

.. code::  

    https://external.feeds.endpoint/archive/<nastId>/<container>/<region>_feed-events_YYYY-MM-DD.[xml|json]

.. Important:: 
    The archive resource in the request URI denotes that the request is
    targeted to retrieve an archived feed. The request must be made using
    the exact format that is shown above.

    A successful **GET** request returns links that help you navigate the
    Atom entries in the feed.

    The following is an example of a request to retrieve an archived feed in
    XML format.

.. code::  

    curl -u cfeedstestadminrole:apikey http://external.feeds.endpoint/archive/StagingUS_cab08997-1c5d-4545-815a-186592907ef9/FeedsArchives/dfw_backup-events_2015-01-27.xml

    The following is an example of a request to retrieve an archived feed in
    JSON format.

.. code::  

    curl -i -u cfeedstestadminrole:apikey -H "accept: application/json" http://external.feeds.endpoint/archive/StagingUS_cab08997-1c5d-4545-815a-186592907ef9/FeedsArchives/dfw_backup-events_2015-01-27.json 

A successful **GET** request returns a 200: OK, success code and a link
to the archived feeds.

If the request is unsuccessful, one of the following error codes is
returned.

**Table: Error codes**

+--------------------+-------------------------------------------------------+
| Error code         | Description                                           |
+====================+=======================================================+
| 401                | Authentication error, the credentials provided are    |
|                    | invalid.                                              |
+--------------------+-------------------------------------------------------+
| 404                | The archived file does not exist.                     |
+--------------------+-------------------------------------------------------+
| 405                | Invalid method and/or invalid container/filenname.    |
|                    | For example a **POST** operation was used instead of  |
|                    | a **GET** operation.                                  |
+--------------------+-------------------------------------------------------+


.. _cf-navigating-archived-feeds:

Navigating archived feeds
...........................

You can navigate archived feeds in similar way to live feeds by using
the **prev-archive** and **next-archive**, and **current** links:

-  Use the **next-archive** link to navigate by page to the next page of
   Atom entries.

-  Use the **prev-archive** link to navigate by page to the previous
   page of Atom entries.

-  Use the **current** link to navigate to the actual feed.

The **self**, **next-archive**, and **prev-archive** links point to
static files, which may or may not exist in the user's Cloud Files
account.

Accessing archived feeds provides limited functionality compared to
working with live feeds:

-  No parameters are enabled. This includes the: ``marker``, ``limit``,
   ``direction``, ``search``, ``startingAt`` parameters.

For general information on how to navigate feeds, see :ref:`navigating-through-feeds`.


.. _cf-rbac-roles-archive-config: 

RBAC Roles for the Archiving Configuration API
................................................

The RBAC roles for the Archiving Configuration API differ from the RBAC
roles for the Cloud Feeds API. The main difference is in the
cloudfeeds:service-admin role. Users who are assigned the
cloudfeeds:service-admin role cannot issue **GET** or **POST** requests
on **multiple** tenants but only on a **single** tenant.

The following table shows the RBAC role matrix for Cloud Feeds:

**Table: Cloud Files product roles and permissions**

+-----------------------+-------------------------------------------------------------+
| Role                  | Description                                                 |
+=======================+=============================================================+
| object-store:admin    | This role provides Create, Read, Update, and Delete         |
|                       | permissions in Cloud Files where access is granted.         |
+-----------------------+-------------------------------------------------------------+
| object-store:observer | This role provides Read permission in Cloud Files,          |
|                       | where access is granted.                                    |
+-----------------------+-------------------------------------------------------------+

Additionally, two multi-product roles apply to all products. Users with multi-product roles 
inherit access to future products when those products become RBAC-enabled. The following 
table describes these roles and their permissions.

**Table: Multiproduct roles and permissions**

+-----------------------+-------------------------------------------------------------+
| Role                  | Description                                                 |
+=======================+=============================================================+
| admin                 | This role provides Create, Read, Update, and Delete         |
|                       | permissions in Cloud Files where access is granted.         |
+-----------------------+-------------------------------------------------------------+
| observer              | This role provides Read permission in all products,         |
|                       | where access is granted.                                    |
+-----------------------+-------------------------------------------------------------+

For more information about Cloud Files roles and permissions, see the *Cloud Files 
Developer Guide*.


.. _cf-format-archived-feeds:

Format of archived feeds
.........................

Archived feeds use the same formatting as regular feeds.

In addition, archived feeds have an additional node that denotes the
feed as an archive. The ``archive`` node is a sub-node of the ``feed``
node that is located at the top of each feed. It uses the following
format:

**Table: Archive node in archived feeds**

+----------------------------+-----------------------------------------------+
| Type                       | Format                                        |
+============================+===============================================+
| XML                        | ``<fh:archive/>,``                            |
+----------------------------+-----------------------------------------------+
| JSON                       | ``"archive": "",``                            |
+----------------------------+-----------------------------------------------+


The following example shows an archived XML feed.

**Example: Archived feed example - XML**

.. code::  

    <?xml version="1.0" encoding="UTF-8"?>
    <feed xmlns="http://www.w3.org/2005/Atom" xmlns:fh="http://purl.org/syndication/history/1.0">
      <fh:archive/>
      <link rel="current" href="https://ord.feeds.api.rackspacecloud.com/feed_1/events/5821027"/>
      <link rel="self" href="https://storage.stg.swift.racklabs.com/v1/StagingUS_cab08997-1c5d-4545-815a-186592907ef9/container_1/ord_feed_1-events_2015-01-27.xml"/>
      <id>urn:uuid0803e933-0011-464a-8ea2-5187b8ec4487</id>
      <title type="text">feed_1/events</title>
      <link rel="prev-archive" href="https://storage.stg.swift.racklabs.com/v1/StagingUS_cab08997-1c5d-4545-815a-186592907ef9/container_1/ord_feed_1-events_2015-01-28.xml"/>
      <link rel="next-archive" href="https://storage.stg.swift.racklabs.com/v1/StagingUS_cab08997-1c5d-4545-815a-186592907ef9/container_1/ord_feed_1-events_2015-01-26.xml"/>
      <updated>2015-01-28T15:50:48.497Z</updated>
      <atom:entry xmlns:atom="http://www.w3.org/2005/Atom" xmlns="http://wadl.dev.java.net/2009/02" xmlns:db="http://docbook.org/ns/docbook" xmlns:error="http://docs.rackspace.com/core/error" xmlns:wadl="http://wadl.dev.java.net/2009/02" xmlns:json="http://json-schema.org/schema#" xmlns:saxon="http://saxon.sf.net/" xmlns:sum="http://docs.rackspace.com/core/usage/schema/summary" xmlns:d558e1="http://wadl.dev.java.net/2009/02" xmlns:cldfeeds="http://docs.rackspace.com/api/cloudfeeds" index="7">
        <atom:id>urn:uuid:59085a27-f9ac-44f7-a74b-0d41fe3c4585</atom:id>
        <atom:category term="tid:5821027"/>
        <atom:category term="rgn:ORD"/>
        <atom:category term="dc:ORD1"/>
        <atom:category term="rid:ed3f75f5-bd98-4c62-b670-46c7d15ea601"/>
        <atom:category term="widget.widget.gadget.usage"/>
        <atom:category term="type:widget.widget.gadget.usage"/>
        <atom:content type="application/xml">
          <event xmlns="http://docs.rackspace.com/core/event" xmlns:widget="http://docs.rackspace.com/usage/widget" dataCenter="ORD1" endTime="2015-01-25T15:51:11Z" environment="PROD" id="59085a27-f9ac-44f7-a74b-0d41fe3c4585" region="ORD" resourceId="ed3f75f5-bd98-4c62-b670-46c7d15ea601" startTime="2012-03-12T11:51:11Z" tenantId="5821027" type="USAGE" version="1">
            <widget:product version="3" serviceCode="Widget" resourceType="WIDGET" label="test" widgetOnlyAttribute="bar" privateAttribute1="something you can not see" myAttribute="here it should be private" privateAttribute3="W2" mid="e9a67860-52e6-11e3-a0d1-002500a28a7a">
              <widget:metaData key="foo" value="bar"/>
              <widget:mixPublicPrivateAttributes privateAttribute3="45" myAttribute="here it should be public"/>
            </widget:product>
          </event>
        </atom:content>
        <updated>2015-01-27T15:16:12.247Z</updated>
        <published>2015-01-27T20:59:53.836Z</published>
      </atom:entry>
      <atom:entry xmlns:atom="http://www.w3.org/2005/Atom" xmlns="http://wadl.dev.java.net/2009/02" xmlns:db="http://docbook.org/ns/docbook" xmlns:error="http://docs.rackspace.com/core/error" xmlns:wadl="http://wadl.dev.java.net/2009/02" xmlns:json="http://json-schema.org/schema#" xmlns:saxon="http://saxon.sf.net/" xmlns:sum="http://docs.rackspace.com/core/usage/schema/summary" xmlns:d558e1="http://wadl.dev.java.net/2009/02" xmlns:cldfeeds="http://docs.rackspace.com/api/cloudfeeds" index="6">
        <atom:id>urn:uuid:59085a27-f9ac-44f7-a74b-0d41fe3c4585</atom:id>
        <atom:category term="tid:5821027"/>
        <atom:category term="rgn:ORD"/>
        <atom:category term="dc:ORD1"/>
        <atom:category term="rid:ed3f75f5-bd98-4c62-b670-46c7d15ea601"/>
        <atom:category term="widget.widget.gadget.usage"/>
        <atom:category term="type:widget.widget.gadget.usage"/>
        <atom:content type="application/xml">
          <event xmlns="http://docs.rackspace.com/core/event" xmlns:widget="http://docs.rackspace.com/usage/widget" dataCenter="ORD1" endTime="2015-01-25T15:51:11Z" environment="PROD" id="59085a27-f9ac-44f7-a74b-0d41fe3c4585" region="ORD" resourceId="ed3f75f5-bd98-4c62-b670-46c7d15ea601" startTime="2012-03-12T11:51:11Z" tenantId="5821027" type="USAGE" version="1">
            <widget:product version="3" serviceCode="Widget" resourceType="WIDGET" label="test" widgetOnlyAttribute="bar" privateAttribute1="something you can not see" myAttribute="here it should be private" privateAttribute3="W2" mid="e9a67860-52e6-11e3-a0d1-002500a28a7a">
              <widget:metaData key="foo" value="bar"/>
              <widget:mixPublicPrivateAttributes privateAttribute3="45" myAttribute="here it should be public"/>
            </widget:product>
          </event>
        </atom:content>
        <updated>2015-01-27T15:16:12.247Z</updated>
        <published>2015-01-27T20:59:53.836Z</published>
      </atom:entry>
    </feed>

The following example shows an archived JSON feed.

 
**Example: Archived feed example - JSON**

.. code::  

    {
        "feed": {
            "@type": "http://www.w3.org/2005/Atom",
            "archive": "",
            "entry": [
                {
                    "category": [
                        {
                            "term": "tid:5821027"
                        },
                        {
                            "term": "rgn:ORD"
                        },
                        {
                            "term": "dc:ORD1"
                        },
                        {
                            "term": "rid:ed3f75f5-bd98-4c62-b670-46c7d15ea601"
                        },
                        {
                            "term": "widget.widget.gadget.usage"
                        },
                        {
                            "term": "type:widget.widget.gadget.usage"
                        }
                    ],
                    "content": {
                        "event": {
                            "@type": "http://docs.rackspace.com/core/event",
                            "dataCenter": "ORD1",
                            "endTime": "2015-01-25T15:51:11Z",
                            "environment": "PROD",
                            "id": "59085a27-f9ac-44f7-a74b-0d41fe3c4585",
                            "product": {
                                "@type": "http://docs.rackspace.com/usage/widget",
                                "label": "test",
                                "metaData": {
                                    "key": "foo",
                                    "value": "bar"
                                },
                                "mid": "e9a67860-52e6-11e3-a0d1-002500a28a7a",
                                "mixPublicPrivateAttributes": {
                                    "myAttribute": "here it should be public",
                                    "privateAttribute3": "45"
                                },
                                "myAttribute": "here it should be private",
                                "privateAttribute1": "something you can not see",
                                "privateAttribute3": "W2",
                                "resourceType": "WIDGET",
                                "serviceCode": "Widget",
                                "version": "3",
                                "widgetOnlyAttribute": "bar"
                            },
                            "region": "ORD",
                            "resourceId": "ed3f75f5-bd98-4c62-b670-46c7d15ea601",
                            "startTime": "2012-03-12T11:51:11Z",
                            "tenantId": "5821027",
                            "type": "USAGE",
                            "version": "1"
                        }
                    },
                    "id": "urn:uuid:59085a27-f9ac-44f7-a74b-0d41fe3c4585",
                    "published": "2015-01-25T20:59:53.836Z",
                    "updated": "2015-01-27T15:16:12.247Z"
                },
                {
                    "category": [
                        {
                            "term": "tid:5821027"
                        },
                        {
                            "term": "rgn:ORD"
                        },
                        {
                            "term": "dc:ORD1"
                        },
                        {
                            "term": "rid:ed3f75f5-bd98-4c62-b670-46c7d15ea601"
                        },
                        {
                            "term": "widget.widget.gadget.usage"
                        },
                        {
                            "term": "type:widget.widget.gadget.usage"
                        }
                    ],
                    "content": {
                        "event": {
                            "@type": "http://docs.rackspace.com/core/event",
                            "dataCenter": "ORD1",
                            "endTime": "2015-01-25T15:51:11Z",
                            "environment": "PROD",
                            "id": "59085a27-f9ac-44f7-a74b-0d41fe3c4585",
                            "product": {
                                "@type": "http://docs.rackspace.com/usage/widget",
                                "label": "test",
                                "metaData": {
                                    "key": "foo",
                                    "value": "bar"
                                },
                                "mid": "e9a67860-52e6-11e3-a0d1-002500a28a7a",
                                "mixPublicPrivateAttributes": {
                                    "myAttribute": "here it should be public",
                                    "privateAttribute3": "45"
                                },
                                "myAttribute": "here it should be private",
                                "privateAttribute1": "something you can not see",
                                "privateAttribute3": "W2",
                                "resourceType": "WIDGET",
                                "serviceCode": "Widget",
                                "version": "3",
                                "widgetOnlyAttribute": "bar"
                            },
                            "region": "ORD",
                            "resourceId": "ed3f75f5-bd98-4c62-b670-46c7d15ea601",
                            "startTime": "2012-03-12T11:51:11Z",
                            "tenantId": "5821027",
                            "type": "USAGE",
                            "version": "1"
                        }
                    },
                    "id": "urn:uuid:59085a27-f9ac-44f7-a74b-0d41fe3c4585",
                    "published": "2015-01-25T20:59:53.836Z",
                    "updated": "2015-01-27T15:16:12.247Z"
                },
            "id": "urn:uuid0803e933-0011-464a-8ea2-5187b8ec4487",
            "link": [
                {
                    "href": "https://ord.feeds.api.rackspacecloud.com/feed_1/events/5821027",
                    "rel": "current"
                },
                {
                    "href": "https://storage.stg.swift.racklabs.com/v1/StagingUS_cab08997-1c5d-4545-815a-186592907ef9/container_1/ord_feed_1-events_2015-01-27.json",
                    "rel": "self"
                },
                {
                    "href": "https://storage.stg.swift.racklabs.com/v1/StagingUS_cab08997-1c5d-4545-815a-186592907ef9/container_1/ord_feed_1-events_2015-01-28.json",
                    "rel": "prev-archive"
                },
                {
                    "href": "https://storage.stg.swift.racklabs.com/v1/StagingUS_cab08997-1c5d-4545-815a-186592907ef9/container_1/ord_feed_1-events_2015-01-26.json",
                    "rel": "next-archive"
                }
            ],
            "title": {
                "@text": "feed_1/events",
                "type": "text"
            },
            "updated": "2015-01-28T15:50:48.497Z"
        }
    }
