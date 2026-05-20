package az.kon.academy.catalog.avro.mapper.management.specification;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.specification.ProductSpecificationInformationChangedAvroModel;
import az.kon.academy.catalog.event.management.specification.ProductSpecificationInformationChangedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductSpecificationInformationChangedEventAvroMapper {

    ProductSpecificationInformationChangedEventAvroMapper INSTANCE = Mappers.getMapper(ProductSpecificationInformationChangedEventAvroMapper.class);

    ProductSpecificationInformationChangedAvroModel toAvro(ProductSpecificationInformationChangedEvent event);

    ProductSpecificationInformationChangedEvent toEvent(ProductSpecificationInformationChangedAvroModel avroModel);
}
