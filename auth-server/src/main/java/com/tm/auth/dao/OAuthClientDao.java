package com.tm.auth.dao;

import com.tm.auth.po.OAuthClient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author tangming
 * @date 2022/8/11
 */
@Repository
public interface OAuthClientDao extends CrudRepository<OAuthClient, String> {
    Optional<OAuthClient> findByClientId(String ClientId);
}

