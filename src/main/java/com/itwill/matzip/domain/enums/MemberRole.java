package com.itwill.matzip.domain.enums;

public enum MemberRole {
	   USER("ROLE_USER"),
	   ADMIN("ROLE_ADMIN"),
	   GUEST("ROLE_GUEST");
	   
	   private String authority;
	   
	   MemberRole(String authority){
	      this.authority = authority;
	   }
	   
	   public String getAuthority() {
	      return this.authority;
	   }
	}
