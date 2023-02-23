package com.localpubsub;

import software.amazon.awssdk.aws.greengrass.GreengrassCoreIPCClientV2;
import software.amazon.awssdk.aws.greengrass.SubscribeToTopicResponseHandler;
import software.amazon.awssdk.aws.greengrass.model.SubscribeToTopicResponse;
import software.amazon.awssdk.aws.greengrass.model.UnauthorizedError;

import java.util.Arrays;

public class App {
    private static GreengrassCoreIPCClientV2 ipcClient;

    public static void main(String[] args) {
        String topic = args[0];
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        try {
            ipcClient = GreengrassCoreIPCClientV2.builder().build();
            // Subscribe to the topic
            Subscriber subscriber = new Subscriber(App.ipcClient);
            GreengrassCoreIPCClientV2.StreamingResponse<SubscribeToTopicResponse,
                    SubscribeToTopicResponseHandler> response = subscriber.subscribe(topic);

            // Publish messages to the topic
            Publisher publisher = new Publisher(App.ipcClient);
            publisher.publishMessageToTopicNTimes(topic, message, 10);

            // To stop subscribing, close the stream.
            response.getHandler().closeStream();
        } catch (Exception e) {
            if (e.getCause() instanceof UnauthorizedError) {
                System.err.println("Unauthorized error while publishing to topic: " + topic);
            } else {
                System.err.println("Exception occurred when using IPC.");
            }
            e.printStackTrace();
            System.exit(1);
        }
    }
}

