## Publish and subscribe local messages

This template serves as example for local communication between Greengrass components over IPC.

In this example, the component uses the local pubsub APIs offered by AWS IoT SDK to send and received messages over topics. 
This `LocalPubSub` component, when deployed on to a core device, publishes and subscribes to the messages on the same topic (/topic/local/pubsub). 

The message and topic values are configurable in the component recipe. If you want to update the topic, the `aws.greengrass.ipc.pubsub` policy under `accessControl` configuration must also be updated to allowlist necessary operations on that topic. 

If the component is successfully deployed, you should see in the component logs (/greengrass/v2/logs/com.example.PythonLocalPubSub) that the it published and received the message on that topic for 10 times. 

Refer to the AWS IoT Greengrass [public documentation](https://docs.aws.amazon.com/greengrass/v2/developerguide/ipc-publish-subscribe.html) for more information on Greengrass local communication between components.

## Create a new component version using gdk
1. Run the following command to download the LocalPubSub component template. 

```
gdk component init -t LocalPubSub -l python -n LocalPubSub
```

2. Change directory to `LocalPubSub`.

```
cd LocalPubSub
```

3. Update configuration in `gdk-config.json`
   1. Config file `gdk-config.json` would have placeholders:
   ```json
    {
        "component": {
            "com.example.PythonLocalPubSub": {
                "author": "<PLACEHOLDER_AUTHOR>",
                "version": "NEXT_PATCH",
                "build": {
                    "build_system": "zip"
                },
                "publish": {
                    "bucket": "<PLACEHOLDER_BUCKET>",
                    "region": "<PLACEHOLDER_REGION>"
                }
            }
        },
        "gdk_version": "1.2.0"
    }
   ```
   2. Replace `<PLACEHOLDER_AUTHOR>` with your name, `<PLACEHOLDER_BUCKET>` with a s3 bucket name and `<PLACEHOLDER_REGION>` with an aws region.
   3. After replace these value the `gdk-config.json` file should look similar to:
   ```json
    {
        "component": {
            "com.example.PythonLocalPubSub": {
                "author": "J. Doe",
                "version": "NEXT_PATCH",
                "build": {
                    "build_system": "zip"
                },
                "publish": {
                    "bucket": "my-s3-bucket",
                    "region": "us-east-1"
                }
            }
        },
        "gdk_version": "1.2.0"
    }
   ```

4. Build the artifacts and recipes of the component using the following command

```
gdk component build
```

5. Creates new version of the component in your AWS account.

```
gdk component publish
```

## License

Add License information here

