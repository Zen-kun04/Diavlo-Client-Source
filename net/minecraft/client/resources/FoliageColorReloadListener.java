/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.renderer.texture.TextureUtil;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.world.ColorizerFoliage;
/*    */ 
/*    */ public class FoliageColorReloadListener
/*    */   implements IResourceManagerReloadListener {
/* 10 */   private static final ResourceLocation LOC_FOLIAGE_PNG = new ResourceLocation("textures/colormap/foliage.png");
/*    */ 
/*    */ 
/*    */   
/*    */   public void onResourceManagerReload(IResourceManager resourceManager) {
/*    */     try {
/* 16 */       ColorizerFoliage.setFoliageBiomeColorizer(TextureUtil.readImageData(resourceManager, LOC_FOLIAGE_PNG));
/*    */     }
/* 18 */     catch (IOException iOException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\resources\FoliageColorReloadListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */