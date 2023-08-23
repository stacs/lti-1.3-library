package edu.virginia.its.canvas.lti.repos;

import edu.virginia.its.canvas.lti.model.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message, Long> {
  List<Message> findAllByToolNameAndLocale(String toolName, String locale);

  Message findByToolNameAndMessageKeyAndLocale(String toolName, String messageKey, String locale);
}
