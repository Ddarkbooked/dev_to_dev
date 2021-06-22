package com.donaldark.dev_to_dev

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import com.devtodev.core.DevToDev
import com.devtodev.core.data.metrics.aggregated.events.CustomEventParams
import com.devtodev.core.utils.log.LogLevel
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** DevToDevPlugin */
class DevToDevPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {

    companion object {
        const val INIT_DATA: String = "initData"
        const val TUTORIAL_COMPLETED: String = "tutorialCompleted"
    }

    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var channel: MethodChannel

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "donaldark.com/devtodev")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        val data = call.arguments

        when (val event = call.method) {
            INIT_DATA -> initData(call.argument("appId"), call.argument("secretKey"))
            TUTORIAL_COMPLETED -> DevToDev.tutorialCompleted(data as Int).also { result.success(null) }.also { eventLogger(event, data) }
            else -> @Suppress("UNCHECKED_CAST") when ((data as HashMap<*, *>).values.first()) {
                "empty" -> DevToDev.customEvent(event).also { result.success(null) }
                is String -> stringEvent(data as HashMap<String, String>, event).also { result.success(null) }.also { eventLogger(event, data) }
                is Double -> doubleEvent(data as HashMap<String, Double>, event).also { result.success(null) }.also { eventLogger(event, data) }
                is Float -> floatEvent(data as HashMap<String, Float>, event).also { result.success(null) }.also { eventLogger(event, data) }
                is Int -> intEvent(data as HashMap<String, Int>, event).also { result.success(null) }.also { eventLogger(event, data) }
                is Long -> longEvent(data as HashMap<String, Long>, event).also { result.success(null) }.also { eventLogger(event, data) }
                is HashMap<*, *> -> multipleParamsEvent((data as HashMap<String, HashMap<String, Any>>).values.first(), event).also { result.success(null) }.also { eventLogger(event, data) }
                else -> result.notImplemented()
            }
        }
    }

    private fun initData(appId: String?, secretKey: String?) {
        DevToDev.init(context, appId, secretKey)
        DevToDev.setLogLevel(LogLevel.Info)
    }

    private fun stringEvent(data: HashMap<String, String>, event: String) {
        val params = CustomEventParams()
        params.putString(data.keys.first(), data.values.first())
        DevToDev.customEvent(event, params)
    }

    private fun doubleEvent(data: HashMap<String, Double>, event: String) {
        val params = CustomEventParams()
        params.putDouble(data.keys.first(), data.values.first())
        DevToDev.customEvent(event, params)
    }

    private fun floatEvent(data: HashMap<String, Float>, event: String) {
        val params = CustomEventParams()
        params.putFloat(data.keys.first(), data.values.first())
        DevToDev.customEvent(event, params)
    }

    private fun intEvent(data: HashMap<String, Int>, event: String) {
        val params = CustomEventParams()
        params.putInteger(data.keys.first(), data.values.first())
        DevToDev.customEvent(event, params)
    }

    private fun longEvent(data: HashMap<String, Long>, event: String) {
        val params = CustomEventParams()
        params.putLong(data.keys.first(), data.values.first())
        DevToDev.customEvent(event, params)
    }

    private fun multipleParamsEvent(data: HashMap<String, Any>, event: String) {
        val params = CustomEventParams()
        data.forEach { (k, v) ->
            when (v) {
                is String -> params.putString(k, v)
                is Double -> params.putDouble(k, v)
                is Float -> params.putFloat(k, v)
                is Int -> params.putInteger(k, v)
                is Long -> params.putLong(k, v)
                else -> throw IllegalArgumentException("Unsupported value type: ${v.javaClass.kotlin}")
            }
        }
        DevToDev.customEvent(event, params)
    }

    private fun eventLogger(eventName: String?, data: Any) = Log.i("DevToDev", "Event '$eventName' Passed Data: $data")

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }

    override fun onDetachedFromActivity() {
    }

}
