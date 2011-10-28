The Maven Numbers Plugin
========================

"Everything Counts (in large amounts)" -- Depeche Mode

"4 8 15 16 23 42" - Lost

Counts:

- number count (from a properties file)
- three digit count (properties file)
- SCM version (current checkout, tip, arbitrary branch)
- date (various formats)

Formats:

- format string with the information from above
- optional separators ({-})
- properties and other data from the pom.

<configuration>
    <activeGroups>snapshot,release</activeGroups>
    <propertyGroups>
        <propertyGroup>
            <name>snapshot</name>
            <activeOnRelease>false</activeOnRelease>
            <properties>
                <property>
                    <name>ness.build.number</name>
                    <value>#{version}-#{regular}-#{user}</value>
                </property>
            </properties>
        </propertyGroup>
        <propertyGroup>
            <name>release</name>
            <activeOnSnapshot>false</activeOnSnapshot>
            <properties>
                <property>
                    <name>ness.build.number</name>
                    <value>#{version}</value>
                </property>
            </properties>
        </propertyGroup>
    </propertyGroups>
    <numbers>
        <number>
            <name>version</name>
            <propertiesFile>/home/jenkins/builds/build-counter</propertiesFile>
            <!-- version is maj.min.rev -->
            <fieldNumber>2</fieldNumber>
        </number>
        <number>
            <name>build</name>
            <propertiesFile>/home/jenkins/builds/build-counter.properties</propertiesFile>
            <propertyName>${project.groupId}-${project.artifactId}.counter</propertyName>
            <onMissingProperty>fail</onMissingProperty>
            <onMissingFile>fail</onMissingFile>
            <initialValue>0</initialValue>
        </number>
    </numbers>
    <strings>
        <string>
            <name>user</name>
            <value>${maven.user}</value>
            <defaultValue>unknown</defaultValue>
        </string>
    </strings>
    <dates>
        <date>
            <name>regular</name>
            <format>yyyyMMdd_ssmmHH</format>
        </date>
    </dates>
    <macros>
        <macro>
            <name>revision</name>
            <type>scm</type>
            <properties>
                <property>
                    <name>revision</name>
                    <value>tip</value>
                </property>
            </properties>
        </macro>
    </macros>
</configuration>

Configuration
=============

* skip - true, false

When set, skip the execution of the plugin.

* onDuplicateProperty - ignore, warn, fail

When a property is defined multiple times, react accordingly. (default: fail)

* onMissingProperty - ignore, warn, fail

When a property definition is incomplete (value missing), react accordingly. (default: fail)

* activeGroups

defines which groups are active. The idea is that most of this configuration can be done in a pluginManagement section and then the <activation> field is used in the actual build profiles to activate various groups.

* propertyGroup

groups and exports a list of properties exposed to the build. 

<propertyGroups>
    <propertyGroup>
        <name>...</name>
        <activeOnRelease>...</activeOnRelease>
        <activeOnSnapshot>...</activeOnSnapshot>
        <onDuplicateProperty>...</onDuplicateProperty>
        <onMissingProperty>...</onMissingProperty>
        <properties>
            ...
        </properties>
    </propertyGroup>
</propertyGroups>

** name - the name of the property group. Required. Must be unique.
** activeOnSnapshot - true, false - this group is active if the current ${project.version} contains "-SNAPSHOT" (default: true)
** activeOnRelease - true, false - this group is active if the current ${project.version} does not contain "-SNAPSHOT" (default: true)
** onDuplicateProperty - ignore, warn, fail - When a property is defined multiple times, react accordingly. (default: from global config)
** onMissingProperty - ignore, warn, fail - When a property definition is incomplete (value missing), react accordingly. (default: from global config)

** properties/property - List of properties in this property group

<property>
    <name>...</name>
    <value>...</value>
    <export>...</export>
    <skip>...</skip>
</property>

** name - the name of the property that gets exported. Required. Must be unique in the group.
** value - a String that gets interpreted as a format string (see below) and assembled to form the value. Can contain constant values, #{ } groups and ${ } properties (see below).
** export - true, false - Whether the value should be exported as a property. (default: true)
** skip - true, false - Whether the property should be evaluated. False implies also "export = false". (default: true)

* numbers / number

A number defines a counter or count.

<number>
    <name>...</name>
    <skip>...</skip>
    <initialValue>...</initialValue>
    <fieldNumber>...</fieldNumber>
    <increment>...</increment>
    <propertiesFile>...</propertiesFile>
    <propertyName>...</propertyName>
    <onMissingProperty>...</onMissingProperty>
    <onMissingFile>...</onMissingFile>
</number>

** name - the name of the number. Required. Must be unique between numbers, strings, dates and macros.
** skip - true, false - Whether the counter should be evaluated. Default is false.
** initialValue - String, containing integers. This is the initial value of the counter.
** fieldNumber - if the value of the counter contains more than one integer field (i.e. 0.0.0 or 1.2-test8), each field can be incremented separately. The field number select which field to increment (0 based), *COUNTED FROM THE LEFT*.
                 If the value is "0.0.0" and the fieldNumber is "2", incrementing it by one will yield 0.0.1. If the field number is "0", it will yield 1.0.0.
** increment - integer, the value that gets added every time the plugin is invoked. Default value is "1".
** propertiesFile - A file to load and store the current state of the counter. Optional, if unset the count is ephemeral.
** propertyName - The name of the property to load and save the counter. Optional, Default is the name.
** onMissingProperty - ignore, warn, fail, create  - If the count should be stored in a file and the file does not contain a property with its name, create the property with its initial value. Otherwise, fail the build. Default is fail.
** onMissingFile - ignore, warn, fail, create - If the count should be stored in a file and the file does not exist, create the file. Otherwise, fail the build. Default is fail.

** strings / string

Defines a text string for further replacement. This can be used to add default values to ${ } properties.

<string>
    <name>user</name>
    <skip>...</skip>
    <values>
        <value>...</value>
        <value>...</value>
    </values>
    <blankIsDefault>...</blankIsDefault>
    <onMissingValue>...</onMissingValue>
</string>

** name - the name of the string. Required. Must be unique between numbers, strings, dates and macros.
** skip - true, false - Whether the value field should be evaluated. Default is false.
** values - a list of strings that are used for the value. If a value is not set, the next one is used until a non-blank value is found or the list is exhausted. At least one value is required to be defined.
** blankIsValid - true / false - whether an empty ('') string is treated as a valid value or whether the next value should be evaluated. Default is true.
** onMissingValue - warn, fail, ignore - Action when no value was found. If warn or ignore are chosen and no value was found, the string subsequently is not defined which in turn might cause an error in a property definition.

** dates / date

Defines a date, time or date/time combination. 

<dates>
    <date>
        <name>...</name>
        <skip>...</skip>
        <format>...</format>
        <timezone>...</timezone>
        <value>...</value>
    </date>
</dates>

** name - the name of the date. Required. Must be unique between numbers, strings, dates and macros.
** skip - true, false - Whether the date field should be evaluated. Default is false.
** format - A simpledateformat string for formatting a date. Required.
** timezone - A valid java timezone name. Optional, defaults to system timezone.
** value - Number of seconds since the epoch to format. Optional, if missing the current time is used. 

** macros / macro

Macros extend the functionality of the plugin. 

<macros>
    <macro>
        <name>...</name>
        <skip>...</skip>
        <type>...</type>
        <class>...</class>
        <properties>...</properties>
        <onMissingValue>...</onMissingValue>
    </macro>
</macros>

** name - the name of the macro. Required. Must be unique between numbers, strings, dates and macros.
** skip - true, false - Whether the macro should be evaluated. Default is false.
** type - String. Selects a predefined macro which is registered with the plugin. Optional. One of type or class must be given.
** class - String. A java class to use as a macro. Optional. One of type or class must be given.
** properties - Set of properties sent to the macro.
** onMissingValue - warn, fail, ignore - Action when the macro did not return a value. If warn or ignore are chosen and no value was found, the macro result subsequently is not defined which in turn might cause an error in a property definition.

** scm macro

The scm macro defines values from the underlying SCM.

<macros>
    <macro>
        <name>revision</name>
        <type>scm</type>
        <properties>
            <property>
                <name>rev</name>
                <value>tip</value>
            </property>
        </properties>
    </macro>
</macros>

** type is SCM
** properties
*** revision - String with valid SCM name - The name gets sent to the underlying SCM to and the SCM returns a revision string.


