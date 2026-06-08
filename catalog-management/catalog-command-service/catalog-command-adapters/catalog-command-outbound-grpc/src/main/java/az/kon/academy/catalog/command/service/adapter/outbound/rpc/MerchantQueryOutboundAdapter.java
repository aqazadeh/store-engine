package az.kon.academy.catalog.command.service.adapter.outbound.rpc;

import az.kon.academy.catalog.command.service.domain.core.port.outbound.MerchantQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import org.springframework.stereotype.Component;

@Component
public class MerchantQueryOutboundAdapter implements MerchantQueryOutboundPort {
    @Override
    public void checkMerchantIsActive(MerchantId merchantId) {
    }
}
