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
            save();
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

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            JsonObject json = new JsonObject();
            json.addProperty("Exadus", exadusEnabled);
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(json, writer);
            }
        } catch (IOException ignored) {
        }
    }

    public static boolean isExadusEnabled() {
        return exadusEnabled;
    }

    public static void setExadusEnabled(boolean enabled) {
        exadusEnabled = enabled;
        save();
    }

    public static boolean toggle() {
        setExadusEnabled(!exadusEnabled);
        return exadusEnabled;
    }
}
