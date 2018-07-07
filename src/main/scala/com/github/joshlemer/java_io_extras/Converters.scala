package com.github.joshlemer.java_io_extras

import java.io.{InputStream, Reader}

import scala.language.implicitConversions

object Converters {
  implicit class InputStreamConverter[IS <: InputStream](private val is: IS) extends AnyVal {
    def iterableOnce: IterableOnce[Byte] = InputStreamIterableOnce(is)
    def iterator: Iterator[Byte] = InputStreamIterator(is)
  }

  implicit class ByteIteratorConverter(private val iterator: Iterator[Byte]) extends AnyVal {
    def inputStream: InputStream = IteratorInputStream(iterator)
  }

  implicit class ReaderConverter[R <: Reader](private val r: R) extends AnyVal {
    def iterableOnce: IterableOnce[Char] = ReaderIterableOnce(r)
    def iterator: Iterator[Char] = ReaderIterator(r)
  }

  implicit class CharIteratorConverter(private val iterator: Iterator[Char]) extends AnyVal {
    def reader: Reader  = IteratorReader(iterator)
  }
}
