package com.github.rubenqba.databursatil;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceUtil {

    public static String loadJsonFile(String filename) {
        try {
            return Files.readString(Path.of("src/test/resources", filename));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
