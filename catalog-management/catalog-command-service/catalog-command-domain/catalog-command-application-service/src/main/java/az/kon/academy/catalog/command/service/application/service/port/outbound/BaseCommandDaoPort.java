package az.kon.academy.catalog.command.service.application.service.port.outbound;

import az.kon.academy.aggragate.BaseRoot;

public interface BaseCommandDaoPort<T extends BaseRoot<?, ?>> {
    T save(T aggregate);
}
