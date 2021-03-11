package dev.tsnanh.myvku.domain.constants

object SecretConstants {
    val SINGLE_NEWS_URL = { cmsId: String ->
        "http://daotao.vku.udn.vn/thong-bao-chung/news-$cmsId.html"
    }

    const val ABSENCE_URL = "http://daotao.vku.udn.vn/thongbaonghi"
    const val MAKEUP_URL = "http://daotao.vku.udn.vn/thongbaobu"
    const val TEACHERS_URL = "http://daotao.vku.udn.vn/listgv"
    const val NEWS_URL = "http://daotao.vku.udn.vn/thong-bao-chung/new-"
    const val TKB_URL = "http://daotao.vku.udn.vn/tkb"
}