class CMEDUser {
  int? id;
  String? gender;
  int? ageInDays;
  int? birthDate;
  double? heightInCm;
  double? weightInKg;

  CMEDUser(this.id, this.gender, this.ageInDays, this.birthDate,
      this.heightInCm, this.weightInKg);

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
    data['id'] = id;
    data['gender'] = gender;
    data['ageInDays'] = ageInDays;
    data['birthDate'] = birthDate;
    data['heightInCm'] = heightInCm;
    data['weightInKg'] = weightInKg;
    return data;
  }
}
