import sys
import traceback

from awsiot.greengrasscoreipc.clientv2 import GreengrassCoreIPCClientV2
from awsiot.greengrasscoreipc.model import SubscriptionResponseMessage


def subscribe_to_topic(ipc_client: GreengrassCoreIPCClientV2, topic):
    return ipc_client.subscribe_to_topic(
        topic=topic, on_stream_event=_on_stream_event, on_stream_error=_on_stream_error, on_stream_closed=_on_stream_closed
    )


def _on_stream_event(event: SubscriptionResponseMessage) -> None:
    try:
        message = str(event.binary_message.message, "utf-8")
        topic = event.binary_message.context.topic
        print("Received new message on topic %s: %s" % (topic, message))
    except Exception as e:
        print("Exception occurred: " + str(e))
        traceback.print_exc()


def _on_stream_error(error: Exception) -> bool:
    print("Received a stream error.", file=sys.stderr)
    traceback.print_exc()
    return False  # Return True to close stream, False to keep stream open.


def _on_stream_closed() -> None:
    print("Subscribe to topic stream closed.")
