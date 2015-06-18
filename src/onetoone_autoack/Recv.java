package onetoone_autoack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Recv {
	public static void main(String[] args) throws Exception {
		ConnectionFactory connFac = new ConnectionFactory();
		connFac.setHost("127.0.0.1");
		Connection conn = connFac.newConnection();
		Channel channel = conn.createChannel();
		String channelName = "channel01";
		channel.queueDeclare(channelName, false, false, false, null);
		// 配置好获取消息的方式
		QueueingConsumer consumer = new QueueingConsumer(channel);
		// 取消 autoAck
		boolean autoAck = false;
		channel.basicConsume(channelName, autoAck, consumer);
		// 循环获取消息
		while (true) {
			// 获取消息，如果没有消息，这一步将会一直阻塞
			Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			// 确认消息，已经收到
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			// 一旦将autoAck关闭之后，一定要记得处理完消息之后，向服务器确认消息。
			// 否则服务器将会一直转发该消息如果将上面的
			// channel.basicAck(delivery.getEnvelope().getDeliveryTag(),
			// false);注释掉， Sender03.java只需要运行一次 ，
			// Recv每次运行将都会收到HelloWorld消息
			System.out.println("received message[" + msg + "] from "
					+ channelName);
		}
	}
}
