package com.cmed.plugin.lib.flutter.cmed_health_flutter_devices_lib.utils
import androidx.annotation.Keep

@Keep
class CMEDUser(
    private var id: Long?,
    private var gender: String?,
    var ageInDays: Int,
    var birthDate: Long,
    var heightInCm: Double,
    var weightInKg: Double
) {

    fun getGenderIndex(): Int? {
        return GenderEnum.getGenderEnum(gender.toString())?.index
    }

    fun setGender(gender: String?) {
        this.gender = gender
    }

    override fun toString(): String {
        return "User{id=$id, sex=$gender, age=$ageInDays,  birthDate=$birthDate, height=$heightInCm, weight=$weightInKg}"
    }
}