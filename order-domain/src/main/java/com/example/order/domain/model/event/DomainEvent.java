package com.example.order.domain.model.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 领域事件基础类
 */
public abstract class DomainEvent implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final LocalDateTime occurredTime;
    private final String eventId;
    
    protected DomainEvent() {
        this.occurredTime = LocalDateTime.now();
        this.eventId = java.util.UUID.randomUUID().toString();
    }
    
    public LocalDateTime getOccurredTime() {
        return occurredTime;
    }
    
    public String getEventId() {
        return eventId;
    }
    
    public abstract String getEventType();
}
