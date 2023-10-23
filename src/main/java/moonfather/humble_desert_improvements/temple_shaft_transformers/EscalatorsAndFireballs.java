package moonfather.humble_desert_improvements.temple_shaft_transformers;

import moonfather.humble_desert_improvements.Constants;
import moonfather.humble_desert_improvements.our_blocks.Repository;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class EscalatorsAndFireballs
{
    public static void setupFireChargeTrap(WorldGenLevel genLevel, BlockPos posBlueTerracotta)
    {
        // first pick one direction for one dispenser. can't just call Direction.getRandom because this gets called 4 times.
        int posModulo = (posBlueTerracotta.getX() + 3 * posBlueTerracotta.getZ()) % 4; // 0..3
        Direction direction = directionList[posModulo];
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        // step 1: lose the 3x3 tnt below
        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dz = -1; dz <= 1; dz++)
            {
                mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11 - 2, posBlueTerracotta.getZ() + dz);
                genLevel.setBlock(mpos, Blocks.SANDSTONE.defaultBlockState(), 2);
            }
        }
        // step 2: trapped chest
        int sx = direction.getStepX(), sz = direction.getStepZ();
        mpos.set(posBlueTerracotta.getX() + 2 * sx, posBlueTerracotta.getY() + -11, posBlueTerracotta.getZ() + 2 * sz);
        if (genLevel.getBlockEntity(mpos) instanceof RandomizableContainerBlockEntity rc)
        {
            Clearable.tryClear(rc);
            rc.setLootTable(null, 0); // without these two lines, original loot would just drop
        }
        genLevel.setBlock(mpos, Blocks.SAND.defaultBlockState(), 2); // need to
        genLevel.setBlock(mpos, Blocks.TRAPPED_CHEST.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite()), 2);
        if (genLevel.getBlockEntity(mpos) instanceof RandomizableContainerBlockEntity rc2)
        {
            rc2.setLootTable(BuiltInLootTables.DESERT_PYRAMID, genLevel.getRandom().nextLong());
        }
        // step 3: redstone escalator
        mpos.set(posBlueTerracotta.getX() + 3 * sx, posBlueTerracotta.getY() + -11, posBlueTerracotta.getZ() + 3 * sz);
        genLevel.setBlock(mpos, Repository.SIGNAL_TRANSFER_RAISER.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite()), 2);
        // step 4: crumbling dispenser
        mpos.set(posBlueTerracotta.getX() + 3 * sx, posBlueTerracotta.getY() + -11 + 1, posBlueTerracotta.getZ() + 3 * sz);
        genLevel.setBlock(mpos, Blocks.DISPENSER.defaultBlockState().setValue(BlockStateProperties.FACING, direction.getOpposite()), 2);
        BlockEntity dispenser = genLevel.getBlockEntity(mpos);
        if (dispenser != null)
        {
            dispenser.getPersistentData().putString(Constants.NBT.BEHAVIOR, Constants.NBT.BEHAVIOR_CRUMBLE);
            if (dispenser instanceof DispenserBlockEntity dispenserBlockEntity)
            {
                dispenserBlockEntity.addItem(new ItemStack(Items.FIRE_CHARGE, 1 + genLevel.getRandom().nextInt(2)));
            }
        }
    }
    private static final Direction[] directionList = new Direction[] { Direction.WEST, Direction.NORTH, Direction.EAST, Direction.SOUTH };
}
