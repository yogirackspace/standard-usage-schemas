.. _using:

Using Cloud Feeds
~~~~~~~~~~~~~~~~~~~~~

To use Cloud Feeds you need to have a basic understanding of REST APIs. This section 
describes how to use the Cloud Feeds REST API to perform basic API operations.

.. important:: 
   This section assumes that you are using token-based authentication and
   that you have successfully authenticated against the Rackspace Cloud API
   and obtained an authentication token as described in 
   :ref:`token authentication <token-authentication>`.
     
   Please make sure to record the token because you need it to complete the
   exercises in this chapter. For information on how to make API calls
   using basic authentication, see 
   :ref:`Authenticating by using basic authentication <authenticate-basic>`.
	
     
.. _reading-a-feed: 

Reading a feed
^^^^^^^^^^^^^^^^
To retrieve an Atom feed or an Atom entry that was posted, submit a
**GET** request by using the following syntax:

.. code::  

    curl -H "X-Auth-Token: authenticationToken" -X GET https://endpointURL/feedName/events/tenantID

The placeholders are defined in the following table.

**Syntax elements**

+-------------------------+------------------------------------------------------------------+
| Placeholder             | Description                                                      |
+=========================+==================================================================+
| ``authenticationToken`` | Specifies the token that you obtain during authentication.       |
+-------------------------+------------------------------------------------------------------+
| ``endpointURL``         | Specifies the URL/VIP that was obtained from the list of Cloud   |
|                         | Feeds endpoints.                                                 |
+-------------------------+------------------------------------------------------------------+
| ``feedName``            | Specifies the name of the feeds for which you are trying to      |
|                         | fetch events. Examples of a feed names are: nova, servers,       |
|                         | queues, files.                                                   |
+-------------------------+------------------------------------------------------------------+
| ``tenantID``            | Specifies the tenantID for the given tenant.                     |
+-------------------------+------------------------------------------------------------------+

**Cloud Feeds cURLrequest**

.. code::  

    curl -H "X-Auth-Token: authenticationToken" -X GET https://dfw.feeds.api.rackspacecloud.com/nova/events/tenantID

This request returns 25 of the latest events in the nova feed from the
DFW data center. To make the formatting of the returned events more
readable, you can submit the following request:

.. code::  

    curl -H "X-Auth-Token: authenticationToken" -X GET https://dfw.feeds.api.rackspacecloud.com/nova/events/tenantID | xmllint --format -

..  note:: 
    Control and illegal characters must be escaped in the URL.


.. _navigating-through-feeds:

Navigating through feeds
^^^^^^^^^^^^^^^^^^^^^^^^^^

A successful response to a GET request to obtain a feed returns links that help you navigate
to the Atom entries in the feed. The following links are returned in the Cloud Feeds responses:

- Use the *current* link node to go to the current feed.

- Use the *last* links to go to the last page of Atom entries in the database for the 
  specified feed. Using the last link can be useful for finding the first Atom for 
  a given feed. Note that Cloud Feeds uses a mock last link marker. This helps move 
  some of the heavier queries off the feed head call.

- Use the *self* link to bring back the entry that you are currently viewing. If the 
  feeds are being hit with no options, any new Atom entries have been entered, 
  then those entries also appear.

- Use the *next* link to navigate by page to the next page of Atom entries. When you get
  to the end of the feed, this link is no longer present.

- Use the *previous* link to navigate by page to the previous page of Atom entries. 
  Sometimes the link is in the feed header; however, the link is not be in the feed 
  header if the feed is empty. If you are at the top of the feed and you follow this 
  link, an empty feed is returned unless new entries have occurred since you last 
  polled. In a feed, the entries are shown in order from newest to oldest.


The following diagram shows how pagination works with Cloud Feeds:
    
 .. image:: _images/CloudFeedsAtomHopper.png
        :alt: How Cloud Feeds works with pagination
       
.. important:: 

   When viewed in a web browser or web tool such as Chrome Poster, each **&** character in 
   a link returns as an HTML ampersand character code. To follow the link in a web 
   browser or web tool such as Poster, you must change each HTML ampersand character 
   (&amp;) to **&**. For example, when viewed in a web browser, the feed returns the 
   following link.
     
   .. code:: 
     
       <link href="https://ord.feeds.api.rackspacecloud.com/namespace/feed/events/tenantID/?marker=urn:uuid:676f3860-447c-40a3-8f61-9791819cc82f&amp;limit=25&amp;search=&amp;direction=forward" rel="previous" />

   To follow the link from a web browser, you must change each instance of &amp; to &, as follows:
     
   .. code::
     
     	https://ord.feeds.api.rackspacecloud.com/namespace/feed/events/tenantID/?marker=urn:uuid:676f3860-447c-40a3-8f61-9791819cc82f&limit=25&search=&direction=forward
     	
   This issue should not be a concern when you are using an XML Parser. 

.. _query-params:

Cloud Feeds query parameters
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

You can use query parameters to customize the entries and their order
within a feed. Query parameters are part of the URL that is passed to
the server as part of an API request. When you add query parameters to
an API request, you modify the results in specific ways, such as
refining your query or sorting the output.

A typical URL that contains a query parameter looks like the following
URL:

``http://server/program/path/?query_string``

The query string is composed of one or more field value pairs that use
the following format:

-  Within each field value pair, the field name and value are separated
   by an equals sign (=).

-  Pairs in series are separated by ampersand (&), as shown in the
   following example:

   ``field1=value1&field2=value2&field3=value3...``

The following table summarizes the query parameters you can specify for
Cloud Feeds.

**Query parameters**

+----------------+--------------------------+--------------------------------------+
| Query          | Description              | Acceptable values                    |
| parameter      |                          |                                      |
+================+==========================+======================================+
| ``marker``     | Specifies a UUI that     | Must be a valid UUI that exists in   |
|                | exists in the Cloud      | the Cloud Feeds system, for example  |
|                | Feeds system.            | ``rn:uuid:cd42141b-c030-6fca-6704-82 |
|                |                          | 85789a274b``.                        |
|                |                          | This parameter can also be set to    |
|                |                          | ``last``. If this parameter is set   |
|                |                          | to ``last``, Cloud Feed locates a    |
|                |                          | page that contains the oldest entry  |
|                |                          | in the feed.                         |
+----------------+--------------------------+--------------------------------------+
| ``direction``  | Specifies the direction  | Can be either ``forward`` or         |
|                | from which to return     | ``backward``.                        |
|                | entries, starting from   |                                      |
|                | the current marker or    |                                      |
|                | entry.                   |                                      |
+----------------+--------------------------+--------------------------------------+
| ``limit``      | Specifies the number of  | Must be an integer from 1 to 1000.   |
|                | entries to be returned.  |                                      |
|                | If the entered limit is  |                                      |
|                | greater than the actual  |                                      |
|                | number of entries, the   |                                      |
|                | actual number of entries |                                      |
|                | is used.                 |                                      |
+----------------+--------------------------+--------------------------------------+
| ``search``     | Allows filtering of a    |See `Filtering by categories`_ .      |
|                | a specified category.    |                                      |
+----------------+--------------------------+--------------------------------------+
| ``startingAt`` | Allows filtering for a   | Must be in ISO 8601 Date and Time    |
|                | number of entries that   | format, and must contain a time      |
|                | start at a specified     | zone, for example:                   |
|                | time stamp.              | 2014-03-10T06:00:00.000Z. For more   |
|                |                          | information, see                     |
|                |                          | `ISO 8601 Date and Time format`_.    |
+----------------+--------------------------+--------------------------------------+

..  note:: 
    The ``startingAt`` parameter cannot be used together with the ``marker``
    parameter. If the ``startingAt`` parameter is used without a
    ``direction`` parameter, then forward direction is assumed.
    
.. _ISO 8601 Date and Time format: http://en.wikipedia.org/wiki/ISO_8601 


 

.. _filter-by-marker:

Filtering entries by using the marker parameter
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

You can use the ``marker`` parameter to denote an entry that you have
previously used. If you specify a marker in the **GET** request, you can
also specify a value for the ``direction`` parameter. If you do not
specify a value for the ``direction`` parameter, the default value of
``forward`` is used.

The following example shows a ``marker`` parameter specified and the
``direction`` parameter set to ``forward``:

.. code::  

    https://ord.feeds.api.rackspacecloud.com/feed/events/tenantID/?marker=urn:uuid:8439541b-b40e-4c23-b290-2820bd64257d&direction=forward 

The following example shows a ``marker`` parameter specified and the
``direction`` parameter set to ``backward``:

.. code::  

    https://ord.feeds.api.rackspacecloud.com/feed/events/tenantID/?marker=urn:uuid:8439541b-b40e-4c23-b290-2820bd64257d&direction=backward

You can use the ``limit`` parameter to specify the number of entries to
return. By default the limit is set to 25. The minimum limit is 1 and
the maximum limit is 1,000.

The following example shows the ``marker`` parameter specified and the
``limit`` parameter set to 50, paging forward.

.. code::  

    https://atom.staging.ord1.us.ci.rackspace.net/namespace/feed/events/tenantID/?marker=urn:uuid:8439541b-b40e-4c23-b290-2820bd64257d&direction=forward&limit=50

The following example shows a marker set and the limit set to 50, paging
backward.

.. code::  

    https://ord.feeds.api.rackspacecloud.com/namespace/feed/events/tenantID/?marker=urn:uuid:8439541b-b40e-4c23-b290-2820bd64257d&direction=backward&limit=50


.. _filter-by-categories:

Filtering by categories
^^^^^^^^^^^^^^^^^^^^^^^^

You can use a **GET** request to filter for certain types of events you
want to obtain from a feed by defining a specific search category. You
specify the search categories by adding ``search`` as the URL parameter
at the end of the feeds URL and then specifying the category or item for
which you want to search. The following example shows how to get all
event types that fall under the ``cloudsites.metered.site.usage`` category:

.. code::  

    curl -H "X-Auth-Token: authenticationToken" -X GET https://ord.feeds.api.rackspacecloud.com/sites/events/tenantID/?search=(cat=type:cloudsites.metered.site.usage)


Advanced filtering by using AND, OR, and NOT
..............................................

Cloud Feeds supports advanced filters by using the AND, OR, and NOT
operators and their combinations.

The following example shows how to filter for multiple categories by
using an AND statement:

 
**Filtering for multiple categories by using an AND
statement**

.. code::  

    http://ord.feeds.api.rackspacecloud.com/namespace/feed/tenantID/?search=(AND(cat=CAT1)(cat=CAT2))


The following example shows how to filter for multiple categories by
using an OR statement:

 
**Filtering for multiple categories by using an OR
statement**

.. code::  

    http://ord.feeds.api.rackspacecloud.com/namespace/feed/tenantID/?search=(OR(cat=CAT1)(cat=CAT2))
 

The following example shows how to filter for a category that is entered
as not **CAT1** by using a NOT statement:

 
**Filtering for a single category using a NOT statement**

.. code::  

    http://ord.feeds.api.rackspacecloud.com/namespace/feed/tenantID/?search=(NOT(cat=CAT1)) 


You can also use the AND, OR, and NOT operators to filter for multiple
categories combined.

The following example shows how to search for a category that is entered
as **CAT1** and ( **CAT2** or **CAT3** ) but not **CAT4**:

 
**Filtering for multiple categories using an AND
statement**

.. code::  

    http://ord.feeds.api.rackspacecloud.com/namespace/namespace/feed/tenantID/?search=(AND(cat=CAT1)(OR(cat=CAT2)(cat=CAT3))(NOT(cat=CAT4))) 


.. _filter-by-time-stamp:

Filtering by time stamp
^^^^^^^^^^^^^^^^^^^^^^^^

You can use the ``startingAt`` query parameter to filter for feed
entries that start at a certain time stamp. The parameter takes an ISO
8601 Date and Time format and must contain a timezone, such as such as 2014-03-10T06:00:00.000Z.

The following URL shows how to fetch entries with a time stamp that is
newer than 2014-03-10 00:00:00.000 UTC:

.. code::  

    http://ord.feeds.api.rackspacecloud.com/namespace/feed/tenantID/?startingAt=2014-03-10T00:00:00.000Z

The following URL shows how to fetch entries with a time stamp that
older than 2014-03-10 00:00:00.000 UTC by setting the ``direction``
parameter to ``backward``:

.. code::  

    http://ord.feeds.api.rackspacecloud.com/namespace/feed/tenantID/?startingAt=2014-03-10T00:00:00.000Z&direction=backward

..  note:: 
     The ``startingAt`` parameter can not be used together with the
     ``marker`` parameter.

     If the ``startingAt`` parameter is used without a ``direction``
     parameter, then the ``forward`` direction is assumed. If you want to fetch
     feeds from a time period before the time specified in the time stamp,
     you need to use the ``direction`` parameter and then the ``backward``
     description, like the following: ``direction`` set to ``backward``.
     
.. _ISO 8601 Date and Time format: http://en.wikipedia.org/wiki/ISO_8601       
     
 
.. _support-for-weak-etags:
     
Support for weak ETags
^^^^^^^^^^^^^^^^^^^^^^^

Cloud Feeds supports weak entity tags (ETags). An ETag identifies a
specific feed version. When the content of the feed changes, a different
ETag is assigned. ETags provide an efficient way of checking whether a
previously processed feed has changed. Weak ETags are sent back in the
HTTP header with a name of ETag.

Following is an example of weak ETag for a feed that contains more than
one Atom entry:

.. code::  

    W/"4ec07c96e1399298d48db885c014703b"

ETags are not returned in the following situations:

-  The feed is empty.

-  You use the ``marker`` parameter, specify the direction as
   forward, and no entries exist after that marker.
   

.. _best-practices for consumers:
   
Best practices for consumers
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
.. Comment  In this section, I had to change the formmatting and remove the second example
   in list because sample line was too long and did not render correctly. If it needs to 
   be restored, investigate different formatting structures that can render content 
   correctly.
   
The following list describes a number of best practices consumers can adhere to when 
reading a feed.

**Walk the feed forward**  

When reading a feed you get the best performance if you start from the last entry 
that was successfully read and then walk the feed forward, towards the head of the 
feed. Use the following format, substituting the values for *<endpoint>*, *<feed>*, and 
*<uuid_of_last_read_entry>*.
    
``https://<endpoint>/<feed>?marker=<uuid_of_last_read_entry>&direction=forward&limit=1000``

		     		
**Read in batches of 1000 using the limit query parameter**

The fewer calls to Cloud Feeds, the less processing has to be done. Cloud Feeds 
allows you to read up to 1000 entries at a time using the limit query 
parameter. Use the following format (along with the recommended direction parameter), 
substituting the values for *<endpoint>*, *<feed>*, and 
*<uuid_of_last_read_entry>*.
       
``https://<endpoint>/<feed>?marker=<uuid_of_last_read_entry>&direction=forward&limit=1000`` 
           
**Use compression when issuing requests to remote data centers.** 
  
Having Cloud Feeds compress its message body can decrease response time, especially 
when you interact with an endpoint in a remote data center. By adding the following 
header to your request, you are instructing Cloud Feeds to compress the output. Your 
HTTP client uncompresses the output before you read the message body::
    
``Accept-Encoding: gzip, deflate`` 
    
**Use high-performance categories when using the search query parameter.**

When filtering a feed with the search query parameter, two category types provide 
better performance than others, especially for cases when the searched-for category is 
rare in the feed. 

**Category prefixes**

+-----------+--------------------------------------+----------------------------------------+
| Category  | Description                          | Example                                |
| prefix    |                                      |                                        |
+===========+======================================+========================================+
|           | Specifies the tenant ID, taken       |                                        |
| ``tid:``  | from the ``tenantid`` attribute from | ``tid:12882``                          |
|           | the event node.                      |                                        |
+-----------+--------------------------------------+----------------------------------------+
| ``type:`` | Specifies the event type.            | ``type:cloudsites.metered.site.usage`` |
+-----------+--------------------------------------+----------------------------------------+
            
