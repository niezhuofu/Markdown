package com.nzf.markdown.bean

/**
 * Created by niezhuofu on 17-11-9.
 */
class MDFileBean {
    var fileName: String? = null
        get() = field
        set(value) {
            field = value
        }

    var filePath: String? = null
        get() = field
        set(value) {
            field = value
        }

    var fileLastTime: Long? = null
        get() = field
        set(value) {
            field = value
        }

    var fileSize: Long? = null
        get() = field
        set(value) {
            field = value
        }

    var fileType: Int? = null
        get() = field
        set(value) {
            field = value
        }
}