package hu.barnabasd.randomyzer;

import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DropSound {

    public static void Execute(@NotNull List<ServerPlayer> players) {

        // Very advanced packet and networking stuff

        if (RandomyzerCommand.enableDropSound.GetValue())
            players.forEach(p -> p.connection.send(new ClientboundSoundPacket
                (
                    Holder.direct(SoundEvents.EXPERIENCE_ORB_PICKUP),
                    SoundSource.MASTER, p.getX(), p.getY(), p.getZ(),
                    1f, 1f, p.level().getRandom().nextLong()
                )));
    }

}
