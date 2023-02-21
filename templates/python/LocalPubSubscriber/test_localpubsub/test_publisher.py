import pytest
from localpubsub import publisher
from awsiot.greengrasscoreipc.model import PublishMessage, BinaryMessage

@pytest.fixture
def ipc_client(mocker):
    mocker.patch("awsiot.greengrasscoreipc.clientv2.GreengrassCoreIPCClientV2")
    mocker.patch("awsiot.greengrasscoreipc.connect")
    from awsiot.greengrasscoreipc.clientv2 import GreengrassCoreIPCClientV2

    client = GreengrassCoreIPCClientV2()
    return client


def test_publish_message_N_times(ipc_client):
    publisher.publish_message_N_times(ipc_client, "topic", "World")
    assert ipc_client.publish_to_topic.call_count == 10
    ipc_client.publish_to_topic.assert_called_with(topic='topic', publish_message=PublishMessage(binary_message=BinaryMessage(message=b'World')))


def test_publish_message_N_times_5(ipc_client):
    publisher.publish_message_N_times(ipc_client, "topic", "World", 5)
    assert ipc_client.publish_to_topic.call_count == 5
    ipc_client.publish_to_topic.assert_called_with(topic='topic', publish_message=PublishMessage(binary_message=BinaryMessage(message=b'World')))
