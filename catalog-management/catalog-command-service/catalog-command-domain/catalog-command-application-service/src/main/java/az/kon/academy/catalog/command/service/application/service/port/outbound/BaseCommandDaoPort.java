package az.kon.academy.catalog.command.service.application.service.port.outbound;

import az.kon.academy.aggragate.BaseRoot;
import az.kon.academy.domain.core.BaseCommandPort;

public interface BaseCommandDaoPort<T extends BaseRoot<?, ?>> extends BaseCommandPort {
    T save(T aggregate);
}
