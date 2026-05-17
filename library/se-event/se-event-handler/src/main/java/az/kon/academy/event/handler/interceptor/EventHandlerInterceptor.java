package az.kon.academy.event.handler.interceptor;


import az.kon.academy.event.AbstractEvent;

public interface EventHandlerInterceptor {
    default <T extends AbstractEvent> void onReceive(T event){}
    default <T extends AbstractEvent> void preExecution(T event){}
    default <T extends AbstractEvent> void postExecution(T event){}
    default <T extends AbstractEvent> void onComplete(T event){}
    default <T extends AbstractEvent> void onError(T event, Exception e){}

}
