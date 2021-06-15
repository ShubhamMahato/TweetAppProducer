package com.tweetapp.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDbConfig {
    private static final String SERVICE_ENDPOINT="";
    private static final String SERVICE_REGION="";
    private static final String SERVICE_KEY="";
    private static final String SERVICE_SECERET_KEY="";



    @Bean
    public DynamoDBMapper mapper(){
        return new DynamoDBMapper(amazonDynamoDBConfig());
    }

    private AmazonDynamoDB amazonDynamoDBConfig() {
        return AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(SERVICE_ENDPOINT,SERVICE_REGION))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(SERVICE_KEY,SERVICE_SECERET_KEY))).build();
    }
}
