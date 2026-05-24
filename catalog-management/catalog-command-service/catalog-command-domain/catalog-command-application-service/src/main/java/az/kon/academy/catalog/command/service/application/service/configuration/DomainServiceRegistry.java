package az.kon.academy.catalog.command.service.application.service.configuration;

import az.kon.academy.catalog.command.service.domain.core.service.brand.BrandDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.service.brand.BrandManagementDomainService;
import az.kon.academy.catalog.command.service.domain.core.service.brand.BrandModificationDomainService;
import az.kon.academy.catalog.command.service.domain.core.service.category.ProductCategoryDomainService;
import az.kon.academy.catalog.command.service.domain.core.service.category.ProductCategoryDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.service.product.ProductDomainService;
import az.kon.academy.catalog.command.service.domain.core.service.product.ProductDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.service.specification.ProductSpecificationDomainService;
import az.kon.academy.catalog.command.service.domain.core.service.specification.ProductSpecificationDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.service.variant.VariantDomainService;
import az.kon.academy.catalog.command.service.domain.core.service.variant.VariantDomainServiceImpl;
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

    @Bean
    public ProductDomainService productDomainService() {
        return new ProductDomainServiceImpl();
    }

    @Bean
    public VariantDomainService variantDomainService() {
        return new VariantDomainServiceImpl();
    }

}
