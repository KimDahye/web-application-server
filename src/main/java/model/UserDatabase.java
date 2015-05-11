package model;

import java.util.HashMap;
import java.util.Map;

public class UserDatabase {
	static Map<String, User> users = new HashMap<String, User>();
	
	public static void putUser(User user) {
		users.put(user.getUserId(), user);
	}
	
	public static boolean isCorrectUser(User user) {
		//id가 존재하지 않으면 올바르지 않은 유저
		if(!users.containsKey(user.getUserId())) {
			return false;
		}
		//id와 password가 저장된 값들과 일치하지 않으면 올바르지 않은 유저
		if(!users.get(user.getUserId()).equals(user)){
			return false;
		}
		
		return true;
	}
}
