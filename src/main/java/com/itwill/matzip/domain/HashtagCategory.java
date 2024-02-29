package com.itwill.matzip.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@EqualsAndHashCode
@Entity
public class HashtagCategory implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "HASHTAG_CATEGORY_PK")
	private Long id;
	
	@Basic(optional = false)
	private String name;

	@Builder.Default
	@OneToMany(fetch = FetchType.LAZY)
	private List<ReviewHashtag> reviewHashtags = new ArrayList<>();
}
