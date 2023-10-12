/*    */ package net.optifine;
/*    */ 
/*    */ import java.util.BitSet;
/*    */ import net.minecraft.src.Config;
/*    */ import net.optifine.shaders.Shaders;
/*    */ 
/*    */ 
/*    */ public class SmartAnimations
/*    */ {
/*    */   private static boolean active;
/* 11 */   private static BitSet spritesRendered = new BitSet();
/* 12 */   private static BitSet texturesRendered = new BitSet();
/*    */ 
/*    */   
/*    */   public static boolean isActive() {
/* 16 */     return (active && !Shaders.isShadowPass);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void update() {
/* 21 */     active = (Config.getGameSettings()).ofSmartAnimations;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void spriteRendered(int animationIndex) {
/* 26 */     if (animationIndex >= 0)
/*    */     {
/* 28 */       spritesRendered.set(animationIndex);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static void spritesRendered(BitSet animationIndexes) {
/* 34 */     if (animationIndexes != null)
/*    */     {
/* 36 */       spritesRendered.or(animationIndexes);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isSpriteRendered(int animationIndex) {
/* 42 */     return (animationIndex < 0) ? false : spritesRendered.get(animationIndex);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void resetSpritesRendered() {
/* 47 */     spritesRendered.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public static void textureRendered(int textureId) {
/* 52 */     if (textureId >= 0)
/*    */     {
/* 54 */       texturesRendered.set(textureId);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isTextureRendered(int texId) {
/* 60 */     return (texId < 0) ? false : texturesRendered.get(texId);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void resetTexturesRendered() {
/* 65 */     texturesRendered.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\SmartAnimations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */