package com.github.joshlemer.java_io_extras

import java.io.Reader

import scala.collection.{AbstractIterator, BufferedIterator}

/** An Iterator[Char] over a [[java.io.Reader]], which consumes the Reader, and automatically closes it when
  * reaching the end.
  *
  * If this iterator is not completely consumed, care should be taken to ensure this iterator is closed by calling
  *   .close()
  *
  * Automatically closes
  *
  * @param reader the reader to iterate over
  * @tparam R the type of the reader, in case callers would like to have access to specialized suptypes
  */
final class ReaderIterator[+R <: Reader](val reader: R)
  extends AbstractIterator[Char] with BufferedIterator[Char] {

  /** A buffered "peeked" byte loaded from the inner reader. Calls to hasNext have no choice but to peek into the
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
  private var peekByte: Int = PeekByte.EMPTY

  /** Set to true if and only if we have already closed the inputStream */
  private var closed: Boolean = false

  @inline private def failEmpty(): Nothing = throw new NoSuchElementException("InputStreamIterator is empty.")

  /** Reads byte directly from inputStream, bypassing peekByte. Closes the stream if the end is reached */
  private def readFromReader(): Int = {
    val r = reader.read()
    if (r == PeekByte.FINISHED && !closed) {
      close()
    }
    r
  }

  override def hasNext: Boolean = !closed && PeekByte.isFull(peek())

  override def next(): Char = {
    if (peekByte == PeekByte.FINISHED) {
      failEmpty()
    } else if (peekByte >= 0) {
      val temp = peekByte
      peekByte = PeekByte.EMPTY
      temp.toChar
    } else {
      // peekByte must be PeekByte.EMPTY
      val read = readFromReader()
      if (read == PeekByte.FINISHED) {
        peekByte = PeekByte.FINISHED
        failEmpty()
      } else {
        read.toChar
      }
    }
  }

  override def head: Char = {
    val peeked = peek()

    if (PeekByte.isFull(peeked)) {
      peeked.toChar
    } else {
      failEmpty()
    }
  }

  /** Returns the next Byte if the iterator has more elements. Otherwise -1 */
  def peek(): Int = {
    if (peekByte == PeekByte.EMPTY) {
      peekByte = readFromReader()
    }

    peekByte
  }

  /** Closes the underlying inputStream */
  def close(): Unit = {
    reader.close()
    closed = true
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////
  // Optimizing overrides
  ////////////////////////////////////////////////////////////////////////////////////////////////////
  def copyToArray(xs: Array[Char]): xs.type =
    copyToArray(xs = xs, start = 0, len = xs.length)

  def copyToArray(xs: Array[Char], start: Int): xs.type =
    copyToArray(xs = xs, start = start, len = xs.length)

  def copyToArray(xs: Array[Char], start: Int, len: Int): xs.type = {
    if (!(closed || peekByte == PeekByte.FINISHED ||  len <= 0 || start >= xs.length)) {
      if (PeekByte.isFull(peekByte)) {
        xs(start) = peekByte.toChar
        peekByte = PeekByte.EMPTY
        reader.read(xs, start + 1, len - 1)
      } else {
        reader.read(xs, start, len)
      }
    }
    xs
  }
}

object ReaderIterator {

  /** Creates an InputStreamIterator over this InputStream */
  def apply[R <: Reader](reader: R): Iterator[Char] = new ReaderIterator(reader)
}
