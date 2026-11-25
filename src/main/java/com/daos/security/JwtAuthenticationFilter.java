package com.daos.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserDetailsServiceImpl userdetailsServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = null;
		if (StringUtils.hasText(request.getHeader("Authorization"))
				&& request.getHeader("Authorization").startsWith("Bearer")) {
			token = request.getHeader("Authorization").substring(7);
			if (jwtTokenProvider.validarToken(token)) {
				String username = jwtTokenProvider.obtenerUserNameFromJwt(token);
				UserDetails user = userdetailsServiceImpl.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,
						null, user.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}
