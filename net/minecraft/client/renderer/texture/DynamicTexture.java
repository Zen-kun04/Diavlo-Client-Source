/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.resources.IResourceManager;
/*    */ 
/*    */ public class DynamicTexture
/*    */   extends AbstractTexture
/*    */ {
/*    */   private final int[] dynamicTextureData;
/*    */   private final int width;
/*    */   private final int height;
/*    */   
/*    */   public DynamicTexture(BufferedImage bufferedImage) {
/* 15 */     this(bufferedImage.getWidth(), bufferedImage.getHeight());
/* 16 */     bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), this.dynamicTextureData, 0, bufferedImage.getWidth());
/* 17 */     updateDynamicTexture();
/*    */   }
/*    */ 
/*    */   
/*    */   public DynamicTexture(int textureWidth, int textureHeight) {
/* 22 */     this.width = textureWidth;
/* 23 */     this.height = textureHeight;
/* 24 */     this.dynamicTextureData = new int[textureWidth * textureHeight];
/* 25 */     TextureUtil.allocateTexture(getGlTextureId(), textureWidth, textureHeight);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void loadTexture(IResourceManager resourceManager) throws IOException {}
/*    */ 
/*    */   
/*    */   public void updateDynamicTexture() {
/* 34 */     TextureUtil.uploadTexture(getGlTextureId(), this.dynamicTextureData, this.width, this.height);
/*    */   }
/*    */ 
/*    */   
/*    */   public int[] getTextureData() {
/* 39 */     return this.dynamicTextureData;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\texture\DynamicTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */