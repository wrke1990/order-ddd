package com.example.order.domain.model.event;

/**
 * 领域事件发布接口
 */
public interface DomainEventPublisher {
    
    /**
     * 发布领域事件
     * @param event 领域事件
     */
    void publish(DomainEvent event);
    
    /**
     * 批量发布领域事件
     * @param events 领域事件列表
     */
    void publishAll(Iterable<DomainEvent> events);
}
