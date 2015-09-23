.. _response-codes:

Response codes for Cloud Feeds publishers and subscribers
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The Rackspace Cloud Fee API uses `standard HTTP/1.1 response codes`_.

**Response code for subscribers**

+---------------+------+-----------------------+-----------------------+-----------------+
| Code category | Code | Code description      | Example request       | Example         |
|               |      |                       |                       | response        |
+===============+======+=======================+=======================+=================+
| Successful    | 200  | OK                    | GET request to        | 200 + Atom feed |
|               |      |                       | collection URI        | document        |
+---------------+------+-----------------------+-----------------------+-----------------+
| Successful    | 200  | OK                    | GET request to        | 200 + Member    |
|               |      |                       | Member URI            | Representation  |
+---------------+------+-----------------------+-----------------------+-----------------+
| Client Error  | 401  | None                  | GET request with a    | None            |
|               |      |                       | missing, expired, or  |                 |
|               |      |                       | invalid token         |                 |
+---------------+------+-----------------------+-----------------------+-----------------+
| Client Error  | 404  | None                  | GET request to        | None            |
|               |      |                       | non-existent page     |                 |
+---------------+------+-----------------------+-----------------------+-----------------+
| Client Error  | 405  | None                  | Using any             | None            |
|               |      |                       | method other than     |                 |
|               |      |                       | GET                   |                 |
+---------------+------+-----------------------+-----------------------+-----------------+
| Server Error  | 500  | Internal server error |                       | None            |
+---------------+------+-----------------------+-----------------------+-----------------+
 
**Example: Error message example**

.. code::  

    HTTP/1.1 400 Bad Request
    Content-Type: application/json

    {
      "title": "Unsupported limit",
      "description": "The given limit cannot be negative, and cannot be greater than 50.",
      "code": 1092,
      "link": {
        "rel": "help",
        "href": "http://docs.example.com/messages#limit",
        "text": "API documentation for the limit parameter"
      }
    }

.. _standard HTTP/1.1 response codes: http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html