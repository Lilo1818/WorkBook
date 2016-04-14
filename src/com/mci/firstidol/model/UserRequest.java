package com.mci.firstidol.model;


public class UserRequest extends BaseRequest {
	/**
	 * 登录
	 * @return
	 */
	public static LoginRequest loginRequest() {
		return new LoginRequest();
	}
	public static class LoginRequest extends UserRequest{
		private String userName;
		private String password;
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
	}
	
	public static RegisterRequest registerRequest() {
		return new RegisterRequest();
	}
	
	public static class RegisterRequest extends UserRequest{
		public RegUser regUser;
	}
	public static RegUser regUser(){
		return new RegUser();
	}
	public static class RegUser{
		public String UserName;
		public String Password;
		public String NickName;
		public String ValidateCode;
	}
	
	
	public static PWDUpdateRequest pwdUpdateRequest() {
		return new PWDUpdateRequest();
	}
	
	public static class PWDUpdateRequest extends UserRequest{
		public String userName;//ss",
		public String password;//00",
		public String validateCode;
	}
	
	
	
	public static LoginThirdRequest loginThirdRequest() {
		return new LoginThirdRequest();
	}
	
	public static class LoginThirdRequest extends UserRequest{
		public Socialuser socialuser;
	}
	public static Socialuser socialuser(){
		return new Socialuser();
	}
	public static class Socialuser{
		public int Platform;
		public String OpenId;
		public String Token;
		public String Avatar;
		public String AppId;
		public String NickName;
	}
	
	
	
}
