package com.github.joshlemer.inputstreamiterator.java_io_extras

import java.io.{ByteArrayInputStream, InputStream}

import com.github.joshlemer.java_io_extras.InputStreamIterator
import junit.framework.TestCase

import scala.util.Try

class InputStreamIteratorTestCase extends TestCase {

  def makeInputStreamIterator(bytes: Byte*): InputStreamIterator[TestInputStream] =
    InputStreamIterator(new TestInputStream(new ByteArrayInputStream(bytes.toArray)))

  def testNewEmptyIsNotClosed(): Unit = {
    val isi = makeInputStreamIterator()
    assert(!isi.inputStream.isClosed, "new input stream should not be closed")
  }
  def testNewEmptyIsClosedAfterReading(): Unit = {
    {
      val isi = makeInputStreamIterator()
      assert(isi.peek() == -1, "empty input stream should return -1 when peeked")
      assert(isi.inputStream.isClosed, "empty input stream should close after read")
    }
    {
      val isi = makeInputStreamIterator()
      assert(!isi.hasNext, "empty input stream should not have next")
      assert(isi.inputStream.isClosed, "empty input stream should close after read")
    }
    {
      val isi = makeInputStreamIterator()
      val t= Try(isi.next())
      assert(t.isFailure, "empty input stream should fail on read")
      assert(isi.inputStream.isClosed, "empty input stream should close after read")
    }
    {
      val isi = makeInputStreamIterator()
      assert(isi.toList == Nil, "empty input stream to list should be Nil")
      assert(isi.inputStream.isClosed, "empty input stream should close after read")
    }
  }
  def testNonEmpty(): Unit = {
    {
      val isi = makeInputStreamIterator(1)
      assert(!isi.inputStream.isClosed, "single elem input stream should not be closed")
      assert(isi.peek() == 1, "single elem input stream should return its byte when peeked")
      assert(!isi.inputStream.isClosed, "single elem input stream should not close when peeked")

      assert(isi.peek() == 1, "single elem input stream should return its byte when peeked twice")
      assert(!isi.inputStream.isClosed, "single elem input stream should not close when peeked twice")
    }
    {
      val isi = makeInputStreamIterator(1)
      assert(!isi.inputStream.isClosed, "single elem input stream should not be closed")
      assert(isi.hasNext, "single elem input stream should have next elem")
      assert(!isi.inputStream.isClosed, "single elem input stream should not be closed when asked if has next")
      assert(isi.hasNext, "single elem input stream should have next elem, when asked twice")
      assert(!isi.inputStream.isClosed, "single elem input stream should not be closed when asked if has next twice")
    }
    {
      val isi = makeInputStreamIterator(10)
      assert(isi.next() == 10, "single elem input stream should return its elem")
      assert(!isi.inputStream.isClosed, "single elem input stream should not close when single elem is read")

      val n = Try(isi.next())
      assert(n.isFailure, "single elem input stream should fail on second next()")
      assert(isi.inputStream.isClosed, "single elem input stream should close when second elem elem is read")
    }
    {
      val isi = makeInputStreamIterator(10)
      assert(isi.next() == 10, "single elem input stream should return its elem")
      assert(!isi.inputStream.isClosed, "single elem input stream should not close when single elem is read")

      assert(!isi.hasNext, "single elem input stream should not have next elem after a read")
      assert(isi.inputStream.isClosed, "single elem input stream should be closed after a read and a hasNext")

    }
    {
      val isi = makeInputStreamIterator(10)
      assert(isi.next() == 10, "single elem input stream should return its elem")
      assert(!isi.inputStream.isClosed, "single elem input stream should not close when single elem is read")

      assert(isi.peek == -1, "single elem input stream should return -1 when peeked after empty")
      assert(isi.inputStream.isClosed, "single elem input stream should be closed after a read and a peek")
    }
    {
      val isi = makeInputStreamIterator(10)
      assert(isi.toList == List(10), "single elem input stream should return its elem")
      assert(isi.inputStream.isClosed, "single elem input stream should not close when single elem is read")

      assert(isi.toList == Nil)
      assert(isi.peek == -1, "single elem input stream should return -1 when peeked after empty")
      assert(isi.inputStream.isClosed, "single elem input stream should be closed after a read and a peek")
    }
  }

  def testToList(): Unit = {
    val isi = makeInputStreamIterator(10, 11, 12, 13, 14)
    assert(isi.toList == List(10, 11, 12, 13, 14))
    assert(isi.inputStream.isClosed)
  }

  def testToLazyList(): Unit = {
    val isi = makeInputStreamIterator(10, 11, 12, 13, 14)
    val lazyList = LazyList.from(isi)
    assert(!isi.inputStream.isClosed)
  }

  def testCopyToArray(): Unit = {
    val isi = makeInputStreamIterator(10, 11, 12, 13, 14)
    val arr = new Array[Byte](10)
    isi.copyToArray(arr)
    assert(arr sameElements  Array(10,11,12,13,14,0,0,0,0,0))
  }
}

class TestInputStream(val byteArrayInputStream: ByteArrayInputStream) extends InputStream {

  private var _isClosed = false

  def isClosed: Boolean = _isClosed

  override def read(): Int = byteArrayInputStream.read()

  override def close(): Unit = {
    byteArrayInputStream.close()
    _isClosed = true
  }
}
