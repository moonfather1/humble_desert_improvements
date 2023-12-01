package moonfather.humble_desert_improvements;

import net.minecraft.resources.ResourceLocation;

public class Constants
{ 
    public static final String MODID = "humble_desert_improvements";
    public static final ResourceLocation SUS_SAND_TABLE = new ResourceLocation("humble_desert_improvements:archaeology/sus_sand_in_husk_trap");

    public static class NBT
    {
        public static final String BEHAVIOR = "pyramid_behavior";
        public static final String BEHAVIOR_MOVE = "move";
        public static final String BEHAVIOR_CRUMBLE = "crumble";
        public static final String BEHAVIOR_MOVE_HANDLER = "pyramid_behavior_move_handler";
        public static final String BEHAVIOR_MOVE_CANCELS_BREAKING = "pyramid_behavior_move_cancels_destruction";

        public static final String BOOLEAN_YES = "yes";
        public static final String BOOLEAN_NO = "no";
    }
}
