package it.me.polymorphismfilter.security;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationManager {
    public boolean authenticate(String username, String password) {
        AdminStore store = new AdminStore();
        Credential credential = store.getCredential(username);
        return credential.getUsername().equals(username) && credential.getPassword().equals(password);
    }
}
