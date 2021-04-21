package com.birth.fit.common.exception

import com.birth.fit.common.exception.error.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.*

@ControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(
        InvalidAuthEmailException::class,
        InvalidAuthCodeException::class,
        LoginFailedException::class
    )
    fun badRequestException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.BAD_REQUEST, e.message!!)
    }

    @ExceptionHandler(
        InvalidTokenException::class
    )
    fun requestFailedException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.UNAUTHORIZED, e.message!!)
    }

    @ExceptionHandler(ExpiredTokenException::class)
    fun unauthorizedException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.FORBIDDEN, e.message!!)
    }

    @ExceptionHandler(
        UserNotFoundException::class,
        PostNotFoundException::class,
        ContentNotFoundException::class
    )
    fun notFoundException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.NOT_FOUND, e.message!!)
    }

    @ExceptionHandler(
        UserAlreadyExistException::class,
        PasswordSameException::class
    )
    fun duplicateException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.CONFLICT, e.message!!)
    }

    @ExceptionHandler(Exception::class)
    fun internalServerErrorException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.message!!)
    }

    private fun generateErrorResponse(
        status: HttpStatus,
        message: String
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(Date(), status.value(), status.name ,message), status)
    }
}