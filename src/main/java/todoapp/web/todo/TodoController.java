package todoapp.web.todo;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractView;

import todoapp.commons.domain.Spreadsheet;
import todoapp.core.todos.application.TodoFinder;
import todoapp.core.todos.domain.Todo;
import todoapp.web.convert.TodoToSpreadsheetConverter;
import todoapp.web.model.SiteProperties;

@Controller
public class TodoController {
	
	private Environment env;
	
	private String siteAuthor;
	
	private SiteProperties siteProperties;
	
	private TodoFinder finder;
	
	public TodoController(Environment env, SiteProperties siteProperties, TodoFinder finder) {
		this.env = env;
		this.siteProperties = siteProperties;
		this.finder = finder;
	}
	
	@Value("${site.author}")
	public void setSiteAuthor(String siteAuthor) {
		this.siteAuthor = siteAuthor;
	}

	@RequestMapping("/todos")
	public void todos(Model model) {
		// SiteProperties site = new SiteProperties();
		// site.setAuthor(env.getProperty("site.author");
		// site.setAuthor(this.siteAuthor);
		
		// ModelAndView mv = new ModelAndView();
		// mv.addObject("site", siteProperties);
		// mv.setViewName("todos");
		
		// View view;
		// ViewResolver viewResolver;		
		// 접두사: "classpath:templates"
		// viewName: "todos"
		// 꼬릿말: ".html"
		
		// model.addAttribute("site", siteProperties);
		// return "todos";
	}
	
	@RequestMapping(value = "/todos", produces = { "text/csv" })
	public void downloadTodos(Model model) {
		List<Todo> todos = finder.getAll();
		Spreadsheet spreadsheet = new TodoToSpreadsheetConverter().convert(todos);
		
		model.addAttribute(spreadsheet);
	}
	
	static class TodoCsvView extends AbstractView implements View {
		
		private final Logger log = LoggerFactory.getLogger(getClass());

		public TodoCsvView() {
			setContentType("text/csv");
		}
		
		@Override
		protected boolean generatesDownloadContent() {
			return true;
		}

		@Override
		protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			log.info("write response to csv");
			
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"todos.csv\"");
			response.getWriter().println("id,title,completed");
			
			List<Todo> todos = (List<Todo>) model.getOrDefault("todos", Collections.emptyList());
			for (Todo todo : todos) {
				String line = String.format("%d,%s,%s", todo.getId(), todo.getTitle(), todo.isCompleted());
				response.getWriter().println(line);
			}
			
			response.flushBuffer();
		}
		
	}
	
	public static class TodoCsvViewResolver implements ViewResolver {

		@Override
		public View resolveViewName(String viewName, Locale locale) throws Exception {
			if (Objects.equals("todos", viewName)) {
				return new TodoCsvView();
			}			
			return null;
		}
		
	}

}
