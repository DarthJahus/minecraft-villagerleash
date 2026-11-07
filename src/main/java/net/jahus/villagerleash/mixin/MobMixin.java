package net.jahus.villagerleash.mixin;

import net.jahus.villagerleash.config.ModConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Mob.class)
public abstract class MobMixin {

	@Unique
	private boolean villagerleash$wasLeashed;

	@Unique
	private int villagerleash$secondCounter;

	@Unique
	private int villagerleash$angerSecondCounter;

	@Inject(method = "tick", at = @At("RETURN"))
	private void villagerleash$tickLeashCosts(CallbackInfo ci) {
		Mob selfMob = (Mob) (Object) this;
		if (!(selfMob instanceof AbstractVillager self)) {
			return;
		}
		if (self.level().isClientSide()) {
			return;
		}
		if (!ModConfig.enable()) {
			villagerleash$wasLeashed = false;
			villagerleash$secondCounter = 0;
			villagerleash$angerSecondCounter = 0;
			return;
		}

		boolean leashed = self.isLeashed();
		if (!leashed) {
			villagerleash$wasLeashed = false;
			villagerleash$secondCounter = 0;
			villagerleash$angerSecondCounter = 0;
			return;
		}

		Entity holder = self.getLeashHolder();
		if (!villagerleash$wasLeashed) {
			villagerleash$wasLeashed = true;
			villagerleash$secondCounter = 0;
			villagerleash$angerSecondCounter = 0;
			if (ModConfig.angerOnLeash()) {
				villagerleash$applyAnger(self, holder);
			}
		}

		villagerleash$secondCounter++;
		if (villagerleash$secondCounter < 20) {
			return;
		}
		villagerleash$secondCounter = 0;

		float damage = ModConfig.damagePerSecond();
		if (damage > 0f && self.level() instanceof ServerLevel serverLevel) {
			DamageSource source = villagerleash$damageSource(serverLevel, holder);
			self.hurtServer(serverLevel, source, damage);
		}

		float breakChance = ModConfig.breakChancePerSecond();
		if (breakChance > 0f && self.getRandom().nextFloat() < breakChance) {
			self.dropLeash();
			return;
		}

		if (ModConfig.renewAnger() && ModConfig.angerOnLeash()) {
			villagerleash$angerSecondCounter++;
			if (villagerleash$angerSecondCounter >= ModConfig.angerRenewIntervalSeconds()) {
				villagerleash$angerSecondCounter = 0;
				villagerleash$applyAnger(self, holder);
			}
		}
	}

	@Unique
	private static void villagerleash$applyAnger(AbstractVillager self, Entity holder) {
		if (holder instanceof Player player) {
			self.setLastHurtByMob(player);
		} else if (holder instanceof net.minecraft.world.entity.LivingEntity living) {
			self.setLastHurtByMob(living);
		}
	}

	@Unique
	private static DamageSource villagerleash$damageSource(ServerLevel level, Entity holder) {
		if (holder instanceof Player player) {
			return level.damageSources().playerAttack(player);
		}
		return level.damageSources().generic();
	}
}
