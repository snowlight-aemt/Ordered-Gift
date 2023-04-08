package me.snowlight.gift.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsSqsConfig {
    @Value("${cloud.aws.access-key}")
    private String awsAccessKey;

    @Value("${cloud.aws.secret-key}")
    private String awsSecretKey;

    @Value("${cloud.aws.end-point}")
    private String awsEndpoint;

    @Value("${cloud.aws.region}")
    private String awsRegion;

    @Bean
    public AmazonSQSAsync amazonSQSAsync() {
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(awsAccessKey, awsSecretKey)
        );

        return AmazonSQSAsyncClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(awsEndpoint, awsRegion))
                .withCredentials(awsCredentialsProvider)
                .build();
    }
}