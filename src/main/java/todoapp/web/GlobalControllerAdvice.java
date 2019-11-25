package todoapp.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import todoapp.web.model.SiteProperties;

@ControllerAdvice
public class GlobalControllerAdvice {

	private SiteProperties siteProperties;
	
	public GlobalControllerAdvice(SiteProperties siteProperties) {
		this.siteProperties = siteProperties;
	}

	@ModelAttribute("site")
	public SiteProperties siteProperties() {
		return siteProperties;
	}
	
	/*
	@ExceptionHandler(Exception.class)
	public ModelAndView handlerException() {
		return null;
	}
	*/
	
}
