package com.exadus.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ExadusConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("exadus.json");
    private static boolean exadusEnabled = false;

    private ExadusConfig() {}

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            saveDefault();
            return;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            if (json != null && json.has("Exadus") && json.get("Exadus").isJsonPrimitive()) {
                exadusEnabled = json.get("Exadus").getAsBoolean();
            } else {
                exadusEnabled = false;
            }
        } catch (Exception e) {
            exadusEnabled = false;
        }
    }

    private static void saveDefault() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            JsonObject defaultJson = new JsonObject();
            defaultJson.addProperty("Exadus", false);
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(defaultJson, writer);
            }
        } catch (IOException ignored) {
        }
    }

    public static boolean isExadusEnabled() {
        return exadusEnabled;
    }
}
