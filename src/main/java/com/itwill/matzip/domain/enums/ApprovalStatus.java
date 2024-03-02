package com.itwill.matzip.domain.enums;

import lombok.Getter;

@Getter
public enum ApprovalStatus {
	WAITING ("대기"),
	APPROVED("완료");


    ApprovalStatus(String desc) {
        this.desc = desc;
    }

	private final String desc;
}
