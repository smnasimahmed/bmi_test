import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'cmed_bmi_devices_lib_method_channel.dart';

abstract class CmedBmiDevicesLibPlatform extends PlatformInterface {
  /// Constructs a CmedBmiDevicesLibPlatform.
  CmedBmiDevicesLibPlatform() : super(token: _token);

  static final Object _token = Object();

  static CmedBmiDevicesLibPlatform _instance = MethodChannelCmedBmiDevicesLib();

  /// The default instance of [CmedBmiDevicesLibPlatform] to use.
  ///
  /// Defaults to [MethodChannelCmedBmiDevicesLib].
  static CmedBmiDevicesLibPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [CmedBmiDevicesLibPlatform] when
  /// they register themselves.
  static set instance(CmedBmiDevicesLibPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<void> setUser(user) {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<void> connect() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<void> disconnect() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Stream<dynamic> getStatus() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
