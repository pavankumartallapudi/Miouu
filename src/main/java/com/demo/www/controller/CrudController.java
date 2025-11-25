package com.demo.www.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.www.model.Users;
import com.demo.www.service.CrudService;

@RestController
public class CrudController {

	@Autowired
	CrudService crudService;
	
	@GetMapping("/getAllUsers")
	public List<Users> getAllUsers(){
		return crudService.getAllUsers();
	}
	
	@PostMapping("/addUser")
	public Users addUser(@RequestBody Users user) {
		return crudService.addUser(user);
	}
	
	@PutMapping("/updateUser/{id}")
	public Users updateUser(@PathVariable Long id,@RequestBody Users user) {
		return crudService.updateUser(id,user);
	}
	
	@DeleteMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable Long id) {
		return crudService.deleteUser(id);
	}
}
