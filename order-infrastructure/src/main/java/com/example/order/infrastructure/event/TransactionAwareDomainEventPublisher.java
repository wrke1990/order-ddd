package com.example.order.infrastructure.event;

import com.example.order.domain.model.event.DomainEvent;
import com.example.order.domain.model.event.DomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 事务感知的领域事件发布器
 * 确保领域事件在事务提交后发布，保证数据一致性
 */
@Component
public class TransactionAwareDomainEventPublisher implements DomainEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(TransactionAwareDomainEventPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public TransactionAwareDomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(DomainEvent event) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            // 如果当前有活跃事务，将事件添加到事务同步器中，在事务提交后发布
            logger.info("当前有活跃事务，将事件 {} 加入事务同步器", event);
            EventPublishingTransactionSynchronization transactionSynchronization =
                    getTransactionSynchronization(TransactionSynchronizationManager.getCurrentTransactionName());
            transactionSynchronization.addEvent(event);
        } else {
            // 如果没有活跃事务，立即发布事件
            logger.info("当前没有活跃事务，立即发布事件: {}", event);
            doPublish(event);
        }
    }

    @Override
    public void publishAll(Iterable<DomainEvent> events) {
        if (events == null) {
            return;
        }

        logger.info("准备发布领域事件列表");
        events.forEach(this::publish);
    }

    /**
     * 从ThreadLocal中获取或创建事务同步器
     */
    private EventPublishingTransactionSynchronization getTransactionSynchronization(String transactionName) {
        ConcurrentMap<String, EventPublishingTransactionSynchronization> synchronizations =
                TransactionSynchronizationManager.getResource(this) instanceof ConcurrentMap ?
                        (ConcurrentMap<String, EventPublishingTransactionSynchronization>) TransactionSynchronizationManager.getResource(this) :
                        new ConcurrentHashMap<>();

        // 如果ThreadLocal中没有资源，初始化并绑定
        if (!TransactionSynchronizationManager.hasResource(this)) {
            TransactionSynchronizationManager.bindResource(this, synchronizations);
        }

        // 获取或创建事务同步器
        return synchronizations.computeIfAbsent(transactionName != null ? transactionName : "default",
                name -> {
                    EventPublishingTransactionSynchronization synchronization = new EventPublishingTransactionSynchronization();
                    TransactionSynchronizationManager.registerSynchronization(synchronization);
                    return synchronization;
                });
    }

    /**
     * 实际发布事件的方法
     */
    @Async("eventPublicationExecutor")
    private void doPublish(DomainEvent event) {
        try {
            applicationEventPublisher.publishEvent(event);
            logger.info("成功异步发布领域事件: {}", event);
        } catch (Exception e) {
            logger.error("异步发布领域事件失败: {}", event, e);
            // 可以考虑将失败的事件存入数据库，以便后续重试
            throw e;
        }
    }

    /**
     * 事务同步器，负责在事务提交后发布事件
     */
    private class EventPublishingTransactionSynchronization implements TransactionSynchronization {

        private final List<DomainEvent> events = new ArrayList<>();

        public void addEvent(DomainEvent event) {
            events.add(event);
        }

        @Override
        public void afterCommit() {
            logger.info("事务提交后，开始发布 {} 个领域事件", events.size());
            events.forEach(TransactionAwareDomainEventPublisher.this::doPublish);
        }

        @Override
        public void afterCompletion(int status) {
            // 事务完成后清理资源
            logger.info("事务完成，清理事务同步器资源");
            ConcurrentMap<String, EventPublishingTransactionSynchronization> synchronizations =
                    (ConcurrentMap<String, EventPublishingTransactionSynchronization>) TransactionSynchronizationManager.getResource(TransactionAwareDomainEventPublisher.this);
            if (synchronizations != null) {
                String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
                synchronizations.remove(transactionName != null ? transactionName : "default");
                if (synchronizations.isEmpty()) {
                    TransactionSynchronizationManager.unbindResource(TransactionAwareDomainEventPublisher.this);
                }
            }
        }
        
        // 实现 TransactionSynchronization 接口的其他必要方法
        @Override
        public void suspend() {}

        @Override
        public void resume() {}

        @Override
        public void flush() {}

        @Override
        public void beforeCommit(boolean readOnly) {}

        @Override
        public void beforeCompletion() {}
    }
}