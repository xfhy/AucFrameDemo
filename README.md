## AucFrameDemo
学习新的组件方式

架构如下

![](https://s2.ax1x.com/2019/07/25/eeaGSx.png)


1. buildSrc组件,主要是为其他组件提供一些配置信息,像编译版本+三方库版本的等等
2. 将Library的build.gradle中的通用配置全部提取出来,放入buildLib.gradle. Library只需要引入即可.同理还有buildApp.gradle
3. lib作为最基础的组件,里面存储公共基础库
4. feature中存放各个业务组件,其中featureX就是代表的某个业务组件,它里面有单独的app模块(负责运行这个模块,单独调试,单独运行,比较方便),还有pkg:这个就是该featureX的核心业务代码了,
export可能是拿来模块间通信的.
5. feature下面的Launcher是主app,是用来组装整个项目的,这时需要在该app模块引入所有模块的pkg(即核心业务代码).这时一个组件化项目,初见雏型.
6. 解放setting: 监听生命周期,在settingsEvaluated(这个方法会在执行setting时执行)中include模块
7.