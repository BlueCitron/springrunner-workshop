package todoapp.core.todos.infrastructure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import todoapp.core.todos.domain.Todo;
import todoapp.core.todos.domain.TodoRepository;

@Component
public class TodoRegister 
  implements InitializingBean
           , CommandLineRunner
           , ApplicationRunner {

	private TodoRepository todoRepository;
	
	public TodoRegister(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		todoRepository.save(Todo.create("Task one"));
	}

	@Override
	public void run(String... args) throws Exception {
		// CommandLineRunner
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// ApplicationRunner
		todoRepository.save(Todo.create("Task two"));
	}

}
