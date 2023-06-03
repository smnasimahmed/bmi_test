
import 'cmed_bmi_devices_lib_platform_interface.dart';

class CmedBmiDevicesLib {
  Future<String?> getPlatformVersion() {
    return CmedBmiDevicesLibPlatform.instance.getPlatformVersion();
  }
  Future<void> setUser(user) {
    return CmedBmiDevicesLibPlatform.instance.setUser(user);
  }
  Future<void> connect() {
    return CmedBmiDevicesLibPlatform.instance.connect();
  }
  Future<void> disconnect() {
    return CmedBmiDevicesLibPlatform.instance.disconnect();
  }
  Stream<dynamic> getStatus() {
    return CmedBmiDevicesLibPlatform.instance.getStatus();
  }
}
