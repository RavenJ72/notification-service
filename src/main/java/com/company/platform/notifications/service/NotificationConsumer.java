package com.company.platform.notifications.service;

import com.company.platform.notifications.config.RabbitMQConfig;
import com.company.platform.notifications.models.notifications.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    @RabbitListener(queues = RabbitMQConfig.NOTIFICATIONS_QUEUE)
    public void handleNotification(Notification notification) {
        String destination = "/topic/notifications/" + notification.getRecipientId();

        switch (notification.getType()) {
            case NEW_MESSAGE:
                handleNewMessage(notification, destination);
                break;
            case TICKET_ASSIGNED:
                handleTicketAssigned(notification, destination);
                break;
            case VIOLATION_RECORDED:
                handleViolation(notification, destination);
                break;
        }
    }
    private void handleNewMessage(Notification notification, String destination) {
        log.info("Sending NEW_MESSAGE: {}", notification);
        messagingTemplate.convertAndSend(destination, notification);
    }
    private void handleTicketAssigned(Notification notification, String destination) {
        log.info("Sending TICKET_ASSIGNED: {}", notification);
        messagingTemplate.convertAndSend(destination, notification);
    }
    private void handleViolation(Notification notification, String destination) {
        log.info("Sending VIOLATION_RECORDED: {}", notification);
        messagingTemplate.convertAndSend(destination, notification);
    }
}

