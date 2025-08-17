package com.saga.notification.consumer;

import com.saga.shared.event.OrderFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderFailedEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(OrderFailedEventConsumer.class);

    @KafkaListener(topics = "order-failed", groupId = "notification-group")
    public void consumeOrderFailed(OrderFailedEvent event) {
        logger.warn("Order failed for orderId: {}. Reason: {}. Notifying user.", event.getOrderId(), event.getReason());
        // Simulate sending failure notification
        logger.info("Failure notification sent for orderId: {} with reason: {}", event.getOrderId(), event.getReason());
    }
}
