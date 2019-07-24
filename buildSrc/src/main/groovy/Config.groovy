class Config {
    static applicationId = 'com.xfhy.aucframe'
    static appName = 'AucFrame'

    static compileSdkVersion = 28
    static buildToolsVersion = '28.0.3'
    static minSdkVersion = 21
    static targetSdkVersion = 28
    static versionCode = 1_000_000
    static versionName = '1.0.0'   //eg: 1.9.72 => 1,009,072

    static kotlin_version = '1.3.41'
    static appcompat_androidx_version = '1.0.2'
    static recyclerview_androidx_version = '1.0.0'
    static leakcanary_version = '1.6.3'
    static design_version = '1.0.0'
    static multidex_version = '1.0.2'
    static constraint_version = '1.1.3'

    static depConfig = [
            plugin       : [
                    gradle: "com.android.tools.build:gradle:3.5.0-rc01",
                    kotlin: "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
            ],
            support      : [
                    appcompat_androidx   : "androidx.appcompat:appcompat:$appcompat_androidx_version",
                    recyclerview_androidx: "androidx.recyclerview:recyclerview:$recyclerview_androidx_version",
                    design               : "com.google.android.material:material:$design_version",
                    multidex             : "com.android.support:multidex:$multidex_version",
                    constraint           : "com.android.support.constraint:constraint-layout:$constraint_version",
            ],
            kotlin       : "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version",
            utilcode     : "com.blankj:utilcode:1.25.0",
            free_proguard: "com.blankj:free-proguard:1.0.1",
            swipe_panel  : "com.blankj:swipe-panel:1.1",
            leakcanary   : [
                    android         : "com.squareup.leakcanary:leakcanary-android:$leakcanary_version",
                    android_no_op   : "com.squareup.leakcanary:leakcanary-android-no-op:$leakcanary_version",
                    support_fragment: "com.squareup.leakcanary:leakcanary-support-fragment:$leakcanary_version",
            ],
    ]
}