package az.kon.academy.catalog.avro.mapper.management.category;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.management.category.ProductCategoryInformationChangedAvroModel;
import az.kon.academy.catalog.event.management.category.ProductCategoryInformationChangedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductCategoryInformationChangedEventAvroMapper {

    ProductCategoryInformationChangedEventAvroMapper INSTANCE = Mappers.getMapper(ProductCategoryInformationChangedEventAvroMapper.class);

    ProductCategoryInformationChangedAvroModel toAvro(ProductCategoryInformationChangedEvent event);

    ProductCategoryInformationChangedEvent toEvent(ProductCategoryInformationChangedAvroModel avroModel);
}
