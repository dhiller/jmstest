package de.dhiller.jmstest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.util.FileSystemUtils;

import java.io.File;

import javax.jms.ConnectionFactory;

@SpringBootApplication
@EnableJms
public class JmsTestApplication implements CommandLineRunner {

    public static final String DESTINATION_NAME = "mailbox-destination";

    private static final Logger log = LoggerFactory.getLogger(JmsTestApplication.class);

    private static final MessageCreator MESSAGE_CREATOR = session -> session.createTextMessage("ping!");

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory simpleJmsListenerContainerFactory = new SimpleJmsListenerContainerFactory();
        simpleJmsListenerContainerFactory.setConnectionFactory(connectionFactory);
        simpleJmsListenerContainerFactory.setPubSubDomain(true);
        return simpleJmsListenerContainerFactory;
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setPubSubDomain(true);
        simpleMessageListenerContainer.setDestinationName(DESTINATION_NAME);
        simpleMessageListenerContainer.setAutoStartup(false);
        return simpleMessageListenerContainer;
    }

    public static void main(String[] args) {
        cleanQueueData();
        SpringApplication.run(JmsTestApplication.class, args);
    }

    private static void cleanQueueData() {
        FileSystemUtils.deleteRecursively(new File("activemq-data"));
    }

    @Override
    public void run(String... strings) throws Exception {

        int numberOfMessagesToSend = 3;
        try {
            while (numberOfMessagesToSend-- > 0) {
                log.info("Sending a new message.");
                jmsTemplate.send(DESTINATION_NAME, MESSAGE_CREATOR);
                log.info("Sleeping....");
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            applicationContext.close();
            cleanQueueData();
        }

    }
}
