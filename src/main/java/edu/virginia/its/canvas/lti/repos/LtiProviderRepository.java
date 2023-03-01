package edu.virginia.its.canvas.lti.repos;

import edu.virginia.its.canvas.lti.model.LtiProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LtiProviderRepository extends JpaRepository<LtiProvider, Long> {}
