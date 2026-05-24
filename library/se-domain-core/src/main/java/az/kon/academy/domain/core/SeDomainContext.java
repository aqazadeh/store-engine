package az.kon.academy.domain.core;

import az.kon.academy.domain.core.security.SeSecurityContextHolder;

public interface SeDomainContext {
    <T> T getQueryPort(Class<T> portClass);
    <T> T getCommandPort(Class<T> portClass);
}
