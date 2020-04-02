package boardSample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
	@GetMapping("loginForm")
	public String viewLoginForm() {
		return "login/loginForm";
	}
}
