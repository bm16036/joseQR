package com.menudigital.menuapi.menu.domain;
import com.fasterxml.jackson.annotation.JsonIgnore; import jakarta.persistence.*; import lombok.*; import org.hibernate.annotations.CreationTimestamp; import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal; import java.time.Instant; import java.util.*; 
@Entity @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Product {
  @Id @GeneratedValue(strategy=GenerationType.UUID) private UUID id;
  @Column(nullable=false) private String name;
  @Column(length=800) private String description;
  @Column(nullable=false,precision=10,scale=2) private BigDecimal price;
  @Column(nullable=false,columnDefinition="boolean default true") @Builder.Default private boolean active=true;
  private String imageUrl;
  @ManyToOne(optional=false) private Category category;
  @ManyToOne(optional=false) private Company company;
  @JsonIgnore @ManyToMany @JoinTable(name="menu_products",joinColumns=@JoinColumn(name="product_id"),inverseJoinColumns=@JoinColumn(name="menu_id")) private Set<Menu> menus=new HashSet<>();
  @CreationTimestamp private Instant createdAt; @UpdateTimestamp private Instant updatedAt;
}
