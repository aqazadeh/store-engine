package az.kon.academy.catalog.command.service.application.service.configuration;

import az.kon.academy.domain.core.security.SeSecurityContextHolder;
import az.kon.academy.domain.core.security.User;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SeSecurityContextHolderImpl implements SeSecurityContextHolder {
    @Override
    public Boolean isLoggedIn() {
        return null;
    }

    @Override
    public User getUser() {
        return null;
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
