package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.domain.core.BaseQueryPort;

public interface MerchantQueryPort extends BaseQueryPort {
    Boolean isMerchantExists(MerchantId merchantId);
}
