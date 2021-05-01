package com.birth.fit.domain.image.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.S3ObjectInputStream
import com.amazonaws.util.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ImageService(
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,

    @Autowired private val s3Client: AmazonS3
) {

    fun getImage(imageName: String?): ByteArray? {
        val s3Object: S3Object = s3Client.getObject(bucket, imageName)
        val stream: S3ObjectInputStream = s3Object.objectContent
        return IOUtils.toByteArray(stream)
    }
}