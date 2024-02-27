package com.itwill.matzip.domain;

import com.itwill.matzip.domain.enums.ApprovalStatus;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "UPDATE_REQUEST")
public class UpdateRequest implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UPDATE_REQUEST_PK")
	private Long id;
	
	@Basic(optional = false)
	private Long restId;
	
	@Basic(optional = false)
	private String content;
	
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private ApprovalStatus status = ApprovalStatus.WAITING;//기본으로 승인 대기,,
	
	
}
