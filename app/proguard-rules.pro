# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in d:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keepattributes EnclosingMethod
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose # 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/* # 混淆时所采用的算法
-keep public class * extends android.app.Activity # 保持哪些类不被混淆
-keep public class * extends android.app.Application # 保持哪些类不被混淆
-keep public class * extends android.app.Service # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService # 保持哪些类不被混淆
-keepclasseswithmembernames class * { # 保持 native 方法不被混淆
   native <methods>;
}
-keepclasseswithmembers class * { # 保持自定义控件类不被混淆
   public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
   public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
   public void *(android.view.View);
}
-keepclassmembers enum * { # 保持枚举 enum 类不被混淆
   public static **[] values();
   public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {#保持Parcelable不被混淆
   public static final android.os.Parcelable$Creator *;
}
# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keep public class * implements java.io.Serializable {*;}
-keepclassmembers class * implements java.io.Serializable {
   static final long serialVersionUID;
   private static final java.io.ObjectStreamField[]   serialPersistentFields;
   private void writeObject(java.io.ObjectOutputStream);
   private void readObject(java.io.ObjectInputStream);
   java.lang.Object writeReplace();
   java.lang.Object readResolve();
}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
#com.demo.demo是你的包名
-keep public class com.quootta.mdate.R$*{
   public static final int *;
}
#OK3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**


# glide
 -keep public class * implements com.bumptech.glide.module.GlideModule
 -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
   **[] $VALUES;
   public *;
 }

 # Gson specific classes
 -keep class sun.misc.Unsafe { *; }
 -keep class com.google.gson.stream.** { *; }
 # Application classes that will be serialized/deserialized over Gson 下面替换成自己的实体类
 -keep class com.otcpesa.bean.** { *; }


# event
 -keepattributes *Annotation*
 -keepclassmembers class ** {
     @de.greenrobot.event.Subscribe <methods>;
 }
 -keep enum de.greenrobot.event.ThreadMode { *; }
 # Only required if you use AsyncExecutor
 -keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
     <init>(Java.lang.Throwable);
 }

 -keepclassmembers class ** {
     public void onEvent*(**);
 }

-keepattributes Exceptions,InnerClasses

-keepattributes Signature

# RongCloud SDK
-keep class io.rong.** {*;}
-keep class * implements io.rong.imlib.model.MessageContent {*;}
-dontwarn io.rong.push.**
-dontnote com.xiaomi.**
-dontnote com.google.android.gms.gcm.**
-dontnote io.rong.**

# VoIP
-keep class io.agora.rtc.** {*;}

# Location
-keep class com.amap.api.**{*;}
-keep class com.amap.api.services.**{*;}

# 红包
-keep class com.google.gson.** { *; }
-keep class com.uuhelper.Application.** {*;}
-keep class net.sourceforge.zbar.** { *; }
-keep class com.google.android.gms.** { *; }
-keep class com.alipay.** {*;}
-keep class com.jrmf360.rylib.** {*;}

-ignorewarnings
-keep class com.quootta.mdate.receiver.RongPushReceiver {*;}
 -keep class cn.rongcloud.rtc.core.**  { *; }
 -keep class cn.rongcloud.rtc.engine.binstack.json.**  { *; }

 -dontoptimize
 -dontpreverify

 -dontwarn cn.jpush.**
 -keep class cn.jpush.** { *; }
 -keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

 -dontwarn cn.jiguang.**
 -keep class cn.jiguang.** { *; }
 #==================gson && protobuf==========================
 -dontwarn com.google.**
 -keep class com.google.gson.** {*;}
 -keep class com.google.protobuf.** {*;}

 #permissiongen.jar
 -dontwarn kr.co.namee.permissiongen.**
 -keep class kr.co.namee.permissiongen.**{*;}

 -keep @kr.co.namee.permissiongen.PermissionFail class * {*;}
 -keep @kr.co.namee.permissiongen.PermissionSuccess class * {*;}
 -keep class * {
     @kr.co.namee.permissiongen.PermissionFail <fields>;
     @kr.co.namee.permissiongen.PermissionSuccess <fields>;
 }
 -keepclassmembers class * {
     @kr.co.namee.permissiongen.PermissionFail <methods>;
     @kr.co.namee.permissiongen.PermissionSuccess <methods>;
 }

 ##     volley混淆
 ## -------------------------------------------
 -keep class com.android.volley.** {*;}
 -keep class com.android.volley.toolbox.** {*;}
 -keep class com.android.volley.Response$* { *; }
 -keep class com.android.volley.Request$* { *; }
 -keep class com.android.volley.RequestQueue$* { *; }
 -keep class com.android.volley.toolbox.HurlStack$* { *; }
 -keep class com.android.volley.toolbox.ImageLoader$* { *; }

 ## ----------------------------------
 ##     butterknife 相关的混淆配置
 ## ----------------------------------
 -keep class butterknife.** { *; }
 -dontwarn butterknife.internal.**
 -keep class **$$ViewBinder { *; }

 -keepclasseswithmembernames class * {
     @butterknife.* <fields>;
 }

 -keepclasseswithmembernames class * {
     @butterknife.* <methods>;
 }


 -keep class com.pili.pldroid.player.** { *; }
 -keep class com.qiniu.qplayer.mediaEngine.MediaPlayer{*;}

 -dontwarn com.pingplusplus.**
 -keep class com.pingplusplus.** {*;}

#-keep class com.alipayzhima.**{*;}
#-keep class com.android.moblie.zmxy.antgroup.creditsdk.**{*;}
#-keep class com.antgroup.zmxy.mobile.android.container.**{*;}
#-keep class org.json.alipayzhima.**{*;}