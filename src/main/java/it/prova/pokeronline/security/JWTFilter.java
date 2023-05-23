package it.prova.pokeronline.security;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

@Component
public class JWTFilter extends OncePerRequestFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(JWTFilter.class);

	@Autowired
	private CustomUserDetailsService userDetailsService;
	@Autowired
	private JWTUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Extracting the "Authorization" header
		String authHeader = request.getHeader("Authorization");

		// Checking if the header contains a Bearer token
		if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith("Bearer ")) {
			// Extract JWT
			String jwt = authHeader.substring(7);
			if (StringUtils.isBlank(jwt)) {
				// Invalid JWT
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token in Bearer Header");
			} else {
				try {
					// Verify token and extract username
					String username = jwtUtil.validateTokenAndRetrieveSubject(jwt);

					// Fetch User Details
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authentication);

				} catch (TokenExpiredException exc) {
					// JWT expired
					LOGGER.error("JWT token is expired: {}", exc.getMessage());
					request.setAttribute("expired", exc.getMessage());
				} catch (JWTVerificationException exc) {
					// Failed to verify JWT
					LOGGER.error("Cannot set user authentication: {}", exc);
				}
			}
		}

		// Continuing the execution of the filter chain
		filterChain.doFilter(request, response);
	}
}
