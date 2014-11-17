#!/bin/sh

usage()
{
    echo "Usage: `basename $0` <product schema xml file> <feedname>"
    exit 1
}

if [ $# -lt 2 ]; then
    echo "Error: missing argument(s)"
    usage
fi

SCHEMA_XML=$1
FEED_NAME=$2
mvn -P generate-samples clean generate-sources -DproductSchema=${SCHEMA_XML} -DfeedName=${FEED_NAME}
exit $?
