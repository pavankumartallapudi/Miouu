package com.demo.www.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.www.model.Users;

@Repository
public interface CrudRepository extends JpaRepository<Users,Long> {

}
