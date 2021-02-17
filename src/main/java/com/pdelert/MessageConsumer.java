package com.pdelert;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class MessageConsumer {

    @Incoming("generic-events")
    public CompletionStage<Void> consume(Message<String> message) {
        System.out.printf("Consuming message %s", message.getPayload());
        return message.nack(new RuntimeException());
    }

    @Incoming("ctl-inbound-generic-dlq")
    public CompletionStage<Void> handleDlq(Message<String> message) {
        System.out.printf("Consuming dlq message %s", message.getPayload());
        return message.ack();
    }
}
