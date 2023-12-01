package moonfather.humble_desert_improvements.pyramids.mixins;

import com.minecraftserverzone.scorpions.mobs.emperor_scorpion.EmperorScorpion;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(EmperorScorpion.class)
public abstract class Scorpion2Mixin extends Animal
{
    @Shadow @Final private static EntityDataAccessor<Integer> DATA_HIDE;
    @Shadow public abstract int getHideTick();
    @Shadow public abstract void setHideTick(int p_30398_);

    private Scorpion2Mixin(EntityType<? extends Animal> p_27557_, Level p_27558_) { super(p_27557_, p_27558_);  }



    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Animal;tick()V", shift = At.Shift.AFTER), method = "tick()V", remap = false, cancellable = true)
    private void tickSpecial(CallbackInfo ci)
    {
        if (! this.level().isClientSide && this.goalSelector.getAvailableGoals().size() == 2)
        {
            if (this.getHideTick() == 0 && this.tickCount % 10 == 7)
            {
                this.entityData.set(DATA_HIDE, 0, true);
            }
            if (this.getHideTick() != 0)
            {
                this.setHideTick(0);
            }
            ci.cancel();
        }
    }
}
