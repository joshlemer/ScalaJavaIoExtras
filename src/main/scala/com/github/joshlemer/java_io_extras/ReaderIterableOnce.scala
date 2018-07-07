package com.github.joshlemer.java_io_extras

import java.io.Reader

final class ReaderIterableOnce[R <: Reader](val reader: R) extends IterableOnce[Char] {
  override def iterator: Iterator[Char] = ReaderIterator(reader)
  override def knownSize: Int = -1
}

object ReaderIterableOnce {
  def apply[R <: Reader](reader: R): ReaderIterableOnce[R] = new ReaderIterableOnce(reader)
}

