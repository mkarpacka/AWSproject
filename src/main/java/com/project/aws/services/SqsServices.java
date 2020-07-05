package com.project.aws.services;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SqsServices {

    @Autowired
    AmazonSQS sqs;

    @Value("${sqs_queue_url}")
    private String queueUrl;

    public String sendMessageFromQueue() {
        String myMessage = "Hello World form epsilon";

        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("AttributeOne", new MessageAttributeValue()
                .withStringValue("This is an attribute")
                .withDataType("String"));

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(myMessage)
                .withDelaySeconds(5)
                .withMessageAttributes(messageAttributes);

        sqs.sendMessage(send_msg_request);
        return myMessage;
    }

    public List<Message> getMessageToQueue() {
        List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();

        for (Message m : messages) {
            System.out.println(m.getBody());
        }
        return messages;
    }
}
