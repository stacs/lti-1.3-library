package edu.virginia.its.canvas.lti.roles;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import edu.virginia.its.canvas.lti.util.Constants;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import uk.ac.ox.ctl.lti13.lti.Claims;

@Slf4j
@Component
public class CanvasRoleMapper implements GrantedAuthoritiesMapper {

  private final CanvasRolesMap canvasRoleMappings;

  public CanvasRoleMapper(CanvasRolesMap canvasRoleMappings) {
    this.canvasRoleMappings = canvasRoleMappings;
    log.info("Using the following Canvas role mappings: {}", canvasRoleMappings);
  }

  @Override
  public Collection<? extends GrantedAuthority> mapAuthorities(
      Collection<? extends GrantedAuthority> authorities) {
    Set<GrantedAuthority> newAuthorities = new HashSet<>(authorities);
    for (GrantedAuthority authority : authorities) {
      OidcUserAuthority userAuth = (OidcUserAuthority) authority;
      Object customClaims = userAuth.getAttributes().get(Claims.CUSTOM);
      if (customClaims instanceof LinkedTreeMap map) {
        Object enrollmentRolesObject = map.get(Constants.CANVAS_ROLES_CUSTOM_KEY);
        if (enrollmentRolesObject instanceof String enrollmentRoles
            && !ObjectUtils.isEmpty(enrollmentRoles)) {
          String[] splitEnrollmentRoles = enrollmentRoles.split(",");
          for (String splitEnrollmentRole : splitEnrollmentRoles) {
            String newRole = canvasRoleMappings.get(splitEnrollmentRole);
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
