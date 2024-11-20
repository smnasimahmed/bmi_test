class CMEDUser {
  int? id;
  String? gender;
  int? ageInYears;
  int? birthDate;
  double? heightInCm;
  double? weightInKg;

  CMEDUser({this.id = 1, this.gender, this.ageInYears, this.birthDate,
    this.heightInCm, this.weightInKg});

  CMEDUser.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    gender = json['gender'];
    ageInYears = json['ageInYears'];
    birthDate = json['birthDate'];
    heightInCm = json['heightInCm'];
    weightInKg = json['weightInKg'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['id'] = id??1;
    data['gender'] = gender??"Male";
    data['ageInYears'] = ageInYears??18;
    data['birthDate'] = birthDate??0;
    data['heightInCm'] = heightInCm??167.0;
    data['weightInKg'] = weightInKg??60.0;
    return data;
  }
}
