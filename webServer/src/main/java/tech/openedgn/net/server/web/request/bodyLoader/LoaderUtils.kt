package tech.openedgn.net.server.web.request.bodyLoader

import tech.openedgn.net.server.web.utils.dataBlock.IDataBlock
import java.lang.IndexOutOfBoundsException


/**
 * 在  IDataBlock 下搜索 data 字段首次出现的位置
 *
 * (搜索算法没有任何优化！)
 *
 * @param start Long 搜索开始的位置
 * @param end Long 截止位置
 * @return Long 第一次出现的位置，如果未发现则返回 -1
 */
fun IDataBlock.searchIndex(data: ByteArray, start: Long = 0, end: Long = size): Long {
    if (start >= size || start >= end) {
        throw IndexOutOfBoundsException("start  > size or start >= end ")
    }
    val dataSize = data.size
    if (size < dataSize) {
        return -1
    }
    val lastIndex = end - dataSize + 1 //如果到此处无法得到结果则返回 -1
    var index: Long = start
    var next :Int
    var equalsTrue:Boolean
    while (true) {
        if (index > lastIndex) {
            return -1
        }
        next = 0
        equalsTrue = false
        for ((ind, value) in (index until (index + dataSize)).withIndex()) {
            if (get(value) == data[ind]) {
                equalsTrue = true
            } else {
                equalsTrue = false
                break
            }
            if (get(value) == data[0] && next == 0) {
                next = ind
            }
        }
        if (equalsTrue) {
            return index
        }
        if (next != 0) {
            index += next
        } else {
            index++
        }
    }
}