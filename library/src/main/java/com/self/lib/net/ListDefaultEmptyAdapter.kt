package com.self.lib.net

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

/**
 *
 *
 * @date 2019-06-13
 */

class ListDefaultEmptyAdapter : JsonSerializer<List<*>>{

    override fun serialize(src: List<*>?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val array = JsonArray()

        if (src != null) {
            for (child in src) {
                val element = context?.serialize(child)
                array.add(element)
            }
        }

        return array
    }

}