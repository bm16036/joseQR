package com.menudigital.menuapi.menu.domain;
import com.fasterxml.jackson.annotation.JsonIgnore; import jakarta.persistence.*; import lombok.*; import org.hibernate.annotations.CreationTimestamp; import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant; import java.util.*; 
@Entity @Table(name="menu_entity") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Menu {
  @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
  @Column(nullable=false) private String name;
  @Column(nullable=false) private boolean active=true;
  @ManyToOne(optional=false) private Company company;
  @JsonIgnore @ManyToMany(mappedBy="menus") private Set<Product> products=new HashSet<>();
  @CreationTimestamp private Instant createdAt; @UpdateTimestamp private Instant updatedAt;
}
