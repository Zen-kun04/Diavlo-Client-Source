/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.awt.image.ImageObserver;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.List;
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.client.resources.IResourceManager;
/*    */ import net.minecraft.item.EnumDyeColor;
/*    */ import net.minecraft.src.Config;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.optifine.shaders.ShadersTex;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class LayeredColorMaskTexture
/*    */   extends AbstractTexture {
/* 21 */   private static final Logger LOG = LogManager.getLogger();
/*    */   
/*    */   private final ResourceLocation textureLocation;
/*    */   private final List<String> field_174949_h;
/*    */   private final List<EnumDyeColor> field_174950_i;
/*    */   
/*    */   public LayeredColorMaskTexture(ResourceLocation textureLocationIn, List<String> p_i46101_2_, List<EnumDyeColor> p_i46101_3_) {
/* 28 */     this.textureLocation = textureLocationIn;
/* 29 */     this.field_174949_h = p_i46101_2_;
/* 30 */     this.field_174950_i = p_i46101_3_;
/*    */   }
/*    */   
/*    */   public void loadTexture(IResourceManager resourceManager) throws IOException {
/*    */     BufferedImage bufferedimage;
/* 35 */     deleteGlTexture();
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 40 */       BufferedImage bufferedimage1 = TextureUtil.readBufferedImage(resourceManager.getResource(this.textureLocation).getInputStream());
/* 41 */       int i = bufferedimage1.getType();
/*    */       
/* 43 */       if (i == 0)
/*    */       {
/* 45 */         i = 6;
/*    */       }
/*    */       
/* 48 */       bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), i);
/* 49 */       Graphics graphics = bufferedimage.getGraphics();
/* 50 */       graphics.drawImage(bufferedimage1, 0, 0, (ImageObserver)null);
/*    */       
/* 52 */       for (int j = 0; j < 17 && j < this.field_174949_h.size() && j < this.field_174950_i.size(); j++) {
/*    */         
/* 54 */         String s = this.field_174949_h.get(j);
/* 55 */         MapColor mapcolor = ((EnumDyeColor)this.field_174950_i.get(j)).getMapColor();
/*    */         
/* 57 */         if (s != null) {
/*    */           
/* 59 */           InputStream inputstream = resourceManager.getResource(new ResourceLocation(s)).getInputStream();
/* 60 */           BufferedImage bufferedimage2 = TextureUtil.readBufferedImage(inputstream);
/*    */           
/* 62 */           if (bufferedimage2.getWidth() == bufferedimage.getWidth() && bufferedimage2.getHeight() == bufferedimage.getHeight() && bufferedimage2.getType() == 6)
/*    */           {
/* 64 */             for (int k = 0; k < bufferedimage2.getHeight(); k++) {
/*    */               
/* 66 */               for (int l = 0; l < bufferedimage2.getWidth(); l++) {
/*    */                 
/* 68 */                 int i1 = bufferedimage2.getRGB(l, k);
/*    */                 
/* 70 */                 if ((i1 & 0xFF000000) != 0) {
/*    */                   
/* 72 */                   int j1 = (i1 & 0xFF0000) << 8 & 0xFF000000;
/* 73 */                   int k1 = bufferedimage1.getRGB(l, k);
/* 74 */                   int l1 = MathHelper.func_180188_d(k1, mapcolor.colorValue) & 0xFFFFFF;
/* 75 */                   bufferedimage2.setRGB(l, k, j1 | l1);
/*    */                 } 
/*    */               } 
/*    */             } 
/*    */             
/* 80 */             bufferedimage.getGraphics().drawImage(bufferedimage2, 0, 0, (ImageObserver)null);
/*    */           }
/*    */         
/*    */         } 
/*    */       } 
/* 85 */     } catch (IOException ioexception) {
/*    */       
/* 87 */       LOG.error("Couldn't load layered image", ioexception);
/*    */       
/*    */       return;
/*    */     } 
/* 91 */     if (Config.isShaders()) {
/*    */       
/* 93 */       ShadersTex.loadSimpleTexture(getGlTextureId(), bufferedimage, false, false, resourceManager, this.textureLocation, getMultiTexID());
/*    */     }
/*    */     else {
/*    */       
/* 97 */       TextureUtil.uploadTextureImage(getGlTextureId(), bufferedimage);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\texture\LayeredColorMaskTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */