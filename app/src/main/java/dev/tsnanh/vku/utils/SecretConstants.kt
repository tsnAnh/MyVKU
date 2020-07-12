package dev.tsnanh.vku.utils

class SecretConstants {
    companion object {
        const val TKB_URL = "http://daotao.sict.udn.vn/tkb"
        const val NEWS_URL = "http://daotao.sict.udn.vn/baimoinhat"
        val SINGLE_NEWS_URL: (String) -> String = {
            "http://daotao.sict.udn.vn/tin-tuc/news-$it.html"
        }
        const val TEACHERS_URL = "http://daotao.sict.udn.vn/listgv"
    }
}