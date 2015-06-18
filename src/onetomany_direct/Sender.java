package onetomany_direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {

	public static void main(String[] args) throws Exception {
		ConnectionFactory connFac = new ConnectionFactory();
		// RabbitMQ-Server安装在本机，所以直接用127.0.0.1
		connFac.setHost("127.0.0.1");
		// 创建一个连接
		Connection conn = connFac.newConnection();
		// 创建一个渠道
		Channel channel = conn.createChannel();
		String exchangeName = "exchange02";
		String messageType = "type01";
		channel.exchangeDeclare(exchangeName, "direct");
		// 定义Queue名
		String msg = "Hello World!";
		// 发送消息
		channel.basicPublish(exchangeName, messageType, null, msg.getBytes());
		System.out.println("send message[" + msg + "] to " + exchangeName
				+ " success!");
		channel.close();
		conn.close();

	}

}
