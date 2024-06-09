package hu.barnabasd.randomyzermod;

import hu.barnabasd.randomyzermod.config.Setting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.List;

public class CycleSounds {

    public static void PlaySounds(List<ServerPlayer> players) {
        SoundEvent sound = SoundEvents.EXPERIENCE_ORB_PICKUP;
        ProjectStrings.CycleSound soundType = (ProjectStrings.CycleSound)Setting.ByName(ProjectStrings.CycleSoundId).getValue();
        if (soundType == ProjectStrings.CycleSound.none) return;
        if (soundType == ProjectStrings.CycleSound.itemPickup) sound = SoundEvents.ITEM_PICKUP;
        SoundEvent finalSound = sound;
        players.forEach(player -> player.level().playSound(null, player.blockPosition().above(), finalSound, SoundSource.MASTER));
    }

}
