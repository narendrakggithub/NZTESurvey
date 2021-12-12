package com.nkg.pmbot.service;

import com.nkg.pmbot.exception.AppException;
import com.nkg.pmbot.exception.BadRequestException;
import com.nkg.pmbot.exception.ResourceNotFoundException;
import com.nkg.pmbot.model.*;
import com.nkg.pmbot.payload.PagedResponse;
import com.nkg.pmbot.payload.PollRequest;
import com.nkg.pmbot.payload.PollResponse;
import com.nkg.pmbot.payload.VoteRequest;
import com.nkg.pmbot.repository.HexStringRepository;
import com.nkg.pmbot.repository.PollRepository;
import com.nkg.pmbot.repository.RoleRepository;
import com.nkg.pmbot.repository.UserRepository;
import com.nkg.pmbot.repository.VoteRepository;
import com.nkg.pmbot.security.UserPrincipal;
import com.nkg.pmbot.util.AppConstants;
import com.nkg.pmbot.util.ModelMapper;
import com.nkg.pmbot.util.MyUtil;
import com.nkg.pmbot.util.SendMail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserService {

	@Autowired
	HexStringRepository hexStringRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	private Environment appProperties;

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Transactional
	public User createUser(User user) {

		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
				.orElseThrow(() -> new AppException("User Role not set."));

		user.setRoles(Collections.singleton(userRole));

		User result = userRepository.save(user);

		logger.info("User created:", user.getId());

		String hex = MyUtil.getRandomHexString();
		String validateLink = "https://localhost:3030/validate?id=" + hex;

		HexStrings hexString = new HexStrings(hex, "USER", "VALIDATE", "VALIDATE", result.getId());

		hexStringRepository.save(hexString);

		logger.info("HexString created:", hexString);

		SendMail.sendEmail(user.getEmail(), appProperties.getProperty(AppConstants.PROP_VALIDATE_EMAIL_SUB),
				appProperties.getProperty(AppConstants.PROP_VALIDATE_EMAIL_BODY).replace("{0}", validateLink), true, null);

		logger.info("User creation email sent.", user.getId());
		
		return result;
	}

}