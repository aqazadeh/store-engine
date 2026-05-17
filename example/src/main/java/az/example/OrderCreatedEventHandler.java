package az.example;

import az.kon.academy.event.handler.BaseEventHandler;
import az.kon.academy.event.handler.autoconfiguration.annotation.EventHandler;

import java.util.Random;

@EventHandler
public class OrderCreatedEventHandler implements BaseEventHandler<OrderCreatedEvent> {
    @Override
    public void handle(OrderCreatedEvent event) {
//        Random random = new Random();
//        if (random.nextBoolean()) {
//            throw new RuntimeException("Simulating error");
//        }
        System.out.println("Handling OrderCreatedEvent: " + event);
    }
}
