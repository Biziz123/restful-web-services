package com.socialmedia.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.socialmedia.rest.webservices.restfulwebservices.jpa.PostRepository;
import com.socialmedia.rest.webservices.restfulwebservices.jpa.UserRepository;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {
  
	
	private UserRepository repository;
	private PostRepository postrepository;
	
	public UserJpaResource(UserRepository repository,PostRepository postrepository) {
		this.repository=repository;
		this.postrepository=postrepository;
	}
	
	
	@GetMapping("/jpa/users")
	public MappingJacksonValue retrieveAllUsers(){
		List<User> list=repository.findAll();
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","user_name");
		FilterProvider filterprovider = new SimpleFilterProvider().addFilter("mybean",filter);
		
		MappingJacksonValue jacksonValue = new MappingJacksonValue(list);
		jacksonValue.setFilters(filterprovider);
		return jacksonValue;
	}
	@GetMapping("/jpa/user/{id}")
	public MappingJacksonValue findUser(@PathVariable int id){
		Optional<User> user= repository.findById(id);
 		if(user.isEmpty()) {
			throw new UserNotFoundException("id:" + id) ;
		}
		
		EntityModel<User> entityModel = EntityModel.of(user.get());
		WebMvcLinkBuilder link =  linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));
		
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","user_name","birthDate");
		FilterProvider filterprovider = new SimpleFilterProvider().addFilter("mybean",filter);
		
		MappingJacksonValue jacksonValue = new MappingJacksonValue(entityModel);
		jacksonValue.setFilters(filterprovider);
		
		return jacksonValue;
	}
	
	@DeleteMapping("/jpa/user/{id}")
	public void deleteUser(@PathVariable int id){
		repository.deleteById(id);
	}
	@GetMapping("/jpa/user/{id}/posts")
	public List<Post> retrievePostsForUser(@PathVariable int id){
		Optional<User> user= repository.findById(id);
 		if(user.isEmpty()) {
			throw new UserNotFoundException("id:" + id) ;
		}
 		return user.get().getPosts();
	}
	
	@PostMapping("/jpa/user/{id}/posts")
	public ResponseEntity<Object> CreatePostsForUser(@PathVariable int id,@Valid @RequestBody Post post){
		Optional<User> user= repository.findById(id);
 		if(user.isEmpty()) {
			throw new UserNotFoundException("id:" + id) ;
		}
 		post.setUser(user.get());
 		Post savedpost=postrepository.save(post);
 		
 		URI location = 	ServletUriComponentsBuilder.fromCurrentRequest()
		        .path("/{id}")
		        .buildAndExpand(savedpost.getId())
		        .toUri();
return ResponseEntity.created(location).build();
	}
	
	@PostMapping("/jpa/user")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user){
		User savedUser= repository.save(user);
		URI location = 	ServletUriComponentsBuilder.fromCurrentRequest()
				        .path("/{id}")
				        .buildAndExpand(savedUser.getId())
				        .toUri();
		return ResponseEntity.created(location).build();
	}
}
