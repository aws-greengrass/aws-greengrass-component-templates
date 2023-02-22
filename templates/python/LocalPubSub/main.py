import sys
import traceback

from awsiot.greengrasscoreipc.clientv2 import GreengrassCoreIPCClientV2
from localpubsub import publisher, subscriber


def main():
    args = sys.argv[1:]
    topic = args[0]
    message = " ".join(args[1:])

    try:
        ipc_client = GreengrassCoreIPCClientV2()
        # Subscribe to the topic before publishing
        subscriber.subscribe_to_topic(ipc_client, topic)
        # Publish a message for N times and exit
        publisher.publish_message_N_times(ipc_client, topic, message)
    except Exception:
        print("Exception occurred", file=sys.stderr)
        traceback.print_exc()
        exit(1)


if __name__ == "__main__":
    main()
