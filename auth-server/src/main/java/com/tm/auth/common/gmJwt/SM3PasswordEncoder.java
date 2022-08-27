package com.tm.auth.common.gmJwt;

import com.tm.auth.common.utils.SMUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

/**
 * @author: tangming
 * @date: 2022-08-27
 */
@Slf4j
public class SM3PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        } else {
            return SMUtils.sm3HashHex(rawPassword.toString().getBytes());
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        } else if (encodedPassword != null && encodedPassword.length() != 0) {
            String encodePassword = encode(rawPassword);
            return encodePassword.equalsIgnoreCase(encodedPassword);
        } else {
            log.warn("Empty encoded password");
            return false;
        }
    }
}
