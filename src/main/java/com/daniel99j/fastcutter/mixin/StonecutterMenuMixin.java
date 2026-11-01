package com.daniel99j.fastcutter.mixin;

import com.daniel99j.fastcutter.StonecutterFastCraft;
import com.daniel99j.fastcutter.TryRepeatCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SelectableRecipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(StonecutterMenu.class)
public abstract class StonecutterMenuMixin extends AbstractContainerMenu implements TryRepeatCraft {
	@Shadow
	@Final
	private Slot inputSlot;

	protected StonecutterMenuMixin(@Nullable MenuType<?> menuType, int containerId) {
		super(menuType, containerId);
	}

	@Shadow
	public abstract ItemStack quickMoveStack(Player player, int slotIndex);

	@Shadow
	@Final
	private DataSlot selectedRecipeIndex;
	@Shadow
	@Final
	private static int INV_SLOT_START;
	@Shadow
	@Final
	private static int USE_ROW_SLOT_END;

	@Unique
    private int lastSelected = -1;
	@Unique
    private Item lastItem = null;

	@Override
	public void fastcutter$repeatCraft(KeyEvent event) {
		if(this.lastSelected != -1 && this.lastItem != null && Minecraft.getInstance().player != null && Minecraft.getInstance().player.getInventory().hasAnyOf(Set.of(lastItem)) && this.inputSlot.getItem().isEmpty()) {
			int index = -1;
			for (int i = INV_SLOT_START; i < USE_ROW_SLOT_END; ++i) {
				if (this.getSlot(i).getItem().is(this.lastItem)) {
					index = i;
					break;
				}
			}
			if(index != -1) {
                assert Minecraft.getInstance().gameMode != null;
                Minecraft.getInstance().gameMode.handleContainerInput(this.containerId, index, 0, ContainerInput.QUICK_MOVE, Minecraft.getInstance().player);
				if(this.clickMenuButton(Minecraft.getInstance().player, this.lastSelected)) {
					AbstractWidget.playButtonClickSound(Minecraft.getInstance().getSoundManager());
					Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.containerId, this.lastSelected);
				}
			}
		}
		event.hasShiftDown();
	}

	@Inject(at = @At("TAIL"), method = "clickMenuButton")
	private void repeatCraft(Player player, int buttonId, CallbackInfoReturnable<Boolean> cir) {
		if(cir.getReturnValue()) {
			this.lastItem = this.inputSlot.getItem().getItem();
			this.lastSelected = this.selectedRecipeIndex.get();
		}
	}
}