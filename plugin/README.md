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


<count>
  <name>current.build.number</name>
  <file>foo.properties</file>
  <increment>1</increment>
  <field>1</field>
  <create>false</create>
</count>

increment: amount to add 
field: if the number is formatted a.b.c.d, the field in the count.
create: whether to create a count property or to die otherwise.
multiple counts can be grouped.

multiple countGroups can exist:

<countGroups>
  <countGroup>
    <id>regular</id>
    <counts>


countGroups can be activated with activation

<configuration>
  <activation>regular</activation>

activates the "regular countGroup" for that specific build. This allows putting all the countGroups in one place (pluginManagement) and just activate what is needed in an execution.



