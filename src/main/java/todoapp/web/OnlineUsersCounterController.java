package todoapp.web;

import java.util.stream.IntStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import todoapp.web.support.ConnectedClientCountBroadcaster;

@Controller
public class OnlineUsersCounterController {
	
	private ConnectedClientCountBroadcaster broadcaster = new ConnectedClientCountBroadcaster();
	
	
	@RequestMapping(value = "/stream/online-users-counter", produces = "text/event-stream")
	public SseEmitter counter() {
		return broadcaster.subscribe();
	}
	
	// @RequestMapping(value = "/stream/online-users-counter", produces = "text/event-stream")
	public void counter(HttpServletResponse response) {
		response.setContentType("text/event-stream");
		
		IntStream.range(1, 11).forEach(number -> {
			try {
				Thread.sleep(500);
				
				ServletOutputStream outputStream = response.getOutputStream();
				outputStream.write(("data: " + number + "\n\n").getBytes());
				outputStream.flush();
			} catch (Exception ignore) {

			}
		});
	}

}
