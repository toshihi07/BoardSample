package boardSample.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import boardSample.entity.User;
import boardSample.repository.UserRepository;

@Service
@Transactional
public class UserService {
	@Autowired
	UserRepository userRepository;
	
	public User getUser(int user_id) {
		return userRepository.findByUserId(user_id);		
	}
}
