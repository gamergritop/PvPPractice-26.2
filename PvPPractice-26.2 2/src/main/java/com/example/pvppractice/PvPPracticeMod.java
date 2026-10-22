package com.example.pvppractice;

import com.mojang.brigadier.Command;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.minecraft.commands.Commands.literal;

/**
 * First playable prototype for Minecraft Java 26.2 / Fabric.
 * The opponent is a geared, persistent vanilla Zombie with enhanced attributes.
 */
public final class PvPPracticeMod implements ModInitializer {
    private static final Map<UUID, UUID> ACTIVE_BOTS = new HashMap<>();

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            // Permission level 0 is intentional: the command must be available in a
            // normal single-player world even when Allow Cheats is disabled.
            dispatcher.register(literal("pvpsummon")
                    .requires(source -> source.getEntity() instanceof ServerPlayer)
                    .executes(context -> summon(context.getSource().getPlayerOrException())));

            dispatcher.register(literal("pvpbot")
                    .requires(source -> source.getEntity() instanceof ServerPlayer)
                    .then(literal("remove").executes(context -> remove(context.getSource().getPlayerOrException())))
                    .then(literal("gear").executes(context -> giveGear(context.getSource().getPlayerOrException())))
                    .then(literal("help").executes(context -> {
                        context.getSource().sendSuccess(() -> Component.literal(
                                "PvP Practice: /pvpsummon, /pvpbot remove, /pvpbot gear"), false);
                        return Command.SINGLE_SUCCESS;
                    })));
        });
    }

    private static int summon(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        UUID ownerId = player.getUUID();
        UUID oldBotId = ACTIVE_BOTS.get(ownerId);
        if (oldBotId != null) {
            Entity old = level.getEntity(oldBotId);
            if (old != null && old.isAlive()) {
                player.sendSystemMessage(Component.literal("Your PvP bot is already active. Use /pvpbot remove first."));
                return 0;
            }
            ACTIVE_BOTS.remove(ownerId);
        }

        giveGear(player);

        Zombie bot = EntityType.ZOMBIE.create(level, net.minecraft.world.entity.EntitySpawnReason.COMMAND);
        if (bot == null) {
            player.sendSystemMessage(Component.literal("Could not create the PvP bot."));
            return 0;
        }

        double radians = Math.toRadians(player.getYRot());
        double x = player.getX() - Math.sin(radians) * 2.5;
        double z = player.getZ() + Math.cos(radians) * 2.5;
        bot.moveTo(x, player.getY(), z, player.getYRot() + 180.0F, 0.0F);
        bot.setCustomName(Component.literal("§c§lPro PvP Bot"));
        bot.setCustomNameVisible(true);
        bot.setPersistenceRequired();
        bot.setCanPickUpLoot(false);

        setAttribute(bot, Attributes.MAX_HEALTH, 40.0);
        setAttribute(bot, Attributes.MOVEMENT_SPEED, 0.34);
        setAttribute(bot, Attributes.ATTACK_DAMAGE, 8.0);
        setAttribute(bot, Attributes.FOLLOW_RANGE, 48.0);
        bot.setHealth(40.0F);
        equipBot(level, bot);

        if (!level.addFreshEntity(bot)) {
            player.sendSystemMessage(Component.literal("Could not add the PvP bot to this world."));
            return 0;
        }
        bot.setTarget(player);
        ACTIVE_BOTS.put(ownerId, bot.getUUID());
        player.sendSystemMessage(Component.literal("§aPvP bot summoned! You and the bot have been equipped."));
        return Command.SINGLE_SUCCESS;
    }

    private static int remove(ServerPlayer player) {
        UUID botId = ACTIVE_BOTS.remove(player.getUUID());
        if (botId == null) {
            player.sendSystemMessage(Component.literal("You don't have an active PvP bot."));
            return 0;
        }
        Entity entity = player.level().getEntity(botId);
        if (entity != null) entity.discard();
        player.sendSystemMessage(Component.literal("§eYour PvP bot was removed."));
        return Command.SINGLE_SUCCESS;
    }

    private static int giveGear(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        player.getInventory().armor.set(3, enchanted(level, Items.NETHERITE_HELMET,
                Enchantments.PROTECTION, 4, Enchantments.UNBREAKING, 3, Enchantments.MENDING, 1));
        player.getInventory().armor.set(2, enchanted(level, Items.NETHERITE_CHESTPLATE,
                Enchantments.PROTECTION, 4, Enchantments.UNBREAKING, 3, Enchantments.MENDING, 1, Enchantments.THORNS, 3));
        player.getInventory().armor.set(1, enchanted(level, Items.NETHERITE_LEGGINGS,
                Enchantments.PROTECTION, 4, Enchantments.UNBREAKING, 3, Enchantments.MENDING, 1, Enchantments.THORNS, 3));
        player.getInventory().armor.set(0, enchanted(level, Items.NETHERITE_BOOTS,
                Enchantments.PROTECTION, 4, Enchantments.UNBREAKING, 3, Enchantments.MENDING, 1,
                Enchantments.FEATHER_FALLING, 4, Enchantments.DEPTH_STRIDER, 3));

        player.getInventory().offhand.set(0, enchanted(level, Items.SHIELD,
                Enchantments.UNBREAKING, 3, Enchantments.MENDING, 1));
        player.getInventory().setItem(0, enchanted(level, Items.NETHERITE_SWORD,
                Enchantments.SHARPNESS, 5, Enchantments.FIRE_ASPECT, 2, Enchantments.KNOCKBACK, 2,
                Enchantments.LOOTING, 3, Enchantments.UNBREAKING, 3, Enchantments.MENDING, 1));
        player.getInventory().setItem(1, enchanted(level, Items.NETHERITE_AXE,
                Enchantments.SHARPNESS, 5, Enchantments.EFFICIENCY, 5, Enchantments.UNBREAKING, 3, Enchantments.MENDING, 1));
        player.getInventory().setItem(2, new ItemStack(Items.GOLDEN_APPLE, 16));
        player.getInventory().setItem(3, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 4));
        player.getInventory().setItem(4, new ItemStack(Items.END_CRYSTAL, 16));
        player.getInventory().setItem(5, new ItemStack(Items.OBSIDIAN, 32));
        player.getInventory().setItem(6, new ItemStack(Items.TOTEM_OF_UNDYING, 1));
        player.sendSystemMessage(Component.literal("§6Maxed PvP loadout equipped (sword, axe, shield, healing, crystal supplies)."));
        return Command.SINGLE_SUCCESS;
    }

    private static void equipBot(ServerLevel level, Zombie bot) {
        bot.setItemSlot(EquipmentSlot.HEAD, enchanted(level, Items.NETHERITE_HELMET,
                Enchantments.PROTECTION, 4, Enchantments.UNBREAKING, 3, Enchantments.MENDING, 1));
        bot.setItemSlot(EquipmentSlot.CHEST, enchanted(level, Items.NETHERITE_CHESTPLATE,
                Enchantments.PROTECTION, 4, Enchantments.UNBREAKING, 3, Enchantments.MENDING, 1));
        bot.setItemSlot(EquipmentSlot.LEGS, enchanted(level, Items.NETHERITE_LEGGINGS,
                Enchantments.PROTECTION, 4, Enchantments.UNBREAKING, 3, Enchantments.MENDING, 1));
        bot.setItemSlot(EquipmentSlot.FEET, enchanted(level, Items.NETHERITE_BOOTS,
                Enchantments.PROTECTION, 4, Enchantments.UNBREAKING, 3, Enchantments.MENDING, 1,
                Enchantments.FEATHER_FALLING, 4));
        bot.setItemSlot(EquipmentSlot.MAINHAND, enchanted(level, Items.NETHERITE_SWORD,
                Enchantments.SHARPNESS, 5, Enchantments.FIRE_ASPECT, 2, Enchantments.KNOCKBACK, 2,
                Enchantments.UNBREAKING, 3, Enchantments.MENDING, 1));
        bot.setItemSlot(EquipmentSlot.OFFHAND, enchanted(level, Items.SHIELD,
                Enchantments.UNBREAKING, 3, Enchantments.MENDING, 1));
    }

    private static void setAttribute(Zombie bot, net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute, double value) {
        AttributeInstance instance = bot.getAttribute(attribute);
        if (instance != null) instance.setBaseValue(value);
    }

    @SafeVarargs
    private static ItemStack enchanted(ServerLevel level, Item item, Object... enchantmentAndLevels) {
        ItemStack stack = new ItemStack(item);
        Registry<Enchantment> registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        for (int i = 0; i + 1 < enchantmentAndLevels.length; i += 2) {
            @SuppressWarnings("unchecked")
            net.minecraft.resources.ResourceKey<Enchantment> key =
                    (net.minecraft.resources.ResourceKey<Enchantment>) enchantmentAndLevels[i];
            int levelValue = (Integer) enchantmentAndLevels[i + 1];
            Holder<Enchantment> enchantment = registry.getHolderOrThrow(key);
            stack.enchant(enchantment, levelValue);
        }
        return stack;
    }
}
