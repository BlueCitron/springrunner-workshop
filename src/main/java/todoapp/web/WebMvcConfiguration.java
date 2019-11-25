package todoapp.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import todoapp.commons.web.error.ReadableErrorAttributes;
import todoapp.commons.web.view.CommaSeparatedValuesView;
import todoapp.security.UserSessionRepository;
import todoapp.security.web.method.UserSessionHandlerMethodArgumentResolver;
import todoapp.security.web.servlet.RolesVerifyHandlerInterceptor;

/**
 * Spring Web MVC 설정
 *
 * @author springrunner.kr@gmail.com
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
	
	private UserSessionRepository userSessionRepository;
	
	public WebMvcConfiguration(UserSessionRepository userSessionRepository) {
		this.userSessionRepository = userSessionRepository;
	}

	// 1) 정적 자원 제공
	// http://localhost:8080/assets/css/todos.css
	
    @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	/*
		registry.addResourceHandler("/assets/**")
			    .addResourceLocations("assets/", "file:/Users/woowabros/springrunner/workshops/c101-6th/todos/files/assets/", "classpath:assets/");
		*/
    	
		// request: assets/css/servlet.css
		//         /assets/**
		//
		// webapp + assets/ + css/servlet.css
		
    	// URI 표기
		// http:
		// ftp:		
		// s3:
    	
    	// Resource Interface - Spring
    	// ResourceLoader Interface - Spring
	}
	
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
    	// registry.viewResolver(new TodoController.TodoCsvViewResolver());
    	
        // registry.enableContentNegotiation();
        // 위와 같이 직접 설정하면, 스프링부트가 구성한 ContentNegotiatingViewResolver 전략이 무시된다.
    }
    
    @Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new UserSessionHandlerMethodArgumentResolver(userSessionRepository));
	}
    
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RolesVerifyHandlerInterceptor());
	}

	@Bean
    public ErrorAttributes errorAttributes(MessageSource messageSource) {
    	return new ReadableErrorAttributes(messageSource);
    }

	/**
     * 스프링부트가 생성한 ContentNegotiatingViewResolver를 조작할 목적으로 작성된 컴포넌트
     */
    @Configuration
    public static class ContentNegotiationCustomizer {

    	@Autowired
        public void configurer(ContentNegotiatingViewResolver viewResolver) {
            List<View> defaultViews = new ArrayList<>(viewResolver.getDefaultViews());
        	defaultViews.add(new CommaSeparatedValuesView());
        	
        	viewResolver.setDefaultViews(defaultViews);
        }

    }

}
