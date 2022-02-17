package org.moon.moonfix;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class Commands {

    public static void initCommands() {
        //sound
        LiteralArgumentBuilder<FabricClientCommandSource> command = LiteralArgumentBuilder.literal("sound");

        //stop all
        LiteralArgumentBuilder<FabricClientCommandSource> stop = LiteralArgumentBuilder.literal("stop");
        stop.executes(context -> stopSound(null, null));

        for (SoundCategory soundCategory : SoundCategory.values()) {
            //stop category
            stop.then((ClientCommandManager.literal(soundCategory.getName())
                    .executes(context -> stopSound(null, soundCategory)))

                    //stop specific
                    .then(ClientCommandManager.argument("sound", IdentifierArgumentType.identifier())
                            .suggests((context, builder) -> CommandSource.suggestIdentifiers(context.getSource().getSoundIds(), builder))
                            .executes(context -> stopSound(context.getArgument("sound", Identifier.class), soundCategory))));
        }

        //play
        LiteralArgumentBuilder<FabricClientCommandSource> play = LiteralArgumentBuilder.literal("play");

        for (SoundCategory soundCategory : SoundCategory.values()) {
            //categories
            play.then(makePlaySoundArguments(soundCategory));
        }

        //add command
        command.then(stop);
        command.then(play);
        ClientCommandManager.DISPATCHER.register(command);
    }

    private static LiteralArgumentBuilder<FabricClientCommandSource> makePlaySoundArguments(SoundCategory category) {
        //category and sound
        return ClientCommandManager.literal(category.getName()).then(ClientCommandManager.argument("sound", IdentifierArgumentType.identifier())
                .suggests((context, builder) -> CommandSource.suggestIdentifiers(context.getSource().getSoundIds(), builder))
                .executes(context -> playSound(context.getArgument("sound", Identifier.class), category, MinecraftClient.getInstance().player.getPos(), 1f, 1f))

                //pos
                .then(ClientCommandManager.argument("pos", Vec3ArgumentType.vec3())
                        .executes(context -> playSound(context.getArgument("sound", Identifier.class), category,
                                toAbsolutePos(context),
                                1f, 1f))

                        //volume
                        .then(ClientCommandManager.argument("volume", FloatArgumentType.floatArg(0f))
                                .executes(context -> playSound(context.getArgument("sound", Identifier.class), category,
                                        toAbsolutePos(context),
                                        context.getArgument("volume", Float.class), 1f))

                                //pitch
                                .then(ClientCommandManager.argument("pitch", FloatArgumentType.floatArg(0f, 2f))
                                        .executes(context -> playSound(context.getArgument("sound", Identifier.class), category,
                                                toAbsolutePos(context),
                                                context.getArgument("volume", Float.class), context.getArgument("pitch", Float.class)))
                                )
                        )
                )
        );
    }

    private static int stopSound(Identifier sound, SoundCategory category) {
        //stop sound
        MinecraftClient.getInstance().getSoundManager().stopSounds(sound, category);

        //feedback
        if (category != null) {
            if (sound != null) {
                sendChatMessage(new TranslatableText("commands.stopsound.success.source.sound", sound, category.getName()));
            } else {
                sendChatMessage(new TranslatableText("commands.stopsound.success.source.any", category.getName()));
            }
        } else if (sound != null) {
            sendChatMessage(new TranslatableText("commands.stopsound.success.sourceless.sound", sound));
        } else {
            sendChatMessage(new TranslatableText("commands.stopsound.success.sourceless.any"));
        }

        return 1;
    }

    private static int playSound(Identifier sound, SoundCategory category, Vec3d pos, float volume, float pitch) {
        MinecraftClient.getInstance().world.playSound(
                pos.x, pos.y, pos.z,
                new SoundEvent(sound), category,
                volume, pitch, true
        );

        return 1;
    }

    private static void sendChatMessage(Object text) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text instanceof Text t ? t : new LiteralText(text.toString()));
    }

    private static Vec3d toAbsolutePos(CommandContext<FabricClientCommandSource> context) {
        FabricClientCommandSource source = context.getSource();
        ServerCommandSource serverSource =  new ServerCommandSource(null, source.getPosition(), source.getRotation(), null, 0, null, null, null, source.getEntity());
        return context.getArgument("pos", PosArgument.class).toAbsolutePos(serverSource);
    }
}
