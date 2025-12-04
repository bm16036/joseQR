package com.menudigital.menuapi.security;
import lombok.RequiredArgsConstructor; import org.springframework.security.core.authority.SimpleGrantedAuthority; import org.springframework.security.core.userdetails.*; import org.springframework.stereotype.Service; import java.util.List;
@Service @RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;
  @Override public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    var u=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
    return new User(u.getEmail(),u.getPasswordHash(),u.isActive(),true,true,true,List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().getNombre())));
  }
}
