package az.kon.academy.catalog.command.service.adapter.outbound.rpc;

import az.kon.academy.catalog.command.service.domain.core.port.outbound.MerchantQueryPort;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import org.springframework.stereotype.Component;

@Component
public class MerchantQueryAdapter implements MerchantQueryPort {
    @Override
    public Boolean isActiveMerchantExists(MerchantId merchantId) {
        return true; // FIXME dummy return
    }
}
