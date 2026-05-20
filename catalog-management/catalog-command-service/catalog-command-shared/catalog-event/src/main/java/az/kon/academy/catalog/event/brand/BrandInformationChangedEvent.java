package az.kon.academy.catalog.event.brand;


import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class BrandInformationChangedEvent extends DomainEvent implements BrandEvent {
    private final String name;
    private final String description;
    private final String path;

    public BrandInformationChangedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                        String name, String description, String path) {
        super(eventId, aggregateId, timestamp, version);
        this.name = name;
        this.description = description;
        this.path = path;
    }

    private BrandInformationChangedEvent(String aggregateId, OffsetDateTime timestamp,String name,
                                        String description, String path) {
        super(aggregateId, timestamp);
        this.name = name;
        this.description = description;
        this.path = path;
    }

    public static BrandInformationChangedEvent of(String aggregateId, OffsetDateTime timestamp, String name,
                                                  String description, String path){
        return new BrandInformationChangedEvent(aggregateId, timestamp, name, description, path);
    }
}
