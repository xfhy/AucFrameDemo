import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle

class ConfigUtils {

    static addBuildListener(Gradle g) {

        g.addBuildListener(new BuildListener() {
            @Override
            void buildStarted(Gradle gradle) {
                GLog.d("buildStarted")
            }

            @Override
            void settingsEvaluated(Settings settings) {
                GLog.d("settingsEvaluated")
                includeModule(settings)
            }

            @Override
            void projectsLoaded(Gradle gradle) {
                //加载module时会被调用
                GLog.d("projectsLoaded")
                projectsLoadedToDoSome(gradle)
            }

            @Override
            void projectsEvaluated(Gradle gradle) {
                GLog.d("projectsEvaluated")
            }

            @Override
            void buildFinished(BuildResult buildResult) {
                GLog.d("buildFinished")
            }
        })

    }

    /**
     * 解放setting.gradle
     */
    private static includeModule(Settings settings) {
        /*//引入module  放到了这里    古老的方式
        settings.include ':lib:base', ':lib:common',
                ':feature:feature0:export', ':feature:feature1:export',
                ':feature:feature0:pkg', ':feature:feature1:pkg',
                ':feature:feature0:app', ':feature:feature1:app',
                ':feature:launcher:app'*/

        def configResult = getDepConfigByFilter(new DepConfigFilter() {
            @Override
            boolean accept(String name, DepConfig config) {
                //如果最终是app的话
                if (name.endsWith('.app')) {
                    //获取app模块的名字
                    def appName = name.substring('feature.'.length(), name.length() - 4)
                    //如果没有配置该app,那就不依赖它
                    if (!Config.appConfig.contains(appName)) {
                        config.isApply = false
                    }
                }

                //如果Config.pkgConfig不为空,说明是pkg调试模式   不是最终的打所有的依赖
                if (!Config.pkgConfig.isEmpty()) {
                    if (name.endsWith('.pkg')) {
                        //获取pkg模块名字
                        def pkgName = name.substring('feature.'.length(), name.length() - 4)
                        //如果Config.pkgConfig中不存在,那就不让它进依赖
                        if (!Config.pkgConfig.contains(pkgName)) {
                            config.isApply = false
                        }
                    }
                }

                //过滤出本地并且apply的模块
                if (!config.isApply) {
                    return false
                }
                if (!config.useLocal) {
                    return false
                }
                if (config.localPath == "") {
                    return false
                }
                return true
            }
        }).each { _, cfg ->
            //依赖到include   当然是用localPath
            settings.include cfg.localPath
        }

        GLog.l("includeModule = ${GLog.object2String(configResult)}")
    }

    private static projectsLoadedToDoSome(Gradle gradle) {
        generateDep(gradle)
        gradle.addProjectEvaluationListener(new ProjectEvaluationListener() {
            @Override
            void beforeEvaluate(Project project) {
                //执行每个module的build.gradle之前会被调用
                GLog.d("beforeEvaluate")

                //每个module加载之前就去apply自己的plugin,app就是对应buildApp.gradle  而其他的就是buildLib.gradle
                //定位到具体project,这里是没有子module
                if (project.subprojects.isEmpty()) {
                    if (project.name == "app") {
                        GLog.l(project.toString() + " applies buildApp.gradle")
                        project.apply {
                            from "${project.rootDir.path}/buildApp.gradle"
                        }
                    } else {
                        GLog.l(project.toString() + " applies buildLib.gradle")
                        project.apply {
                            from "${project.rootDir.path}/buildLib.gradle"
                        }
                    }
                }
            }

            @Override
            void afterEvaluate(Project project, ProjectState projectState) {
                //执行每个module的build.gradle之后会被调用
                GLog.d("afterEvaluate")
            }
        })
    }

    /**
     * 根据条件生成最终的依赖项
     */
    private static generateDep(Gradle gradle) {
        def configResult = getDepConfigByFilter(new DepConfigFilter() {
            @Override
            boolean accept(String name, DepConfig config) {
                //如果使用的是本地模块,那么把它转化为project
                if (config.useLocal) {
                    //根据条件生成最终的依赖项=rootProject去找本地路径
                    config.dep = gradle.rootProject.findProject(config.localPath)
                } else {
                    //如果是远端依赖,那就直接使用远端依赖即可
                    config.dep = config.remotePath
                }
                return false
            }
        })

        GLog.l("generateDep = ${GLog.object2String(configResult)}")
    }

    /**
     * 遍历Config中的depConfig配置,获取所有的DepConfig(可能是module的,也可能是三方库的)
     * @param filter 过滤器
     * @return 符合条件的
     */
    static Map<String, DepConfig> getDepConfigByFilter(DepConfigFilter filter) {
        return _getDepConfigByFilter("", Config.depConfig, filter)
    }

    private static _getDepConfigByFilter(String namePrefix, Map map, DepConfigFilter filter) {
        //结果Map
        def depConfigList = [:]
        for (Map.Entry entry : map.entrySet()) {
            def (name, value) = [entry.key, entry.value]
            //如果值是Map类型就加到结果Map中    Config.depConfig.feature.feature0.app
            //不断深入,直到遍历到DepConfig
            if (value instanceof Map) {
                namePrefix += (name + ".")
                depConfigList.putAll(_getDepConfigByFilter(namePrefix, value, filter))
                namePrefix -= (name + ".")
                continue
            }

            //最里面的时候,只能是DepConfig
            //如果符合过滤条件,就加到结果Map中
            if (value instanceof DepConfig) {
                def config = value as DepConfig
                def result = namePrefix + name
                if (filter == null || filter.accept(result, config)) {
                    depConfigList.put(result, config)
                }
            }
        }
        return depConfigList
    }

    /**
     * 获取可用的pkg
     */
    static getApplyPkgs() {
        def applyPkgs = getDepConfigByFilter(new DepConfigFilter() {
            @Override
            boolean accept(String name, DepConfig config) {
                if (!config.isApply) return false
                return name.endsWith(".pkg")
            }
        })
        GLog.d("getApplyPkgs = ${GLog.object2String(applyPkgs)}")
        return applyPkgs
    }

    static getApplyExports() {
        def applyExports = getDepConfigByFilter(new DepConfigFilter() {
            @Override
            boolean accept(String name, DepConfig config) {
                if (!config.isApply) {
                    return false
                }
                return name.endsWith(".export")
            }
        })
        GLog.d("getApplyExports = ${GLog.object2String(applyExports)}")
        return applyExports
    }

    interface DepConfigFilter {
        /**
         * 筛选  是否符合条件
         * @param name module或者是三方库 在Config中的全称
         * @param config 它的配置
         * @return true:符合
         */
        boolean accept(String name, DepConfig config)
    }

}