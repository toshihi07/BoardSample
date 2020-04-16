package boardSample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import boardSample.entity.RoleName;
import boardSample.entity.User;
import boardSample.repository.UserRepository;

@Controller
public class UserController{
	
	@Autowired
	PasswordEncoder passwdEncoder;
	
	//この2つのコードの違いがわからない。
	@Autowired
	UserRepository repository;
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/signup")
	public ModelAndView add(@ModelAttribute("formModel")User user,ModelAndView mav){
		mav.setViewName("user/signup");
		return mav;
	}

	@RequestMapping(value = "/signup" , method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public ModelAndView form(@ModelAttribute("formModel")User user, ModelAndView mav){
		if(userRepository.countByUsername(user.getUsername()) > 0){
			mav.addObject("obj", user.getUsername() + "は既に使用されてるユーザ名です。");
			mav.setViewName("user/signup");
		} else {
			user.setPassword(passwdEncoder.encode(user.getPassword()));
			user.setEmail(user.getEmail());
			user.setRoleName("User");
			repository.saveAndFlush(user);
			mav.addObject("user", user.getUsername() + "さん登録完了しました。");
			mav.setViewName("login/loginForm");
		}
		return mav;
	}
}