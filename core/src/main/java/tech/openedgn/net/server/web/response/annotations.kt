package tech.openedgn.net.server.web.response

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
 annotation class Controller()

@Controller
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Get(val bindLocation:String)

@Controller
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Post(val bindLocation:String)

/**
 * form 表单标记
 * @property formName String
 * @constructor
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class FormItem(val formName:String)


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class RawData()