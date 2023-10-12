/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class DefaultPlayerSkin
/*    */ {
/*  8 */   private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
/*  9 */   private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
/*    */ 
/*    */   
/*    */   public static ResourceLocation getDefaultSkinLegacy() {
/* 13 */     return TEXTURE_STEVE;
/*    */   }
/*    */ 
/*    */   
/*    */   public static ResourceLocation getDefaultSkin(UUID playerUUID) {
/* 18 */     return isSlimSkin(playerUUID) ? TEXTURE_ALEX : TEXTURE_STEVE;
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getSkinType(UUID playerUUID) {
/* 23 */     return isSlimSkin(playerUUID) ? "slim" : "default";
/*    */   }
/*    */ 
/*    */   
/*    */   private static boolean isSlimSkin(UUID playerUUID) {
/* 28 */     return ((playerUUID.hashCode() & 0x1) == 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\resources\DefaultPlayerSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */