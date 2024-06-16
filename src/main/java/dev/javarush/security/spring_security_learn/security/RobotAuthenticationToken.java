package dev.javarush.security.spring_security_learn.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

public class RobotAuthenticationToken extends AbstractAuthenticationToken {
    public RobotAuthenticationToken() {
        super(AuthorityUtils.createAuthorityList("ROLE_robot"));
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return "Ms. Robot 🤖";
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        throw new RuntimeException("🎶 Can't touch this!");
    }
}
