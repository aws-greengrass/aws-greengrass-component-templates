from unittest.mock import call

import pytest
from awsiot.greengrasscoreipc.model import BinaryMessage, PublishMessage
from localpubsub import publisher


@pytest.fixture
def ipc_client(mocker):
    mocker.patch("awsiot.greengrasscoreipc.clientv2.GreengrassCoreIPCClientV2")
    mocker.patch("awsiot.greengrasscoreipc.connect")
    from awsiot.greengrasscoreipc.clientv2 import GreengrassCoreIPCClientV2

    client = GreengrassCoreIPCClientV2()
    return client


def test_publish_message_N_times(ipc_client):
    publisher.publish_message_N_times(ipc_client, "topic", "World")
    args = call(topic="topic", publish_message=PublishMessage(binary_message=BinaryMessage(message=b"World")))
    call_count_with_args = [args] * 10
    assert ipc_client.publish_to_topic.call_args_list == call_count_with_args


def test_publish_message_N_times_5(ipc_client):
    publisher.publish_message_N_times(ipc_client, "topic", "World", 5)
    args = call(topic="topic", publish_message=PublishMessage(binary_message=BinaryMessage(message=b"World")))
    call_count_with_args = [args] * 5
    assert ipc_client.publish_to_topic.call_args_list == call_count_with_args
