# Developer Guide

There is a JAR called `demoJar-<version>.jar` under the `custom_lib` folder. This is a sample of how the user could use plain Java code to interact with the database.

## Methods available

As of now the below are supported in the custom jar.

- This sets the log file name for the current context
    `SpineLogger setLogFile(String logFile);`

- Performs a database select with the given condition
    `List<String> selectRecordIds(String tableName, String condition);`

- Performs a full select on the given table name
    `List<String> selectRecordIds(String tableName);`