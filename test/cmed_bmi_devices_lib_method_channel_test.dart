import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:cmed_bmi_devices_lib/cmed_bmi_devices_lib_method_channel.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  MethodChannelCmedBmiDevicesLib platform = MethodChannelCmedBmiDevicesLib();
  const MethodChannel channel = MethodChannel('cmed_bmi_devices_lib');

  setUp(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(
      channel,
      (MethodCall methodCall) async {
        return '42';
      },
    );
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(channel, null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
