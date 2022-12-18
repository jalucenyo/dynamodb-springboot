package com.example.dynamodb.dynamodbtestextension;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public interface CreateTables {

  void setUp(DynamoDbClient dynamoDbClient);

  void cleanUp(DynamoDbClient dynamoDbClient);

}
