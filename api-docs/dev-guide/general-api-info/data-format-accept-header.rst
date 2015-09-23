.. _-data-format-accept-header:
 
Data format in Accept header
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Cloud Feeds supports both XML and JSON formats. You can specify the
format in which you want your data to be returned in by configuring the
data format the ``Accept:`` header in the API request.

The following table shows the configuration settings for JSON and XML:

**Data format in Accept header**

+--------------------+--------------------------------------------------------+
| Data Format        | Header                                                 |
+====================+========================================================+
| JSON               | ``Accept: application/vnd.rackspace.atom+json``        |
+--------------------+--------------------------------------------------------+
| XML                | ``Accept: application/atom+xml``                       |
+--------------------+--------------------------------------------------------+
