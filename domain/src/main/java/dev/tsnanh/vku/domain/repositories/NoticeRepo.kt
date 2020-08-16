package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.Absence
import dev.tsnanh.vku.domain.entities.MakeUpClass
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface NoticeRepo {
    fun getAbsenceNotice(time: String): Flow<List<Absence>>
    fun getMakeUpClass(time: String): Flow<List<MakeUpClass>>
}

class NoticeRepoImpl : NoticeRepo {
    override fun getAbsenceNotice(time: String): Flow<List<Absence>> {
        return flow {
            emit(
                VKUServiceApi.network.getAbsenceNotice(
                    "http://daotao.sict.udn.vn/thongbaonghi",
                    time
                )
            )
        }
    }

    override fun getMakeUpClass(time: String): Flow<List<MakeUpClass>> {
        return flow {
            emit(
                VKUServiceApi.network.getMakeUpClassNotice(
                    "http://daotao.sict.udn.vn/thongbaobu",
                    time
                )
            )
        }
    }
}