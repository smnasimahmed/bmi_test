package com.cmedhealth.flutter.bmi.cmed_bmi_devices_lib.aifit

import aicare.net.cn.iweightlibrary.entity.BodyFatData
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
class CalcParam : Parcelable {
    protected var bodyFatData: BodyFatData? = null
    var c_weight = 0.0
        protected set
    var c_bmi = 0.0
        protected set
    var c_bfr = 0.0
        protected set
    var c_sfr = 0.0
        protected set
    var c_uvi = 0
        protected set
    var c_rom = 0.0
        protected set
    var c_bmr = 0.0
        protected set
    var c_bm = 0.0
        protected set
    var c_vwc = 0.0
        protected set
    var c_bodyAge = 0
        protected set
    var c_pp = 0.0
        protected set
    var c_number = 0
        protected set
    var c_sex = 0
        protected set
    var c_age = 0
        protected set
    var c_height = 0
        protected set
    var c_adc = 0
        protected set
    protected var c_proteinMass: Double = 0.0
        get() {
            return field
        }
    protected var c_muscleMass = 0.0
    protected var c_fatMass = 0.0
    protected var c_fatNotWeight = 0.0
    protected var c_obesityStatus: String? = null
    var c_stdWeight = 0.0
    protected var c_weightDiff = 0.0
    protected var c_weightDiffStatus: String? = null
    protected var maleWeight: HashMap<Double?, List<Double>>? = null
    protected var femaleWeight: HashMap<Double?, List<Double>>? = null

    constructor(bodyFatData: BodyFatData?) {
        this.bodyFatData = bodyFatData
        init()
    }

    protected constructor(`in`: Parcel) {
        c_weight = `in`.readDouble()
        c_bmi = `in`.readDouble()
        c_bfr = `in`.readDouble()
        c_sfr = `in`.readDouble()
        c_uvi = `in`.readInt()
        c_rom = `in`.readDouble()
        c_bmr = `in`.readDouble()
        c_bm = `in`.readDouble()
        c_vwc = `in`.readDouble()
        c_bodyAge = `in`.readInt()
        c_pp = `in`.readDouble()
        c_number = `in`.readInt()
        c_sex = `in`.readInt()
        c_age = `in`.readInt()
        c_height = `in`.readInt()
        c_adc = `in`.readInt()
        c_proteinMass = `in`.readDouble()
        c_muscleMass = `in`.readDouble()
        c_fatMass = `in`.readDouble()
        c_fatNotWeight = `in`.readDouble()
        c_obesityStatus = `in`.readString()
        c_stdWeight = `in`.readDouble()
        c_weightDiff = `in`.readDouble()
        c_weightDiffStatus = `in`.readString()
    }

    fun init() {
        c_weight = bodyFatData!!.weight / 10
        c_bmi = bodyFatData!!.bmi
        c_bfr = bodyFatData!!.bfr
        c_sfr = bodyFatData!!.sfr
        c_uvi = bodyFatData!!.uvi
        c_rom = bodyFatData!!.rom
        c_bmr = bodyFatData!!.bmr
        c_bm = bodyFatData!!.bm
        c_vwc = bodyFatData!!.vwc
        c_bodyAge = bodyFatData!!.bodyAge
        c_pp = bodyFatData!!.pp
        c_number = bodyFatData!!.number
        c_sex = bodyFatData!!.sex
        c_age = bodyFatData!!.age
        c_height = bodyFatData!!.height
        c_adc = bodyFatData!!.adc
        c_proteinMass = get_C_proteinMass()
        c_muscleMass = get_C_muscleMass()
        c_fatMass = get_C_fatMass()
        c_fatNotWeight = c_weightWithOutFat
        c_obesityStatus = get_C_obesityStatus()
        init_weightStdScale()
    }

    fun get_C_proteinMass(): Double {
        return c_weight * (c_pp / 100)
    }

    fun get_C_muscleMass(): Double {
        return c_weight * (c_rom / 100)
    }

    fun get_C_fatMass(): Double {
        return c_weight * (c_bfr / 100)
    }

    val c_weightWithOutFat: Double
        get() = c_weight - c_fatMass

    fun get_C_obesityStatus(): String {

        //source: https://www.rush.edu/health-wellness/quick-guides/what-is-a-healthy-weight
        var temp = "Not defined"
        if (c_bmi >= 19 && c_bmi <= 24) {
            temp = "Normal"
        }
        if (c_bmi >= 25 && c_bmi <= 29) {
            temp = "Over Weight"
        }
        if (c_bmi >= 30) {
            temp = "obese"
        }
        return temp
    }

    fun init_weightStdScale() {
        if (c_sex == 1) {
            init_maleWeight()
            c_stdWeight = getStdWeight(maleWeight)
        } else {
            init_femaleWeight()
            c_stdWeight = getStdWeight(femaleWeight)
        }
        c_weightDiffStatus = get_C_weightDiffStatus()
        c_weightDiff = get_C_weightDiff()
    }

    fun init_maleWeight() {

        //source : https://www.disabled-world.com/calculators-charts/height-weight.php
        maleWeight = HashMap()
        val value1: MutableList<Double> = ArrayList()
        value1.add(28.5)
        value1.add(34.9)
        val value2: MutableList<Double> = ArrayList()
        value2.add(30.8)
        value2.add(38.1)
        val value3: MutableList<Double> = ArrayList()
        value3.add(33.5)
        value3.add(40.8)
        val value4: MutableList<Double> = ArrayList()
        value4.add(35.8)
        value4.add(43.9)
        val value5: MutableList<Double> = ArrayList()
        value5.add(38.5)
        value5.add(46.7)
        val value6: MutableList<Double> = ArrayList()
        value6.add(40.8)
        value6.add(49.9)
        val value7: MutableList<Double> = ArrayList()
        value7.add(43.1)
        value7.add(53.0)
        val value8: MutableList<Double> = ArrayList()
        value8.add(45.8)
        value8.add(55.8)
        val value9: MutableList<Double> = ArrayList()
        value9.add(48.1)
        value9.add(58.9)
        val value10: MutableList<Double> = ArrayList()
        value10.add(50.8)
        value10.add(61.6)
        val value11: MutableList<Double> = ArrayList()
        value11.add(53.0)
        value11.add(64.8)
        val value12: MutableList<Double> = ArrayList()
        value12.add(55.3)
        value12.add(68.0)
        val value13: MutableList<Double> = ArrayList()
        value13.add(58.0)
        value13.add(70.7)
        val value14: MutableList<Double> = ArrayList()
        value14.add(60.3)
        value14.add(73.9)
        val value15: MutableList<Double> = ArrayList()
        value15.add(63.0)
        value15.add(76.6)
        val value16: MutableList<Double> = ArrayList()
        value16.add(65.3)
        value16.add(79.8)
        val value17: MutableList<Double> = ArrayList()
        value17.add(67.6)
        value17.add(83.0)
        val value18: MutableList<Double> = ArrayList()
        value18.add(70.3)
        value18.add(85.7)
        val value19: MutableList<Double> = ArrayList()
        value19.add(72.6)
        value19.add(88.9)
        val value20: MutableList<Double> = ArrayList()
        value20.add(75.3)
        value20.add(91.6)
        val value21: MutableList<Double> = ArrayList()
        value21.add(77.5)
        value21.add(94.8)
        val value22: MutableList<Double> = ArrayList()
        value22.add(79.8)
        value22.add(98.0)
        val value23: MutableList<Double> = ArrayList()
        value23.add(82.5)
        value23.add(100.6)
        val value24: MutableList<Double> = ArrayList()
        value24.add(84.8)
        value24.add(103.8)
        val value25: MutableList<Double> = ArrayList()
        value25.add(87.5)
        value25.add(106.5)
        val value26: MutableList<Double> = ArrayList()
        value26.add(89.8)
        value26.add(109.7)
        val value27: MutableList<Double> = ArrayList()
        value27.add(92.0)
        value27.add(112.9)
        val value28: MutableList<Double> = ArrayList()
        value28.add(94.8)
        value28.add(115.6)
        val value29: MutableList<Double> = ArrayList()
        value29.add(97.0)
        value29.add(118.8)
        val value30: MutableList<Double> = ArrayList()
        value30.add(99.8)
        value30.add(121.5)
        val value31: MutableList<Double> = ArrayList()
        value31.add(102.0)
        value31.add(124.7)
        maleWeight!![135.0] = value1
        maleWeight!![140.0] = value2
        maleWeight!![142.0] = value3
        maleWeight!![145.0] = value4
        maleWeight!![147.0] = value5
        maleWeight!![150.0] = value6
        maleWeight!![152.0] = value7
        maleWeight!![155.0] = value8
        maleWeight!![157.0] = value9
        maleWeight!![160.0] = value10
        maleWeight!![163.0] = value11
        maleWeight!![165.0] = value12
        maleWeight!![168.0] = value13
        maleWeight!![170.0] = value14
        maleWeight!![173.0] = value15
        maleWeight!![175.0] = value16
        maleWeight!![178.0] = value17
        maleWeight!![180.0] = value18
        maleWeight!![183.0] = value19
        maleWeight!![185.0] = value20
        maleWeight!![188.0] = value21
        maleWeight!![190.0] = value22
        maleWeight!![193.0] = value23
        maleWeight!![195.0] = value24
        maleWeight!![198.0] = value25
        maleWeight!![200.0] = value26
        maleWeight!![203.0] = value27
        maleWeight!![205.0] = value28
        maleWeight!![208.0] = value29
        maleWeight!![210.0] = value30
        maleWeight!![215.0] = value31
    }

    fun init_femaleWeight() {
        femaleWeight = HashMap()
        val value1: MutableList<Double> = ArrayList()
        value1.add(28.5)
        value1.add(34.9)
        val value2: MutableList<Double> = ArrayList()
        value2.add(30.8)
        value2.add(37.6)
        val value3: MutableList<Double> = ArrayList()
        value3.add(32.6)
        value3.add(39.9)
        val value4: MutableList<Double> = ArrayList()
        value4.add(34.9)
        value4.add(42.6)
        val value5: MutableList<Double> = ArrayList()
        value5.add(36.4)
        value5.add(44.9)
        val value6: MutableList<Double> = ArrayList()
        value6.add(39.0)
        value6.add(47.6)
        val value7: MutableList<Double> = ArrayList()
        value7.add(40.8)
        value7.add(49.9)
        val value8: MutableList<Double> = ArrayList()
        value8.add(43.1)
        value8.add(52.6)
        val value9: MutableList<Double> = ArrayList()
        value9.add(44.9)
        value9.add(54.9)
        val value10: MutableList<Double> = ArrayList()
        value10.add(47.2)
        value10.add(57.6)
        val value11: MutableList<Double> = ArrayList()
        value11.add(49.0)
        value11.add(59.9)
        val value12: MutableList<Double> = ArrayList()
        value12.add(51.2)
        value12.add(62.6)
        val value13: MutableList<Double> = ArrayList()
        value13.add(53.0)
        value13.add(64.8)
        val value14: MutableList<Double> = ArrayList()
        value14.add(55.3)
        value14.add(67.6)
        val value15: MutableList<Double> = ArrayList()
        value15.add(57.1)
        value15.add(69.8)
        val value16: MutableList<Double> = ArrayList()
        value16.add(59.4)
        value16.add(72.6)
        val value17: MutableList<Double> = ArrayList()
        value17.add(61.2)
        value17.add(74.8)
        val value18: MutableList<Double> = ArrayList()
        value18.add(63.5)
        value18.add(77.5)
        val value19: MutableList<Double> = ArrayList()
        value19.add(65.3)
        value19.add(79.8)
        val value20: MutableList<Double> = ArrayList()
        value20.add(67.6)
        value20.add(82.5)
        val value21: MutableList<Double> = ArrayList()
        value21.add(69.4)
        value21.add(84.8)
        val value22: MutableList<Double> = ArrayList()
        value22.add(71.6)
        value22.add(87.5)
        val value23: MutableList<Double> = ArrayList()
        value23.add(73.5)
        value23.add(89.8)
        val value24: MutableList<Double> = ArrayList()
        value24.add(75.7)
        value24.add(92.5)
        val value25: MutableList<Double> = ArrayList()
        value25.add(77.5)
        value25.add(94.8)
        val value26: MutableList<Double> = ArrayList()
        value26.add(79.8)
        value26.add(97.5)
        val value27: MutableList<Double> = ArrayList()
        value27.add(81.6)
        value27.add(99.8)
        val value28: MutableList<Double> = ArrayList()
        value28.add(83.9)
        value28.add(102.5)
        val value29: MutableList<Double> = ArrayList()
        value29.add(85.7)
        value29.add(104.8)
        val value30: MutableList<Double> = ArrayList()
        value30.add(88.0)
        value30.add(107.5)
        val value31: MutableList<Double> = ArrayList()
        value31.add(89.8)
        value31.add(109.7)
        femaleWeight!![135.0] = value1
        femaleWeight!![140.0] = value2
        femaleWeight!![142.0] = value3
        femaleWeight!![145.0] = value4
        femaleWeight!![147.0] = value5
        femaleWeight!![150.0] = value6
        femaleWeight!![152.0] = value7
        femaleWeight!![155.0] = value8
        femaleWeight!![157.0] = value9
        femaleWeight!![160.0] = value10
        femaleWeight!![163.0] = value11
        femaleWeight!![165.0] = value12
        femaleWeight!![168.0] = value13
        femaleWeight!![170.0] = value14
        femaleWeight!![173.0] = value15
        femaleWeight!![175.0] = value16
        femaleWeight!![178.0] = value17
        femaleWeight!![180.0] = value18
        femaleWeight!![183.0] = value19
        femaleWeight!![185.0] = value20
        femaleWeight!![188.0] = value21
        femaleWeight!![190.0] = value22
        femaleWeight!![193.0] = value23
        femaleWeight!![195.0] = value24
        femaleWeight!![198.0] = value25
        femaleWeight!![200.0] = value26
        femaleWeight!![203.0] = value27
        femaleWeight!![205.0] = value28
        femaleWeight!![208.0] = value29
        femaleWeight!![210.0] = value30
        femaleWeight!![215.0] = value31
    }

    fun getStdWeight(weightHash: HashMap<Double?, List<Double>>?): Double {
        var tempHeight = c_height.toDouble()
        val stdWeightList: List<Double>
        val stdWeight: Double
        if (weightHash!!.containsKey(tempHeight)) {
            stdWeightList = weightHash[tempHeight]!!
            stdWeight = (stdWeightList[0] + stdWeightList[1]) / 2
        } else {
            tempHeight = (Math.round(tempHeight / 5) * 5).toDouble()
            stdWeightList = weightHash[tempHeight]!!
            stdWeight = (stdWeightList[0] + stdWeightList[1]) / 2
        }
        return stdWeight
    }

    fun get_C_weightDiffStatus(): String {
        var temp = "null"
        if (c_weight > c_stdWeight) {
            temp = "Need to loose"
        }
        if (c_weight < c_stdWeight) {
            temp = "Need to gain"
        }
        return temp
    }

    fun get_C_weightDiff(): Double {
        return if (c_weight == c_stdWeight) {
            0.0
        } else {
            Math.abs(c_weight - c_stdWeight)
        }
    }

    val resultString: String
        get() = ("weight>>" + c_weight + " , sex>>" + c_sex + " , age>>" + c_age + " , number>>" + c_number
                + " , height>>" + c_height + " , BMI>>" + c_bmi + " , BFR>>" + c_bfr + " , Subcuteneous Fat>>" + c_sfr
                + " , Visceral Fat Index>>" + c_uvi + " , Muscle Rate>>" + c_rom + " , Basal Metabolic Rate>>" + c_bmr + " , Bone Mass>>" + c_bm
                + " , Body Water>>" + c_vwc + " , Metabolic Age>>" + c_bodyAge + " , Protein Rate>>" + c_pp
                + " , Impedance>>" + c_adc + " , Protein Mass>>" + c_proteinMass + " , Muscle Mass>>" + c_muscleMass
                + " , Fat Mass>>" + c_fatMass + " , Weight without fat>>" + c_fatNotWeight
                + " , Obesity Level>>" + c_obesityStatus + " , Standard Weight>>" + c_stdWeight
                + " , " + c_weightDiffStatus + ">>" + c_weightDiff)

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeDouble(c_weight)
        dest.writeDouble(c_bmi)
        dest.writeDouble(c_bfr)
        dest.writeDouble(c_sfr)
        dest.writeInt(c_uvi)
        dest.writeDouble(c_rom)
        dest.writeDouble(c_bmr)
        dest.writeDouble(c_bm)
        dest.writeDouble(c_vwc)
        dest.writeInt(c_bodyAge)
        dest.writeDouble(c_pp)
        dest.writeInt(c_number)
        dest.writeInt(c_sex)
        dest.writeInt(c_age)
        dest.writeInt(c_height)
        dest.writeInt(c_adc)
        dest.writeDouble(c_proteinMass)
        dest.writeDouble(c_muscleMass)
        dest.writeDouble(c_fatMass)
        dest.writeDouble(c_fatNotWeight)
        dest.writeString(c_obesityStatus)
        dest.writeDouble(c_stdWeight)
        dest.writeDouble(c_weightDiff)
        dest.writeString(c_weightDiffStatus)
    }

    companion object CREATOR : Parcelable.Creator<CalcParam> {
        override fun createFromParcel(parcel: Parcel): CalcParam {
            return CalcParam(parcel)
        }

        override fun newArray(size: Int): Array<CalcParam?> {
            return arrayOfNulls(size)
        }
    }
}