package com.example.yum_web_server.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration


@ReadingConverter
class ByteToBooleanConverter : Converter<Byte, Boolean> {
    override fun convert(source: Byte): Boolean {
        return source != 0.toByte()
    }
}

@WritingConverter
class BooleanToByteConverter : Converter<Boolean, Byte> {
    override fun convert(source: Boolean): Byte {
        return if (source) 1.toByte() else 0.toByte()
    }
}

@Configuration
abstract class R2dbcConfig : AbstractR2dbcConfiguration() {
    override fun getCustomConverters(): List<Any> {
        return listOf(ByteToBooleanConverter(), BooleanToByteConverter())
    }
}