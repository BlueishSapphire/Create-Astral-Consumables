package com.github.notroy.astralconsumables.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;

public class RecallItem extends Item {
	public RecallItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 20;
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		user.setCurrentHand(hand);
		return TypedActionResult.consume(itemStack);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		ItemStack itemStack = super.finishUsing(stack, world, user);
		
		if (!world.isClient && user instanceof ServerPlayerEntity player) {
			ServerWorld serverWorld = (ServerWorld) world;
			
			double oldX = player.getX();
			double oldY = player.getY();
			double oldZ = player.getZ();

			BlockPos spawnPosition = player.getSpawnPointPosition();
			float spawnAngle = player.getSpawnAngle();
			
			if (spawnPosition == null) {
				spawnPosition = serverWorld.getSpawnPos();
				spawnAngle = serverWorld.getSpawnAngle();
			}
			
			Optional<Vec3d> spawnPointOptional = PlayerEntity.findRespawnPosition(
				serverWorld,
				spawnPosition,
				spawnAngle,
				player.isSpawnForced(),
				player.isAlive()
			);
			
			if (spawnPointOptional.isPresent()) {
				Vec3d spawnPoint = spawnPointOptional.get();
				
				double newX = spawnPoint.getX();
				double newY = spawnPoint.getY();
				double newZ = spawnPoint.getZ();
				
				if (player.hasVehicle()) {
					player.stopRiding();
				}
				
				player.teleport(serverWorld, newX, newY, newZ, 0, 0);
				
				SoundEvent soundEvent = SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
				world.playSound(null, oldX, oldY, oldZ, soundEvent, SoundCategory.PLAYERS, 1.0f, 1.0f);
				player.playSound(soundEvent, 1.0f, 1.0f);
				
				player.getItemCooldownManager().set(this, 20);
			}
		}
		
		return itemStack;
	}
}
