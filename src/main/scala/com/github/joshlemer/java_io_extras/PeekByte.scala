package com.github.joshlemer.java_io_extras

private[java_io_extras] object PeekByte {
  /** Indicates the end of the stream has been reached */
  val FINISHED: Int = -1
  /** Indicates the peekByte is empty */
  val EMPTY: Int = -2

  /** Returns true if peekByte is not empty or finished */
  def isFull(peekByte: Int): Boolean = peekByte >= 0
}

