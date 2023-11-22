package moonfather.humble_desert_improvements.pyramids.temple_shaft_transformers;

import moonfather.humble_desert_improvements.Constants;
import moonfather.humble_desert_improvements.pyramids.our_blocks.Repository;
import moonfather.humble_desert_improvements.pyramids.utility.TempleShaftUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ArrowsFromBottomLevel
{
    public static void setupEightDispensersOriginal(WorldGenLevel genLevel, BlockPos posBlueTerracotta)
    {
        // copy-paste: fix blue terracotta location.
        int fixBlueTerracottaPosition = TempleShaftUtilities.getBlueTerracottaOffset(genLevel, posBlueTerracotta);
        if (fixBlueTerracottaPosition == TempleShaftUtilities.NOT_FOUND) { posBlueTerracotta = posBlueTerracotta.offset(0, fixBlueTerracottaPosition, 0); }
        if (fixBlueTerracottaPosition != 0) { return; }
        //////////////////////////////

        // step 1: lose the 3x3 tnt below
        TempleShaftUtilities.lose3x3TNT(genLevel, posBlueTerracotta, 30);
        // dispensers
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            for (int one = -1; one <= 1; one += 2)
            {
                // step 2: dispensers and arrows
                int dx = direction.getStepX() != 0 ? direction.getStepX() * 2 : one;     //todo: 2->3 in vent variant, vent on 2, pipe below
                int dz = direction.getStepZ() != 0 ? direction.getStepZ() * 2 : one;
                mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11, posBlueTerracotta.getZ() + dz);
                genLevel.setBlock(mpos, Blocks.DISPENSER.defaultBlockState().setValue(BlockStateProperties.FACING, direction.getOpposite()), 2);
                BlockEntity dispenser = genLevel.getBlockEntity(mpos);
                if (dispenser != null)
                {
                    dispenser.getPersistentData().putString(Constants.NBT.BEHAVIOR, Constants.NBT.BEHAVIOR_CRUMBLE);
                    if (dispenser instanceof DispenserBlockEntity dispenserBlockEntity)
                    {
                        if (genLevel.getRandom().nextInt(8) == 5)
                        {
                            ItemStack tipped = PotionUtils.setPotion(new ItemStack(Items.TIPPED_ARROW, 1), Potions.POISON);
                            dispenserBlockEntity.addItem(tipped);
                        }
                        dispenserBlockEntity.addItem(new ItemStack(Items.ARROW, 1 + genLevel.getRandom().nextInt(3)));
                    }
                }
                // step 3: redstone escalators
                mpos.move(0, -1, 0);
                genLevel.setBlock(mpos, Repository.SIGNAL_TRANSFER_RAISER.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite()), 2);
            }
        }

        // step 4: sandstone pressure plates
        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dz = -1; dz <= 1; dz += 2)
            {
                mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11, posBlueTerracotta.getZ() + dz);
                genLevel.setBlock(mpos, Repository.PRESSURE_PLATE.get().defaultBlockState(), 2);
                mpos.move(0, -1, 0);
                genLevel.setBlock(mpos, Repository.SIGNAL_TRANSFER_PIPE.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, dx < 0 ? Direction.EAST : Direction.WEST), 2);
            }
        }
    }



    public static void setupEightDispensers(WorldGenLevel genLevel, BlockPos posBlueTerracotta)
    {
        // copy-paste: fix blue terracotta location.
        int fixBlueTerracottaPosition = TempleShaftUtilities.getBlueTerracottaOffset(genLevel, posBlueTerracotta);
        if (fixBlueTerracottaPosition == TempleShaftUtilities.NOT_FOUND) { posBlueTerracotta = posBlueTerracotta.offset(0, fixBlueTerracottaPosition, 0); }
        if (fixBlueTerracottaPosition != 0) { return; }
        //////////////////////////////

        // step 1: lose the 3x3 tnt below
        TempleShaftUtilities.lose3x3TNT(genLevel, posBlueTerracotta, 70);
        // dispensers
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            for (int one = -1; one <= 1; one += 2)
            {
                // step 2: dispensers and arrows
                int dx = direction.getStepX() != 0 ? direction.getStepX() * 3 : one;
                int dz = direction.getStepZ() != 0 ? direction.getStepZ() * 3 : one;
                mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11, posBlueTerracotta.getZ() + dz);
                genLevel.setBlock(mpos, Blocks.DISPENSER.defaultBlockState().setValue(BlockStateProperties.FACING, direction.getOpposite()), 2);
                BlockEntity dispenser = genLevel.getBlockEntity(mpos);
                if (dispenser != null)
                {
                    dispenser.getPersistentData().putString(Constants.NBT.BEHAVIOR, Constants.NBT.BEHAVIOR_CRUMBLE);
                    if (dispenser instanceof DispenserBlockEntity dispenserBlockEntity)
                    {
                        if (genLevel.getRandom().nextInt(8) == 5)
                        {
                            ItemStack tipped = PotionUtils.setPotion(new ItemStack(Items.TIPPED_ARROW, 1), Potions.POISON);
                            dispenserBlockEntity.addItem(tipped);
                        }
                        dispenserBlockEntity.addItem(new ItemStack(Items.ARROW, 1 + genLevel.getRandom().nextInt(3)));
                    }
                }
                // step 3: redstone escalators
                mpos.move(0, -1, 0);
                genLevel.setBlock(mpos, Repository.SIGNAL_TRANSFER_RAISER.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite()), 2);
                // step 4: vents
                dx = direction.getStepX() != 0 ? direction.getStepX() * 2 : one;
                dz = direction.getStepZ() != 0 ? direction.getStepZ() * 2 : one;
                mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11, posBlueTerracotta.getZ() + dz);
                genLevel.setBlock(mpos, Repository.VENT.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite()), 2);
                // step 5: pipes
                mpos.move(0, -1, 0);
                genLevel.setBlock(mpos, Repository.SIGNAL_TRANSFER_PIPE.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite()), 2);
            }
        }

        // step 6: sandstone pressure plates
        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dz = -1; dz <= 1; dz += 2)
            {
                mpos.set(posBlueTerracotta.getX() + dx, posBlueTerracotta.getY() + -11, posBlueTerracotta.getZ() + dz);
                genLevel.setBlock(mpos, Repository.PRESSURE_PLATE.get().defaultBlockState(), 2);
                mpos.move(0, -1, 0);
                genLevel.setBlock(mpos, Repository.SIGNAL_TRANSFER_PIPE.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, dx < 0 ? Direction.EAST : Direction.WEST), 2);
            }
        }
    }
}
