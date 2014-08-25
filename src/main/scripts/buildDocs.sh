#!/bin/sh

export MAVEN_OPTS="-Xms1024m -XX:MaxPermSize=1024m -Xmx1024m -Xrs"
mvn clean -Dgenerate.docs generate-sources process-sources -Dwebhelp.war=1
