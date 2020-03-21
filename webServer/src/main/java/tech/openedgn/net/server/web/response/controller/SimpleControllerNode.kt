package tech.openedgn.net.server.web.response.controller

import tech.openedgn.net.server.web.WebConfig
import tech.openedgn.net.server.web.request.HttpRequest
import java.util.concurrent.locks.ReentrantReadWriteLock

class SimpleControllerNode(config: WebConfig.InternalConfig) : BaseControllerNode(config) {
    private val read2WriteLock = ReentrantReadWriteLock(true)
    private val readLock = read2WriteLock.readLock()
    private val writeLock = read2WriteLock.writeLock()

    override fun find(
        location: List<String>,
        request: HttpRequest,
        regexValues: MutableList<String>,
        regexControllers: MutableList<Controller>
    ) {
        readLock.lock()
        val rule = config.locationRule
        try {
            rule.acceptLocationSplit()
        } catch (_: Exception) {

        } finally {
            readLock.unlock()
        }
    }

    override fun add(location: List<String>, controllerBean: Controller): Boolean {
        writeLock.lock()
        try {

        } catch (_: Exception) {
        } finally {
            writeLock.unlock()
        }
        return false
    }


    override fun close() {
        TODO("Not yet implemented")
    }

}