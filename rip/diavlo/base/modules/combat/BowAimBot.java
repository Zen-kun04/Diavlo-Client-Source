/*    */ package rip.diavlo.base.modules.combat;
/*    */ 
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.Vec3;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.api.value.impl.ModeValue;
/*    */ import rip.diavlo.base.events.player.UpdateEvent;
/*    */ import rip.diavlo.base.utils.McUtils;
/*    */ 
/*    */ public class BowAimBot
/*    */   extends Module
/*    */ {
/*    */   EntityPlayer p;
/* 21 */   public final ModeValue distance0 = new ModeValue("Distance", this, new String[] { "0.9", "1.1", "1.3", "1.5", "1.7", "1.9", "2.1", "2.3", "2.5", "2.7", "2.9", "3.1", "3.3", "3.5", "3.7", "3.9", "4.1", "4.3", "4.5", "4.7", "4.9", "5.1", "5.3", "5.5", "5.7", "5.9", "6.1", "6.3", "6.5", "6.7", "6.9", "7.1", "7.3", "7.5", "7.7", "8.0", "10.0", "15.0", "20.0" });
/*    */   
/*    */   public BowAimBot() {
/* 24 */     super("BowAimBot", 0, Category.COMBAT);
/* 25 */     getValues().add(this.distance0);
/* 26 */     this.distance0.setValue("5.0");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Subscribe
/*    */   public void onUpdate(UpdateEvent event) {
/* 34 */     Double dist = Double.valueOf(Double.parseDouble((String)this.distance0.get()));
/* 35 */     this.p = McUtils.getClosestPlayerToEntity((Entity)mc.thePlayer, dist.doubleValue());
/*    */     
/* 37 */     if (this.p != null && !this.p.isDead && this.p.getHealth() >= 0.0F && mc.thePlayer
/* 38 */       .getHealth() > 0.0F && ((mc.thePlayer.onGround && mc.thePlayer.inventory
/* 39 */       .hasItem(Item.getItemById(262))) || (Minecraft.getMinecraft()).thePlayer.capabilities.isCreativeMode) && mc.thePlayer.inventory
/* 40 */       .getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof net.minecraft.item.ItemBow && !this.p.isDead && this.p
/*    */       
/* 42 */       .getHealth() >= 0.0F && canEntityBeSeen((Entity)mc.thePlayer)) {
/* 43 */       double diffX = this.p.posX - mc.thePlayer.posX;
/*    */ 
/*    */ 
/*    */       
/* 47 */       double diffY = this.p.posY + ((dist.doubleValue() < 6.0D) ? 1.5D : ((dist.doubleValue() <= 10.0D) ? 1.6D : 1.7D)) - (mc.thePlayer.getEntityBoundingBox()).minY + mc.thePlayer.getEyeHeight();
/* 48 */       double diffZ = this.p.posZ - mc.thePlayer.posZ;
/* 49 */       double sqrt = Math.sqrt(diffX * diffX + diffZ * diffZ);
/* 50 */       float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
/* 51 */       float pitch = (float)-(Math.atan2(diffY, sqrt) * 180.0D / Math.PI);
/* 52 */       mc.thePlayer.rotationYaw += MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);
/* 53 */       mc.thePlayer.rotationPitch += MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canEntityBeSeen(Entity entityIn) {
/* 59 */     EntityPlayerSP entityPlayerSP = mc.thePlayer;
/* 60 */     return (mc.theWorld.rayTraceBlocks(new Vec3(((EntityPlayer)entityPlayerSP).posX, ((EntityPlayer)entityPlayerSP).posY + entityPlayerSP.getEyeHeight(), ((EntityPlayer)entityPlayerSP).posZ), new Vec3(entityIn.posX, entityIn.posY + entityIn
/* 61 */           .getEyeHeight(), entityIn.posZ), false) == null);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSuffix() {
/* 66 */     return (String)this.distance0.get();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\combat\BowAimBot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */