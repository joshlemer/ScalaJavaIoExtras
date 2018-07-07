package com.github.joshlemer.java_io_extras

import java.io.Reader

class IteratorReader(val iterator: Iterator[Char]) extends Reader {
  override def read(): Int = if (iterator.hasNext) iterator.next() else -1

  override def read(cbuf: Array[Char], off: Int, len: Int): Int = {
    val numCharsToWrite = math.min(cbuf.length - off, len)
    var i = 0
    while (i < numCharsToWrite) {
      cbuf(off + i) = iterator.next()
      i += 1
    }
    i
  }

  override def close(): Unit = ()
}

object IteratorReader {
  def apply(iterator: Iterator[Char]): IteratorReader = new IteratorReader(iterator)
}

