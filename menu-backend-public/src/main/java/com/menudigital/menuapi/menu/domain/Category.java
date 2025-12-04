package com.menudigital.menuapi.menu.domain;
import jakarta.persistence.*; import lombok.*; import org.hibernate.annotations.CreationTimestamp; import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant; import java.util.UUID;
@Entity @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Category {
  @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
  @Column(nullable=false) private String name;
  @Column(nullable=false) private boolean active=true;
  @ManyToOne(optional=false) private Company company;
  @CreationTimestamp private Instant createdAt; @UpdateTimestamp private Instant updatedAt;
}
