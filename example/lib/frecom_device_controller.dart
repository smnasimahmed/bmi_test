import 'dart:convert';
import 'dart:core';

import 'package:cmed_bmi_devices_lib/cmed_bmi_devices_lib.dart';
import 'package:cmed_bmi_devices_lib/cmed_user.dart';
import 'package:get/get.dart';

class FrecomDeviceController extends GetxController {
  final _cmedBmiDevicesLib = CmedBmiDevicesLib();
  final RxString status = "".obs;
  final RxString reading = "".obs;

  connect() async {
    _cmedBmiDevicesLib.getStatus().listen((event) {
      status.value = event.toString();
      if (event.toString().contains("CS_ONLINE_WEIGHT")) {
        reading.value = event.toString().split(":")[1];
      }
    });
    setUser();
    await _cmedBmiDevicesLib.connect();
  }

  setUser() {
    _cmedBmiDevicesLib.setUser(CMEDUser(
        id: 10,
        gender: 'Female' ,
        ageInYear: 9650,
        birthDate: 961851562000,
        heightInCm: 165,
        weightInKg: 67
    ));
  }

  disconnect() {
    _cmedBmiDevicesLib.disconnect();
  }
}
