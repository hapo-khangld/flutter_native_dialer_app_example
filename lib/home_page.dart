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

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: Center(
        child: Text('Demo dialer app'),
      ),
    );
  }
}
