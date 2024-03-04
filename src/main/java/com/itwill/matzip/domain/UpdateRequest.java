package com.itwill.matzip.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

	@JsonInclude
	@Fetch(FetchMode.JOIN)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RESTAURANT_FK")
	private Restaurant restaurant;
	
	@Basic(optional = false)
	private String content;

	@JsonInclude
	@Fetch(FetchMode.JOIN)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private ApprovalStatus status = ApprovalStatus.WAITING;//기본으로 승인 대기,,

	public void completeRequest () {
		this.status = ApprovalStatus.APPROVED;
	}
	
}
