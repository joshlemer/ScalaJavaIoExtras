package com.github.joshlemer.java_io_extras

import java.io.InputStream

import scala.language.implicitConversions

object ImplicitConverters {
  implicit def inputStreamToIterableOnce[IS <: InputStream](is: IS): IterableOnce[Byte] = InputStreamIterableOnce(is)
}
