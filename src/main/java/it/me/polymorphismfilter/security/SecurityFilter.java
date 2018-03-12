package it.me.polymorphismfilter.security;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sun.misc.BASE64Decoder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.StringTokenizer;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private static final String AUTHENTICATION_HEADER = "Authorization";

    private AuthenticationManager authenticationManager;

    public SecurityFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!authenticate(request)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized.");
        }

        filterChain.doFilter(request, response);
    }

    public boolean authenticate(HttpServletRequest request) {
        String credential = request.getHeader(AUTHENTICATION_HEADER);
        if (null == credential) {
            return false;
        }
        // header value format will be "Basic encodedstring" for Basic
        // authentication. Example "Basic YWRtaW46YWRtaW4="
        final String encodedUserPassword = credential.replaceFirst("Basic" + " ", "");
        String usernameAndPassword = null;
        try {
            byte[] decodedBytes = new BASE64Decoder().decodeBuffer(encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();

        // we have fixed the userid and password as admin
        // call some UserService/LDAP here
        return authenticationManager.authenticate(username, password);
    }


}
