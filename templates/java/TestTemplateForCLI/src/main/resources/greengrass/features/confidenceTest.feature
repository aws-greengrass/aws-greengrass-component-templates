Feature: Confidence Test suite

  Background:
    Given my device is registered as a Thing
    And my device is running Greengrass

  @ConfidenceTest
  Scenario: As a Developer, I can deploy GDK_COMPONENT_NAME, stop it and start it again
    When I create a Greengrass deployment with components
      | GDK_COMPONENT_NAME | GDK_COMPONENT_RECIPE_FILE |
      | aws.greengrass.Cli | LATEST                    |
    And I deploy the Greengrass deployment configuration
    Then the Greengrass deployment is COMPLETED on the device after 180 seconds
    # Update component state accordingly. Possible states: {RUNNING, FINISHED, BROKEN, STOPPING}
    Then I verify the GDK_COMPONENT_NAME component is RUNNING using the greengrass-cli
    When I use greengrass-cli to stop the component GDK_COMPONENT_NAME
    # Update component state accordingly. Possible states: {RUNNING, FINISHED, BROKEN, STOPPING}
    Then I verify the GDK_COMPONENT_NAME component is FINISHED using the greengrass-cli
    When I use greengrass-cli to restart the component GDK_COMPONENT_NAME
    # Update component state accordingly. Possible states: {RUNNING, FINISHED, BROKEN, STOPPING}
    Then I verify the GDK_COMPONENT_NAME component is RUNNING using the greengrass-cli

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
  Scenario: As a Developer, I can deploy GDK_COMPONENT_NAME and disable internet to check if component is working. Restore internet to check if component is still working
    When I create a Greengrass deployment with components
      | GDK_COMPONENT_NAME | GDK_COMPONENT_RECIPE_FILE |
      | aws.greengrass.Cli | LATEST                    |
    And I deploy the Greengrass deployment configuration
    Then the Greengrass deployment is COMPLETED on the device after 180 seconds
    # Update component state accordingly. Possible states: {RUNNING, FINISHED, BROKEN, STOPPING}
    Then I verify the GDK_COMPONENT_NAME component is RUNNING using the greengrass-cli
    When I set device network connectivity to OFFLINE
    And I restart Greengrass
    Then I wait 5 seconds
    # Update component state accordingly. Possible states: {RUNNING, FINISHED, BROKEN, STOPPING}
    Then I verify the GDK_COMPONENT_NAME component is RUNNING using the greengrass-cli
    When I set device network connectivity to ONLINE
    # Update component state accordingly. Possible states: {RUNNING, FINISHED, BROKEN, STOPPING}
    Then I verify GDK_COMPONENT_NAME component is RUNNING using the greengrass-cli