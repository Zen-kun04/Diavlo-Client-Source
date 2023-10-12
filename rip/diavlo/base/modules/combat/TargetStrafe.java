/*    */ package rip.diavlo.base.modules.combat;
/*    */ 
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.api.value.impl.ModeValue;
/*    */ import rip.diavlo.base.events.player.UpdateEvent;
/*    */ import rip.diavlo.base.utils.McUtils;
/*    */ 
/*    */ public class TargetStrafe
/*    */   extends Module {
/*    */   EntityPlayer player;
/* 15 */   public final ModeValue speedValue = new ModeValue("Speed", this, new String[] { "0.9", "1.1", "1.3", "1.5", "1.7", "1.9", "2.1", "2.3", "2.5", "2.7", "2.9", "3.1", "3.3", "3.5", "3.7", "3.9", "4.1", "4.3", "4.5", "4.7", "4.9", "5.1", "5.3", "5.5", "5.7", "5.9", "6.1", "6.3", "6.5", "6.7", "6.9", "7.1", "7.3", "7.5", "7.7", "8.0" });
/* 16 */   public final ModeValue distance0 = new ModeValue("Distance", this, new String[] { "0.9", "1.1", "1.3", "1.5", "1.7", "1.9", "2.1", "2.3", "2.5", "2.7", "2.9", "3.1", "3.3", "3.5", "3.7", "3.9", "4.1", "4.3", "4.5", "4.7", "4.9", "5.1", "5.3", "5.5", "5.7", "5.9", "6.1", "6.3", "6.5", "6.7", "6.9", "7.1", "7.3", "7.5", "7.7", "8.0" });
/*    */   
/*    */   public TargetStrafe() {
/* 19 */     super("TargetStrafe", 0, Category.COMBAT);
/* 20 */     getValues().add(this.speedValue);
/* 21 */     this.speedValue.setValue("3.1");
/* 22 */     getValues().add(this.distance0);
/* 23 */     this.speedValue.setValue("4.1");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Subscribe
/*    */   public void onUpdate(UpdateEvent event) {
/* 30 */     this.player = McUtils.getClosestPlayerToEntity((Entity)mc.thePlayer, Double.parseDouble((String)this.distance0.get()));
/*    */     
/* 32 */     if (this.player != null && mc.thePlayer.moveForward > 0.0F) {
/* 33 */       if (mc.thePlayer.onGround) {
/* 34 */         mc.thePlayer.jump();
/*    */       }
/*    */       
/* 37 */       if (Math.sqrt(Math.pow(mc.thePlayer.posX - this.player.posX, 2.0D) + Math.pow(mc.thePlayer.posZ - this.player.posZ, 2.0D)) != 0.0D) {
/* 38 */         double c1 = (mc.thePlayer.posX - this.player.posX) / Math.sqrt(Math.pow(mc.thePlayer.posX - this.player.posX, 2.0D) + Math.pow(mc.thePlayer.posZ - this.player.posZ, 2.0D));
/* 39 */         double s1 = (mc.thePlayer.posZ - this.player.posZ) / Math.sqrt(Math.pow(mc.thePlayer.posX - this.player.posX, 2.0D) + Math.pow(mc.thePlayer.posZ - this.player.posZ, 2.0D));
/* 40 */         if (Math.sqrt(Math.pow(mc.thePlayer.posX - this.player.posX, 2.0D) + Math.pow(mc.thePlayer.posZ - this.player.posZ, 2.0D)) <= Double.parseDouble((String)this.distance0.get())) {
/* 41 */           if (mc.gameSettings.keyBindLeft.isPressed()) {
/* 42 */             mc.thePlayer.motionX = -Double.parseDouble((String)this.speedValue.get()) * s1 - 0.18D * Double.parseDouble((String)this.speedValue.get()) * c1;
/* 43 */             mc.thePlayer.motionZ = Double.parseDouble((String)this.speedValue.get()) * c1 - 0.18D * Double.parseDouble((String)this.speedValue.get()) * s1;
/*    */           } else {
/* 45 */             mc.thePlayer.motionX = Double.parseDouble((String)this.speedValue.get()) * s1 - 0.18D * Double.parseDouble((String)this.speedValue.get()) * c1;
/* 46 */             mc.thePlayer.motionZ = -Double.parseDouble((String)this.speedValue.get()) * c1 - 0.18D * Double.parseDouble((String)this.speedValue.get()) * s1;
/*    */           } 
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSuffix() {
/* 56 */     return (String)this.distance0.get();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\combat\TargetStrafe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */