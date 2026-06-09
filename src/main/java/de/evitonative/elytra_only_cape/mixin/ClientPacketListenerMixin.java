package de.evitonative.elytra_only_cape.mixin;

import de.evitonative.elytra_only_cape.CapeToggleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleContainerContent", at = @At("TAIL"))
    private void onInventoryLoaded(ClientboundContainerSetContentPacket packet, CallbackInfo ci){
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null) return;

        ItemStack chestItem = player.getItemBySlot(EquipmentSlot.CHEST);

        boolean elytraIsEquipped = chestItem.is(Items.ELYTRA);
        CapeToggleHelper.updateCapeVisibility(elytraIsEquipped);
    }
}
