package com.example.mapwize_navigation

import android.content.Intent
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat.startActivity
import com.example.mapwize_navigation.NavigationActivity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.mapwize.mapwizesdk.core.MapwizeConfiguration

/** MapwizeNavigationPlugin */
public class MapwizeNavigationPlugin: FlutterPlugin, MethodCallHandler,ActivityAware {
  private val CHANNEL = "flutter.indoor.navigation";

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    val channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "mapwize_navigation")
    channel.setMethodCallHandler(MapwizeNavigationPlugin());
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "mapwize_navigation")
      channel.setMethodCallHandler(MapwizeNavigationPlugin())
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } 
    else if(call.method.equals("navigate")) {
        displayMap()
    }
    else if(call.method.equals("pointLocation")){
        val long = call.argument<Double>("lat")
        val lat = call.argument<Double>("long")
        val floor = call.argument<Double>("floor")
        pointLocation("5ee8eef0042588001617f5eb",lat,long,floor)
    }
    else {
      result.notImplemented()
    }
  }

  //  override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
  //       GeneratedPluginRegistrant.registerWith(flutterEngine)
  //       MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler(
  //       object : MethodCallHandler {
  //                   override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
  //                       if(call.method.equals("navigate")) {
  //                           displayMap()
  //                       }
  //                       else if(call.method.equals("pointLocation")){
  //                          val long = call.argument<Double>("lat")
  //                          val lat = call.argument<Double>("long")
  //                          val floor = call.argument<Double>("floor")
  //                           pointLocation("5ee8eef0042588001617f5eb",lat,long,floor)
  //                       }
  //                   }
  //               })
  //   }

    private fun displayMap() {
        val config = MapwizeConfiguration.Builder(this, "fd757e7dc6ec8df7c1f9a5b1e9f7e4e0").build()
        MapwizeConfiguration.start(config)
        val intent = Intent..(this, NavigationActivity::class.java)
        startActivity(intent)
    }    

    private fun pointLocation(venueId: String,lat: Double?,long: Double?,floor: Double?)
    {
        val config = MapwizeConfiguration.Builder(this, "fd757e7dc6ec8df7c1f9a5b1e9f7e4e0").build()
        MapwizeConfiguration.start(config)
        val intent = Intent(this, NavigationActivity::class.java)
        intent.putExtra("lat",lat)
        intent.putExtra("long",long)
        intent.putExtra("floor",floor)
        startActivity(intent)
    }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
  }
}
