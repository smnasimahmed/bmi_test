import 'dart:core';

import 'package:cmed_bmi_devices_lib/cmed_bmi_devices_lib.dart';
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
    await _cmedBmiDevicesLib.connect();
  }

  setUser() {
    // _cmedBmiDevicesLib.setUser(CMEDUser());
  }

  disconnect() {
    _cmedBmiDevicesLib.disconnect();
  }
}
