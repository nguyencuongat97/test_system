package com.foxconn.fii.security.jwt.model.token.extractor;

public interface TokenExtractor {
    String extract(String payload);
}
