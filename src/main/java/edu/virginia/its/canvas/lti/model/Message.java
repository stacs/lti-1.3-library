package edu.virginia.its.canvas.lti.model;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Message {

  public Message(
      String toolName, String messageKey, String message, String defaultMessage, String locale) {
    this.toolName = toolName;
    this.messageKey = messageKey;
    this.message = message;
    this.defaultMessage = defaultMessage;
    this.locale = locale;
  }

  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String toolName;

  @Column(nullable = false)
  private String messageKey;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String message;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String defaultMessage;

  @Column(nullable = false)
  private String locale;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private OffsetDateTime dateCreated;

  @CreatedBy
  @Column(nullable = false, updatable = false)
  private String createdBy;

  @LastModifiedDate
  @Column(nullable = false)
  private OffsetDateTime lastUpdated;

  @LastModifiedBy
  @Column(nullable = false)
  private String lastUpdatedBy;
}
