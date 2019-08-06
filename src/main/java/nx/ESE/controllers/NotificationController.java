package nx.ESE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import nx.ESE.dtos.Notification;

@PreAuthorize("hasRole('TEACHER')")
@RestController
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate template;

    // Initialize Notifications
    private Notification notifications = new Notification(0);

    @MessageMapping("/notification")
    public Notification getNotification() {

        // Increment Notification by one
        notifications.increment();

        // Push notifications to front-end
       // template.convertAndSend("/topic/notification", notifications);

        //return "Notifications successfully sent to Angular !";
        return notifications;
    }
}