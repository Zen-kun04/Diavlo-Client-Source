/*      */ package net.minecraft.client.renderer;
/*      */ 
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.FloatBuffer;
/*      */ import java.nio.IntBuffer;
/*      */ import net.minecraft.src.Config;
/*      */ import net.optifine.SmartAnimations;
/*      */ import net.optifine.render.GlAlphaState;
/*      */ import net.optifine.render.GlBlendState;
/*      */ import net.optifine.shaders.Shaders;
/*      */ import net.optifine.util.LockCounter;
/*      */ import org.lwjgl.opengl.GL11;
/*      */ import org.lwjgl.opengl.GL14;
/*      */ 
/*      */ 
/*      */ 
/*      */ public class GlStateManager
/*      */ {
/*   19 */   private static AlphaState alphaState = new AlphaState();
/*   20 */   private static BooleanState lightingState = new BooleanState(2896);
/*   21 */   private static BooleanState[] lightState = new BooleanState[8];
/*   22 */   private static ColorMaterialState colorMaterialState = new ColorMaterialState();
/*   23 */   private static BlendState blendState = new BlendState();
/*   24 */   private static DepthState depthState = new DepthState();
/*   25 */   private static FogState fogState = new FogState();
/*   26 */   private static CullState cullState = new CullState();
/*   27 */   private static PolygonOffsetState polygonOffsetState = new PolygonOffsetState();
/*   28 */   private static ColorLogicState colorLogicState = new ColorLogicState();
/*   29 */   private static TexGenState texGenState = new TexGenState();
/*   30 */   private static ClearState clearState = new ClearState();
/*   31 */   private static StencilState stencilState = new StencilState();
/*   32 */   private static BooleanState normalizeState = new BooleanState(2977);
/*   33 */   private static int activeTextureUnit = 0;
/*   34 */   private static TextureState[] textureState = new TextureState[32];
/*   35 */   private static int activeShadeModel = 7425;
/*   36 */   private static BooleanState rescaleNormalState = new BooleanState(32826);
/*   37 */   private static ColorMask colorMaskState = new ColorMask();
/*   38 */   private static Color colorState = new Color();
/*      */   public static boolean clearEnabled = true;
/*   40 */   private static LockCounter alphaLock = new LockCounter();
/*   41 */   private static GlAlphaState alphaLockState = new GlAlphaState();
/*   42 */   private static LockCounter blendLock = new LockCounter();
/*   43 */   private static GlBlendState blendLockState = new GlBlendState();
/*      */   
/*      */   private static boolean creatingDisplayList = false;
/*      */   
/*      */   public static void pushAttrib() {
/*   48 */     GL11.glPushAttrib(8256);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void popAttrib() {
/*   53 */     GL11.glPopAttrib();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disableAlpha() {
/*   58 */     if (alphaLock.isLocked()) {
/*      */       
/*   60 */       alphaLockState.setDisabled();
/*      */     }
/*      */     else {
/*      */       
/*   64 */       alphaState.alphaTest.setDisabled();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void enableAlpha() {
/*   70 */     if (alphaLock.isLocked()) {
/*      */       
/*   72 */       alphaLockState.setEnabled();
/*      */     }
/*      */     else {
/*      */       
/*   76 */       alphaState.alphaTest.setEnabled();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void alphaFunc(int func, float ref) {
/*   82 */     if (alphaLock.isLocked()) {
/*      */       
/*   84 */       alphaLockState.setFuncRef(func, ref);
/*      */ 
/*      */     
/*      */     }
/*   88 */     else if (func != alphaState.func || ref != alphaState.ref) {
/*      */       
/*   90 */       alphaState.func = func;
/*   91 */       alphaState.ref = ref;
/*   92 */       GL11.glAlphaFunc(func, ref);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void enableLighting() {
/*   99 */     lightingState.setEnabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disableLighting() {
/*  104 */     lightingState.setDisabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void enableLight(int light) {
/*  109 */     lightState[light].setEnabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disableLight(int light) {
/*  114 */     lightState[light].setDisabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void enableColorMaterial() {
/*  119 */     colorMaterialState.colorMaterial.setEnabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disableColorMaterial() {
/*  124 */     colorMaterialState.colorMaterial.setDisabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void colorMaterial(int face, int mode) {
/*  129 */     if (face != colorMaterialState.face || mode != colorMaterialState.mode) {
/*      */       
/*  131 */       colorMaterialState.face = face;
/*  132 */       colorMaterialState.mode = mode;
/*  133 */       GL11.glColorMaterial(face, mode);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disableDepth() {
/*  139 */     depthState.depthTest.setDisabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void enableDepth() {
/*  144 */     depthState.depthTest.setEnabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void depthFunc(int depthFunc) {
/*  149 */     if (depthFunc != depthState.depthFunc) {
/*      */       
/*  151 */       depthState.depthFunc = depthFunc;
/*  152 */       GL11.glDepthFunc(depthFunc);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void depthMask(boolean flagIn) {
/*  158 */     if (flagIn != depthState.maskEnabled) {
/*      */       
/*  160 */       depthState.maskEnabled = flagIn;
/*  161 */       GL11.glDepthMask(flagIn);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disableBlend() {
/*  167 */     if (blendLock.isLocked()) {
/*      */       
/*  169 */       blendLockState.setDisabled();
/*      */     }
/*      */     else {
/*      */       
/*  173 */       blendState.blend.setDisabled();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void enableBlend() {
/*  179 */     if (blendLock.isLocked()) {
/*      */       
/*  181 */       blendLockState.setEnabled();
/*      */     }
/*      */     else {
/*      */       
/*  185 */       blendState.blend.setEnabled();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void blendFunc(int srcFactor, int dstFactor) {
/*  191 */     if (blendLock.isLocked()) {
/*      */       
/*  193 */       blendLockState.setFactors(srcFactor, dstFactor);
/*      */ 
/*      */     
/*      */     }
/*  197 */     else if (srcFactor != blendState.srcFactor || dstFactor != blendState.dstFactor || srcFactor != blendState.srcFactorAlpha || dstFactor != blendState.dstFactorAlpha) {
/*      */       
/*  199 */       blendState.srcFactor = srcFactor;
/*  200 */       blendState.dstFactor = dstFactor;
/*  201 */       blendState.srcFactorAlpha = srcFactor;
/*  202 */       blendState.dstFactorAlpha = dstFactor;
/*      */       
/*  204 */       if (Config.isShaders())
/*      */       {
/*  206 */         Shaders.uniform_blendFunc.setValue(srcFactor, dstFactor, srcFactor, dstFactor);
/*      */       }
/*      */       
/*  209 */       GL11.glBlendFunc(srcFactor, dstFactor);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void tryBlendFuncSeparate(int srcFactor, int dstFactor, int srcFactorAlpha, int dstFactorAlpha) {
/*  216 */     if (blendLock.isLocked()) {
/*      */       
/*  218 */       blendLockState.setFactors(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
/*      */ 
/*      */     
/*      */     }
/*  222 */     else if (srcFactor != blendState.srcFactor || dstFactor != blendState.dstFactor || srcFactorAlpha != blendState.srcFactorAlpha || dstFactorAlpha != blendState.dstFactorAlpha) {
/*      */       
/*  224 */       blendState.srcFactor = srcFactor;
/*  225 */       blendState.dstFactor = dstFactor;
/*  226 */       blendState.srcFactorAlpha = srcFactorAlpha;
/*  227 */       blendState.dstFactorAlpha = dstFactorAlpha;
/*      */       
/*  229 */       if (Config.isShaders())
/*      */       {
/*  231 */         Shaders.uniform_blendFunc.setValue(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
/*      */       }
/*      */       
/*  234 */       OpenGlHelper.glBlendFunc(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void enableFog() {
/*  241 */     fogState.fog.setEnabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disableFog() {
/*  246 */     fogState.fog.setDisabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setFog(int param) {
/*  251 */     if (param != fogState.mode) {
/*      */       
/*  253 */       fogState.mode = param;
/*  254 */       GL11.glFogi(2917, param);
/*      */       
/*  256 */       if (Config.isShaders())
/*      */       {
/*  258 */         Shaders.setFogMode(param);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setFogDensity(float param) {
/*  265 */     if (param < 0.0F)
/*      */     {
/*  267 */       param = 0.0F;
/*      */     }
/*      */     
/*  270 */     if (param != fogState.density) {
/*      */       
/*  272 */       fogState.density = param;
/*  273 */       GL11.glFogf(2914, param);
/*      */       
/*  275 */       if (Config.isShaders())
/*      */       {
/*  277 */         Shaders.setFogDensity(param);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setFogStart(float param) {
/*  284 */     if (param != fogState.start) {
/*      */       
/*  286 */       fogState.start = param;
/*  287 */       GL11.glFogf(2915, param);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setFogEnd(float param) {
/*  293 */     if (param != fogState.end) {
/*      */       
/*  295 */       fogState.end = param;
/*  296 */       GL11.glFogf(2916, param);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glFog(int p_glFog_0_, FloatBuffer p_glFog_1_) {
/*  302 */     GL11.glFog(p_glFog_0_, p_glFog_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glFogi(int p_glFogi_0_, int p_glFogi_1_) {
/*  307 */     GL11.glFogi(p_glFogi_0_, p_glFogi_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void enableCull() {
/*  312 */     cullState.cullFace.setEnabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disableCull() {
/*  317 */     cullState.cullFace.setDisabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void cullFace(int mode) {
/*  322 */     if (mode != cullState.mode) {
/*      */       
/*  324 */       cullState.mode = mode;
/*  325 */       GL11.glCullFace(mode);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void enablePolygonOffset() {
/*  331 */     polygonOffsetState.polygonOffsetFill.setEnabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disablePolygonOffset() {
/*  336 */     polygonOffsetState.polygonOffsetFill.setDisabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void doPolygonOffset(float factor, float units) {
/*  341 */     if (factor != polygonOffsetState.factor || units != polygonOffsetState.units) {
/*      */       
/*  343 */       polygonOffsetState.factor = factor;
/*  344 */       polygonOffsetState.units = units;
/*  345 */       GL11.glPolygonOffset(factor, units);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void enableColorLogic() {
/*  351 */     colorLogicState.colorLogicOp.setEnabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disableColorLogic() {
/*  356 */     colorLogicState.colorLogicOp.setDisabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void colorLogicOp(int opcode) {
/*  361 */     if (opcode != colorLogicState.opcode) {
/*      */       
/*  363 */       colorLogicState.opcode = opcode;
/*  364 */       GL11.glLogicOp(opcode);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void enableTexGenCoord(TexGen p_179087_0_) {
/*  370 */     (texGenCoord(p_179087_0_)).textureGen.setEnabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disableTexGenCoord(TexGen p_179100_0_) {
/*  375 */     (texGenCoord(p_179100_0_)).textureGen.setDisabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void texGen(TexGen texGen, int param) {
/*  380 */     TexGenCoord glstatemanager$texgencoord = texGenCoord(texGen);
/*      */     
/*  382 */     if (param != glstatemanager$texgencoord.param) {
/*      */       
/*  384 */       glstatemanager$texgencoord.param = param;
/*  385 */       GL11.glTexGeni(glstatemanager$texgencoord.coord, 9472, param);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void texGen(TexGen p_179105_0_, int pname, FloatBuffer params) {
/*  391 */     GL11.glTexGen((texGenCoord(p_179105_0_)).coord, pname, params);
/*      */   }
/*      */ 
/*      */   
/*      */   private static TexGenCoord texGenCoord(TexGen p_179125_0_) {
/*  396 */     switch (p_179125_0_) {
/*      */       
/*      */       case S:
/*  399 */         return texGenState.s;
/*      */       
/*      */       case T:
/*  402 */         return texGenState.t;
/*      */       
/*      */       case R:
/*  405 */         return texGenState.r;
/*      */       
/*      */       case Q:
/*  408 */         return texGenState.q;
/*      */     } 
/*      */     
/*  411 */     return texGenState.s;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setActiveTexture(int texture) {
/*  417 */     if (activeTextureUnit != texture - OpenGlHelper.defaultTexUnit) {
/*      */       
/*  419 */       activeTextureUnit = texture - OpenGlHelper.defaultTexUnit;
/*  420 */       OpenGlHelper.setActiveTexture(texture);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void enableTexture2D() {
/*  426 */     (textureState[activeTextureUnit]).texture2DState.setEnabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disableTexture2D() {
/*  431 */     (textureState[activeTextureUnit]).texture2DState.setDisabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static int generateTexture() {
/*  436 */     return GL11.glGenTextures();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void deleteTexture(int texture) {
/*  441 */     if (texture != 0) {
/*      */       
/*  443 */       GL11.glDeleteTextures(texture);
/*      */       
/*  445 */       for (TextureState glstatemanager$texturestate : textureState) {
/*      */         
/*  447 */         if (glstatemanager$texturestate.textureName == texture)
/*      */         {
/*  449 */           glstatemanager$texturestate.textureName = 0;
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void bindTexture(int texture) {
/*  457 */     if (texture != (textureState[activeTextureUnit]).textureName) {
/*      */       
/*  459 */       (textureState[activeTextureUnit]).textureName = texture;
/*  460 */       GL11.glBindTexture(3553, texture);
/*      */       
/*  462 */       if (SmartAnimations.isActive())
/*      */       {
/*  464 */         SmartAnimations.textureRendered(texture);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void enableNormalize() {
/*  471 */     normalizeState.setEnabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disableNormalize() {
/*  476 */     normalizeState.setDisabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void shadeModel(int mode) {
/*  481 */     if (mode != activeShadeModel) {
/*      */       
/*  483 */       activeShadeModel = mode;
/*  484 */       GL11.glShadeModel(mode);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void enableRescaleNormal() {
/*  490 */     rescaleNormalState.setEnabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void disableRescaleNormal() {
/*  495 */     rescaleNormalState.setDisabled();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void viewport(int x, int y, int width, int height) {
/*  500 */     GL11.glViewport(x, y, width, height);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
/*  505 */     if (red != colorMaskState.red || green != colorMaskState.green || blue != colorMaskState.blue || alpha != colorMaskState.alpha) {
/*      */       
/*  507 */       colorMaskState.red = red;
/*  508 */       colorMaskState.green = green;
/*  509 */       colorMaskState.blue = blue;
/*  510 */       colorMaskState.alpha = alpha;
/*  511 */       GL11.glColorMask(red, green, blue, alpha);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void clearDepth(double depth) {
/*  517 */     if (depth != clearState.depth) {
/*      */       
/*  519 */       clearState.depth = depth;
/*  520 */       GL11.glClearDepth(depth);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void clearColor(java.awt.Color color) {
/*  525 */     clearColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
/*      */   }
/*      */   
/*      */   public static void clearColor(float red, float green, float blue, float alpha) {
/*  529 */     if (red != clearState.color.red || green != clearState.color.green || blue != clearState.color.blue || alpha != clearState.color.alpha) {
/*      */       
/*  531 */       clearState.color.red = red;
/*  532 */       clearState.color.green = green;
/*  533 */       clearState.color.blue = blue;
/*  534 */       clearState.color.alpha = alpha;
/*  535 */       GL11.glClearColor(red, green, blue, alpha);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void clear(int mask) {
/*  541 */     if (clearEnabled)
/*      */     {
/*  543 */       GL11.glClear(mask);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void matrixMode(int mode) {
/*  549 */     GL11.glMatrixMode(mode);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void loadIdentity() {
/*  554 */     GL11.glLoadIdentity();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void pushMatrix() {
/*  559 */     GL11.glPushMatrix();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void popMatrix() {
/*  564 */     GL11.glPopMatrix();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void getFloat(int pname, FloatBuffer params) {
/*  569 */     GL11.glGetFloat(pname, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void ortho(double left, double right, double bottom, double top, double zNear, double zFar) {
/*  574 */     GL11.glOrtho(left, right, bottom, top, zNear, zFar);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void rotate(float angle, float x, float y, float z) {
/*  579 */     GL11.glRotatef(angle, x, y, z);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void scale(float x, float y, float z) {
/*  584 */     GL11.glScalef(x, y, z);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void scale(double x, double y, double z) {
/*  589 */     GL11.glScaled(x, y, z);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void translate(float x, float y, float z) {
/*  594 */     GL11.glTranslatef(x, y, z);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void translate(double x, double y, double z) {
/*  599 */     GL11.glTranslated(x, y, z);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void multMatrix(FloatBuffer matrix) {
/*  604 */     GL11.glMultMatrix(matrix);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void color(float colorRed, float colorGreen, float colorBlue, float colorAlpha) {
/*  609 */     if (colorRed != colorState.red || colorGreen != colorState.green || colorBlue != colorState.blue || colorAlpha != colorState.alpha) {
/*      */       
/*  611 */       colorState.red = colorRed;
/*  612 */       colorState.green = colorGreen;
/*  613 */       colorState.blue = colorBlue;
/*  614 */       colorState.alpha = colorAlpha;
/*  615 */       GL11.glColor4f(colorRed, colorGreen, colorBlue, colorAlpha);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void color(float colorRed, float colorGreen, float colorBlue) {
/*  621 */     color(colorRed, colorGreen, colorBlue, 1.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void resetColor() {
/*  626 */     colorState.red = colorState.green = colorState.blue = colorState.alpha = -1.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glNormalPointer(int p_glNormalPointer_0_, int p_glNormalPointer_1_, ByteBuffer p_glNormalPointer_2_) {
/*  631 */     GL11.glNormalPointer(p_glNormalPointer_0_, p_glNormalPointer_1_, p_glNormalPointer_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glTexCoordPointer(int p_glTexCoordPointer_0_, int p_glTexCoordPointer_1_, int p_glTexCoordPointer_2_, int p_glTexCoordPointer_3_) {
/*  636 */     GL11.glTexCoordPointer(p_glTexCoordPointer_0_, p_glTexCoordPointer_1_, p_glTexCoordPointer_2_, p_glTexCoordPointer_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glTexCoordPointer(int p_glTexCoordPointer_0_, int p_glTexCoordPointer_1_, int p_glTexCoordPointer_2_, ByteBuffer p_glTexCoordPointer_3_) {
/*  641 */     GL11.glTexCoordPointer(p_glTexCoordPointer_0_, p_glTexCoordPointer_1_, p_glTexCoordPointer_2_, p_glTexCoordPointer_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glVertexPointer(int p_glVertexPointer_0_, int p_glVertexPointer_1_, int p_glVertexPointer_2_, int p_glVertexPointer_3_) {
/*  646 */     GL11.glVertexPointer(p_glVertexPointer_0_, p_glVertexPointer_1_, p_glVertexPointer_2_, p_glVertexPointer_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glVertexPointer(int p_glVertexPointer_0_, int p_glVertexPointer_1_, int p_glVertexPointer_2_, ByteBuffer p_glVertexPointer_3_) {
/*  651 */     GL11.glVertexPointer(p_glVertexPointer_0_, p_glVertexPointer_1_, p_glVertexPointer_2_, p_glVertexPointer_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glColorPointer(int p_glColorPointer_0_, int p_glColorPointer_1_, int p_glColorPointer_2_, int p_glColorPointer_3_) {
/*  656 */     GL11.glColorPointer(p_glColorPointer_0_, p_glColorPointer_1_, p_glColorPointer_2_, p_glColorPointer_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glColorPointer(int p_glColorPointer_0_, int p_glColorPointer_1_, int p_glColorPointer_2_, ByteBuffer p_glColorPointer_3_) {
/*  661 */     GL11.glColorPointer(p_glColorPointer_0_, p_glColorPointer_1_, p_glColorPointer_2_, p_glColorPointer_3_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glDisableClientState(int p_glDisableClientState_0_) {
/*  666 */     GL11.glDisableClientState(p_glDisableClientState_0_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glEnableClientState(int p_glEnableClientState_0_) {
/*  671 */     GL11.glEnableClientState(p_glEnableClientState_0_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glBegin(int p_glBegin_0_) {
/*  676 */     GL11.glBegin(p_glBegin_0_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glEnd() {
/*  681 */     GL11.glEnd();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glDrawArrays(int p_glDrawArrays_0_, int p_glDrawArrays_1_, int p_glDrawArrays_2_) {
/*  686 */     GL11.glDrawArrays(p_glDrawArrays_0_, p_glDrawArrays_1_, p_glDrawArrays_2_);
/*      */     
/*  688 */     if (Config.isShaders() && !creatingDisplayList) {
/*      */       
/*  690 */       int i = Shaders.activeProgram.getCountInstances();
/*      */       
/*  692 */       if (i > 1) {
/*      */         
/*  694 */         for (int j = 1; j < i; j++) {
/*      */           
/*  696 */           Shaders.uniform_instanceId.setValue(j);
/*  697 */           GL11.glDrawArrays(p_glDrawArrays_0_, p_glDrawArrays_1_, p_glDrawArrays_2_);
/*      */         } 
/*      */         
/*  700 */         Shaders.uniform_instanceId.setValue(0);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void callList(int list) {
/*  707 */     GL11.glCallList(list);
/*      */     
/*  709 */     if (Config.isShaders() && !creatingDisplayList) {
/*      */       
/*  711 */       int i = Shaders.activeProgram.getCountInstances();
/*      */       
/*  713 */       if (i > 1) {
/*      */         
/*  715 */         for (int j = 1; j < i; j++) {
/*      */           
/*  717 */           Shaders.uniform_instanceId.setValue(j);
/*  718 */           GL11.glCallList(list);
/*      */         } 
/*      */         
/*  721 */         Shaders.uniform_instanceId.setValue(0);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void callLists(IntBuffer p_callLists_0_) {
/*  728 */     GL11.glCallLists(p_callLists_0_);
/*      */     
/*  730 */     if (Config.isShaders() && !creatingDisplayList) {
/*      */       
/*  732 */       int i = Shaders.activeProgram.getCountInstances();
/*      */       
/*  734 */       if (i > 1) {
/*      */         
/*  736 */         for (int j = 1; j < i; j++) {
/*      */           
/*  738 */           Shaders.uniform_instanceId.setValue(j);
/*  739 */           GL11.glCallLists(p_callLists_0_);
/*      */         } 
/*      */         
/*  742 */         Shaders.uniform_instanceId.setValue(0);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glDeleteLists(int p_glDeleteLists_0_, int p_glDeleteLists_1_) {
/*  749 */     GL11.glDeleteLists(p_glDeleteLists_0_, p_glDeleteLists_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glNewList(int p_glNewList_0_, int p_glNewList_1_) {
/*  754 */     GL11.glNewList(p_glNewList_0_, p_glNewList_1_);
/*  755 */     creatingDisplayList = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glEndList() {
/*  760 */     GL11.glEndList();
/*  761 */     creatingDisplayList = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int glGetError() {
/*  766 */     return GL11.glGetError();
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glTexImage2D(int p_glTexImage2D_0_, int p_glTexImage2D_1_, int p_glTexImage2D_2_, int p_glTexImage2D_3_, int p_glTexImage2D_4_, int p_glTexImage2D_5_, int p_glTexImage2D_6_, int p_glTexImage2D_7_, IntBuffer p_glTexImage2D_8_) {
/*  771 */     GL11.glTexImage2D(p_glTexImage2D_0_, p_glTexImage2D_1_, p_glTexImage2D_2_, p_glTexImage2D_3_, p_glTexImage2D_4_, p_glTexImage2D_5_, p_glTexImage2D_6_, p_glTexImage2D_7_, p_glTexImage2D_8_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glTexSubImage2D(int p_glTexSubImage2D_0_, int p_glTexSubImage2D_1_, int p_glTexSubImage2D_2_, int p_glTexSubImage2D_3_, int p_glTexSubImage2D_4_, int p_glTexSubImage2D_5_, int p_glTexSubImage2D_6_, int p_glTexSubImage2D_7_, IntBuffer p_glTexSubImage2D_8_) {
/*  776 */     GL11.glTexSubImage2D(p_glTexSubImage2D_0_, p_glTexSubImage2D_1_, p_glTexSubImage2D_2_, p_glTexSubImage2D_3_, p_glTexSubImage2D_4_, p_glTexSubImage2D_5_, p_glTexSubImage2D_6_, p_glTexSubImage2D_7_, p_glTexSubImage2D_8_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glCopyTexSubImage2D(int p_glCopyTexSubImage2D_0_, int p_glCopyTexSubImage2D_1_, int p_glCopyTexSubImage2D_2_, int p_glCopyTexSubImage2D_3_, int p_glCopyTexSubImage2D_4_, int p_glCopyTexSubImage2D_5_, int p_glCopyTexSubImage2D_6_, int p_glCopyTexSubImage2D_7_) {
/*  781 */     GL11.glCopyTexSubImage2D(p_glCopyTexSubImage2D_0_, p_glCopyTexSubImage2D_1_, p_glCopyTexSubImage2D_2_, p_glCopyTexSubImage2D_3_, p_glCopyTexSubImage2D_4_, p_glCopyTexSubImage2D_5_, p_glCopyTexSubImage2D_6_, p_glCopyTexSubImage2D_7_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glGetTexImage(int p_glGetTexImage_0_, int p_glGetTexImage_1_, int p_glGetTexImage_2_, int p_glGetTexImage_3_, IntBuffer p_glGetTexImage_4_) {
/*  786 */     GL11.glGetTexImage(p_glGetTexImage_0_, p_glGetTexImage_1_, p_glGetTexImage_2_, p_glGetTexImage_3_, p_glGetTexImage_4_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glTexParameterf(int p_glTexParameterf_0_, int p_glTexParameterf_1_, float p_glTexParameterf_2_) {
/*  791 */     GL11.glTexParameterf(p_glTexParameterf_0_, p_glTexParameterf_1_, p_glTexParameterf_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glTexParameteri(int p_glTexParameteri_0_, int p_glTexParameteri_1_, int p_glTexParameteri_2_) {
/*  796 */     GL11.glTexParameteri(p_glTexParameteri_0_, p_glTexParameteri_1_, p_glTexParameteri_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int glGetTexLevelParameteri(int p_glGetTexLevelParameteri_0_, int p_glGetTexLevelParameteri_1_, int p_glGetTexLevelParameteri_2_) {
/*  801 */     return GL11.glGetTexLevelParameteri(p_glGetTexLevelParameteri_0_, p_glGetTexLevelParameteri_1_, p_glGetTexLevelParameteri_2_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getActiveTextureUnit() {
/*  806 */     return OpenGlHelper.defaultTexUnit + activeTextureUnit;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void bindCurrentTexture() {
/*  811 */     GL11.glBindTexture(3553, (textureState[activeTextureUnit]).textureName);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getBoundTexture() {
/*  816 */     return (textureState[activeTextureUnit]).textureName;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void checkBoundTexture() {
/*  821 */     if (Config.isMinecraftThread()) {
/*      */       
/*  823 */       int i = GL11.glGetInteger(34016);
/*  824 */       int j = GL11.glGetInteger(32873);
/*  825 */       int k = getActiveTextureUnit();
/*  826 */       int l = getBoundTexture();
/*      */       
/*  828 */       if (l > 0)
/*      */       {
/*  830 */         if (i != k || j != l)
/*      */         {
/*  832 */           Config.dbg("checkTexture: act: " + k + ", glAct: " + i + ", tex: " + l + ", glTex: " + j);
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void deleteTextures(IntBuffer p_deleteTextures_0_) {
/*  840 */     p_deleteTextures_0_.rewind();
/*      */     
/*  842 */     while (p_deleteTextures_0_.position() < p_deleteTextures_0_.limit()) {
/*      */       
/*  844 */       int i = p_deleteTextures_0_.get();
/*  845 */       deleteTexture(i);
/*      */     } 
/*      */     
/*  848 */     p_deleteTextures_0_.rewind();
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isFogEnabled() {
/*  853 */     return fogState.fog.currentState;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setFogEnabled(boolean p_setFogEnabled_0_) {
/*  858 */     fogState.fog.setState(p_setFogEnabled_0_);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void lockAlpha(GlAlphaState p_lockAlpha_0_) {
/*  863 */     if (!alphaLock.isLocked()) {
/*      */       
/*  865 */       getAlphaState(alphaLockState);
/*  866 */       setAlphaState(p_lockAlpha_0_);
/*  867 */       alphaLock.lock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void unlockAlpha() {
/*  873 */     if (alphaLock.unlock())
/*      */     {
/*  875 */       setAlphaState(alphaLockState);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void getAlphaState(GlAlphaState p_getAlphaState_0_) {
/*  881 */     if (alphaLock.isLocked()) {
/*      */       
/*  883 */       p_getAlphaState_0_.setState(alphaLockState);
/*      */     }
/*      */     else {
/*      */       
/*  887 */       p_getAlphaState_0_.setState(alphaState.alphaTest.currentState, alphaState.func, alphaState.ref);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setAlphaState(GlAlphaState p_setAlphaState_0_) {
/*  893 */     if (alphaLock.isLocked()) {
/*      */       
/*  895 */       alphaLockState.setState(p_setAlphaState_0_);
/*      */     }
/*      */     else {
/*      */       
/*  899 */       alphaState.alphaTest.setState(p_setAlphaState_0_.isEnabled());
/*  900 */       alphaFunc(p_setAlphaState_0_.getFunc(), p_setAlphaState_0_.getRef());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void lockBlend(GlBlendState p_lockBlend_0_) {
/*  906 */     if (!blendLock.isLocked()) {
/*      */       
/*  908 */       getBlendState(blendLockState);
/*  909 */       setBlendState(p_lockBlend_0_);
/*  910 */       blendLock.lock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void unlockBlend() {
/*  916 */     if (blendLock.unlock())
/*      */     {
/*  918 */       setBlendState(blendLockState);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void getBlendState(GlBlendState p_getBlendState_0_) {
/*  924 */     if (blendLock.isLocked()) {
/*      */       
/*  926 */       p_getBlendState_0_.setState(blendLockState);
/*      */     }
/*      */     else {
/*      */       
/*  930 */       p_getBlendState_0_.setState(blendState.blend.currentState, blendState.srcFactor, blendState.dstFactor, blendState.srcFactorAlpha, blendState.dstFactorAlpha);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setBlendState(GlBlendState p_setBlendState_0_) {
/*  936 */     if (blendLock.isLocked()) {
/*      */       
/*  938 */       blendLockState.setState(p_setBlendState_0_);
/*      */     }
/*      */     else {
/*      */       
/*  942 */       blendState.blend.setState(p_setBlendState_0_.isEnabled());
/*      */       
/*  944 */       if (!p_setBlendState_0_.isSeparate()) {
/*      */         
/*  946 */         blendFunc(p_setBlendState_0_.getSrcFactor(), p_setBlendState_0_.getDstFactor());
/*      */       }
/*      */       else {
/*      */         
/*  950 */         tryBlendFuncSeparate(p_setBlendState_0_.getSrcFactor(), p_setBlendState_0_.getDstFactor(), p_setBlendState_0_.getSrcFactorAlpha(), p_setBlendState_0_.getDstFactorAlpha());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void glMultiDrawArrays(int p_glMultiDrawArrays_0_, IntBuffer p_glMultiDrawArrays_1_, IntBuffer p_glMultiDrawArrays_2_) {
/*  957 */     GL14.glMultiDrawArrays(p_glMultiDrawArrays_0_, p_glMultiDrawArrays_1_, p_glMultiDrawArrays_2_);
/*      */     
/*  959 */     if (Config.isShaders() && !creatingDisplayList) {
/*      */       
/*  961 */       int i = Shaders.activeProgram.getCountInstances();
/*      */       
/*  963 */       if (i > 1) {
/*      */         
/*  965 */         for (int j = 1; j < i; j++) {
/*      */           
/*  967 */           Shaders.uniform_instanceId.setValue(j);
/*  968 */           GL14.glMultiDrawArrays(p_glMultiDrawArrays_0_, p_glMultiDrawArrays_1_, p_glMultiDrawArrays_2_);
/*      */         } 
/*      */         
/*  971 */         Shaders.uniform_instanceId.setValue(0);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static {
/*  978 */     for (int i = 0; i < 8; i++)
/*      */     {
/*  980 */       lightState[i] = new BooleanState(16384 + i);
/*      */     }
/*      */     
/*  983 */     for (int j = 0; j < textureState.length; j++)
/*      */     {
/*  985 */       textureState[j] = new TextureState();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class AlphaState
/*      */   {
/*      */     private AlphaState() {}
/*      */ 
/*      */ 
/*      */     
/*  997 */     public GlStateManager.BooleanState alphaTest = new GlStateManager.BooleanState(3008);
/*  998 */     public int func = 519;
/*  999 */     public float ref = -1.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class BlendState
/*      */   {
/*      */     private BlendState() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1013 */     public GlStateManager.BooleanState blend = new GlStateManager.BooleanState(3042);
/* 1014 */     public int srcFactor = 1;
/* 1015 */     public int dstFactor = 0;
/* 1016 */     public int srcFactorAlpha = 1;
/* 1017 */     public int dstFactorAlpha = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   static class BooleanState
/*      */   {
/*      */     private final int capability;
/*      */     
/*      */     private boolean currentState = false;
/*      */     
/*      */     public BooleanState(int capabilityIn) {
/* 1028 */       this.capability = capabilityIn;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setDisabled() {
/* 1033 */       setState(false);
/*      */     }
/*      */ 
/*      */     
/*      */     public void setEnabled() {
/* 1038 */       setState(true);
/*      */     }
/*      */ 
/*      */     
/*      */     public void setState(boolean state) {
/* 1043 */       if (state != this.currentState) {
/*      */         
/* 1045 */         this.currentState = state;
/*      */         
/* 1047 */         if (state) {
/*      */           
/* 1049 */           GL11.glEnable(this.capability);
/*      */         }
/*      */         else {
/*      */           
/* 1053 */           GL11.glDisable(this.capability);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class ClearState
/*      */   {
/*      */     private ClearState() {}
/*      */ 
/*      */ 
/*      */     
/* 1067 */     public double depth = 1.0D;
/* 1068 */     public GlStateManager.Color color = new GlStateManager.Color(0.0F, 0.0F, 0.0F, 0.0F);
/* 1069 */     public int field_179204_c = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   static class Color
/*      */   {
/* 1075 */     public float red = 1.0F;
/* 1076 */     public float green = 1.0F;
/* 1077 */     public float blue = 1.0F;
/* 1078 */     public float alpha = 1.0F;
/*      */ 
/*      */ 
/*      */     
/*      */     public Color() {}
/*      */ 
/*      */     
/*      */     public Color(float redIn, float greenIn, float blueIn, float alphaIn) {
/* 1086 */       this.red = redIn;
/* 1087 */       this.green = greenIn;
/* 1088 */       this.blue = blueIn;
/* 1089 */       this.alpha = alphaIn;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class ColorLogicState
/*      */   {
/*      */     private ColorLogicState() {}
/*      */ 
/*      */     
/* 1100 */     public GlStateManager.BooleanState colorLogicOp = new GlStateManager.BooleanState(3058);
/* 1101 */     public int opcode = 5379;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class ColorMask
/*      */   {
/*      */     private ColorMask() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean red = true;
/*      */ 
/*      */     
/*      */     public boolean green = true;
/*      */ 
/*      */     
/*      */     public boolean blue = true;
/*      */     
/*      */     public boolean alpha = true;
/*      */   }
/*      */ 
/*      */   
/*      */   static class ColorMaterialState
/*      */   {
/*      */     private ColorMaterialState() {}
/*      */ 
/*      */     
/* 1129 */     public GlStateManager.BooleanState colorMaterial = new GlStateManager.BooleanState(2903);
/* 1130 */     public int face = 1032;
/* 1131 */     public int mode = 5634;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class CullState
/*      */   {
/*      */     private CullState() {}
/*      */ 
/*      */ 
/*      */     
/* 1142 */     public GlStateManager.BooleanState cullFace = new GlStateManager.BooleanState(2884);
/* 1143 */     public int mode = 1029;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class DepthState
/*      */   {
/*      */     private DepthState() {}
/*      */ 
/*      */ 
/*      */     
/* 1155 */     public GlStateManager.BooleanState depthTest = new GlStateManager.BooleanState(2929);
/*      */     public boolean maskEnabled = true;
/* 1157 */     public int depthFunc = 513;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class FogState
/*      */   {
/*      */     private FogState() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1171 */     public GlStateManager.BooleanState fog = new GlStateManager.BooleanState(2912);
/* 1172 */     public int mode = 2048;
/* 1173 */     public float density = 1.0F;
/* 1174 */     public float start = 0.0F;
/* 1175 */     public float end = 1.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class PolygonOffsetState
/*      */   {
/*      */     private PolygonOffsetState() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1188 */     public GlStateManager.BooleanState polygonOffsetFill = new GlStateManager.BooleanState(32823);
/* 1189 */     public GlStateManager.BooleanState polygonOffsetLine = new GlStateManager.BooleanState(10754);
/* 1190 */     public float factor = 0.0F;
/* 1191 */     public float units = 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class StencilFunc
/*      */   {
/*      */     private StencilFunc() {}
/*      */ 
/*      */ 
/*      */     
/* 1203 */     public int field_179081_a = 519;
/* 1204 */     public int field_179079_b = 0;
/* 1205 */     public int field_179080_c = -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class StencilState
/*      */   {
/*      */     private StencilState() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1219 */     public GlStateManager.StencilFunc field_179078_a = new GlStateManager.StencilFunc();
/* 1220 */     public int field_179076_b = -1;
/* 1221 */     public int field_179077_c = 7680;
/* 1222 */     public int field_179074_d = 7680;
/* 1223 */     public int field_179075_e = 7680;
/*      */   }
/*      */ 
/*      */   
/*      */   public enum TexGen
/*      */   {
/* 1229 */     S,
/* 1230 */     T,
/* 1231 */     R,
/* 1232 */     Q;
/*      */   }
/*      */   
/*      */   static class TexGenCoord
/*      */   {
/*      */     public GlStateManager.BooleanState textureGen;
/*      */     public int coord;
/* 1239 */     public int param = -1;
/*      */ 
/*      */     
/*      */     public TexGenCoord(int p_i46254_1_, int p_i46254_2_) {
/* 1243 */       this.coord = p_i46254_1_;
/* 1244 */       this.textureGen = new GlStateManager.BooleanState(p_i46254_2_);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class TexGenState
/*      */   {
/*      */     private TexGenState() {}
/*      */ 
/*      */ 
/*      */     
/* 1257 */     public GlStateManager.TexGenCoord s = new GlStateManager.TexGenCoord(8192, 3168);
/* 1258 */     public GlStateManager.TexGenCoord t = new GlStateManager.TexGenCoord(8193, 3169);
/* 1259 */     public GlStateManager.TexGenCoord r = new GlStateManager.TexGenCoord(8194, 3170);
/* 1260 */     public GlStateManager.TexGenCoord q = new GlStateManager.TexGenCoord(8195, 3171);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class TextureState
/*      */   {
/*      */     private TextureState() {}
/*      */ 
/*      */ 
/*      */     
/* 1271 */     public GlStateManager.BooleanState texture2DState = new GlStateManager.BooleanState(3553);
/* 1272 */     public int textureName = 0;
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\GlStateManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */