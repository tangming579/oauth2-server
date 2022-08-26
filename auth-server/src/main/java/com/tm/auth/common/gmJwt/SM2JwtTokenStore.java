package com.tm.auth.common.gmJwt;

import com.tm.auth.common.converter.SM2JwtAccessTokenConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.*;

/**
 * @author tangming
 * @date 2022/8/22
 */
public class SM2JwtTokenStore implements TokenStore {
    private SM2JwtAccessTokenConverter jwtTokenEnhancer;
    private ApprovalStore approvalStore;

    public SM2JwtTokenStore(SM2JwtAccessTokenConverter jwtTokenEnhancer) {
        this.jwtTokenEnhancer = jwtTokenEnhancer;
    }

    public void setApprovalStore(ApprovalStore approvalStore) {
        this.approvalStore = approvalStore;
    }

    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return this.readAuthentication(token.getValue());
    }

    public OAuth2Authentication readAuthentication(String token) {
        return this.jwtTokenEnhancer.extractAuthentication(this.jwtTokenEnhancer.decode(token));
    }

    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
    }

    public OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AccessToken accessToken = this.convertAccessToken(tokenValue);
        return accessToken;
    }

    private OAuth2AccessToken convertAccessToken(String tokenValue) {
        return this.jwtTokenEnhancer.extractAccessToken(tokenValue, this.jwtTokenEnhancer.decode(tokenValue));
    }

    public void removeAccessToken(OAuth2AccessToken token) {
    }

    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
    }

    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        OAuth2AccessToken encodedRefreshToken = this.convertAccessToken(tokenValue);
        OAuth2RefreshToken refreshToken = this.createRefreshToken(encodedRefreshToken);
        if (this.approvalStore != null) {
            OAuth2Authentication authentication = this.readAuthentication(tokenValue);
            if (authentication.getUserAuthentication() != null) {
                String userId = authentication.getUserAuthentication().getName();
                String clientId = authentication.getOAuth2Request().getClientId();
                Collection<Approval> approvals = this.approvalStore.getApprovals(userId, clientId);
                Collection<String> approvedScopes = new HashSet();
                Iterator var9 = approvals.iterator();

                while(var9.hasNext()) {
                    Approval approval = (Approval)var9.next();
                    if (approval.isApproved()) {
                        approvedScopes.add(approval.getScope());
                    }
                }

                if (!approvedScopes.containsAll(authentication.getOAuth2Request().getScope())) {
                    return null;
                }
            }
        }

        return refreshToken;
    }

    private OAuth2RefreshToken createRefreshToken(OAuth2AccessToken encodedRefreshToken) {
        return new DefaultOAuth2RefreshToken(encodedRefreshToken.getValue());
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return this.readAuthentication(token.getValue());
    }

    public void removeRefreshToken(OAuth2RefreshToken token) {
        this.remove(token.getValue());
    }

    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
    }

    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        return null;
    }

    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        return Collections.emptySet();
    }

    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        return Collections.emptySet();
    }

    public void setTokenEnhancer(SM2JwtAccessTokenConverter tokenEnhancer) {
        this.jwtTokenEnhancer = tokenEnhancer;
    }

    private void remove(String token) {
        if (this.approvalStore != null) {
            OAuth2Authentication auth = this.readAuthentication(token);
            String clientId = auth.getOAuth2Request().getClientId();
            Authentication user = auth.getUserAuthentication();
            if (user != null) {
                Collection<Approval> approvals = new ArrayList();
                Iterator var6 = auth.getOAuth2Request().getScope().iterator();

                while(var6.hasNext()) {
                    String scope = (String)var6.next();
                    approvals.add(new Approval(user.getName(), clientId, scope, new Date(), Approval.ApprovalStatus.APPROVED));
                }

                this.approvalStore.revokeApprovals(approvals);
            }
        }

    }
}
