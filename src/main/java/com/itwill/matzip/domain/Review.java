package com.itwill.matzip.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@Entity
public class Review extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_PK")
    private Long id;

    @EqualsAndHashCode.Include
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESTAURANT_FK", nullable = false)
    private Restaurant restaurant;

    @ToString.Exclude
    @JsonInclude
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "MEMBER_FK", nullable = false)
    private Member member;

    @Basic(optional = false)
    private String content;

    @Basic(optional = false)
    private Integer flavorScore;

    @Basic(optional = false)
    private Integer serviceScore;

    @Basic(optional = false)
    private Integer priceScore;

    @JsonInclude
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReviewImage> reviewImages = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonInclude
    @JoinTable(
            name = "REVIEW_HASHTAG_REL",
            joinColumns = @JoinColumn(name = "REVIEW_PK"),
            inverseJoinColumns = @JoinColumn(name = "REVIEW_HASHTAG_PK")
    )
    @Builder.Default
    private Set<ReviewHashtag> hashtags = new HashSet<>();

    public void updateReview(Integer flavorScore, Integer priceScore, Integer serviceScore, String content) {
        if (flavorScore != null) this.flavorScore = flavorScore;
        if (priceScore != null) this.priceScore = priceScore;
        if (serviceScore != null) this.serviceScore = serviceScore;
        if (content != null && !content.isEmpty()) this.content = content;
    }
}
