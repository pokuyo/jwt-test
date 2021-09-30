package kr.co.datarse.user.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.datarse.user.mapper.UserMapper;


@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	public Map<String, Object> retrieveUserInfo(String usrId) {
		Map<String, Object> returnData = userMapper.retrieveUserInfo(usrId); 
		return returnData;
	}
}
