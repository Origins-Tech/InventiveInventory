package net.origins.inventive_inventory.config;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.commands.config.type.ConfigType;
import net.origins.inventive_inventory.config.enums.Status;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingModes;
import net.origins.inventive_inventory.config.enums.automatic_refilling.ToolReplacementBehaviour;
import net.origins.inventive_inventory.config.enums.automatic_refilling.ToolReplacementPriority;
import net.origins.inventive_inventory.config.enums.sorting.CursorStackBehaviour;
import net.origins.inventive_inventory.config.enums.sorting.SortingModes;
import net.origins.inventive_inventory.config.options.AdvancedConfigOption;
import net.origins.inventive_inventory.config.options.ConfigOption;
import net.origins.inventive_inventory.config.options.SimpleConfigOption;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.origins.inventive_inventory.features.profiles.ProfileHandler;
import net.origins.inventive_inventory.util.FileHandler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class ConfigManager {
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(InventiveInventory.MOD_ID);
    public static final ConfigOption<Status> SORTING = new AdvancedConfigOption<>("sorting", "status", Status.ENABLED, ConfigType.SORTING);
    public static final ConfigOption<SortingModes> SORTING_MODE = new AdvancedConfigOption<>("sorting.mode", SortingModes.NAME, ConfigType.SORTING);
    public static final ConfigOption<CursorStackBehaviour> CURSOR_STACK_BEHAVIOUR = new AdvancedConfigOption<>("sorting.cursor_stack_behaviour", CursorStackBehaviour.AOK_DEPENDENT, ConfigType.SORTING);
    public static final ConfigOption<Status> AUTOMATIC_REFILLING = new AdvancedConfigOption<>("automatic_refilling", "status", Status.ENABLED, ConfigType.AUTOMATIC_REFILLING);
    public static final ConfigOption<AutomaticRefillingModes> AUTOMATIC_REFILLING_MODE = new AdvancedConfigOption<>("automatic_refilling.mode", AutomaticRefillingModes.AUTOMATIC, ConfigType.AUTOMATIC_REFILLING);
    public static final ConfigOption<ToolReplacementBehaviour> TOOL_REPLACEMENT_BEHAVIOUR = new AdvancedConfigOption<>("automatic_refilling.tool_replacement_behaviour", ToolReplacementBehaviour.BREAK_TOOL, ConfigType.AUTOMATIC_REFILLING);
    public static final ConfigOption<ToolReplacementPriority> TOOL_REPLACEMENT_PRIORITY = new AdvancedConfigOption<>("automatic_refilling.tool_replacement_priority", ToolReplacementPriority.MATERIAL, ConfigType.AUTOMATIC_REFILLING);
    public static final ConfigOption<Boolean> AUTOMATIC_REFILLING_IGNORE_LOCKED_SLOTS = new SimpleConfigOption("ignore_locked_slots", true, ConfigType.AUTOMATIC_REFILLING);
    public static final ConfigOption<Status> PROFILES = new AdvancedConfigOption<>("profiles", "status", Status.ENABLED, ConfigType.PROFILES);
    public static final ConfigOption<Boolean> FAST_LOAD = new SimpleConfigOption("profiles.fast_load", true, ConfigType.PROFILES);
    public static final ConfigOption<Boolean> PROFILES_IGNORE_LOCKED_SLOTS = new SimpleConfigOption("ignore_locked_slots", true, ConfigType.PROFILES);

    private static final String CONFIG_FILE = "config.json";
    private static final Path CONFIG_FILE_PATH = CONFIG_PATH.resolve(CONFIG_FILE);

    public static void init() throws IOException {
        deleteOldConfigs();
        Files.createDirectories(CONFIG_PATH);
        FileHandler.createFile(CONFIG_FILE_PATH);
        FileHandler.createFile(LockedSlotsHandler.LOCKED_SLOTS_PATH);
        FileHandler.createFile(ProfileHandler.PROFILES_PATH);
        initConfig();
        save();
    }

    public static void save() {
        try {
            JsonObject config = new JsonObject();
            for (Field field : ConfigManager.class.getDeclaredFields()) {
                if (ConfigOption.class.isAssignableFrom(field.getType())) {
                    ConfigOption<?> option = (ConfigOption<?>) field.get(null);
                    config.addProperty(field.getName().toLowerCase(), option.getValue().toString().toLowerCase());
                }
            }
            FileHandler.write(CONFIG_FILE_PATH, config);
        } catch (IllegalAccessException ex) {
            for (StackTraceElement traceElement : ex.getStackTrace()) {
                InventiveInventory.LOGGER.error(traceElement.toString());
            }
        }

    }

    private static void initConfig() {
        try {
            JsonObject config = FileHandler.get(CONFIG_FILE_PATH);
            for (Field field : ConfigManager.class.getDeclaredFields()) {
                String value = null;
                if (ConfigOption.class.isAssignableFrom(field.getType())) {
                    ConfigOption<?> option = (ConfigOption<?>) field.get(null);
                    if (config.has(field.getName().toLowerCase())) {
                        value = config.get(field.getName().toLowerCase()).getAsString();
                    }
                    option.setValue(value);
                }
            }
        } catch (IllegalAccessException ex) {
            for (StackTraceElement traceElement : ex.getStackTrace()) {
                InventiveInventory.LOGGER.error(traceElement.toString());
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void deleteOldConfigs() throws IOException {
        if (Files.exists(CONFIG_PATH.resolve("settings.properties"))) {
            try (Stream<Path> paths = Files.walk(CONFIG_PATH)) {
                paths.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }
    }
}
