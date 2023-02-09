Feature: Testing Cloud component in Greengrass

    Background:
        Given my device is registered as a Thing
        And my device is running Greengrass

    @Sample
    Scenario: As a developer, I can create a component in Cloud and deploy it on my device
        When I create a Greengrass deployment with components
            | com.example.HelloWorldWithTestFramework | classpath:/greengrass/recipes/recipe.yaml |
        And I deploy the Greengrass deployment configuration
        Then the Greengrass deployment is COMPLETED on the device after 180 seconds
        And I call my custom step