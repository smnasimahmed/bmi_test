package com.cmed.plugin.lib.flutter.cmed_health_flutter_devices_lib.utils

enum class GenderEnum(val index: Int, val nameEn: String, val nameBn: String) {
    MALE(1, "Male", "পুরুষ"), FEMALE(2, "Female", "মহিলা"), OTHERS(3, "Others", "অন্যান্য");

    companion object {
        fun getGender(index: Int): GenderEnum? {
            for (gender in GenderEnum.values()) {
                if (gender.index == index) return gender
            }
            return null
        }
        fun getGenderEnum(genderName: String): GenderEnum? {
            for (gender in GenderEnum.values()) {
                if (gender.name == genderName) return gender
            }
            return GenderEnum.MALE
        }
    }
}
