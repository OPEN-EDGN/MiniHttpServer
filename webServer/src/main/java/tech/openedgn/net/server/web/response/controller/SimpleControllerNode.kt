package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.WebConfig
import tech.openedgn.net.server.web.consts.METHOD
import tech.openedgn.net.server.web.request.BaseHttpRequest
import java.util.concurrent.locks.ReentrantReadWriteLock

class SimpleControllerNode(config: WebConfig.InternalConfig) : BaseControllerNode(config) {
    private val read2WriteLock = ReentrantReadWriteLock(true)
    private val readLock = read2WriteLock.readLock()
    private val writeLock = read2WriteLock.writeLock()
    override fun find(request: BaseHttpRequest): IController? {

        readLock.lock()
        val rule = config.locationRule
        try {
            rule.acceptLocationSplit()
        } catch (_: Exception) {

        } finally {
            readLock.unlock()
        }
        TODO("Not yet implemented")
    }

    override fun add(method: METHOD, location: String, controllerBean: IController): Boolean {
        writeLock.lock()
        try {

        } catch (_: Exception) {
        } finally {
            writeLock.unlock()
        }
        TODO("Not yet implemented")
    }


    override fun close() {
        TODO("Not yet implemented")
    }

}