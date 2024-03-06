package com.itwill.matzip.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.itwill.matzip.domain.enums.Expose;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@EqualsAndHashCode
@Entity
public class ReviewHashtag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_HASHTAG_PK")
    private Long id;

    @Basic(optional = false)
    private String keyword;

    @ToString.Exclude
    @JsonInclude
    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HASHTAG_CATEGORY_FK")
    private HashtagCategory htCategory;

    @ManyToMany(mappedBy = "hashtags", fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private Set<Review> reviews = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Expose expose = Expose.N;

    public void updateKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void changeCategory(HashtagCategory htCategory) {
        this.htCategory = htCategory;
    }

    public void changeExpose(Expose expose) {
        this.expose = expose;
    }
}
