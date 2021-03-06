/*
 * Copyright (C) 2012 The Regents of The University California.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package stream.framework.output.column

import java.nio.ByteBuffer
import java.nio.ByteOrder

import it.unimi.dsi.fastutil.longs.LongArrayList

class LongColumnBuilder extends ColumnBuilder[Long] {

  private var _arr: LongArrayList = null

  override def initialize(initialSize: Int) {
    _arr = new LongArrayList(initialSize)
    super.initialize(initialSize)
  }

  override def append(v: Long) {
    _arr.add(v)
  }

  override def appendNull() {
    _nullBitmap.set(_arr.size)
    _arr.add(0)
  }

  override def build: ByteBuffer = {
    val buf = ByteBuffer.allocate(
      _arr.size * 8 + ColumnIterator.COLUMN_TYPE_LENGTH + sizeOfNullBitmap)
    buf.order(ByteOrder.nativeOrder())
    buf.putLong(ColumnIterator.LONG)

    writeNullBitmap(buf)

    var i = 0
    while (i < _arr.size) {
      buf.putLong(_arr.get(i))
      i += 1
    }
    buf.rewind()
    buf
  }
}
