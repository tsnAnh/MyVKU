package dev.tsnanh.vku.domain.constants

class SecretConstants {
    companion object {
        const val TKB_URL = "http://daotao.vku.udn.vn/tkb"
        const val NEWS_URL = "http://daotao.vku.udn.vn/baimoinhat"
        val SINGLE_NEWS_URL: (String) -> String = {
            "http://daotao.vku.udn.vn/tin-tuc/news-$it.html"
        }
        const val TEACHERS_URL = "http://daotao.vku.udn.vn/listgv"
        const val ABSENCE_URL = "http://daotao.vku.udn.vn/thongbaonghi"
        const val MAKEUP_URL =
            "http://daotao.vku.udn.vn/thongbaobu"
    }
}