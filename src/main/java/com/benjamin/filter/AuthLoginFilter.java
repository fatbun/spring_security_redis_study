package com.benjamin.filter;

import com.benjamin.misc.MultiReadhttpServletRequest;
import com.benjamin.payload.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Ben Li.
 * Date: 2020/6/15
 */
@Component
public class AuthLoginFilter extends OncePerRequestFilter {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!checkLoginUri(request)) {
            filterChain.doFilter(request,
                    response);
            return;
        }

        MultiReadhttpServletRequest mRequest = new MultiReadhttpServletRequest(request);
        LoginRequest loginRequest = objectMapper.readValue(mRequest.getRequestBody(),
                LoginRequest.class);
        System.out.println(loginRequest);

        String username = loginRequest.getUsername()
                .trim();
        String password = loginRequest.getPassword();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username,
                password);

        // Allow subclasses to set the "details" property
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        authRequest.setDetails(userDetails);

        Authentication authenticate = authenticationManager.authenticate(authRequest);
        SecurityContextHolder.getContext()
                .setAuthentication(authenticate);

        filterChain.doFilter(mRequest,
                response);
    }

    private boolean checkLoginUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (StringUtils.isNotBlank(uri) && "/api/auth/login".equals(uri)) {
            return true;
        }

        return false;
    }
}
