package az.example;

import az.kon.academy.event.dispatcher.EventDispatcher;
import az.kon.academy.event.handler.DomainEventPublisher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootApplication
public class ExampleApplication {
    static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}

@Component
class Test implements CommandLineRunner {

    private final DomainEventPublisher publisher;
    private final EventDispatcher dispatcher;

    Test(DomainEventPublisher publisher, EventDispatcher dispatcher) {
        this.publisher = publisher;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run(String... args) throws Exception {
        var executionCount = 100;
        Thread.sleep(3000);
        var start = System.nanoTime();
        IntStream.range(0, executionCount)
                .parallel()
                .forEach(i -> {
                    var correlationId = UUID.randomUUID();
                    var causationId = UUID.randomUUID();
//                    dispatcher.dispatch(new OrderCreatedEvent(String.valueOf(i), String.valueOf(i), OffsetDateTime.now()), correlationId, causationId);
                        publisher.publish(new OrderCreatedEvent(String.valueOf(i), String.valueOf(i), OffsetDateTime.now()));
                });

        System.out.println("Time: " + (System.nanoTime() - start) / 1000000 + "ms");
    }
}
