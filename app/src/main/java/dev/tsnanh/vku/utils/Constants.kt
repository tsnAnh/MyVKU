package dev.tsnanh.vku.utils

class Constants {
    companion object {
        // Display mode
        const val MODE_SYSTEM = "system"
        const val MODE_DARK = "dark"
        const val MODE_LIGHT = "light"

        // URL
        const val DAO_TAO_URL = "http://daotao.sict.udn.vn"

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
        const val POST_TAG = "create_post"
        const val BOTTOM_SHEET_TAG = "new_reply"
        const val POST = "post"

        // Key
        const val THREAD_ID_KEY = "threadId"
        const val THREAD_TITLE_KEY = "threadTitle"
        const val CONTAINER_KEY = "container"
        const val TOKEN_KEY = "token"
        const val UNIQUE_ID_KEY = "uid"
        const val IMAGE_KEY = "image"
        const val REPLY_KEY = "reply"

        // Worker key
        const val IMAGE_URL_KEY = "imageURL"
        const val THREAD_KEY = "thread"

        // DateFormat template
        const val DATE_FORMAT_PATTERN = "yyyy/MM/dd - HH:mm:ss"

        // SaveState
        const val URIS_KEY = "uris"

        // Shared elements key
        const val FAB_TRANSFORM_TO_NEW_THREAD = "view"
    }
}