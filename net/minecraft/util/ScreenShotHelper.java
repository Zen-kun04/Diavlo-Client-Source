/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.nio.IntBuffer;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import javax.imageio.ImageIO;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.texture.TextureUtil;
/*     */ import net.minecraft.client.shader.Framebuffer;
/*     */ import net.minecraft.event.ClickEvent;
/*     */ import net.minecraft.src.Config;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScreenShotHelper
/*     */ {
/*  27 */   private static final Logger logger = LogManager.getLogger();
/*  28 */   private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
/*     */   
/*     */   private static IntBuffer pixelBuffer;
/*     */   private static int[] pixelValues;
/*     */   
/*     */   public static IChatComponent saveScreenshot(File gameDirectory, int width, int height, Framebuffer buffer) {
/*  34 */     return saveScreenshot(gameDirectory, (String)null, width, height, buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static IChatComponent saveScreenshot(File gameDirectory, String screenshotName, int width, int height, Framebuffer buffer) {
/*     */     try {
/*  41 */       File file1 = new File(gameDirectory, "screenshots");
/*  42 */       file1.mkdir();
/*  43 */       Minecraft minecraft = Minecraft.getMinecraft();
/*  44 */       int i = (Config.getGameSettings()).guiScale;
/*  45 */       ScaledResolution scaledresolution = new ScaledResolution(minecraft);
/*  46 */       int j = scaledresolution.getScaleFactor();
/*  47 */       int k = Config.getScreenshotSize();
/*  48 */       boolean flag = (OpenGlHelper.isFramebufferEnabled() && k > 1);
/*     */       
/*  50 */       if (flag) {
/*     */         
/*  52 */         (Config.getGameSettings()).guiScale = j * k;
/*  53 */         resize(width * k, height * k);
/*  54 */         GlStateManager.pushMatrix();
/*  55 */         GlStateManager.clear(16640);
/*  56 */         minecraft.getFramebuffer().bindFramebuffer(true);
/*  57 */         minecraft.entityRenderer.updateCameraAndRender(Config.renderPartialTicks, System.nanoTime());
/*     */       } 
/*     */       
/*  60 */       if (OpenGlHelper.isFramebufferEnabled()) {
/*     */         
/*  62 */         width = buffer.framebufferTextureWidth;
/*  63 */         height = buffer.framebufferTextureHeight;
/*     */       } 
/*     */       
/*  66 */       int l = width * height;
/*     */       
/*  68 */       if (pixelBuffer == null || pixelBuffer.capacity() < l) {
/*     */         
/*  70 */         pixelBuffer = BufferUtils.createIntBuffer(l);
/*  71 */         pixelValues = new int[l];
/*     */       } 
/*     */       
/*  74 */       GL11.glPixelStorei(3333, 1);
/*  75 */       GL11.glPixelStorei(3317, 1);
/*  76 */       pixelBuffer.clear();
/*     */       
/*  78 */       if (OpenGlHelper.isFramebufferEnabled()) {
/*     */         
/*  80 */         GlStateManager.bindTexture(buffer.framebufferTexture);
/*  81 */         GL11.glGetTexImage(3553, 0, 32993, 33639, pixelBuffer);
/*     */       }
/*     */       else {
/*     */         
/*  85 */         GL11.glReadPixels(0, 0, width, height, 32993, 33639, pixelBuffer);
/*     */       } 
/*     */       
/*  88 */       pixelBuffer.get(pixelValues);
/*  89 */       TextureUtil.processPixelValues(pixelValues, width, height);
/*  90 */       BufferedImage bufferedimage = null;
/*     */       
/*  92 */       if (OpenGlHelper.isFramebufferEnabled()) {
/*     */         
/*  94 */         bufferedimage = new BufferedImage(buffer.framebufferWidth, buffer.framebufferHeight, 1);
/*  95 */         int i1 = buffer.framebufferTextureHeight - buffer.framebufferHeight;
/*     */         
/*  97 */         for (int j1 = i1; j1 < buffer.framebufferTextureHeight; j1++)
/*     */         {
/*  99 */           for (int k1 = 0; k1 < buffer.framebufferWidth; k1++)
/*     */           {
/* 101 */             bufferedimage.setRGB(k1, j1 - i1, pixelValues[j1 * buffer.framebufferTextureWidth + k1]);
/*     */           }
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 107 */         bufferedimage = new BufferedImage(width, height, 1);
/* 108 */         bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);
/*     */       } 
/*     */       
/* 111 */       if (flag) {
/*     */         
/* 113 */         minecraft.getFramebuffer().unbindFramebuffer();
/* 114 */         GlStateManager.popMatrix();
/* 115 */         (Config.getGameSettings()).guiScale = i;
/* 116 */         resize(width, height);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 121 */       if (screenshotName == null) {
/*     */         
/* 123 */         file2 = getTimestampedPNGFileForDirectory(file1);
/*     */       }
/*     */       else {
/*     */         
/* 127 */         file2 = new File(file1, screenshotName);
/*     */       } 
/*     */       
/* 130 */       File file2 = file2.getCanonicalFile();
/* 131 */       ImageIO.write(bufferedimage, "png", file2);
/* 132 */       IChatComponent ichatcomponent = new ChatComponentText(file2.getName());
/* 133 */       ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath()));
/* 134 */       ichatcomponent.getChatStyle().setUnderlined(Boolean.valueOf(true));
/* 135 */       return new ChatComponentTranslation("screenshot.success", new Object[] { ichatcomponent });
/*     */     }
/* 137 */     catch (Exception exception) {
/*     */       
/* 139 */       logger.warn("Couldn't save screenshot", exception);
/* 140 */       return new ChatComponentTranslation("screenshot.failure", new Object[] { exception.getMessage() });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static File getTimestampedPNGFileForDirectory(File gameDirectory) {
/* 146 */     String s = dateFormat.format(new Date()).toString();
/* 147 */     int i = 1;
/*     */ 
/*     */     
/*     */     while (true) {
/* 151 */       File file1 = new File(gameDirectory, s + ((i == 1) ? "" : ("_" + i)) + ".png");
/*     */       
/* 153 */       if (!file1.exists())
/*     */       {
/* 155 */         return file1;
/*     */       }
/*     */       
/* 158 */       i++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void resize(int p_resize_0_, int p_resize_1_) {
/* 164 */     Minecraft minecraft = Minecraft.getMinecraft();
/* 165 */     minecraft.displayWidth = Math.max(1, p_resize_0_);
/* 166 */     minecraft.displayHeight = Math.max(1, p_resize_1_);
/*     */     
/* 168 */     if (minecraft.currentScreen != null) {
/*     */       
/* 170 */       ScaledResolution scaledresolution = new ScaledResolution(minecraft);
/* 171 */       minecraft.currentScreen.onResize(minecraft, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
/*     */     } 
/*     */     
/* 174 */     updateFramebufferSize();
/*     */   }
/*     */ 
/*     */   
/*     */   private static void updateFramebufferSize() {
/* 179 */     Minecraft minecraft = Minecraft.getMinecraft();
/* 180 */     minecraft.getFramebuffer().createBindFramebuffer(minecraft.displayWidth, minecraft.displayHeight);
/*     */     
/* 182 */     if (minecraft.entityRenderer != null)
/*     */     {
/* 184 */       minecraft.entityRenderer.updateShaderGroupSize(minecraft.displayWidth, minecraft.displayHeight);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\ScreenShotHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */