## [Greengrass Testing Framework Template]

### Overview

Base template for starting a greengrass generic component configured to work with the [open testing framework](https://github.com/aws-greengrass/aws-greengrass-testing).
It allows developers to leverage the testing framework to build e2e user acceptances tests.

Template Structure:

```
TestingFramework
│   pom.xml
│
└───component
│   │   ...
|   |   pom.xml
│   
└───uat
    │   pom.xml 
    └── src
        └── main
            ├── java
            │        └── com
            │            └── aws
            │                └── greengrass
            │                    └── CustomSteps.java
            └── resources
                └── greengrass
                    ├── features
                    │        └── component.feature
                    └── recipes
                        └── recipe.yaml

```

### component (module)
This is where you would write your component. Inside there is an App.java file which acts as the entry point
for your component

### uat (module)
Sets up the [open testing framework](https://github.com/aws-greengrass/aws-greengrass-testing) and consumes your
component as a dependency.

The open testing framework uses [cucumber](https://cucumber.io/docs/guides/) under the hood to run your tests. The
framework has some predefined steps to help you automatically deploying your component to a greengrass core and 
execute e2e tests.

* Place your tests under the `resources.greengrass.features`
* Define custom recipes to test your artifact under `resources.greengrass.recipes`
* Define custom test steps inside the `com.aws.greengrass` package

When your project is built from the root, the `uat` module will copy your component's artifact with the name 
`componentArtifact` and store it on the `classpath:/greengrass/artifacts/componentArtifact.jar` of the generated jar.

### Building the template

From the root run `mvn clean verify`

### Running the UATs

The sample test on this template will spin up a Greengrass Core on your local machine, upload your component to Iot Core
and deploy it to the local core and wait for the deployment to succeed.

Prerequisites:

 * Export your aws credentials to environment variables
 * You must run the tests on a Linux distro or Windows
 * Have java installed
 * Have maven installed
 * Build the template `mvn clean verify`
 * Download the latest nucleus `curl -s https://d2s8p88vqu9w66.cloudfront.net/releases/greengrass-nucleus-latest.zip > greengrass-nucleus-latest.zip`
   * Note down the path of the zip in your file system (you'll need it below)


From the root of the project run (make sure to replace the values)

`java -Dggc.archive=<path-to-nucleus-zip> -Dtest.log.path=./results -Dtags=Sample -jar ./uat/target/ComponentUATs-1.0.0-SNAPSHOT.jar`


### Making changes

In order for the template to be useful for your component you will have to change a few package names

1) Replace the `com.templates.otf` package name to whatever name you want. Make sure to not only change the folder names but also the text referencing it.
2) Replace the contents of this README
3) Change the component. Do a search and replace for `HelloWorldWithTestFramework`


## License

Add License information here

