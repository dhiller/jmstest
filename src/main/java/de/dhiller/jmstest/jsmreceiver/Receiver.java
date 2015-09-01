package de.dhiller.jmstest.jsmreceiver;

import de.dhiller.jmstest.JmsTestApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    private static final Logger log = LoggerFactory.getLogger(Receiver.class);

    @JmsListener(destination = JmsTestApplication.DESTINATION_NAME)
    public void receiveMessage(String message, @Header("id") String messageId) {
        log.info("Received message with id {}: {}", messageId, message);
    }

}