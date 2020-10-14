package com.self.lib.util

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import org.json.JSONException
import java.lang.reflect.Type
import java.util.ArrayList

/**
 *
 *
 * @date 2017/12/8
 */

object GsonUtil {

  private var gson:Gson = Gson()

  //将bean转化为json
  @JvmStatic fun toJson(bean:Any?):String{
    return if(bean != null)
      gson.toJson(bean)
    else
      ""
  }

  //解析简单Json数据
  @JvmStatic fun <T> parseJson(jsonData: String, entityType: Class<T>): T {
    return gson.fromJson(jsonData, entityType)
  }

  //解析JsonArray数据
  @JvmStatic fun <T> parseList(json: String, cls: Class<T>): List<T> {
    val array = JsonParser.parseString(json).asJsonArray
    return array.map { gson.fromJson(it, cls) }
  }

  @JvmStatic fun <T> parseJsonElement(json: JsonElement?,typeOfT: Type?):T{
    return gson.fromJson(json,typeOfT)
  }

  //下面两种方法是解析JsonArray的自己写的方法，可用new TypeToken<List<T>>(){}.getType()代替

  @JvmStatic fun <T> readJsonArray(array: JsonArray, entityType: Class<T>): List<T> {
    val list = ArrayList<T>()
    for (i in 0 until array.size()) {
      try {
        val t = gson.fromJson(array.get(i).toString(), entityType)
        list.add(t)
      } catch (e: JSONException) {
        e.printStackTrace()
      }

    }
    return list
  }

  @JvmStatic fun isJsonData(content: String): Boolean {
    return try {
      JsonParser.parseString(content)
      true
    } catch (e: JsonParseException) {
      false
    }
  }

  @JvmStatic fun getGson(): Gson {
    return gson
  }

}