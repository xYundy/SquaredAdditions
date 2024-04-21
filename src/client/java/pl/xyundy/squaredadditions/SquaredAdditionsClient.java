package pl.xyundy.squaredadditions;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import pl.xyundy.squaredadditions.slabs.SlabLockEnum;

public class SquaredAdditionsClient implements ClientModInitializer {
	final ModContainer container = FabricLoader.getInstance().getModContainer(SquaredAdditions.MOD_ID).get();

	public static KeyBinding SLAB_LOCK_KEYBIND = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.squaredadditions.slab_lock", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K, KeyBinding.GAMEPLAY_CATEGORY)
	);

	private static Boolean validKeyPress = true;
	public static SlabLockEnum clientSlabLockPosition = SlabLockEnum.DEFAULT_AUTO;

	private void sendKeybind(SlabLockEnum lockedPosition) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeEnumConstant(lockedPosition);
		ClientPlayNetworking.send(new Identifier("squaredadditions", "slab_lock"), buf);
	}

	private void setKeybind(MinecraftClient client) {
		clientSlabLockPosition = clientSlabLockPosition.loop(client.options.sneakKey.isPressed());
		sendKeybind(clientSlabLockPosition);
		client.player.sendMessage( Text.translatable("text.squaredadditions.slab_lock."+ clientSlabLockPosition.toString()), true);
	}

	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("squaredadditions", "slabs"), container, Text.literal("Squared Additions Slabs (Built-In)"), ResourcePackActivationType.DEFAULT_ENABLED);

		HudRenderCallback.EVENT.register((context, tickDelta) -> {

		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null) {
				if (SLAB_LOCK_KEYBIND.isPressed() && validKeyPress) {
					ItemStack heldItem = client.player.getStackInHand(client.player.getActiveHand());
					if (heldItem != null && !heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem && ((BlockItem) heldItem.getItem()).getBlock() instanceof SlabBlock) {
						validKeyPress = false;
						setKeybind(client);
					}
				}
				if (!SLAB_LOCK_KEYBIND.isPressed() && !validKeyPress) {
					validKeyPress = true;
				}
			}
		});
	}
}