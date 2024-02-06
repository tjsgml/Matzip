package com.itwill.matzip.domain;

import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;


import com.itwill.matzip.domain.enums.Gender;
import com.itwill.matzip.domain.enums.MemberRole;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
public class Member {
   
   @Id   
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   @EqualsAndHashCode.Include
//   @NaturalId
//   @Basic(optional = false)
//   @Column(updatable = false)
   private String username;
   
//   @Basic(optional = false)
   private String password;
   
   //@Basic(optional = false)
   private String email;
   
   private String kakaoClientId;
   
   //@Basic(optional = false)
   private LocalDate birth;
   
   //@Basic(optional = false)
   private String nickname;
   
   //@ColumnDefault("'default.png'")
   private String img;
   
   //@Basic(optional = false)
   @Enumerated(EnumType.STRING)
   private Gender gender;
   
   @Builder.Default
   @ToString.Exclude
   @ElementCollection(fetch = FetchType.EAGER)
   @Enumerated(EnumType.STRING)
   private Set<MemberRole> roles = new HashSet<>(); 
   
   public Member addRole(MemberRole role) {
      roles.add(role);
      return this;
   }
   
   public Member clearRoles() {
      roles.clear();
      return this;
   }
   
}
