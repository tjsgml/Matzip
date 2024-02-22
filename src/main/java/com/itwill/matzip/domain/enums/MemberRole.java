package com.itwill.matzip.domain.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum MemberRole {
    USER("ROLE_USER", "유저"),
    ADMIN("ROLE_ADMIN", "어드민"),
    GUEST("ROLE_GUEST", "게스트");

    private String authority;
    private String kor;

    MemberRole(String authority, String kor) {
        this.authority = authority;
        this.kor = kor;
    }

    public String getAuthority() {
        return this.authority;
    }
}
