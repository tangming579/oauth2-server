package com.tm.auth.dto;

import lombok.Data;

/**
 * @author tangming
 * @date 2022/8/30
 */
@Data
public class PublicKeyDto {
    private String clientId;
    private String publicKey;
}
