package de.cas_ual_ty.ydm.duel.screen.animation;




import de.cas_ual_ty.ydm.YDM;
import de.cas_ual_ty.ydm.clientutil.ClientProxy;
import de.cas_ual_ty.ydm.clientutil.YdmBlitUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;

public class RemoveTokenAnimation extends Animation
{
    public float centerPosX;
    public float centerPosY;
    public int size;
    public int endSize;
    
    public RemoveTokenAnimation(float centerPosX, float centerPosY, int size, int endSize)
    {
        super(ClientProxy.specialAnimationLength);
        
        this.centerPosX = centerPosX;
        this.centerPosY = centerPosY;
        this.size = size;
        this.endSize = endSize;
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        var ms = guiGraphics.pose();
        double relativeTickTime = (tickTime + partialTicks) / maxTickTime;
        
        // [0, 1/2pi]
        double cosTime1 = 0.5D * Math.PI * relativeTickTime;
        // [0, 1]
        float alpha = (float) (Math.cos(cosTime1));
        
        float size = (float) relativeTickTime * (endSize - this.size) + this.size;
        float halfSize = 0.5F * size;
        
        ms.pushPose();
        
        ms.translate((float)(centerPosX), (float)(centerPosY));
        
        
        
        
        
        YdmBlitUtil.fullBlit(ms, getTexture(), -halfSize, -halfSize, size, size);
        
        
        
        ms.popPose();
    }
    
    public Identifier getTexture()
    {
        return Identifier.fromNamespaceAndPath(YDM.MOD_ID, "textures/gui/action_animations/remove_token.png");
    }
}
