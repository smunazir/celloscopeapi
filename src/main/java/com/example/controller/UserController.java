package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;


import com.example.model.UserModel;
import com.example.repository.UserRepository;

@RestController

@CrossOrigin(origins = "*")
public class UserController {

	Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	public UserRepository userRepository;

	
	@PostMapping("/add")
	public ResponseEntity<Map> save(@RequestBody UserModel user) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Optional<UserModel> dbuser = userRepository.findById(user.getUserId());

			if (!dbuser.isEmpty()) {
				map.put("message", "user already exists");
				map.put("status", 403);
				return ResponseEntity.status(403).body(map);

			} else {
				user = userRepository.save(user);
				map.put("data", user);
				map.put("status", 200);
				map.put("message", "user successfully added");

				logger.info("user added");
				return ResponseEntity.ok(map);
			}

		} catch (Exception e) {
			map.put("message", "user added failed");
			map.put("Data", null);
			map.put("error", e.getLocalizedMessage());
			logger.error("userId already in use");
			return ResponseEntity.status(500).body(map);
		}

	}
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserModel user) {
		List<UserModel> users = (List<UserModel>) userRepository.findAll();
		Map<String, Object> map = new HashMap<String, Object>();

		for (UserModel other : users) {
			if (other.getUserId() == (user.getUserId()) && other.getPassword().equals(user.getPassword())) {
				map.put("message", "Login Successful");
				map.put("status", "success");
				map.put("data", other);
				logger.info("login success");
				return ResponseEntity.ok(map);
			}
		}
		map.put("message", "Login fail!");
		map.put("status", "Failed");
		map.put("data", null);
		logger.info("login failed");
		return ResponseEntity.status(409).body(map);
	}
	

	@PostMapping("/reset_password")
	public ResponseEntity<Map<String, Object>> resetpass(@RequestBody UserModel user) {

		Map<String, Object> map = new HashMap<String, Object>();
		UserModel dbuser = userRepository.findById(user.getUserId()).get();
		if (user.getUserId() == (user.getUserId()) && dbuser.getMobile().equals(user.getMobile())) {
			map.put("message", "Provided  Information Matched");
			map.put("status", "success");
			map.put("data", dbuser);
			logger.info("user information matched");
			return ResponseEntity.ok(map);
		}

		map.put("message", "not matched");
		map.put("status", "failed");
		map.put("data", null);
		logger.error("user information not matched");
		return ResponseEntity.status(409).body(map);
	}
	
	
	@PutMapping("/update_password")
	public ResponseEntity<Map> update(@RequestBody UserModel user) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Optional<UserModel> dbuser = userRepository.findById(user.getUserId());
			if (dbuser.isEmpty()) {
				map.put("message", "user not found");
				map.put("status", 403);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
			} else {
				user = userRepository.save(user);
				map.put("data", user);
				map.put("status", 200);
				map.put("message", "data successfully updated");
				logger.info("Upadated user password");
				return ResponseEntity.ok(map);
			}

		} catch (Exception e) {
			map.put("message", "data failed to update");
			map.put("error", e.getLocalizedMessage());
			logger.info("password failed to update");
			return ResponseEntity.status(500).body(map);

		}

	}


}
