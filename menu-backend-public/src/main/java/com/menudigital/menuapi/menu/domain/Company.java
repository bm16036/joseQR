package com.menudigital.menuapi.menu.domain;
import jakarta.persistence.*; import lombok.*; import org.hibernate.annotations.CreationTimestamp; import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant; import java.util.UUID;
@Entity @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Company {
  @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
  private String taxId; private String businessName; private String commercialName; private String email; private String phone; private String logoUrl;
  @CreationTimestamp private Instant createdAt; @UpdateTimestamp private Instant updatedAt;
}
