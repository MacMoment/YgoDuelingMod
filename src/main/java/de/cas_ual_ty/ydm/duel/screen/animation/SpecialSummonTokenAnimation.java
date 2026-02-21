package de.cas_ual_ty.ydm.duel.screen.animation;

import de.cas_ual_ty.ydm.YDM;
import net.minecraft.resources.Identifier;

public class SpecialSummonTokenAnimation extends SpecialSummonAnimation
{
    public SpecialSummonTokenAnimation(float centerPosX, float centerPosY, int size, int endSize)
    {
        super(centerPosX, centerPosY, size, endSize);
    }
    
    @Override
    public Identifier getTexture()
    {
        return Identifier.fromNamespaceAndPath(YDM.MOD_ID, "textures/gui/action_animations/special_summon_token.png");
    }
}
