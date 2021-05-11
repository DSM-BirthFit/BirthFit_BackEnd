package com.birth.fit.common.s3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.util.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.IOException


@Component
class S3Service(
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
    @Autowired private val s3Client: AmazonS3
) {

    @Throws(IOException::class)
    fun upload(file: MultipartFile, fileName: String?) {
        val bytes: ByteArray = IOUtils.toByteArray(file.inputStream)
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        
        s3Client.putObject(
            PutObjectRequest(bucket, fileName, byteArrayInputStream, null)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        )
    }

    fun delete(objectName: String?) {
        s3Client.deleteObject(bucket, objectName)
    }
}