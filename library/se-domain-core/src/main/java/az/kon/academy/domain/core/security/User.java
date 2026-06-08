package az.kon.academy.domain.core.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class User {
    private final UUID userId;
    private final List<String> roles;
    private final List<String> permissions;
}
