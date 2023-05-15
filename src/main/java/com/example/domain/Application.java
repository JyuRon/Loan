package com.example.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Where(clause = "is_deleted=false")
/** 대출 신청 */
public class Application extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long applicationId;

  @Column(columnDefinition = "varchar(12) DEFAULT NULL COMMENT '신청자'")
  private String name;

  @Column(columnDefinition = "varchar(13) DEFAULT NULL COMMENT '전화번호'")
  private String cellPhone;

  @Column(columnDefinition = "varchar(50) DEFAULT NULL COMMENT '신청자 이메일'")
  private String email;

  @Column(columnDefinition = "decimal(5,4) DEFAULT NULL COMMENT '금리'")
  private BigDecimal interestRate;

  /**
   * BigDecimal 의 사용 이유???
   * java : https://jsonobject.tistory.com/466
   * mysql : http://minsql.com/mysql/MySQL-decimal-type-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EC%95%8C%EA%B3%A0-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0/
   * - 실수끼리의 계산시 부동소수점 방식때문에 의도하지 않은 결과값이 나올 수 있음
   * - 이로 인해 돈과 소수점을 다룬다면 BigDecimal은 선택이 아니라 필수이다.
   */
  @Column(columnDefinition = "decimal(5,4) DEFAULT NULL COMMENT '취급수수료'")
  private BigDecimal fee;

  @Column(columnDefinition = "datetime DEFAULT NULL COMMENT '만기'")
  private LocalDateTime maturity;

  @Column(columnDefinition = "decimal(15,2) DEFAULT NULL COMMENT '대출 신청 금액'")
  private BigDecimal hopeAmount;

  @Column(columnDefinition = "datetime DEFAULT NULL COMMENT '신청일자'")
  private LocalDateTime appliedAt;

  @Column(columnDefinition = "decimal(15,2) DEFAULT NULL COMMENT '승인 금액'")
  private BigDecimal approvalAmount;

  @Column(columnDefinition = "datetime DEFAULT NULL COMMENT '약정일자'")
  private LocalDateTime contractedAt;
}