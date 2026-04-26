package az.kon.academy.catalog.command.service.application.service.handler;

public interface AbstractCommandHandler<IN, OUT> {
    OUT handle(IN command);
}
