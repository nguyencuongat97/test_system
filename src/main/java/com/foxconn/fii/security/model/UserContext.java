package com.foxconn.fii.security.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class UserContext {

    protected String username;

    @JsonDeserialize(converter = GrantedAuthorityConverter.class)
    protected List<GrantedAuthority> authorities;

    public static UserContext of(String username, List<GrantedAuthority> authorities) {
        UserContext userContext = new UserContext() {};
        userContext.setUsername(username);
        userContext.setAuthorities(authorities);
        return userContext;
    }

    public static UserContext of(String username, List<GrantedAuthority> authorities, String qbloxToken) {
        UserContext userContext = new UserContext() {};
        userContext.setUsername(username);
        userContext.setAuthorities(authorities);
        return userContext;
    }

    public static class GrantedAuthorityConverter extends StdConverter<List, List<GrantedAuthority>> {
        @Override
        public List<GrantedAuthority> convert(List value) {
            List<GrantedAuthority> convertedValue = new ArrayList<>();

            try {
                List<Map<String, String>> values = (List<Map<String, String>>) value;
                for (Map<String, String> object : values) {
                    String authority = object.get("authority");
                    if (!StringUtils.isEmpty(authority)) {
                        convertedValue.add(new SimpleGrantedAuthority(authority));
                    }
                }
            }catch (Exception e) {
                log.error("### GrantedAuthorityConverter error", e);
            }

            return convertedValue;
        }
    }
}
