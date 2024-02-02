package com.socialmedia.rest.webservices.restfulwebservices.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {
    
	
	private static List<User> users= new ArrayList<>();
	
	static {
		users.add(new User(1,"Adam",LocalDate.now().minusYears(30)));
		users.add(new User(2,"Amit",LocalDate.now().minusYears(20)));
		users.add(new User(3,"Sourav",LocalDate.now().minusYears(15)));
	}
	
	public List<User> findAll(){
		return users;
	}

	public User findUser(int id) {
		for(User a:users) {
			if(a.getId()==id) {
				return a;
			}
		}
		return null;
	}
	
	
			}
