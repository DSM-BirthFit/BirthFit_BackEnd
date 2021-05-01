package com.birth.fit.domain.image.controller

import com.birth.fit.domain.image.service.ImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/image")
class ImageController(
    @Autowired private val imageService: ImageService
) {

    @GetMapping(
        value = ["/{imageName}"],
        produces = [MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE]
    )
    fun getImage(@PathVariable imageName: String?): ByteArray? {
        return imageService.getImage(imageName)
    }
}