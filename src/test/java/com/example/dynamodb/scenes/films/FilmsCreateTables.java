package com.example.dynamodb.scenes.films;

import com.example.dynamodb.dynamodbtestextension.CreateTables;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;


@Slf4j
public class FilmsCreateTables implements CreateTables {

  private final String TABLE_NAME = "films";
  private final String FIELD_PK = "id";


  @Override
  public void setUp(DynamoDbClient dynamoDbClient) {

    CreateTableRequest request = CreateTableRequest.builder()
        .attributeDefinitions(
            AttributeDefinition.builder()
                .attributeName(FIELD_PK)
                .attributeType(ScalarAttributeType.S)
                .build())
        .keySchema(KeySchemaElement.builder()
            .attributeName(FIELD_PK)
            .keyType(KeyType.HASH)
            .build())
        .provisionedThroughput(ProvisionedThroughput.builder()
            .readCapacityUnits(1L)
            .writeCapacityUnits(1L)
            .build())
        .tableName(TABLE_NAME)
        .build();

    dynamoDbClient.createTable(request);

  }

  @Override
  public void cleanUp(DynamoDbClient dynamoDbClient) {

    DeleteTableRequest request = DeleteTableRequest.builder().tableName(TABLE_NAME).build();
    dynamoDbClient.deleteTable(request);

  }

}
