package com.itwill.matzip.domain;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@ToString(callSuper = true)
@EqualsAndHashCode
@Entity
@Table(name="RESTAURANT")
public class Restaurant extends BaseTimeEntity{
	
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "RESTAURANT_PK")
   private Long id;
   
   @Basic(optional = false)
   private String name;
   
   @Basic(optional = false)
   private String address;
   
   private String detail_address;
   
   private String contact;
   
   private double lon;
   
   private double lat;

}
