library dev_to_dev;

import 'dart:async';
import 'package:flutter/services.dart';

export 'src/dev_to_dev_src.dart';


Future<void> methodInvoker(MethodChannel channel, String eventName, [dynamic params]) async {
  try {
    await channel.invokeMethod<void>(eventName, params ?? {'data': 'empty'});
  } on PlatformException catch (e) {
    print(e.message);
  }
}
