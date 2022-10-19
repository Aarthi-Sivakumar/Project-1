package com.example.project1.Repositories;

import com.example.project1.Credentials.TokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity,Long> {

    default TokenEntity findByConfirmationToken(String tokenVerification){
        List<TokenEntity> token = (List<TokenEntity>) findAll();
        for (TokenEntity singletoken :token) {
            if(singletoken.getConfirmationtoken()!=null && singletoken.getConfirmationtoken().equals(tokenVerification)){
                return singletoken;
            }
        }
        return null;
    }
    }

