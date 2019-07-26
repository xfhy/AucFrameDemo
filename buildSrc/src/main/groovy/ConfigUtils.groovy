import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle

class ConfigUtils {

    static addBuildListener(Gradle g) {

        g.addBuildListener(new BuildListener() {
            @Override
            void buildStarted(Gradle gradle) {
                GLogU.d("buildStarted")
            }

            @Override
            void settingsEvaluated(Settings settings) {
                GLogU.d("settingsEvaluated")
                includeModule(settings)
            }

            @Override
            void projectsLoaded(Gradle gradle) {
                GLogU.d("projectsLoaded")
            }

            @Override
            void projectsEvaluated(Gradle gradle) {
                GLogU.d("projectsEvaluated")
            }

            @Override
            void buildFinished(BuildResult buildResult) {
                GLogU.d("buildFinished")
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

}