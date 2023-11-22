package moonfather.humble_desert_improvements.pyramids.mixins;

import moonfather.humble_desert_improvements.ModDesertChanges;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.structures.DesertPyramidPiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DesertPyramidPiece.class)
public class TempleShaftMixin
{
    private TempleShaftMixin() { }

    @Inject(at = @At(value = "TAIL"), method = "postProcess", cancellable = false)
    public void testTail(WorldGenLevel genLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos, CallbackInfo ci)
    {
        int index = Math.abs(pos.getX() / 16 + pos.getZ() / 16) % (ModDesertChanges.DesertTempleTransformers.size() + 1);
        // x+z ensures a fixed transformation for a single start location.
        // this mixin will be called 4 times for 4 chunks containing a pyramid. it's fine so long as we stick to the same transformation.
        ////System.out.println("~~~mixinTest 1, pos==" + pos.getX() + ',' + pos.getZ() + "     index==" + index + "   y==" + pos.getY());
        if (index > 0)  // 0 is unchanged
        {
            ModDesertChanges.DesertTempleTransformers.get(index - 1).accept(genLevel, pos);
        }
    }
}
