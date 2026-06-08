package az.kon.academy.catalog.command.service.application.service.configuration;

import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandManagementDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandManagementDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandModerationDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandModerationDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection.BrandRejectionManagementService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection.BrandRejectionManagementServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection.BrandRejectionModerationDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection.BrandRejectionModerationDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.category.ProductCategoryModerationDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.category.ProductCategoryModerationDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.general.ProductManagementDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.general.ProductManagementDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.price.ProductPriceManagementDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.price.ProductPriceManagementDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.price.ProductPriceModerationDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.price.ProductPriceModerationDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.rejection.ProductRejectionManagementDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.rejection.ProductRejectionManagementDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.rejection.ProductRejectionModerationDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.rejection.ProductRejectionModerationDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.stock.ProductStockManagementDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.stock.ProductStockManagementDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.stock.ProductStockModerationDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.stock.ProductStockModerationDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.specification.ProductSpecificationModerationDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.specification.ProductSpecificationModerationDomainServiceImpl;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.variant.VariantModerationDomainService;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.variant.VariantModerationDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceRegistry {

    @Bean
    public BrandModerationDomainService brandModerationDomainService() {
        return new BrandModerationDomainServiceImpl();
    }

    @Bean
    public BrandManagementDomainService brandManagementDomainService() {
        return new BrandManagementDomainServiceImpl();
    }

    @Bean
    public BrandRejectionModerationDomainService brandRejectionModerationDomainService() {
        return new BrandRejectionModerationDomainServiceImpl();
    }

    @Bean
    public BrandRejectionManagementService brandRejectionManagementService(){
        return new BrandRejectionManagementServiceImpl();
    }

    @Bean
    public ProductCategoryModerationDomainService productCategoryDomainService() {
        return new ProductCategoryModerationDomainServiceImpl();
    }

    @Bean
    public ProductSpecificationModerationDomainService productSpecificationDomainService() {
        return new ProductSpecificationModerationDomainServiceImpl();
    }

    @Bean
    public ProductManagementDomainService productDomainService() {
        return new ProductManagementDomainServiceImpl();
    }

    @Bean
    public VariantModerationDomainService variantDomainService() {
        return new VariantModerationDomainServiceImpl();
    }

    @Bean
    public ProductStockModerationDomainService productStockModerationDomainService() {
        return new ProductStockModerationDomainServiceImpl();
    }

    @Bean
    public ProductStockManagementDomainService productStockManagementDomainService() {
        return new ProductStockManagementDomainServiceImpl();
    }

    @Bean
    public ProductPriceModerationDomainService productPriceModerationDomainService() {
        return new ProductPriceModerationDomainServiceImpl();
    }

    @Bean
    public ProductPriceManagementDomainService productPriceManagementDomainService() {
        return new ProductPriceManagementDomainServiceImpl();
    }

    @Bean
    public ProductRejectionModerationDomainService productRejectionModerationDomainService() {
        return new ProductRejectionModerationDomainServiceImpl();
    }

    @Bean
    public ProductRejectionManagementDomainService productRejectionManagementDomainService() {
        return new ProductRejectionManagementDomainServiceImpl();
    }

}
