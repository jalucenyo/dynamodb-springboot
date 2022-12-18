package com.example.dynamodb.scenes.films;

import com.example.dynamodb.dynamodbtestextension.LoadData;
import com.example.dynamodb.infraestructure.database.model.FilmDocument;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
public class FilmsLoadData implements LoadData {

  @Override
  public void apply(DynamoDbEnhancedClient dynamoDbEnhancedClient) {

    log.info("Load data test scene of films ...");
    Stream.of(
        FilmDocument.builder().id(UUID.fromString("e536ba01-55a3-4a04-95e8-1ecec1c378a5")).originalTitle("Test original title").build(),
        FilmDocument.builder().id(UUID.fromString("9d003dae-b6dc-4b44-9fd1-879b4c5f331e")).originalTitle("Another test original title").build()
    ).forEach(it ->
        dynamoDbEnhancedClient.table("films", TableSchema.fromBean(FilmDocument.class)).putItem(it)
    );

  }

}
