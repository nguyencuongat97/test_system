package com.foxconn.fii.security.config;

import com.foxconn.fii.security.jwt.config.PathRequestMatcher;
import com.foxconn.fii.security.jwt.model.token.extractor.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.oauth2.logoutUrl}")
    private String logoutUrl;

    @Value("${server.domain}")
    private String domain;

    @Value("${security.oauth2.resource.tokenInfoUri}")
    private String tokenInfoUri;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private TokenExtractor tokenExtractor;

    protected JwtAuthenticationFilter buildJwtTokenAuthenticationProcessingFilter(List<String> paths) throws Exception {
        PathRequestMatcher matcher = new PathRequestMatcher(paths);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(failureHandler, tokenExtractor, restTemplate, tokenInfoUri, matcher);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] securedPageList = {
                "/task-management",
                "/task-management-view",
                "/task-management-confirm",
                "/engineer-management"
        };

        String[] securedEndpointList = {
                "/api/test/task-daily/**",
                "/api/test/task-comment/**",
                "/api/test/resource/**",
                "/api/test/mobile/**"
        };

        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**")
                .permitAll()
                .antMatchers(securedPageList)
                .hasRole("WS_USER")
                .antMatchers(securedEndpointList)
                .hasRole("WS_USER")
                .antMatchers("/customer/**")
                .hasAnyRole("WS_USER", "WS_CUSTOMER")
                .anyRequest().permitAll()
        ;

        http.logout().logoutSuccessUrl(String.format("%s?redirectUrl=%s", logoutUrl, domain)).invalidateHttpSession(true).deleteCookies("WSSESSION");

        http.csrf().disable();

        http.addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(Arrays.asList(securedEndpointList)), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LanguageFilter(), AbstractPreAuthenticatedProcessingFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/ws-data/**", "/assets/**", "/templates/**", "/WEB-INF/jsp/**");
    }
}
