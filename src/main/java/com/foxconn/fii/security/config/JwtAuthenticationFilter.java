package com.foxconn.fii.security.config;

import com.foxconn.fii.security.jwt.model.token.JwtAuthenticationToken;
import com.foxconn.fii.security.jwt.model.token.extractor.TokenExtractor;
import com.foxconn.fii.security.model.UserContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.foxconn.fii.security.jwt.config.JwtProperties.AUTHENTICATION_HEADER_NAME;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
//    private static final String jwtTokenCookieName = "JWT-TOKEN";
//    private static final String signingKey = "signingKey";

    private final AuthenticationFailureHandler failureHandler;

    private final TokenExtractor tokenExtractor;

    private final RestTemplate restTemplate;

    private final String tokenInfoUri;

    public JwtAuthenticationFilter(AuthenticationFailureHandler failureHandler, TokenExtractor tokenExtractor, RestTemplate restTemplate, String tokenInfoUri, RequestMatcher matcher) {
        super(matcher);
        this.failureHandler = failureHandler;
        this.tokenExtractor = tokenExtractor;
        this.restTemplate = restTemplate;
        this.tokenInfoUri = tokenInfoUri;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String tokenPayload = request.getHeader(AUTHENTICATION_HEADER_NAME);
        if(!StringUtils.isEmpty(tokenPayload)){
            String token = tokenExtractor.extract(tokenPayload);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic d3Mtc3lzdGVtOkZveGNvbm4xNjghIQ==");

            HttpEntity<String> entity = new HttpEntity<>("", headers);

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(tokenInfoUri)
                    .queryParam("token", token);

            ResponseEntity<Map<String, Object>> responseEntity;
            try {
                responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {});
            } catch (RestClientException e) {
                throw new BadCredentialsException("Invalid JWT access token");
            }

            if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
                throw new BadCredentialsException("Invalid JWT access token");
            }

            String username = (String) responseEntity.getBody().get("user_name");
            List<GrantedAuthority> authorities = ((List<String>) responseEntity.getBody().get("authorities")).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

            UserContext context = UserContext.of(username, authorities);
            return new JwtAuthenticationToken(context, context.getAuthorities());
        } else{
            return new JwtAuthenticationToken(UserContext.of("anonymousUser", Collections.emptyList()), Collections.emptyList());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if (authResult != null && (authResult.getPrincipal() instanceof UserContext) && !"anonymousUser".equals(((UserContext)authResult.getPrincipal()).getUsername())) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authResult);
            SecurityContextHolder.setContext(context);
        }
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
