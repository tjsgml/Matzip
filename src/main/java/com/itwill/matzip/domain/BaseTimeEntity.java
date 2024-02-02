package com.itwill.matzip.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@MappedSuperclass // ㄷㅏ른 엔터티 클래스의 상위 클래스로 사용됨
@EntityListeners(AuditingEntityListener.class)
// => 엔터티 최초 생성시간, 최종 수정시간 등을 자동으로 DB에 저장하기 위해서 사용
// -> EntityListeners 어노테이션과 함께 컬럼 별로 생성시간, 수정시간을 저장할 변수를 각각 명시해주어야 한다.
// 스프릥 부트가 항상 가지고 있는 메인 클래스의 `메인 메서드`에도! 설정을 해주어야 함.
public class BaseTimeEntity {

    @CreatedDate // -> Entity 생성 시간을 저장하는 필드
    @Column(updatable = false)
    private LocalDateTime createdTime;

    @LastModifiedDate // -> Entity 최종 수정 시간을 저장하는 필드
    private LocalDateTime modifiedTime;
}
