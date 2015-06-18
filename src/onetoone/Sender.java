package onetoone;
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
		// 定义Queue名称
		String queueName = "queue01";
		// 为channel定义queue的属性，queueName为Queue名称
		channel.queueDeclare(queueName, false, false, false, null);
		String msg = "Hello World!";
		// 发送消息
		channel.basicPublish("", queueName, null, msg.getBytes());
		System.out.println("send message[" + msg + "] to " + queueName
				+ " success!");
		channel.close();
		conn.close();
	}

}
