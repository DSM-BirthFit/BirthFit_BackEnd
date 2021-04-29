package com.birth.fit.common.s3

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Config(
    @Value("\${cloud.aws.s3.region}")
    private val region: String,

    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,

    @Value("\${cloud.aws.credentials.accessKey}")
    private val accessKey: String,

    @Value("\${cloud.aws.credentials.secretKey}")
    private val secretKey: String
) {

    @Bean
    fun basicAWSCredentials(): BasicAWSCredentials {
        return BasicAWSCredentials(accessKey, secretKey)
    }

    @Bean
    fun amazonS3Client(awsCredentials: AWSCredentials?): AmazonS3Client {
        val amazonS3Client = AmazonS3Client(awsCredentials)
        amazonS3Client.setRegion(Region.getRegion(Regions.fromName(region)))
        return amazonS3Client
    }
}