# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
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


# 不使用大小写混合
-dontusemixedcaseclassnames
# 如果应用程序引入的有jar包,并且想混淆jar包里面的class
-dontskipnonpubliclibraryclasses
# 混淆后生产映射文件 map 类名->转化后类名的映射
-verbose
-ignorewarnings

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
# 混淆时不做预校验
-dontpreverify

# If you want to enable optimization, you should include the following:
# 混淆采用的算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
# 设置混淆的压缩比率 0 ~ 7
-optimizationpasses 5
-allowaccessmodification
   #这样再次崩溃的时候就有源文件和行号的信息了
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# Add any project specific keep options here:
-keepattributes *Annotation*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**
-dontwarn java.awt.**,javax.security.**,java.beans.**

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}


# !!!!!!!!!!!!!!!!!!!!!!!!!!!!! 请不要在这之前添加东西！！！！！！！！！！！！！！！！！！！！



# 从这里开始添加我们所希望不被混淆的类
-keep public class com.free.launcher3d.R$*{
    public static final int *;
}
 #如果有其它包有warning，在报出warning的包加入下面类似的-dontwarn 报名
-dontwarn com.fengmap.*.**

-keepclassmembers class ** {
 @com.squareup.otto.Subscribe public *;
 @com.squareup.otto.Produce public *;
}
## 注解支持
-keepclassmembers class *{
   void *(android.view.View);
}
-dontwarn org.apache.http.**
-keep class com.badlogic.**{*;}
-keep enum com.badlogic.** {*;}
-keep class com.esotericsoftware.spine.** {*;}


-keep class aurelienribon.tweenengine.**{*;}

-keep class com.free.launcher3d.bean.**{*;}

-keep class com.free.launcher.iwidget.**{*;}

# rx
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
#glide 图片加载混淆配置
-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.**{ *;}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class  com.free.launcher.wallpaperstore.mxdownload.** { *; }
-keepattributes Signature
-keep class * extends java.lang.annotation.Annotation { *; }
-keep class com.free.launcher3d.api.** { *; }
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep public class com.google.android.gms.ads.** {
   public *;
}

-keep public class com.google.ads.** {
   public *;
}

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
-keep class com.tbruyelle.rxpermissions2.** { *; }
-keep interface com.tbruyelle.rxpermissions2.** { *; }