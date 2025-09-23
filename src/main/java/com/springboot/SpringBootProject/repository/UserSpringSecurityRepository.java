package com.springboot.SpringBootProject.repository;

import com.springboot.SpringBootProject.entity.UserSpringSecurity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserSpringSecurityRepository extends MongoRepository<UserSpringSecurity, ObjectId> {

    UserSpringSecurity findByUserName(String userName);
}
