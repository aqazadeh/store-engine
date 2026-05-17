package az.kon.academy.catalog.event.brand;


import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class BrandCreatedGlobalEvent extends DomainEvent implements BrandEvent {
    private final UUID owner;
    private final String name;
    private final String description;
    private final String path;
    private final Boolean isGlobal;
    private final String status;

    public BrandCreatedGlobalEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version, UUID owner,
                                   String name, String description, String path, Boolean isGlobal, String status) {
        super(eventId, aggregateId, timestamp, version);
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.path = path;
        this.isGlobal = isGlobal;
        this.status = status;
    }

    public BrandCreatedGlobalEvent(String aggregateId, OffsetDateTime timestamp, UUID owner, String name,
                                   String description, String path, Boolean isGlobal, String status) {
        super(aggregateId, timestamp);
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.path = path;
        this.isGlobal = isGlobal;
        this.status = status;
    }

    public static BrandCreatedGlobalEvent of(String aggregateId, OffsetDateTime timestamp, UUID owner, String name,
                                             String description, String path, Boolean isGlobal, String status){
        return new BrandCreatedGlobalEvent(aggregateId, timestamp, owner, name, description, path, isGlobal, status);
    }
}
