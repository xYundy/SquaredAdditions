package pl.xyundy.squaredadditions.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.SlabType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import pl.xyundy.squaredadditions.block.MixedSlabBlock;
import pl.xyundy.squaredadditions.block.ModBlocks;

import static net.minecraft.state.property.Properties.SLAB_TYPE;

public class MixedSlabBlockEntity extends BlockEntity {
    public BlockState topSlabState;
    public BlockState bottomSlabState;

    public MixedSlabBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.MIXED_SLAB_BLOCK_ENTITY, pos, state);

        this.topSlabState = ((MixedSlabBlock)state.getBlock()).getTopSlabState().with(SLAB_TYPE, SlabType.TOP);
        this.bottomSlabState = ((MixedSlabBlock)state.getBlock()).getBottomSlabState().with(SLAB_TYPE, SlabType.BOTTOM);
    }

    public MixedSlabBlockEntity(BlockPos pos, BlockState state, BlockState topSlabState, BlockState bottomSlabState) {
        super(ModBlocks.MIXED_SLAB_BLOCK_ENTITY, pos, state);

        this.topSlabState = topSlabState.with(SLAB_TYPE, SlabType.TOP);;
        this.bottomSlabState = bottomSlabState.with(SLAB_TYPE, SlabType.BOTTOM);
    }

    public void setTopSlabState(BlockState topSlabState) {
        this.topSlabState = topSlabState;
    }

    public void setBottomSlabState(BlockState bottomSlabState) {
        this.bottomSlabState = bottomSlabState;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.put("top_slab_state", NbtHelper.fromBlockState(topSlabState));
        nbt.put("bottom_slab_state", NbtHelper.fromBlockState(bottomSlabState));

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        topSlabState    = BlockState.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("top_slab_state")).result().orElse(null);
        bottomSlabState = BlockState.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("bottom_slab_state")).result().orElse(null);
    }
}
