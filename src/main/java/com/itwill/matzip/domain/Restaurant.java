package com.itwill.matzip.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@Getter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    private String name;

    @Basic(optional = false)
    private String address;

    @Basic(optional = true)
    private String detail_address;

    @NaturalId
    @Basic(optional = false)
    private String contact;

    @Basic(optional = false)
    private Double lon;

    @Basic(optional = false)
    private Double lat;

    @CreatedDate // -> Entity 생성 시간을 저장하는 필드
    @Column(updatable = false)
    private LocalDate created_time;

    @LastModifiedDate
    private LocalDate modified_time;
}
