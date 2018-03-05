package rva.ctrls;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloRestController {
	
	@RequestMapping("/")
	public String helloWorld() {
		return "Hello world!";
	}
	
	@RequestMapping("zbir")
	public String zbir() {
		long x = Math.round(Math.random()*10); // Metoda random vraca neki decimalni broj
		                                       // koji mnozimo sa 10 da bi dobili ceo broj
		long y = Math.round(Math.random()*10);
		return x+" + "+y+" = "+(x+y);
	}
}
