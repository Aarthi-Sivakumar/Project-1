package com.example.project1.Repositories;

import com.example.project1.Credentials.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginRepository extends CrudRepository<UserEntity,Long> {

    default UserEntity findByEmailIdIgnoreCase(String mail){
        List<UserEntity> mailid = (List<UserEntity>) findAll();
        for (UserEntity id :mailid) {
                if(id.getMail()!=null && id.getMail().equals(mail)){
                    return id;
                }
        }
        return null;
    }
}
