package tech.openedgn.net.server.web

/**
 * 表明只接受什么类型的 Content-Type
 * @property type String
 * @constructor
 */
@Target(AnnotationTarget.FUNCTION,AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ContentType(val type:String )

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
 annotation class Controller(val bindLocation:String)

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Get(val bindLocation:String)

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Post(val bindLocation:String)