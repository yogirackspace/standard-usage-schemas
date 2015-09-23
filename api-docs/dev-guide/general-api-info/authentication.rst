.. _authentication:

Authentication
---------------

Cloud Feeds provides two methods to authenticate users who want to use
the Cloud Feeds API:

-  Authentication by using an authentication token. For more
   information, see :ref:`Authenticating by using token-based
   authentication <token-authentication>`.

-  Basic Authentication by using a username and API key. For more
   information, see 
   :ref:`Authenticating by using basic authentication <authenticate-basic>`.

..  note:: 
    The examples in this guide on how to use the Cloud Feeds API use
    token-based authentication.
    

.. _rbac-permissions:

RBAC permissions cross-reference to Cloud Feeds API operations
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Role Based Access Control (RBAC) restricts access to the capabilities of
Rackspace Cloud services, including the Cloud Feeds API, to authorized
users only. RBAC enables Rackspace Cloud customers to specify which
account users of their Cloud account have access to which Cloud Feeds
API service capabilities, based on roles defined by Rackspace.

The following table shows the RBAC role matrix for Cloud Feeds:

**RBAC Role Matrix**

+----------------------------+------------+-------------+
| Role                       | GET method | POST method |
+============================+============+=============+
| *admin*                    | Yes        | No          |
+----------------------------+------------+-------------+
| *identity:user-admin*      | Yes        | No          |
+----------------------------+------------+-------------+
| *observer*                 | Yes        | No          |
+----------------------------+------------+-------------+
| *cloudfeeds:observer*      | Yes        | No          |
+----------------------------+------------+-------------+
| *cloudfeeds:service-admin* | Yes        | Yes         |
+----------------------------+------------+-------------+
| any other roles            | No         | No          |
+----------------------------+------------+-------------+

For more information about RBAC, read the `Detailed Permissions Matrix
for Cloud Feeds`_ and `Permission Matrix for Role-Based Access Control`_
articles on the Rackspace Knowledge Center.


.. _Detailed Permissions Matrix for Cloud Feeds: http://www.rackspace.com/knowledge_center/article/detailed-permissions-matrix-for-cloud-feeds
.. _Permission Matrix for Role-Based Access 
    Control: http://www.rackspace.com/knowledge_center/article/permissions-matrix-for-role-based-access-control-rbac


.. _authorization-roles-required:

Authorization roles required for Cloud Feeds access
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

To read Cloud Feeds data for a single tenant (GET operations), a user must be assigned the 
``cloudfeeds:observer`` RBAC role.    
    

.. _rate-limits-per-role:

Rate limits
^^^^^^^^^^^^

Customers with the ``cloudfeeds:observer`` role can perform 10 **GET**
requests per minutes on all feeds they are authorized for.


.. _token-authentication:

Authenticating by using token-based authentication
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

To make calls against the Cloud Feeds API by using an authentication
token, you must first generate an authentication token. You provide this
token in the X-Auth-Token header in each Cloud Feeds API request.

The examples below demonstrate how to use cURL to obtain the
authentication token and your account number. You must provide both when
making subsequent Cloud Feeds API requests by using token-based authentication.

Remember to replace the placeholder names in the following
authentication request examples with your information:

-  *yourUserName* — Your common Rackspace Cloud username, as supplied
   during registration.

-  *yourApiKey* — Your API access key.

   You can obtain the key from the Rackspace `Cloud Control Panel`_ by selecting
   **Account Settings** from the yourAccount menu in the top-right
   corner of the window.

Use the following endpoint to access the Authentication Service:

-  ``https://identity.api.rackspacecloud.com/v2.0/``

Notice that you authenticate by using a special URL for the Cloud
authentication service. For example, you may use
``https://identity.api.rackspacecloud.com/v2.0/tokens`` as shown in the
following Authenticate Request examples. Note that the ``v2.0``
component in the URL indicates that you are using version 2.0 of the
Cloud Authentication API.

 
**Example 2.1. cURL authenticate request: XML**

.. code::  

    curl -i -d \
    '<?xml version="1.0" encoding="UTF-8"?>
    <auth>
       <apiKeyCredentials
          xmlns="http://docs.rackspace.com/identity/api/ext/RAX-KSKEY/v1.0"
             username="yourUserName"
             apiKey="yourApiKey"/>
    </auth>' \
    -H 'Content-Type: application/xml' \
    -H 'Accept: application/xml' \
    'https://identity.api.rackspacecloud.com/v2.0/tokens'

 
**Example 2.2. cURL authenticate request: JSON**

.. code::  

    curl -s https://identity.api.rackspacecloud.com/v2.0/tokens -X 'POST' \
         -d '{"auth":{"RAX-KSKEY:apiKeyCredentials":{"username":"yourUserName",
            "apiKey":"yourApiKey"}}}' \ 
         -H "Content-Type:
            application/json" 

The authentication token ``id`` is returned along with an ``expires``
attribute that specifies when the token expires.

.. note::
   - If the authentication response returns a 401 response with a request
     for additional credentials, your account requires multi-factor
     authentication. To complete the authentication process, submit a
     second ``POST`` token request with these multi-factor authentication
     credentials:

     - The session ID value returned in the
       ``WWW-Authenticate: OS-MF sessionId`` header parameter included in
       the response to the initial authentication request.

     - The passcode from the mobile phone associated with your user
       account.

       
       **Authentication request with multi-factor
       authentication credentials**

       .. code::  

          $curl https://identity.api.rackspacecloud.com/v2.0/tokens \
                 -X POST \
                 -d '{"auth": {"RAX-AUTH:passcodeCredentials": {"passcode":"1411594"}}}'\
                 -H "X-SessionId: $SESSION_ID" \
                 -H "Content-Type: application/json" --verbose | python -m json.tool


     For more information, see `Multi-factor authentication`_
     in the "Rackspace Cloud Identity Client Developer Guide".

   - The token, user, and service catalog information that you receive in
     your responses vary from the examples shown in this document because
     they are specific to your account.

   - The ``expires`` attribute denotes the time after which the token will
     automatically expire. A token may be manually revoked before the time
     identified by the expires attribute; ``expires`` predicts a token's
     maximum possible lifespan but does not guarantee that it will reach
     that lifespan. Clients are encouraged to cache a token until it
     expires.

   - Applications should be designed to re-authenticate after receiving a
     401 (Unauthorized) response from a service endpoint.

   - For more detailed authentication instructions and examples, see 
     `Quick Start`_ in the "Identity Client Developer Guide".

The actual account number is located after the final slash (/) in the
``publicURL`` field. You must specify your account number on most of the
Cloud Feeds API operations, wherever you see the placeholder
*``tenantID``* specified in the examples in this guide. A successful
authentication request returns the authentication token, as well as the
Identity Service catalog in the response.


.. _Cloud Control Panel: http://mycloud.rackspace.com/
.. _Quick Start: http://docs.rackspace.com/auth/api/v2.0/auth-client-devguide/content/QuickStart-000.html
.. _Multi-factor 
    authentication: http://docs.rackspace.com/auth/api/v2.0/auth-client-devguide/content/MFA_Ops.html
.. _cf-basic-authentication:


.. _authenticate-basic:

Authenticating by using basic authentication
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

In addition to token-based authentication Cloud Feeds also supports
basic authentication by using your Rackspace cloud account username and
API key.

.. Important:: 
   Basic authentcation cannot be used for :ref:`cf-archiving-api-ops`.

To make a request to the Cloud Feeds API with basic authentication, you
need to issue a cURL call directly against the requested end point by
providing the username and API key directly in the call as shown here:

.. code::  

    curl -u <username:api-key> -X <method> https://endpointURL/

The following example shows how to retrieve the feeds catalog by using
basic authentication:

.. code::  

    curl -u username:user_api_key –X GET https://atom.test.ord1.us.ci.rackspace.net/ 

The following example show how to retrieve an event by using basic
authentication:

.. code::  

    curl -u username:user_api_key –X GET https://atom.test.ord1.us.ci.rackspace.net/usagesummary/bigdata/events
