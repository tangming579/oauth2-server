package com.tm.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tm.auth.mbg.model.OauthClientDetails;
import lombok.Data;

import java.util.Date;

/**
 * @author tangming
 * @date 2022/9/5
 */

public class OauthClientDetailsDto extends OauthClientDetails {
    @Override
    @JsonIgnore
    public String getClientSecret() {
        return super.getClientSecret();
    }
}
