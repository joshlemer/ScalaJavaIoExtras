package com.github.joshlemer.java_io_extras

import java.io.InputStream

class IteratorInputStream(val iterator: Iterator[Byte]) extends InputStream {
  override def read(): Int = if (iterator.hasNext) iterator.next() else -1
}

object IteratorInputStream {
  def apply(iterator: Iterator[Byte]): IteratorInputStream = new IteratorInputStream(iterator)
}
