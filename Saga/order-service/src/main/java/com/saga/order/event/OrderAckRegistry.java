package com.saga.order.event;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

public class OrderAckRegistry {
    private static final ConcurrentHashMap<String, ScheduledFuture<?>> ackTimeouts = new ConcurrentHashMap<>();

    public static void register(String orderId, ScheduledFuture<?> future) {
        ackTimeouts.put(orderId, future);
    }

    public static void acknowledge(String orderId) {
        ScheduledFuture<?> future = ackTimeouts.remove(orderId);
        if (future != null) {
            future.cancel(false);
        }
    }

    public static boolean isPending(String orderId) {
        return ackTimeouts.containsKey(orderId);
    }
}
