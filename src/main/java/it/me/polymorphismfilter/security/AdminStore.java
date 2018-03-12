package it.me.polymorphismfilter.security;

public class AdminStore {
    public Credential getCredential(String username) {
        return new Credential("admin", "admin");
    }
}
