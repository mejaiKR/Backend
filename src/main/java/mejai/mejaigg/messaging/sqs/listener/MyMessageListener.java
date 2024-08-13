package mejai.mejaigg.messaging.sqs.listener;

import org.springframework.messaging.Message;

import io.awspring.cloud.sqs.listener.MessageListener;

public class MyMessageListener implements MessageListener<Object> {

	@Override
	public void onMessage(Message<Object> message) {
		System.out.println("Received message: " + message.getPayload());
		// 메시지 처리 로직 추가
	}
}
