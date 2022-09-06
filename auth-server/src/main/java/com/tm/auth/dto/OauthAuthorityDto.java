package com.tm.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author tangming
 * @date 2022/9/6
 */
@Data
public class OauthAuthorityDto {
    @NotBlank(message = "不能为空")
    private String methods;
    @NotBlank(message = "不能为空")
    private String paths;
}
