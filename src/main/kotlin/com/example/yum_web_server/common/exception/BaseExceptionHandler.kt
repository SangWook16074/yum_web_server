package com.example.yum_web_server.common.exception

import com.example.yum_web_server.common.dto.ErrorResponse
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@Order(1)
@RestControllerAdvice
class BaseExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun methodArgumentNotValidExceptionHandler(
        exception: MethodArgumentNotValidException
    ) : ResponseEntity<ErrorResponse<Map<String, String>>> {
        var errors = mutableMapOf<String, String>()
        exception.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMsg = error.defaultMessage
            errors[fieldName] = errorMsg ?: "에러 메시지가 존재하지 않습니다!"
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                data = errors,
            )
        )
    }
}