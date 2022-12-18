package com.example.dynamodb.dynamodbtestextension;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static org.springframework.test.context.junit.jupiter.SpringExtension.getApplicationContext;


@Slf4j
public class DynamoDbTestExtension implements BeforeEachCallback, AfterEachCallback {

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {

    var dynamoDbClient = getApplicationContext(context).getBean(DynamoDbClient.class);
    var dynamoDbEnhancedClient= getApplicationContext(context).getBean(DynamoDbEnhancedClient.class);

    if(context.getRequiredTestClass().isAnnotationPresent(DynamoDbTestTables.class)){
      var annotation = context.getRequiredTestClass().getAnnotation(DynamoDbTestTables.class);
      for (Class<? extends CreateTables> createTables: annotation.value()) {
        createTables.getConstructor().newInstance().setUp(dynamoDbClient);
      }
    }

    if(context.getRequiredTestMethod().isAnnotationPresent(DynamoDbTestLoadData.class)){
      var annotation = context.getRequiredTestMethod().getAnnotation(DynamoDbTestLoadData.class);
      for (Class<? extends LoadData> loadData: annotation.value()) {
        loadData.getConstructor().newInstance().apply(dynamoDbEnhancedClient);
      }
    }

  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {

    var dynamoDbClient = getApplicationContext(context).getBean(DynamoDbClient.class);

    if(context.getRequiredTestClass().isAnnotationPresent(DynamoDbTestTables.class)){
      var annotation = context.getRequiredTestClass().getAnnotation(DynamoDbTestTables.class);
      for (Class<? extends CreateTables> createTables: annotation.value()) {
        createTables.getConstructor().newInstance().cleanUp(dynamoDbClient);
      }
    }

  }

}
