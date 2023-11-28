package com.codecool.tasx.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
  Optional<User> findOneByEmail(String email);


  Optional<User> findOneByEmailAndOAuth2User(String email, OAuth2User oauth2User);

  Optional<User> findOneByEmailAndOAuth2UserNot(String email);
}
