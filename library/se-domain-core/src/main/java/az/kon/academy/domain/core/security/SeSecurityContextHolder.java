package az.kon.academy.domain.core.security;

import java.util.List;

public interface SeSecurityContextHolder {
    Boolean isLoggedIn();
    User getUser();
    List<String> getUserRoles();
    List<String> getUserPermissions();
}
