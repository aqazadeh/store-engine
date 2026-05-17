package az.kon.academy.event.dispatcher.autoconfiguration.data;


import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.dispatcher.EventDispatchStrategy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventDispatchEntry {
    private String name;
    private Class<AbstractEvent> event;
    private Class<EventDispatchStrategy> strategy;
}
