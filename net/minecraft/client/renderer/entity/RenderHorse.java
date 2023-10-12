/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.google.common.collect.Maps;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.model.ModelHorse;
/*    */ import net.minecraft.client.renderer.texture.ITextureObject;
/*    */ import net.minecraft.client.renderer.texture.LayeredTexture;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.passive.EntityHorse;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class RenderHorse extends RenderLiving<EntityHorse> {
/* 14 */   private static final Map<String, ResourceLocation> field_110852_a = Maps.newHashMap();
/* 15 */   private static final ResourceLocation whiteHorseTextures = new ResourceLocation("textures/entity/horse/horse_white.png");
/* 16 */   private static final ResourceLocation muleTextures = new ResourceLocation("textures/entity/horse/mule.png");
/* 17 */   private static final ResourceLocation donkeyTextures = new ResourceLocation("textures/entity/horse/donkey.png");
/* 18 */   private static final ResourceLocation zombieHorseTextures = new ResourceLocation("textures/entity/horse/horse_zombie.png");
/* 19 */   private static final ResourceLocation skeletonHorseTextures = new ResourceLocation("textures/entity/horse/horse_skeleton.png");
/*    */ 
/*    */   
/*    */   public RenderHorse(RenderManager rendermanagerIn, ModelHorse model, float shadowSizeIn) {
/* 23 */     super(rendermanagerIn, (ModelBase)model, shadowSizeIn);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void preRenderCallback(EntityHorse entitylivingbaseIn, float partialTickTime) {
/* 28 */     float f = 1.0F;
/* 29 */     int i = entitylivingbaseIn.getHorseType();
/*    */     
/* 31 */     if (i == 1) {
/*    */       
/* 33 */       f *= 0.87F;
/*    */     }
/* 35 */     else if (i == 2) {
/*    */       
/* 37 */       f *= 0.92F;
/*    */     } 
/*    */     
/* 40 */     GlStateManager.scale(f, f, f);
/* 41 */     super.preRenderCallback(entitylivingbaseIn, partialTickTime);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(EntityHorse entity) {
/* 46 */     if (!entity.func_110239_cn()) {
/*    */       
/* 48 */       switch (entity.getHorseType()) {
/*    */ 
/*    */         
/*    */         default:
/* 52 */           return whiteHorseTextures;
/*    */         
/*    */         case 1:
/* 55 */           return donkeyTextures;
/*    */         
/*    */         case 2:
/* 58 */           return muleTextures;
/*    */         
/*    */         case 3:
/* 61 */           return zombieHorseTextures;
/*    */         case 4:
/*    */           break;
/* 64 */       }  return skeletonHorseTextures;
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 69 */     return func_110848_b(entity);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private ResourceLocation func_110848_b(EntityHorse horse) {
/* 75 */     String s = horse.getHorseTexture();
/*    */     
/* 77 */     if (!horse.func_175507_cI())
/*    */     {
/* 79 */       return null;
/*    */     }
/*    */ 
/*    */     
/* 83 */     ResourceLocation resourcelocation = field_110852_a.get(s);
/*    */     
/* 85 */     if (resourcelocation == null) {
/*    */       
/* 87 */       resourcelocation = new ResourceLocation(s);
/* 88 */       Minecraft.getMinecraft().getTextureManager().loadTexture(resourcelocation, (ITextureObject)new LayeredTexture(horse.getVariantTexturePaths()));
/* 89 */       field_110852_a.put(s, resourcelocation);
/*    */     } 
/*    */     
/* 92 */     return resourcelocation;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\entity\RenderHorse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */