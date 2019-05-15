package com.cm.android.doubleclick.plugin

import java.nio.file.Path;

class Utils {
    static Path toOutputPath(Path outputRoot, Path inputRoot, Path inputPath) {
        return outputRoot.resolve(inputRoot.relativize(inputPath))
    }
}
