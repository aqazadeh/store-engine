package az.kon.academy.application.core.security;

import az.kon.academy.domain.core.security.SeSecurityContextHolder;
import az.kon.academy.domain.core.security.User;

import java.util.List;

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
