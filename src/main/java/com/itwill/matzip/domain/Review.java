package com.itwill.matzip.domain;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
	@JoinColumn(name = "RESTAURANT_FK", nullable = false)
	private Restaurant restaurant;
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "MEMBER_FK", nullable = false)
	private Member member;
	
	@Basic(optional = false)
	private String content;
	
	@Basic(optional = false)
	private Integer flavorScore; 
	
	@Basic(optional = false)
	private Integer serviceScore;
	
	@Basic(optional = false)
	private Integer priceScore;
	
	@OneToMany(mappedBy = "review")
	private List<ReviewImage> reviewImages = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "REVIEW_HASHTAG_REL", // 중간 테이블 이름 
        joinColumns = @JoinColumn(name = "REVIEW_PK"), // 현재 엔티티 참조 컬럼이름
        inverseJoinColumns = @JoinColumn(name = "REVIEW_HASHTAG_PK") // 반대 엔티티 참조 컬럼이름
    )
    private Set<ReviewHashtag> hashtags = new HashSet<>();

		
	public void updateReview(Integer flavorScore, Integer priceScore, Integer serviceScore, String content) {
		   if(flavorScore != null) this.flavorScore = flavorScore;
		   if(priceScore != null) this.priceScore = priceScore;
		   if(serviceScore != null) this.serviceScore = serviceScore;
		   if(content != null && !content.isEmpty()) this.content = content;
	}

		
	
	

}
