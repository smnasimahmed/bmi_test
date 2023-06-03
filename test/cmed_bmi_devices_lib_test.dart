import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:cmed_bmi_devices_lib/cmed_bmi_devices_lib.dart';
import 'package:cmed_bmi_devices_lib/cmed_bmi_devices_lib_platform_interface.dart';
import 'package:cmed_bmi_devices_lib/cmed_bmi_devices_lib_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockCmedBmiDevicesLibPlatform
    with MockPlatformInterfaceMixin
    implements CmedBmiDevicesLibPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<void> connect() async {
    debugPrint("connect");
  }

  @override
  Future<void> disconnect() async {
    debugPrint("disconnect");
  }

  @override
  Stream<dynamic> getStatus() {
    final StreamController<dynamic> controller = StreamController<dynamic>();

    // Mocked data
    final List<dynamic> statuses = ['Online', 'Offline', 'Away', 'Busy'];

    // Emit a random status every second
    Timer.periodic(const Duration(seconds: 1), (timer) {
      final randomIndex = DateTime.now().second % statuses.length;
      controller.add(statuses[randomIndex]);
    });

    return controller.stream;
  }

  @override
  Future<void> measure() async {
    debugPrint("measure");
  }

  @override
  Future<void> setUser(user) async {
    debugPrint("setUser:$user");
  }
}

void main() {
  final CmedBmiDevicesLibPlatform initialPlatform = CmedBmiDevicesLibPlatform.instance;

  test('$MethodChannelCmedBmiDevicesLib is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelCmedBmiDevicesLib>());
  });

  test('getPlatformVersion', () async {
    CmedBmiDevicesLib cmedBmiDevicesLibPlugin = CmedBmiDevicesLib();
    MockCmedBmiDevicesLibPlatform fakePlatform = MockCmedBmiDevicesLibPlatform();
    CmedBmiDevicesLibPlatform.instance = fakePlatform;

    expect(await cmedBmiDevicesLibPlugin.getPlatformVersion(), '42');
  });
}
