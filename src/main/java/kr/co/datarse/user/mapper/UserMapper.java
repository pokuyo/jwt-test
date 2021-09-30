package kr.co.datarse.user.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

	public Map<String, Object> retrieveUserInfo(String usrId);
	
}
