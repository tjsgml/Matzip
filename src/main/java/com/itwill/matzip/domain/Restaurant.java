package com.itwill.matzip.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "RESTAURANT")
public class Restaurant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESTAURANT_PK")
    private Long id;

    @Basic(optional = false)
    private String name;

    @Basic(optional = false)
    private String address;

    private String detailAddress;

    private String contact;

    private double lon;

    private double lat;

    @ToString.Exclude
    @JsonInclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_FK")
    private Category category;

    @ToString.Exclude
    @JsonInclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "RESTAURANT_FK")
    private List<Menu> menus;
    @Builder.Default
    @ToString.Exclude

    @Enumerated(EnumType.STRING)
    private RestaurantStatus status = RestaurantStatus.WAIT;

    public void updateName(String name) {
        this.name = name;
    }

    public void updateAddress(String address) {
        this.address = address;
    }

    public void updateDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public void updateContact(String contact) {
        this.contact = contact;
    }

    public void updateLon(Double lon) {
        this.lon = lon;
    }

    public void updateLat(Double lat) {
        this.lat = lat;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }


}

