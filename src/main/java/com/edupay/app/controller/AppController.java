package com.edupay.app.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.edupay.app.entity.App;
import com.edupay.app.service.AppService;
import com.edupay.commons.api.controller.ApiCommonController;
import com.edupay.commons.utils.PropertiesUtil;

@Controller
@RequestMapping("/edupay/app")
public class AppController extends ApiCommonController{

	@Autowired
	private AppService appService;
	private static Properties appConfig = PropertiesUtil.loadProperties("app-config.properties");
	
	@RequestMapping(value = "/getAppById", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getAppById(Model model, Integer appId, String password){
		// 验证登陆信息
		String pw = appConfig.getProperty("password");
		if(!pw.equals(password)){
			return null;
		}
		//
		App app = null;
		if(appId!=null){
			app = appService.getAppById(appId);
		}
		model.addAttribute("app", app);
		ModelAndView view = new ModelAndView("appManage/appInfo");
		return view;
	}
	
	@RequestMapping(value = "/updApp", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView updApp(Model model, App app, String password){
		// 验证登陆信息
		String pw = appConfig.getProperty("password");
		if(!pw.equals(password)){
			return null;
		}
		//
		int result = appService.updApp(app);
		RequestDispatcher rd = request.getRequestDispatcher("getAppList");
		try {
			rd.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/addApp", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView addApp(HttpServletRequest request, HttpServletResponse response, App app, String password){
		// 验证登陆信息
		String pw = appConfig.getProperty("password");
		if(!pw.equals(password)){
			return null;
		}
		//
		appService.add(app);
		RequestDispatcher rd = request.getRequestDispatcher("getAppList");
		try {
			rd.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
	public void test(Model model,HttpServletRequest request){
		try {
			PrintWriter pw = response.getWriter();
			request.setCharacterEncoding("utf-8");
			System.out.println("--1-----------ok");
			pw.print("ok");
			pw.flush();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/toPage", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView toPage(Model model,HttpServletRequest request, String page, String password){
		// 验证登陆信息
//		String pw = appConfig.getProperty("password");
//		if(!pw.equals(password)){
//			return null;
//		}
		//
		System.out.println("-------------"+page);
		ModelAndView view = new ModelAndView(page);
		return view;
	}
	
	@RequestMapping(value = "/getAppList", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getAppList(Model model, HttpServletRequest request, HttpServletResponse response, App app, String password){
		// 验证登陆信息
		String pw = appConfig.getProperty("password");
		if(!pw.equals(password)){
			return null;
		}
		//
		List<App> list = appService.getAppList();
		model.addAttribute("list", list);
		ModelAndView view = new ModelAndView("appManage/appManage");
		return view;
	}
}
