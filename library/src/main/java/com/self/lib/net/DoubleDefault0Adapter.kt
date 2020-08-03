package com.self.lib.net

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type


/**
 *
 *
 * @date 2019-06-13
 */

class DoubleDefault0Adapter : JsonSerializer<Double>, JsonDeserializer<Double>{

    override fun serialize(src: Double?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src)
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Double {
        return if(json?.asString.isNullOrBlank() || json?.asString == "null"){
            0.0
        }else{
            json?.asDouble?:0.0
        }
    }
}