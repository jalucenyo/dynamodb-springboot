package com.example.dynamodb.dynamodbtestextension;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public interface LoadData {

  void apply(DynamoDbEnhancedClient dynamoDbEnhancedClient);

}
