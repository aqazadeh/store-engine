package az.kon.academy.application.core.handler;

public interface AbstractCommandHandler<IN, OUT> {
    OUT handle(IN command);
}
