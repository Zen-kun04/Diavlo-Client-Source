/*     */ package net.minecraft.client.renderer.tileentity;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiUtilRenderComponents;
/*     */ import net.minecraft.client.model.ModelSign;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntitySign;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.optifine.CustomColors;
/*     */ import net.optifine.shaders.Shaders;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class TileEntitySignRenderer
/*     */   extends TileEntitySpecialRenderer<TileEntitySign> {
/*  23 */   private static final ResourceLocation SIGN_TEXTURE = new ResourceLocation("textures/entity/sign.png");
/*  24 */   private final ModelSign model = new ModelSign();
/*  25 */   private static double textRenderDistanceSq = 4096.0D;
/*     */ 
/*     */   
/*     */   public void renderTileEntityAt(TileEntitySign te, double x, double y, double z, float partialTicks, int destroyStage) {
/*  29 */     Block block = te.getBlockType();
/*  30 */     GlStateManager.pushMatrix();
/*  31 */     float f = 0.6666667F;
/*     */     
/*  33 */     if (block == Blocks.standing_sign) {
/*     */       
/*  35 */       GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F * f, (float)z + 0.5F);
/*  36 */       float f1 = (te.getBlockMetadata() * 360) / 16.0F;
/*  37 */       GlStateManager.rotate(-f1, 0.0F, 1.0F, 0.0F);
/*  38 */       this.model.signStick.showModel = true;
/*     */     }
/*     */     else {
/*     */       
/*  42 */       int k = te.getBlockMetadata();
/*  43 */       float f2 = 0.0F;
/*     */       
/*  45 */       if (k == 2)
/*     */       {
/*  47 */         f2 = 180.0F;
/*     */       }
/*     */       
/*  50 */       if (k == 4)
/*     */       {
/*  52 */         f2 = 90.0F;
/*     */       }
/*     */       
/*  55 */       if (k == 5)
/*     */       {
/*  57 */         f2 = -90.0F;
/*     */       }
/*     */       
/*  60 */       GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F * f, (float)z + 0.5F);
/*  61 */       GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
/*  62 */       GlStateManager.translate(0.0F, -0.3125F, -0.4375F);
/*  63 */       this.model.signStick.showModel = false;
/*     */     } 
/*     */     
/*  66 */     if (destroyStage >= 0) {
/*     */       
/*  68 */       bindTexture(DESTROY_STAGES[destroyStage]);
/*  69 */       GlStateManager.matrixMode(5890);
/*  70 */       GlStateManager.pushMatrix();
/*  71 */       GlStateManager.scale(4.0F, 2.0F, 1.0F);
/*  72 */       GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
/*  73 */       GlStateManager.matrixMode(5888);
/*     */     }
/*     */     else {
/*     */       
/*  77 */       bindTexture(SIGN_TEXTURE);
/*     */     } 
/*     */     
/*  80 */     GlStateManager.enableRescaleNormal();
/*  81 */     GlStateManager.pushMatrix();
/*  82 */     GlStateManager.scale(f, -f, -f);
/*  83 */     this.model.renderSign();
/*  84 */     GlStateManager.popMatrix();
/*     */     
/*  86 */     if (isRenderText(te)) {
/*     */       
/*  88 */       FontRenderer fontrenderer = getFontRenderer();
/*  89 */       float f3 = 0.015625F * f;
/*  90 */       GlStateManager.translate(0.0F, 0.5F * f, 0.07F * f);
/*  91 */       GlStateManager.scale(f3, -f3, f3);
/*  92 */       GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
/*  93 */       GlStateManager.depthMask(false);
/*  94 */       int i = 0;
/*     */       
/*  96 */       if (Config.isCustomColors())
/*     */       {
/*  98 */         i = CustomColors.getSignTextColor(i);
/*     */       }
/*     */       
/* 101 */       if (destroyStage < 0)
/*     */       {
/* 103 */         for (int j = 0; j < te.signText.length; j++) {
/*     */           
/* 105 */           if (te.signText[j] != null) {
/*     */             
/* 107 */             IChatComponent ichatcomponent = te.signText[j];
/* 108 */             List<IChatComponent> list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false, true);
/* 109 */             String s = (list != null && list.size() > 0) ? ((IChatComponent)list.get(0)).getFormattedText() : "";
/*     */             
/* 111 */             if (j == te.lineBeingEdited) {
/*     */               
/* 113 */               s = "> " + s + " <";
/* 114 */               fontrenderer.drawString(s, (-fontrenderer.getStringWidth(s) / 2), (j * 10 - te.signText.length * 5), i);
/*     */             }
/*     */             else {
/*     */               
/* 118 */               fontrenderer.drawString(s, (-fontrenderer.getStringWidth(s) / 2), (j * 10 - te.signText.length * 5), i);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 125 */     GlStateManager.depthMask(true);
/* 126 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 127 */     GlStateManager.popMatrix();
/*     */     
/* 129 */     if (destroyStage >= 0) {
/*     */       
/* 131 */       GlStateManager.matrixMode(5890);
/* 132 */       GlStateManager.popMatrix();
/* 133 */       GlStateManager.matrixMode(5888);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isRenderText(TileEntitySign p_isRenderText_0_) {
/* 139 */     if (Shaders.isShadowPass)
/*     */     {
/* 141 */       return false;
/*     */     }
/* 143 */     if ((Config.getMinecraft()).currentScreen instanceof net.minecraft.client.gui.inventory.GuiEditSign)
/*     */     {
/* 145 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 149 */     if (!Config.zoomMode && p_isRenderText_0_.lineBeingEdited < 0) {
/*     */       
/* 151 */       Entity entity = Config.getMinecraft().getRenderViewEntity();
/* 152 */       double d0 = p_isRenderText_0_.getDistanceSq(entity.posX, entity.posY, entity.posZ);
/*     */       
/* 154 */       if (d0 > textRenderDistanceSq)
/*     */       {
/* 156 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 160 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void updateTextRenderDistance() {
/* 166 */     Minecraft minecraft = Config.getMinecraft();
/* 167 */     double d0 = Config.limit(minecraft.gameSettings.fovSetting, 1.0F, 120.0F);
/* 168 */     double d1 = Math.max(1.5D * minecraft.displayHeight / d0, 16.0D);
/* 169 */     textRenderDistanceSq = d1 * d1;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\tileentity\TileEntitySignRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */