/**
 * 用于记录各个库的属性
 */
class DepConfig {

    /**
     * 是否使用本地的
     */
    boolean useLocal
    /**
     * 本地路径
     */
    String localPath
    /**
     * 远程路径
     */
    String remotePath
    /**
     * 是否应用
     */
    boolean isApply
    /**
     * 最后的路径
     */
    String path
    /**
     * 根据条件生成项目最终的依赖项
     */
    def dep

    DepConfig(String path) {
        this(path, true)
    }

    DepConfig(String path, boolean isApply) {
        if (path.startsWith(":")) {
            this.useLocal = true
            this.localPath = path
        } else {
            this.useLocal = false
            this.remotePath = path
        }
        this.isApply = isApply
        this.path = path
    }

    DepConfig(boolean useLocal, String localPath, String remotePath, boolean isApply) {
        this.useLocal = useLocal
        this.localPath = localPath
        this.remotePath = remotePath
        this.isApply = isApply
        this.path = useLocal ? localPath : remotePath
    }


    @Override
    String toString() {
        return "DepConfig { " +
                "useLocal = " + useLocal +
                (dep == null ? ", path = " + path : (", dep = " + dep)) +
                ", isApply = " + isApply +
                " }"
    }
}