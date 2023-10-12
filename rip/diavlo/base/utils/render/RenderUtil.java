/*     */ package rip.diavlo.base.utils.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GLAllocation;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3;
/*     */ import org.lwjgl.opengl.Display;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.util.glu.GLU;
/*     */ import rip.diavlo.base.utils.render.gl.GLUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RenderUtil
/*     */ {
/*     */   private RenderUtil() {
/*  29 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*  30 */   } private static final Frustum FRUSTUM = new Frustum();
/*  31 */   static Minecraft mc = Minecraft.getMinecraft();
/*     */ 
/*     */   
/*  34 */   private static final FloatBuffer windowPosition = GLAllocation.createDirectFloatBuffer(4);
/*  35 */   private static final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
/*  36 */   private static final FloatBuffer modelMatrix = GLAllocation.createDirectFloatBuffer(16);
/*  37 */   private static final FloatBuffer projectionMatrix = GLAllocation.createDirectFloatBuffer(16);
/*  38 */   private static final float[] BUFFER = new float[3];
/*     */   
/*     */   public static void drawImage(ResourceLocation resourceLocation, float x, float y, float width, float height, int rgba) {
/*  41 */     float a = (rgba >> 24 & 0xFF) / 255.0F;
/*  42 */     float r = (rgba >> 16 & 0xFF) / 255.0F;
/*  43 */     float g = (rgba >> 8 & 0xFF) / 255.0F;
/*  44 */     float b = (rgba & 0xFF) / 255.0F;
/*  45 */     GL11.glPushMatrix();
/*  46 */     mc.getTextureManager().bindTexture(resourceLocation);
/*  47 */     GlStateManager.color(r, g, b, a);
/*  48 */     GlStateManager.enableBlend();
/*  49 */     GlStateManager.blendFunc(770, 771);
/*  50 */     Gui.drawScaledCustomSizeModalRect((int)x, (int)y, 0.0F, 0.0F, (int)width, (int)height, (int)width, (int)height, width, height);
/*  51 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   public static void start() {
/*  55 */     GlStateManager.enableBlend();
/*  56 */     GlStateManager.blendFunc(770, 771);
/*  57 */     GlStateManager.disableTexture2D();
/*  58 */     GlStateManager.disableCull();
/*  59 */     GlStateManager.disableAlpha();
/*  60 */     GlStateManager.disableDepth();
/*     */   }
/*     */   
/*     */   public static void begin(int glMode) {
/*  64 */     GL11.glBegin(glMode);
/*     */   }
/*     */   
/*     */   public static void stop() {
/*  68 */     GlStateManager.enableDepth();
/*  69 */     GlStateManager.enableAlpha();
/*  70 */     GlStateManager.enableCull();
/*  71 */     GlStateManager.enableTexture2D();
/*  72 */     GlStateManager.disableBlend();
/*  73 */     GlStateManager.resetColor();
/*     */   }
/*     */   
/*     */   public static void setAlphaLimit(float limit) {
/*  77 */     GlStateManager.enableAlpha();
/*  78 */     GlStateManager.alphaFunc(516, (float)(limit * 0.01D));
/*     */   }
/*     */   
/*     */   public static void end() {
/*  82 */     GL11.glEnd();
/*     */   }
/*     */   
/*     */   public static void scissor(double x, double y, double width, double height, Runnable data) {
/*  86 */     GL11.glEnable(3089);
/*  87 */     scissor(x, y, width, height);
/*  88 */     data.run();
/*  89 */     GL11.glDisable(3089);
/*     */   }
/*     */   
/*     */   public static void scissor(double x, double y, double width, double height) {
/*  93 */     ScaledResolution sr = new ScaledResolution(mc);
/*  94 */     double scale = sr.getScaleFactor();
/*  95 */     double finalHeight = height * scale;
/*  96 */     double finalY = (sr.getScaledHeight() - y) * scale;
/*  97 */     double finalX = x * scale;
/*  98 */     double finalWidth = width * scale;
/*  99 */     GL11.glScissor((int)finalX, (int)(finalY - finalHeight), (int)finalWidth, (int)finalHeight);
/*     */   }
/*     */   
/*     */   public static void vertex(double x, double y) {
/* 103 */     GL11.glVertex2d(x, y);
/*     */   }
/*     */   
/*     */   public static void color(double red, double green, double blue, double alpha) {
/* 107 */     GL11.glColor4d(red, green, blue, alpha);
/*     */   }
/*     */   
/*     */   public static void color(double red, double green, double blue) {
/* 111 */     color(red, green, blue, 1.0D);
/*     */   }
/*     */   
/*     */   public static void color(Color color) {
/* 115 */     if (color == null)
/* 116 */       color = Color.white; 
/* 117 */     color((color.getRed() / 255.0F), (color.getGreen() / 255.0F), (color.getBlue() / 255.0F), (color.getAlpha() / 255.0F));
/*     */   }
/*     */   
/*     */   public static void color(int color, float alpha) {
/* 121 */     float r = (color >> 16 & 0xFF) / 255.0F;
/* 122 */     float g = (color >> 8 & 0xFF) / 255.0F;
/* 123 */     float b = (color & 0xFF) / 255.0F;
/* 124 */     GlStateManager.color(r, g, b, alpha);
/*     */   }
/*     */   
/*     */   public static void color(int color) {
/* 128 */     color(color, (color >> 24 & 0xFF) / 255.0F);
/*     */   }
/*     */   
/*     */   public static void color(Color color, int alpha) {
/* 132 */     if (color == null)
/* 133 */       color = Color.white; 
/* 134 */     color((color.getRed() / 255.0F), (color.getGreen() / 255.0F), (color.getBlue() / 255.0F), 0.5D);
/*     */   }
/*     */   
/*     */   public static void resetColor() {
/* 138 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public static void scaleStart(float x, float y, float scale) {
/* 142 */     GL11.glPushMatrix();
/* 143 */     GL11.glTranslatef(x, y, 0.0F);
/* 144 */     GL11.glScalef(scale, scale, 1.0F);
/* 145 */     GL11.glTranslatef(-x, -y, 0.0F);
/*     */   }
/*     */   
/*     */   public static void scaleEnd() {
/* 149 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   public static void polygon(double x, double y, double sideLength, double amountOfSides, boolean filled, Color color) {
/* 153 */     sideLength /= 2.0D;
/* 154 */     start();
/* 155 */     if (color != null)
/* 156 */       color(color); 
/* 157 */     if (!filled) GL11.glLineWidth(2.0F); 
/* 158 */     GL11.glEnable(2848);
/* 159 */     begin(filled ? 6 : 3);
/*     */     double i;
/* 161 */     for (i = 0.0D; i <= amountOfSides / 4.0D; i++) {
/* 162 */       double angle = i * 4.0D * 6.283185307179586D / 360.0D;
/* 163 */       vertex(x + sideLength * Math.cos(angle) + sideLength, y + sideLength * Math.sin(angle) + sideLength);
/*     */     } 
/*     */     
/* 166 */     end();
/* 167 */     GL11.glDisable(2848);
/* 168 */     stop();
/*     */   }
/*     */   
/*     */   public static void polygon(double x, double y, double sideLength, int amountOfSides, Color color) {
/* 172 */     polygon(x, y, sideLength, amountOfSides, true, color);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isInViewFrustrum(Entity entity) {
/* 177 */     return (isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck);
/*     */   }
/*     */   
/*     */   private static boolean isInViewFrustrum(AxisAlignedBB bb) {
/* 181 */     Entity current = mc.getRenderViewEntity();
/* 182 */     FRUSTUM.setPosition(current.posX, current.posY, current.posZ);
/* 183 */     return FRUSTUM.isBoundingBoxInFrustum(bb);
/*     */   }
/*     */   
/*     */   public static void renderItem(double x, double y, ItemStack itemStack) {
/* 187 */     if (itemStack != null) {
/* 188 */       GlStateManager.pushMatrix();
/* 189 */       GlStateManager.enableRescaleNormal();
/* 190 */       GlStateManager.enableBlend();
/* 191 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 192 */       RenderHelper.enableGUIStandardItemLighting();
/*     */       
/* 194 */       mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int)x, (int)y);
/*     */       
/* 196 */       GlStateManager.disableRescaleNormal();
/* 197 */       GlStateManager.disableBlend();
/* 198 */       RenderHelper.disableStandardItemLighting();
/* 199 */       GlStateManager.popMatrix();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rectangle(double x, double y, double width, double height, Color color) {
/* 214 */     start();
/*     */     
/* 216 */     if (color != null) {
/* 217 */       ColorUtil.glColor(color);
/*     */     }
/*     */     
/* 220 */     GL11.glBegin(7);
/* 221 */     GL11.glVertex2d(x, y);
/* 222 */     GL11.glVertex2d(x + width, y);
/* 223 */     GL11.glVertex2d(x + width, y + height);
/* 224 */     GL11.glVertex2d(x, y + height);
/* 225 */     GL11.glEnd();
/*     */     
/* 227 */     stop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void horizontalGradient(double x, double y, double width, double height, Color leftColor, Color rightColor) {
/* 234 */     start();
/* 235 */     GL11.glShadeModel(7425);
/* 236 */     GL11.glBegin(7);
/*     */     
/* 238 */     ColorUtil.glColor(leftColor);
/* 239 */     GL11.glVertex2d(x, y);
/* 240 */     GL11.glVertex2d(x, y + height);
/*     */     
/* 242 */     ColorUtil.glColor(rightColor);
/* 243 */     GL11.glVertex2d(x + width, y + height);
/* 244 */     GL11.glVertex2d(x + width, y);
/*     */     
/* 246 */     GL11.glEnd();
/* 247 */     GL11.glShadeModel(7424);
/* 248 */     stop();
/*     */   }
/*     */   
/*     */   public static void verticalGradient(double x, double y, double width, double height, Color topColor, Color bottomColor) {
/* 252 */     start();
/* 253 */     GlStateManager.alphaFunc(516, 0.0F);
/* 254 */     GL11.glShadeModel(7425);
/* 255 */     GL11.glBegin(7);
/*     */     
/* 257 */     ColorUtil.glColor(topColor);
/* 258 */     GL11.glVertex2d(x, y);
/* 259 */     GL11.glVertex2d(x + width, y);
/*     */     
/* 261 */     ColorUtil.glColor(bottomColor);
/* 262 */     GL11.glVertex2d(x + width, y + height);
/* 263 */     GL11.glVertex2d(x, y + height);
/*     */     
/* 265 */     GL11.glEnd();
/* 266 */     GL11.glShadeModel(7424);
/* 267 */     stop();
/*     */   }
/*     */   
/*     */   public static void roundedRectangle(double x, double y, double width, double height, double radius, Color color) {
/* 271 */     (new Shaders()).draw(x, y, width, height, radius, color);
/*     */   }
/*     */   
/*     */   public static void roundedCenteredRectangle(double x, double y, double width, double height, double radius, Color color) {
/* 275 */     (new Shaders()).draw(x - width, y, width, height, radius, color);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void image(ResourceLocation imageLocation, float x, float y, float width, float height, Color color) {
/* 280 */     GL11.glTexParameteri(3553, 10241, 9728);
/* 281 */     GL11.glTexParameteri(3553, 10240, 9728);
/* 282 */     GlStateManager.enableBlend();
/* 283 */     GlStateManager.enableAlpha();
/* 284 */     GlStateManager.alphaFunc(516, 0.0F);
/* 285 */     color(color);
/* 286 */     OpenGlHelper.glBlendFunc(770, 771, 1, 0);
/* 287 */     mc.getTextureManager().bindTexture(imageLocation);
/* 288 */     Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, 0.0F, 0.0F, (int)width, (int)height, width, height);
/* 289 */     GlStateManager.resetColor();
/* 290 */     GlStateManager.disableBlend();
/*     */   }
/*     */   
/*     */   public static void image(ResourceLocation imageLocation, double x, double y, double width, double height, Color color) {
/* 294 */     image(imageLocation, (float)x, (float)y, (float)width, (float)height, color);
/*     */   }
/*     */   
/*     */   public static void image(ResourceLocation imageLocation, float x, float y, float width, float height) {
/* 298 */     image(imageLocation, x, y, width, height, Color.WHITE);
/*     */   }
/*     */   
/*     */   public static void image(ResourceLocation imageLocation, double x, double y, double width, double height) {
/* 302 */     image(imageLocation, (float)x, (float)y, (float)width, (float)height);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void drawBoundingBox(AxisAlignedBB aa) {
/* 308 */     GL11.glBegin(7);
/* 309 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.minY, aa.minZ));
/* 310 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.minZ));
/* 311 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.minZ));
/* 312 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.minZ));
/* 313 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.maxZ));
/* 314 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.maxZ));
/* 315 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.minY, aa.maxZ));
/* 316 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.maxZ));
/* 317 */     end();
/*     */     
/* 319 */     GL11.glBegin(7);
/* 320 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.minZ));
/* 321 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.minZ));
/* 322 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.minZ));
/* 323 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.minY, aa.minZ));
/* 324 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.maxZ));
/* 325 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.minY, aa.maxZ));
/* 326 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.maxZ));
/* 327 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.maxZ));
/* 328 */     end();
/*     */     
/* 330 */     GL11.glBegin(7);
/* 331 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.minZ));
/* 332 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.minZ));
/* 333 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.maxZ));
/* 334 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.maxZ));
/* 335 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.minZ));
/* 336 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.maxZ));
/* 337 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.maxZ));
/* 338 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.minZ));
/* 339 */     end();
/*     */     
/* 341 */     GL11.glBegin(7);
/* 342 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.minY, aa.minZ));
/* 343 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.minZ));
/* 344 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.maxZ));
/* 345 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.minY, aa.maxZ));
/* 346 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.minY, aa.minZ));
/* 347 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.minY, aa.maxZ));
/* 348 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.maxZ));
/* 349 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.minZ));
/* 350 */     end();
/*     */     
/* 352 */     GL11.glBegin(7);
/* 353 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.minY, aa.minZ));
/* 354 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.minZ));
/* 355 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.minY, aa.maxZ));
/* 356 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.maxZ));
/* 357 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.maxZ));
/* 358 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.maxZ));
/* 359 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.minZ));
/* 360 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.minZ));
/* 361 */     end();
/*     */     
/* 363 */     GL11.glBegin(7);
/* 364 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.maxZ));
/* 365 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.minY, aa.maxZ));
/* 366 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.maxY, aa.minZ));
/* 367 */     GLUtil.glVertex3D(getRenderPos(aa.minX, aa.minY, aa.minZ));
/* 368 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.minZ));
/* 369 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.minZ));
/* 370 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.maxY, aa.maxZ));
/* 371 */     GLUtil.glVertex3D(getRenderPos(aa.maxX, aa.minY, aa.maxZ));
/* 372 */     end();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Vec3 getRenderPos(double x, double y, double z) {
/* 377 */     x -= (mc.getRenderManager()).renderPosX;
/* 378 */     y -= (mc.getRenderManager()).renderPosY;
/* 379 */     z -= (mc.getRenderManager()).renderPosZ;
/*     */     
/* 381 */     return new Vec3(x, y, z);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static double interpolate(double old, double now, float partialTicks) {
/* 387 */     return old + (now - old) * partialTicks;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static float[] project2D(float x, float y, float z, int scaleFactor) {
/* 393 */     GL11.glGetFloat(2982, modelMatrix);
/* 394 */     GL11.glGetFloat(2983, projectionMatrix);
/* 395 */     GL11.glGetInteger(2978, viewport);
/* 396 */     if (GLU.gluProject(x, y, z, modelMatrix, projectionMatrix, viewport, windowPosition)) {
/*     */       
/* 398 */       BUFFER[0] = windowPosition.get(0) / scaleFactor;
/* 399 */       BUFFER[1] = (Display.getHeight() - windowPosition.get(1)) / scaleFactor;
/* 400 */       BUFFER[2] = windowPosition.get(2);
/* 401 */       return BUFFER;
/*     */     } 
/* 403 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\render\RenderUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */