package com.project.aws.services;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SqsServices {

    @Autowired
    AmazonSQS sqs;

    @Autowired
    ImageTransformServices imageTransformServices;

    @Autowired
    S3Services s3Services;

    @Value("${sqs_queue_url}")
    private String queueUrl;

    public void sendMessagesToQueue(String[] nameOfFiles) {
        for (String fileName : nameOfFiles) {
            Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
            try {
                messageAttributes.put("AttributeOne", new MessageAttributeValue()
                        .withStringValue("This is an attribute")
                        .withDataType("String"));

                SendMessageRequest send_msg_request = new SendMessageRequest()
                        .withQueueUrl(queueUrl)
                        .withMessageBody(fileName)
                        .withDelaySeconds(5)
                        .withMessageAttributes(messageAttributes);

                sqs.sendMessage(send_msg_request);
                System.out.println("workin");

            } catch (Exception e) {
            }
        }
    }


    public List<Message> getMessageToQueue() {
        List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();

//        for (Message m : messages) {
//            System.out.println(m.getBody());
//            try {
//                //s3Services.uploadFile(imageTransformServices.saveGraphicAsImage(m.getBody()));
//                sqs.deleteMessage(queueUrl, m.getReceiptHandle());
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println("error in getmessagetoqueue");
//            }
//        }
        return messages;
    }
}
