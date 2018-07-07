package com.github.joshlemer.java_io_extras

import java.io.InputStream

import com.github.joshlemer.java_io_extras.InputStreamIterator.PeekByte

import scala.collection.{AbstractIterator, BufferedIterator}

/** An Iterator[Byte] over a [[java.io.InputStream]], which consumes the InputStream, and automatically closes it when
  * reaching the end.
  *
  * If this iterator is not completely consumed, care should be taken to ensure this iterator is closed by calling
  *   .close()
  *
  * Automatically closes
  *
  * @param inputStream the inputStream to iterate over
  * @tparam IS the type of the input stream, in case callers would like to have access to specialized suptypes
  */
class InputStreamIterator[IS <: InputStream](val inputStream: IS)
  extends AbstractIterator[Byte] with BufferedIterator[Byte] {

  /** A buffered "peeked" byte loaded from the inner inputStream. Calls to hasNext have no choice but to peek into the
    * InputStream and compare against -1, which indicates the stream has finished. In the case that the stream was not
    * empty, we must buffer that byte for the next invocation of `next()` to not drop the byte.
    *
    * can either be:
    *   >= 0: the byte is a real cached byte, to be returned on next invocation of next()
    *
    *   -1: the input stream has finished
    *
    *   -2: the byte is "empty". Equivalent to None, but without allocating Options. Indicates that we must read the
    *       input stream to get the next byte
    *
    */
  private var peekByte: Int = InputStreamIterator.PeekByte.EMPTY

  /** Set to true if and only if we have already closed the inputStream */
  private var closed: Boolean = false

  @inline private def failEmpty(): Nothing = throw new NoSuchElementException("InputStreamIterator is empty.")

  private def readFromInputStream(): Int = {
    val r = inputStream.read()
    if (r == PeekByte.FINISHED && !closed) {
      close()
    }
    r
  }

  override def hasNext: Boolean = peek() >= 0

  override def next(): Byte = {
    if (peekByte == PeekByte.FINISHED) {
      failEmpty()
    } else if (peekByte >= 0) {
      val temp = peekByte
      peekByte = PeekByte.EMPTY
      temp.toByte
    } else {
      // peekByte must be PeekByte.EMPTY
      val read = readFromInputStream()
      if (read == PeekByte.FINISHED) {
        peekByte = PeekByte.FINISHED
        failEmpty()
      } else {
        read.toByte
      }
    }
  }

  override def head: Byte =
    peek().toByte

  def peek(): Int = {
    if (peekByte == PeekByte.EMPTY) {
      peekByte = readFromInputStream()
    }

    peekByte
  }

  def close(): Unit = {
    inputStream.close()
    closed = true
  }
}

object InputStreamIterator {
  final object PeekByte {
    val EMPTY: Int = -2
    val FINISHED: Int = -1
  }

  def apply[IS <: InputStream](inputStream: IS): InputStreamIterator[IS] = new InputStreamIterator(inputStream)
}
