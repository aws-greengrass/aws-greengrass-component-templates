package com.localpubsub;

import software.amazon.awssdk.aws.greengrass.GreengrassCoreIPCClientV2;
import software.amazon.awssdk.aws.greengrass.model.BinaryMessage;
import software.amazon.awssdk.aws.greengrass.model.SubscribeToTopicRequest;
import software.amazon.awssdk.aws.greengrass.model.SubscriptionResponseMessage;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class Subscriber {
    private final GreengrassCoreIPCClientV2 clientV2;

    public Subscriber(GreengrassCoreIPCClientV2 greengrassCoreIPCClientV2) {
        this.clientV2 = greengrassCoreIPCClientV2;

    }

    public GreengrassCoreIPCClientV2.StreamingResponse subscribe(String topic) throws InterruptedException {
        SubscribeToTopicRequest request = new SubscribeToTopicRequest().withTopic(topic);
        return clientV2.subscribeToTopic(request, this::onStreamEvent,
                        Optional.of(this::onStreamError),
                        Optional.of(this::onStreamClosed));
    }

    private void onStreamEvent(SubscriptionResponseMessage subscriptionResponseMessage) {
        try {
            BinaryMessage binaryMessage = subscriptionResponseMessage.getBinaryMessage();
            String message = new String(binaryMessage.getMessage(), StandardCharsets.UTF_8);
            String topic = binaryMessage.getContext().getTopic();
            System.out.printf("Received new message on topic %s: %s%n", topic, message);
        } catch (Exception e) {
            System.err.println("Exception occurred while processing subscription response " +
                    "message.");
            e.printStackTrace();
        }
    }

    private boolean onStreamError(Throwable error) {
        System.err.println("Received a stream error.");
        error.printStackTrace();
        return false; // Return true to close stream, false to keep stream open.
    }

    private void onStreamClosed() {
        System.out.println("Subscribe to topic stream closed.");
    }
}
