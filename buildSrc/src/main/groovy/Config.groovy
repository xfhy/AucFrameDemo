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

    //appConfig 配置的是可以跑app的模块,git体积务必只包含launcher
    static appConfig = ['launcher', 'feature0']
    //pkgConfig 配置的是要依赖的功能包,为空则依赖全部,git提交务必为空
    static pkgConfig = ['feature0']

    static depConfig = [

            feature      : [
                    launcher: [
                            app: new DepConfig(":feature:launcher:app")
                    ],
                    feature0: [
                            app   : new DepConfig(":feature:feature0:app"),
                            pkg   : new DepConfig(true, ":feature:feature0:pkg",
                                    "com.xfhy:feature-feature0-pkg:1.0", true),
                            export: new DepConfig(":feature:feature0:export"),
                    ],
                    feature1: [
                            app   : new DepConfig(":feature:feature1:app"),
                            pkg   : new DepConfig(":feature:feature1:pkg"),
                            export: new DepConfig(":feature:feature1:export"),
                    ],
            ],
            lib          : [
                    base  : new DepConfig(":lib:base"),
                    common: new DepConfig(":lib:common"),
            ],

            plugin       : [
                    gradle: "com.android.tools.build:gradle:3.5.0-rc01",
                    kotlin: "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
            ],
            support      : [
                    appcompat_androidx   : new DepConfig("androidx.appcompat:appcompat:$appcompat_androidx_version"),
                    recyclerview_androidx: new DepConfig("androidx.recyclerview:recyclerview:$recyclerview_androidx_version"),
                    design               : new DepConfig("com.google.android.material:material:$design_version"),
                    multidex             : new DepConfig("com.android.support:multidex:$multidex_version"),
                    constraint           : new DepConfig("com.android.support.constraint:constraint-layout:$constraint_version"),
            ],
            kotlin       : new DepConfig("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"),
            utilcode     : new DepConfig("com.blankj:utilcode:1.25.0"),
            free_proguard: new DepConfig("com.blankj:free-proguard:1.0.1"),
            swipe_panel  : new DepConfig("com.blankj:swipe-panel:1.1"),
            leakcanary   : [
                    android         : new DepConfig("com.squareup.leakcanary:leakcanary-android:$leakcanary_version"),
                    android_no_op   : new DepConfig("com.squareup.leakcanary:leakcanary-android-no-op:$leakcanary_version"),
                    support_fragment: new DepConfig("com.squareup.leakcanary:leakcanary-support-fragment:$leakcanary_version"),
            ],
    ]
}