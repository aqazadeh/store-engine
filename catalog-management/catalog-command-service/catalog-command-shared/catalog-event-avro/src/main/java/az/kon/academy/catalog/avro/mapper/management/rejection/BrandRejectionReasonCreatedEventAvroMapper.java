package az.kon.academy.catalog.avro.mapper.management.rejection;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.rejection.BrandRejectionReasonCreatedAvroModel;
import az.kon.academy.catalog.event.management.rejection.BrandRejectionReasonCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface BrandRejectionReasonCreatedEventAvroMapper {

    BrandRejectionReasonCreatedEventAvroMapper INSTANCE = Mappers.getMapper(BrandRejectionReasonCreatedEventAvroMapper.class);

    BrandRejectionReasonCreatedAvroModel toAvro(BrandRejectionReasonCreatedEvent event);

    BrandRejectionReasonCreatedEvent toEvent(BrandRejectionReasonCreatedAvroModel avroModel);
}
