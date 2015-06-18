package rpc;
import java.util.UUID;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
public class RPCClient {
	public static final String RPC_QUEUE_NAME = "rpc_queue";
	public static void main(String[] args) throws Exception {
		ConnectionFactory connFac = new ConnectionFactory();
		connFac.setHost("localhost");
		Connection conn = connFac.newConnection();
		Channel channel = conn.createChannel();
		// 响应QueueName ，服务端将会把要返回的信息发送到该Queue
		String responseQueue = channel.queueDeclare().getQueue();
		String correlationId = UUID.randomUUID().toString();
		BasicProperties props = new BasicProperties.Builder()
				.replyTo(responseQueue).correlationId(correlationId).build();
		String message = "is_zhoufeng";
		channel.basicPublish("", RPC_QUEUE_NAME, props,
				message.getBytes("UTF-8"));
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(responseQueue, consumer);
		while (true) {
			Delivery delivery = consumer.nextDelivery();
			if (delivery.getProperties().getCorrelationId()
					.equals(correlationId)) {
				String result = new String(delivery.getBody());
				System.out.println(result);
			}
		}
	}
}
