package com.alberto.user_service.tests;

import com.alberto.user_service.entities.Role;
import com.alberto.user_service.entities.User;

public class UserFactory {
    
    public static User createClientUser(Long id, String username) {
		User user = new User(id, "João", username, "password");
		user.addRole(new Role(1L, "ROLE_CLIENT"));		
		return user;
	}
}
