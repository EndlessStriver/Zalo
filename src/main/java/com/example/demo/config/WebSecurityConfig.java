package com.example.demo.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.example.demo.service.MyUserDetailService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	
	private MyUserDetailService myUserDetailService;
	private JwtTokenFilter jwtTokenFilter;
	
	public WebSecurityConfig(MyUserDetailService myUserDetailService, JwtTokenFilter jwtTokenFilter) {
		this.myUserDetailService = myUserDetailService;
		this.jwtTokenFilter = jwtTokenFilter;
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
		daoAuthenticationProvider.setUserDetailsService(myUserDetailService);
		return daoAuthenticationProvider;
	}
	
	@Bean
	AuthenticationManager authenticationManager()
			throws Exception {
		return new AuthenticationManager() {
			
			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				return daoAuthenticationProvider().authenticate(authentication);
			}
		};
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(request -> {
			CorsConfiguration corsConfig = new CorsConfiguration();
			corsConfig.setAllowedOrigins(Arrays.asList(EndPoint.ALLOWED_ORIGINS));
			corsConfig.setAllowedMethods(Arrays.asList(EndPoint.ALLOWED_METHODS));
			corsConfig.setAllowCredentials(true);
			return corsConfig;
		}));
		http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().permitAll())
				.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		http.formLogin(formLogin -> formLogin.disable());
		http.httpBasic(httpBasic -> httpBasic.disable());
		http.csrf(csrf -> csrf.disable());
		http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
	}
}
