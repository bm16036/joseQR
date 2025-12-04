package com.menudigital.menuapi.security;
import com.menudigital.menuapi.menu.domain.Company;
import jakarta.persistence.*; import lombok.*; import org.hibernate.annotations.CreationTimestamp; import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant; import java.util.UUID;
@Entity @Table(name="usuarios") @Data @Builder @NoArgsConstructor @AllArgsConstructor

public class UserEntity {
  @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
  @Column(name="nombre",nullable=false,unique=true,length=60) private String username;
  @Column(name="contrasena",nullable=false) private String passwordHash;
  @Column(name="correo",nullable=false,length=120) private String email;
  @ManyToOne(optional=false)
  @JoinColumn(name = "role_id")
  private Role role;
  @ManyToOne(optional=false) 
  @JoinColumn(name = "company_id")
  private Company company;
  @Column(name="activo",nullable=false) private boolean active=true;
 
  @CreationTimestamp private Instant createdAt; @UpdateTimestamp private Instant updatedAt;
}
