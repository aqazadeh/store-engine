package az.kon.academy.domain.core.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class User {
    private final UUID userId;
}
