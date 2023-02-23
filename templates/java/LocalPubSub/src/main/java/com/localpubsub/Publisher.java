package com.localpubsub;

import software.amazon.awssdk.aws.greengrass.GreengrassCoreIPCClientV2;
import software.amazon.awssdk.aws.greengrass.model.BinaryMessage;
import software.amazon.awssdk.aws.greengrass.model.PublishMessage;
import software.amazon.awssdk.aws.greengrass.model.PublishToTopicRequest;
import software.amazon.awssdk.aws.greengrass.model.PublishToTopicResponse;

import java.nio.charset.StandardCharsets;

public class Publisher {
    private final GreengrassCoreIPCClientV2 clientV2;

    public Publisher(GreengrassCoreIPCClientV2 greengrassCoreIPCClientV2) {
        this.clientV2 = greengrassCoreIPCClientV2;

    }

    private PublishToTopicResponse publishBinaryMessageToTopic(String topic, String message) throws InterruptedException {
        BinaryMessage binaryMessage = new BinaryMessage().withMessage(message.getBytes(StandardCharsets.UTF_8));
        PublishMessage publishMessage = new PublishMessage().withBinaryMessage(binaryMessage);
        PublishToTopicRequest publishToTopicRequest = new PublishToTopicRequest().withTopic(topic)
                .withPublishMessage(publishMessage);
        return clientV2.publishToTopic(publishToTopicRequest);
    }

    public void publishMessageToTopicNTimes(
            String topic, String message, int n)
            throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            publishBinaryMessageToTopic(topic, message);
            System.out.println("Successfully published " + i + " message(s) on the topic: " + topic);
        }
    }
}
