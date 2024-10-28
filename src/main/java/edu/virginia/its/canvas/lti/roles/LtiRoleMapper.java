package edu.virginia.its.canvas.lti.roles;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.stereotype.Component;
import uk.ac.ox.ctl.lti13.lti.Claims;

@Slf4j
@Component
public class LtiRoleMapper implements GrantedAuthoritiesMapper {

  private final LtiRolesMap ltiRoleMappings;

  public LtiRoleMapper(LtiRolesMap ltiRoleMappings) {
    this.ltiRoleMappings = ltiRoleMappings;
    log.info("Using the following LTI role mappings: {}", ltiRoleMappings);
  }

  @Override
  public Collection<? extends GrantedAuthority> mapAuthorities(
      Collection<? extends GrantedAuthority> authorities) {
    Set<GrantedAuthority> newAuthorities = new HashSet<>(authorities);
    for (GrantedAuthority authority : authorities) {
      OidcUserAuthority userAuth = (OidcUserAuthority) authority;
      Object rolesObject = userAuth.getAttributes().get(Claims.ROLES);
      if (rolesObject instanceof List roles) {
        for (Object roleObject : roles) {
          if (roleObject instanceof String role) {
            String newRole = ltiRoleMappings.get(role);
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
