package com.self.lib

import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * 弱引用，kotlin委托
 * https://www.jianshu.com/p/9e07774dd32d
 * @date 2018/5/9
 */

class Weak<T : Any>(initializer: () -> T?) {
  private var weakReference = WeakReference(initializer())

  constructor():this({
    null
  })

  operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
    return weakReference.get()
  }

  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
    weakReference = WeakReference(value)
  }

}