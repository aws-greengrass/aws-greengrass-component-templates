from awsiot.greengrasscoreipc.clientv2 import GreengrassCoreIPCClientV2
from awsiot.greengrasscoreipc.model import BinaryMessage, PublishMessage


def publish_binary_message_to_topic(ipc_client: GreengrassCoreIPCClientV2, topic, message):
    binary_message = BinaryMessage(message=bytes(message, "utf-8"))
    publish_message = PublishMessage(binary_message=binary_message)
    return ipc_client.publish_to_topic(topic=topic, publish_message=publish_message)


def publish_message_N_times(ipc_client, topic, message, N=10):
    for i in range(1, N + 1):
        publish_binary_message_to_topic(ipc_client, topic, message)
        print("Successfully published " + str(i) + " message(s)")
