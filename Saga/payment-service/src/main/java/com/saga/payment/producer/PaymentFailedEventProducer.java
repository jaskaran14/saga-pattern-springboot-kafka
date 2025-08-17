package com.saga.payment.producer;

import com.saga.shared.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentFailedEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(PaymentFailedEventProducer.class);
    private final KafkaTemplate<String, PaymentFailedEvent> paymentFailedKafkaTemplate;

    public void publishPaymentFailed(Long orderId, String reason) {
        PaymentFailedEvent event = new PaymentFailedEvent(orderId, reason);
        paymentFailedKafkaTemplate.send("payment-failed", event);
        logger.warn("Published PaymentFailedEvent for orderId: {} with reason: {}", orderId, reason);
    }
}
