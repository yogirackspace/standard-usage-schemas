.. _service-access-endpoints

Service access/endpoints
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The Rackspace Cloud Feeds service is a regionalized service. 
The user can choose the Cloud Feeds endpoint in a specific region to coordinate 
the appropriate replication, caching, and overall
maintenance of Cloud Feeds data across regional boundaries to other
Cloud Feeds servers.

..  note::
 
    - For more information about regions, see the `About regions`_ article in the Rackspace Knowledge Center.
    
    - The Identity Service catalog returned in response to a successful authentication 
      request includes a link to the Cloud Feeds service catalog. The Cloud Feeds service 
      catalog lists all available feeds.

**Regionalized service endpoints**

+------------------------+------------------------------------------------+
| Region                 | Endpoint                                       |
+========================+================================================+
| Chicago (ORD)          |  ``https://ord.feeds.api.rackspacecloud.com/`` |
+------------------------+------------------------------------------------+
| Dallas/Fort Worth (DFW)|  ``https://dfw.feeds.api.rackspacecloud.com/`` |
+------------------------+------------------------------------------------+
| Northern Virginia      |  ``https://iad.feeds.api.rackspacecloud.com/`` |
+------------------------+------------------------------------------------+
| London (LON)           |  ``https://lon.feeds.api.rackspacecloud.com/`` |
+------------------------+------------------------------------------------+
| Sydney (SYD)           |  ``https://syd.feeds.api.rackspacecloud.com/`` |
+------------------------+------------------------------------------------+
| Hong Kong (HKG)        |  ``https://hkg.feeds.api.rackspacecloud.com/`` |
+------------------------+------------------------------------------------+

..  note:: 

    -  Choose the endpoint for the data center where your cloud resources
       are located.

    -  The cloud server that you use must be located in the same data center
       where your database resides.

    -  All examples in this guide assume that you are operating against the
       DFW data center. If you are using a different datacenter, be sure to
       use the associated endpoint from the table instead.

    -  The endpoints provided in this sections are the base URL for
       accessing Cloud Feeds. To access actual feeds, you need to provide
       additional information.

.. _About regions: http://www.rackspace.com/knowledge_center/article/about-regions