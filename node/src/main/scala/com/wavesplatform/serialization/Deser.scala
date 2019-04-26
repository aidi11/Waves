package com.wavesplatform.serialization

import com.google.common.primitives.{Bytes, Shorts}

object Deser {

  def serializeBoolean(b: Boolean): Array[Byte] = if (b) Array(1: Byte) else Array(0: Byte)

  def serializeArray(b: Array[Byte]): Array[Byte] = {
    val lengthBytes = Shorts.toByteArray(b.length.ensuring(_.isValidShort).toShort)
    Bytes.concat(lengthBytes, b)
  }

  def parseArrayWithLength(bytes: Array[Byte], position: Int): (Array[Byte], Int) = {
    val length = Shorts.fromByteArray(bytes.slice(position, position + 2))
    (bytes.slice(position + 2, position + 2 + length), position + 2 + length)
  }

  def parseArrayByLength(bytes: Array[Byte], position: Int, length: Int): (Array[Byte], Int) = {
    (bytes.slice(position, position + length), position + length)
  }

  def parseByteArrayOption(bytes: Array[Byte], position: Int, length: Int): (Option[Array[Byte]], Int) = {
    if (bytes.slice(position, position + 1).head == (1: Byte)) {
      val b = bytes.slice(position + 1, position + 1 + length)
      (Some(b), position + 1 + length)
    } else (None, position + 1)
  }

  def parseOption[T](bytes: Array[Byte], position: Int, length: Int = -1)(deser: Array[Byte] => T): (Option[T], Int) = {
    if (bytes.slice(position, position + 1).head == (1: Byte)) {
      val (arr, arrPosEnd) =
        if (length < 0) {
          parseArrayWithLength(bytes, position + 1)
        } else {
          parseArrayByLength(bytes, position + 1, length)
        }
      (Some(deser(arr)), arrPosEnd)
    } else (None, position + 1)
  }

  def parseArrays(bytes: Array[Byte]): Seq[Array[Byte]] = {
    val length = Shorts.fromByteArray(bytes.slice(0, 2))
    val r = (0 until length).foldLeft((Seq.empty[Array[Byte]], 2)) {
      case ((acc, pos), _) =>
        val (arr, nextPos) = parseArrayWithLength(bytes, pos)
        (acc :+ arr, nextPos)
    }
    r._1
  }

  def serializeOption[T](b: Option[T])(ser: T => Array[Byte]): Array[Byte] = b.map(a => (1: Byte) +: ser(a)).getOrElse(Array(0: Byte))

  def serializeOptionOfArray[T](b: Option[T])(ser: T => Array[Byte]): Array[Byte] = b.map(a => (1: Byte) +: serializeArray(ser(a))).getOrElse(Array(0: Byte))

  def serializeArrays(bs: Seq[Array[Byte]]): Array[Byte] = {
    val countBytes  = Shorts.toByteArray(bs.length.ensuring(_.isValidShort).toShort)
    val arraysBytes = Bytes.concat(bs.map(serializeArray): _*)

    Bytes.concat(countBytes, arraysBytes)
  }
}
