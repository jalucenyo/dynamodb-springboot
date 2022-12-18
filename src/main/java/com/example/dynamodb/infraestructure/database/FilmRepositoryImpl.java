package com.example.dynamodb.infraestructure.database;

import com.example.dynamodb.infraestructure.database.model.FilmDocument;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FilmRepositoryImpl {

  private static final String TABLE_NAME = "films";

  private final DynamoDbEnhancedClient dynamoDb;

  public Try<Void> create(FilmDocument filmDocument) {
    return Try.run(() -> getTable().putItem(filmDocument));
  }

  public Try<Void> deleteById(UUID id){

    var request = DeleteItemEnhancedRequest.builder()
        .conditionExpression(itemExistsExpression())
        .key(Key.builder().partitionValue(id.toString()).build())
        .build();

    return Try.run(() -> getTable().deleteItem(request));

  }

  public Try<FilmDocument> update(FilmDocument updateFilm) {

    var request = UpdateItemEnhancedRequest.builder(FilmDocument.class)
        .conditionExpression(itemExistsExpression())
        .item(updateFilm)
        .ignoreNulls(Boolean.TRUE)
        .build();

    return Try.of(() -> getTable().updateItem(request));

  }

  public Try<FilmDocument> findById(UUID id) {

    var request = GetItemEnhancedRequest.builder()
        .key(Key.builder().partitionValue(id.toString()).build())
        .build();

    var result = getTable().getItem(request);
    if(Objects.isNull(result)){
      return Try.failure(new NullPointerException());
    }

    return  Try.of(() -> result);
  }

  public Try<List<FilmDocument>> findByTitle(String title) {

    var request = ScanEnhancedRequest.builder()
        .filterExpression(Expression.builder()
            .expression("originalTitle = :titleParam")
            .expressionValues(Map.of(":titleParam", AttributeValue.builder().s(title).build()))
            .build())
        .build();

    return Try.of(() -> getTable().scan(request).items().stream().toList());

  }

  private Expression itemExistsExpression(){
    return Expression.builder()
        .expression("attribute_exists(id)")
        .build();
  }

  private DynamoDbTable<FilmDocument> getTable() {
    return dynamoDb.table(TABLE_NAME, TableSchema.fromBean(FilmDocument.class));
  }

}
