package com.hsbc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RfiTaskService {

	private TaskService taskService;
	private RuntimeService runTimeService;
	private RepositoryService repositoryService;
	private FormService formService;

	private static final Logger logger = LoggerFactory
			.getLogger(RfiTaskService.class);

	@Autowired
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	@Autowired
	public void setRunTimeService(RuntimeService runTimeService) {
		this.runTimeService = runTimeService;
	}

	@Autowired
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	@Autowired
	public void setFormService(FormService formService) {
		this.formService = formService;
	}

	public void showTask(String alertid, String username) {
		logger.info("show task for the user :" + username);

		logger.info("Number of process definitions: "
				+ repositoryService.createProcessDefinitionQuery().count());
		logger.info("id = "
				+ repositoryService.createProcessDefinitionQuery().active()
						.singleResult().getId());
		logger.info("name = "
				+ repositoryService.createProcessDefinitionQuery().active()
						.singleResult().getName());

		logger.info("No of process instance of the process: "
				+ Long.toString(runTimeService.createProcessInstanceQuery()
						.count()));

		List<Task> listOfTask = null;
		String taskId = null;

		if (username.equals("kermit")) {
			listOfTask = taskService.createTaskQuery().taskAssignee(username)
					.list();
		} else {
			listOfTask = taskService.createTaskQuery()
					.taskCandidateUser(username).list();
		}

		logger.info("no of task for the user " + username + " : "
				+ listOfTask.size());

		for (Task task : listOfTask) {
			logger.info(task.getId());
			logger.info(task.getAssignee());
			logger.info(task.getName());
			taskId = task.getId();
			// logger.info(task.getAssignee());
		}
		Map<String, Object> taskVariables = new HashMap<String, Object>();
		taskVariables.put("alertid", alertid);
		
		try {
			if (taskId != null) {
				logger.info("no of task for the user in the task form data "
						+ username + " : "
						+ formService.getTaskFormData(taskId).getFormProperties().size());
				
				for (FormProperty formprop : formService.getTaskFormData(taskId).getFormProperties()) {
					logger.info(formprop.getValue() + formprop.getId());
				}
			}
		} catch (ActivitiObjectNotFoundException act) {

		}
		
		if (taskId != null) {
			taskService.complete(taskId, taskVariables);
		}

	}

}
