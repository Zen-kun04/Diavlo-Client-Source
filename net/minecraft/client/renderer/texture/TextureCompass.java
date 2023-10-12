/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class TextureCompass
/*    */   extends TextureAtlasSprite
/*    */ {
/*    */   public double currentAngle;
/*    */   public double angleDelta;
/*    */   public static String locationSprite;
/*    */   
/*    */   public TextureCompass(String iconName) {
/* 16 */     super(iconName);
/* 17 */     locationSprite = iconName;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateAnimation() {
/* 22 */     Minecraft minecraft = Minecraft.getMinecraft();
/*    */     
/* 24 */     if (minecraft.theWorld != null && minecraft.thePlayer != null) {
/*    */       
/* 26 */       updateCompass((World)minecraft.theWorld, minecraft.thePlayer.posX, minecraft.thePlayer.posZ, minecraft.thePlayer.rotationYaw, false, false);
/*    */     }
/*    */     else {
/*    */       
/* 30 */       updateCompass((World)null, 0.0D, 0.0D, 0.0D, true, false);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateCompass(World worldIn, double p_94241_2_, double p_94241_4_, double p_94241_6_, boolean p_94241_8_, boolean p_94241_9_) {
/* 36 */     if (!this.framesTextureData.isEmpty()) {
/*    */       
/* 38 */       double d0 = 0.0D;
/*    */       
/* 40 */       if (worldIn != null && !p_94241_8_) {
/*    */         
/* 42 */         BlockPos blockpos = worldIn.getSpawnPoint();
/* 43 */         double d1 = blockpos.getX() - p_94241_2_;
/* 44 */         double d2 = blockpos.getZ() - p_94241_4_;
/* 45 */         p_94241_6_ %= 360.0D;
/* 46 */         d0 = -((p_94241_6_ - 90.0D) * Math.PI / 180.0D - Math.atan2(d2, d1));
/*    */         
/* 48 */         if (!worldIn.provider.isSurfaceWorld())
/*    */         {
/* 50 */           d0 = Math.random() * Math.PI * 2.0D;
/*    */         }
/*    */       } 
/*    */       
/* 54 */       if (p_94241_9_) {
/*    */         
/* 56 */         this.currentAngle = d0;
/*    */       } else {
/*    */         double d3;
/*    */ 
/*    */ 
/*    */         
/* 62 */         for (d3 = d0 - this.currentAngle; d3 < -3.141592653589793D; d3 += 6.283185307179586D);
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 67 */         while (d3 >= Math.PI)
/*    */         {
/* 69 */           d3 -= 6.283185307179586D;
/*    */         }
/*    */         
/* 72 */         d3 = MathHelper.clamp_double(d3, -1.0D, 1.0D);
/* 73 */         this.angleDelta += d3 * 0.1D;
/* 74 */         this.angleDelta *= 0.8D;
/* 75 */         this.currentAngle += this.angleDelta;
/*    */       } 
/*    */       
/*    */       int i;
/*    */       
/* 80 */       for (i = (int)((this.currentAngle / 6.283185307179586D + 1.0D) * this.framesTextureData.size()) % this.framesTextureData.size(); i < 0; i = (i + this.framesTextureData.size()) % this.framesTextureData.size());
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 85 */       if (i != this.frameCounter) {
/*    */         
/* 87 */         this.frameCounter = i;
/* 88 */         TextureUtil.uploadTextureMipmap(this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\texture\TextureCompass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */