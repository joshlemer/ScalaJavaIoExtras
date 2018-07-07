
# Java Io Extras

This is a library for extra integrations between Scala and java.io that should make working with java.io a little bit easier in Scala.

## Features

### Integrations for `java.io.InputStream`

Usage:

```scala
import com.github.joshlemer.java_io_extras._
import java.io.InputStream

// get an InputStream from somewhere
val inputStream: InputStream = ???

// convert it to `Iterator[Byte]`
val iterator: Iterator[Byte] = InputStreamIterator(inputStream)

// convert an Iterator[Byte] to an InputStream
val inputStream2: InputStream = IteratorInputStream(iterator)
```

or you can use the supplied implicit converters:
```scala
import com.github.joshlemer.java_io_extras._
import com.github.joshlemer.java_io_extras.Converters._
import java.io.InputStream

// get an InputStream from somewhere
val inputStream: InputStream = ???

// convert it to `Iterator[Byte]`
val iterator: Iterator[Byte] = inputStream.iterator

// convert an Iterator[Byte] to an InputStream
val inputStream2: InputStream = iterator.inputStream
```
### Integrations for `java.io.Reader`

Usage:

```scala
import com.github.joshlemer.java_io_extras._
import java.io.Reader

// get an InputStream from somewhere
val reader: InputStream = ???

// convert it to `Iterator[Byte]`
val iterator: Iterator[Byte] = InputStreamIterator(inputStream)

// convert an Iterator[Byte] to an InputStream
val inputStream2: InputStream = IteratorInputStream(iterator)
```

or you can use the supplied implicit converters:
```scala
import com.github.joshlemer.java_io_extras._
import com.github.joshlemer.java_io_extras.Converters._
import java.io.Reader

// get an InputStream from somewhere
val reader: Reader =

// convert it to `Iterator[Char]`
val iterator: Iterator[Char] = reader.iterator

// convert an Iterator[Char] to a Reader
val reader2: Reader = iterator.reader
```
