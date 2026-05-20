package az.kon.academy.catalog.avro.mapper.product;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.ProductSpecificationRemovedAvroModel;
import az.kon.academy.catalog.event.product.ProductSpecificationRemovedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductSpecificationRemovedEventAvroMapper {

    ProductSpecificationRemovedEventAvroMapper INSTANCE = Mappers.getMapper(ProductSpecificationRemovedEventAvroMapper.class);

    ProductSpecificationRemovedAvroModel toAvro(ProductSpecificationRemovedEvent event);

    ProductSpecificationRemovedEvent toEvent(ProductSpecificationRemovedAvroModel avroModel);
}
