/*    */ package net.minecraft.client.audio;
/*    */ 
/*    */ import net.minecraft.entity.item.EntityMinecart;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class MovingSoundMinecartRiding
/*    */   extends MovingSound
/*    */ {
/*    */   private final EntityPlayer player;
/*    */   private final EntityMinecart minecart;
/*    */   
/*    */   public MovingSoundMinecartRiding(EntityPlayer playerRiding, EntityMinecart minecart) {
/* 15 */     super(new ResourceLocation("minecraft:minecart.inside"));
/* 16 */     this.player = playerRiding;
/* 17 */     this.minecart = minecart;
/* 18 */     this.attenuationType = ISound.AttenuationType.NONE;
/* 19 */     this.repeat = true;
/* 20 */     this.repeatDelay = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 25 */     if (!this.minecart.isDead && this.player.isRiding() && this.player.ridingEntity == this.minecart) {
/*    */       
/* 27 */       float f = MathHelper.sqrt_double(this.minecart.motionX * this.minecart.motionX + this.minecart.motionZ * this.minecart.motionZ);
/*    */       
/* 29 */       if (f >= 0.01D)
/*    */       {
/* 31 */         this.volume = 0.0F + MathHelper.clamp_float(f, 0.0F, 1.0F) * 0.75F;
/*    */       }
/*    */       else
/*    */       {
/* 35 */         this.volume = 0.0F;
/*    */       }
/*    */     
/*    */     } else {
/*    */       
/* 40 */       this.donePlaying = true;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\audio\MovingSoundMinecartRiding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */