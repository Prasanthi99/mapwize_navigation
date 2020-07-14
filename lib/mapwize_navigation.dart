import 'dart:async';

import 'package:flutter/services.dart';

class MapwizeNavigation {
  static const MethodChannel _channel =
      const MethodChannel('mapwize_navigation');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
