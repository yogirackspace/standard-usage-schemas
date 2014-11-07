#!/bin/sh

usage()
{
    echo "Usage: `basename $0` <path_to_xml_file_to_convert_to_json>"
    exit 1
}

if [ $# -lt 1 ]; then
    echo "Error: missing argument"
    usage
fi

# these must match with the pom.xml
SAXON=saxon-ee
SAXON_VERSION=9.4.0.6

SAXON_JAR=$HOME/.m2/repository/net/sf/saxon/$SAXON/$SAXON_VERSION/$SAXON-$SAXON_VERSION.jar

if [ ! -f $SAXON_JAR ]; then
    echo "Error: unable to find a version of saxon.jar in local Maven repository"
    echo "       here: $SAXON_JAR."
    echo "       Have you done a build (mvn clean install) in this repo?"
    exit 2
fi

INPUT_FILE=$1
TRANSFORMER=`dirname $01`/../../../target/xslt-artifacts/xml2json-feeds.xsl

if [ ! -f $TRANSFORMER ]; then
    echo "Error: unable to find the xslt here $TRANSFORMER"
    echo "Have you done a build (mvn clean install) in this repo?"
    exit 3
fi

java -classpath $SAXON_JAR net.sf.saxon.Transform -s:$INPUT_FILE -xsl:$TRANSFORMER -it:main | python -mjson.tool 
exit $?
