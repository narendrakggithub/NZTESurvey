package com.nkg.pmbot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.env.Environment;

import com.nkg.pmbot.exception.AppException;
import com.nkg.pmbot.model.HexStrings;
import com.nkg.pmbot.model.Role;
import com.nkg.pmbot.model.RoleName;
import com.nkg.pmbot.model.User;
import com.nkg.pmbot.payload.ApiResponse;
import com.nkg.pmbot.payload.JwtAuthenticationResponse;
import com.nkg.pmbot.payload.LoginRequest;
import com.nkg.pmbot.payload.SignUpRequest;
import com.nkg.pmbot.repository.HexStringRepository;
import com.nkg.pmbot.repository.RoleRepository;
import com.nkg.pmbot.repository.UserRepository;
import com.nkg.pmbot.security.JwtTokenProvider;
import com.nkg.pmbot.service.PollService;
import com.nkg.pmbot.service.UserService;
import com.nkg.pmbot.util.AppConstants;
import com.nkg.pmbot.util.MyUtil;
import com.nkg.pmbot.util.SendMail;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	HexStringRepository hexStringRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	private UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		/*
		 * if(userRepository.existsByUsername(signUpRequest.getUsername())) { return new
		 * ResponseEntity(new ApiResponse(false, "Username is already taken!"),
		 * HttpStatus.BAD_REQUEST); }
		 */

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
		}

		// Creating user's account
		User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
				signUpRequest.getPassword());

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		User result = userService.createUser(user);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{username}")
				.buildAndExpand(result.getUsername()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
	}

}