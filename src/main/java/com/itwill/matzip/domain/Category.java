package com.itwill.matzip.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnDefault;

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

    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer listOrder;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_FK")
    private List<Restaurant> restaurants;

    public void changeOrderToShow(Integer order) {
        this.listOrder = order;
    }

    public void changeName(String name) {
        this.name = name;
    }
}