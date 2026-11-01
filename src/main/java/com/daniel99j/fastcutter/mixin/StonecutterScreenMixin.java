package com.daniel99j.fastcutter.mixin;

import com.daniel99j.fastcutter.TryRepeatCraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.StonecutterMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StonecutterScreen.class)
public abstract class StonecutterScreenMixin extends AbstractContainerScreen<StonecutterMenu> implements TryRepeatCraft {
	public StonecutterScreenMixin(StonecutterMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@Override
	public void fastcutter$repeatCraft(KeyEvent event) {
		((TryRepeatCraft) this.menu).fastcutter$repeatCraft(event);
	}
}