package com.alberto.user_service.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alberto.user_service.dto.UserDTO;
import com.alberto.user_service.dto.UserMinDTO;
import com.alberto.user_service.dto.UserNewDTO;
import com.alberto.user_service.entities.Role;
import com.alberto.user_service.entities.User;
import com.alberto.user_service.projections.UserDetailsProjection;
import com.alberto.user_service.repositories.RoleRepository;
import com.alberto.user_service.repositories.UserRepository;
import com.alberto.user_service.services.exceptions.BadRequestException;
import com.alberto.user_service.util.CustomUserUtil;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private CustomUserUtil customUserUtil;

	@Autowired
	@Lazy
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
		if (result.size() == 0) {
			throw new UsernameNotFoundException("Email not found");
		}

		User user = new User();
		user.setEmail(result.get(0).getUsername());
		user.setPassword(result.get(0).getPassword());
		for (UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}

		return user;
	}

	protected User authenticated() {
		try {
			String username = customUserUtil.getLoggedUsername();
			return repository.findByEmail(username).get();
		} catch (Exception e) {
			throw new UsernameNotFoundException("Email not found");
		}
	}

	@Transactional
	public UserMinDTO insert(UserNewDTO userNewDto) {

		if (repository.findByEmail(userNewDto.getEmail()).isPresent()) {
			throw new BadRequestException("Email already exists");
		}

		if (!userNewDto.getPassword().equals(userNewDto.getConfirmPassword())) {
			throw new BadRequestException("Passwords do not match");
		}
		User entity = new User();
		entity.setName(userNewDto.getName());
		entity.setEmail(userNewDto.getEmail());
		entity.setPassword(passwordEncoder.encode(userNewDto.getPassword()));
		Role role = roleRepository.findById(1L).get();
		entity.addRole(role);
		entity = repository.save(entity);
		return new UserMinDTO(entity);
	}

	@Transactional(readOnly = true)
	public UserDTO getMe() {
		User user = authenticated();
		return new UserDTO(user);
	}

	@Transactional(readOnly = true)
	public UserDTO findByEmail(String email) {
		User user = repository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Email not found"));
		return new UserDTO(user.getId(), user.getEmail());
	}

}
