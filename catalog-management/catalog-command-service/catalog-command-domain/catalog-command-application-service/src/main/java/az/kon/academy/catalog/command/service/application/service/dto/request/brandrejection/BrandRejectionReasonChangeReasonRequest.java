package az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class BrandRejectionReasonChangeReasonRequest {
    private UUID brandRejectionReasonId;
    private String reason;
}
