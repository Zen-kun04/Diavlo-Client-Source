/*     */ package rip.diavlo.base.events.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.ThreadDownloadImageData;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.client.renderer.texture.ITextureObject;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3d;
/*     */ import net.minecraft.util.Vec3i;
/*     */ import org.lwjgl.opengl.GL11;
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
/*     */ public class RenderUtils
/*     */ {
/*     */   public static void tracerLine(BlockPos loc, int color) {
/*  37 */     double d0 = loc.getX() - (Minecraft.getMinecraft().getRenderManager()).renderPosX;
/*  38 */     double d1 = loc.getY() - (Minecraft.getMinecraft().getRenderManager()).renderPosY;
/*  39 */     double d2 = loc.getZ() - (Minecraft.getMinecraft().getRenderManager()).renderPosZ;
/*  40 */     GL11.glBlendFunc(770, 771);
/*  41 */     GL11.glEnable(3042);
/*  42 */     GL11.glLineWidth(2.0F);
/*  43 */     GL11.glDisable(3553);
/*  44 */     GL11.glDisable(2929);
/*  45 */     GL11.glDepthMask(false);
/*  46 */     if (color == 0) {
/*  47 */       GL11.glColor4d(1.0D - (Minecraft.getMinecraft()).thePlayer.getDistanceSqToCenter(loc) / 40.0D, 
/*  48 */           (Minecraft.getMinecraft()).thePlayer.getDistanceSqToCenter(loc) / 40.0D, 0.0D, 0.5D);
/*  49 */     } else if (color == 1) {
/*  50 */       GL11.glColor4d(0.0D, 0.0D, 1.0D, 0.5D);
/*  51 */     } else if (color == 2) {
/*  52 */       GL11.glColor4d(1.0D, 1.0D, 0.0D, 0.5D);
/*  53 */     } else if (color == 3) {
/*  54 */       GL11.glColor4d(1.0D, 0.0D, 0.0D, 0.5D);
/*  55 */     } else if (color == 4) {
/*  56 */       GL11.glColor4d(0.0D, 1.0D, 0.0D, 0.5D);
/*     */     } 
/*     */ 
/*     */     
/*  60 */     Vec3d vec3d = (new Vec3d(0.0D, 0.0D, 1.0D)).rotatePitch(-((float)Math.toRadians((Minecraft.getMinecraft()).thePlayer.rotationPitch))).rotateYaw(-((float)Math.toRadians((Minecraft.getMinecraft()).thePlayer.rotationYaw)));
/*  61 */     GL11.glBegin(1);
/*  62 */     GL11.glVertex3d(vec3d.xCoord, (Minecraft.getMinecraft()).thePlayer.getEyeHeight() + vec3d.yCoord, vec3d.zCoord);
/*     */     
/*  64 */     GL11.glVertex3d(d0, d1, d2);
/*  65 */     GL11.glEnd();
/*  66 */     GL11.glEnable(3553);
/*  67 */     GL11.glEnable(2929);
/*  68 */     GL11.glDepthMask(true);
/*  69 */     GL11.glDisable(3042);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void framelessBlockESP(int x, int y, int z, int xoffset, int yoffset, int zoffset, float red, float green, float blue) {
/*  74 */     double d0 = x - (Minecraft.getMinecraft().getRenderManager()).renderPosX;
/*  75 */     double d1 = y - (Minecraft.getMinecraft().getRenderManager()).renderPosY;
/*  76 */     double d2 = z - (Minecraft.getMinecraft().getRenderManager()).renderPosZ;
/*  77 */     GL11.glBlendFunc(770, 771);
/*  78 */     GL11.glEnable(3042);
/*  79 */     GL11.glLineWidth(2.0F);
/*  80 */     GL11.glDisable(3553);
/*  81 */     GL11.glDisable(2929);
/*  82 */     GL11.glDepthMask(false);
/*  83 */     GL11.glColor4f(red, green, blue, 0.15F);
/*  84 */     drawColorBox(new AxisAlignedBB(d0, d1, d2, d0 + xoffset, d1 + yoffset, d2 + zoffset), red, green, blue, 0.15F);
/*  85 */     GL11.glEnable(3553);
/*  86 */     GL11.glEnable(2929);
/*  87 */     GL11.glDepthMask(true);
/*  88 */     GL11.glDisable(3042);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void framelessBlockESP(BlockPos pos, Vec3i offset, float red, float green, float blue) {
/*  93 */     double d0 = pos.getX() - (Minecraft.getMinecraft().getRenderManager()).renderPosX + 0.5D;
/*  94 */     double d1 = pos.getY() - (Minecraft.getMinecraft().getRenderManager()).renderPosY + 0.5D;
/*  95 */     double d2 = pos.getZ() - (Minecraft.getMinecraft().getRenderManager()).renderPosZ + 0.5D;
/*  96 */     GL11.glBlendFunc(770, 771);
/*  97 */     GL11.glEnable(3042);
/*  98 */     GL11.glLineWidth(2.0F);
/*  99 */     GL11.glDisable(3553);
/* 100 */     GL11.glDisable(2929);
/* 101 */     GL11.glDepthMask(false);
/* 102 */     GL11.glColor4f(red, green, blue, 0.15F);
/* 103 */     double d = 0.25D;
/* 104 */     double x = offset.getX() * (0.5D - d);
/* 105 */     double y = offset.getY() * (0.5D - d);
/* 106 */     double z = offset.getZ() * (0.5D - d);
/*     */     
/* 108 */     drawColorBox(new AxisAlignedBB(d0 - d + x, d1 - d + y, d2 - d + z, d0 + d + x, d1 + d + y, d2 + d + z), red, green, blue, 0.15F);
/*     */     
/* 110 */     GL11.glEnable(3553);
/* 111 */     GL11.glEnable(2929);
/* 112 */     GL11.glDepthMask(true);
/* 113 */     GL11.glDisable(3042);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void renderString(BlockPos pos, String s) {
/* 118 */     GlStateManager.pushMatrix();
/*     */     
/* 120 */     Minecraft mc = Minecraft.getMinecraft();
/* 121 */     double x = pos.getX() - (mc.getRenderManager()).renderPosX + 0.5D;
/* 122 */     double y = pos.getY() - (mc.getRenderManager()).renderPosY + 1.0D;
/* 123 */     double z = pos.getZ() - (mc.getRenderManager()).renderPosZ + 0.5D;
/* 124 */     GlStateManager.translate(x, y, z);
/* 125 */     GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/* 126 */     GlStateManager.rotate(-(mc.getRenderManager()).playerViewY, 0.0F, 1.0F, 0.0F);
/* 127 */     GlStateManager.rotate((mc.getRenderManager()).playerViewX, 1.0F, 0.0F, 0.0F);
/* 128 */     float scale = 0.05F;
/* 129 */     GlStateManager.scale(-scale, -scale, scale);
/* 130 */     GlStateManager.disableDepth();
/*     */     
/* 132 */     GlStateManager.enableBlend();
/* 133 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*     */     
/* 135 */     GlStateManager.disableBlend();
/* 136 */     mc.fontRendererObj.drawString(s, (-mc.fontRendererObj.getStringWidth(s) / 2), 0.0D, -1);
/*     */     
/* 138 */     GlStateManager.popMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent, boolean rotate) {
/* 143 */     GlStateManager.enableDepth();
/* 144 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 145 */     GlStateManager.enableColorMaterial();
/* 146 */     GlStateManager.pushMatrix();
/* 147 */     GlStateManager.translate(posX, posY, 50.0F);
/* 148 */     GlStateManager.scale(-scale, scale, scale);
/* 149 */     GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
/* 150 */     float f = ent.renderYawOffset;
/* 151 */     float f1 = ent.rotationYaw;
/* 152 */     float f2 = ent.rotationPitch;
/* 153 */     float f3 = ent.prevRotationYawHead;
/* 154 */     float f4 = ent.rotationYawHead;
/* 155 */     GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
/* 156 */     RenderHelper.enableStandardItemLighting();
/* 157 */     GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
/* 158 */     if (rotate) {
/*     */       
/* 160 */       ent.renderYawOffset = (float)Math.atan(((posX - mouseX) / 40.0F)) * 20.0F;
/* 161 */       ent.rotationYaw = (float)Math.atan(((posX - mouseX) / 40.0F)) * 40.0F;
/* 162 */       GlStateManager.rotate(-((float)Math.atan(((posY - mouseY) / 40.0F - ent.getEyeHeight()))) * 20.0F, 1.0F, 0.0F, 0.0F);
/*     */       
/* 164 */       ent.rotationPitch = -((float)Math.atan(((posY - mouseY) / 40.0F - ent.getEyeHeight()))) * 20.0F;
/*     */     }
/*     */     else {
/*     */       
/* 168 */       ent.renderYawOffset = 0.0F;
/* 169 */       ent.rotationYaw = 0.0F;
/* 170 */       ent.rotationPitch = 0.0F;
/*     */     } 
/* 172 */     ent.rotationYawHead = ent.rotationYaw;
/* 173 */     ent.prevRotationYawHead = ent.rotationYaw;
/* 174 */     GlStateManager.translate(0.0F, 0.0F, 0.0F);
/* 175 */     RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
/* 176 */     rendermanager.setPlayerViewY(180.0F);
/* 177 */     rendermanager.setRenderShadow(false);
/*     */     
/* 179 */     rendermanager.doRenderEntity((Entity)ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
/* 180 */     rendermanager.setRenderShadow(true);
/* 181 */     ent.renderYawOffset = f;
/* 182 */     ent.rotationYaw = f1;
/* 183 */     ent.rotationPitch = f2;
/* 184 */     ent.prevRotationYawHead = f3;
/* 185 */     ent.rotationYawHead = f4;
/* 186 */     GlStateManager.popMatrix();
/* 187 */     RenderHelper.disableStandardItemLighting();
/* 188 */     GlStateManager.disableRescaleNormal();
/* 189 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/* 190 */     GlStateManager.disableTexture2D();
/* 191 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/* 192 */     GlStateManager.disableDepth();
/*     */   }
/*     */   
/*     */   public static ThreadDownloadImageData downloadURL(ResourceLocation resourceLocationIn, String url) {
/*     */     ThreadDownloadImageData threadDownloadImageData;
/* 197 */     TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
/* 198 */     ITextureObject itextureobject = texturemanager.getTexture(resourceLocationIn);
/* 199 */     if (itextureobject == null) {
/*     */       
/* 201 */       threadDownloadImageData = new ThreadDownloadImageData(null, url, null, null);
/*     */       
/* 203 */       texturemanager.loadTexture(resourceLocationIn, (ITextureObject)threadDownloadImageData);
/*     */     } 
/* 205 */     return threadDownloadImageData;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void drawPic(int x, int y, int h, int w, ResourceLocation pic) {
/* 210 */     GlStateManager.enableAlpha();
/* 211 */     GlStateManager.disableLighting();
/* 212 */     GlStateManager.disableFog();
/* 213 */     Tessellator tessellator = Tessellator.getInstance();
/* 214 */     WorldRenderer vertexbuffer = tessellator.getWorldRenderer();
/* 215 */     Minecraft.getMinecraft().getTextureManager().bindTexture(pic);
/* 216 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 217 */     vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 218 */     vertexbuffer.pos(x, (y + w), 0.0D).tex(0.0D, 1.0D).color(255, 255, 255, 255).endVertex();
/* 219 */     vertexbuffer.pos((x + h), (y + w), 0.0D).tex(1.0D, 1.0D).color(255, 255, 255, 255).endVertex();
/* 220 */     vertexbuffer.pos((x + h), y, 0.0D).tex(1.0D, 0.0D).color(255, 255, 255, 255).endVertex();
/* 221 */     vertexbuffer.pos(x, y, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
/* 222 */     tessellator.draw();
/* 223 */     GlStateManager.disableAlpha();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void drawPic(int x, int y, int h, String pic) {
/* 228 */     drawPic(x, y, h, h, new ResourceLocation("mcmodding4k/" + pic + ".png"));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void framelessBlockESP(BlockPos blockPos, float red, float green, float blue, float alpha) {
/* 233 */     double d0 = blockPos.getX() - (Minecraft.getMinecraft().getRenderManager()).renderPosX;
/* 234 */     double d1 = blockPos.getY() - (Minecraft.getMinecraft().getRenderManager()).renderPosY;
/* 235 */     double d2 = blockPos.getZ() - (Minecraft.getMinecraft().getRenderManager()).renderPosZ;
/* 236 */     GL11.glBlendFunc(770, 771);
/* 237 */     GL11.glEnable(3042);
/* 238 */     GL11.glLineWidth(2.0F);
/* 239 */     GL11.glDisable(3553);
/* 240 */     GL11.glDisable(2929);
/* 241 */     GL11.glDepthMask(false);
/* 242 */     GL11.glColor4f(red, green, blue, alpha);
/* 243 */     drawColorBox(new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 1.0D, d2 + 1.0D), red, green, blue, alpha);
/* 244 */     GL11.glEnable(3553);
/* 245 */     GL11.glEnable(2929);
/* 246 */     GL11.glDepthMask(true);
/* 247 */     GL11.glDisable(3042);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void framelessBlockESP(BlockPos blockPos, Color color) {
/* 256 */     double x = blockPos.getX() - (Minecraft.getMinecraft().getRenderManager()).renderPosX;
/* 257 */     double y = blockPos.getY() - (Minecraft.getMinecraft().getRenderManager()).renderPosY;
/* 258 */     double z = blockPos.getZ() - (Minecraft.getMinecraft().getRenderManager()).renderPosZ;
/* 259 */     GL11.glBlendFunc(770, 771);
/* 260 */     GL11.glEnable(3042);
/* 261 */     GL11.glLineWidth(2.0F);
/* 262 */     GL11.glColor4d((color.getRed() / 255), (color.getGreen() / 255), (color.getBlue() / 255), 0.15D);
/* 263 */     GL11.glDisable(3553);
/* 264 */     GL11.glDisable(2929);
/* 265 */     GL11.glDepthMask(false);
/* 266 */     drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
/* 267 */     GL11.glEnable(3553);
/* 268 */     GL11.glEnable(2929);
/* 269 */     GL11.glDepthMask(true);
/* 270 */     GL11.glDisable(3042);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 275 */   public static int enemy = 0;
/* 276 */   public static int friend = 1;
/* 277 */   public static int other = 2;
/* 278 */   public static int target = 3;
/* 279 */   public static int team = 4;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void drawColorBox(AxisAlignedBB axisalignedbb) {
/* 285 */     drawColorBox(axisalignedbb, 255.0F, 255.0F, 255.0F, 255.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void drawColorBox(AxisAlignedBB axisalignedbb, float red, float green, float blue, float alpha) {
/* 290 */     Tessellator tessellator = Tessellator.getInstance();
/* 291 */     WorldRenderer vertexbuffer = tessellator.getWorldRenderer();
/* 292 */     vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 293 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 294 */       .endVertex();
/* 295 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 296 */       .endVertex();
/* 297 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 298 */       .endVertex();
/* 299 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 300 */       .endVertex();
/* 301 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 302 */       .endVertex();
/* 303 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 304 */       .endVertex();
/* 305 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 306 */       .endVertex();
/* 307 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 308 */       .endVertex();
/* 309 */     tessellator.draw();
/* 310 */     vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 311 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 312 */       .endVertex();
/* 313 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 314 */       .endVertex();
/* 315 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 316 */       .endVertex();
/* 317 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 318 */       .endVertex();
/* 319 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 320 */       .endVertex();
/* 321 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 322 */       .endVertex();
/* 323 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 324 */       .endVertex();
/* 325 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 326 */       .endVertex();
/* 327 */     tessellator.draw();
/* 328 */     vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 329 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 330 */       .endVertex();
/* 331 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 332 */       .endVertex();
/* 333 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 334 */       .endVertex();
/* 335 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 336 */       .endVertex();
/* 337 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 338 */       .endVertex();
/* 339 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 340 */       .endVertex();
/* 341 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 342 */       .endVertex();
/* 343 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 344 */       .endVertex();
/* 345 */     tessellator.draw();
/* 346 */     vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 347 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 348 */       .endVertex();
/* 349 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 350 */       .endVertex();
/* 351 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 352 */       .endVertex();
/* 353 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 354 */       .endVertex();
/* 355 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 356 */       .endVertex();
/* 357 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 358 */       .endVertex();
/* 359 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 360 */       .endVertex();
/* 361 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 362 */       .endVertex();
/* 363 */     tessellator.draw();
/* 364 */     vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 365 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 366 */       .endVertex();
/* 367 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 368 */       .endVertex();
/* 369 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 370 */       .endVertex();
/* 371 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 372 */       .endVertex();
/* 373 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 374 */       .endVertex();
/* 375 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 376 */       .endVertex();
/* 377 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 378 */       .endVertex();
/* 379 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 380 */       .endVertex();
/* 381 */     tessellator.draw();
/* 382 */     vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 383 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 384 */       .endVertex();
/* 385 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 386 */       .endVertex();
/* 387 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 388 */       .endVertex();
/* 389 */     vertexbuffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 390 */       .endVertex();
/* 391 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 392 */       .endVertex();
/* 393 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha)
/* 394 */       .endVertex();
/* 395 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 396 */       .endVertex();
/* 397 */     vertexbuffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha)
/* 398 */       .endVertex();
/* 399 */     tessellator.draw();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void tracerLine(Entity entity, int mode) {
/* 404 */     if (mode == 0) {
/* 405 */       tracerLine(entity, (1.0F - (Minecraft.getMinecraft()).thePlayer.getDistanceToEntity(entity) / 40.0F), (
/* 406 */           (Minecraft.getMinecraft()).thePlayer.getDistanceToEntity(entity) / 40.0F), 0.0D, 0.5D);
/* 407 */     } else if (mode == 1) {
/* 408 */       tracerLine(entity, 0.0D, 0.0D, 1.0D, 0.5D);
/* 409 */     } else if (mode == 2) {
/* 410 */       tracerLine(entity, 1.0D, 1.0D, 0.0D, 0.5D);
/* 411 */     } else if (mode == 3) {
/* 412 */       tracerLine(entity, 1.0D, 0.0D, 0.0D, 0.5D);
/* 413 */     } else if (mode == 4) {
/* 414 */       tracerLine(entity, 0.0D, 1.0D, 0.0D, 0.5D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void tracerLine(Entity entity, double red, double green, double blue, double alpha) {
/* 420 */     double d0 = entity.posX - (Minecraft.getMinecraft().getRenderManager()).renderPosX;
/*     */     
/* 422 */     double d1 = entity.posY + (entity.height / 2.0F) - (Minecraft.getMinecraft().getRenderManager()).renderPosY;
/* 423 */     double d2 = entity.posZ - (Minecraft.getMinecraft().getRenderManager()).renderPosZ;
/* 424 */     GL11.glBlendFunc(770, 771);
/* 425 */     GL11.glEnable(3042);
/* 426 */     GL11.glLineWidth(2.0F);
/* 427 */     GL11.glDisable(3553);
/* 428 */     GL11.glDisable(2929);
/* 429 */     GL11.glDepthMask(false);
/*     */     
/* 431 */     GL11.glColor4d(red, green, blue, alpha);
/*     */ 
/*     */ 
/*     */     
/* 435 */     Vec3d vec3d = (new Vec3d(0.0D, 0.0D, 1.0D)).rotatePitch(-((float)Math.toRadians((Minecraft.getMinecraft()).thePlayer.rotationPitch))).rotateYaw(-((float)Math.toRadians((Minecraft.getMinecraft()).thePlayer.rotationYaw)));
/* 436 */     GL11.glBegin(1);
/* 437 */     GL11.glVertex3d(vec3d.xCoord, (Minecraft.getMinecraft()).thePlayer.getEyeHeight() + vec3d.yCoord, vec3d.zCoord);
/*     */     
/* 439 */     GL11.glVertex3d(d0, d1, d2);
/* 440 */     GL11.glEnd();
/* 441 */     GL11.glEnable(3553);
/* 442 */     GL11.glEnable(2929);
/* 443 */     GL11.glDepthMask(true);
/* 444 */     GL11.glDisable(3042);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void tracerLine(Entity entity, Color color) {
/* 449 */     double x = entity.posX - (Minecraft.getMinecraft().getRenderManager()).renderPosX;
/* 450 */     double y = entity.posY + (entity.height / 2.0F) - (Minecraft.getMinecraft().getRenderManager()).renderPosY;
/* 451 */     double z = entity.posZ - (Minecraft.getMinecraft().getRenderManager()).renderPosZ;
/* 452 */     GL11.glBlendFunc(770, 771);
/* 453 */     GL11.glEnable(3042);
/* 454 */     GL11.glLineWidth(2.0F);
/* 455 */     GL11.glDisable(3553);
/* 456 */     GL11.glDisable(2929);
/* 457 */     GL11.glDepthMask(false);
/* 458 */     setColor(color);
/* 459 */     GL11.glBegin(1);
/*     */     
/* 461 */     GL11.glVertex3d(0.0D, (Minecraft.getMinecraft()).thePlayer.getEyeHeight(), 0.0D);
/* 462 */     GL11.glVertex3d(x, y, z);
/*     */     
/* 464 */     GL11.glEnd();
/* 465 */     GL11.glEnable(3553);
/* 466 */     GL11.glEnable(2929);
/* 467 */     GL11.glDepthMask(true);
/* 468 */     GL11.glDisable(3042);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void tracerLine(int x, int y, int z, Color color) {
/* 473 */     x = (int)(x + 0.5D - (Minecraft.getMinecraft().getRenderManager()).renderPosX);
/* 474 */     y = (int)(y + 0.5D - (Minecraft.getMinecraft().getRenderManager()).renderPosY);
/* 475 */     z = (int)(z + 0.5D - (Minecraft.getMinecraft().getRenderManager()).renderPosZ);
/* 476 */     GL11.glBlendFunc(770, 771);
/* 477 */     GL11.glEnable(3042);
/* 478 */     GL11.glLineWidth(2.0F);
/* 479 */     GL11.glDisable(3553);
/* 480 */     GL11.glDisable(2929);
/* 481 */     GL11.glDepthMask(false);
/* 482 */     setColor(color);
/* 483 */     GL11.glBegin(1);
/*     */     
/* 485 */     GL11.glVertex3d(0.0D, (Minecraft.getMinecraft()).thePlayer.getEyeHeight(), 0.0D);
/* 486 */     GL11.glVertex3d(x, y, z);
/*     */     
/* 488 */     GL11.glEnd();
/* 489 */     GL11.glEnable(3553);
/* 490 */     GL11.glEnable(2929);
/* 491 */     GL11.glDepthMask(true);
/* 492 */     GL11.glDisable(3042);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void tracerLine(double x, double y, double z, Color color) {
/* 497 */     x -= (Minecraft.getMinecraft().getRenderManager()).renderPosX;
/* 498 */     y -= (Minecraft.getMinecraft().getRenderManager()).renderPosY;
/* 499 */     z -= (Minecraft.getMinecraft().getRenderManager()).renderPosZ;
/* 500 */     GL11.glBlendFunc(770, 771);
/* 501 */     GL11.glEnable(3042);
/* 502 */     GL11.glLineWidth(2.0F);
/* 503 */     GL11.glDisable(3553);
/* 504 */     GL11.glDisable(2929);
/* 505 */     GL11.glDepthMask(false);
/* 506 */     setColor(color);
/* 507 */     GL11.glBegin(1);
/*     */     
/* 509 */     GL11.glVertex3d(0.0D, (Minecraft.getMinecraft()).thePlayer.getEyeHeight(), 0.0D);
/* 510 */     GL11.glVertex3d(x, y, z);
/*     */     
/* 512 */     GL11.glEnd();
/* 513 */     GL11.glEnable(3553);
/* 514 */     GL11.glEnable(2929);
/* 515 */     GL11.glDepthMask(true);
/* 516 */     GL11.glDisable(3042);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void scissorBox(int x, int y, int xend, int yend) {
/* 521 */     int width = xend - x;
/* 522 */     int height = yend - y;
/* 523 */     ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
/* 524 */     int factor = sr.getScaleFactor();
/* 525 */     int bottomY = (Minecraft.getMinecraft()).currentScreen.height - yend;
/* 526 */     GL11.glScissor(x * factor, bottomY * factor, width * factor, height * factor);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setColor(Color c) {
/* 531 */     GL11.glColor4f(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F, c.getAlpha() / 255.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void drawLine(double x, double y, double x_, double y_, Color color) {
/* 536 */     GL11.glLineWidth(5.0F);
/* 537 */     GL11.glDisable(3553);
/* 538 */     setColor(color);
/* 539 */     GL11.glBegin(1);
/*     */     
/* 541 */     GL11.glVertex2d(x, y);
/* 542 */     GL11.glVertex2d(x_, y_);
/*     */     
/* 544 */     GL11.glEnd();
/* 545 */     GL11.glEnable(3553);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\events\render\RenderUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */