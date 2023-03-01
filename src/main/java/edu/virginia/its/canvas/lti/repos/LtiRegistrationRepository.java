package edu.virginia.its.canvas.lti.repos;

import edu.virginia.its.canvas.lti.model.LtiRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LtiRegistrationRepository extends JpaRepository<LtiRegistration, Long> {
  public LtiRegistration findByName(String name);
}
