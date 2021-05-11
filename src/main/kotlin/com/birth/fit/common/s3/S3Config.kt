package com.birth.fit.common.s3

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Config(
    @Value("\${cloud.aws.s3.region}")
    private val region: String,

    @Value("\${cloud.aws.credentials.accessKey}")
    private val accessKey: String,

    @Value("\${cloud.aws.credentials.secretKey}")
    private val secretKey: String
) {

    @Bean
    fun assetS3Client(): AmazonS3 {
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey)))
            .withRegion(region)
            .build()
    }
}