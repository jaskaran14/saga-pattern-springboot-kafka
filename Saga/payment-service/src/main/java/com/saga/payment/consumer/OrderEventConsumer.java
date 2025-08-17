package com.saga.payment.consumer;

import com.saga.shared.event.OrderCreatedEvent;
import com.saga.shared.event.PaymentProcessedEvent;
import com.saga.payment.producer.PaymentFailedEventProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OrderEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventConsumer.class);
    private final KafkaTemplate<String, PaymentProcessedEvent> kafkaTemplate;
    private final PaymentFailedEventProducer paymentFailedEventProducer;

    @KafkaListener(topics = "order-created", groupId = "payment-group")
    public void consumeOrderCreated(OrderCreatedEvent event) {
        logger.info("Received OrderCreatedEvent for orderId: {}", event.getOrderId());
        // Simulate payment processing with random success/failure
        logger.info("Processing payment for orderId: {}", event.getOrderId());
        Random random = new Random();
        boolean paymentSuccess = random.nextBoolean();
       //Simulates random payment success/failure,
        String status = paymentSuccess ? "SUCCESS" : "FAILED";
        try {
            // Simulate processing time
            Thread.sleep(1000 + random.nextInt(2000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Send payment processed event
        PaymentProcessedEvent paymentEvent = new PaymentProcessedEvent(event.getOrderId(), status);
        if (paymentSuccess) {
            logger.info("Payment processed for orderId: {}, status: {}", event.getOrderId(), status);
            kafkaTemplate.send("payment-processed", paymentEvent);
            logger.info("Published PaymentProcessedEvent for orderId: {} with status: {}", event.getOrderId(), status);
        } else {
            logger.warn("Payment failed for orderId: {}! Publishing PaymentFailedEvent.", event.getOrderId());
            paymentFailedEventProducer.publishPaymentFailed(event.getOrderId(), "Payment processing failed");
        }
    }
}
