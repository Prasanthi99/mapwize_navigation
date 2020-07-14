import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mapwize_navigation/mapwize_navigation.dart';

void main() {
  const MethodChannel channel = MethodChannel('mapwize_navigation');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await MapwizeNavigation.platformVersion, '42');
  });
}
