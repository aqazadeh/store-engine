package az.kon.academy.catalog.command.service.domain.core;

public interface DomainContext {
    <T> T getQueryPort(Class<T> portClass);
    <T> T getCommandPort(Class<T> portClass);
    void getUserContent(); // Fixme change void to UserContent when security context will be added
}
