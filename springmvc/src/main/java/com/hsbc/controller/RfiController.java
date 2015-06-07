package com.hsbc.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hsbc.service.RfiTaskService;

@Controller
public class RfiController {
	
	private static final Logger logger = LoggerFactory.getLogger(RfiController.class);
	
	protected RfiTaskService rfiTaskService;
	
	@Autowired
	public void setRfiTaskService(RfiTaskService rfiTaskService) {
		this.rfiTaskService = rfiTaskService;
	}

	@RequestMapping(value = "/submitrfi", method = RequestMethod.POST)
	public String submitRfi(HttpServletRequest request,Locale locale, Model model) {
		logger.info("submitting the rfi :" +request.getParameter("alertid"), locale);
		String username = (String) request.getSession().getAttribute("username");
		rfiTaskService.showTask(request.getParameter("alertid"), username);
		return "dashboard";
	}

}
