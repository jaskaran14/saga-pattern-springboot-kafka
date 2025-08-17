package com.saga.order.consumer;

import com.saga.shared.event.PaymentProcessedEvent;
import com.saga.order.event.OrderTimeoutWatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumes PaymentProcessedEvent messages from Kafka and acknowledges orders for timeout tracking.
 */
@Component
public class PaymentProcessedEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(PaymentProcessedEventConsumer.class);

    /**
     * Listens for PaymentProcessedEvent messages and acknowledges the order.
     * @param record the Kafka consumer record containing the event
     */
    @KafkaListener(topics = "payment-processed") // groupId from properties
    public void listen(ConsumerRecord<String, PaymentProcessedEvent> record) {
        try {
            PaymentProcessedEvent event = record.value();
            if (event == null) {
                logger.warn("Received null PaymentProcessedEvent record");
                return;
            }
            logger.info("Received PaymentProcessedEvent for orderId: {}", event.getOrderId());
            OrderTimeoutWatcher.acknowledge(event.getOrderId().toString());
            // Update order status in DB if needed
        } catch (Exception ex) {
            logger.error("Error processing PaymentProcessedEvent: {}", ex.getMessage(), ex);
        }
    }
}
