package com.saga.order.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.saga.shared.event.OrderFailedEvent;

@Component
public class OrderTimeoutWatcher {
    private static final Logger logger = LoggerFactory.getLogger(OrderTimeoutWatcher.class);
    private static final long TIMEOUT_MS = 15000; // 15 seconds
    private static final ConcurrentHashMap<String, Long> pendingOrders = new ConcurrentHashMap<>();

    @Autowired
    private KafkaTemplate<String, OrderFailedEvent> orderFailedKafkaTemplate;

    public static void addPendingOrder(String orderId) {
        pendingOrders.put(orderId, System.currentTimeMillis());
    }

    public static void acknowledge(String orderId) {
        pendingOrders.remove(orderId);
    }

    @Scheduled(fixedDelay = 5000)
    public void checkTimeouts() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String, Long>> it = pendingOrders.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Long> entry = it.next();
            if (now - entry.getValue() > TIMEOUT_MS) {
                String orderId = entry.getKey();
                logger.warn("Timeout waiting for payment ack for orderId: {}. Executing compensation.", orderId);
                OrderFailedEvent event = new OrderFailedEvent(Long.valueOf(orderId), "Timeout waiting for payment");
                orderFailedKafkaTemplate.send("order-failed", event);
                it.remove();
            }
        }
    }
}
