<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
    <p>
        The following schematron is used to check assertions
        on product XSDs that cannot be easily checked
        directly by the XSD validator.
    </p>
    <ns uri="http://www.w3.org/2001/XMLSchema" prefix="xsd"/>
    <ns uri="http://docs.rackspace.com/usage/core" prefix="usage"/>
    <pattern id="main">
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
        </rule>
    </pattern>
</schema>