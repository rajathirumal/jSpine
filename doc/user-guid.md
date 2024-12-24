# User guid
This provides the step by step instruction to use jSpine to write your own bussiness logic

## Setup
This project when extracted will contains all the required components in a folder called jSpine. 

> [!NOTE] 
> This folder is your home folder for jSpine

The below will be the folder structure for the framework,
```bash
|- jSpine
|   |-conf
|   |   |-prop.default
|   |   |-framework.properties
|   |-custom_lib   
|   |   |-demoJar-0.0.1-SNAPSHOT.jar
|   |   |-YourJar.jar
|   |-doc
|   |   |-developer-guid.md
|   |   |-user-guid.md
|   |-lib
|   |   |-SpineCommon.jar
|   |   |-SpineCommunicator.jar
|   |   |-SpineCore.jar
|-.gitignore
```


*Step 1* : Navigate to `conf/prop.default` set your own property file name.

*Step 2* : The property file should follow the same syntax as the given sample property file [framework.properties](/conf/framework.properties)

*Step 3* : Edit the details mentioned. You are good to use it.

Start building your code. Refer to the [documentation](/doc/developer-guid.md) for the available functions.

# Integration guid

This is under development. We have worked on selecting and inserting data from the DB via the core framework. This works fine in standalone mode.
The current development is for the API calls on the framework operation (Querying the DB via API)
