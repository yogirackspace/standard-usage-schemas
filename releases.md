# Changelog for Usage Schema


**usage-schema-1.46.0**  
2014-04-14 | B-70224 Schema update: CBU - remove version 2 (Paul Benoit)  

**usage-schema-1.45.0**  
**usage-schema-1.44.1**  
2014-04-03 | B-70394 - update UOM (Joe Savak)  
2014-04-02 | B-59857: add synthesize attributes: avgConcurrentConnectionsSum, hasSSLConnection, publicBandWidthInSum, publicBandWidthOutSum (Shinta Smith)  

**usage-schema-1.44.0**  
2014-04-01 | B-66289: rename /emailapps feed to /emailapps_usage (Shinta Smith)  
2014-03-27 | D-17584 fixing regex for Username type (Greg Sharek)  
2014-03-27 | B-66030: add new /emailapps/msservice feed and schema (Shinta Smith)  

**usage-schema-1.43.1**  

**usage-schema-1.43.0**  
2014-03-24 | B-65932: add more enum values for sites subscription action (Shinta Smith)  
2014-03-20 | B-66236: /dedicatedvcloud schema changes (Shinta Smith)  
2014-03-19 | B-66235: update to /dedicatedvcloud schema per BenT (Shinta Smith)  
2014-03-12 | B-65932 - add revert_cancel (Joe Savak)  

**usage-schema-1.41.0**  
2014-03-07 | B-65764: update the entry.xml (Shinta Smith)  
2014-03-07 | B-65764: update nova assert for applicationLicense MSSQL_WEB and MSSQL (Shinta Smith)  
2014-03-06 | B-65764: update nova xml assert (Joe Savak)  
2014-03-06 | B-65764: update sample (Joe Savak)  
2014-03-06 | D-17018 - UsageDeadLetter and UsageSummary can not be included in /functest1 (Greg Sharek)  
2014-03-04 | B-52505 - Upgrade Repose 3.0.2 (Greg Sharek)  
2014-03-04 | B-63677 - New Feed - ebs/events for Oracle EBS (Greg Sharek)  

**usage-schema-1.40.3**  

**usage-schema-1.40.2**  
2014-02-28 | B-64691: update to nova schema, remove is* attributes, add osLicenseType and applicationLicense attribute (Shinta Smith)  
2014-02-28 | B-65400: set CloudBackup license product schema to have groupBy=true (Shinta Smith)  
2014-02-28 | B-65384: set CloudDatabase product schemas to have groupBy=true (Shinta Smith)  
2014-02-27 | B-65400 - add groupby true for serverID for RCBU (Joe Savak)  
2014-02-27 | adding groupby true for dbaas per B-65384 (Joe Savak)  
2014-02-24 | B-39159: Tenanted Feeds, adding functest1 to observer wadl (Greg Sharek)  

**usage-schema-1.40.1**  

**usage-schema-1.40.0**  
2014-02-20 | B-64251: rename /dedicatedvcenter to /dedicatedvcloud, update schemas (Shinta Smith)  
2014-02-19 | B-39159 - Tenanted AH Feed for Public Cloud Access (Greg Sharek)  
2014-02-19 | B-46189: added a way to auto-generate the wadl/filter_private_attrs.xsl (Shinta Smith)  
2014-02-18 | B-46189: private attributes for Widget and CloudServer slice events (Shinta Smith)  
2014-02-18 | B-58598: add identity to observer.wadl (Shinta Smith)  
2014-02-17 | B-64002 Update CBD (Big Data) Usage Event #2 (Paul Benoit)  
2014-02-14 | B-64002 Update CBD (Big Data) Usage Event (Paul Benoit)  

**usage-schema-1.39.0**  
2014-02-07 | B-63987: generated schema update for /netdevice (Shinta Smith)  

**usage-schema-1.38.1**  
2014-02-05 | commenting out fixes for D-17014; introduces other errors (Greg Sharek)  

**usage-schema-1.38.0**  
2014-02-03 | B-61024: remove all the -event files (Shinta Smith)  
2014-02-03 | B-61024: add response file for v3 (Shinta Smith)  
2014-01-30 | B-61024: add v3 of cloudbackup with type USAGE_SNAPSHOT (Shinta Smith)  
2014-01-30 | B-62772 - Update Domain Registration Event - Add REDEMPTION (Greg Sharek)  
2014-01-30 | B-63768 - add startTime & endTime to glance sub-event (Greg Sharek)  
2014-01-29 | D-17014_product.wadl (Greg Sharek)  

**usage-schema-1.37.0**  
2014-01-27 | B-58613 - Update Glance Usage Event (Greg Sharek)  
2014-01-27 | B-61206: add all other product wadl resource to /functest1 (Shinta Smith)  
2014-01-24 | B-63438: increase requestCount to 3M (Shinta Smith)  
2014-01-21 | B-62570: add /migrationtest feed (Shinta Smith)  

**usage-schema-1.36.0**  
2014-01-17 | B-58268: update sites aggregation period to just one (Shinta Smith)  
2014-01-17 | B-50559: add v2 of servers host down event (Shinta Smith)  
2014-01-16 | B-60532 - CDB - update usage event for Redis (Greg Sharek)  
2014-01-16 | B-62164 - Update Usage Error Event:  Add DUPLICATE_USAGE_ID and UNSUPPORTED_CORRECTED_USAGE (Greg Sharek)  

**usage-schema-1.35.0**  
2014-01-13 | B-61918: update /support schema (Shinta Smith)  

**usage-schema-1.34.1**  
2014-01-10 | D-16790: fix lbaas vip resourceId rules per email from Jorge Miramontes (Shinta Smith)  

**usage-schema-1.34.0**  
2014-01-08 | B-61993: add /automation feed (Shinta Smith)  
2014-01-08 | B-58430 - New Schema for net_device/events for FDaAT (Greg Sharek)  
2014-01-07 | D-16790: update xpathAssert for IPv6 resourceId (Shinta Smith)  
2014-01-07 | B-61494: add multiFactorEnabled attribute for Identity events (Shinta Smith)  
2014-01-07 | B-60544: initial draft of Dedicated vCenter schema (Shinta Smith)  
2013-12-19 | B-61918: initial work for /support events (Shinta Smith)  
2013-12-19 | B-58430 - New Schema for net_device/events for FDaAT (Greg Sharek)  
2013-12-19 | D-16658: fix issue where only unbounded value can be used for maxOccurs or attributeGroup (Shinta Smith)  

**usage-schema-1.33.1**  
2013-12-16 | D-07837 - Product_Security - S2 - Long term attributes on category element expose Hibernate error (Greg Sharek)  

**usage-schema-1.32.0**  
2013-12-04 | D-16404: change ranEnrichmentStrategy of emailApps schemas to NONE (Shinta Smith)  
2013-12-04 | B-55572: add schema for SSL subscription event (Shinta Smith)  
2013-12-02 | B-56152: /usagesummary feed, load the usage-summary.xsd properly (Shinta Smith)  

**usage-schema-1.31.1**  

**usage-schema-1.31.0**  
2013-11-18 | B-59357 - Update CFiles Storage Usage Event - add WEIGHTED_AVG on Disk (Greg Sharek)  
2013-11-18 | D-16259: add checks to allow non-starndard events w/o dc/region (Shinta Smith)  
2013-11-14 | B-58665: move ranEnrichmentStrategy to be part of <appinfo> of product schema (Shinta Smith)  
2013-11-13 | B-59122: add /ssl feed (Shinta Smith)  

**usage-schema-1.30.1**  
2013-11-08 | D-16172: set groupByResource=false for Cloud Queues Bandwidth, remove resourceId and resourceName for all queues events (Shinta Smith)  
2013-11-06 | D-15931: fix issues where some Sites events without DC/Region are not rejected (Shinta Smith)  

**usage-schema-1.30.0**  
2013-11-05 | B-56159: rename deadletter back to usagedeadletter (Shinta Smith)  
2013-11-04 | B-58359 - Add appliance attribute. (Greg Sharek)  
2013-11-04 | D-15931: further restrict events that do not send dc/Region, except for Maas, Domain and Sites subscription, which would default to GLOBAL (Shinta Smith)  
2013-10-31 | B-58701 - Email & Apps Usage Event for Exchange (Greg Sharek)  
2013-10-31 | B-58701 - Email & Apps Usage Event for Exchange (Greg Sharek)  
2013-10-31 | B-59327: remove aggregateFunction=SUM on the ServiceNet bandwidth in & out attributes, since they are not used for billing (Shinta Smith)  
2013-10-30 | B-59327: add new attributes for Cloud Queues Bandwidth (Shinta Smith)  
2013-10-30 | B-58535: change aggregation periods for some sites events (Shinta Smith)  
2013-10-30 | B-58699: push actual changes to reject synthesized attribute (Shinta Smith)  
2013-10-30 | B-58699: get rid of experimental code (Shinta Smith)  

**usage-schema-1.29.0**  
2013-10-29 | B-58427: add /core feed to wadls (Shinta Smith)  
2013-10-29 | B-56175: remove billable from core_xsd (Shinta Smith)  
2013-10-28 | B-58910: added /usagesummary to observer wadl (Shinta Smith)  
2013-10-28 | B-58910: add /usagesummary feed (Shinta Smith)  
2013-10-28 | B-58356 - Queues Usage Event for Request Count (Shinta Smith)  
2013-10-28 | D-15981 - Unable to access individual entries in staging or test (Shinta Smith)  
2013-10-28 | B-58493 - Update Queues Usage Bandwidth Events (Shinta Smith)  
2013-10-24 | B-58710 - Update CBS Usage Events (Greg Sharek)  

**usage-schema-1.28.0**  
2013-10-22 | B-57395: get rid of CloudIdentity dups in productSchema-wadl, more clean ups (Shinta Smith)  
2013-10-22 | B-57395: clean up samples (Shinta Smith)  
2013-10-21 | B-57395 rebase to master and fix resourceType conflict (Bryan Taylor)  
2013-10-21 | B-57395 implement transormation to calculate big data aggregatedClusterDuration (Bryan Taylor)  
2013-10-21 | B-58641: rename client_username to clientUsername and agent_username to agentUsername (Shinta Smith)  
2013-10-17 | B-58136: rename usageerror/events to usagedeadletter/events (Shinta Smith)  
2013-10-17 | B-58429: add unvalidated /netdevice feed to wadl (Shinta Smith)  
2013-10-17 | Fix merge B-51154 (Greg Sharek)  
2013-10-17 | Merge B-51154 (Greg Sharek)  
2013-10-16 | B-51154: got rid of extranous variables (Shinta Smith)  
2013-10-16 | B-51154: add wadl asserts to restrict the use of GLOBAL dataCenter and region (Shinta Smith)  
2013-10-15 | B-56175: remove billable attribute from product schemas (Shinta Smith)  
2013-10-15 | B-40572: remove lbaas.xsl and lbaas-stopgap-tests.scala (Shinta Smith)  
2013-10-15 | B-40572: remove lbaas unit test on the transformation, generate wadl (Shinta Smith)  
2013-10-15 | B-40572: remove SSL transformation on LBaaS usage event (Shinta Smith)  

**usage-schema-1.27.0**  
2013-10-08 | B-54028 - Update Big Data Usage Event - flavorName and flavorId cannot be empty String. (Greg Sharek)  
2013-10-07 | B-56849 - Add Metadata to Define Tenant Enrichment (Greg Sharek)  
2013-10-07 | B-54030: add minLength to string types (Shinta Smith)  
2013-10-07 | B-54030: add minLength to nova's flavorId and flavorName attributes (Shinta Smith)  
2013-10-02 | B-53783 - Add delegated/impersonation/agent username to Core Schema (Greg Sharek)  

**usage-schema-1.26.3**  
2013-10-01 | D-15294: manually merge in Greg's changes (Shinta Smith)  

**usage-schema-1.26.2**  
2013-10-01 | D-15294: rename atom_hopper_identity.wadl (Shinta Smith)  

**usage-schema-1.26.0**  
2013-09-27 | B-57395 adding synthesized attributes & using them for aggregatedClusterDuration in bigdata (Bryan Taylor)  

**usage-schema-1.25.0**  
2013-09-24 | B-55600: add UPDATE action for sites subscription (Shinta Smith)  
2013-09-24 | B-56944: update resourceTypes for bigdata (Shinta Smith)  
2013-09-12 | B-56207 - Update CDB Usage Event - Remove aggregateFunction on Memory & Storage (Greg Sharek)  

**usage-schema-1.24.1**  
2013-09-11 | B-53705: add atom_hopper_observer.wadl into rpm (Shinta Smith)  

**usage-schema-1.24.0**  
2013-09-09 | Revert "Merge pull request #83 from shintasmith/B-50883" (Greg Sharek)  
2013-08-30 | B-53705: remove test related feeds from the readonly wadl (Shinta Smith)  
2013-08-30 | B-53705: add atom_hopper_observer.wadl that will be used by ahaas/Atom Hopper public/Cloud Feeds (Shinta Smith)  

**usage-schema-1.23.3**  
2013-08-30 | B-55725 - Sites feed:  increase requestCount to 50M max. (Greg Sharek)  
2013-08-16 | B-54382 - Update CDB Usage Event - Add aggregateFunction of WEIGHTED_AVG (Greg Sharek)  
2013-08-16 | B-54666 - Update aggregate Functions - Remove CUMULATIVE_AVG, add WEIGHED_AVG (Greg Sharek)  

**usage-schema-1.23.2**  
2013-08-13 | B-50562: add range for v2 (Shinta Smith)  

**usage-schema-1.23.1**  
2013-08-12 | B-52649: add namespace restriction for the any element other than event element, fix unit tests that do not have the expect error 400 (Shinta Smith)  
2013-08-06 | B-50799: add /usagerecovery and /usageerror feeds (Shinta Smith)  
2013-08-06 | B-50562: add min/max for monitoring zones (Shinta Smith)  

**usage-schema-1.23.0**  
2013-08-05 | B-50560: add bad sample files (Shinta Smith)  
2013-08-05 | B-50560: add range for freeops and costops for files storange events (Shinta Smith)  
2013-07-31 | B-47908: add serverName to glance (Shinta Smith)  
2013-07-31 | B-52644: modify bigdata schema, flavor=>flavorId, add flavorName, add resourceName in samples (Shinta Smith)  
2013-07-31 | B-53194 - Add Cloud Queues Usage Event (Greg Sharek)  
2013-07-22 | B-50935: make the 'use' attribute a required attribute (Shinta Smith)  
2013-07-22 | B-50883: convert nova's assert to using xpathAssert (Shinta Smith)  
2013-07-19 | B-52483 - Add username to Core Schema (Greg Sharek)  
2013-07-19 | B-50883 need capability to add WADL assertion in productSchema (Greg Sharek)  

**usage-schema-1.22.0**  
2013-07-17 | B-52648: add new feeds (Shinta Smith)  
2013-07-16 | B-50561: add min, max for memory and storage (Shinta Smith)  
2013-07-15 | B-50523: add min, max to bandwidthIn, bandwidthOut and storage attributes (Shinta Smith)  
2013-07-15 | B-50883: add xpath asssertions for wadl, adapt the generation of <assert> in XSD, change identity xpath assert (Shinta Smith)  
2013-07-08 | B-51717 Add Dedicated feed -Modified atom hopper wadl file to support dedicated feed. (Srikanth Kambhampati)  
2013-07-08 | B-50281 Removed feed links for Quantum and Melange, added feed for perftest (Srikanth Kambhampati)  
2013-07-05 | B-51658 Add Autoscale Feed -Modified wadl file to add support for new autoscale feed. (Srikanth Kambhampati)  
2013-07-01 | B-50281 Removed feed links for Quantum and Melange, added feed for perftest (Srikanth Kambhampati)  

**usage-schema-1.21.1**  
2013-06-25 | B-49764: update nova with flavorId, flavorName, status (Shinta Smith)  
2013-06-25 | B-51417: allow string and string* types to specify optional maxLength attribute (Shinta Smith)  
2013-06-24 | B-49810: add assert for v2 non-UPDATE, the updatedAttributes attribute should not be used (Shinta Smith)  

**usage-schema-1.21.0**  
2013-06-19 | B-50937: add the use="optional" attribute to all product schema that does not have it (Shinta Smith)  
2013-06-18 | B-49579: change resourceType=SLICE to SERVER, add samples (Shinta Smith)  
2013-06-17 | B-48562: add monitoring product enum and productInstanceId (Shinta Smith)  
2013-06-14 | B-49810: add identity type=UPDATE version 2 with required updatedAttributes (Shinta Smith)  

**usage-schema-1.20.0**  
2013-05-31 | B-49811 - add aggregation periods for cloud sites product event metadata. (Benjamin Truitt)  
2013-05-31 | B-38000: add bad samples (Shinta Smith)  
2013-05-30 | B-38000: add sample response files (Shinta Smith)  
2013-05-30 | B-38000: Cloud DNS create and delete system events (Shinta Smith)  

**usage-schema-1.19.1**  
2013-05-28 | B-48322: package atom_hopper_identity.wadl in the RPM (Shinta Smith)  
2013-05-28 | B-48178: Update Sites Email Usage Event (mattkovacs)  

**usage-schema-1.19.0**  
2013-05-22 | B-48307: Update Nova Host Server Down Event - Remove 'status' attribute (mattkovacs)  
2013-05-16 | Add aggregation durations to product metadata for B-43160. (Benjamin Truitt)  

**usage-schema-1.18.1**  
2013-05-13 | B-48551: combine several identity feeds into one as /identity/events (Shinta Smith)  
2013-05-13 | B-48528: Add CloudBackLicense version two event (mattkovacs)  

**usage-schema-1.17.4**  
**usage-schema-1.17.3**  
2013-05-03 | D-12736: fix RPM so we specify just %config, and not %config(noreplace) (Shinta Smith)  
2013-05-03 | D-12072: upgrade to api-checker-1.0.8 (Shinta Smith)  

**usage-schema-1.17.1**  
**usage-schema-1.17.0**  
2013-04-29 | B-46480: add additional IP for First Gen Cloud Servers (Shinta Smith)  
2013-04-29 | B-47501: Update Legacy Server Slice Action Event (mattkovacs)  
2013-04-29 | B-44343: make serverId optional, add optional serverName (Shinta Smith)  
2013-04-29 | B-47354: CBU License usage event update (mattkovacs)  
2013-04-26 | B-44343: rename all references to plural IP to singular IP (Shinta Smith)  
2013-04-26 | B-37262: fix permission (Shinta Smith)  
2013-04-26 | B-37262: change pom.xml version to 1.1.17 since R0016 is already out (Shinta Smith)  
2013-04-25 | B-44343: add Nova IP usage event (Shinta Smith)  
2013-04-25 | B-46594: Add LBaaS System Event for DELETE (mattkovacs)  
2013-04-25 | B-47481: minor formatting (Shinta Smith)  
2013-04-25 | B-47481: add instructions on how to generate XSD 1.0 schemas (Shinta Smith)  
2013-04-24 | B-47481: semi-automate the generation of the XSD 1.0 (Shinta Smith)  
2013-04-24 | B-47481: add SYD to XSD 1.0 (Shinta Smith)  
2013-04-24 | B-37262: correct permissions (Shinta Smith)  
2013-04-22 | B-37262: rpm-ize usage-schema (Shinta Smith)  

**R0016**  

**R0015**  
2013-04-10 | B-46506: Update servers slice action Event - Add password attribute (mattkovacs)  
2013-04-09 | B-37309, B-44543: fix some of the malform response files (Shinta Smith)  
2013-04-09 | B-37309,B-44534 (Kari Davis)  
2013-04-09 | B-40571: add new usage for Sites email (Shinta Smith)  
2013-04-09 | B-41580: Adding public flag to all attributes, default is false. (mattkovacs)  
2013-04-08 | B-41470: add more files to bigdata usage events (Shinta Smith)  
2013-04-08 | B-41470: add bigdata usage events (Shinta Smith)  
2013-04-08 | D-12072, D-12007 unit test (Shinta Smith)  
2013-04-03 | \[B-45250\] remove aggregateFunction for numFiles attribute, update descriptions of other attributes (Shinta Smith)  
2013-04-02 | \[B-45726\] for Sites Metered usage, add default 0 for computeCyles if not specified (Shinta Smith)  

**R0014**  
2013-03-26 | \[B-43299\] add new Legacy server slice actions (Shinta Smith)  
2013-03-25 | B-39660: Add Next Gen Servers Usage Event (mattkovacs)  
2013-03-25 | B-43746: Add Next Gen Servers Usage Event (mattkovacs)  
2013-03-22 | \[B-44637\] update domain event to add endUserTenantId and remove domainGoLiveDate (Shinta Smith)  
2013-03-22 | \[B-43478\] fix unit test break caused by the move of domain events out of sites feed (Shinta Smith)  
2013-03-20 | \[B-43478\] add new domain feed and remove it from sites (Shinta Smith)  
2013-03-19 | B-43746: Adding action attribute to sites subscription event (mattkovacs)  

**R0013**  
2013-03-18 11:48:05 -0500    changed to 1.0.12 wadl tool release so we can release R0013 (Matt Kovacs)  
2013-03-18 | \[B-44127\]\[TK-77618\] added unit test for LBaaS SSL=ON rule (Shinta Smith)  
2013-03-18 09:50:23 -0500    changed to wadl tool release so we can release R0013 (Matt Kovacs)  
2013-02-28 | Add aggregation durations to product metadata for B-43160. (Benjamin Truitt)  

**R0012**  

**R0011**  

**R0010**  
2013-01-28 | Address B-40007 by adding groupBy public IPs, private IPs and Flavor. (Jorge L. Williams)  
2012-12-17 | Add min and max values for bandwidth, flavor, extra IPs (see B-36951). (Benjamin Truitt)  

**R0007**  

**R0006**  

**R0005**  

**R0004**  

**R0003**  

**R0002**  

**R0001**  
