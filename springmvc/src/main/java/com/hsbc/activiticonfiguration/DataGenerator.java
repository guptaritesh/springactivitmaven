package com.hsbc.activiticonfiguration;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.repository.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hsbc.controller.HomeController;

public class DataGenerator {

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	protected ProcessEngine processEngine;
	protected IdentityService identityService;

	public void setProcessEngine(ProcessEngine processEngine) {
		logger.info("Setting up the process engine");
		this.processEngine = processEngine;
		this.identityService = processEngine.getIdentityService();
		initDemoGroups();
		initDemoUsers();
		initProcessDefinitions();
	}

	protected void initDemoGroups() {
		logger.info("initiating the demo group");
		String[] assignmentGroups = new String[] { "management", "sales",
				"marketing", "engineering" };
		for (String groupId : assignmentGroups) {
			createGroup(groupId, "assignment");
		}

		String[] securityGroups = new String[] { "user", "admin" };
		for (String groupId : securityGroups) {
			createGroup(groupId, "security-role");
		}
	}

	protected void createGroup(String groupId, String type) {
		logger.info("creating the group : " + groupId + " : " + type);
		if (identityService.createGroupQuery().groupId(groupId).count() == 0) {
			Group newGroup = identityService.newGroup(groupId);
			newGroup.setName(groupId.substring(0, 1).toUpperCase()
					+ groupId.substring(1));
			newGroup.setType(type);
			identityService.saveGroup(newGroup);
		}
	}

	protected void initDemoUsers() {
		logger.info("initiating the users");
		createUser("kermit", "Kermit", "The Frog", "kermit",
				"kermit@activiti.org", "images/kermit.jpg", Arrays.asList(
						"management", "sales", "marketing", "engineering",
						"user", "admin"), Arrays.asList("birthDate",
						"10-10-1955", "jobTitle", "Muppet", "location",
						"Hollywoord", "phone", "+123456789", "twitterName",
						"alfresco", "skype", "activiti_kermit_frog"));

		createUser("gonzo", "Gonzo", "The Great", "gonzo",
				"gonzo@activiti.org", "images/gonzo.jpg",
				Arrays.asList("management", "sales", "marketing", "user"), null);
		createUser("fozzie", "Fozzie", "Bear", "fozzie", "fozzie@activiti.org",
				"images/fozzie.jpg",
				Arrays.asList("marketing", "engineering", "user"), null);
	}

	protected void createUser(String userId, String firstName, String lastName,
			String password, String email, String imageResource,
			List<String> groups, List<String> userInfo) {
		logger.info("creating the user :" + firstName + " : " + lastName );
		if (identityService.createUserQuery().userId(userId).count() == 0) {

			// Following data can already be set by demo setup script

			User user = identityService.newUser(userId);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setPassword(password);
			user.setEmail(email);
			identityService.saveUser(user);

			if (groups != null) {
				for (String group : groups) {
					identityService.createMembership(userId, group);
				}
			}
		}

		// Following data is not set by demo setup script

		// image
		if (imageResource != null) {
			byte[] pictureBytes = IoUtil.readInputStream(this.getClass()
					.getClassLoader().getResourceAsStream(imageResource), null);
			Picture picture = new Picture(pictureBytes, "image/jpeg");
			identityService.setUserPicture(userId, picture);
		}

		// user info
		if (userInfo != null) {
			for (int i = 0; i < userInfo.size(); i += 2) {
				identityService.setUserInfo(userId, userInfo.get(i),
						userInfo.get(i + 1));
			}
		}

	}

	protected void initProcessDefinitions() {
		logger.info("deploying the process"); 
		Deployment deployment = processEngine.getRepositoryService().createDeployment()
				.name("Demo processes")
				.addClasspathResource("diagrams/process.bpmn20.xml").deploy();
		
		logger.info("deployment id : " + deployment.getId()); 
		processEngine.getRuntimeService().startProcessInstanceById(processEngine.getRepositoryService().createProcessDefinitionQuery().active().singleResult().getId());
		logger.info("deployment name : " + deployment.getName()); 
		logger.info("deployed the process"); 
	}

}
