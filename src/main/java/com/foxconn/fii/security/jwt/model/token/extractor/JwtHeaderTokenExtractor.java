package com.foxconn.fii.security.jwt.model.token.extractor;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {

    public static String HEADER_PREFIX = "Bearer ";

    @Override
    public String extract(String header) {
        if (StringUtils.isEmpty(header)) {
            throw new AuthenticationServiceException("Authorization header cannot be blank!");
        }

        if (!header.startsWith(HEADER_PREFIX)) {
            throw new AuthenticationServiceException("Invalid authorization header format.");
        }

        return header.substring(HEADER_PREFIX.length());
    }

}