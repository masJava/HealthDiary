package mas.com.health_diary.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class Health(
    val id: String = "",
    var dateFull: Date = Date(),
    val date: String = "",
    val time: String = "",
    val max: Int = 0,
    val min: Int = 0,
    val pulse: Int = 0,
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Health

        if (id != other.id) return false
        return true
    }
}