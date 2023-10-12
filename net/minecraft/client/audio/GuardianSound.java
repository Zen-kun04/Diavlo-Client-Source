/*    */ package net.minecraft.client.audio;
/*    */ 
/*    */ import net.minecraft.entity.monster.EntityGuardian;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class GuardianSound
/*    */   extends MovingSound
/*    */ {
/*    */   private final EntityGuardian guardian;
/*    */   
/*    */   public GuardianSound(EntityGuardian guardian) {
/* 12 */     super(new ResourceLocation("minecraft:mob.guardian.attack"));
/* 13 */     this.guardian = guardian;
/* 14 */     this.attenuationType = ISound.AttenuationType.NONE;
/* 15 */     this.repeat = true;
/* 16 */     this.repeatDelay = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 21 */     if (!this.guardian.isDead && this.guardian.hasTargetedEntity()) {
/*    */       
/* 23 */       this.xPosF = (float)this.guardian.posX;
/* 24 */       this.yPosF = (float)this.guardian.posY;
/* 25 */       this.zPosF = (float)this.guardian.posZ;
/* 26 */       float f = this.guardian.func_175477_p(0.0F);
/* 27 */       this.volume = 0.0F + 1.0F * f * f;
/* 28 */       this.pitch = 0.7F + 0.5F * f;
/*    */     }
/*    */     else {
/*    */       
/* 32 */       this.donePlaying = true;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\audio\GuardianSound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */