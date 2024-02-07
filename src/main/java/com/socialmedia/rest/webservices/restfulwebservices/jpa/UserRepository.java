package com.socialmedia.rest.webservices.restfulwebservices.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.socialmedia.rest.webservices.restfulwebservices.user.User;

public interface UserRepository extends JpaRepository<User,Integer> {
	

}
