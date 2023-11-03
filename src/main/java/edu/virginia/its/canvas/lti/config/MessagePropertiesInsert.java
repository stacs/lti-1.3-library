package edu.virginia.its.canvas.lti.config;

import edu.virginia.its.canvas.lti.model.Message;
import edu.virginia.its.canvas.lti.repos.MessageRepo;
import edu.virginia.its.canvas.lti.util.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ObjectUtils;

@Component
@Slf4j
public class MessagePropertiesInsert {

  @Autowired private ResourceLoader resourceLoader;

  @Autowired private ApplicationContext applicationContext;

  @Autowired private MessageRepo messageRepo;

  @Autowired
  @Qualifier("transactionManager")
  protected PlatformTransactionManager txManager;

  @Value("${ltitool.toolName}")
  private String toolName;

  @Value("${ltitool.messageSourcePropertiesFiles:/messages/messages.properties}")
  private List<String> messageSourcePropertiesFiles;

  @Value("${ltitool.resetMessages:false}")
  private boolean resetMessages;

  @PostConstruct
  public void postConstruct() {
    // Transactions (which are needed for 'deleteByToolName()') aren't ready by default in
    // PostConstruct,
    // so we use a callback to ensure we can run transactions for 'insertMessagesIntoDatabase()'.
    TransactionTemplate tmpl = new TransactionTemplate(txManager);
    tmpl.execute(
        new TransactionCallbackWithoutResult() {
          @Override
          protected void doInTransactionWithoutResult(TransactionStatus status) {
            insertMessagesIntoDatabase();
          }
        });
  }

  public void insertMessagesIntoDatabase() {
    log.info("Starting insertMessagesIntoDatabase()");
    if (resetMessages) {
      long deletedNumber = messageRepo.deleteByToolName(toolName);
      log.info(
          "The resetMessages property was set to true, '{}' messages were deleted from the DB for toolName '{}'",
          deletedNumber,
          toolName);
    }
    for (String messageSourcePropertiesFile : messageSourcePropertiesFiles) {
      String fileLookupString = messageSourcePropertiesFile.replace(".properties", "*.properties");
      Resource[] resources = new Resource[0];
      try {
        resources = applicationContext.getResources("classpath*:" + fileLookupString);
      } catch (IOException ex) {
        log.error(
            "Error while trying to find message properties files via search string '{}'",
            fileLookupString,
            ex);
      }
      for (Resource messagePropertiesFile : resources) {
        log.info("Loading message properties file: {}", messagePropertiesFile);
        if (messagePropertiesFile.exists()) {
          Properties properties = new Properties();
          try {
            properties.load(messagePropertiesFile.getInputStream());
          } catch (IOException e) {
            log.error(
                "Error while trying to load '{}' file, skipping insert logic",
                messagePropertiesFile);
            return;
          }
          String locale = getLocale(messagePropertiesFile);
          Map<String, String> propertiesMap = new HashMap<>();
          properties.forEach((k, v) -> propertiesMap.put((String) k, (String) v));
          List<Message> existingMessages = messageRepo.findAllByToolNameAndLocale(toolName, locale);
          Set<String> foundKeys =
              existingMessages.stream().map(Message::getMessageKey).collect(Collectors.toSet());
          List<Message> messagesToAdd = new ArrayList<>();
          List<Message> messagesToUpdate = new ArrayList<>();
          propertiesMap.forEach(
              (key, value) -> {
                if (!foundKeys.contains(key)) {
                  Message message = new Message(toolName, key, value, value, locale);
                  message.setCreatedBy(Constants.SYSTEM_USER);
                  message.setLastUpdatedBy(Constants.SYSTEM_USER);
                  messagesToAdd.add(message);
                } else {
                  Message message =
                      existingMessages.stream()
                          .filter(m -> m.getMessageKey().equals(key))
                          .findFirst()
                          .orElse(null);
                  if (message != null
                      && (resetMessages || !message.getDefaultMessage().equals(value))) {
                    message.setDefaultMessage(value);
                    message.setLastUpdatedBy(Constants.SYSTEM_USER);
                    messagesToUpdate.add(message);
                  }
                }
              });
          if (!messagesToAdd.isEmpty()) {
            messageRepo.saveAll(messagesToAdd);
            log.info("Inserted {} messages into the DB", messagesToAdd.size());
          }
          if (!messagesToUpdate.isEmpty()) {
            messageRepo.saveAll(messagesToUpdate);
            log.info("Updated {} messages in the DB", messagesToUpdate.size());
          }
        } else {
          log.warn("Could not find '{}' file", messagePropertiesFile);
        }
      }
    }
    log.info("Finished insertMessagesIntoDatabase()");
  }

  private String getLocale(Resource messagePropertyFile) {
    String filename = messagePropertyFile.getFilename();
    if (filename != null && filename.contains(".")) {
      String filenameWithoutExtension = filename.substring(0, filename.lastIndexOf("."));
      // Spring handles locale via messages_XX.properties, but locales can also have underscores in
      // them, so we only split based on the first underscore found
      if (filenameWithoutExtension.contains("_")) {
        String locale = filenameWithoutExtension.split("_", 2)[1];
        if (!ObjectUtils.isEmpty(locale)) {
          return locale;
        }
      }
    }
    log.warn("Could not determine locale for file '{}'", messagePropertyFile);
    return Constants.DEFAULT_LOCALE.toString();
  }
}
