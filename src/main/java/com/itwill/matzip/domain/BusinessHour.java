package com.itwill.matzip.domain;

import com.itwill.matzip.domain.enums.BusinessDay;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "BUSINESS_HOUR")
public class BusinessHour {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BUSINESS_HOUR_PK")
	private Long id;
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESTAURNT_FK")
    private Restaurant restaurant;
	
	
	@ToString.Exclude
    @Enumerated(EnumType.STRING)
    private BusinessDay days;
	
	private Boolean isHoliday;
	
	private String openTime;
	
	private String closeTime;
}

