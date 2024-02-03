package com.socialmedia.rest.webservices.restfulwebservices.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {
    
	
	private static List<User> users= new ArrayList<>();
	
	private static int usersCount=1;
	
	static {
		users.add(new User(usersCount++,"Adam",LocalDate.now().minusYears(30)));
		users.add(new User(usersCount++,"Amit",LocalDate.now().minusYears(20)));
		users.add(new User(usersCount++,"Sourav",LocalDate.now().minusYears(15)));
	}
	
	public List<User> findAll(){
		return users;
	}

	public void deleteById(int id) {
		for(User a:users) {
			if(a.getId()==id) {
				users.remove(a);
			}
		}
	}
	
	public User findUser(int id) {
		for(User a:users) {
			if(a.getId()==id) {
				return a;
			}
		}
		return null;
	}
	
	public User save(User user) {
		user.setId(usersCount++);
		users.add(user);
		return user;
	}
	
	
			}
