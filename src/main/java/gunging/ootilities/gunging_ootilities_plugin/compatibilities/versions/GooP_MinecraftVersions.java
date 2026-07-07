package gunging.ootilities.gunging_ootilities_plugin.compatibilities.versions;

import gunging.ootilities.gunging_ootilities_plugin.Gunging_Ootilities_Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class GooP_MinecraftVersions {

    public static double mcVersion = -1.0;

    /**
     * It is assumed that this is not minecraft 2.0+ ffs
     * Returns the minecraft version ignoring that first 1.
     *
     * (e.g. Minecraft 1.15.2 returns 15.2)
     * @return (e.g.) Minecraft 1.13.2 returns 13.2
     */
    public static double GetMinecraftVersion() {
        // Gather if hasnt been gathered
        if (mcVersion < 0) {
            String vers = Bukkit.getVersion();

            if (vers.contains("1.13")) {
                mcVersion = 13.0;
                if (vers.contains("1.13.1")) { mcVersion = 13.1; }
                if (vers.contains("1.13.2")) { mcVersion = 13.2; }

            } else if (vers.contains("1.14")) {
                mcVersion = 14.0;
                if (vers.contains("1.14.1")) { mcVersion = 14.1; }
                if (vers.contains("1.14.2")) { mcVersion = 14.2; }
                if (vers.contains("1.14.3")) { mcVersion = 14.3; }
                if (vers.contains("1.14.4")) { mcVersion = 14.4; }

            } else if (vers.contains("1.15")) {
                mcVersion = 15.0;
                if (vers.contains("1.15.1")) { mcVersion = 15.1; }
                if (vers.contains("1.15.2")) { mcVersion = 15.2; }

            } else if (vers.contains("1.16")) {
                mcVersion = 16.0;
                if (vers.contains("1.16.1")) { mcVersion = 16.1; }
                if (vers.contains("1.16.2")) { mcVersion = 16.2; }
                if (vers.contains("1.16.3")) { mcVersion = 16.3; }
                if (vers.contains("1.16.4")) { mcVersion = 16.4; }
                if (vers.contains("1.16.5")) { mcVersion = 16.5; }

            } else if (vers.contains("1.17")) {
                mcVersion = 17.0;
                if (vers.contains("1.17.1")) { mcVersion = 17.1; }

            } else if (vers.contains("1.18")) {
                mcVersion = 18.0;
                if (vers.contains("1.18.1")) { mcVersion = 18.1; }
                if (vers.contains("1.18.2")) { mcVersion = 18.2; }

            } else if (vers.contains("1.19")) {
                mcVersion = 19.0;
                if (vers.contains("1.19.1")) { mcVersion = 19.1; }
                if (vers.contains("1.19.2")) { mcVersion = 19.2; }
                if (vers.contains("1.19.3")) { mcVersion = 19.3; }
                if (vers.contains("1.19.4")) { mcVersion = 19.4; }

            } else if (vers.contains("1.20")) {
                mcVersion = 20.0;
                if (vers.contains("1.20.1")) { mcVersion = 20.1; }
                if (vers.contains("1.20.2")) { mcVersion = 20.2; }
                if (vers.contains("1.20.3")) { mcVersion = 20.3; }
                if (vers.contains("1.20.4")) { mcVersion = 20.4; }
                if (vers.contains("1.20.5")) { mcVersion = 20.5; }
                if (vers.contains("1.20.6")) { mcVersion = 20.6; }

            } else if (vers.contains("1.21")) {
                mcVersion = 21.0;
                if (vers.contains("1.21.1")) { mcVersion = 21.1; }
                if (vers.contains("1.21.2")) { mcVersion = 21.2; }
                if (vers.contains("1.21.3")) { mcVersion = 21.3; }
                if (vers.contains("1.21.4")) { mcVersion = 21.4; }
                if (vers.contains("1.21.5")) { mcVersion = 21.5; }
                if (vers.contains("1.21.6")) { mcVersion = 21.6; }

            } else if (vers.contains("1.22")) {
                mcVersion = 22.0;
                if (vers.contains("1.22.1")) { mcVersion = 22.1; }
                if (vers.contains("1.22.2")) { mcVersion = 22.2; }
                if (vers.contains("1.22.3")) { mcVersion = 22.3; }
                if (vers.contains("1.22.4")) { mcVersion = 22.4; }
                if (vers.contains("1.22.5")) { mcVersion = 22.5; }
                if (vers.contains("1.22.6")) { mcVersion = 22.6; }

            } else if (vers.contains("1.23")) {
                mcVersion = 23.0;
                if (vers.contains("1.23.1")) { mcVersion = 23.1; }
                if (vers.contains("1.23.2")) { mcVersion = 23.2; }
                if (vers.contains("1.23.3")) { mcVersion = 23.3; }
                if (vers.contains("1.23.4")) { mcVersion = 23.4; }
                if (vers.contains("1.23.5")) { mcVersion = 23.5; }
                if (vers.contains("1.23.6")) { mcVersion = 23.6; }
            }

            return mcVersion;

        } else {

            // Return the gathered value
            return mcVersion;
        }
    }

    public static HashMap<GooPVersionMaterials, Material> versionMaterials = new HashMap<>();
    public static HashMap<GooPVersionEntities, EntityType> versionEntityTypes = new HashMap<>();
    public static HashMap<GooPVersionEnchantments, Enchantment> versionEnchantments = new HashMap<>();
    public static HashMap<GooPVersionAttributes, Attribute> versionAttributes = new HashMap<>();
    public static HashMap<GooPVersionPotionEffects, PotionEffectType> versionPotionEffects = new HashMap<>();
    public static void InitializeMaterials() {

        // Get MC Version
        GetMinecraftVersion();

        //region Materials
        //region Minecraft 1.13-
        if (mcVersion < 14.0) {
            versionMaterials.put(GooPVersionMaterials.CACTUS_GREEN, GetMaterialFromString("CACTUS_GREEN"));
            versionMaterials.put(GooPVersionMaterials.DANDELION_YELLOW, GetMaterialFromString("DANDELION_YELLOW"));
            versionMaterials.put(GooPVersionMaterials.ROSE_RED, GetMaterialFromString("ROSE_RED"));
            versionMaterials.put(GooPVersionMaterials.SIGN, GetMaterialFromString("SIGN"));
            versionMaterials.put(GooPVersionMaterials.WALL_SIGN, GetMaterialFromString("WALL_SIGN"));
        }
        //endregion

        //region Minecraft 1.14+
        if (mcVersion >= 14.0) {
            versionMaterials.put(GooPVersionMaterials.ACACIA_SIGN, GetMaterialFromString("ACACIA_SIGN"));
            versionMaterials.put(GooPVersionMaterials.ACACIA_WALL_SIGN, GetMaterialFromString("ACACIA_WALL_SIGN"));
            versionMaterials.put(GooPVersionMaterials.ANDESITE_SLAB, GetMaterialFromString("ANDESITE_SLAB"));
            versionMaterials.put(GooPVersionMaterials.ANDESITE_STAIRS, GetMaterialFromString("ANDESITE_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.ANDESITE_WALL, GetMaterialFromString("ANDESITE_WALL"));

            versionMaterials.put(GooPVersionMaterials.BAMBOO, GetMaterialFromString("BAMBOO"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_SAPLING, GetMaterialFromString("BAMBOO_SAPLING"));
            versionMaterials.put(GooPVersionMaterials.BARREL, GetMaterialFromString("BARREL"));
            versionMaterials.put(GooPVersionMaterials.BELL, GetMaterialFromString("BELL"));
            versionMaterials.put(GooPVersionMaterials.BIRCH_SIGN, GetMaterialFromString("BIRCH_SIGN"));

            versionMaterials.put(GooPVersionMaterials.BIRCH_WALL_SIGN, GetMaterialFromString("BIRCH_WALL_SIGN"));
            versionMaterials.put(GooPVersionMaterials.BLACK_DYE, GetMaterialFromString("BLACK_DYE"));
            versionMaterials.put(GooPVersionMaterials.BLAST_FURNACE, GetMaterialFromString("BLAST_FURNACE"));
            versionMaterials.put(GooPVersionMaterials.BLUE_DYE, GetMaterialFromString("BLUE_DYE"));
            versionMaterials.put(GooPVersionMaterials.BRICK_WALL, GetMaterialFromString("BRICK_WALL"));

            versionMaterials.put(GooPVersionMaterials.BROWN_DYE, GetMaterialFromString("BROWN_DYE"));

            versionMaterials.put(GooPVersionMaterials.CACTUS_GREEN, GetMaterialFromString("GREEN_DYE"));
            versionMaterials.put(GooPVersionMaterials.CAMPFIRE, GetMaterialFromString("CAMPFIRE"));
            versionMaterials.put(GooPVersionMaterials.CARTOGRAPHY_TABLE, GetMaterialFromString("CARTOGRAPHY_TABLE"));
            versionMaterials.put(GooPVersionMaterials.CAT_SPAWN_EGG, GetMaterialFromString("CAT_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.COMPOSTER, GetMaterialFromString("COMPOSTER"));

            versionMaterials.put(GooPVersionMaterials.CORNFLOWER, GetMaterialFromString("CORNFLOWER"));
            versionMaterials.put(GooPVersionMaterials.CREEPER_BANNER_PATTERN, GetMaterialFromString("CREEPER_BANNER_PATTERN"));
            versionMaterials.put(GooPVersionMaterials.CROSSBOW, GetMaterialFromString("CROSSBOW"));
            versionMaterials.put(GooPVersionMaterials.CUT_RED_SANDSTONE_SLAB, GetMaterialFromString("CUT_RED_SANDSTONE_SLAB"));
            versionMaterials.put(GooPVersionMaterials.CUT_SANDSTONE_SLAB, GetMaterialFromString("CUT_SANDSTONE_SLAB"));

            versionMaterials.put(GooPVersionMaterials.DANDELION_YELLOW, GetMaterialFromString("YELLOW_DYE"));
            versionMaterials.put(GooPVersionMaterials.DARK_OAK_SIGN, GetMaterialFromString("DARK_OAK_SIGN"));
            versionMaterials.put(GooPVersionMaterials.DARK_OAK_WALL_SIGN, GetMaterialFromString("DARK_OAK_WALL_SIGN"));
            versionMaterials.put(GooPVersionMaterials.DIORITE_SLAB, GetMaterialFromString("DIORITE_SLAB"));
            versionMaterials.put(GooPVersionMaterials.DIORITE_STAIRS, GetMaterialFromString("DIORITE_STAIRS"));

            versionMaterials.put(GooPVersionMaterials.DIORITE_WALL, GetMaterialFromString("DIORITE_WALL"));

            versionMaterials.put(GooPVersionMaterials.END_STONE_BRICK_SLAB, GetMaterialFromString("END_STONE_BRICK_SLAB"));
            versionMaterials.put(GooPVersionMaterials.END_STONE_BRICK_STAIRS, GetMaterialFromString("END_STONE_BRICK_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.END_STONE_BRICK_WALL, GetMaterialFromString("END_STONE_BRICK_WALL"));

            versionMaterials.put(GooPVersionMaterials.FLETCHING_TABLE, GetMaterialFromString("FLETCHING_TABLE"));
            versionMaterials.put(GooPVersionMaterials.FLOWER_BANNER_PATTERN, GetMaterialFromString("FLOWER_BANNER_PATTERN"));
            versionMaterials.put(GooPVersionMaterials.FOX_SPAWN_EGG, GetMaterialFromString("FOX_SPAWN_EGG"));

            versionMaterials.put(GooPVersionMaterials.GLOBE_BANNER_PATTERN, GetMaterialFromString("GLOBE_BANNER_PATTERN"));
            versionMaterials.put(GooPVersionMaterials.GRANITE_SLAB, GetMaterialFromString("GRANITE_SLAB"));
            versionMaterials.put(GooPVersionMaterials.GRANITE_STAIRS, GetMaterialFromString("GRANITE_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.GRANITE_WALL, GetMaterialFromString("GRANITE_WALL"));
            versionMaterials.put(GooPVersionMaterials.GREEN_DYE, GetMaterialFromString("GREEN_DYE"));

            versionMaterials.put(GooPVersionMaterials.GRINDSTONE, GetMaterialFromString("GRINDSTONE"));

            versionMaterials.put(GooPVersionMaterials.JIGSAW, GetMaterialFromString("JIGSAW"));
            versionMaterials.put(GooPVersionMaterials.JUNGLE_SIGN, GetMaterialFromString("JUNGLE_SIGN"));
            versionMaterials.put(GooPVersionMaterials.JUNGLE_WALL_SIGN, GetMaterialFromString("JUNGLE_WALL_SIGN"));

            versionMaterials.put(GooPVersionMaterials.LECTERN, GetMaterialFromString("LECTERN"));
            versionMaterials.put(GooPVersionMaterials.LANTERN, GetMaterialFromString("LANTERN"));
            versionMaterials.put(GooPVersionMaterials.LEATHER_HORSE_ARMOR, GetMaterialFromString("LEATHER_HORSE_ARMOR"));
            versionMaterials.put(GooPVersionMaterials.LILY_OF_THE_VALLEY, GetMaterialFromString("LILY_OF_THE_VALLEY"));
            versionMaterials.put(GooPVersionMaterials.LOOM, GetMaterialFromString("LOOM"));

            versionMaterials.put(GooPVersionMaterials.MOJANG_BANNER_PATTERN, GetMaterialFromString("MOJANG_BANNER_PATTERN"));
            versionMaterials.put(GooPVersionMaterials.MOSSY_COBBLESTONE_SLAB, GetMaterialFromString("MOSSY_COBBLESTONE_SLAB"));
            versionMaterials.put(GooPVersionMaterials.MOSSY_COBBLESTONE_STAIRS, GetMaterialFromString("MOSSY_COBBLESTONE_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.MOSSY_STONE_BRICK_SLAB, GetMaterialFromString("MOSSY_STONE_BRICK_SLAB"));
            versionMaterials.put(GooPVersionMaterials.MOSSY_STONE_BRICK_STAIRS, GetMaterialFromString("MOSSY_STONE_BRICK_STAIRS"));

            versionMaterials.put(GooPVersionMaterials.MOSSY_STONE_BRICK_WALL, GetMaterialFromString("MOSSY_STONE_BRICK_WALL"));

            versionMaterials.put(GooPVersionMaterials.NETHER_BRICK_WALL, GetMaterialFromString("NETHER_BRICK_WALL"));

            versionMaterials.put(GooPVersionMaterials.OAK_SIGN, GetMaterialFromString("OAK_SIGN"));
            versionMaterials.put(GooPVersionMaterials.OAK_WALL_SIGN, GetMaterialFromString("OAK_WALL_SIGN"));

            versionMaterials.put(GooPVersionMaterials.PANDA_SPAWN_EGG, GetMaterialFromString("PANDA_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.PILLAGER_SPAWN_EGG, GetMaterialFromString("PILLAGER_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_ANDESITE_SLAB, GetMaterialFromString("POLISHED_ANDESITE_SLAB"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_DIORITE_SLAB, GetMaterialFromString("POLISHED_DIORITE_SLAB"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_GRANITE_SLAB, GetMaterialFromString("POLISHED_GRANITE_SLAB"));

            versionMaterials.put(GooPVersionMaterials.POTTED_BAMBOO, GetMaterialFromString("POTTED_BAMBOO"));
            versionMaterials.put(GooPVersionMaterials.POTTED_CORNFLOWER, GetMaterialFromString("POTTED_CORNFLOWER"));
            versionMaterials.put(GooPVersionMaterials.POTTED_LILY_OF_THE_VALLEY, GetMaterialFromString("POTTED_LILY_OF_THE_VALLEY"));
            versionMaterials.put(GooPVersionMaterials.POTTED_WITHER_ROSE, GetMaterialFromString("POTTED_WITHER_ROSE"));
            versionMaterials.put(GooPVersionMaterials.PRISMARINE_WALL, GetMaterialFromString("PRISMARINE_WALL"));

            versionMaterials.put(GooPVersionMaterials.RAVAGER_SPAWN_EGG, GetMaterialFromString("RAVAGER_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.RED_DYE, GetMaterialFromString("RED_DYE"));
            versionMaterials.put(GooPVersionMaterials.RED_NETHER_BRICK_SLAB, GetMaterialFromString("RED_NETHER_BRICK_SLAB"));
            versionMaterials.put(GooPVersionMaterials.RED_NETHER_BRICK_STAIRS, GetMaterialFromString("RED_NETHER_BRICK_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.RED_NETHER_BRICK_WALL, GetMaterialFromString("RED_NETHER_BRICK_WALL"));

            versionMaterials.put(GooPVersionMaterials.RED_SANDSTONE_WALL, GetMaterialFromString("RED_SANDSTONE_WALL"));
            versionMaterials.put(GooPVersionMaterials.ROSE_RED, GetMaterialFromString("RED_DYE"));

            versionMaterials.put(GooPVersionMaterials.SANDSTONE_WALL, GetMaterialFromString("SANDSTONE_WALL"));
            versionMaterials.put(GooPVersionMaterials.SCAFFOLDING, GetMaterialFromString("SCAFFOLDING"));
            versionMaterials.put(GooPVersionMaterials.SIGN, GetMaterialFromString("OAK_SIGN"));
            versionMaterials.put(GooPVersionMaterials.SKULL_BANNER_PATTERN, GetMaterialFromString("SKULL_BANNER_PATTERN"));
            versionMaterials.put(GooPVersionMaterials.SMITHING_TABLE, GetMaterialFromString("SMITHING_TABLE"));

            versionMaterials.put(GooPVersionMaterials.SMOKER, GetMaterialFromString("SMOKER"));
            versionMaterials.put(GooPVersionMaterials.SMOOTH_QUARTZ_SLAB, GetMaterialFromString("SMOOTH_QUARTZ_SLAB"));
            versionMaterials.put(GooPVersionMaterials.SMOOTH_QUARTZ_STAIRS, GetMaterialFromString("SMOOTH_QUARTZ_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.SMOOTH_RED_SANDSTONE_SLAB, GetMaterialFromString("SMOOTH_RED_SANDSTONE_SLAB"));
            versionMaterials.put(GooPVersionMaterials.SMOOTH_RED_SANDSTONE_STAIRS, GetMaterialFromString("SMOOTH_RED_SANDSTONE_STAIRS"));

            versionMaterials.put(GooPVersionMaterials.SMOOTH_SANDSTONE_SLAB, GetMaterialFromString("SMOOTH_SANDSTONE_SLAB"));
            versionMaterials.put(GooPVersionMaterials.SMOOTH_SANDSTONE_STAIRS, GetMaterialFromString("SMOOTH_SANDSTONE_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.SMOOTH_STONE_SLAB, GetMaterialFromString("SMOOTH_STONE_SLAB"));
            versionMaterials.put(GooPVersionMaterials.SPRUCE_SIGN, GetMaterialFromString("SPRUCE_SIGN"));
            versionMaterials.put(GooPVersionMaterials.SPRUCE_WALL_SIGN, GetMaterialFromString("SPRUCE_WALL_SIGN"));

            versionMaterials.put(GooPVersionMaterials.STONECUTTER, GetMaterialFromString("STONECUTTER"));
            versionMaterials.put(GooPVersionMaterials.STONE_BRICK_WALL, GetMaterialFromString("STONE_BRICK_WALL"));
            versionMaterials.put(GooPVersionMaterials.STONE_STAIRS, GetMaterialFromString("STONE_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.SUSPICIOUS_STEW, GetMaterialFromString("SUSPICIOUS_STEW"));
            versionMaterials.put(GooPVersionMaterials.SWEET_BERRIES, GetMaterialFromString("SWEET_BERRIES"));

            versionMaterials.put(GooPVersionMaterials.SWEET_BERRY_BUSH, GetMaterialFromString("SWEET_BERRY_BUSH"));

            versionMaterials.put(GooPVersionMaterials.TRADER_LLAMA_SPAWN_EGG, GetMaterialFromString("TRADER_LLAMA_SPAWN_EGG"));

            versionMaterials.put(GooPVersionMaterials.WALL_SIGN, GetMaterialFromString("OAK_WALL_SIGN"));
            versionMaterials.put(GooPVersionMaterials.WANDERING_TRADER_SPAWN_EGG, GetMaterialFromString("WANDERING_TRADER_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.WHITE_DYE, GetMaterialFromString("WHITE_DYE"));
            versionMaterials.put(GooPVersionMaterials.WITHER_ROSE, GetMaterialFromString("WITHER_ROSE"));

            versionMaterials.put(GooPVersionMaterials.YELLOW_DYE, GetMaterialFromString("YELLOW_DYE"));
        }
        //endregion

        //region Minecraft Version 1.15+
        if (mcVersion >= 15.0) {
            versionMaterials.put(GooPVersionMaterials.BEEHIVE, GetMaterialFromString("BEEHIVE"));
            versionMaterials.put(GooPVersionMaterials.BEE_NEST, GetMaterialFromString("BEE_NEST"));
            versionMaterials.put(GooPVersionMaterials.BEE_SPAWN_EGG, GetMaterialFromString("BEE_SPAWN_EGG"));

            versionMaterials.put(GooPVersionMaterials.HONEYCOMB, GetMaterialFromString("HONEYCOMB"));
            versionMaterials.put(GooPVersionMaterials.HONEYCOMB_BLOCK, GetMaterialFromString("HONEYCOMB_BLOCK"));
            versionMaterials.put(GooPVersionMaterials.HONEY_BLOCK, GetMaterialFromString("HONEY_BLOCK"));
            versionMaterials.put(GooPVersionMaterials.HONEY_BOTTLE, GetMaterialFromString("HONEY_BOTTLE"));
        }
        //endregion

        //region Minecraft Version 1.16+
        if (mcVersion >= 16.0) {
            versionMaterials.put(GooPVersionMaterials.ANCIENT_DEBRIS, GetMaterialFromString("ANCIENT_DEBRIS"));

            versionMaterials.put(GooPVersionMaterials.BASALT, GetMaterialFromString("BASALT"));
            versionMaterials.put(GooPVersionMaterials.BLACKSTONE, GetMaterialFromString("BLACKSTONE"));
            versionMaterials.put(GooPVersionMaterials.BLACKSTONE_SLAB, GetMaterialFromString("BLACKSTONE_SLAB"));
            versionMaterials.put(GooPVersionMaterials.BLACKSTONE_STAIRS, GetMaterialFromString("BLACKSTONE_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.BLACKSTONE_WALL, GetMaterialFromString("BLACKSTONE_WALL"));

            versionMaterials.put(GooPVersionMaterials.CHAIN, GetMaterialFromString("CHAIN"));
            versionMaterials.put(GooPVersionMaterials.CHISELED_NETHER_BRICKS, GetMaterialFromString("CHISELED_NETHER_BRICKS"));
            versionMaterials.put(GooPVersionMaterials.CHISELED_POLISHED_BLACKSTONE, GetMaterialFromString("CHISELED_POLISHED_BLACKSTONE"));
            versionMaterials.put(GooPVersionMaterials.CRACKED_NETHER_BRICKS, GetMaterialFromString("CRACKED_NETHER_BRICKS"));
            versionMaterials.put(GooPVersionMaterials.CRACKED_POLISHED_BLACKSTONE_BRICKS, GetMaterialFromString("CRACKED_POLISHED_BLACKSTONE_BRICKS"));

            versionMaterials.put(GooPVersionMaterials.CRIMSON_BUTTON, GetMaterialFromString("CRIMSON_BUTTON"));
            versionMaterials.put(GooPVersionMaterials.CRIMSON_DOOR, GetMaterialFromString("CRIMSON_DOOR"));
            versionMaterials.put(GooPVersionMaterials.CRIMSON_FENCE, GetMaterialFromString("CRIMSON_FENCE"));
            versionMaterials.put(GooPVersionMaterials.CRIMSON_FENCE_GATE, GetMaterialFromString("CRIMSON_FENCE_GATE"));
            versionMaterials.put(GooPVersionMaterials.CRIMSON_FUNGUS, GetMaterialFromString("CRIMSON_FUNGUS"));

            versionMaterials.put(GooPVersionMaterials.CRIMSON_HYPHAE, GetMaterialFromString("CRIMSON_HYPHAE"));
            versionMaterials.put(GooPVersionMaterials.CRIMSON_NYLIUM, GetMaterialFromString("CRIMSON_NYLIUM"));
            versionMaterials.put(GooPVersionMaterials.CRIMSON_PLANKS, GetMaterialFromString("CRIMSON_PLANKS"));
            versionMaterials.put(GooPVersionMaterials.CRIMSON_PRESSURE_PLATE, GetMaterialFromString("CRIMSON_PRESSURE_PLATE"));
            versionMaterials.put(GooPVersionMaterials.CRIMSON_ROOTS, GetMaterialFromString("CRIMSON_ROOTS"));

            versionMaterials.put(GooPVersionMaterials.CRIMSON_SIGN, GetMaterialFromString("CRIMSON_SIGN"));
            versionMaterials.put(GooPVersionMaterials.CRIMSON_SLAB, GetMaterialFromString("CRIMSON_SLAB"));
            versionMaterials.put(GooPVersionMaterials.CRIMSON_STAIRS, GetMaterialFromString("CRIMSON_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.CRIMSON_STEM, GetMaterialFromString("CRIMSON_STEM"));
            versionMaterials.put(GooPVersionMaterials.CRIMSON_TRAPDOOR, GetMaterialFromString("CRIMSON_TRAPDOOR"));

            versionMaterials.put(GooPVersionMaterials.CRIMSON_WALL_SIGN, GetMaterialFromString("CRIMSON_WALL_SIGN"));
            versionMaterials.put(GooPVersionMaterials.CRYING_OBSIDIAN, GetMaterialFromString("CRYING_OBSIDIAN"));
            versionMaterials.put(GooPVersionMaterials.GILDED_BLACKSTONE, GetMaterialFromString("GILDED_BLACKSTONE"));

            versionMaterials.put(GooPVersionMaterials.LODESTONE, GetMaterialFromString("LODESTONE"));

            versionMaterials.put(GooPVersionMaterials.MUSIC_DISC_PIGSTEP, GetMaterialFromString("MUSIC_DISC_PIGSTEP"));

            versionMaterials.put(GooPVersionMaterials.NETHERITE_BLOCK, GetMaterialFromString("NETHERITE_BLOCK"));
            versionMaterials.put(GooPVersionMaterials.NETHER_GOLD_ORE, GetMaterialFromString("NETHER_GOLD_ORE"));
            versionMaterials.put(GooPVersionMaterials.NETHER_SPROUTS, GetMaterialFromString("NETHER_SPROUTS"));
            versionMaterials.put(GooPVersionMaterials.NETHERITE_AXE, GetMaterialFromString("NETHERITE_AXE"));
            versionMaterials.put(GooPVersionMaterials.NETHERITE_BOOTS, GetMaterialFromString("NETHERITE_BOOTS"));
            versionMaterials.put(GooPVersionMaterials.NETHERITE_CHESTPLATE, GetMaterialFromString("NETHERITE_CHESTPLATE"));
            versionMaterials.put(GooPVersionMaterials.NETHERITE_HELMET, GetMaterialFromString("NETHERITE_HELMET"));
            versionMaterials.put(GooPVersionMaterials.NETHERITE_HOE, GetMaterialFromString("NETHERITE_HOE"));
            versionMaterials.put(GooPVersionMaterials.NETHERITE_INGOT, GetMaterialFromString("NETHERITE_INGOT"));
            versionMaterials.put(GooPVersionMaterials.NETHERITE_LEGGINGS, GetMaterialFromString("NETHERITE_LEGGINGS"));
            versionMaterials.put(GooPVersionMaterials.NETHERITE_PICKAXE, GetMaterialFromString("NETHERITE_PICKAXE"));
            versionMaterials.put(GooPVersionMaterials.NETHERITE_SCRAP, GetMaterialFromString("NETHERITE_SCRAP"));
            versionMaterials.put(GooPVersionMaterials.NETHERITE_SHOVEL, GetMaterialFromString("NETHERITE_SHOVEL"));
            versionMaterials.put(GooPVersionMaterials.NETHERITE_SWORD, GetMaterialFromString("NETHERITE_SWORD"));

            versionMaterials.put(GooPVersionMaterials.POLISHED_BASALT, GetMaterialFromString("POLISHED_BASALT"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_BLACKSTONE, GetMaterialFromString("POLISHED_BLACKSTONE"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_BLACKSTONE_BRICKS, GetMaterialFromString("POLISHED_BLACKSTONE_BRICKS"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_BLACKSTONE_BRICK_SLAB, GetMaterialFromString("POLISHED_BLACKSTONE_BRICK_SLAB"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_BLACKSTONE_BRICK_STAIRS, GetMaterialFromString("POLISHED_BLACKSTONE_BRICK_STAIRS"));

            versionMaterials.put(GooPVersionMaterials.POLISHED_BLACKSTONE_BRICK_WALL, GetMaterialFromString("POLISHED_BLACKSTONE_BRICK_WALL"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_BLACKSTONE_BUTTON, GetMaterialFromString("POLISHED_BLACKSTONE_BUTTON"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_BLACKSTONE_PRESSURE_PLATE, GetMaterialFromString("POLISHED_BLACKSTONE_PRESSURE_PLATE"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_BLACKSTONE_SLAB, GetMaterialFromString("POLISHED_BLACKSTONE_SLAB"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_BLACKSTONE_STAIRS, GetMaterialFromString("POLISHED_BLACKSTONE_STAIRS"));

            versionMaterials.put(GooPVersionMaterials.POLISHED_BLACKSTONE_WALL, GetMaterialFromString("POLISHED_BLACKSTONE_WALL"));

            versionMaterials.put(GooPVersionMaterials.POTTED_CRIMSON_FUNGUS, GetMaterialFromString("POTTED_CRIMSON_FUNGUS"));
            versionMaterials.put(GooPVersionMaterials.POTTED_CRIMSON_ROOTS, GetMaterialFromString("POTTED_CRIMSON_ROOTS"));
            versionMaterials.put(GooPVersionMaterials.POTTED_WARPED_FUNGUS, GetMaterialFromString("POTTED_WARPED_FUNGUS"));
            versionMaterials.put(GooPVersionMaterials.POTTED_WARPED_ROOTS, GetMaterialFromString("POTTED_WARPED_ROOTS"));
            versionMaterials.put(GooPVersionMaterials.PIGLIN_BANNER_PATTERN, GetMaterialFromString("PIGLIN_BANNER_PATTERN"));

            versionMaterials.put(GooPVersionMaterials.HOGLIN_SPAWN_EGG, GetMaterialFromString("HOGLIN_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.PIGLIN_SPAWN_EGG, GetMaterialFromString("PIGLIN_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.ZOMBIFIED_PIGLIN_SPAWN_EGG, GetMaterialFromString("ZOMBIFIED_PIGLIN_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.STRIDER_SPAWN_EGG, GetMaterialFromString("STRIDER_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.ZOGLIN_SPAWN_EGG, GetMaterialFromString("ZOGLIN_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.WARPED_FUNGUS_ON_A_STICK, GetMaterialFromString("WARPED_FUNGUS_ON_A_STICK"));

            versionMaterials.put(GooPVersionMaterials.QUARTZ_BRICKS, GetMaterialFromString("QUARTZ_BRICKS"));

            versionMaterials.put(GooPVersionMaterials.RESPAWN_ANCHOR, GetMaterialFromString("RESPAWN_ANCHOR"));

            versionMaterials.put(GooPVersionMaterials.SHROOMLIGHT, GetMaterialFromString("SHROOMLIGHT"));
            versionMaterials.put(GooPVersionMaterials.SOUL_CAMPFIRE, GetMaterialFromString("SOUL_CAMPFIRE"));
            versionMaterials.put(GooPVersionMaterials.SOUL_FIRE, GetMaterialFromString("SOUL_FIRE"));
            versionMaterials.put(GooPVersionMaterials.SOUL_LANTERN, GetMaterialFromString("SOUL_LANTERN"));
            versionMaterials.put(GooPVersionMaterials.SOUL_SOIL, GetMaterialFromString("SOUL_SOIL"));

            versionMaterials.put(GooPVersionMaterials.SOUL_TORCH, GetMaterialFromString("SOUL_TORCH"));
            versionMaterials.put(GooPVersionMaterials.SOUL_WALL_TORCH, GetMaterialFromString("SOUL_WALL_TORCH"));
            versionMaterials.put(GooPVersionMaterials.STRIPPED_CRIMSON_HYPHAE, GetMaterialFromString("STRIPPED_CRIMSON_HYPHAE"));
            versionMaterials.put(GooPVersionMaterials.STRIPPED_CRIMSON_STEM, GetMaterialFromString("STRIPPED_CRIMSON_STEM"));
            versionMaterials.put(GooPVersionMaterials.STRIPPED_WARPED_HYPHAE, GetMaterialFromString("STRIPPED_WARPED_HYPHAE"));

            versionMaterials.put(GooPVersionMaterials.STRIPPED_WARPED_STEM, GetMaterialFromString("STRIPPED_WARPED_STEM"));

            versionMaterials.put(GooPVersionMaterials.TARGET, GetMaterialFromString("TARGET"));
            versionMaterials.put(GooPVersionMaterials.TWISTING_VINES, GetMaterialFromString("TWISTING_VINES"));
            versionMaterials.put(GooPVersionMaterials.TWISTING_VINES_PLANT, GetMaterialFromString("TWISTING_VINES_PLANT"));

            versionMaterials.put(GooPVersionMaterials.WARPED_BUTTON, GetMaterialFromString("WARPED_BUTTON"));
            versionMaterials.put(GooPVersionMaterials.WARPED_DOOR, GetMaterialFromString("WARPED_DOOR"));
            versionMaterials.put(GooPVersionMaterials.WARPED_FENCE, GetMaterialFromString("WARPED_FENCE"));
            versionMaterials.put(GooPVersionMaterials.WARPED_FENCE_GATE, GetMaterialFromString("WARPED_FENCE_GATE"));
            versionMaterials.put(GooPVersionMaterials.WARPED_FUNGUS, GetMaterialFromString("WARPED_FUNGUS"));

            versionMaterials.put(GooPVersionMaterials.WARPED_HYPHAE, GetMaterialFromString("WARPED_HYPHAE"));
            versionMaterials.put(GooPVersionMaterials.WARPED_NYLIUM, GetMaterialFromString("WARPED_NYLIUM"));
            versionMaterials.put(GooPVersionMaterials.WARPED_PLANKS, GetMaterialFromString("WARPED_PLANKS"));
            versionMaterials.put(GooPVersionMaterials.WARPED_PRESSURE_PLATE, GetMaterialFromString("WARPED_PRESSURE_PLATE"));
            versionMaterials.put(GooPVersionMaterials.WARPED_ROOTS, GetMaterialFromString("WARPED_ROOTS"));

            versionMaterials.put(GooPVersionMaterials.WARPED_SIGN, GetMaterialFromString("WARPED_SIGN"));
            versionMaterials.put(GooPVersionMaterials.WARPED_SLAB, GetMaterialFromString("WARPED_SLAB"));
            versionMaterials.put(GooPVersionMaterials.WARPED_STAIRS, GetMaterialFromString("WARPED_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.WARPED_STEM, GetMaterialFromString("WARPED_STEM"));
            versionMaterials.put(GooPVersionMaterials.WARPED_TRAPDOOR, GetMaterialFromString("WARPED_TRAPDOOR"));

            versionMaterials.put(GooPVersionMaterials.WARPED_WALL_SIGN, GetMaterialFromString("WARPED_WALL_SIGN"));
            versionMaterials.put(GooPVersionMaterials.WARPED_WART_BLOCK, GetMaterialFromString("WARPED_WART_BLOCK"));
            versionMaterials.put(GooPVersionMaterials.WEEPING_VINES, GetMaterialFromString("WEEPING_VINES"));
            versionMaterials.put(GooPVersionMaterials.WEEPING_VINES_PLANT, GetMaterialFromString("WEEPING_VINES_PLANT"));
        }
        //endregion

        //region Minecraft Version 1.17+
        if (mcVersion >= 17.0) {
            //* YE-OLD-MMO
            versionMaterials.put(GooPVersionMaterials.AXOLOTL_SPAWN_EGG, GetMaterialFromString("AXOLOTL_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.AMETHYST_SHARD, GetMaterialFromString("AMETHYST_SHARD"));
            versionMaterials.put(GooPVersionMaterials.AXOLOTL_BUCKET, GetMaterialFromString("AXOLOTL_BUCKET"));
            versionMaterials.put(GooPVersionMaterials.AZALEA, GetMaterialFromString("AZALEA"));
            versionMaterials.put(GooPVersionMaterials.AZALEA_LEAVES, GetMaterialFromString("AZALEA_LEAVES"));
            versionMaterials.put(GooPVersionMaterials.AMETHYST_BLOCK, GetMaterialFromString("AMETHYST_BLOCK"));
            versionMaterials.put(GooPVersionMaterials.AMETHYST_CLUSTER, GetMaterialFromString("AMETHYST_CLUSTER"));

            versionMaterials.put(GooPVersionMaterials.BUDDING_AMETHYST, GetMaterialFromString("BUDDING_AMETHYST"));
            versionMaterials.put(GooPVersionMaterials.BIG_DRIPLEAF, GetMaterialFromString("BIG_DRIPLEAF"));
            versionMaterials.put(GooPVersionMaterials.BUNDLE, GetMaterialFromString("BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.BIG_DRIPLEAF_STEM, GetMaterialFromString("BIG_DRIPLEAF_STEM"));
            versionMaterials.put(GooPVersionMaterials.BLACK_CANDLE_CAKE, GetMaterialFromString("BLACK_CANDLE_CAKE"));
            versionMaterials.put(GooPVersionMaterials.BLUE_CANDLE, GetMaterialFromString("BLUE_CANDLE"));
            versionMaterials.put(GooPVersionMaterials.BROWN_CANDLE, GetMaterialFromString("BROWN_CANDLE"));
            versionMaterials.put(GooPVersionMaterials.BLACK_CANDLE, GetMaterialFromString("BLACK_CANDLE"));
            versionMaterials.put(GooPVersionMaterials.BLUE_CANDLE_CAKE, GetMaterialFromString("BLUE_CANDLE_CAKE"));
            versionMaterials.put(GooPVersionMaterials.BROWN_CANDLE_CAKE, GetMaterialFromString("BROWN_CANDLE_CAKE"));

            versionMaterials.put(GooPVersionMaterials.CANDLE, GetMaterialFromString("CANDLE"));
            versionMaterials.put(GooPVersionMaterials.COPPER_INGOT, GetMaterialFromString("COPPER_INGOT"));
            versionMaterials.put(GooPVersionMaterials.CUT_COPPER, GetMaterialFromString("CUT_COPPER"));
            versionMaterials.put(GooPVersionMaterials.CUT_COPPER_STAIRS, GetMaterialFromString("CUT_COPPER_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.COPPER_BLOCK, GetMaterialFromString("COPPER_BLOCK"));
            versionMaterials.put(GooPVersionMaterials.COPPER_ORE, GetMaterialFromString("COPPER_ORE"));
            versionMaterials.put(GooPVersionMaterials.CALCITE, GetMaterialFromString("CALCITE"));
            versionMaterials.put(GooPVersionMaterials.COBBLED_DEEPSLATE, GetMaterialFromString("COBBLED_DEEPSLATE"));
            versionMaterials.put(GooPVersionMaterials.CUT_COPPER_SLAB, GetMaterialFromString("CUT_COPPER_SLAB"));
            versionMaterials.put(GooPVersionMaterials.CRACKED_DEEPSLATE_BRICKS, GetMaterialFromString("CRACKED_DEEPSLATE_BRICKS"));
            versionMaterials.put(GooPVersionMaterials.CRACKED_DEEPSLATE_TILES, GetMaterialFromString("CRACKED_DEEPSLATE_TILES"));
            versionMaterials.put(GooPVersionMaterials.COBBLED_DEEPSLATE_WALL, GetMaterialFromString("COBBLED_DEEPSLATE_WALL"));
            versionMaterials.put(GooPVersionMaterials.COBBLED_DEEPSLATE_STAIRS, GetMaterialFromString("COBBLED_DEEPSLATE_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.COBBLED_DEEPSLATE_SLAB, GetMaterialFromString("COBBLED_DEEPSLATE_SLAB"));
            versionMaterials.put(GooPVersionMaterials.CUT_RED_SANDSTONE, GetMaterialFromString("CUT_RED_SANDSTONE"));
            versionMaterials.put(GooPVersionMaterials.CHISELED_DEEPSLATE, GetMaterialFromString("CHISELED_DEEPSLATE"));
            versionMaterials.put(GooPVersionMaterials.CYAN_CANDLE_CAKE, GetMaterialFromString("CYAN_CANDLE_CAKE"));
            versionMaterials.put(GooPVersionMaterials.CAVE_VINES, GetMaterialFromString("CAVE_VINES"));
            versionMaterials.put(GooPVersionMaterials.CAVE_VINES_PLANT, GetMaterialFromString("CAVE_VINES_PLANT"));
            versionMaterials.put(GooPVersionMaterials.CYAN_CANDLE, GetMaterialFromString("CYAN_CANDLE"));
            versionMaterials.put(GooPVersionMaterials.CANDLE_CAKE, GetMaterialFromString("CANDLE_CAKE"));

            versionMaterials.put(GooPVersionMaterials.DEEPSLATE_COPPER_ORE, GetMaterialFromString("DEEPSLATE_COPPER_ORE"));
            versionMaterials.put(GooPVersionMaterials.DEEPSLATE_GOLD_ORE, GetMaterialFromString("DEEPSLATE_GOLD_ORE"));
            versionMaterials.put(GooPVersionMaterials.DEEPSLATE_REDSTONE_ORE, GetMaterialFromString("DEEPSLATE_REDSTONE_ORE"));
            versionMaterials.put(GooPVersionMaterials.DEEPSLATE_EMERALD_ORE, GetMaterialFromString("DEEPSLATE_EMERALD_ORE"));
            versionMaterials.put(GooPVersionMaterials.DEEPSLATE_LAPIS_ORE, GetMaterialFromString("DEEPSLATE_LAPIS_ORE"));
            versionMaterials.put(GooPVersionMaterials.DEEPSLATE_DIAMOND_ORE, GetMaterialFromString("DEEPSLATE_DIAMOND_ORE"));
            versionMaterials.put(GooPVersionMaterials.DEEPSLATE, GetMaterialFromString("DEEPSLATE"));
            versionMaterials.put(GooPVersionMaterials.DEEPSLATE_COAL_ORE, GetMaterialFromString("DEEPSLATE_COAL_ORE"));
            versionMaterials.put(GooPVersionMaterials.DEEPSLATE_IRON_ORE, GetMaterialFromString("DEEPSLATE_IRON_ORE"));
            versionMaterials.put(GooPVersionMaterials.DRIPSTONE_BLOCK, GetMaterialFromString("DRIPSTONE_BLOCK"));
            versionMaterials.put(GooPVersionMaterials.DEEPSLATE_TILES, GetMaterialFromString("DEEPSLATE_TILES"));
            versionMaterials.put(GooPVersionMaterials.DEEPSLATE_BRICKS, GetMaterialFromString("DEEPSLATE_BRICKS"));
            versionMaterials.put(GooPVersionMaterials.DEEPSLATE_BRICK_WALL, GetMaterialFromString("DEEPSLATE_BRICK_WALL"));
            versionMaterials.put(GooPVersionMaterials.DEEPSLATE_TILE_WALL, GetMaterialFromString("DEEPSLATE_TILE_WALL"));
            versionMaterials.put(GooPVersionMaterials.DEEPSLATE_BRICK_STAIRS, GetMaterialFromString("DEEPSLATE_BRICK_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.DEEPSLATE_TILE_STAIRS, GetMaterialFromString("DEEPSLATE_TILE_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.DEEPSLATE_BRICK_SLAB, GetMaterialFromString("DEEPSLATE_BRICK_SLAB"));
            versionMaterials.put(GooPVersionMaterials.DEEPSLATE_TILE_SLAB, GetMaterialFromString("DEEPSLATE_TILE_SLAB"));
            versionMaterials.put(GooPVersionMaterials.DIRT_PATH, GetMaterialFromString("DIRT_PATH"));   //renamed from GRASS_PATH
            versionMaterials.put(GooPVersionMaterials.GRASS_PATH, GetMaterialFromString("DIRT_PATH"));   //renamed from GRASS_PATH

            versionMaterials.put(GooPVersionMaterials.EXPOSED_CUT_COPPER_STAIRS, GetMaterialFromString("EXPOSED_CUT_COPPER_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.EXPOSED_CUT_COPPER, GetMaterialFromString("EXPOSED_CUT_COPPER"));
            versionMaterials.put(GooPVersionMaterials.EXPOSED_COPPER, GetMaterialFromString("EXPOSED_COPPER"));
            versionMaterials.put(GooPVersionMaterials.EXPOSED_CUT_COPPER_SLAB, GetMaterialFromString("EXPOSED_CUT_COPPER_SLAB"));

            versionMaterials.put(GooPVersionMaterials.FLOWERING_AZALEA_LEAVES, GetMaterialFromString("FLOWERING_AZALEA_LEAVES"));
            versionMaterials.put(GooPVersionMaterials.FLOWERING_AZALEA, GetMaterialFromString("FLOWERING_AZALEA"));

            versionMaterials.put(GooPVersionMaterials.GLOW_LICHEN, GetMaterialFromString("GLOW_LICHEN"));
            versionMaterials.put(GooPVersionMaterials.GLOW_INK_SAC, GetMaterialFromString("GLOW_INK_SAC"));
            versionMaterials.put(GooPVersionMaterials.GLOW_SQUID_SPAWN_EGG, GetMaterialFromString("GLOW_SQUID_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.GOAT_SPAWN_EGG, GetMaterialFromString("GOAT_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.GLOW_ITEM_FRAME, GetMaterialFromString("GLOW_ITEM_FRAME"));
            versionMaterials.put(GooPVersionMaterials.GLOW_BERRIES, GetMaterialFromString("GLOW_BERRIES"));
            versionMaterials.put(GooPVersionMaterials.GRAY_CANDLE, GetMaterialFromString("GRAY_CANDLE"));
            versionMaterials.put(GooPVersionMaterials.GREEN_CANDLE, GetMaterialFromString("GREEN_CANDLE"));
            versionMaterials.put(GooPVersionMaterials.GRAY_CANDLE_CAKE, GetMaterialFromString("GRAY_CANDLE_CAKE"));
            versionMaterials.put(GooPVersionMaterials.GREEN_CANDLE_CAKE, GetMaterialFromString("GREEN_CANDLE_CAKE"));

            versionMaterials.put(GooPVersionMaterials.HANGING_ROOTS, GetMaterialFromString("HANGING_ROOTS"));

            versionMaterials.put(GooPVersionMaterials.LIGHT, GetMaterialFromString("LIGHT"));
            versionMaterials.put(GooPVersionMaterials.LIGHT_GRAY_CANDLE, GetMaterialFromString("LIGHT_GRAY_CANDLE"));
            versionMaterials.put(GooPVersionMaterials.LIME_CANDLE, GetMaterialFromString("LIME_CANDLE"));
            versionMaterials.put(GooPVersionMaterials.LIGHTNING_ROD, GetMaterialFromString("LIGHTNING_ROD"));
            versionMaterials.put(GooPVersionMaterials.LIGHT_BLUE_CANDLE, GetMaterialFromString("LIGHT_BLUE_CANDLE"));
            versionMaterials.put(GooPVersionMaterials.LARGE_AMETHYST_BUD, GetMaterialFromString("LARGE_AMETHYST_BUD"));
            versionMaterials.put(GooPVersionMaterials.LAVA_CAULDRON, GetMaterialFromString("LAVA_CAULDRON"));
            versionMaterials.put(GooPVersionMaterials.LIGHT_BLUE_CANDLE_CAKE, GetMaterialFromString("LIGHT_BLUE_CANDLE_CAKE"));
            versionMaterials.put(GooPVersionMaterials.LIME_CANDLE_CAKE, GetMaterialFromString("LIME_CANDLE_CAKE"));
            versionMaterials.put(GooPVersionMaterials.LIGHT_GRAY_CANDLE_CAKE, GetMaterialFromString("LIGHT_GRAY_CANDLE_CAKE"));
            versionMaterials.put(GooPVersionMaterials.INFESTED_DEEPSLATE, GetMaterialFromString("INFESTED_DEEPSLATE"));

            versionMaterials.put(GooPVersionMaterials.MOSS_CARPET, GetMaterialFromString("MOSS_CARPET"));
            versionMaterials.put(GooPVersionMaterials.MOSS_BLOCK, GetMaterialFromString("MOSS_BLOCK"));
            versionMaterials.put(GooPVersionMaterials.MAGENTA_CANDLE, GetMaterialFromString("MAGENTA_CANDLE"));
            versionMaterials.put(GooPVersionMaterials.MEDIUM_AMETHYST_BUD, GetMaterialFromString("MEDIUM_AMETHYST_BUD"));
            versionMaterials.put(GooPVersionMaterials.MAGENTA_CANDLE_CAKE, GetMaterialFromString("MAGENTA_CANDLE_CAKE"));

            versionMaterials.put(GooPVersionMaterials.OXIDIZED_CUT_COPPER_STAIRS, GetMaterialFromString("OXIDIZED_CUT_COPPER_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.OXIDIZED_CUT_COPPER, GetMaterialFromString("OXIDIZED_CUT_COPPER"));
            versionMaterials.put(GooPVersionMaterials.OXIDIZED_COPPER, GetMaterialFromString("OXIDIZED_COPPER"));
            versionMaterials.put(GooPVersionMaterials.OXIDIZED_CUT_COPPER_SLAB, GetMaterialFromString("OXIDIZED_CUT_COPPER_SLAB"));
            versionMaterials.put(GooPVersionMaterials.ORANGE_CANDLE, GetMaterialFromString("ORANGE_CANDLE"));
            versionMaterials.put(GooPVersionMaterials.ORANGE_CANDLE_CAKE, GetMaterialFromString("ORANGE_CANDLE_CAKE"));

            versionMaterials.put(GooPVersionMaterials.POLISHED_DEEPSLATE, GetMaterialFromString("POLISHED_DEEPSLATE"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_DEEPSLATE_WALL, GetMaterialFromString("POLISHED_DEEPSLATE_WALL"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_DEEPSLATE_STAIRS, GetMaterialFromString("POLISHED_DEEPSLATE_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_DEEPSLATE_SLAB, GetMaterialFromString("POLISHED_DEEPSLATE_SLAB"));
            versionMaterials.put(GooPVersionMaterials.PURPLE_CANDLE_CAKE, GetMaterialFromString("PURPLE_CANDLE_CAKE"));
            versionMaterials.put(GooPVersionMaterials.POWDER_SNOW, GetMaterialFromString("POWDER_SNOW"));
            versionMaterials.put(GooPVersionMaterials.POTTED_AZALEA_BUSH, GetMaterialFromString("POTTED_AZALEA_BUSH"));
            versionMaterials.put(GooPVersionMaterials.POTTED_FLOWERING_AZALEA_BUSH, GetMaterialFromString("POTTED_FLOWERING_AZALEA_BUSH"));
            versionMaterials.put(GooPVersionMaterials.POWDER_SNOW_BUCKET, GetMaterialFromString("POWDER_SNOW_BUCKET"));
            versionMaterials.put(GooPVersionMaterials.PINK_CANDLE, GetMaterialFromString("PINK_CANDLE"));
            versionMaterials.put(GooPVersionMaterials.POINTED_DRIPSTONE, GetMaterialFromString("POINTED_DRIPSTONE"));
            versionMaterials.put(GooPVersionMaterials.PURPLE_CANDLE, GetMaterialFromString("PURPLE_CANDLE"));
            versionMaterials.put(GooPVersionMaterials.POWDER_SNOW_CAULDRON, GetMaterialFromString("POWDER_SNOW_CAULDRON"));
            versionMaterials.put(GooPVersionMaterials.PINK_CANDLE_CAKE, GetMaterialFromString("PINK_CANDLE_CAKE"));

            versionMaterials.put(GooPVersionMaterials.ROOTED_DIRT, GetMaterialFromString("ROOTED_DIRT"));
            versionMaterials.put(GooPVersionMaterials.RAW_IRON_BLOCK, GetMaterialFromString("RAW_IRON_BLOCK"));
            versionMaterials.put(GooPVersionMaterials.RAW_COPPER_BLOCK, GetMaterialFromString("RAW_COPPER_BLOCK"));
            versionMaterials.put(GooPVersionMaterials.RAW_GOLD_BLOCK, GetMaterialFromString("RAW_GOLD_BLOCK"));
            versionMaterials.put(GooPVersionMaterials.RAW_IRON, GetMaterialFromString("RAW_IRON"));
            versionMaterials.put(GooPVersionMaterials.RAW_COPPER, GetMaterialFromString("RAW_COPPER"));
            versionMaterials.put(GooPVersionMaterials.RAW_GOLD, GetMaterialFromString("RAW_GOLD"));
            versionMaterials.put(GooPVersionMaterials.RED_CANDLE, GetMaterialFromString("RED_CANDLE"));
            versionMaterials.put(GooPVersionMaterials.RED_CANDLE_CAKE, GetMaterialFromString("RED_CANDLE_CAKE"));

            versionMaterials.put(GooPVersionMaterials.SMALL_DRIPLEAF, GetMaterialFromString("SMALL_DRIPLEAF"));
            versionMaterials.put(GooPVersionMaterials.SMOOTH_BASALT, GetMaterialFromString("SMOOTH_BASALT"));
            versionMaterials.put(GooPVersionMaterials.SPORE_BLOSSOM, GetMaterialFromString("SPORE_BLOSSOM"));
            versionMaterials.put(GooPVersionMaterials.SPYGLASS, GetMaterialFromString("SPYGLASS"));
            versionMaterials.put(GooPVersionMaterials.SCULK_SENSOR, GetMaterialFromString("SCULK_SENSOR"));
            versionMaterials.put(GooPVersionMaterials.SMALL_AMETHYST_BUD, GetMaterialFromString("SMALL_AMETHYST_BUD"));

            versionMaterials.put(GooPVersionMaterials.TUFF, GetMaterialFromString("TUFF"));
            versionMaterials.put(GooPVersionMaterials.TINTED_GLASS, GetMaterialFromString("TINTED_GLASS"));

            versionMaterials.put(GooPVersionMaterials.WEATHERED_CUT_COPPER_SLAB, GetMaterialFromString("WEATHERED_CUT_COPPER_SLAB"));
            versionMaterials.put(GooPVersionMaterials.WAXED_COPPER_BLOCK, GetMaterialFromString("WAXED_COPPER_BLOCK"));
            versionMaterials.put(GooPVersionMaterials.WAXED_EXPOSED_COPPER, GetMaterialFromString("WAXED_EXPOSED_COPPER"));
            versionMaterials.put(GooPVersionMaterials.WAXED_WEATHERED_COPPER, GetMaterialFromString("WAXED_WEATHERED_COPPER"));
            versionMaterials.put(GooPVersionMaterials.WAXED_OXIDIZED_COPPER, GetMaterialFromString("WAXED_OXIDIZED_COPPER"));
            versionMaterials.put(GooPVersionMaterials.WAXED_CUT_COPPER, GetMaterialFromString("WAXED_CUT_COPPER"));
            versionMaterials.put(GooPVersionMaterials.WAXED_EXPOSED_CUT_COPPER, GetMaterialFromString("WAXED_EXPOSED_CUT_COPPER"));
            versionMaterials.put(GooPVersionMaterials.WAXED_WEATHERED_CUT_COPPER, GetMaterialFromString("WAXED_WEATHERED_CUT_COPPER"));
            versionMaterials.put(GooPVersionMaterials.WAXED_OXIDIZED_CUT_COPPER, GetMaterialFromString("WAXED_OXIDIZED_CUT_COPPER"));
            versionMaterials.put(GooPVersionMaterials.WAXED_CUT_COPPER_STAIRS, GetMaterialFromString("WAXED_CUT_COPPER_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.WAXED_EXPOSED_CUT_COPPER_STAIRS, GetMaterialFromString("WAXED_EXPOSED_CUT_COPPER_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.WAXED_WEATHERED_CUT_COPPER_STAIRS, GetMaterialFromString("WAXED_WEATHERED_CUT_COPPER_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.WAXED_OXIDIZED_CUT_COPPER_STAIRS, GetMaterialFromString("WAXED_OXIDIZED_CUT_COPPER_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.WAXED_CUT_COPPER_SLAB, GetMaterialFromString("WAXED_CUT_COPPER_SLAB"));
            versionMaterials.put(GooPVersionMaterials.WAXED_EXPOSED_CUT_COPPER_SLAB, GetMaterialFromString("WAXED_EXPOSED_CUT_COPPER_SLAB"));
            versionMaterials.put(GooPVersionMaterials.WAXED_WEATHERED_CUT_COPPER_SLAB, GetMaterialFromString("WAXED_WEATHERED_CUT_COPPER_SLAB"));
            versionMaterials.put(GooPVersionMaterials.WAXED_OXIDIZED_CUT_COPPER_SLAB, GetMaterialFromString("WAXED_OXIDIZED_CUT_COPPER_SLAB"));
            versionMaterials.put(GooPVersionMaterials.WEATHERED_CUT_COPPER_STAIRS, GetMaterialFromString("WEATHERED_CUT_COPPER_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.WEATHERED_CUT_COPPER, GetMaterialFromString("WEATHERED_CUT_COPPER"));
            versionMaterials.put(GooPVersionMaterials.WEATHERED_COPPER, GetMaterialFromString("WEATHERED_COPPER"));
            versionMaterials.put(GooPVersionMaterials.WHITE_CANDLE, GetMaterialFromString("WHITE_CANDLE"));
            versionMaterials.put(GooPVersionMaterials.WHITE_CANDLE_CAKE, GetMaterialFromString("WHITE_CANDLE_CAKE"));
            versionMaterials.put(GooPVersionMaterials.WATER_CAULDRON, GetMaterialFromString("WATER_CAULDRON"));

            versionMaterials.put(GooPVersionMaterials.YELLOW_CANDLE, GetMaterialFromString("YELLOW_CANDLE"));
            versionMaterials.put(GooPVersionMaterials.YELLOW_CANDLE_CAKE, GetMaterialFromString("YELLOW_CANDLE_CAKE"));
            // YE-OLD-MMO */
        }
        //endregion

        //region Minecraft Version 1.17-
        if (mcVersion < 17.0) {
            versionMaterials.put(GooPVersionMaterials.GRASS_PATH, GetMaterialFromString("GRASS_PATH"));
        }
        //endregion

        //region Minecraft Version 1.18+
        if (mcVersion >= 18.0) {
            //* YE-OLD-MMO
            versionMaterials.put(GooPVersionMaterials.MUSIC_DISC_OTHERSIDE, GetMaterialFromString("MUSIC_DISC_OTHERSIDE"));
            // YE-OLD-MMO */
        }
        //endregion

        //region Minecraft Version 1.19+
        if (mcVersion >= 19.0) {
            versionMaterials.put(GooPVersionMaterials.MUD, GetMaterialFromString("MUD"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_PLANKS, GetMaterialFromString("MANGROVE_PLANKS"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_PROPAGULE, GetMaterialFromString("MANGROVE_PROPAGULE"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_LOG, GetMaterialFromString("MANGROVE_LOG"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_ROOTS, GetMaterialFromString("MANGROVE_ROOTS"));
            versionMaterials.put(GooPVersionMaterials.MUDDY_MANGROVE_ROOTS, GetMaterialFromString("MUDDY_MANGROVE_ROOTS"));
            versionMaterials.put(GooPVersionMaterials.STRIPPED_MANGROVE_LOG, GetMaterialFromString("STRIPPED_MANGROVE_LOG"));
            versionMaterials.put(GooPVersionMaterials.STRIPPED_MANGROVE_WOOD, GetMaterialFromString("STRIPPED_MANGROVE_WOOD"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_WOOD, GetMaterialFromString("MANGROVE_WOOD"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_LEAVES, GetMaterialFromString("MANGROVE_LEAVES"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_SLAB, GetMaterialFromString("MANGROVE_SLAB"));
            versionMaterials.put(GooPVersionMaterials.MUD_BRICK_SLAB, GetMaterialFromString("MUD_BRICK_SLAB"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_FENCE, GetMaterialFromString("MANGROVE_FENCE"));
            versionMaterials.put(GooPVersionMaterials.PACKED_MUD, GetMaterialFromString("PACKED_MUD"));
            versionMaterials.put(GooPVersionMaterials.MUD_BRICKS, GetMaterialFromString("MUD_BRICKS"));
            versionMaterials.put(GooPVersionMaterials.REINFORCED_DEEPSLATE, GetMaterialFromString("REINFORCED_DEEPSLATE"));
            versionMaterials.put(GooPVersionMaterials.MUD_BRICK_STAIRS, GetMaterialFromString("MUD_BRICK_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.SCULK, GetMaterialFromString("SCULK"));
            versionMaterials.put(GooPVersionMaterials.SCULK_VEIN, GetMaterialFromString("SCULK_VEIN"));
            versionMaterials.put(GooPVersionMaterials.SCULK_CATALYST, GetMaterialFromString("SCULK_CATALYST"));
            versionMaterials.put(GooPVersionMaterials.SCULK_SHRIEKER, GetMaterialFromString("SCULK_SHRIEKER"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_STAIRS, GetMaterialFromString("MANGROVE_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.MUD_BRICK_WALL, GetMaterialFromString("MUD_BRICK_WALL"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_BUTTON, GetMaterialFromString("MANGROVE_BUTTON"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_PRESSURE_PLATE, GetMaterialFromString("MANGROVE_PRESSURE_PLATE"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_DOOR, GetMaterialFromString("MANGROVE_DOOR"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_TRAPDOOR, GetMaterialFromString("MANGROVE_TRAPDOOR"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_FENCE_GATE, GetMaterialFromString("MANGROVE_FENCE_GATE"));
            versionMaterials.put(GooPVersionMaterials.OAK_CHEST_BOAT, GetMaterialFromString("OAK_CHEST_BOAT"));
            versionMaterials.put(GooPVersionMaterials.SPRUCE_CHEST_BOAT, GetMaterialFromString("SPRUCE_CHEST_BOAT"));
            versionMaterials.put(GooPVersionMaterials.BIRCH_CHEST_BOAT, GetMaterialFromString("BIRCH_CHEST_BOAT"));
            versionMaterials.put(GooPVersionMaterials.JUNGLE_CHEST_BOAT, GetMaterialFromString("JUNGLE_CHEST_BOAT"));
            versionMaterials.put(GooPVersionMaterials.ACACIA_CHEST_BOAT, GetMaterialFromString("ACACIA_CHEST_BOAT"));
            versionMaterials.put(GooPVersionMaterials.DARK_OAK_CHEST_BOAT, GetMaterialFromString("DARK_OAK_CHEST_BOAT"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_BOAT, GetMaterialFromString("MANGROVE_BOAT"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_CHEST_BOAT, GetMaterialFromString("MANGROVE_CHEST_BOAT"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_SIGN, GetMaterialFromString("MANGROVE_SIGN"));
            versionMaterials.put(GooPVersionMaterials.TADPOLE_BUCKET, GetMaterialFromString("TADPOLE_BUCKET"));
            versionMaterials.put(GooPVersionMaterials.RECOVERY_COMPASS, GetMaterialFromString("RECOVERY_COMPASS"));
            versionMaterials.put(GooPVersionMaterials.ALLAY_SPAWN_EGG, GetMaterialFromString("ALLAY_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.FROG_SPAWN_EGG, GetMaterialFromString("FROG_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.TADPOLE_SPAWN_EGG, GetMaterialFromString("TADPOLE_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.WARDEN_SPAWN_EGG, GetMaterialFromString("WARDEN_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.MUSIC_DISC_5, GetMaterialFromString("MUSIC_DISC_5"));
            versionMaterials.put(GooPVersionMaterials.DISC_FRAGMENT_5, GetMaterialFromString("DISC_FRAGMENT_5"));
            versionMaterials.put(GooPVersionMaterials.GOAT_HORN, GetMaterialFromString("GOAT_HORN"));
            versionMaterials.put(GooPVersionMaterials.OCHRE_FROGLIGHT, GetMaterialFromString("OCHRE_FROGLIGHT"));
            versionMaterials.put(GooPVersionMaterials.VERDANT_FROGLIGHT, GetMaterialFromString("VERDANT_FROGLIGHT"));
            versionMaterials.put(GooPVersionMaterials.PEARLESCENT_FROGLIGHT, GetMaterialFromString("PEARLESCENT_FROGLIGHT"));
            versionMaterials.put(GooPVersionMaterials.FROGSPAWN, GetMaterialFromString("FROGSPAWN"));
            versionMaterials.put(GooPVersionMaterials.ECHO_SHARD, GetMaterialFromString("ECHO_SHARD"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_WALL_SIGN, GetMaterialFromString("MANGROVE_WALL_SIGN"));
            versionMaterials.put(GooPVersionMaterials.POTTED_MANGROVE_PROPAGULE, GetMaterialFromString("POTTED_MANGROVE_PROPAGULE"));
        }
        //endregion

        //region Minecraft Version 1.19.4+
        if (mcVersion >= 19.4) {
            versionMaterials.put(GooPVersionMaterials.CHERRY_PLANKS, GetMaterialFromString("CHERRY_PLANKS"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_PLANKS, GetMaterialFromString("BAMBOO_PLANKS"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_MOSAIC, GetMaterialFromString("BAMBOO_MOSAIC"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_SAPLING, GetMaterialFromString("CHERRY_SAPLING"));
            versionMaterials.put(GooPVersionMaterials.SUSPICIOUS_SAND, GetMaterialFromString("SUSPICIOUS_SAND"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_LOG, GetMaterialFromString("CHERRY_LOG"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_BLOCK, GetMaterialFromString("BAMBOO_BLOCK"));
            versionMaterials.put(GooPVersionMaterials.STRIPPED_CHERRY_LOG, GetMaterialFromString("STRIPPED_CHERRY_LOG"));
            versionMaterials.put(GooPVersionMaterials.STRIPPED_CHERRY_WOOD, GetMaterialFromString("STRIPPED_CHERRY_WOOD"));
            versionMaterials.put(GooPVersionMaterials.STRIPPED_BAMBOO_BLOCK, GetMaterialFromString("STRIPPED_BAMBOO_BLOCK"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_WOOD, GetMaterialFromString("CHERRY_WOOD"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_LEAVES, GetMaterialFromString("CHERRY_LEAVES"));
            versionMaterials.put(GooPVersionMaterials.TORCHFLOWER, GetMaterialFromString("TORCHFLOWER"));
            versionMaterials.put(GooPVersionMaterials.PINK_PETALS, GetMaterialFromString("PINK_PETALS"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_SLAB, GetMaterialFromString("CHERRY_SLAB"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_SLAB, GetMaterialFromString("BAMBOO_SLAB"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_MOSAIC_SLAB, GetMaterialFromString("BAMBOO_MOSAIC_SLAB"));
            versionMaterials.put(GooPVersionMaterials.CHISELED_BOOKSHELF, GetMaterialFromString("CHISELED_BOOKSHELF"));
            versionMaterials.put(GooPVersionMaterials.DECORATED_POT, GetMaterialFromString("DECORATED_POT"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_FENCE, GetMaterialFromString("CHERRY_FENCE"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_FENCE, GetMaterialFromString("BAMBOO_FENCE"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_STAIRS, GetMaterialFromString("CHERRY_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_STAIRS, GetMaterialFromString("BAMBOO_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_MOSAIC_STAIRS, GetMaterialFromString("BAMBOO_MOSAIC_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_BUTTON, GetMaterialFromString("CHERRY_BUTTON"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_BUTTON, GetMaterialFromString("BAMBOO_BUTTON"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_PRESSURE_PLATE, GetMaterialFromString("CHERRY_PRESSURE_PLATE"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_PRESSURE_PLATE, GetMaterialFromString("BAMBOO_PRESSURE_PLATE"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_DOOR, GetMaterialFromString("CHERRY_DOOR"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_DOOR, GetMaterialFromString("BAMBOO_DOOR"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_TRAPDOOR, GetMaterialFromString("CHERRY_TRAPDOOR"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_TRAPDOOR, GetMaterialFromString("BAMBOO_TRAPDOOR"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_FENCE_GATE, GetMaterialFromString("CHERRY_FENCE_GATE"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_FENCE_GATE, GetMaterialFromString("BAMBOO_FENCE_GATE"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_BOAT, GetMaterialFromString("CHERRY_BOAT"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_CHEST_BOAT, GetMaterialFromString("CHERRY_CHEST_BOAT"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_RAFT, GetMaterialFromString("BAMBOO_RAFT"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_CHEST_RAFT, GetMaterialFromString("BAMBOO_CHEST_RAFT"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_SIGN, GetMaterialFromString("CHERRY_SIGN"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_SIGN, GetMaterialFromString("BAMBOO_SIGN"));
            versionMaterials.put(GooPVersionMaterials.OAK_HANGING_SIGN, GetMaterialFromString("OAK_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.SPRUCE_HANGING_SIGN, GetMaterialFromString("SPRUCE_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.BIRCH_HANGING_SIGN, GetMaterialFromString("BIRCH_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.JUNGLE_HANGING_SIGN, GetMaterialFromString("JUNGLE_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.ACACIA_HANGING_SIGN, GetMaterialFromString("ACACIA_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_HANGING_SIGN, GetMaterialFromString("CHERRY_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.DARK_OAK_HANGING_SIGN, GetMaterialFromString("DARK_OAK_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_HANGING_SIGN, GetMaterialFromString("MANGROVE_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_HANGING_SIGN, GetMaterialFromString("BAMBOO_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.CRIMSON_HANGING_SIGN, GetMaterialFromString("CRIMSON_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.WARPED_HANGING_SIGN, GetMaterialFromString("WARPED_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.CAMEL_SPAWN_EGG, GetMaterialFromString("CAMEL_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.ENDER_DRAGON_SPAWN_EGG, GetMaterialFromString("ENDER_DRAGON_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.IRON_GOLEM_SPAWN_EGG, GetMaterialFromString("IRON_GOLEM_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.SNIFFER_SPAWN_EGG, GetMaterialFromString("SNIFFER_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.SNOW_GOLEM_SPAWN_EGG, GetMaterialFromString("SNOW_GOLEM_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.WITHER_SPAWN_EGG, GetMaterialFromString("WITHER_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.PIGLIN_HEAD, GetMaterialFromString("PIGLIN_HEAD"));
            versionMaterials.put(GooPVersionMaterials.TORCHFLOWER_SEEDS, GetMaterialFromString("TORCHFLOWER_SEEDS"));
            versionMaterials.put(GooPVersionMaterials.BRUSH, GetMaterialFromString("BRUSH"));
            versionMaterials.put(GooPVersionMaterials.NETHERITE_UPGRADE_SMITHING_TEMPLATE, GetMaterialFromString("NETHERITE_UPGRADE_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("DUNE_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("COAST_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("WILD_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("WARD_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("EYE_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("VEX_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("TIDE_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("RIB_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_WALL_SIGN, GetMaterialFromString("CHERRY_WALL_SIGN"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_WALL_SIGN, GetMaterialFromString("BAMBOO_WALL_SIGN"));
            versionMaterials.put(GooPVersionMaterials.OAK_WALL_HANGING_SIGN, GetMaterialFromString("OAK_WALL_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.SPRUCE_WALL_HANGING_SIGN, GetMaterialFromString("SPRUCE_WALL_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.BIRCH_WALL_HANGING_SIGN, GetMaterialFromString("BIRCH_WALL_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.ACACIA_WALL_HANGING_SIGN, GetMaterialFromString("ACACIA_WALL_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.CHERRY_WALL_HANGING_SIGN, GetMaterialFromString("CHERRY_WALL_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.JUNGLE_WALL_HANGING_SIGN, GetMaterialFromString("JUNGLE_WALL_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.DARK_OAK_WALL_HANGING_SIGN, GetMaterialFromString("DARK_OAK_WALL_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.MANGROVE_WALL_HANGING_SIGN, GetMaterialFromString("MANGROVE_WALL_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.CRIMSON_WALL_HANGING_SIGN, GetMaterialFromString("CRIMSON_WALL_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.WARPED_WALL_HANGING_SIGN, GetMaterialFromString("WARPED_WALL_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.BAMBOO_WALL_HANGING_SIGN, GetMaterialFromString("BAMBOO_WALL_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.POTTED_TORCHFLOWER, GetMaterialFromString("POTTED_TORCHFLOWER"));
            versionMaterials.put(GooPVersionMaterials.POTTED_CHERRY_SAPLING, GetMaterialFromString("POTTED_CHERRY_SAPLING"));
            versionMaterials.put(GooPVersionMaterials.PIGLIN_WALL_HEAD, GetMaterialFromString("PIGLIN_WALL_HEAD"));
            versionMaterials.put(GooPVersionMaterials.TORCHFLOWER_CROP, GetMaterialFromString("TORCHFLOWER_CROP"));
        }
        //endregion

        //region Minecraft Version 1.20.1+
        if (mcVersion >= 20.1) {
            versionMaterials.put(GooPVersionMaterials.SUSPICIOUS_GRAVEL, GetMaterialFromString("SUSPICIOUS_GRAVEL"));
            versionMaterials.put(GooPVersionMaterials.PITCHER_PLANT, GetMaterialFromString("PITCHER_PLANT"));
            versionMaterials.put(GooPVersionMaterials.SNIFFER_EGG, GetMaterialFromString("SNIFFER_EGG"));
            versionMaterials.put(GooPVersionMaterials.CALIBRATED_SCULK_SENSOR, GetMaterialFromString("CALIBRATED_SCULK_SENSOR"));
            versionMaterials.put(GooPVersionMaterials.PITCHER_POD, GetMaterialFromString("PITCHER_POD"));
            versionMaterials.put(GooPVersionMaterials.MUSIC_DISC_RELIC, GetMaterialFromString("MUSIC_DISC_RELIC"));
            versionMaterials.put(GooPVersionMaterials.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("RAISER_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("HOST_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.ANGLER_POTTERY_SHERD, GetMaterialFromString("ANGLER_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.ARCHER_POTTERY_SHERD, GetMaterialFromString("ARCHER_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.ARMS_UP_POTTERY_SHERD, GetMaterialFromString("ARMS_UP_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.BLADE_POTTERY_SHERD, GetMaterialFromString("BLADE_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.BREWER_POTTERY_SHERD, GetMaterialFromString("BREWER_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.BURN_POTTERY_SHERD, GetMaterialFromString("BURN_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.DANGER_POTTERY_SHERD, GetMaterialFromString("DANGER_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.EXPLORER_POTTERY_SHERD, GetMaterialFromString("EXPLORER_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.FRIEND_POTTERY_SHERD, GetMaterialFromString("FRIEND_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.HEART_POTTERY_SHERD, GetMaterialFromString("HEART_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.HEARTBREAK_POTTERY_SHERD, GetMaterialFromString("HEARTBREAK_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.HOWL_POTTERY_SHERD, GetMaterialFromString("HOWL_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.MINER_POTTERY_SHERD, GetMaterialFromString("MINER_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.MOURNER_POTTERY_SHERD, GetMaterialFromString("MOURNER_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.PLENTY_POTTERY_SHERD, GetMaterialFromString("PLENTY_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.PRIZE_POTTERY_SHERD, GetMaterialFromString("PRIZE_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.SHEAF_POTTERY_SHERD, GetMaterialFromString("SHEAF_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.SHELTER_POTTERY_SHERD, GetMaterialFromString("SHELTER_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.SKULL_POTTERY_SHERD, GetMaterialFromString("SKULL_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.SNORT_POTTERY_SHERD, GetMaterialFromString("SNORT_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.PITCHER_CROP, GetMaterialFromString("PITCHER_CROP"));

            versionMaterials.put(GooPVersionMaterials.POTTERY_SHARD_SKULL, GetMaterialFromString("SKULL_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.POTTERY_SHARD_PRIZE, GetMaterialFromString("PRIZE_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.POTTERY_SHARD_ARMS_UP, GetMaterialFromString("ARMS_UP_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.POTTERY_SHARD_ARCHER, GetMaterialFromString("ARCHER_POTTERY_SHERD"));
        }
        //endregion

        //region Minecraft Version 1.20.6+
        if (mcVersion < 20.1) {
            if (mcVersion >= 19.4) {
                versionMaterials.put(GooPVersionMaterials.POTTERY_SHARD_SKULL, GetMaterialFromString("POTTERY_SHARD_SKULL"));
                versionMaterials.put(GooPVersionMaterials.POTTERY_SHARD_PRIZE, GetMaterialFromString("POTTERY_SHARD_PRIZE"));
                versionMaterials.put(GooPVersionMaterials.POTTERY_SHARD_ARMS_UP, GetMaterialFromString("POTTERY_SHARD_ARMS_UP"));
                versionMaterials.put(GooPVersionMaterials.POTTERY_SHARD_ARCHER, GetMaterialFromString("POTTERY_SHARD_ARCHER"));

                versionMaterials.put(GooPVersionMaterials.SKULL_POTTERY_SHERD, GetMaterialFromString("POTTERY_SHARD_SKULL"));
                versionMaterials.put(GooPVersionMaterials.PRIZE_POTTERY_SHERD, GetMaterialFromString("POTTERY_SHARD_PRIZE"));
                versionMaterials.put(GooPVersionMaterials.ARMS_UP_POTTERY_SHERD, GetMaterialFromString("POTTERY_SHARD_ARMS_UP"));
                versionMaterials.put(GooPVersionMaterials.ARCHER_POTTERY_SHERD, GetMaterialFromString("POTTERY_SHARD_ARCHER"));
            }
        }
        //endregion

        //region Minecraft Version 1.20.6+
        if (mcVersion >= 20.6) {
            versionMaterials.put(GooPVersionMaterials.SHORT_GRASS, GetMaterialFromString("SHORT_GRASS"));
            versionMaterials.put(GooPVersionMaterials.TURTLE_SCUTE, GetMaterialFromString("TURTLE_SCUTE"));
            versionMaterials.put(GooPVersionMaterials.TUFF_SLAB, GetMaterialFromString("TUFF_SLAB"));
            versionMaterials.put(GooPVersionMaterials.TUFF_STAIRS, GetMaterialFromString("TUFF_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.TUFF_WALL, GetMaterialFromString("TUFF_WALL"));
            versionMaterials.put(GooPVersionMaterials.CHISELED_TUFF, GetMaterialFromString("CHISELED_TUFF"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_TUFF, GetMaterialFromString("POLISHED_TUFF"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_TUFF_SLAB, GetMaterialFromString("POLISHED_TUFF_SLAB"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_TUFF_STAIRS, GetMaterialFromString("POLISHED_TUFF_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.POLISHED_TUFF_WALL, GetMaterialFromString("POLISHED_TUFF_WALL"));
            versionMaterials.put(GooPVersionMaterials.TUFF_BRICKS, GetMaterialFromString("TUFF_BRICKS"));
            versionMaterials.put(GooPVersionMaterials.TUFF_BRICK_SLAB, GetMaterialFromString("TUFF_BRICK_SLAB"));
            versionMaterials.put(GooPVersionMaterials.TUFF_BRICK_STAIRS, GetMaterialFromString("TUFF_BRICK_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.TUFF_BRICK_WALL, GetMaterialFromString("TUFF_BRICK_WALL"));
            versionMaterials.put(GooPVersionMaterials.CHISELED_TUFF_BRICKS, GetMaterialFromString("CHISELED_TUFF_BRICKS"));
            versionMaterials.put(GooPVersionMaterials.HEAVY_CORE, GetMaterialFromString("HEAVY_CORE"));
            versionMaterials.put(GooPVersionMaterials.CHISELED_COPPER, GetMaterialFromString("CHISELED_COPPER"));
            versionMaterials.put(GooPVersionMaterials.EXPOSED_CHISELED_COPPER, GetMaterialFromString("EXPOSED_CHISELED_COPPER"));
            versionMaterials.put(GooPVersionMaterials.WEATHERED_CHISELED_COPPER, GetMaterialFromString("WEATHERED_CHISELED_COPPER"));
            versionMaterials.put(GooPVersionMaterials.OXIDIZED_CHISELED_COPPER, GetMaterialFromString("OXIDIZED_CHISELED_COPPER"));
            versionMaterials.put(GooPVersionMaterials.WAXED_CHISELED_COPPER, GetMaterialFromString("WAXED_CHISELED_COPPER"));
            versionMaterials.put(GooPVersionMaterials.WAXED_EXPOSED_CHISELED_COPPER, GetMaterialFromString("WAXED_EXPOSED_CHISELED_COPPER"));
            versionMaterials.put(GooPVersionMaterials.WAXED_WEATHERED_CHISELED_COPPER, GetMaterialFromString("WAXED_WEATHERED_CHISELED_COPPER"));
            versionMaterials.put(GooPVersionMaterials.WAXED_OXIDIZED_CHISELED_COPPER, GetMaterialFromString("WAXED_OXIDIZED_CHISELED_COPPER"));
            versionMaterials.put(GooPVersionMaterials.COPPER_DOOR, GetMaterialFromString("COPPER_DOOR"));
            versionMaterials.put(GooPVersionMaterials.EXPOSED_COPPER_DOOR, GetMaterialFromString("EXPOSED_COPPER_DOOR"));
            versionMaterials.put(GooPVersionMaterials.WEATHERED_COPPER_DOOR, GetMaterialFromString("WEATHERED_COPPER_DOOR"));
            versionMaterials.put(GooPVersionMaterials.OXIDIZED_COPPER_DOOR, GetMaterialFromString("OXIDIZED_COPPER_DOOR"));
            versionMaterials.put(GooPVersionMaterials.WAXED_COPPER_DOOR, GetMaterialFromString("WAXED_COPPER_DOOR"));
            versionMaterials.put(GooPVersionMaterials.WAXED_EXPOSED_COPPER_DOOR, GetMaterialFromString("WAXED_EXPOSED_COPPER_DOOR"));
            versionMaterials.put(GooPVersionMaterials.WAXED_WEATHERED_COPPER_DOOR, GetMaterialFromString("WAXED_WEATHERED_COPPER_DOOR"));
            versionMaterials.put(GooPVersionMaterials.WAXED_OXIDIZED_COPPER_DOOR, GetMaterialFromString("WAXED_OXIDIZED_COPPER_DOOR"));
            versionMaterials.put(GooPVersionMaterials.COPPER_TRAPDOOR, GetMaterialFromString("COPPER_TRAPDOOR"));
            versionMaterials.put(GooPVersionMaterials.EXPOSED_COPPER_TRAPDOOR, GetMaterialFromString("EXPOSED_COPPER_TRAPDOOR"));
            versionMaterials.put(GooPVersionMaterials.WEATHERED_COPPER_TRAPDOOR, GetMaterialFromString("WEATHERED_COPPER_TRAPDOOR"));
            versionMaterials.put(GooPVersionMaterials.OXIDIZED_COPPER_TRAPDOOR, GetMaterialFromString("OXIDIZED_COPPER_TRAPDOOR"));
            versionMaterials.put(GooPVersionMaterials.WAXED_COPPER_TRAPDOOR, GetMaterialFromString("WAXED_COPPER_TRAPDOOR"));
            versionMaterials.put(GooPVersionMaterials.WAXED_EXPOSED_COPPER_TRAPDOOR, GetMaterialFromString("WAXED_EXPOSED_COPPER_TRAPDOOR"));
            versionMaterials.put(GooPVersionMaterials.WAXED_WEATHERED_COPPER_TRAPDOOR, GetMaterialFromString("WAXED_WEATHERED_COPPER_TRAPDOOR"));
            versionMaterials.put(GooPVersionMaterials.WAXED_OXIDIZED_COPPER_TRAPDOOR, GetMaterialFromString("WAXED_OXIDIZED_COPPER_TRAPDOOR"));
            versionMaterials.put(GooPVersionMaterials.ARMADILLO_SCUTE, GetMaterialFromString("ARMADILLO_SCUTE"));
            versionMaterials.put(GooPVersionMaterials.WOLF_ARMOR, GetMaterialFromString("WOLF_ARMOR"));
            versionMaterials.put(GooPVersionMaterials.CRAFTER, GetMaterialFromString("CRAFTER"));
            versionMaterials.put(GooPVersionMaterials.ARMADILLO_SPAWN_EGG, GetMaterialFromString("ARMADILLO_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.BOGGED_SPAWN_EGG, GetMaterialFromString("BOGGED_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.BREEZE_SPAWN_EGG, GetMaterialFromString("BREEZE_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.WIND_CHARGE, GetMaterialFromString("WIND_CHARGE"));
            versionMaterials.put(GooPVersionMaterials.MACE, GetMaterialFromString("MACE"));
            versionMaterials.put(GooPVersionMaterials.FLOW_BANNER_PATTERN, GetMaterialFromString("FLOW_BANNER_PATTERN"));
            versionMaterials.put(GooPVersionMaterials.GUSTER_BANNER_PATTERN, GetMaterialFromString("GUSTER_BANNER_PATTERN"));
            versionMaterials.put(GooPVersionMaterials.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("FLOW_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, GetMaterialFromString("BOLT_ARMOR_TRIM_SMITHING_TEMPLATE"));
            versionMaterials.put(GooPVersionMaterials.FLOW_POTTERY_SHERD, GetMaterialFromString("FLOW_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.GUSTER_POTTERY_SHERD, GetMaterialFromString("GUSTER_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.SCRAPE_POTTERY_SHERD, GetMaterialFromString("SCRAPE_POTTERY_SHERD"));
            versionMaterials.put(GooPVersionMaterials.COPPER_GRATE, GetMaterialFromString("COPPER_GRATE"));
            versionMaterials.put(GooPVersionMaterials.EXPOSED_COPPER_GRATE, GetMaterialFromString("EXPOSED_COPPER_GRATE"));
            versionMaterials.put(GooPVersionMaterials.WEATHERED_COPPER_GRATE, GetMaterialFromString("WEATHERED_COPPER_GRATE"));
            versionMaterials.put(GooPVersionMaterials.OXIDIZED_COPPER_GRATE, GetMaterialFromString("OXIDIZED_COPPER_GRATE"));
            versionMaterials.put(GooPVersionMaterials.WAXED_COPPER_GRATE, GetMaterialFromString("WAXED_COPPER_GRATE"));
            versionMaterials.put(GooPVersionMaterials.WAXED_EXPOSED_COPPER_GRATE, GetMaterialFromString("WAXED_EXPOSED_COPPER_GRATE"));
            versionMaterials.put(GooPVersionMaterials.WAXED_WEATHERED_COPPER_GRATE, GetMaterialFromString("WAXED_WEATHERED_COPPER_GRATE"));
            versionMaterials.put(GooPVersionMaterials.WAXED_OXIDIZED_COPPER_GRATE, GetMaterialFromString("WAXED_OXIDIZED_COPPER_GRATE"));
            versionMaterials.put(GooPVersionMaterials.COPPER_BULB, GetMaterialFromString("COPPER_BULB"));
            versionMaterials.put(GooPVersionMaterials.EXPOSED_COPPER_BULB, GetMaterialFromString("EXPOSED_COPPER_BULB"));
            versionMaterials.put(GooPVersionMaterials.WEATHERED_COPPER_BULB, GetMaterialFromString("WEATHERED_COPPER_BULB"));
            versionMaterials.put(GooPVersionMaterials.OXIDIZED_COPPER_BULB, GetMaterialFromString("OXIDIZED_COPPER_BULB"));
            versionMaterials.put(GooPVersionMaterials.WAXED_COPPER_BULB, GetMaterialFromString("WAXED_COPPER_BULB"));
            versionMaterials.put(GooPVersionMaterials.WAXED_EXPOSED_COPPER_BULB, GetMaterialFromString("WAXED_EXPOSED_COPPER_BULB"));
            versionMaterials.put(GooPVersionMaterials.WAXED_WEATHERED_COPPER_BULB, GetMaterialFromString("WAXED_WEATHERED_COPPER_BULB"));
            versionMaterials.put(GooPVersionMaterials.WAXED_OXIDIZED_COPPER_BULB, GetMaterialFromString("WAXED_OXIDIZED_COPPER_BULB"));
            versionMaterials.put(GooPVersionMaterials.TRIAL_SPAWNER, GetMaterialFromString("TRIAL_SPAWNER"));
            versionMaterials.put(GooPVersionMaterials.TRIAL_KEY, GetMaterialFromString("TRIAL_KEY"));
            versionMaterials.put(GooPVersionMaterials.OMINOUS_TRIAL_KEY, GetMaterialFromString("OMINOUS_TRIAL_KEY"));
            versionMaterials.put(GooPVersionMaterials.VAULT, GetMaterialFromString("VAULT"));
            versionMaterials.put(GooPVersionMaterials.OMINOUS_BOTTLE, GetMaterialFromString("OMINOUS_BOTTLE"));
            versionMaterials.put(GooPVersionMaterials.BREEZE_ROD, GetMaterialFromString("BREEZE_ROD"));

            versionMaterials.put(GooPVersionMaterials.SCUTE, GetMaterialFromString("TURTLE_SCUTE"));
            versionMaterials.put(GooPVersionMaterials.GRASS, GetMaterialFromString("SHORT_GRASS"));
        }
        //endregion

        //region Minecraft Version 1.20.6+
        if (mcVersion < 20.6) {
            versionMaterials.put(GooPVersionMaterials.SCUTE, GetMaterialFromString("SCUTE"));
            versionMaterials.put(GooPVersionMaterials.GRASS, GetMaterialFromString("GRASS"));

            versionMaterials.put(GooPVersionMaterials.TURTLE_SCUTE, GetMaterialFromString("SCUTE"));
            versionMaterials.put(GooPVersionMaterials.SHORT_GRASS, GetMaterialFromString("GRASS"));
        }
        //endregion

        //region Minecraft Version 1.21.1+
        if (mcVersion >= 21.1) {
            versionMaterials.put(GooPVersionMaterials.MUSIC_DISC_CREATOR, GetMaterialFromString("MUSIC_DISC_CREATOR"));
            versionMaterials.put(GooPVersionMaterials.MUSIC_DISC_CREATOR_MUSIC_BOX, GetMaterialFromString("MUSIC_DISC_CREATOR_MUSIC_BOX"));
            versionMaterials.put(GooPVersionMaterials.MUSIC_DISC_PRECIPICE, GetMaterialFromString("MUSIC_DISC_PRECIPICE"));
        }
        //endregion

        //region Minecraft Version 1.21.3+
        if (mcVersion >= 21.3) {
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_PLANKS, GetMaterialFromString("PALE_OAK_PLANKS"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_SAPLING, GetMaterialFromString("PALE_OAK_SAPLING"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_LOG, GetMaterialFromString("PALE_OAK_LOG"));
            versionMaterials.put(GooPVersionMaterials.STRIPPED_PALE_OAK_LOG, GetMaterialFromString("STRIPPED_PALE_OAK_LOG"));
            versionMaterials.put(GooPVersionMaterials.STRIPPED_PALE_OAK_WOOD, GetMaterialFromString("STRIPPED_PALE_OAK_WOOD"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_WOOD, GetMaterialFromString("PALE_OAK_WOOD"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_LEAVES, GetMaterialFromString("PALE_OAK_LEAVES"));
            versionMaterials.put(GooPVersionMaterials.PALE_MOSS_CARPET, GetMaterialFromString("PALE_MOSS_CARPET"));
            versionMaterials.put(GooPVersionMaterials.PALE_HANGING_MOSS, GetMaterialFromString("PALE_HANGING_MOSS"));
            versionMaterials.put(GooPVersionMaterials.PALE_MOSS_BLOCK, GetMaterialFromString("PALE_MOSS_BLOCK"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_SLAB, GetMaterialFromString("PALE_OAK_SLAB"));
            versionMaterials.put(GooPVersionMaterials.CREAKING_HEART, GetMaterialFromString("CREAKING_HEART"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_FENCE, GetMaterialFromString("PALE_OAK_FENCE"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_STAIRS, GetMaterialFromString("PALE_OAK_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_BUTTON, GetMaterialFromString("PALE_OAK_BUTTON"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_PRESSURE_PLATE, GetMaterialFromString("PALE_OAK_PRESSURE_PLATE"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_DOOR, GetMaterialFromString("PALE_OAK_DOOR"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_TRAPDOOR, GetMaterialFromString("PALE_OAK_TRAPDOOR"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_FENCE_GATE, GetMaterialFromString("PALE_OAK_FENCE_GATE"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_BOAT, GetMaterialFromString("PALE_OAK_BOAT"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_CHEST_BOAT, GetMaterialFromString("PALE_OAK_CHEST_BOAT"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_SIGN, GetMaterialFromString("PALE_OAK_SIGN"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_HANGING_SIGN, GetMaterialFromString("PALE_OAK_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.WHITE_BUNDLE, GetMaterialFromString("WHITE_BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.ORANGE_BUNDLE, GetMaterialFromString("ORANGE_BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.MAGENTA_BUNDLE, GetMaterialFromString("MAGENTA_BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.LIGHT_BLUE_BUNDLE, GetMaterialFromString("LIGHT_BLUE_BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.YELLOW_BUNDLE, GetMaterialFromString("YELLOW_BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.LIME_BUNDLE, GetMaterialFromString("LIME_BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.PINK_BUNDLE, GetMaterialFromString("PINK_BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.GRAY_BUNDLE, GetMaterialFromString("GRAY_BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.LIGHT_GRAY_BUNDLE, GetMaterialFromString("LIGHT_GRAY_BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.CYAN_BUNDLE, GetMaterialFromString("CYAN_BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.PURPLE_BUNDLE, GetMaterialFromString("PURPLE_BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.BLUE_BUNDLE, GetMaterialFromString("BLUE_BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.BROWN_BUNDLE, GetMaterialFromString("BROWN_BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.GREEN_BUNDLE, GetMaterialFromString("GREEN_BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.RED_BUNDLE, GetMaterialFromString("RED_BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.BLACK_BUNDLE, GetMaterialFromString("BLACK_BUNDLE"));
            versionMaterials.put(GooPVersionMaterials.CREAKING_SPAWN_EGG, GetMaterialFromString("CREAKING_SPAWN_EGG"));
            versionMaterials.put(GooPVersionMaterials.FIELD_MASONED_BANNER_PATTERN, GetMaterialFromString("FIELD_MASONED_BANNER_PATTERN"));
            versionMaterials.put(GooPVersionMaterials.BORDURE_INDENTED_BANNER_PATTERN, GetMaterialFromString("BORDURE_INDENTED_BANNER_PATTERN"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_WALL_SIGN, GetMaterialFromString("PALE_OAK_WALL_SIGN"));
            versionMaterials.put(GooPVersionMaterials.PALE_OAK_WALL_HANGING_SIGN, GetMaterialFromString("PALE_OAK_WALL_HANGING_SIGN"));
            versionMaterials.put(GooPVersionMaterials.POTTED_PALE_OAK_SAPLING, GetMaterialFromString("POTTED_PALE_OAK_SAPLING"));
        }
        //endregion

        //region Minecraft Version 1.21.4+
        if (mcVersion >= 21.4) {
            versionMaterials.put(GooPVersionMaterials.OPEN_EYEBLOSSOM, GetMaterialFromString("OPEN_EYEBLOSSOM"));
            versionMaterials.put(GooPVersionMaterials.CLOSED_EYEBLOSSOM, GetMaterialFromString("CLOSED_EYEBLOSSOM"));
            versionMaterials.put(GooPVersionMaterials.RESIN_CLUMP, GetMaterialFromString("RESIN_CLUMP"));
            versionMaterials.put(GooPVersionMaterials.RESIN_BLOCK, GetMaterialFromString("RESIN_BLOCK"));
            versionMaterials.put(GooPVersionMaterials.RESIN_BRICKS, GetMaterialFromString("RESIN_BRICKS"));
            versionMaterials.put(GooPVersionMaterials.RESIN_BRICK_STAIRS, GetMaterialFromString("RESIN_BRICK_STAIRS"));
            versionMaterials.put(GooPVersionMaterials.RESIN_BRICK_SLAB, GetMaterialFromString("RESIN_BRICK_SLAB"));
            versionMaterials.put(GooPVersionMaterials.RESIN_BRICK_WALL, GetMaterialFromString("RESIN_BRICK_WALL"));
            versionMaterials.put(GooPVersionMaterials.CHISELED_RESIN_BRICKS, GetMaterialFromString("CHISELED_RESIN_BRICKS"));
            versionMaterials.put(GooPVersionMaterials.RESIN_BRICK, GetMaterialFromString("RESIN_BRICK"));
            versionMaterials.put(GooPVersionMaterials.POTTED_OPEN_EYEBLOSSOM, GetMaterialFromString("POTTED_OPEN_EYEBLOSSOM"));
            versionMaterials.put(GooPVersionMaterials.POTTED_CLOSED_EYEBLOSSOM, GetMaterialFromString("POTTED_CLOSED_EYEBLOSSOM"));
        }
        //endregion

        // Finally, fill the holes with null material VOID_AIR
        for (GooPVersionMaterials gvm : GooPVersionMaterials.values()) {

            // If its not contained
            if (!versionMaterials.containsKey(gvm)) {

                // Add it as VOID_AIR
                versionMaterials.put(gvm, GetMaterialFromString("VOID_AIR"));
            }
        }
        //endregion

        //region Entity Types
        //region Minecraft 1.13-
        if (mcVersion <= 13.0) {
            versionEntityTypes.put(GooPVersionEntities.LINGERING_POTION, GetEntityTypeFromString("LINGERING_POTION"));
        }
        //endregion

        //region Minecraft 1.14+
        if (mcVersion >= 14.0) {
            versionEntityTypes.put(GooPVersionEntities.CAT, GetEntityTypeFromString("CAT"));
            versionEntityTypes.put(GooPVersionEntities.PANDA, GetEntityTypeFromString("PANDA"));
            versionEntityTypes.put(GooPVersionEntities.PILLAGER, GetEntityTypeFromString("PILLAGER"));
            versionEntityTypes.put(GooPVersionEntities.RAVAGER, GetEntityTypeFromString("RAVAGER"));
            versionEntityTypes.put(GooPVersionEntities.TRADER_LLAMA, GetEntityTypeFromString("TRADER_LLAMA"));
            versionEntityTypes.put(GooPVersionEntities.WANDERING_TRADER, GetEntityTypeFromString("WANDERING_TRADER"));
            versionEntityTypes.put(GooPVersionEntities.FOX, GetEntityTypeFromString("FOX"));
        }
        //endregion

        //region Minecraft 1.15+
        if (mcVersion >= 15.0) {
            versionEntityTypes.put(GooPVersionEntities.BEE, GetEntityTypeFromString("BEE"));
        }
        //endregion

        //region Minecraft 1.16+
        if (mcVersion >= 16.0) {
            versionEntityTypes.put(GooPVersionEntities.ZOMBIFIED_PIGLIN, GetEntityTypeFromString("ZOMBIFIED_PIGLIN"));
            versionEntityTypes.put(GooPVersionEntities.HOGLIN, GetEntityTypeFromString("HOGLIN"));
            versionEntityTypes.put(GooPVersionEntities.PIGLIN, GetEntityTypeFromString("PIGLIN"));
            versionEntityTypes.put(GooPVersionEntities.STRIDER, GetEntityTypeFromString("STRIDER"));
            versionEntityTypes.put(GooPVersionEntities.ZOGLIN, GetEntityTypeFromString("ZOGLIN"));

            versionEntityTypes.put(GooPVersionEntities.PIG_ZOMBIE, GetEntityTypeFromString("ZOMBIFIED_PIGLIN"));
        }
        //endregion

        //region Minecraft 1.16-
        if (mcVersion < 16.0) {
            versionEntityTypes.put(GooPVersionEntities.PIG_ZOMBIE, GetEntityTypeFromString("PIG_ZOMBIE"));
            versionEntityTypes.put(GooPVersionEntities.ZOMBIFIED_PIGLIN, GetEntityTypeFromString("PIG_ZOMBIE"));
        }
        //endregion

        //region Minecraft 1.17+
        if (mcVersion >= 17.0) {
            //* YE-OLD-MMO
            versionEntityTypes.put(GooPVersionEntities.AXOLOTL, GetEntityTypeFromString("AXOLOTL"));
            versionEntityTypes.put(GooPVersionEntities.GLOW_ITEM_FRAME, GetEntityTypeFromString("GLOW_ITEM_FRAME"));
            versionEntityTypes.put(GooPVersionEntities.GLOW_SQUID, GetEntityTypeFromString("GLOW_SQUID"));
            versionEntityTypes.put(GooPVersionEntities.GOAT, GetEntityTypeFromString("GOAT"));
            versionEntityTypes.put(GooPVersionEntities.MARKER, GetEntityTypeFromString("MARKER"));
            // YE-OLD-MMO */
        }
        //endregion

        //region Minecraft 1.17+
        if (mcVersion >= 17.0) {
            //* YE-OLD-MMO
            versionEntityTypes.put(GooPVersionEntities.AXOLOTL, GetEntityTypeFromString("AXOLOTL"));
            versionEntityTypes.put(GooPVersionEntities.GLOW_ITEM_FRAME, GetEntityTypeFromString("GLOW_ITEM_FRAME"));
            versionEntityTypes.put(GooPVersionEntities.GLOW_SQUID, GetEntityTypeFromString("GLOW_SQUID"));
            versionEntityTypes.put(GooPVersionEntities.GOAT, GetEntityTypeFromString("GOAT"));
            versionEntityTypes.put(GooPVersionEntities.MARKER, GetEntityTypeFromString("MARKER"));
            // YE-OLD-MMO */
        }
        //endregion

        //region Minecraft 1.19+
        if (mcVersion >= 19.0) {
            versionEntityTypes.put(GooPVersionEntities.ALLAY, GetEntityTypeFromString("ALLAY"));
            versionEntityTypes.put(GooPVersionEntities.FROG, GetEntityTypeFromString("FROG"));
            versionEntityTypes.put(GooPVersionEntities.TADPOLE, GetEntityTypeFromString("TADPOLE"));
            versionEntityTypes.put(GooPVersionEntities.WARDEN, GetEntityTypeFromString("WARDEN"));
        }
        //endregion

        //region Minecraft 1.19.4+
        if (mcVersion >= 19.4) {
            versionEntityTypes.put(GooPVersionEntities.CAMEL, GetEntityTypeFromString("CAMEL"));
            versionEntityTypes.put(GooPVersionEntities.BLOCK_DISPLAY, GetEntityTypeFromString("BLOCK_DISPLAY"));
            versionEntityTypes.put(GooPVersionEntities.INTERACTION, GetEntityTypeFromString("INTERACTION"));
            versionEntityTypes.put(GooPVersionEntities.ITEM_DISPLAY, GetEntityTypeFromString("ITEM_DISPLAY"));
            versionEntityTypes.put(GooPVersionEntities.SNIFFER, GetEntityTypeFromString("SNIFFER"));
            versionEntityTypes.put(GooPVersionEntities.TEXT_DISPLAY, GetEntityTypeFromString("TEXT_DISPLAY"));
        }
        //endregion

        //region Minecraft 1.20.6+
        if (mcVersion >= 20.6) {
            versionEntityTypes.put(GooPVersionEntities.ITEM, GetEntityTypeFromString("ITEM"));
            versionEntityTypes.put(GooPVersionEntities.LEASH_KNOT, GetEntityTypeFromString("LEASH_KNOT"));
            versionEntityTypes.put(GooPVersionEntities.EYE_OF_ENDER, GetEntityTypeFromString("EYE_OF_ENDER"));
            versionEntityTypes.put(GooPVersionEntities.POTION, GetEntityTypeFromString("POTION"));
            versionEntityTypes.put(GooPVersionEntities.EXPERIENCE_BOTTLE, GetEntityTypeFromString("EXPERIENCE_BOTTLE"));
            versionEntityTypes.put(GooPVersionEntities.TNT, GetEntityTypeFromString("TNT"));
            versionEntityTypes.put(GooPVersionEntities.FIREWORK_ROCKET, GetEntityTypeFromString("FIREWORK_ROCKET"));
            versionEntityTypes.put(GooPVersionEntities.COMMAND_BLOCK_MINECART, GetEntityTypeFromString("COMMAND_BLOCK_MINECART"));
            versionEntityTypes.put(GooPVersionEntities.CHEST_MINECART, GetEntityTypeFromString("CHEST_MINECART"));
            versionEntityTypes.put(GooPVersionEntities.FURNACE_MINECART, GetEntityTypeFromString("FURNACE_MINECART"));
            versionEntityTypes.put(GooPVersionEntities.TNT_MINECART, GetEntityTypeFromString("TNT_MINECART"));
            versionEntityTypes.put(GooPVersionEntities.HOPPER_MINECART, GetEntityTypeFromString("HOPPER_MINECART"));
            versionEntityTypes.put(GooPVersionEntities.SPAWNER_MINECART, GetEntityTypeFromString("SPAWNER_MINECART"));
            versionEntityTypes.put(GooPVersionEntities.MOOSHROOM, GetEntityTypeFromString("MOOSHROOM"));
            versionEntityTypes.put(GooPVersionEntities.SNOW_GOLEM, GetEntityTypeFromString("SNOW_GOLEM"));
            versionEntityTypes.put(GooPVersionEntities.END_CRYSTAL, GetEntityTypeFromString("END_CRYSTAL"));
            versionEntityTypes.put(GooPVersionEntities.FISHING_BOBBER, GetEntityTypeFromString("FISHING_BOBBER"));
            versionEntityTypes.put(GooPVersionEntities.LIGHTNING_BOLT, GetEntityTypeFromString("LIGHTNING_BOLT"));
            versionEntityTypes.put(GooPVersionEntities.BREEZE, GetEntityTypeFromString("BREEZE"));
            versionEntityTypes.put(GooPVersionEntities.WIND_CHARGE, GetEntityTypeFromString("WIND_CHARGE"));
            versionEntityTypes.put(GooPVersionEntities.BREEZE_WIND_CHARGE, GetEntityTypeFromString("BREEZE_WIND_CHARGE"));
            versionEntityTypes.put(GooPVersionEntities.ARMADILLO, GetEntityTypeFromString("ARMADILLO"));
            versionEntityTypes.put(GooPVersionEntities.BOGGED, GetEntityTypeFromString("BOGGED"));
            versionEntityTypes.put(GooPVersionEntities.OMINOUS_ITEM_SPAWNER, GetEntityTypeFromString("OMINOUS_ITEM_SPAWNER"));

            versionEntityTypes.put(GooPVersionEntities.LIGHTNING, GetEntityTypeFromString("LIGHTNING_BOLT"));
            versionEntityTypes.put(GooPVersionEntities.FISHING_HOOK, GetEntityTypeFromString("FISHING_BOBBER"));
            versionEntityTypes.put(GooPVersionEntities.ENDER_CRYSTAL, GetEntityTypeFromString("END_CRYSTAL"));
            versionEntityTypes.put(GooPVersionEntities.SNOWMAN, GetEntityTypeFromString("SNOW_GOLEM"));
            versionEntityTypes.put(GooPVersionEntities.MUSHROOM_COW, GetEntityTypeFromString("MOOSHROOM"));
            versionEntityTypes.put(GooPVersionEntities.MINECART_MOB_SPAWNER, GetEntityTypeFromString("SPAWNER_MINECART"));
            versionEntityTypes.put(GooPVersionEntities.MINECART_HOPPER, GetEntityTypeFromString("HOPPER_MINECART"));
            versionEntityTypes.put(GooPVersionEntities.MINECART_TNT, GetEntityTypeFromString("TNT_MINECART"));
            versionEntityTypes.put(GooPVersionEntities.MINECART_FURNACE, GetEntityTypeFromString("FURNACE_MINECART"));
            versionEntityTypes.put(GooPVersionEntities.MINECART_CHEST, GetEntityTypeFromString("CHEST_MINECART"));
            versionEntityTypes.put(GooPVersionEntities.MINECART_COMMAND, GetEntityTypeFromString("COMMAND_BLOCK_MINECART"));
            versionEntityTypes.put(GooPVersionEntities.FIREWORK, GetEntityTypeFromString("FIREWORK_ROCKET"));
            versionEntityTypes.put(GooPVersionEntities.PRIMED_TNT, GetEntityTypeFromString("TNT"));
            versionEntityTypes.put(GooPVersionEntities.THROWN_EXP_BOTTLE, GetEntityTypeFromString("EXPERIENCE_BOTTLE"));
            versionEntityTypes.put(GooPVersionEntities.SPLASH_POTION, GetEntityTypeFromString("POTION"));
            versionEntityTypes.put(GooPVersionEntities.ENDER_SIGNAL, GetEntityTypeFromString("EYE_OF_ENDER"));
            versionEntityTypes.put(GooPVersionEntities.LEASH_HITCH, GetEntityTypeFromString("LEASH_KNOT"));
            versionEntityTypes.put(GooPVersionEntities.DROPPED_ITEM, GetEntityTypeFromString("ITEM"));
        }
        //endregion

        //region Minecraft 1.20.6-
        if (mcVersion < 20.6) {
            versionEntityTypes.put(GooPVersionEntities.LIGHTNING, GetEntityTypeFromString("LIGHTNING"));
            versionEntityTypes.put(GooPVersionEntities.FISHING_HOOK, GetEntityTypeFromString("FISHING_HOOK"));
            versionEntityTypes.put(GooPVersionEntities.ENDER_CRYSTAL, GetEntityTypeFromString("ENDER_CRYSTAL"));
            versionEntityTypes.put(GooPVersionEntities.SNOWMAN, GetEntityTypeFromString("SNOWMAN"));
            versionEntityTypes.put(GooPVersionEntities.MUSHROOM_COW, GetEntityTypeFromString("MUSHROOM_COW"));
            versionEntityTypes.put(GooPVersionEntities.MINECART_MOB_SPAWNER, GetEntityTypeFromString("MINECART_MOB_SPAWNER"));
            versionEntityTypes.put(GooPVersionEntities.MINECART_HOPPER, GetEntityTypeFromString("MINECART_HOPPER"));
            versionEntityTypes.put(GooPVersionEntities.MINECART_TNT, GetEntityTypeFromString("MINECART_TNT"));
            versionEntityTypes.put(GooPVersionEntities.MINECART_FURNACE, GetEntityTypeFromString("MINECART_FURNACE"));
            versionEntityTypes.put(GooPVersionEntities.MINECART_CHEST, GetEntityTypeFromString("MINECART_CHEST"));
            versionEntityTypes.put(GooPVersionEntities.MINECART_COMMAND, GetEntityTypeFromString("MINECART_COMMAND"));
            versionEntityTypes.put(GooPVersionEntities.FIREWORK, GetEntityTypeFromString("FIREWORK"));
            versionEntityTypes.put(GooPVersionEntities.PRIMED_TNT, GetEntityTypeFromString("PRIMED_TNT"));
            versionEntityTypes.put(GooPVersionEntities.THROWN_EXP_BOTTLE, GetEntityTypeFromString("THROWN_EXP_BOTTLE"));
            versionEntityTypes.put(GooPVersionEntities.SPLASH_POTION, GetEntityTypeFromString("SPLASH_POTION"));
            versionEntityTypes.put(GooPVersionEntities.ENDER_SIGNAL, GetEntityTypeFromString("ENDER_SIGNAL"));
            versionEntityTypes.put(GooPVersionEntities.LEASH_HITCH, GetEntityTypeFromString("LEASH_HITCH"));
            versionEntityTypes.put(GooPVersionEntities.DROPPED_ITEM, GetEntityTypeFromString("DROPPED_ITEM"));

            versionEntityTypes.put(GooPVersionEntities.ITEM, GetEntityTypeFromString("DROPPED_ITEM"));
            versionEntityTypes.put(GooPVersionEntities.LEASH_KNOT, GetEntityTypeFromString("LEASH_HITCH"));
            versionEntityTypes.put(GooPVersionEntities.EYE_OF_ENDER, GetEntityTypeFromString("ENDER_SIGNAL"));
            versionEntityTypes.put(GooPVersionEntities.POTION, GetEntityTypeFromString("SPLASH_POTION"));
            versionEntityTypes.put(GooPVersionEntities.EXPERIENCE_BOTTLE, GetEntityTypeFromString("THROWN_EXP_BOTTLE"));
            versionEntityTypes.put(GooPVersionEntities.TNT, GetEntityTypeFromString("PRIMED_TNT"));
            versionEntityTypes.put(GooPVersionEntities.FIREWORK_ROCKET, GetEntityTypeFromString("FIREWORK"));
            versionEntityTypes.put(GooPVersionEntities.COMMAND_BLOCK_MINECART, GetEntityTypeFromString("MINECART_COMMAND"));
            versionEntityTypes.put(GooPVersionEntities.CHEST_MINECART, GetEntityTypeFromString("MINECART_CHEST"));
            versionEntityTypes.put(GooPVersionEntities.FURNACE_MINECART, GetEntityTypeFromString("MINECART_FURNACE"));
            versionEntityTypes.put(GooPVersionEntities.TNT_MINECART, GetEntityTypeFromString("MINECART_TNT"));
            versionEntityTypes.put(GooPVersionEntities.HOPPER_MINECART, GetEntityTypeFromString("MINECART_HOPPER"));
            versionEntityTypes.put(GooPVersionEntities.SPAWNER_MINECART, GetEntityTypeFromString("MINECART_MOB_SPAWNER"));
            versionEntityTypes.put(GooPVersionEntities.MOOSHROOM, GetEntityTypeFromString("MUSHROOM_COW"));
            versionEntityTypes.put(GooPVersionEntities.SNOW_GOLEM, GetEntityTypeFromString("SNOWMAN"));
            versionEntityTypes.put(GooPVersionEntities.END_CRYSTAL, GetEntityTypeFromString("ENDER_CRYSTAL"));
            versionEntityTypes.put(GooPVersionEntities.FISHING_BOBBER, GetEntityTypeFromString("FISHING_HOOK"));
            versionEntityTypes.put(GooPVersionEntities.LIGHTNING_BOLT, GetEntityTypeFromString("LIGHTNING"));
        }
        //endregion

        //region Minecraft 1.21.3+
        if (mcVersion >= 21.3) {
            versionEntityTypes.put(GooPVersionEntities.OAK_BOAT, GetEntityTypeFromString("OAK_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.OAK_CHEST_BOAT, GetEntityTypeFromString("OAK_CHEST_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.ACACIA_BOAT, GetEntityTypeFromString("ACACIA_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.ACACIA_CHEST_BOAT, GetEntityTypeFromString("ACACIA_CHEST_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.BAMBOO_RAFT, GetEntityTypeFromString("BAMBOO_RAFT"));
            versionEntityTypes.put(GooPVersionEntities.BAMBOO_CHEST_RAFT, GetEntityTypeFromString("BAMBOO_CHEST_RAFT"));
            versionEntityTypes.put(GooPVersionEntities.BIRCH_BOAT, GetEntityTypeFromString("BIRCH_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.BIRCH_CHEST_BOAT, GetEntityTypeFromString("BIRCH_CHEST_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.CHERRY_BOAT, GetEntityTypeFromString("CHERRY_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.CHERRY_CHEST_BOAT, GetEntityTypeFromString("CHERRY_CHEST_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.DARK_OAK_BOAT, GetEntityTypeFromString("DARK_OAK_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.DARK_OAK_CHEST_BOAT, GetEntityTypeFromString("DARK_OAK_CHEST_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.JUNGLE_BOAT, GetEntityTypeFromString("JUNGLE_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.JUNGLE_CHEST_BOAT, GetEntityTypeFromString("JUNGLE_CHEST_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.MANGROVE_BOAT, GetEntityTypeFromString("MANGROVE_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.MANGROVE_CHEST_BOAT, GetEntityTypeFromString("MANGROVE_CHEST_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.PALE_OAK_BOAT, GetEntityTypeFromString("PALE_OAK_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.PALE_OAK_CHEST_BOAT, GetEntityTypeFromString("PALE_OAK_CHEST_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.SPRUCE_BOAT, GetEntityTypeFromString("SPRUCE_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.SPRUCE_CHEST_BOAT, GetEntityTypeFromString("SPRUCE_CHEST_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.CREAKING, GetEntityTypeFromString("CREAKING"));

            versionEntityTypes.put(GooPVersionEntities.BOAT, GetEntityTypeFromString("OAK_BOAT"));
            versionEntityTypes.put(GooPVersionEntities.CHEST_BOAT, GetEntityTypeFromString("OAK_CHEST_BOAT"));
        }
        //endregion

        //region Minecraft 1.21.3-
        if (mcVersion < 21.3) {
            versionEntityTypes.put(GooPVersionEntities.BOAT, GetEntityTypeFromString("BOAT"));

            // Bro only lasted a few versions
            if (mcVersion >= 19.0) { versionEntityTypes.put(GooPVersionEntities.CHEST_BOAT, GetEntityTypeFromString("CHEST_BOAT")); }
        }
        //endregion

        //region Minecraft 1.21.4-
        if (mcVersion < 21.4) {

            // Bro only lasted a single release
            if (mcVersion >= 21.3) { versionEntityTypes.put(GooPVersionEntities.CREAKING_TRANSIENT, GetEntityTypeFromString("CREAKING_TRANSIENT")); }
        }
        //endregion

        // Finally, fill the holes with null entity UNKNOWN
        for (GooPVersionEntities gvm : GooPVersionEntities.values()) {

            // If its not contianed
            if (!versionEntityTypes.containsKey(gvm)) {

                // Add it as UNKNOWN
                versionEntityTypes.put(gvm, GetEntityTypeFromString("UNKNOWN"));
            }
        }
        //endregion

        //region Enchantments
        //region Minecraft 1.14+
        if (mcVersion >= 14.0) {
            versionEnchantments.put(GooPVersionEnchantments.MULTISHOT, GetEnchantmentFromString("MULTISHOT"));
            versionEnchantments.put(GooPVersionEnchantments.QUICK_CHARGE, GetEnchantmentFromString("QUICK_CHARGE"));
            versionEnchantments.put(GooPVersionEnchantments.PIERCING, GetEnchantmentFromString("PIERCING"));
        }
        //endregion

        //region Minecraft 1.16+
        if (mcVersion >= 16.0) {
            versionEnchantments.put(GooPVersionEnchantments.SOUL_SPEED, GetEnchantmentFromString("SOUL_SPEED"));
        }
        //endregion

        //region Minecraft 1.19+
        if (mcVersion >= 19.0) {
            versionEnchantments.put(GooPVersionEnchantments.SWIFT_SNEAK, GetEnchantmentFromString("SWIFT_SNEAK"));
        }
        //endregion

        //region Minecraft 1.20.6+
        if (mcVersion >= 20.6) {
            versionEnchantments.put(GooPVersionEnchantments.PROTECTION, GetEnchantmentFromString("PROTECTION"));
            versionEnchantments.put(GooPVersionEnchantments.FIRE_PROTECTION, GetEnchantmentFromString("FIRE_PROTECTION"));
            versionEnchantments.put(GooPVersionEnchantments.FEATHER_FALLING, GetEnchantmentFromString("FEATHER_FALLING"));
            versionEnchantments.put(GooPVersionEnchantments.BLAST_PROTECTION, GetEnchantmentFromString("BLAST_PROTECTION"));
            versionEnchantments.put(GooPVersionEnchantments.PROJECTILE_PROTECTION, GetEnchantmentFromString("PROJECTILE_PROTECTION"));
            versionEnchantments.put(GooPVersionEnchantments.RESPIRATION, GetEnchantmentFromString("RESPIRATION"));
            versionEnchantments.put(GooPVersionEnchantments.AQUA_AFFINITY, GetEnchantmentFromString("AQUA_AFFINITY"));
            versionEnchantments.put(GooPVersionEnchantments.SHARPNESS, GetEnchantmentFromString("SHARPNESS"));
            versionEnchantments.put(GooPVersionEnchantments.SMITE, GetEnchantmentFromString("SMITE"));
            versionEnchantments.put(GooPVersionEnchantments.BANE_OF_ARTHROPODS, GetEnchantmentFromString("BANE_OF_ARTHROPODS"));
            versionEnchantments.put(GooPVersionEnchantments.LOOTING, GetEnchantmentFromString("LOOTING"));
            versionEnchantments.put(GooPVersionEnchantments.EFFICIENCY, GetEnchantmentFromString("EFFICIENCY"));
            versionEnchantments.put(GooPVersionEnchantments.UNBREAKING, GetEnchantmentFromString("UNBREAKING"));
            versionEnchantments.put(GooPVersionEnchantments.FORTUNE, GetEnchantmentFromString("FORTUNE"));
            versionEnchantments.put(GooPVersionEnchantments.POWER, GetEnchantmentFromString("POWER"));
            versionEnchantments.put(GooPVersionEnchantments.PUNCH, GetEnchantmentFromString("PUNCH"));
            versionEnchantments.put(GooPVersionEnchantments.FLAME, GetEnchantmentFromString("FLAME"));
            versionEnchantments.put(GooPVersionEnchantments.INFINITY, GetEnchantmentFromString("INFINITY"));
            versionEnchantments.put(GooPVersionEnchantments.LUCK_OF_THE_SEA, GetEnchantmentFromString("LUCK_OF_THE_SEA"));
            versionEnchantments.put(GooPVersionEnchantments.DENSITY, GetEnchantmentFromString("DENSITY"));
            versionEnchantments.put(GooPVersionEnchantments.BREACH, GetEnchantmentFromString("BREACH"));
            versionEnchantments.put(GooPVersionEnchantments.WIND_BURST, GetEnchantmentFromString("WIND_BURST"));

            versionEnchantments.put(GooPVersionEnchantments.LUCK, GetEnchantmentFromString("LUCK_OF_THE_SEA"));
            versionEnchantments.put(GooPVersionEnchantments.ARROW_INFINITE, GetEnchantmentFromString("INFINITY"));
            versionEnchantments.put(GooPVersionEnchantments.ARROW_FIRE, GetEnchantmentFromString("FLAME"));
            versionEnchantments.put(GooPVersionEnchantments.ARROW_KNOCKBACK, GetEnchantmentFromString("PUNCH"));
            versionEnchantments.put(GooPVersionEnchantments.ARROW_DAMAGE, GetEnchantmentFromString("POWER"));
            versionEnchantments.put(GooPVersionEnchantments.LOOT_BONUS_BLOCKS, GetEnchantmentFromString("FORTUNE"));
            versionEnchantments.put(GooPVersionEnchantments.DURABILITY, GetEnchantmentFromString("UNBREAKING"));
            versionEnchantments.put(GooPVersionEnchantments.DIG_SPEED, GetEnchantmentFromString("EFFICIENCY"));
            versionEnchantments.put(GooPVersionEnchantments.LOOT_BONUS_MOBS, GetEnchantmentFromString("LOOTING"));
            versionEnchantments.put(GooPVersionEnchantments.DAMAGE_ARTHROPODS, GetEnchantmentFromString("BANE_OF_ARTHROPODS"));
            versionEnchantments.put(GooPVersionEnchantments.DAMAGE_UNDEAD, GetEnchantmentFromString("SMITE"));
            versionEnchantments.put(GooPVersionEnchantments.DAMAGE_ALL, GetEnchantmentFromString("SHARPNESS"));
            versionEnchantments.put(GooPVersionEnchantments.WATER_WORKER, GetEnchantmentFromString("AQUA_AFFINITY"));
            versionEnchantments.put(GooPVersionEnchantments.OXYGEN, GetEnchantmentFromString("RESPIRATION"));
            versionEnchantments.put(GooPVersionEnchantments.PROTECTION_PROJECTILE, GetEnchantmentFromString("PROJECTILE_PROTECTION"));
            versionEnchantments.put(GooPVersionEnchantments.PROTECTION_EXPLOSIONS, GetEnchantmentFromString("BLAST_PROTECTION"));
            versionEnchantments.put(GooPVersionEnchantments.PROTECTION_FALL, GetEnchantmentFromString("FEATHER_FALLING"));
            versionEnchantments.put(GooPVersionEnchantments.PROTECTION_FIRE, GetEnchantmentFromString("FIRE_PROTECTION"));
            versionEnchantments.put(GooPVersionEnchantments.PROTECTION_ENVIRONMENTAL, GetEnchantmentFromString("PROTECTION"));
        }
        //endregion

        //region Minecraft 1.20.6+
        if (mcVersion < 20.6) {
            versionEnchantments.put(GooPVersionEnchantments.LUCK, GetEnchantmentFromString("LUCK"));
            versionEnchantments.put(GooPVersionEnchantments.ARROW_INFINITE, GetEnchantmentFromString("ARROW_INFINITE"));
            versionEnchantments.put(GooPVersionEnchantments.ARROW_FIRE, GetEnchantmentFromString("ARROW_FIRE"));
            versionEnchantments.put(GooPVersionEnchantments.ARROW_KNOCKBACK, GetEnchantmentFromString("ARROW_KNOCKBACK"));
            versionEnchantments.put(GooPVersionEnchantments.ARROW_DAMAGE, GetEnchantmentFromString("ARROW_DAMAGE"));
            versionEnchantments.put(GooPVersionEnchantments.LOOT_BONUS_BLOCKS, GetEnchantmentFromString("LOOT_BONUS_BLOCKS"));
            versionEnchantments.put(GooPVersionEnchantments.DURABILITY, GetEnchantmentFromString("DURABILITY"));
            versionEnchantments.put(GooPVersionEnchantments.DIG_SPEED, GetEnchantmentFromString("DIG_SPEED"));
            versionEnchantments.put(GooPVersionEnchantments.LOOT_BONUS_MOBS, GetEnchantmentFromString("LOOT_BONUS_MOBS"));
            versionEnchantments.put(GooPVersionEnchantments.DAMAGE_ARTHROPODS, GetEnchantmentFromString("DAMAGE_ARTHROPODS"));
            versionEnchantments.put(GooPVersionEnchantments.DAMAGE_UNDEAD, GetEnchantmentFromString("DAMAGE_UNDEAD"));
            versionEnchantments.put(GooPVersionEnchantments.DAMAGE_ALL, GetEnchantmentFromString("DAMAGE_ALL"));
            versionEnchantments.put(GooPVersionEnchantments.WATER_WORKER, GetEnchantmentFromString("WATER_WORKER"));
            versionEnchantments.put(GooPVersionEnchantments.OXYGEN, GetEnchantmentFromString("OXYGEN"));
            versionEnchantments.put(GooPVersionEnchantments.PROTECTION_PROJECTILE, GetEnchantmentFromString("PROTECTION_PROJECTILE"));
            versionEnchantments.put(GooPVersionEnchantments.PROTECTION_EXPLOSIONS, GetEnchantmentFromString("PROTECTION_EXPLOSIONS"));
            versionEnchantments.put(GooPVersionEnchantments.PROTECTION_FALL, GetEnchantmentFromString("PROTECTION_FALL"));
            versionEnchantments.put(GooPVersionEnchantments.PROTECTION_FIRE, GetEnchantmentFromString("PROTECTION_FIRE"));
            versionEnchantments.put(GooPVersionEnchantments.PROTECTION_ENVIRONMENTAL, GetEnchantmentFromString("PROTECTION_ENVIRONMENTAL"));

            versionEnchantments.put(GooPVersionEnchantments.PROTECTION, GetEnchantmentFromString("PROTECTION_ENVIRONMENTAL"));
            versionEnchantments.put(GooPVersionEnchantments.FIRE_PROTECTION, GetEnchantmentFromString("PROTECTION_FIRE"));
            versionEnchantments.put(GooPVersionEnchantments.FEATHER_FALLING, GetEnchantmentFromString("PROTECTION_FALL"));
            versionEnchantments.put(GooPVersionEnchantments.BLAST_PROTECTION, GetEnchantmentFromString("PROTECTION_EXPLOSIONS"));
            versionEnchantments.put(GooPVersionEnchantments.PROJECTILE_PROTECTION, GetEnchantmentFromString("PROTECTION_PROJECTILE"));
            versionEnchantments.put(GooPVersionEnchantments.RESPIRATION, GetEnchantmentFromString("OXYGEN"));
            versionEnchantments.put(GooPVersionEnchantments.AQUA_AFFINITY, GetEnchantmentFromString("WATER_WORKER"));
            versionEnchantments.put(GooPVersionEnchantments.SHARPNESS, GetEnchantmentFromString("DAMAGE_ALL"));
            versionEnchantments.put(GooPVersionEnchantments.SMITE, GetEnchantmentFromString("DAMAGE_UNDEAD"));
            versionEnchantments.put(GooPVersionEnchantments.BANE_OF_ARTHROPODS, GetEnchantmentFromString("DAMAGE_ARTHROPODS"));
            versionEnchantments.put(GooPVersionEnchantments.LOOTING, GetEnchantmentFromString("LOOT_BONUS_MOBS"));
            versionEnchantments.put(GooPVersionEnchantments.EFFICIENCY, GetEnchantmentFromString("DIG_SPEED"));
            versionEnchantments.put(GooPVersionEnchantments.UNBREAKING, GetEnchantmentFromString("DURABILITY"));
            versionEnchantments.put(GooPVersionEnchantments.FORTUNE, GetEnchantmentFromString("LOOT_BONUS_BLOCKS"));
            versionEnchantments.put(GooPVersionEnchantments.POWER, GetEnchantmentFromString("ARROW_DAMAGE"));
            versionEnchantments.put(GooPVersionEnchantments.PUNCH, GetEnchantmentFromString("ARROW_KNOCKBACK"));
            versionEnchantments.put(GooPVersionEnchantments.FLAME, GetEnchantmentFromString("ARROW_FIRE"));
            versionEnchantments.put(GooPVersionEnchantments.INFINITY, GetEnchantmentFromString("ARROW_INFINITE"));
            versionEnchantments.put(GooPVersionEnchantments.LUCK_OF_THE_SEA, GetEnchantmentFromString("LUCK"));
        }

        // Finally, fill the holes with null entity UNKNOWN
        for (GooPVersionEnchantments gvm : GooPVersionEnchantments.values()) {

            // If its not contianed
            if (!versionEnchantments.containsKey(gvm)) {

                // Add it as NULL
                versionEnchantments.put(gvm, null);
            }
        }
        //endregion
        //endregion

        //region Attributes
        //region Minecraft 1.20.6+
        if (mcVersion >= 20.6) {
            versionAttributes.put(GooPVersionAttributes.GENERIC_MAX_HEALTH, GetAttributeFromString("MAX_HEALTH"));
            versionAttributes.put(GooPVersionAttributes.MAX_HEALTH, GetAttributeFromString("MAX_HEALTH"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_FOLLOW_RANGE, GetAttributeFromString("FOLLOW_RANGE"));
            versionAttributes.put(GooPVersionAttributes.FOLLOW_RANGE, GetAttributeFromString("FOLLOW_RANGE"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_KNOCKBACK_RESISTANCE, GetAttributeFromString("KNOCKBACK_RESISTANCE"));
            versionAttributes.put(GooPVersionAttributes.KNOCKBACK_RESISTANCE, GetAttributeFromString("KNOCKBACK_RESISTANCE"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_MOVEMENT_SPEED, GetAttributeFromString("MOVEMENT_SPEED"));
            versionAttributes.put(GooPVersionAttributes.MOVEMENT_SPEED, GetAttributeFromString("MOVEMENT_SPEED"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_FLYING_SPEED, GetAttributeFromString("FLYING_SPEED"));
            versionAttributes.put(GooPVersionAttributes.FLYING_SPEED, GetAttributeFromString("FLYING_SPEED"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_ATTACK_DAMAGE, GetAttributeFromString("ATTACK_DAMAGE"));
            versionAttributes.put(GooPVersionAttributes.ATTACK_DAMAGE, GetAttributeFromString("ATTACK_DAMAGE"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_ATTACK_KNOCKBACK, GetAttributeFromString("ATTACK_KNOCKBACK"));
            versionAttributes.put(GooPVersionAttributes.ATTACK_KNOCKBACK, GetAttributeFromString("ATTACK_KNOCKBACK"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_ATTACK_SPEED, GetAttributeFromString("ATTACK_SPEED"));
            versionAttributes.put(GooPVersionAttributes.ATTACK_SPEED, GetAttributeFromString("ATTACK_SPEED"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_ARMOR, GetAttributeFromString("ARMOR"));
            versionAttributes.put(GooPVersionAttributes.ARMOR, GetAttributeFromString("ARMOR"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_ARMOR_TOUGHNESS, GetAttributeFromString("ARMOR_TOUGHNESS"));
            versionAttributes.put(GooPVersionAttributes.ARMOR_TOUGHNESS, GetAttributeFromString("ARMOR_TOUGHNESS"));
            versionAttributes.put(GooPVersionAttributes.FALL_DAMAGE_MULTIPLIER, GetAttributeFromString("FALL_DAMAGE_MULTIPLIER"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_LUCK, GetAttributeFromString("LUCK"));
            versionAttributes.put(GooPVersionAttributes.LUCK, GetAttributeFromString("LUCK"));
            versionAttributes.put(GooPVersionAttributes.MAX_ABSORPTION, GetAttributeFromString("MAX_ABSORPTION"));
            versionAttributes.put(GooPVersionAttributes.SAFE_FALL_DISTANCE, GetAttributeFromString("SAFE_FALL_DISTANCE"));
            versionAttributes.put(GooPVersionAttributes.SCALE, GetAttributeFromString("SCALE"));
            versionAttributes.put(GooPVersionAttributes.STEP_HEIGHT, GetAttributeFromString("STEP_HEIGHT"));
            versionAttributes.put(GooPVersionAttributes.GRAVITY, GetAttributeFromString("GRAVITY"));
            versionAttributes.put(GooPVersionAttributes.HORSE_JUMP_STRENGTH, GetAttributeFromString("JUMP_STRENGTH"));
            versionAttributes.put(GooPVersionAttributes.JUMP_STRENGTH, GetAttributeFromString("JUMP_STRENGTH"));
            versionAttributes.put(GooPVersionAttributes.BURNING_TIME, GetAttributeFromString("BURNING_TIME"));
            versionAttributes.put(GooPVersionAttributes.EXPLOSION_KNOCKBACK_RESISTANCE, GetAttributeFromString("EXPLOSION_KNOCKBACK_RESISTANCE"));
            versionAttributes.put(GooPVersionAttributes.MOVEMENT_EFFICIENCY, GetAttributeFromString("MOVEMENT_EFFICIENCY"));
            versionAttributes.put(GooPVersionAttributes.OXYGEN_BONUS, GetAttributeFromString("OXYGEN_BONUS"));
            versionAttributes.put(GooPVersionAttributes.WATER_MOVEMENT_EFFICIENCY, GetAttributeFromString("WATER_MOVEMENT_EFFICIENCY"));
            versionAttributes.put(GooPVersionAttributes.TEMPT_RANGE, GetAttributeFromString("TEMPT_RANGE"));
            versionAttributes.put(GooPVersionAttributes.BLOCK_INTERACTION_RANGE, GetAttributeFromString("BLOCK_INTERACTION_RANGE"));
            versionAttributes.put(GooPVersionAttributes.ENTITY_INTERACTION_RANGE, GetAttributeFromString("ENTITY_INTERACTION_RANGE"));
            versionAttributes.put(GooPVersionAttributes.BLOCK_BREAK_SPEED, GetAttributeFromString("BLOCK_BREAK_SPEED"));
            versionAttributes.put(GooPVersionAttributes.MINING_EFFICIENCY, GetAttributeFromString("MINING_EFFICIENCY"));
            versionAttributes.put(GooPVersionAttributes.SNEAKING_SPEED, GetAttributeFromString("SNEAKING_SPEED"));
            versionAttributes.put(GooPVersionAttributes.SUBMERGED_MINING_SPEED, GetAttributeFromString("SUBMERGED_MINING_SPEED"));
            versionAttributes.put(GooPVersionAttributes.SWEEPING_DAMAGE_RATIO, GetAttributeFromString("SWEEPING_DAMAGE_RATIO"));
            versionAttributes.put(GooPVersionAttributes.ZOMBIE_SPAWN_REINFORCEMENTS, GetAttributeFromString("SPAWN_REINFORCEMENTS"));
            versionAttributes.put(GooPVersionAttributes.SPAWN_REINFORCEMENTS, GetAttributeFromString("SPAWN_REINFORCEMENTS"));
        }
        //endregion

        //region Minecraft 1.20.6-
        if (mcVersion < 20.6) {
            versionAttributes.put(GooPVersionAttributes.SPAWN_REINFORCEMENTS, GetAttributeFromString("ZOMBIE_SPAWN_REINFORCEMENTS"));
            versionAttributes.put(GooPVersionAttributes.ZOMBIE_SPAWN_REINFORCEMENTS, GetAttributeFromString("ZOMBIE_SPAWN_REINFORCEMENTS"));
            versionAttributes.put(GooPVersionAttributes.JUMP_STRENGTH, GetAttributeFromString("HORSE_JUMP_STRENGTH"));
            versionAttributes.put(GooPVersionAttributes.HORSE_JUMP_STRENGTH, GetAttributeFromString("HORSE_JUMP_STRENGTH"));
            versionAttributes.put(GooPVersionAttributes.LUCK, GetAttributeFromString("GENERIC_LUCK"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_LUCK, GetAttributeFromString("GENERIC_LUCK"));
            versionAttributes.put(GooPVersionAttributes.ARMOR_TOUGHNESS, GetAttributeFromString("GENERIC_ARMOR_TOUGHNESS"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_ARMOR_TOUGHNESS, GetAttributeFromString("GENERIC_ARMOR_TOUGHNESS"));
            versionAttributes.put(GooPVersionAttributes.ARMOR, GetAttributeFromString("GENERIC_ARMOR"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_ARMOR, GetAttributeFromString("GENERIC_ARMOR"));
            versionAttributes.put(GooPVersionAttributes.ATTACK_SPEED, GetAttributeFromString("GENERIC_ATTACK_SPEED"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_ATTACK_SPEED, GetAttributeFromString("GENERIC_ATTACK_SPEED"));
            versionAttributes.put(GooPVersionAttributes.ATTACK_KNOCKBACK, GetAttributeFromString("GENERIC_ATTACK_KNOCKBACK"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_ATTACK_KNOCKBACK, GetAttributeFromString("GENERIC_ATTACK_KNOCKBACK"));
            versionAttributes.put(GooPVersionAttributes.ATTACK_DAMAGE, GetAttributeFromString("GENERIC_ATTACK_DAMAGE"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_ATTACK_DAMAGE, GetAttributeFromString("GENERIC_ATTACK_DAMAGE"));
            versionAttributes.put(GooPVersionAttributes.FLYING_SPEED, GetAttributeFromString("GENERIC_FLYING_SPEED"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_FLYING_SPEED, GetAttributeFromString("GENERIC_FLYING_SPEED"));
            versionAttributes.put(GooPVersionAttributes.MOVEMENT_SPEED, GetAttributeFromString("GENERIC_MOVEMENT_SPEED"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_MOVEMENT_SPEED, GetAttributeFromString("GENERIC_MOVEMENT_SPEED"));
            versionAttributes.put(GooPVersionAttributes.KNOCKBACK_RESISTANCE, GetAttributeFromString("GENERIC_KNOCKBACK_RESISTANCE"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_KNOCKBACK_RESISTANCE, GetAttributeFromString("GENERIC_KNOCKBACK_RESISTANCE"));
            versionAttributes.put(GooPVersionAttributes.FOLLOW_RANGE, GetAttributeFromString("GENERIC_FOLLOW_RANGE"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_FOLLOW_RANGE, GetAttributeFromString("GENERIC_FOLLOW_RANGE"));
            versionAttributes.put(GooPVersionAttributes.MAX_HEALTH, GetAttributeFromString("GENERIC_MAX_HEALTH"));
            versionAttributes.put(GooPVersionAttributes.GENERIC_MAX_HEALTH, GetAttributeFromString("GENERIC_MAX_HEALTH"));
        }
        //endregion
        //endregion

        //region Potion Effects
        //region Minecraft 1.14.4+
        if (mcVersion >= 14.4) {
            versionPotionEffects.put(GooPVersionPotionEffects.BAD_OMEN, GetPotionEffectFromString("BAD_OMEN"));
            versionPotionEffects.put(GooPVersionPotionEffects.HERO_OF_THE_VILLAGE, GetPotionEffectFromString("HERO_OF_THE_VILLAGE"));
        }
        //endregion

        //region Minecraft 1.19+
        if (mcVersion >= 19.0) {
            versionPotionEffects.put(GooPVersionPotionEffects.DARKNESS, GetPotionEffectFromString("DARKNESS"));
        }
        //endregion

        //region Minecraft 1.20.6+
        if (mcVersion >= 20.6) {
            versionPotionEffects.put(GooPVersionPotionEffects.SLOW, GetPotionEffectFromString("SLOWNESS"));
            versionPotionEffects.put(GooPVersionPotionEffects.SLOWNESS, GetPotionEffectFromString("SLOWNESS"));
            versionPotionEffects.put(GooPVersionPotionEffects.FAST_DIGGING, GetPotionEffectFromString("HASTE"));
            versionPotionEffects.put(GooPVersionPotionEffects.HASTE, GetPotionEffectFromString("HASTE"));
            versionPotionEffects.put(GooPVersionPotionEffects.SLOW_DIGGING, GetPotionEffectFromString("MINING_FATIGUE"));
            versionPotionEffects.put(GooPVersionPotionEffects.MINING_FATIGUE, GetPotionEffectFromString("MINING_FATIGUE"));
            versionPotionEffects.put(GooPVersionPotionEffects.INCREASE_DAMAGE, GetPotionEffectFromString("STRENGTH"));
            versionPotionEffects.put(GooPVersionPotionEffects.STRENGTH, GetPotionEffectFromString("STRENGTH"));
            versionPotionEffects.put(GooPVersionPotionEffects.HEAL, GetPotionEffectFromString("INSTANT_HEALTH"));
            versionPotionEffects.put(GooPVersionPotionEffects.INSTANT_HEALTH, GetPotionEffectFromString("INSTANT_HEALTH"));
            versionPotionEffects.put(GooPVersionPotionEffects.HARM, GetPotionEffectFromString("INSTANT_DAMAGE"));
            versionPotionEffects.put(GooPVersionPotionEffects.INSTANT_DAMAGE, GetPotionEffectFromString("INSTANT_DAMAGE"));
            versionPotionEffects.put(GooPVersionPotionEffects.JUMP, GetPotionEffectFromString("JUMP_BOOST"));
            versionPotionEffects.put(GooPVersionPotionEffects.JUMP_BOOST, GetPotionEffectFromString("JUMP_BOOST"));
            versionPotionEffects.put(GooPVersionPotionEffects.CONFUSION, GetPotionEffectFromString("NAUSEA"));
            versionPotionEffects.put(GooPVersionPotionEffects.NAUSEA, GetPotionEffectFromString("NAUSEA"));
            versionPotionEffects.put(GooPVersionPotionEffects.REGENERATION, GetPotionEffectFromString("REGENERATION"));
            versionPotionEffects.put(GooPVersionPotionEffects.DAMAGE_RESISTANCE, GetPotionEffectFromString("RESISTANCE"));
            versionPotionEffects.put(GooPVersionPotionEffects.RESISTANCE, GetPotionEffectFromString("RESISTANCE"));
            versionPotionEffects.put(GooPVersionPotionEffects.TRIAL_OMEN, GetPotionEffectFromString("TRIAL_OMEN"));
            versionPotionEffects.put(GooPVersionPotionEffects.RAID_OMEN, GetPotionEffectFromString("RAID_OMEN"));
            versionPotionEffects.put(GooPVersionPotionEffects.WIND_CHARGED, GetPotionEffectFromString("WIND_CHARGED"));
            versionPotionEffects.put(GooPVersionPotionEffects.WEAVING, GetPotionEffectFromString("WEAVING"));
            versionPotionEffects.put(GooPVersionPotionEffects.OOZING, GetPotionEffectFromString("OOZING"));
            versionPotionEffects.put(GooPVersionPotionEffects.INFESTED, GetPotionEffectFromString("INFESTED"));
        }
        //endregion

        //region Minecraft 1.20.6-
        if (mcVersion < 20.6) {
            versionPotionEffects.put(GooPVersionPotionEffects.RESISTANCE, GetPotionEffectFromString("DAMAGE_RESISTANCE"));
            versionPotionEffects.put(GooPVersionPotionEffects.DAMAGE_RESISTANCE, GetPotionEffectFromString("DAMAGE_RESISTANCE"));
            versionPotionEffects.put(GooPVersionPotionEffects.NAUSEA, GetPotionEffectFromString("CONFUSION"));
            versionPotionEffects.put(GooPVersionPotionEffects.CONFUSION, GetPotionEffectFromString("CONFUSION"));
            versionPotionEffects.put(GooPVersionPotionEffects.JUMP_BOOST, GetPotionEffectFromString("JUMP"));
            versionPotionEffects.put(GooPVersionPotionEffects.JUMP, GetPotionEffectFromString("JUMP"));
            versionPotionEffects.put(GooPVersionPotionEffects.INSTANT_DAMAGE, GetPotionEffectFromString("HARM"));
            versionPotionEffects.put(GooPVersionPotionEffects.HARM, GetPotionEffectFromString("HARM"));
            versionPotionEffects.put(GooPVersionPotionEffects.INSTANT_HEALTH, GetPotionEffectFromString("HEAL"));
            versionPotionEffects.put(GooPVersionPotionEffects.HEAL, GetPotionEffectFromString("HEAL"));
            versionPotionEffects.put(GooPVersionPotionEffects.STRENGTH, GetPotionEffectFromString("INCREASE_DAMAGE"));
            versionPotionEffects.put(GooPVersionPotionEffects.INCREASE_DAMAGE, GetPotionEffectFromString("INCREASE_DAMAGE"));
            versionPotionEffects.put(GooPVersionPotionEffects.MINING_FATIGUE, GetPotionEffectFromString("SLOW_DIGGING"));
            versionPotionEffects.put(GooPVersionPotionEffects.SLOW_DIGGING, GetPotionEffectFromString("SLOW_DIGGING"));
            versionPotionEffects.put(GooPVersionPotionEffects.HASTE, GetPotionEffectFromString("FAST_DIGGING"));
            versionPotionEffects.put(GooPVersionPotionEffects.FAST_DIGGING, GetPotionEffectFromString("FAST_DIGGING"));
            versionPotionEffects.put(GooPVersionPotionEffects.SLOWNESS, GetPotionEffectFromString("SLOW"));
            versionPotionEffects.put(GooPVersionPotionEffects.SLOW, GetPotionEffectFromString("SLOW"));
        }
        //endregion
        //endregion
    }

    //region Materials
    /**
     * Returns a material from a string.
     *
     * @return Either the Material, or VOID_AIR if it doesnt exist.
     */
    @NotNull public static Material GetMaterialFromString(String str) {

        // Is it a supported plugin command?
        try {

            // Yes, it seems to be
            Material mat = Material.valueOf(str);
            return mat;

        // Not recognized
        } catch (IllegalArgumentException ex) {

            // Return Air
            return GetMaterialFromString("VOID_AIR");
        }
    }

    /**
     * If the current material exists in this minecraft version, return it.
     * Otherwise, returns defaultIfMissing
     */
    public static Material GetVersionMaterial(GooPVersionMaterials mat, Material defaultIfMissing) {
        Material mt = GetVersionMaterial(mat);
        if (mt != GetMaterialFromString("VOID_AIR")) {
            return mt;
        } else {
            return defaultIfMissing;
        }
    }

    /**
     * If the current material exists in this minecraft version, return it.
     * Otherwise, returns VOID_AIR.
     */
    public static Material GetVersionMaterial(GooPVersionMaterials mat) {
        return versionMaterials.get(mat);
    }
    //endregion

    //region Entities
    /**
     * Returns an EntityType from a string.
     *
     * @return Either the EntityType, or UNKNOWN if it doesnt exist.
     */
    public static EntityType GetEntityTypeFromString(String str) {

        // Is it a supported plugin command?
        try {

            // Yes, it seems to be
            EntityType eny = EntityType.valueOf(str);
            return eny;

            // Not recognized
        } catch (IllegalArgumentException ex) {

            // Retry with lowercase via registry
            try {
                return org.bukkit.Registry.ENTITY_TYPE.get(NamespacedKey.minecraft(str.toLowerCase()));
            } catch (Exception ignored2) {
                return GetEntityTypeFromString("UNKNOWN");
            }
        }
    }

    /**
     * If the current EntityType exists in this minecraft version, return it.
     * Otherwise, returns defaultIfMissing
     */
    public static EntityType GetVersionEntityType(GooPVersionEntities ent, EntityType defaultIfMissing) {
        EntityType mt = GetVersionEntityType(ent);
        if (mt != GetEntityTypeFromString("UNKNOWN")) {
            return mt;
        } else {
            return defaultIfMissing;
        }
    }

    /**
     * If the current EntityType exists in this minecraft version, return it.
     * Otherwise, returns UNKNOWN.
     */
    public static EntityType GetVersionEntityType(GooPVersionEntities ent) {
        return versionEntityTypes.get(ent);
    }
    //endregion

    //region Enchantments
    /**
     * Returns an Enchantment from a string.
     *
     * @return Either the Enchantment, or null if it doesnt exist.
     */
    @Nullable public static Enchantment GetEnchantmentFromString(@Nullable String str) {
        if (str == null) { return null; };
        str = str.toLowerCase();

        if (mcVersion < 20.3) {

            return Enchantment.getByKey(NamespacedKey.minecraft(str));
        } else {
            if (mcVersion < 21.0 || !Gunging_Ootilities_Plugin.asPaperSpigot) {
                return org.bukkit.Registry.ENCHANTMENT.get(NamespacedKey.minecraft(str));

            } else {
                return org.bukkit.Registry.ENCHANTMENT.get(NamespacedKey.minecraft(str));
            }
        }
    }

    /**
     * If the current Enchantment exists in this minecraft version, return it.
     * Otherwise, returns defaultIfMissing
     */
    public static Enchantment GetVersionEnchantment(GooPVersionEnchantments ench, Enchantment defaultIfMissing) {
        Enchantment mt = GetVersionEnchantment(ench);
        if (mt != null) {
            return mt;
        } else {
            return defaultIfMissing;
        }
    }

    /**
     * If the current Enchantment exists in this minecraft version, return it.
     * Otherwise, returns NULL.
     */
    public static Enchantment GetVersionEnchantment(GooPVersionEnchantments ench) {
        return versionEnchantments.get(ench);
    }
    //endregion

    //region Attributes
    /**
     * Returns an Attributes from a string.
     *
     * @return Either the Attributes, or null if it doesnt exist.
     */
    @Nullable public static Attribute GetAttributeFromString(@Nullable String str) {
        if (str == null) { return null; };
        String upperStr = str.toUpperCase();
        String lowerStr = str.toLowerCase();

        if (mcVersion < 21.3) {
            try {
                return Attribute.valueOf(upperStr);
            } catch (IllegalArgumentException e) {
                return org.bukkit.Registry.ATTRIBUTE.get(NamespacedKey.minecraft(lowerStr));
            }

        } else {
            if (!Gunging_Ootilities_Plugin.asPaperSpigot) {
                return org.bukkit.Registry.ATTRIBUTE.get(NamespacedKey.minecraft(lowerStr));

            } else {
                return org.bukkit.Registry.ATTRIBUTE.get(NamespacedKey.minecraft(lowerStr));
            }
        }
    }

    /**
     * If the current Attribute exists in this minecraft version, return it.
     * Otherwise, returns defaultIfMissing
     */
    public static Attribute GetVersionAttribute(GooPVersionAttributes ench, Attribute defaultIfMissing) {
        Attribute mt = GetVersionAttribute(ench);
        if (mt != null) {
            return mt;
        } else {
            return defaultIfMissing;
        }
    }

    /**
     * If the current Attribute exists in this minecraft version, return it.
     * Otherwise, returns NULL.
     */
    public static Attribute GetVersionAttribute(GooPVersionAttributes ench) {
        return versionAttributes.get(ench);
    }
    //endregion

    //region PotionEffects
    /**
     * Returns an Attributes from a string.
     *
     * @return Either the Attributes, or null if it doesnt exist.
     */
    @Nullable public static PotionEffectType GetPotionEffectFromString(@Nullable String str) {
        if (str == null) { return null; };
        str = str.toLowerCase();

        if (mcVersion < 21.3) {
            return PotionEffectType.getByKey(NamespacedKey.minecraft(str));

        } else {
            return org.bukkit.Registry.POTION_EFFECT_TYPE.get(NamespacedKey.minecraft(str));
        }
    }

    /**
     * If the current Attribute exists in this minecraft version, return it.
     * Otherwise, returns defaultIfMissing
     */
    public static PotionEffectType GetVersionPotionEffect(GooPVersionPotionEffects ench, PotionEffectType defaultIfMissing) {
        PotionEffectType mt = GetVersionPotionEffect(ench);
        if (mt != null) {
            return mt;
        } else {
            return defaultIfMissing;
        }
    }

    /**
     * If the current Attribute exists in this minecraft version, return it.
     * Otherwise, returns NULL.
     */
    public static PotionEffectType GetVersionPotionEffect(GooPVersionPotionEffects ench) {
        return versionPotionEffects.get(ench);
    }
    //endregion
}
