package pl.xyundy.squaredadditions.slabs;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import virtuoel.statement.api.StateRefresher;

import java.util.HashMap;
import java.util.Map;

public class Slabs {
    public static final RuntimeResourcePack SLABS_RESOURCES = RuntimeResourcePack.create("squaredadditionsslabs:resources", 15);

    public static final Map<PlayerEntity, SlabLockEnum> slabLockPosition = new HashMap<>();

    public static void initialize() {
        for (Block block : Registries.BLOCK) {
            Util.registerSlab(block);
        }

        RegistryEntryAddedCallback.event(Registries.BLOCK).register((raw, id, block) -> {
            Util.registerSlab(block);
        });

        StateRefresher.INSTANCE.reorderBlockStates();

        RRPCallback.BETWEEN_MODS_AND_USER.register(a -> a.add(SLABS_RESOURCES));

        ServerPlayNetworking.registerGlobalReceiver(new Identifier("squaredadditions", "slab_lock"), (server, player, handler, buf, responseSender) -> {
            SlabLockEnum slabLockBuf = buf.readEnumConstant(SlabLockEnum.class);
            slabLockPosition.put(player, slabLockBuf);
        });
    }
}