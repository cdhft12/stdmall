package com.std.stdmall.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @PrePersist // JPA 엔티티 저장 이전에 실행
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
    }
    @PreUpdate // JPA 엔티티 수정 이전에 실행
    public void preUpdate() {
        this.modifiedDate = LocalDateTime.now();
    }
}
