package com.example.demo.filter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.config.EndPoint;
import com.example.demo.exception.AuthorizationException;
import com.example.demo.service.JwtService;
import com.example.demo.service.MyUserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	private MyUserDetailService myUserDetailsService;
	private JwtService jwtService;

	public JwtTokenFilter(MyUserDetailService myUserDetailsService, JwtService jwtService) {
		this.myUserDetailsService = myUserDetailsService;
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {

			String requestURI = request.getRequestURI();
			String requestMethod = request.getMethod();
			String username = null;
			String jwt = null;
			String authorizationHeader = request.getHeader("Authorization");

			if (shouldBypassFilter(requestURI, requestMethod)) {
				filterChain.doFilter(request, response);
				return;
			}

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				username = jwtService.extractUsername(jwt);
			} else {
				throw new AuthorizationException("Token không hợp lệ hoặc đã hết hạn");
			}

			if (username != null) {
				UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
				if (jwtService.validateToken(jwt, userDetails.getUsername())) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				} else {
					throw new AuthorizationException("Token không hợp lệ hoặc đã hết hạn");
				}
			}
			filterChain.doFilter(request, response);

		} catch (Exception e) {

			SecurityContextHolder.clearContext();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			Map<String, Object> errors = new LinkedHashMap<String, Object>();
			errors.put("status", "error");
			errors.put("code", HttpServletResponse.SC_UNAUTHORIZED);
			errors.put("message", e.getMessage());

			response.getWriter().write(new ObjectMapper().writeValueAsString(errors));

		}

	}

	private boolean shouldBypassFilter(String requestURI, String requestMethod) {
		
		PathMatcher pathMatcher = new AntPathMatcher();

		for (String endpoint : EndPoint.ALLOWED_PATHS) {
			if (pathMatcher.match(endpoint, requestURI)) return true;
		}
		
		if (requestMethod.equals("PATCH")) {
			for (String endpoint : EndPoint.PUBLIC_METHODS_PATCH) {
				if (pathMatcher.match(endpoint, requestURI) && requestMethod.equals("PATCH")) return true;
			}
		}

		if (requestMethod.equals("POST")) {
			for (String endpoint : EndPoint.PUBLIC_METHODS_POST) {
				if (pathMatcher.match(endpoint, requestURI) && requestMethod.equals("POST")) return true;
			}
		}

		if (requestMethod.equals("GET")) {
			for (String endpoint : EndPoint.PUBLIC_METHODS_GET) {
				if (pathMatcher.match(endpoint, requestURI) && requestMethod.equals("GET")) return true;
			}
		}

		return false;
	}

}
