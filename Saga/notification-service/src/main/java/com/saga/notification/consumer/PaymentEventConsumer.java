package com.saga.notification.consumer;


import com.saga.shared.event.PaymentProcessedEvent;
import com.saga.shared.event.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(PaymentEventConsumer.class);

    @KafkaListener(topics = "payment-processed", groupId = "notification-group")
    public void consumePaymentProcessed(PaymentProcessedEvent event) {
        logger.info("Received PaymentProcessedEvent for orderId: {}, status: {}", event.getOrderId(), event.getPaymentStatus());
        // Simulate notification (e.g., email)
        String message;
        if ("SUCCESS".equals(event.getPaymentStatus())) {
            message = String.format("Order %d: Payment successful! Sending confirmation email...", event.getOrderId());
            logger.info(message);
            // Simulate sending email
            logger.info("Confirmation email sent for orderId: {}", event.getOrderId());
        } else {
            message = String.format("Order %d: Payment failed! Sending failure notification...", event.getOrderId());
            logger.warn(message);
            // Simulate sending failure notification
            logger.info("Failure notification sent for orderId: {}", event.getOrderId());
        }
        logger.info("Notification process completed for orderId: {}", event.getOrderId());
    }
}
