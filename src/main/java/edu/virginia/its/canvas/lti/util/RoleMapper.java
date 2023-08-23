package edu.virginia.its.canvas.lti.util;

import com.nimbusds.jose.shaded.json.JSONArray;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.stereotype.Component;
import uk.ac.ox.ctl.lti13.lti.Claims;

@Slf4j
@Component
public class RoleMapper implements GrantedAuthoritiesMapper {

  private final RolesMap roleMappings;

  public RoleMapper(RolesMap roleMappings) {
    this.roleMappings = roleMappings;
    log.info("Using the following role mappings: {}", roleMappings);
  }

  @Override
  public Collection<? extends GrantedAuthority> mapAuthorities(
      Collection<? extends GrantedAuthority> authorities) {
    Set<GrantedAuthority> newAuthorities = new HashSet<>(authorities);
    for (GrantedAuthority authority : authorities) {
      OidcUserAuthority userAuth = (OidcUserAuthority) authority;
      Object rolesObject = userAuth.getAttributes().get(Claims.ROLES);
      if (rolesObject instanceof JSONArray roles) {
        for (Object roleObject : roles.toArray()) {
          if (roleObject instanceof String role) {
            String newRole = roleMappings.get(role);
            if (newRole != null) {
              newAuthorities.add(
                  new OidcUserAuthority(newRole, userAuth.getIdToken(), userAuth.getUserInfo()));
            }
          }
        }
      }
    }
    return newAuthorities;
  }
}
