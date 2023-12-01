package moonfather.humble_desert_improvements.pyramids.temple_shaft_transformers;

import moonfather.humble_desert_improvements.Constants;
import moonfather.humble_desert_improvements.pyramids.our_blocks.Repository;
import moonfather.humble_desert_improvements.pyramids.utility.TempleShaftUtilities;
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
        // copy-paste: fix blue terracotta location.
        int fixBlueTerracottaPosition = TempleShaftUtilities.getBlueTerracottaOffset(genLevel, posBlueTerracotta);
        if (fixBlueTerracottaPosition == TempleShaftUtilities.NOT_FOUND) { return; }
        if (fixBlueTerracottaPosition != 0) { posBlueTerracotta = posBlueTerracotta.offset(0, fixBlueTerracottaPosition, 0); }
        //////////////////////////////

        // this is called four times for four chunks that the pyramid takes up. while we can just have these run 4x, there are two reasons not to:
        // 1) call dispenserBlockEntity.addItem(arrow); is cumulative. not a big deal but let's not.
        // 2) setupOneDirectionTrap(randomDirection) results in 2-3 dispensers instead of one. solvable by getting direction from x and z. random enough.
        // 3) speed. let's not lag the game if we can easily avoid it.
        if (TempleShaftUtilities.isTNTRemoved(genLevel, posBlueTerracotta)) { return; }
        //////////////////////////////

        // first pick one direction for one dispenser. we can just call Direction.getRandom because this no longer gets called 4 times.
        // previously we had to do things like   posModulo = (posBlueTerracotta.getX() + 3 * posBlueTerracotta.getZ()) % 4
        Direction direction;
        do direction = Direction.getRandom(genLevel.getRandom()); while (direction.getStepY() != 0);
        // call main
        setupFireChargeTrapInternal(genLevel, posBlueTerracotta, direction);
        int random = genLevel.getRandom().nextInt(100);
        if (random < 66)
        {
            setupFireChargeTrapInternal(genLevel, posBlueTerracotta, direction.getClockWise());
        }
        if (random < 33)
        {
            setupFireChargeTrapInternal(genLevel, posBlueTerracotta, direction.getCounterClockWise());
        }
    }



    public static void setupFireChargeTrapInternal(WorldGenLevel genLevel, BlockPos posBlueTerracotta, Direction direction)
    {
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        // step 1: lose the 3x3 tnt below
        TempleShaftUtilities.lose3x3TNT(genLevel, posBlueTerracotta, 20);
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
        // step 5: facade
        mpos.set(posBlueTerracotta.getX() + 2 * sx, posBlueTerracotta.getY() + -11 + 1, posBlueTerracotta.getZ() + 2 * sz);
        genLevel.setBlock(mpos, Repository.FACADE.get().defaultBlockState().setValue(BlockStateProperties.FACING, direction.getOpposite()), 2);

    }
}
