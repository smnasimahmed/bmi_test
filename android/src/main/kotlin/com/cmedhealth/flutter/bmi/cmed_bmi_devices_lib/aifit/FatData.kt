package com.cmedhealth.flutter.bmi.cmed_bmi_devices_lib.aifit

import android.util.Log

class FatData(calcParam: CalcParam) {
    fun getBodyFatMap(): Map<String, String> {
        return bodyFatMap
    }

    val weight: Double
    val height: Int
    val gender: Int
    val age: Int
    private val bodyFatMap: MutableMap<String, String>

    init {
        weight = calcParam.c_weight
        height = calcParam.c_height
        gender = calcParam.c_sex
        age = calcParam.c_age
        bodyFatMap = HashMap()
        bodyFatMap[BodyFatEnum.BMR.name] = java.lang.String.valueOf(calcParam.c_bmr)
        bodyFatMap[BodyFatEnum.BFR.name] = java.lang.String.valueOf(calcParam.c_bfr)
        bodyFatMap[BodyFatEnum.BMI.name] = java.lang.String.valueOf(calcParam.c_bmi)
        bodyFatMap[BodyFatEnum.BODY_WATER.name] = java.lang.String.valueOf(calcParam.c_vwc)
        bodyFatMap[BodyFatEnum.BONE_MASS.name] = java.lang.String.valueOf(calcParam.c_bm)
        bodyFatMap[BodyFatEnum.FAT_MASS.name] = java.lang.String.valueOf(calcParam.get_C_fatMass())
        bodyFatMap[BodyFatEnum.METABOLIC_AGE.name] =
            java.lang.String.valueOf(calcParam.c_bodyAge)
        bodyFatMap[BodyFatEnum.MUSCLE_MASS.name] =
            java.lang.String.valueOf(calcParam.get_C_muscleMass())
        bodyFatMap[BodyFatEnum.MUSCLE_RATE.name] = java.lang.String.valueOf(calcParam.c_rom)
        bodyFatMap[BodyFatEnum.OBESITY_LEVEL.name] =
            java.lang.String.valueOf(calcParam.get_C_obesityStatus())
        bodyFatMap[BodyFatEnum.PROTEIN_MASS.name] =
            java.lang.String.valueOf(calcParam.get_C_proteinMass())
        bodyFatMap[BodyFatEnum.PROTEIN_RATE.name] = java.lang.String.valueOf(calcParam.c_pp)
        bodyFatMap[BodyFatEnum.STD_WEIGHT.name] = java.lang.String.valueOf(calcParam.c_stdWeight)
        bodyFatMap[BodyFatEnum.SFR.name] = java.lang.String.valueOf(calcParam.c_sfr)
        bodyFatMap[BodyFatEnum.VFI.name] = java.lang.String.valueOf(calcParam.c_uvi)
        bodyFatMap[BodyFatEnum.WEIGHT_CONTROL.name] =
            java.lang.String.valueOf(calcParam.get_C_weightDiff())
        bodyFatMap[BodyFatEnum.WEIGHT_WITHOUT_FAT.name] =
            java.lang.String.valueOf(calcParam.c_weightWithOutFat)
        Log.v("CalcParamString", calcParam.resultString)
    }
}