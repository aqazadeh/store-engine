package az.kon.academy.application.core.security;

import az.kon.academy.domain.core.security.SeSecurityContextHolder;
import az.kon.academy.domain.core.security.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
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
        return this.getUser().getRoles();
    }

    @Override
    public List<String> getUserPermissions() {
        return this.getUser().getPermissions();
    }
}

