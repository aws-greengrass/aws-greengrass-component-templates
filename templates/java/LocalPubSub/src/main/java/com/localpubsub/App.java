package com.localpubsub;

import software.amazon.awssdk.aws.greengrass.GreengrassCoreIPCClientV2;
import software.amazon.awssdk.aws.greengrass.SubscribeToTopicResponseHandler;
import software.amazon.awssdk.aws.greengrass.model.SubscribeToTopicResponse;
import software.amazon.awssdk.aws.greengrass.model.UnauthorizedError;

import java.io.IOException;
import java.util.Arrays;

public class App {
    private  final GreengrassCoreIPCClientV2 ipcClient ;

    public App() throws IOException {
        ipcClient = GreengrassCoreIPCClientV2.builder().build();
    }

    public static void main(String[] args) {
        String topic = args[0];
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        try  {
            App app = new App();
            // Subscribe to the topic
            Subscriber subscriber = new Subscriber(app.ipcClient);
            GreengrassCoreIPCClientV2.StreamingResponse<SubscribeToTopicResponse,
                    SubscribeToTopicResponseHandler> response = subscriber.subscribe(topic);

            // Publish messages to the topic
            Publisher publisher = new Publisher(app.ipcClient);
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
