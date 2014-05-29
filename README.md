__Project Summary__

All validation to Cloud Feeds (or Feed Service) is done by Repose. A WADL is fed as input that provides important criteria that are relevant to the [Cloud Feeds validation and transformations](https://github.com/rackerlabs/standard-usage-schemas/wiki/Overview).
It is known that managing and maintaining the WADL and its underlying artifacts (XSDs, XSLs) manually could be cumbersome, and the process would be error prone. Instead, we describe the individual rules separately using a simplified format called a Product Schema. A product schema describes a message (or series of related messages) sent by a product to Atom Hopper. Product schemas are stored in the [sample_product_schemas](https://github.com/rackerlabs/standard-usage-schemas/tree/master/sample_product_schemas) directory.

This project takes a look at all of the product schemas in the product schemas directory and generates the redundant parts of the Atom Hopper WADL (sub WADL, XSDs, and XSLTs). Importantly, there is clear scope for automation as some parts are still manual in the process, and hence the goal is to eventually generate the entire WADL from the product schemas.


__Schemas__

In a typical Atom Hopper Usage Entry, there are three separate schema definitions contained within a single entry – product schema, core event schema, and entry schema. There is a well-defined provision to create multiple versions of the schema in different files or in the same file, which are illustrated under Schema Versioning](https://github.com/rackerlabs/standard-usage-schemas/wiki/Versioning). Also, rules for the display of various attributes for the user’s usage summary message could be found at [Usage Summary Messages](https://github.com/rackerlabs/standard-usage-schemas/wiki/Usage-Summary-Messages). This is helpful in distinguishing the billable attributes from non-billable ones and thereby invoking corresponding actions.

__Developers Guide__

[Developer Set-Up Guide](https://github.com/rackerlabs/standard-usage-schemas/wiki/Developer-Set-Up-Guide) provides step-by-step process to setup project before onboarding a new product schema so that Atom Hopper can deploy them. That includes processes starting from setting up git repository till submitting any changes in github for the review. [Writing Tests]( https://github.com/rackerlabs/standard-usage-schemas/wiki/Tests) lists the types of tests, which are relevant to the project. These are important from the perspective to understand the project well as they ensure the coverage of various complex test scenarios.

