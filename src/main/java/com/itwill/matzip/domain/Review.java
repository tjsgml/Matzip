package com.itwill.matzip.domain;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@Entity
public class Review extends BaseTimeEntity{ 
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "REVIEW_PK")
	private Long id;
	
	@EqualsAndHashCode.Include
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RESTAURANT_FK")
	@Basic(optional = false)
	private Restaurant restaurant;
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "MEMBER_FK")
	@Basic(optional = false)
	private Member member;
	
	@Basic(optional = false)
	private String content;
	
	@Basic(optional = false)
	private Integer flavorScore;
	
	@Basic(optional = false)
	private Integer serviceScore;
	
	@Basic(optional = false)
	private Integer priveScore;
	
	@OneToMany(mappedBy = "review")
	private List<ReviewImage> reviewImages = new ArrayList<>();

	
	

}
