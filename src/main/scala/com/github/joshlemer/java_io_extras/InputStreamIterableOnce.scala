package com.github.joshlemer.java_io_extras

import java.io.InputStream

final class InputStreamIterableOnce[IS <: InputStream](val inputStream: IS) extends IterableOnce[Byte] {
  override def iterator: Iterator[Byte] = InputStreamIterator(inputStream)
  override def knownSize: Int = -1
}

object InputStreamIterableOnce {

  def apply[IS <: InputStream](inputStream: IS): InputStreamIterableOnce[IS] = new InputStreamIterableOnce(inputStream)
}
