<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
    <p>
        The following schematron is used to check assertions
        on product XSDs that cannot be easily checked
        directly by the XSD validator.
    </p>
    <ns uri="http://www.w3.org/2001/XMLSchema" prefix="xsd"/>
    <ns uri="http://docs.rackspace.com/usage/core" prefix="usage"/>
    <pattern id="attributes">
        <p>
            Make sure that each XSD defines version and resourceType attributes.
        </p>
        <rule context="xsd:complexType">
            <assert test="xsd:attribute[@name='version' and @use='required' and @fixed]">
                The Product XSD must contain an attribute named version.  The attribute should be
                required and must have a fixed value.
            </assert>
            <assert test="xsd:attribute[@name='resourceType' and @use='required']">
                The Product XSD must contain an attribute named resourceType. The attribute should
                be required.
            </assert>
            <assert test="count(xsd:attribute) &lt; 50">
                A Product XSD may not define more than 50 attributes.
            </assert>
        </rule>
    </pattern>
    <pattern id="unit-of-measure">
        <p>
            Make sure that the unit of measure is applied to a number of some kind.
        </p>
        <rule context="xsd:attribute[xsd:annotation/xsd:appinfo/usage:attributes/@aggregate-function != 'NONE']">
            <let name="qn" value="resolve-QName(@type,.)"/>
            <assert test="namespace-uri-from-QName($qn) = 'http://www.w3.org/2001/XMLSchema'">
                You must use a standard schema type when the aggregate-function whole value != NONE.
            </assert>
            <assert test="local-name-from-QName($qn) = ('decimal','integer','nonPositiveInteger',
                    'negativeInteger','nonNegativeInteger', 'long',
                    'positiveInteger', 'unsignedLong',
                     'int', 'unsignedInt', 'short', 'unsignedShort', 'byte', 'unsignedByte')">
                Type for aggregate-function attribute should be standard XSD numeric type.
            </assert>
        </rule>
    </pattern>
</schema>
