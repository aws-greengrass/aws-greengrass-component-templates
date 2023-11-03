Feature: Confidence Test Suite

  Background:
    Given my device is registered as a Thing
    And my device is running Greengrass

  @ConfidenceTest
  Scenario: As a Developer, I can deploy GDK_COMPONENT_NAME to my device and see it is working as expected
    When I create a Greengrass deployment with components
      | GDK_COMPONENT_NAME | GDK_COMPONENT_RECIPE_FILE |
      | aws.greengrass.Cli | LATEST                    |
    And I deploy the Greengrass deployment configuration
    Then the Greengrass deployment is COMPLETED on the device after 180 seconds
    # Update component state accordingly. Possible states: {RUNNING, FINISHED, BROKEN, STOPPING}
    And I verify the GDK_COMPONENT_NAME component is RUNNING using the greengrass-cli

  @ConfidenceTest
  Scenario: As a Developer, I can deploy GDK_COMPONENT_NAME and restart Greengrass to check if it is still working as expected
    When I create a Greengrass deployment with components
      | GDK_COMPONENT_NAME | GDK_COMPONENT_RECIPE_FILE |
      | aws.greengrass.Cli | LATEST                    |
    And I deploy the Greengrass deployment configuration
    Then the Greengrass deployment is COMPLETED on the device after 180 seconds
    # Update component state accordingly. Possible states: {RUNNING, FINISHED, BROKEN, STOPPING}
    Then I verify the GDK_COMPONENT_NAME component is RUNNING using the greengrass-cli
    When I restart Greengrass
    Then I wait 5 seconds
    # Update component state accordingly. Possible states: {RUNNING, FINISHED, BROKEN, STOPPING}
    And I verify the GDK_COMPONENT_NAME component is RUNNING using the greengrass-cli

  @ConfidenceTest
  Scenario: As a Developer, I can deploy GDK_COMPONENT_NAME to my device and see it is working as expected after an additional local deployment to change component configuration
    When I create a Greengrass deployment with components
      | GDK_COMPONENT_NAME | GDK_COMPONENT_RECIPE_FILE |
      | aws.greengrass.Cli | LATEST                    |
    And I deploy the Greengrass deployment configuration
    Then the Greengrass deployment is COMPLETED on the device after 180 seconds
    # Update component state accordingly. Possible states: {RUNNING, FINISHED, BROKEN, STOPPING}.
    Then I verify the GDK_COMPONENT_NAME component is RUNNING using the greengrass-cli
    # Update the MERGE deployment configuration with the intended key-value pairs.
    And I update my local deployment configuration, setting the component GDK_COMPONENT_NAME configuration to:
      """
        {
          "MERGE": {
            "configurationKey": "keyValue"
          }
        }
      """
    # Update component state accordingly. Possible states: {RUNNING, FINISHED, BROKEN, STOPPING}. Additional verification steps may be added here too.
    Then I verify the GDK_COMPONENT_NAME component is RUNNING using the greengrass-cli

  @ConfidenceTest
  Scenario: As a Developer, I can deploy GDK_COMPONENT_NAME to my device with configuration updates and see it is working as expected
    When I create a Greengrass deployment with components
      | GDK_COMPONENT_NAME | GDK_COMPONENT_RECIPE_FILE |
      | aws.greengrass.Cli | LATEST                    |
    # Update the MERGE deployment configuration with the intended key-value pairs.
    And I update my Greengrass deployment configuration, setting the component GDK_COMPONENT_NAME configuration to:
      """
        {
          "MERGE": {
            "configurationKey": "keyValue"
          }
        }
      """
    And I deploy the Greengrass deployment configuration
    Then the Greengrass deployment is COMPLETED on the device after 180 seconds
    # Update component state accordingly. Possible states: {RUNNING, FINISHED, BROKEN, STOPPING}. Additional verification steps may be added here too.
    Then I verify the GDK_COMPONENT_NAME component is RUNNING using the greengrass-cli
    