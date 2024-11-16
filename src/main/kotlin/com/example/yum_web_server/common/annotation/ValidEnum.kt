package com.example.yum_web_server.common.annotation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [ValidEnumValidator::class])
annotation class ValidEnum(
    val message: String = "Invalid Enum Value",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val enumClass : KClass<out Enum<*>>
)

class ValidEnumValidator : ConstraintValidator<ValidEnum, Any?> {
    private lateinit var enumValues : Array<out Enum<*>>

    override fun initialize(annotaion: ValidEnum) {
        enumValues = annotaion.enumClass.java.enumConstants
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean {
        if (value == null || value.toString().isBlank()) {
            return true
        }
        return enumValues.any { it.name == value.toString() }
    }
}
