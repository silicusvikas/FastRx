
package com.parkwoodrx.fastrx;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.parkwoodrx.fastrx.security.TokenAuthenticationService;

/**
 * Handles csrf protection and session management
 * 
 * @author SMurmu
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger LOGGER = Logger.getLogger(SecurityConfig.class);

	private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.OPTIONS);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.exceptionHandling().accessDeniedPage("/403");
		http.authorizeRequests().antMatchers("/login**").permitAll();
		http.formLogin().loginProcessingUrl("/login");
		http.logout().logoutSuccessUrl("/logout");
		http.csrf().disable().addFilterBefore(securityFilter(), CsrfFilter.class);
	}

	private Filter securityFilter() {
		return new OncePerRequestFilter() {

			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(
							"SecurityFilter Intercepts " + request.getRequestURI() + " method " + request.getMethod());
				}

				if (request.getRequestURI().contains("/login")
						|| request.getRequestURI().contains("/pharmacy/corporation/paymentTypeList")
						|| request.getRequestURI().contains("/pharmacy/state/list")
						|| request.getRequestURI().contains("/pharmacy/corporation")) {
					// don't authenticate
				} else {

					Authentication authentication = tokenAuthenticationService
							.getAuthentication((HttpServletRequest) request);
					if (authentication == null) {
						accessDeniedHandler.handle(request, response,
								new AccessDeniedException("Missing or non-matching CSRF-token"));
						return;
					}
				}
				filterChain.doFilter(request, response);
			}
		};
	}

	/**
	 * Http Methods for which csrf protection is not checked
	 * 
	 * @author SMurmu
	 *
	 */
	public static final class DefaultRequiresCsrfMatcher implements RequestMatcher {
		private final Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

		@Override
		public boolean matches(HttpServletRequest request) {
			return !allowedMethods.matcher(request.getMethod()).matches();
		}
	}

}
