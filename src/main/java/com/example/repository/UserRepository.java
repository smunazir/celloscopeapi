package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Integer> {

}
