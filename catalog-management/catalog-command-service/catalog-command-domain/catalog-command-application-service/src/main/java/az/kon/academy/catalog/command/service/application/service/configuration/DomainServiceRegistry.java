package az.kon.academy.catalog.command.service.application.service.configuration;

import az.kon.academy.catalog.command.service.domain.core.service.brand.BrandDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.service.brand.BrandManagementDomainService;
import az.kon.academy.catalog.command.service.domain.core.service.brand.BrandModificationDomainService;
import az.kon.academy.catalog.command.service.domain.core.service.category.ProductCategoryDomainService;
import az.kon.academy.catalog.command.service.domain.core.service.category.ProductCategoryDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.service.specification.ProductSpecificationDomainService;
import az.kon.academy.catalog.command.service.domain.core.service.specification.ProductSpecificationDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceRegistry {

    @Bean
    public BrandManagementDomainService brandManagementDomainService() {
        return new BrandDomainServiceImpl();
    }

    @Bean
    public BrandModificationDomainService brandModificationDomainService() {
        return new BrandDomainServiceImpl();
    }

    @Bean
    public ProductCategoryDomainService productCategoryDomainService() {
        return new ProductCategoryDomainServiceImpl();
    }

    @Bean
    public ProductSpecificationDomainService productSpecificationDomainService() {
        return new ProductSpecificationDomainServiceImpl();
    }

}
