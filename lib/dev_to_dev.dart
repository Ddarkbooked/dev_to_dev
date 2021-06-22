import 'dart:async';
import 'package:flutter/services.dart';

const _channelName = 'donaldark.com/devtodev';
const _initData = 'initData';
const _tutorialCompleted = 'tutorialCompleted';

class DevToDev {
  final MethodChannel _channel = MethodChannel(_channelName);

  DevToDev({required String appId, required String secretKey}) {
    _channel.invokeMethod<void>(_initData, {'appId': appId, 'secretKey': secretKey});
  }

  /// Custom event with params.
  /// The event must have a unique name and can include up to `20` parameters.
  ///
  /// [eventName] - event name.
  /// [params] - event parameters.
  ///
  /// If [params] is empty - send simple custom event. [params] value supports
  /// five types: `double`, `float`, `int`, `long` and `string`.
  Future<void> customEvent(String eventName, [Map<String, dynamic>? params]) => _methodInvoker(eventName, params);

  /// The event allowing to track the stage of tutorial a player is on.
  ///
  /// [tutorialStep] - the latest successfully completed tutorial step.
  Future<void> tutorialCompleted(int tutorialStep) => _methodInvoker(_tutorialCompleted, tutorialStep);

  Future<void> _methodInvoker(String eventName, [dynamic params]) async {
    try {
      await _channel.invokeMethod<void>(eventName, params ?? {'data': 'empty'});
    } on PlatformException catch (e) {
      print(e.message);
    }
  }
}
