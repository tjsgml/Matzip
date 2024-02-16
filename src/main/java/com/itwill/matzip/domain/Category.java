package com.itwill.matzip.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Entity
public class Category {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_PK")
    private Integer id;

    @Basic(optional = false)
    private String name;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_FK")
    private Set<Restaurant> restaurants;

}
