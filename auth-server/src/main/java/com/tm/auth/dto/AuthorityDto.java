package com.tm.auth.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangming
 * @date 2022/9/6
 */
@Data
public class AuthorityDto {
    private String targetId;
    private List<OauthAuthorityDto> targetRules;

    public AuthorityDto() {
        targetRules = new ArrayList<>();
    }
}
