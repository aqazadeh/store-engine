package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;

public interface MerchantQueryPort {
    Boolean isMerchantExists(MerchantId merchantId);
}
