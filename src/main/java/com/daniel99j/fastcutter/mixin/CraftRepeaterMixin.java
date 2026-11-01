package com.daniel99j.fastcutter.mixin;

import com.daniel99j.fastcutter.StonecutterFastCraft;
import com.daniel99j.fastcutter.TryRepeatCraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public class CraftRepeaterMixin {
	@Inject(at = @At("TAIL"), method = "keyPressed", cancellable = true)
	private void repeatCraft(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
		if(!cir.getReturnValue() && event.isSelection() && ((Object) this) instanceof TryRepeatCraft repeat) {
			repeat.fastcutter$repeatCraft(event);
			cir.setReturnValue(true);
		}
	}
}