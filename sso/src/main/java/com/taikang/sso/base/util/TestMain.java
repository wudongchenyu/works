package com.taikang.sso.base.util;

import org.mindrot.jbcrypt.BCrypt;

public class TestMain {
	
	public static void main(String[] args) {
		String pass = "111111";
		String hashpw = BCrypt.hashpw(pass, BCrypt.gensalt());
		System.out.println(hashpw);
		boolean checkpw = BCrypt.checkpw(pass, hashpw);
		System.out.println(checkpw);
	}

}
