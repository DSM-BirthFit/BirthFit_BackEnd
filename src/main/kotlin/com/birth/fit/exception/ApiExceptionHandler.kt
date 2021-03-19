package com.birth.fit.exception

import com.birth.fit.exception.error.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.*

@ControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(
        InvalidAuthEmailException::class,
        InvalidAuthCodeException::class
    )
    fun badRequestException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.BAD_REQUEST, e.message!!)
    }

    @ExceptionHandler(AuthenticationException::class)
    fun forbiddenException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.UNAUTHORIZED, "You are not allowed to do this operation")
    }

    @ExceptionHandler(LoginFailedException::class)
    fun requestFailedException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.UNAUTHORIZED, e.message!!)
    }

    @ExceptionHandler(AuthorizationException::class)
    fun unauthorizedException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.FORBIDDEN, e.message!!)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun notFoundException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.NOT_FOUND, e.message!!)
    }

    @ExceptionHandler(UserAlreadyExistException::class)
    fun duplicateException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.CONFLICT, e.message!!)
    }

    @ExceptionHandler(Exception::class)
    fun internalServerErrorException(e: Exception): ResponseEntity<ErrorResponse> {
        return generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Generic internal error")
    }

    private fun generateErrorResponse(
        status: HttpStatus,
        message: String
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(Date(), status.value(), status.name ,message), status)
    }
}