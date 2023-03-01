package edu.virginia.its.canvas.lti.util;

import com.nimbusds.jose.shaded.json.JSONArray;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RoleMapper implements GrantedAuthoritiesMapper {

  private final Map<String, String> rolesMap;

  public RoleMapper() {
    this.rolesMap = new HashMap<>();
    // Canvas does not differentiate between Account Admins and Subaccount Admins
    this.rolesMap.put(Constants.LTI_ADMIN, Constants.ADMIN_ROLE);
    // Canvas gives this role to TAs in addition to Teachers
    this.rolesMap.put(Constants.LTI_INSTRUCTOR, Constants.INSTRUCTOR_ROLE);
    this.rolesMap.put(Constants.LTI_TEACHING_ASSISTANT, Constants.TA_ROLE);
    this.rolesMap.put(Constants.LTI_STUDENT, Constants.STUDENT_ROLE);
  }

  @Override
  public Collection<? extends GrantedAuthority> mapAuthorities(
      Collection<? extends GrantedAuthority> authorities) {
    Set<GrantedAuthority> newAuthorities = new HashSet<>(authorities);
    for (GrantedAuthority authority : authorities) {
      OidcUserAuthority userAuth = (OidcUserAuthority) authority;
      Object rolesObject = userAuth.getAttributes().get(Constants.LTI_ROLES_CLAIM);
      if (rolesObject instanceof JSONArray roles) {
        for (Object roleObject : roles.toArray()) {
          if (roleObject instanceof String role) {
            String newRole = rolesMap.get(role);
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
