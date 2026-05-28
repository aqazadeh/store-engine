package az.kon.academy.catalog.command.service.application.service.configuration;

import az.kon.academy.domain.core.security.SeSecurityContextHolder;
import az.kon.academy.domain.core.security.User;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

@Configuration
public class SeSecurityContextHolderImpl implements SeSecurityContextHolder {
    @Override
    public Boolean isLoggedIn() {
        return true;
    }

    @Override
    public User getUser() {
        return User.builder()
                .userId(UUID.fromString("9357a609-4403-4a57-8e75-194e1b70fc00"))
                .build();
    }

    @Override
    public List<String> getUserRoles() {
        return List.of();
    }

    @Override
    public List<String> getUserPermissions() {
        return List.of();
    }
}
