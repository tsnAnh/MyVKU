package dev.tsnanh.myvku.utils

class Constants {
    companion object {
        const val QUOTED_REPLY = "quoted_reply"

        // Display mode
        const val MODE_SYSTEM = "system"
        const val MODE_DARK = "dark"
        const val MODE_LIGHT = "light"

        // URL
        const val DAO_TAO_URL = "http://daotao.vku.udn.vn"
        const val DAO_TAO_UPLOAD_URL = "http://daotao.vku.udn.vn/uploads"

        // Request code
        const val RC_SIGN_IN = 0
        const val RC_IMAGE_PICKER = 1
        const val RC_ADD_PHOTO = 2
        const val RC_PERMISSION = 3

        // Day of week (Vietnamese)
        const val MONDAY = "Hai"
        const val TUESDAY = "Ba"
        const val WEDNESDAY = "Tư"
        const val THURSDAY = "Năm"
        const val FRIDAY = "Sáu"
        const val SATURDAY = "Bảy"

        // WakeLock tag
        const val WAKE_LOCK_TAG = "vku:wakeLock"
        const val POST_TAG = "dev.tsnanh.newreply"
        const val BOTTOM_SHEET_TAG = "new_reply"
        const val POST = "post"

        // Key
        const val THREAD_ID_KEY = "threadId"
        const val THREAD_TITLE_KEY = "threadTitle"
        const val THREAD_WORK_KEY = "container"
        const val TOKEN_KEY = "token"
        const val UNIQUE_ID_KEY = "uid"
        const val IMAGES_KEY = "images"
        const val REPLY_KEY = "reply"

        // Worker key
        const val IMAGE_URL_KEY = "imageURL"
        const val THREAD_KEY = "thread"

        // DateFormat template
        const val DATE_FORMAT_PATTERN_DASH = "yyyy/MM/dd - HH:mm:ss"
        const val DATE_FORMAT_PATTERN_SPACE = "yyyy/MM/dd HH:mm:ss"

        // SaveState
        const val URIS_KEY = "uris"

        // Shared elements key
        const val FAB_TRANSFORM_TO_NEW_THREAD = "view"
        const val FAB_TRANSFORM_TO_NEW_REPLY = "view2"

        val ROOMS = mapOf(
            "B203" to "https://meet.google.com/cvg-epyu-jua",
            "B204" to "https://meet.google.com/wso-vdsq-qsg",
            "B205" to "https://meet.google.com/hjj-ndar-kcc",
            "B403" to "https://meet.google.com/npt-szam-ccs",
            "B404" to "https://meet.google.com/cmj-ppvs-qnz",
            "A402" to "https://meet.google.com/par-bhho-hnu",
            "A403" to "https://meet.google.com/yww-wcef-piu",
            "A502" to "https://meet.google.com/cto-bjro-txq"
        )
    }
}
