package az.kon.academy.domain.core;

public interface SeDomainContext {
    <T extends BaseQueryPort> T getQueryPort(Class<T> portClass);

    <T extends BaseCommandPort> T getCommandPort(Class<T> portClass);
}
