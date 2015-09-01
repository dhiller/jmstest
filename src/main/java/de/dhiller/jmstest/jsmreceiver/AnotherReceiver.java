package de.dhiller.jmstest.jsmreceiver;

import de.dhiller.jmstest.JmsTestApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AnotherReceiver {
    private static final Logger log = LoggerFactory.getLogger(AnotherReceiver.class);

    @JmsListener(destination = JmsTestApplication.DESTINATION_NAME)
    public void handleMessage(String message, @Headers Map<String,Object> headers) {
        log.info("Message received: {}, message headers: {}", message, headers);
    }
}
