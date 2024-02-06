package com.itwill.matzip.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    @Id
    private Long id;

    private String name;

    private Integer price;

    @ManyToOne
    @JoinColumn
    private Restaurant restaurant;
}
