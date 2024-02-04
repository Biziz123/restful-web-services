package com.socialmedia.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;

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

import jakarta.validation.Valid;

@RestController
public class UserResource {
  
	
	private UserDaoService service;
	
	public UserResource(UserDaoService service) {
		this.service=service;
	}
	
	@GetMapping("/users")
	public MappingJacksonValue retrieveAllUsers(){
		List<User> list=service.findAll();
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","user_name");
		FilterProvider filterprovider = new SimpleFilterProvider().addFilter("mybean",filter);
		
		MappingJacksonValue jacksonValue = new MappingJacksonValue(list);
		jacksonValue.setFilters(filterprovider);
		return jacksonValue;
	}
	@GetMapping("/user/{id}")
	public MappingJacksonValue findUser(@PathVariable int id){
		User user= service.findUser(id);
		if(user==null) {
			throw new UserNotFoundException("id:" + id) ;
		}
		
		EntityModel<User> entityModel = EntityModel.of(user);
		WebMvcLinkBuilder link =  linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));
		
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","user_name","birthDate");
		FilterProvider filterprovider = new SimpleFilterProvider().addFilter("mybean",filter);
		
		MappingJacksonValue jacksonValue = new MappingJacksonValue(entityModel);
		jacksonValue.setFilters(filterprovider);
		
		return jacksonValue;
	}
	
	@DeleteMapping("/user/{id}")
	public void deleteUser(@PathVariable int id){
		service.deleteById(id);
	}
	
	@PostMapping("/user")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user){
		User savedUser= service.save(user);
		URI location = 	ServletUriComponentsBuilder.fromCurrentRequest()
				        .path("/{id}")
				        .buildAndExpand(savedUser.getId())
				        .toUri();
		return ResponseEntity.created(location).build();
	}
}
