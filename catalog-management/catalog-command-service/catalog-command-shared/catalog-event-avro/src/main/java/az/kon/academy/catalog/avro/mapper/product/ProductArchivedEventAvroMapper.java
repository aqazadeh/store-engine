package az.kon.academy.catalog.avro.mapper.product;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.ProductArchivedAvroModel;
import az.kon.academy.catalog.event.product.ProductArchivedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductArchivedEventAvroMapper {

    ProductArchivedEventAvroMapper INSTANCE = Mappers.getMapper(ProductArchivedEventAvroMapper.class);

    ProductArchivedAvroModel toAvro(ProductArchivedEvent event);

    ProductArchivedEvent toEvent(ProductArchivedAvroModel avroModel);
}
