package com.foxconn.fii.security.jwt.config;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class PathRequestMatcher implements RequestMatcher {
    private OrRequestMatcher matchers;

    public PathRequestMatcher(List<String> paths) {
        Assert.notNull(paths, "paths must be not null.");
        List<RequestMatcher> m = paths.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList());
        matchers = new OrRequestMatcher(m);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return matchers.matches(request);
    }
}
