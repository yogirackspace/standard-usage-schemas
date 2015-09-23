Pagination
^^^^^^^^^^^^

To reduce the load on the service, list operations return a maximum of
25 entries at a time. However, you can use the ``limit`` parameter in
the **GET** request to allow up to 1,000 entries per page. Specifying
the number of entries to return is referred to as pagination. If a
request supplies no limit or one that exceeds the configured default
limit, the default value is used instead.

Pagination provides the ability to limit the size of the returned data
and retrieve a specified subset of a large data set. Pagination has the
following key concepts: limit and marker.

-  *Limit* is the restriction on the maximum number of items for that
   type that can be returned.

-  *Marker* is the ID of the last item in the previous list returned.
   For example, a query could request the next 10 instances after the
   instance 1234 as follows: ``?limit=10&marker=1234``. Items are
   displayed sorted by ID.

If the content returned by a request is paginated, the response includes
a structured link with the basic structure
``{"href": "<url>", "rel":                         "next"}``. Any
response that is truncated by pagination will have a *next* link, which
points to the next item in the collection. If there are no more items,
no *next* link is returned.
