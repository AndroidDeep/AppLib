package com.self.lib.net

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.self.lib.util.GsonUtil
import java.lang.reflect.Type
import java.util.Collections

/**
 *
 *
 * @date 2019-06-13
 */

class ListDefaultEmptyAdapter : JsonDeserializer<List<*>> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): List<*> {
        return if(json != null && json.isJsonArray){
            GsonUtil.parseJsonElement(json, typeOfT)
        }else{
            Collections.EMPTY_LIST
        }
    }
}