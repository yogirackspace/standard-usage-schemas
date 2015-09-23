Feeds catalog
~~~~~~~~~~~~~~

You can obtain a list of all the feeds that are available by submitting
a **GET** request against the Cloud Feeds endpoint as shown in the
following examples:

 
**Retrieve feeds catalog request - XML example**

.. code::  

    curl -H "X-Auth-Token: authenticationToken" -X GET https://endpointURL/tenantID/

 
**Retrieve feeds catalog request - JSON example**

.. code::  

    curl –H "X-Auth-Token: authenticationToken" -H "Accept: application/vnd.rackspace.atomsvc+json" -X GET https://endpointURL/tenantID/

You can also use **basic authentication** to retrieve a catalog feed by
using the following syntax:

 
**Retrieve feeds catalog request using basic authentication
- XML example**

.. code::  

    curl -u username:api_key -X GET https://endpointURL/tenantID/

 
**Retrieve feeds catalog request using basic authentication
- JSON example**

.. code::  

    curl -u username:api_key  -H "Accept: application/vnd.rackspace.atomsvc+json" -X GET https://endpointURL/tenantID/

This operation returns a list of supported feeds as shown in the
following examples:

 
**Retrieve feeds catalog response - XML example**

.. code::  

    <?xml version="1.0" encoding="UTF-8"?>
    <service xmlns="http://www.w3.org/2007/app" xmlns:atom="http://www.w3.org/2005/Atom">
        <workspace>
            <atom:title>backup_events_obs</atom:title>
            <collection href="https://ord.feeds.api.rackspacecloud.com/backup/events/8492382">
                <atom:title>backup_events_obs</atom:title>
            </collection>
        </workspace>
        <workspace>
            <atom:title>bigdata_events_obs</atom:title>
            <collection href="https://ord.feeds.api.rackspacecloud.com/bigdata/events/8492382">
                <atom:title>bigdata_events_obs</atom:title>
            </collection>
        </workspace>
        ...
        <workspace>
            <atom:title>ssl_usagesummary_events_obs</atom:title>
            <collection href="https://ord.feeds.api.rackspacecloud.com/usagesummary/ssl/events/8492382">
                <atom:title>ssl_usagesummary_events_obs</atom:title>
            </collection>
        </workspace>
        <!-- Generated from schema version 1.60.1 -->
    </service>

 
**Retrieve feeds catalog response - JSON example**

.. code::  

    {
        "service": {
            "workspace": [
                {
                    "collection": {
                        "href": "https://ord.feeds.api.rackspacecloud.com/backup/events/8492382",
                        "title": "backup_events_obs"
                    },
                    "title": "backup_events_obs"
                },
                {
                    "collection": {
                        "href": "https://ord.feeds.api.rackspacecloud.com/bigdata/events/8492382",
                        "title": "bigdata_events_obs"
                    },
                    "title": "bigdata_events_obs"
                },
                {
                    "collection": {
                        "href": "https://ord.feeds.api.rackspacecloud.com/usagesummary/ssl/events/8492382",
                        "title": "ssl_usagesummary_events_obs"
                    },
                    "title": "ssl_usagesummary_events_obs"
                }
            ]
        }
    }
