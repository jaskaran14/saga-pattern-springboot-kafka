package com.saga.order.consumer;

import com.saga.shared.event.OrderFailedEvent;
import com.saga.shared.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentFailedEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(PaymentFailedEventConsumer.class);
    private final KafkaTemplate<String, OrderFailedEvent> orderFailedKafkaTemplate;

    @KafkaListener(topics = "payment-failed", groupId = "order-group")
    public void consumePaymentFailed(PaymentFailedEvent event) {
        logger.warn("Payment failed for orderId: {}. Reason: {}. Rolling back order.", event.getOrderId(), event.getReason());
        OrderFailedEvent orderFailedEvent = new OrderFailedEvent(event.getOrderId(), event.getReason());
        orderFailedKafkaTemplate.send("order-failed", orderFailedEvent);
        logger.info("Published OrderFailedEvent for orderId: {}", event.getOrderId());
    }
}
