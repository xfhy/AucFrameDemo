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
        //引入module  放到了这里
        settings.include ':lib:base', ':lib:common',
                ':feature:feature0:export', ':feature:feature1:export',
                ':feature:feature0:pkg', ':feature:feature1:pkg',
                ':feature:feature0:app', ':feature:feature1:app',
                ':feature:launcher:app'
    }

    private static projectsLoadedToDoSome(Gradle gradle) {
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

    interface DepConfigFilter {
        boolean accept(String name, DepConfig config)
    }

}