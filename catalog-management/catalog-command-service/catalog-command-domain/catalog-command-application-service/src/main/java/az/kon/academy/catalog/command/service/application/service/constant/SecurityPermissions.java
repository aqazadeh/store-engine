package az.kon.academy.catalog.command.service.application.service.constant;

public interface SecurityPermissions {
    interface Role {
        String ROLE_CUSTOMER            = "customer";
        String ROLE_MERCHANT            = "merchant";
        String ROLE_DOMAIN_MODERATOR    = "moderator";
    }

    interface Brand {
        String BRAND_MERCHANT_CREATE                                = "BRAND_MERCHANT_CREATE_FROM";
        String BRAND_MERCHANT_CHANGE_INFORMATION                    = "BRAND_MERCHANT_CHANGE_INFORMATION_FROM";
        String BRAND_MERCHANT_CHANGE_IMAGE                          = "BRAND_MERCHANT_CHANGE_IMAGE";
        String BRAND_MANAGEMENT_CHANGE_INFORMATION                  = "BRAND_MANAGEMENT_CHANGE_INFORMATION_FROM";

        String BRAND_MANAGEMENT_CREATE                              = "BRAND_CREATE_FROM_GLOBAL";
        String BRAND_MANAGEMENT_APPROVE                             = "BRAND_MANAGEMENT_APPROVE";
        String BRAND_MANAGEMENT_REJECT                              = "BRAND_MANAGEMENT_REJECT";
        String BRAND_MANAGEMENT_CHANGE_OWNER                        = "BRAND_MANAGEMENT_CHANGE_OWNER";
        String BRAND_CHANGE_IMAGE                                   = "BRAND_CHANGE_IMAGE";
        String BRAND_MERCHANT_MOVE_DRAFT                            = "BRAND_MERCHANT_MOVE_DRAFT";
    }

    interface ProductCategory {
        String PRODUCT_CATEGORY_CREATE                              = "PRODUCT_CATEGORY_CREATE";
        String PRODUCT_CATEGORY_ACTIVATE                            = "PRODUCT_CATEGORY_ACTIVATE";
        String PRODUCT_CATEGORY_ARCHIVE                             = "PRODUCT_CATEGORY_ARCHIVE";
        String PRODUCT_CATEGORY_CHANGE_IMAGE                        = "PRODUCT_CATEGORY_CHANGE_IMAGE";
        String PRODUCT_CATEGORY_CHANGE_INFORMATION                  = "PRODUCT_CATEGORY_CHANGE_INFORMATION";
        String PRODUCT_CATEGORY_CHANGE_PARENT                       = "PRODUCT_CATEGORY_CHANGE_PARENT";
    }

    interface ProductSpecification {
        String PRODUCT_SPECIFICATION_ASSIGN_CATEGORY                = "PRODUCT_SPECIFICATION_ASSIGN_CATEGORY";
        String PRODUCT_SPECIFICATION_CHANGE_INFORMATION             = "PRODUCT_SPECIFICATION_CHANGE_INFORMATION";
        String PRODUCT_SPECIFICATION_CREATE                         = "PRODUCT_SPECIFICATION_CREATE";
        String PRODUCT_SPECIFICATION_DELETE                         = "PRODUCT_SPECIFICATION_DELETE";
        String PRODUCT_SPECIFICATION_REMOVE_CATEGORY_ASSIGNMENT     = "PRODUCT_SPECIFICATION_REMOVE_CATEGORY_ASSIGNMENT";
    }
}
