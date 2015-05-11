package model;

import java.util.ArrayList;

public class UserDatabase {
	static ArrayList<User> users = new ArrayList<User>();
	
	public static void putUser(User user){
		users.add(user);
	}
	
}
