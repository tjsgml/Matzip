package com.itwill.matzip.domain;

import jakarta.persistence.*;
import lombok.*;
import software.amazon.awssdk.core.pagination.sync.PaginatedResponsesIterator;

import java.util.Set;

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
    private Long id;

    @Basic(optional = false)
    private String name;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_PK")
    private Set<Restaurant> restaurants;

}
