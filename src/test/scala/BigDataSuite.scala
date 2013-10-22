package com.rackspace.usage

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import org.w3c.dom.Document

import scala.xml._

import com.rackspace.com.papi.components.checker.servlet.RequestAttributes._
import com.rackspace.cloud.api.wadl.Converters._
import com.rackspace.com.papi.components.checker.Converters._
import com.rackspace.com.papi.components.checker.servlet.RequestAttributes._

import BaseUsageSuite._

//
//  Tests on big data transformations
//

@RunWith(classOf[JUnitRunner])
class BigDataSuite extends BaseUsageSuite {

  register ("bigdata","http://docs.rackspace.com/usage/bigdata")

  val beforeTransform = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                            <atom:title>Cloud Big Data</atom:title>
                            <atom:content type="application/xml">
                                <event xmlns="http://docs.rackspace.com/core/event"
                                       xmlns:bigdata="http://docs.rackspace.com/usage/bigdata"
                                       version="1" tenantId="2888"
                                       id="e53d007a-fc23-11e1-975c-cfa6b29bb814"
                                       resourceId="56"
                                       resourceName="My Big Data Cluster"
                                       type="USAGE" dataCenter="DFW1" region="DFW"
                                       startTime="2013-03-15T11:51:11Z"
                                       endTime="2013-03-16T11:51:11Z">
                                    <bigdata:product version="1" serviceCode="BigData"
                                                resourceType="HADOOP_HDP1_1" flavorId="10"
                                                flavorName="some flavor"
                                                numberServersInCluster="500"
                                                bandwidthIn="1024" bandwidthOut="19992"/>
                                </event>
                            </atom:content>
                        </atom:entry>

  val emptyOptional = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                            <atom:title>Cloud Big Data</atom:title>
                            <atom:content type="application/xml">
                                <event xmlns="http://docs.rackspace.com/core/event"
                                       xmlns:bigdata="http://docs.rackspace.com/usage/bigdata"
                                       version="1" tenantId="2888"
                                       id="e53d007a-fc23-11e1-975c-cfa6b29bb814"
                                       resourceId="56"
                                       resourceName="My Big Data Cluster"
                                       type="USAGE" dataCenter="DFW1" region="DFW"
                                       startTime="2013-03-15T11:51:11Z"
                                       endTime="2013-03-16T11:51:11Z">
                                    <bigdata:product version="1" serviceCode="BigData"
                                                resourceType="HADOOP_HDP1_1" flavorId="10"
                                                aggregatedClusterDuration=""
                                                flavorName="some flavor"
                                                numberServersInCluster="500"
                                                bandwidthIn="1024" bandwidthOut="19992"/>
                                </event>
                            </atom:content>
                        </atom:entry>

  test ("A usage event should set aggregatedClusterDuration by calculating duration as endTime - startTime and multiplying by numberServersInCluster") {
    val req = request("POST", "/bigdata/events", "application/atom+xml", beforeTransform)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:content/event:event/bigdata:product/@aggregatedClusterDuration = '43200000'")
   }

  test ("A usage event should reset the value of aggregatedClusterDuration by calculating duration as endTime - startTime and multiplying by numberServersInCluster") {
    val req = request("POST", "/bigdata/events", "application/atom+xml", emptyOptional)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:content/event:event/bigdata:product/@aggregatedClusterDuration = '43200000'")
   }

}