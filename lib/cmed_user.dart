class CMEDUser {
  int? id;
  String? gender;
  int? ageInDays;
  int? birthDate;
  double? heightInCm;
  double? weightInKg;

  CMEDUser({this.id = 1, this.gender, this.ageInDays, this.birthDate,
    this.heightInCm, this.weightInKg});

  CMEDUser.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    gender = json['gender'];
    ageInDays = json['ageInDays'];
    birthDate = json['birthDate'];
    heightInCm = json['heightInCm'];
    weightInKg = json['weightInKg'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['id'] = id??1;
    data['gender'] = gender??"Male";
    data['ageInDays'] = ageInDays??10585;
    data['birthDate'] = birthDate??0;
    data['heightInCm'] = heightInCm??167.0;
    data['weightInKg'] = weightInKg??60.0;
    return data;
  }

  Map<String, String> toArgument() {
    final Map<String, String> data = {};
    data['id'] = id?.toString()??'1';
    data['gender'] = gender??"Male";
    data['ageInDays'] = ageInDays?.toString()??'10585';
    data['birthDate'] = birthDate?.toString()??'0';
    data['heightInCm'] = heightInCm?.toString()??'167.0';
    data['weightInKg'] = weightInKg?.toString()??'60.0';
    return data;
  }
}
