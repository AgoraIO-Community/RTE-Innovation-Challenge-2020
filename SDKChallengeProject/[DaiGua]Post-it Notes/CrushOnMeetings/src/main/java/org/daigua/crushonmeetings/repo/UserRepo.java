package org.daigua.crushonmeetings.repo;

import org.daigua.crushonmeetings.controller.UserController;
import org.daigua.crushonmeetings.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserModel, String> {

    UserModel findByUid(String uid);

    UserModel findByUsername(String username);

}
