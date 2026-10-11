
package com.bangon.kiosk.controller;

import com.bangon.kiosk.dto.ApiDtos.*;
import com.bangon.kiosk.entity.Customer;
import com.bangon.kiosk.repository.CustomerRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;

import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CustomerRepository customers;
    private final PasswordEncoder encoder;
    private final AuthenticationManager auth;

    public AuthController(
            CustomerRepository customers,
            PasswordEncoder encoder,
            UserDetailsService users) {

        this.customers = customers;
        this.encoder = encoder;

        // Configure password authentication
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(users);

        provider.setPasswordEncoder(encoder);

        this.auth = new ProviderManager(provider);
    }

    // CSRF token
    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken token) {
        return Map.of("token", token.getToken());
    }

    // Customer registration
    @PostMapping("/register")
    public Map<String, Object> register(
            @Valid @RequestBody Register r) {

        String email = r.email().trim()
                .toLowerCase(Locale.ROOT);

        if (customers.findByEmailIgnoreCase(email).isPresent()) {
            throw new IllegalArgumentException(
                    "Email already registered");
        }

        Customer customer = new Customer();

        customer.name = r.name().trim();
        customer.email = email;
        customer.passwordHash = encoder.encode(r.password());

        customers.save(customer);

        return Map.of(
                "message",
                "Registered. Please sign in.");
    }

    // Customer login
    @PostMapping("/login")
    public Map<String, Object> login(
            @Valid @RequestBody Login r,
            HttpServletRequest request,
            HttpServletResponse response) {

        var authentication = auth.authenticate(
                new UsernamePasswordAuthenticationToken(
                        r.email().trim(), r.password())
        );

        // Rotate session ID after authentication
        HttpServletRequest authenticatedRequest = request;

        if (request.getSession(false) == null) {
            request.getSession(true);
        } else {
            request.changeSessionId();
        }

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        HttpSessionSecurityContextRepository repository =
                new HttpSessionSecurityContextRepository();

        repository.saveContext(
                context,
                authenticatedRequest,
                response
        );

        return Map.of("message", "Signed in");
    }

    // Customer logout
    @PostMapping("/logout")
    public Map<String, String> logout(
            HttpServletRequest request) {

        var session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        SecurityContextHolder.clearContext();

        return Map.of("message", "Signed out");
    }
}