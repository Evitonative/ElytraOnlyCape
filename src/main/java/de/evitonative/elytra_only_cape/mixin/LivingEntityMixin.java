package de.evitonative.elytra_only_cape.mixin;

import de.evitonative.elytra_only_cape.CapeToggleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "onEquipItem", at = @At("HEAD"))
    private void onEquip(EquipmentSlot slot, ItemStack oldStack, ItemStack stack, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!(entity instanceof Player)) return;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;
        if (!minecraft.player.getUUID().equals(entity.getUUID())) return;

        if (slot != EquipmentSlot.CHEST) return;

        boolean elytraIsEquipped = stack.is(Items.ELYTRA);
        CapeToggleHelper.updateCapeVisibility(elytraIsEquipped);
    }
}
