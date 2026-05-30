package az.kon.academy.catalog.avro.mapper.management.rejection;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.rejection.ProductRejectionReasonCreatedAvroModel;
import az.kon.academy.catalog.event.management.rejection.ProductRejectionReasonCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductRejectionReasonCreatedEventAvroMapper {

    ProductRejectionReasonCreatedEventAvroMapper INSTANCE = Mappers.getMapper(ProductRejectionReasonCreatedEventAvroMapper.class);

    ProductRejectionReasonCreatedAvroModel toAvro(ProductRejectionReasonCreatedEvent event);

    ProductRejectionReasonCreatedEvent toEvent(ProductRejectionReasonCreatedAvroModel avroModel);
}
