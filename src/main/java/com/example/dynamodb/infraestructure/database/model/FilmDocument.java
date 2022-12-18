package com.example.dynamodb.infraestructure.database.model;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class FilmDocument {

  @Getter(onMethod_ = {@DynamoDbPartitionKey})
  private UUID id;

  private String originalTitle;

}
