package com.self.lib.net

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.Collections

/**
 * @date 2019-06-13
 */

class ListDefaultEmptyAdapter : JsonDeserializer<List<*>> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): List<*> {
        return if(json != null && json.isJsonArray){
            val array = json.asJsonArray
            val itemType: Type = (typeOfT as ParameterizedType).actualTypeArguments[0]
            val list = mutableListOf<Any>()
            for (i in 0 until array.size()) {
                val element = array[i]
                context?.deserialize<Any>(element, itemType)?.let { item ->
                    list.add(item)
                }
            }
            list
        }else{
            Collections.EMPTY_LIST
        }
    }
}