package com.itwill.matzip.entity;

import com.itwill.matzip.domain.Restaurant;
import jakarta.persistence.Basic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantToCreateEntity {
    @Basic(optional = false)
    String name;

    @Basic(optional = false)
    String address;

    String detail_address;

    @NaturalId
    @Basic(optional = false)
    String contact;

    @Basic(optional = false)
    Double lon;

    @Basic(optional = false)
    Double lat;

    public Restaurant toEntity () {
        return Restaurant.builder()
                .name(name)
                .address(address)
                .detail_address(detail_address)
                .contact(contact)
                .lon(lon)
                .lat(lat)
                .build();
    }
}
