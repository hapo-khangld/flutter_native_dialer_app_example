import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  static const platform = MethodChannel('example.com/native_channel');
  @override
  void initState() {
    super.initState();
    requestDialerApp();
  }

  Future<bool> requestDialerApp() async {
    try {
      final bool nativeVariable = await platform.invokeMethod('requestDialerRole');
      return nativeVariable;
    } on PlatformException catch (e) {
      print("Lỗi: ${e.message}");
      return false;
    }
  }

  Future<void> addPhoneNumberToBlocked() async {
    try {
      await platform.invokeMethod('addPhoneNumberToBlocked');
    } on PlatformException catch (e) {
      print("Lỗi: ${e.message}");
    }
  }

  Future<bool> actionCallPhoneNumber() async {
    try {
      final bool result = await platform.invokeMethod('actionCallToNumber', {'phoneNumber': '06243522446'});
      return result;
    } on PlatformException catch (e) {
      print("Lỗi: ${e.message}");
      return false;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          children: [
            InkWell(
              onTap: () => addPhoneNumberToBlocked(),
              child: const Text('Demo dialer app'),
            ),
            InkWell(
              onTap: () => actionCallPhoneNumber(),
              child: const Text('Test action call'),
            ),
          ],
        ),
      ),
    );
  }
}
