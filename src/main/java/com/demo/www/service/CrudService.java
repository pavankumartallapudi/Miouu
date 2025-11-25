package com.demo.www.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.www.model.Users;
import com.demo.www.repository.CrudRepository;

@Service
public class CrudService {
	
	@Autowired
	CrudRepository crudRepo;

	public List<Users> getAllUsers(){
		return crudRepo.findAll();
	}
	
	public Users addUser(Users user) {
		return crudRepo.save(user);
	}
	
	public Users updateUser(Long id,Users user) {
		Optional<Users> oldData=crudRepo.findById(id);
		if(oldData.isPresent()) {
			Users updatedData=oldData.get();
			updatedData.setUsername(user.getUsername());
			updatedData.setPass(user.getPass());
			updatedData.setEmail(user.getEmail());
			return crudRepo.save(updatedData);
		}
		else {
			return null;
		}
		
	}
	
	public String deleteUser(Long id) {
		Optional<Users> user=crudRepo.findById(id);
		if(user.isPresent()) {
			crudRepo.deleteById(id);
			return "user deleted";
		}
		else {
			return "deletion not successful";
		}
	}
}
