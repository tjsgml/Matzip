package com.itwill.matzip.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import java.util.List;

@Getter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant extends BaseTimeEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    private String placeName;

    @Basic(optional = false)
    private String address;

    private String detailAddress;

    @NaturalId
    @Basic(optional = false)
    private String contact;

    @Basic(optional = false)
    private Double lng;

    @Basic(optional = false)
    private Double lat;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RestaurantStatus status = RestaurantStatus.WAIT;

    @OneToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Menu> menus;
}
