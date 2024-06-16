package dev.javarush.security.spring_security_learn.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

public class RobotAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // 1. Decide whether to apply the filter or not
        if (!Collections.list(request.getHeaderNames()).contains("x-robot-secret")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Check credentials and [ authenticate | reject ]
        if (!Objects.equals(request.getHeader("X-robot-secret"), "beep-boop")) {
            // Reject the request
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "text/plain;charset=UTF-8");
            response.getWriter().write("⛔️⛔️🤖🤖 You are not Ms. Robot\n");
            return;
        }
        // Authenticate the request
        Authentication auth = new RobotAuthenticationToken();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // 3. Call next filter in filter chain
        filterChain.doFilter(request, response);

        // 4. Clean up
    }
}
