package rpc;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class RPCServer {
	public static final String RPC_QUEUE_NAME = "rpc_queue";
	public static String sayHello(String name) {
		return "hello " + name;
	}
	public static void main(String[] args) throws Exception {
		ConnectionFactory connFac = new ConnectionFactory();
		connFac.setHost("localhost");
		Connection conn = connFac.newConnection();
		Channel channel = conn.createChannel();
		channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
		while (true) {
			System.out.println("服务端等待接收消息..");
			Delivery deliver = consumer.nextDelivery();
			System.out.println("服务端成功收到消息..");
			BasicProperties props = deliver.getProperties();
			String message = new String(deliver.getBody(), "UTF-8");
			String responseMessage = sayHello(message);
			BasicProperties responseProps = new BasicProperties.Builder()
					.correlationId(props.getCorrelationId()).build();
			// 将结果返回到客户端Queue
			channel.basicPublish("", props.getReplyTo(), responseProps,
					responseMessage.getBytes("UTF-8"));
			// 向客户端确认消息
			channel.basicAck(deliver.getEnvelope().getDeliveryTag(), false);
			System.out.println("服务端返回消息完成..");
		}

	}

}
