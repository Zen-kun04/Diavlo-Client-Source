/*     */ package net.minecraft.client;
/*     */ 
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.shader.Framebuffer;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.util.MinecraftError;
/*     */ import net.optifine.CustomLoadingScreen;
/*     */ import net.optifine.CustomLoadingScreens;
/*     */ import net.optifine.reflect.Reflector;
/*     */ 
/*     */ public class LoadingScreenRenderer
/*     */   implements IProgressUpdate {
/*  19 */   private String message = "";
/*     */   private Minecraft mc;
/*  21 */   private String currentlyDisplayedText = "";
/*  22 */   private long systemTime = Minecraft.getSystemTime();
/*     */   
/*     */   private boolean loadingSuccess;
/*     */   private ScaledResolution scaledResolution;
/*     */   private Framebuffer framebuffer;
/*     */   
/*     */   public LoadingScreenRenderer(Minecraft mcIn) {
/*  29 */     this.mc = mcIn;
/*  30 */     this.scaledResolution = new ScaledResolution(mcIn);
/*  31 */     this.framebuffer = new Framebuffer(mcIn.displayWidth, mcIn.displayHeight, false);
/*  32 */     this.framebuffer.setFramebufferFilter(9728);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetProgressAndMessage(String message) {
/*  37 */     this.loadingSuccess = false;
/*  38 */     displayString(message);
/*     */   }
/*     */ 
/*     */   
/*     */   public void displaySavingString(String message) {
/*  43 */     this.loadingSuccess = true;
/*  44 */     displayString(message);
/*     */   }
/*     */ 
/*     */   
/*     */   private void displayString(String message) {
/*  49 */     this.currentlyDisplayedText = message;
/*     */     
/*  51 */     if (!this.mc.running) {
/*     */       
/*  53 */       if (!this.loadingSuccess)
/*     */       {
/*  55 */         throw new MinecraftError();
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/*  60 */       GlStateManager.clear(256);
/*  61 */       GlStateManager.matrixMode(5889);
/*  62 */       GlStateManager.loadIdentity();
/*     */       
/*  64 */       if (OpenGlHelper.isFramebufferEnabled()) {
/*     */         
/*  66 */         int i = this.scaledResolution.getScaleFactor();
/*  67 */         GlStateManager.ortho(0.0D, (this.scaledResolution.getScaledWidth() * i), (this.scaledResolution.getScaledHeight() * i), 0.0D, 100.0D, 300.0D);
/*     */       }
/*     */       else {
/*     */         
/*  71 */         ScaledResolution scaledresolution = new ScaledResolution(this.mc);
/*  72 */         GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
/*     */       } 
/*     */       
/*  75 */       GlStateManager.matrixMode(5888);
/*  76 */       GlStateManager.loadIdentity();
/*  77 */       GlStateManager.translate(0.0F, 0.0F, -200.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void displayLoadingString(String message) {
/*  83 */     if (!this.mc.running) {
/*     */       
/*  85 */       if (!this.loadingSuccess)
/*     */       {
/*  87 */         throw new MinecraftError();
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/*  92 */       this.systemTime = 0L;
/*  93 */       this.message = message;
/*  94 */       setLoadingProgress(-1);
/*  95 */       this.systemTime = 0L;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLoadingProgress(int progress) {
/* 101 */     if (!this.mc.running) {
/*     */       
/* 103 */       if (!this.loadingSuccess)
/*     */       {
/* 105 */         throw new MinecraftError();
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 110 */       long i = Minecraft.getSystemTime();
/*     */       
/* 112 */       if (i - this.systemTime >= 100L) {
/*     */         
/* 114 */         this.systemTime = i;
/* 115 */         ScaledResolution scaledresolution = new ScaledResolution(this.mc);
/* 116 */         int j = scaledresolution.getScaleFactor();
/* 117 */         int k = scaledresolution.getScaledWidth();
/* 118 */         int l = scaledresolution.getScaledHeight();
/*     */         
/* 120 */         if (OpenGlHelper.isFramebufferEnabled()) {
/*     */           
/* 122 */           this.framebuffer.framebufferClear();
/*     */         }
/*     */         else {
/*     */           
/* 126 */           GlStateManager.clear(256);
/*     */         } 
/*     */         
/* 129 */         this.framebuffer.bindFramebuffer(false);
/* 130 */         GlStateManager.matrixMode(5889);
/* 131 */         GlStateManager.loadIdentity();
/* 132 */         GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
/* 133 */         GlStateManager.matrixMode(5888);
/* 134 */         GlStateManager.loadIdentity();
/* 135 */         GlStateManager.translate(0.0F, 0.0F, -200.0F);
/*     */         
/* 137 */         if (!OpenGlHelper.isFramebufferEnabled())
/*     */         {
/* 139 */           GlStateManager.clear(16640);
/*     */         }
/*     */         
/* 142 */         boolean flag = true;
/*     */         
/* 144 */         if (Reflector.FMLClientHandler_handleLoadingScreen.exists()) {
/*     */           
/* 146 */           Object object = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
/*     */           
/* 148 */           if (object != null)
/*     */           {
/* 150 */             flag = !Reflector.callBoolean(object, Reflector.FMLClientHandler_handleLoadingScreen, new Object[] { scaledresolution });
/*     */           }
/*     */         } 
/*     */         
/* 154 */         if (flag) {
/*     */           
/* 156 */           Tessellator tessellator = Tessellator.getInstance();
/* 157 */           WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 158 */           CustomLoadingScreen customloadingscreen = CustomLoadingScreens.getCustomLoadingScreen();
/*     */           
/* 160 */           if (customloadingscreen != null) {
/*     */             
/* 162 */             customloadingscreen.drawBackground(scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
/*     */           }
/*     */           else {
/*     */             
/* 166 */             this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
/* 167 */             float f = 32.0F;
/* 168 */             worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 169 */             worldrenderer.pos(0.0D, l, 0.0D).tex(0.0D, (l / f)).color(64, 64, 64, 255).endVertex();
/* 170 */             worldrenderer.pos(k, l, 0.0D).tex((k / f), (l / f)).color(64, 64, 64, 255).endVertex();
/* 171 */             worldrenderer.pos(k, 0.0D, 0.0D).tex((k / f), 0.0D).color(64, 64, 64, 255).endVertex();
/* 172 */             worldrenderer.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).color(64, 64, 64, 255).endVertex();
/* 173 */             tessellator.draw();
/*     */           } 
/*     */           
/* 176 */           if (progress >= 0) {
/*     */             
/* 178 */             int l1 = 100;
/* 179 */             int i1 = 2;
/* 180 */             int j1 = k / 2 - l1 / 2;
/* 181 */             int k1 = l / 2 + 16;
/* 182 */             GlStateManager.disableTexture2D();
/* 183 */             worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 184 */             worldrenderer.pos(j1, k1, 0.0D).color(128, 128, 128, 255).endVertex();
/* 185 */             worldrenderer.pos(j1, (k1 + i1), 0.0D).color(128, 128, 128, 255).endVertex();
/* 186 */             worldrenderer.pos((j1 + l1), (k1 + i1), 0.0D).color(128, 128, 128, 255).endVertex();
/* 187 */             worldrenderer.pos((j1 + l1), k1, 0.0D).color(128, 128, 128, 255).endVertex();
/* 188 */             worldrenderer.pos(j1, k1, 0.0D).color(128, 255, 128, 255).endVertex();
/* 189 */             worldrenderer.pos(j1, (k1 + i1), 0.0D).color(128, 255, 128, 255).endVertex();
/* 190 */             worldrenderer.pos((j1 + progress), (k1 + i1), 0.0D).color(128, 255, 128, 255).endVertex();
/* 191 */             worldrenderer.pos((j1 + progress), k1, 0.0D).color(128, 255, 128, 255).endVertex();
/* 192 */             tessellator.draw();
/* 193 */             GlStateManager.enableTexture2D();
/*     */           } 
/*     */           
/* 196 */           GlStateManager.enableBlend();
/* 197 */           GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 198 */           this.mc.fontRendererObj.drawStringWithShadow(this.currentlyDisplayedText, ((k - this.mc.fontRendererObj.getStringWidth(this.currentlyDisplayedText)) / 2), (l / 2 - 4 - 16), 16777215);
/* 199 */           this.mc.fontRendererObj.drawStringWithShadow(this.message, ((k - this.mc.fontRendererObj.getStringWidth(this.message)) / 2), (l / 2 - 4 + 8), 16777215);
/*     */         } 
/*     */         
/* 202 */         this.framebuffer.unbindFramebuffer();
/*     */         
/* 204 */         if (OpenGlHelper.isFramebufferEnabled())
/*     */         {
/* 206 */           this.framebuffer.framebufferRender(k * j, l * j);
/*     */         }
/*     */         
/* 209 */         this.mc.updateDisplay();
/*     */ 
/*     */         
/*     */         try {
/* 213 */           Thread.yield();
/*     */         }
/* 215 */         catch (Exception exception) {}
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDoneWorking() {}
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\LoadingScreenRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */