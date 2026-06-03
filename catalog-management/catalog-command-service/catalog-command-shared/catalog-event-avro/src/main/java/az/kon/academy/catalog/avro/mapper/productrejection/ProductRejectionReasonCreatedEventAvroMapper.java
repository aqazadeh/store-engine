package az.kon.academy.catalog.avro.mapper.productrejection;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.productrejection.ProductRejectionReasonCreatedAvroModel;
import az.kon.academy.catalog.event.productrejection.ProductRejectionReasonCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductRejectionReasonCreatedEventAvroMapper {

    ProductRejectionReasonCreatedEventAvroMapper INSTANCE = Mappers.getMapper(ProductRejectionReasonCreatedEventAvroMapper.class);

    ProductRejectionReasonCreatedAvroModel toAvro(ProductRejectionReasonCreatedEvent event);

    ProductRejectionReasonCreatedEvent toEvent(ProductRejectionReasonCreatedAvroModel avroModel);
}
