package com.hsbc.service;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthenticateService {

	private IdentityService identityService;
	private static final Logger logger = LoggerFactory.getLogger(AuthenticateService.class);

	
	@Autowired
	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

	public void authenticate(String userName, String password) {
		logger.info("Authenticating usename and password");

		if (identityService.checkPassword(userName, password)) {
			User user = identityService.createUserQuery().userId(userName)
					.singleResult();
			// Fetch and cache user data
			logger.info("verified username and password"+user.getFirstName()+ user.getLastName());
			
			List<Group> groups = identityService.createGroupQuery()
					.groupMember(user.getId()).list();
			for (Group group : groups) {
				//if (Constants.SECURITY_ROLE.equals(group.getType())) {
					//loggedInUser.addSecurityRoleGroup(group);
					//if (Constants.SECURITY_ROLE_USER.equals(group.getId())) {
						//loggedInUser.setUser(true);
					//}
					//if (Constants.SECURITY_ROLE_ADMIN.equals(group.getId())) {
						//loggedInUser.setAdmin(true);
					//}
			//	} else {
					//loggedInUser.addGroup(group);
				//}
			}

		} else {
			
			logger.info("Invaliduser");
		}

	}

}
