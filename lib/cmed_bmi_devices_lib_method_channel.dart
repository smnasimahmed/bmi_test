import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'cmed_bmi_devices_lib_platform_interface.dart';

/// An implementation of [CmedBmiDevicesLibPlatform] that uses method channels.
class MethodChannelCmedBmiDevicesLib extends CmedBmiDevicesLibPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('cmed_bmi_devices_lib/method');

  /// The event channel used to interact with the native platform.
  @visibleForTesting
  final eventChannel = const EventChannel('cmed_bmi_devices_lib/event');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Stream<dynamic> getStatus() {
    return eventChannel.receiveBroadcastStream();
  }

  @override
  Future<void> disconnect() async {
    await methodChannel.invokeMethod<String>('disconnect');
  }

  @override
  Future<void> connect() async {
    await methodChannel.invokeMethod<String>('connect');
  }

  @override
  Future<void> setUser(user) async {
    await methodChannel.invokeMethod<bool>('setUser', {'user': user.toJson()});
  }
}
