import 'package:flutter/material.dart';
import 'package:get/get.dart';

import 'frecom_device_controller.dart';

void main() {
  runApp(const CMEDApp());
}

class CMEDApp extends StatelessWidget {
  const CMEDApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        brightness: Brightness.light,
        primarySwatch: Colors.deepPurple,
      ),
      darkTheme: ThemeData.dark(),
      themeMode: ThemeMode.system,
      debugShowCheckedModeBanner: false,
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatelessWidget {
  final _controller = Get.put(FrecomDeviceController());

  @override
  Widget build(context) {
    return Scaffold(
        appBar: AppBar(title: const Text("CMED Frecom BMI Plugin")),
        body: Center(
          child: Center(
              child: Column(
            children: [
              const Spacer(
                flex: 1,
              ),
              Obx(() => Text(
                    '${_controller.reading}',
                    style: const TextStyle(fontSize: 60),
                  )),
              Obx(() => Text('${_controller.status}')),
              const Spacer(
                flex: 1,
              ),
              OutlinedButton(
                  child: const Text('CONNECT'),
                  onPressed: () async {
                    await _controller.connect();
                  }),
              OutlinedButton(
                  child: const Text('SET_USER'),
                  onPressed: () {
                    _controller.setUser();
                  }),
              OutlinedButton(
                  child: const Text('DISCONNECT'),
                  onPressed: () {
                    _controller.disconnect();
                  }),
              const Spacer(
                flex: 1,
              ),
            ],
          )),
        ));
  }
}
