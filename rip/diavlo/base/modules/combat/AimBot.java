/*    */ package rip.diavlo.base.modules.combat;
/*    */ 
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.Vec3;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.api.value.impl.ModeValue;
/*    */ import rip.diavlo.base.events.player.UpdateEvent;
/*    */ import rip.diavlo.base.utils.McUtils;
/*    */ 
/*    */ public class AimBot
/*    */   extends Module {
/*    */   EntityPlayer p;
/* 18 */   public final ModeValue distance0 = new ModeValue("Distance", this, new String[] { "0.9", "1.1", "1.3", "1.5", "1.7", "1.9", "2.1", "2.3", "2.5", "2.7", "2.9", "3.1", "3.3", "3.5", "3.7", "3.9", "4.1", "4.3", "4.5", "4.7", "4.9", "5.1", "5.3", "5.5", "5.7", "5.9", "6.1", "6.3", "6.5", "6.7", "6.9", "7.1", "7.3", "7.5", "7.7", "8.0" });
/*    */   
/*    */   public AimBot() {
/* 21 */     super("AimBot", 0, Category.COMBAT);
/* 22 */     getValues().add(this.distance0);
/* 23 */     this.distance0.setValue("3.1");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Subscribe
/*    */   public void onUpdate(UpdateEvent event) {
/* 30 */     this.p = McUtils.getClosestPlayerToEntity((Entity)mc.thePlayer, Double.parseDouble((String)this.distance0.get()));
/*    */     
/* 32 */     if (this.p != null && !this.p.isDead && this.p.getHealth() >= 0.0F && canEntityBeSeen((Entity)mc.thePlayer)) {
/* 33 */       double diffX = this.p.posX - mc.thePlayer.posX;
/*    */ 
/*    */       
/* 36 */       double diffY = this.p.posY + 1.5D - (mc.thePlayer.getEntityBoundingBox()).minY + mc.thePlayer.getEyeHeight();
/* 37 */       double diffZ = this.p.posZ - mc.thePlayer.posZ;
/* 38 */       double sqrt = Math.sqrt(diffX * diffX + diffZ * diffZ);
/* 39 */       float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
/* 40 */       float pitch = (float)-(Math.atan2(diffY, sqrt) * 180.0D / Math.PI);
/* 41 */       mc.thePlayer.rotationYaw += MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);
/* 42 */       mc.thePlayer.rotationPitch += MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canEntityBeSeen(Entity entityIn) {
/* 48 */     EntityPlayerSP entityPlayerSP = mc.thePlayer;
/* 49 */     return (mc.theWorld.rayTraceBlocks(new Vec3(((EntityPlayer)entityPlayerSP).posX, ((EntityPlayer)entityPlayerSP).posY + entityPlayerSP.getEyeHeight(), ((EntityPlayer)entityPlayerSP).posZ), new Vec3(entityIn.posX, entityIn.posY + entityIn
/* 50 */           .getEyeHeight(), entityIn.posZ), false) == null);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSuffix() {
/* 55 */     return (String)this.distance0.get();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\combat\AimBot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */