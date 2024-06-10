package com.backend.TGF.security;


import com.backend.TGF.model.entity.User;
import com.backend.TGF.model.services.User.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    private JwtTokenProvider jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        final String authHeader = request.getHeader(JwtTokenProvider.TOKEN_HEADER);

        if (authHeader == null || !authHeader.startsWith(JwtTokenProvider.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Sacamos el token
            String token = getJwtFromRequest(request);

            final String userName = jwtService.extractUsername(token);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userName != null && authentication == null)
            {
                User user = (User) customUserDetailsService.loadUserByUsername(userName);

                // Si el token existe y es válido
                if (StringUtils.hasText(token) && jwtService.isTokenValid(token, user))
                {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            user.getRoles(),
                            user.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        }
        catch (Exception exception)
        {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }

    // Procesamos el Token del Request
    private String getJwtFromRequest(HttpServletRequest request) {
        // Tomamos la cabecera
        String bearerToken = request.getHeader(JwtTokenProvider.TOKEN_HEADER);
        // Si tiene el prefijo y es de la logitud indicada
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtTokenProvider.TOKEN_PREFIX)) {
            return bearerToken.substring(JwtTokenProvider.TOKEN_PREFIX.length());
        }
        return null;
    }
}
