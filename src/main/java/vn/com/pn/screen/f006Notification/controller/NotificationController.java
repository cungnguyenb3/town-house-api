//package vn.com.pn.screen.f006Notification.controller;
//
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.web.bind.annotation.RestController;
//import vn.com.pn.screen.f006Notification.entity.Greeting;
//import vn.com.pn.screen.f006Notification.entity.Message;
//
//@RestController
//public class NotificationController {
//
//    @MessageMapping("/notification")
//    @SendTo("/topic/greetings")
//    public Greeting sayGreeting (Message message) throws Exception{
//        Thread.sleep(1000);
//        return new Greeting(message.getTitle(), message.getName());
//    }
//}
