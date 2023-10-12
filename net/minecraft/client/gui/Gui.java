/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class Gui
/*     */ {
/*  12 */   public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
/*  13 */   public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
/*  14 */   public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
/*     */   
/*     */   protected float zLevel;
/*     */   
/*     */   protected static void drawHorizontalLine(int startX, int endX, int y, int color) {
/*  19 */     if (endX < startX) {
/*     */       
/*  21 */       int i = startX;
/*  22 */       startX = endX;
/*  23 */       endX = i;
/*     */     } 
/*     */     
/*  26 */     drawRect(startX, y, (endX + 1), (y + 1), color);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void drawVerticalLine(int x, int startY, int endY, int color) {
/*  31 */     if (endY < startY) {
/*     */       
/*  33 */       int i = startY;
/*  34 */       startY = endY;
/*  35 */       endY = i;
/*     */     } 
/*     */     
/*  38 */     drawRect(x, (startY + 1), (x + 1), endY, color);
/*     */   }
/*     */   
/*     */   public static void drawRoundedRect(double left, double top, double right, double bottom, double radius, int color) {
/*  42 */     if (left < right) {
/*  43 */       double i = left;
/*  44 */       left = right;
/*  45 */       right = i;
/*     */     } 
/*     */     
/*  48 */     if (top < bottom) {
/*  49 */       double j = top;
/*  50 */       top = bottom;
/*  51 */       bottom = j;
/*     */     } 
/*     */     
/*  54 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
/*  55 */     float red = (color >> 16 & 0xFF) / 255.0F;
/*  56 */     float green = (color >> 8 & 0xFF) / 255.0F;
/*  57 */     float blue = (color & 0xFF) / 255.0F;
/*     */     
/*  59 */     Tessellator tessellator = Tessellator.getInstance();
/*  60 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*     */     
/*  62 */     GlStateManager.enableBlend();
/*  63 */     GlStateManager.disableTexture2D();
/*  64 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  65 */     GlStateManager.color(red, green, blue, alpha);
/*     */ 
/*     */     
/*  68 */     drawCircle(worldrenderer, left + radius, top - radius, radius, 0, 90);
/*  69 */     drawCircle(worldrenderer, right - radius, top - radius, radius, 90, 180);
/*  70 */     drawCircle(worldrenderer, right - radius, bottom + radius, radius, 180, 270);
/*  71 */     drawCircle(worldrenderer, left + radius, bottom + radius, radius, 270, 360);
/*     */ 
/*     */     
/*  74 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION);
/*  75 */     worldrenderer.pos(left + radius, top, 0.0D).endVertex();
/*  76 */     worldrenderer.pos(right - radius, top, 0.0D).endVertex();
/*  77 */     worldrenderer.pos(right, top - radius, 0.0D).endVertex();
/*  78 */     worldrenderer.pos(right, bottom + radius, 0.0D).endVertex();
/*  79 */     worldrenderer.pos(right - radius, bottom, 0.0D).endVertex();
/*  80 */     worldrenderer.pos(left + radius, bottom, 0.0D).endVertex();
/*  81 */     worldrenderer.pos(left, bottom + radius, 0.0D).endVertex();
/*  82 */     worldrenderer.pos(left, top - radius, 0.0D).endVertex();
/*  83 */     tessellator.draw();
/*     */     
/*  85 */     GlStateManager.enableTexture2D();
/*  86 */     GlStateManager.disableBlend();
/*     */   }
/*     */   
/*     */   private static void drawCircle(WorldRenderer worldrenderer, double centerX, double centerY, double radius, int startAngle, int endAngle) {
/*  90 */     int segments = 360;
/*  91 */     double angleIncrement = (endAngle - startAngle) / segments;
/*  92 */     Tessellator tessellator = Tessellator.getInstance();
/*     */     
/*  94 */     worldrenderer.begin(6, DefaultVertexFormats.POSITION);
/*     */     
/*  96 */     for (int i = startAngle; i < endAngle; i++) {
/*  97 */       double xPos = centerX + Math.cos(Math.toRadians(i)) * radius;
/*  98 */       double yPos = centerY + Math.sin(Math.toRadians(i)) * radius;
/*  99 */       worldrenderer.pos(xPos, yPos, 0.0D).endVertex();
/*     */     } 
/*     */     
/* 102 */     tessellator.draw();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void drawRect(double left, double top, double right, double bottom, int color) {
/* 108 */     if (left < right) {
/*     */       
/* 110 */       double i = left;
/* 111 */       left = right;
/* 112 */       right = i;
/*     */     } 
/*     */     
/* 115 */     if (top < bottom) {
/*     */       
/* 117 */       double j = top;
/* 118 */       top = bottom;
/* 119 */       bottom = j;
/*     */     } 
/*     */     
/* 122 */     float f3 = (color >> 24 & 0xFF) / 255.0F;
/* 123 */     float f = (color >> 16 & 0xFF) / 255.0F;
/* 124 */     float f1 = (color >> 8 & 0xFF) / 255.0F;
/* 125 */     float f2 = (color & 0xFF) / 255.0F;
/* 126 */     Tessellator tessellator = Tessellator.getInstance();
/* 127 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 128 */     GlStateManager.enableBlend();
/* 129 */     GlStateManager.disableTexture2D();
/* 130 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 131 */     GlStateManager.color(f, f1, f2, f3);
/* 132 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION);
/* 133 */     worldrenderer.pos(left, bottom, 0.0D).endVertex();
/* 134 */     worldrenderer.pos(right, bottom, 0.0D).endVertex();
/* 135 */     worldrenderer.pos(right, top, 0.0D).endVertex();
/* 136 */     worldrenderer.pos(left, top, 0.0D).endVertex();
/* 137 */     tessellator.draw();
/* 138 */     GlStateManager.enableTexture2D();
/* 139 */     GlStateManager.disableBlend();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
/* 144 */     float f = (startColor >> 24 & 0xFF) / 255.0F;
/* 145 */     float f1 = (startColor >> 16 & 0xFF) / 255.0F;
/* 146 */     float f2 = (startColor >> 8 & 0xFF) / 255.0F;
/* 147 */     float f3 = (startColor & 0xFF) / 255.0F;
/* 148 */     float f4 = (endColor >> 24 & 0xFF) / 255.0F;
/* 149 */     float f5 = (endColor >> 16 & 0xFF) / 255.0F;
/* 150 */     float f6 = (endColor >> 8 & 0xFF) / 255.0F;
/* 151 */     float f7 = (endColor & 0xFF) / 255.0F;
/* 152 */     GlStateManager.disableTexture2D();
/* 153 */     GlStateManager.enableBlend();
/* 154 */     GlStateManager.disableAlpha();
/* 155 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 156 */     GlStateManager.shadeModel(7425);
/* 157 */     Tessellator tessellator = Tessellator.getInstance();
/* 158 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 159 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 160 */     worldrenderer.pos(right, top, this.zLevel).color(f1, f2, f3, f).endVertex();
/* 161 */     worldrenderer.pos(left, top, this.zLevel).color(f1, f2, f3, f).endVertex();
/* 162 */     worldrenderer.pos(left, bottom, this.zLevel).color(f5, f6, f7, f4).endVertex();
/* 163 */     worldrenderer.pos(right, bottom, this.zLevel).color(f5, f6, f7, f4).endVertex();
/* 164 */     tessellator.draw();
/* 165 */     GlStateManager.shadeModel(7424);
/* 166 */     GlStateManager.disableBlend();
/* 167 */     GlStateManager.enableAlpha();
/* 168 */     GlStateManager.enableTexture2D();
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
/* 173 */     fontRendererIn.drawStringWithShadow(text, (x - fontRendererIn.getStringWidth(text) / 2), y, color);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
/* 178 */     fontRendererIn.drawStringWithShadow(text, x, y, color);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
/* 183 */     float f = 0.00390625F;
/* 184 */     float f1 = 0.00390625F;
/* 185 */     Tessellator tessellator = Tessellator.getInstance();
/* 186 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 187 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 188 */     worldrenderer.pos((x + 0), (y + height), this.zLevel).tex(((textureX + 0) * f), ((textureY + height) * f1)).endVertex();
/* 189 */     worldrenderer.pos((x + width), (y + height), this.zLevel).tex(((textureX + width) * f), ((textureY + height) * f1)).endVertex();
/* 190 */     worldrenderer.pos((x + width), (y + 0), this.zLevel).tex(((textureX + width) * f), ((textureY + 0) * f1)).endVertex();
/* 191 */     worldrenderer.pos((x + 0), (y + 0), this.zLevel).tex(((textureX + 0) * f), ((textureY + 0) * f1)).endVertex();
/* 192 */     tessellator.draw();
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV) {
/* 197 */     float f = 0.00390625F;
/* 198 */     float f1 = 0.00390625F;
/* 199 */     Tessellator tessellator = Tessellator.getInstance();
/* 200 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 201 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 202 */     worldrenderer.pos((xCoord + 0.0F), (yCoord + maxV), this.zLevel).tex(((minU + 0) * f), ((minV + maxV) * f1)).endVertex();
/* 203 */     worldrenderer.pos((xCoord + maxU), (yCoord + maxV), this.zLevel).tex(((minU + maxU) * f), ((minV + maxV) * f1)).endVertex();
/* 204 */     worldrenderer.pos((xCoord + maxU), (yCoord + 0.0F), this.zLevel).tex(((minU + maxU) * f), ((minV + 0) * f1)).endVertex();
/* 205 */     worldrenderer.pos((xCoord + 0.0F), (yCoord + 0.0F), this.zLevel).tex(((minU + 0) * f), ((minV + 0) * f1)).endVertex();
/* 206 */     tessellator.draw();
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawTexturedModalRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int widthIn, int heightIn) {
/* 211 */     Tessellator tessellator = Tessellator.getInstance();
/* 212 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 213 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 214 */     worldrenderer.pos((xCoord + 0), (yCoord + heightIn), this.zLevel).tex(textureSprite.getMinU(), textureSprite.getMaxV()).endVertex();
/* 215 */     worldrenderer.pos((xCoord + widthIn), (yCoord + heightIn), this.zLevel).tex(textureSprite.getMaxU(), textureSprite.getMaxV()).endVertex();
/* 216 */     worldrenderer.pos((xCoord + widthIn), (yCoord + 0), this.zLevel).tex(textureSprite.getMaxU(), textureSprite.getMinV()).endVertex();
/* 217 */     worldrenderer.pos((xCoord + 0), (yCoord + 0), this.zLevel).tex(textureSprite.getMinU(), textureSprite.getMinV()).endVertex();
/* 218 */     tessellator.draw();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
/* 223 */     float f = 1.0F / textureWidth;
/* 224 */     float f1 = 1.0F / textureHeight;
/* 225 */     Tessellator tessellator = Tessellator.getInstance();
/* 226 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 227 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 228 */     worldrenderer.pos(x, (y + height), 0.0D).tex((u * f), ((v + height) * f1)).endVertex();
/* 229 */     worldrenderer.pos((x + width), (y + height), 0.0D).tex(((u + width) * f), ((v + height) * f1)).endVertex();
/* 230 */     worldrenderer.pos((x + width), y, 0.0D).tex(((u + width) * f), (v * f1)).endVertex();
/* 231 */     worldrenderer.pos(x, y, 0.0D).tex((u * f), (v * f1)).endVertex();
/* 232 */     tessellator.draw();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
/* 237 */     float f = 1.0F / tileWidth;
/* 238 */     float f1 = 1.0F / tileHeight;
/* 239 */     Tessellator tessellator = Tessellator.getInstance();
/* 240 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 241 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 242 */     worldrenderer.pos(x, (y + height), 0.0D).tex((u * f), ((v + vHeight) * f1)).endVertex();
/* 243 */     worldrenderer.pos((x + width), (y + height), 0.0D).tex(((u + uWidth) * f), ((v + vHeight) * f1)).endVertex();
/* 244 */     worldrenderer.pos((x + width), y, 0.0D).tex(((u + uWidth) * f), (v * f1)).endVertex();
/* 245 */     worldrenderer.pos(x, y, 0.0D).tex((u * f), (v * f1)).endVertex();
/* 246 */     tessellator.draw();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\Gui.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */