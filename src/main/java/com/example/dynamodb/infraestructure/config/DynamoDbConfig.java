package com.example.dynamodb.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class DynamoDbConfig {

  @Bean
  public DynamoDbClient getDynamoDbClient() {
    AwsCredentialsProvider credentialsProvider =
        DefaultCredentialsProvider.builder()
            .profileName("test")
            .build();

    return DynamoDbClient.builder()
        .endpointOverride(URI.create("http://localhost:8000"))
        .region(Region.US_EAST_1)
        .credentialsProvider(credentialsProvider).build();
  }

  @Bean
  public DynamoDbEnhancedClient getDynamoDbEnhancedClient() {
    return DynamoDbEnhancedClient.builder()
        .dynamoDbClient(getDynamoDbClient())
        .build();
  }

}
