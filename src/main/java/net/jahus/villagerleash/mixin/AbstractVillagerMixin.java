package net.jahus.villagerleash.mixin;

import net.jahus.villagerleash.config.ModConfig;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractVillager.class)
public abstract class AbstractVillagerMixin {

	@Inject(method = "canBeLeashed", at = @At("HEAD"), cancellable = true)
	private void villagerleash$allowLeash(CallbackInfoReturnable<Boolean> cir) {
		if (ModConfig.enable()) {
			cir.setReturnValue(true);
		}
	}
}
