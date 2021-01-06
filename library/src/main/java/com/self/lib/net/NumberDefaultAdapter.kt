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
 * @author MBP
 * @date 2020/11/21
 */
class NumberDefaultAdapter : JsonSerializer<Number>, JsonDeserializer<Number> {
    override fun serialize(src: Number?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src)
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Number {
        try {
            if(json != null && !json.isJsonNull){
                return json.asNumber
            }
        }catch (e: RuntimeException){
            e.printStackTrace()
        }
        return -1
    }
}