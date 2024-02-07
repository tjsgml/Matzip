package com.itwill.matzip.domain;

import jakarta.persistence.*;
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
@EqualsAndHashCode(callSuper = false)
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

   @ToString.Exclude
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "CATEGORY_FK")
   private Category category;
}
