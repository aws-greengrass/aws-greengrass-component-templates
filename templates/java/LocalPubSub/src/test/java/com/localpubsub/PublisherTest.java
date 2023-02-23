package com.localpubsub;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.aws.greengrass.GreengrassCoreIPCClientV2;
import software.amazon.awssdk.aws.greengrass.model.BinaryMessage;
import software.amazon.awssdk.aws.greengrass.model.PublishMessage;
import software.amazon.awssdk.aws.greengrass.model.PublishToTopicRequest;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PublisherTest {
    private final GreengrassCoreIPCClientV2 clientV2 = mock(GreengrassCoreIPCClientV2.class);
    String testTopics = "test/topic";
    String testMessage = "test message";

    @Test
    public void when_Publisher_publishMessageToTopicNTimes_then_Publish() throws InterruptedException {
        Publisher publisher = new Publisher(clientV2);
        publisher.publishMessageToTopicNTimes(testTopics,testMessage,5);
        PublishToTopicRequest publishToTopicRequest = new PublishToTopicRequest();
        PublishMessage publishMessage = new PublishMessage().withBinaryMessage(
                new BinaryMessage().withMessage(testMessage.getBytes()));
        publishToTopicRequest.setTopic(testTopics);
        publishToTopicRequest.setPublishMessage(publishMessage);
        verify(clientV2, times(5)).publishToTopic(publishToTopicRequest);
    }
}
