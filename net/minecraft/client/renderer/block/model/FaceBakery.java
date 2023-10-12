/*     */ package net.minecraft.client.renderer.block.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.EnumFaceDirection;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.ModelRotation;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3i;
/*     */ import net.minecraftforge.client.model.ITransformation;
/*     */ import net.optifine.model.BlockModelUtils;
/*     */ import net.optifine.reflect.Reflector;
/*     */ import net.optifine.shaders.Shaders;
/*     */ import org.lwjgl.util.vector.Matrix4f;
/*     */ import org.lwjgl.util.vector.ReadableVector3f;
/*     */ import org.lwjgl.util.vector.Vector3f;
/*     */ import org.lwjgl.util.vector.Vector4f;
/*     */ 
/*     */ public class FaceBakery
/*     */ {
/*  21 */   private static final float SCALE_ROTATION_22_5 = 1.0F / (float)Math.cos(0.39269909262657166D) - 1.0F;
/*  22 */   private static final float SCALE_ROTATION_GENERAL = 1.0F / (float)Math.cos(0.7853981633974483D) - 1.0F;
/*     */ 
/*     */   
/*     */   public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face, TextureAtlasSprite sprite, EnumFacing facing, ModelRotation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade) {
/*  26 */     return makeBakedQuad(posFrom, posTo, face, sprite, facing, (ITransformation)modelRotationIn, partRotation, uvLocked, shade);
/*     */   }
/*     */ 
/*     */   
/*     */   public BakedQuad makeBakedQuad(Vector3f p_makeBakedQuad_1_, Vector3f p_makeBakedQuad_2_, BlockPartFace p_makeBakedQuad_3_, TextureAtlasSprite p_makeBakedQuad_4_, EnumFacing p_makeBakedQuad_5_, ITransformation p_makeBakedQuad_6_, BlockPartRotation p_makeBakedQuad_7_, boolean p_makeBakedQuad_8_, boolean p_makeBakedQuad_9_) {
/*  31 */     int[] aint = makeQuadVertexData(p_makeBakedQuad_3_, p_makeBakedQuad_4_, p_makeBakedQuad_5_, getPositionsDiv16(p_makeBakedQuad_1_, p_makeBakedQuad_2_), p_makeBakedQuad_6_, p_makeBakedQuad_7_, p_makeBakedQuad_8_, p_makeBakedQuad_9_);
/*  32 */     EnumFacing enumfacing = getFacingFromVertexData(aint);
/*     */     
/*  34 */     if (p_makeBakedQuad_8_)
/*     */     {
/*  36 */       lockUv(aint, enumfacing, p_makeBakedQuad_3_.blockFaceUV, p_makeBakedQuad_4_);
/*     */     }
/*     */     
/*  39 */     if (p_makeBakedQuad_7_ == null)
/*     */     {
/*  41 */       applyFacing(aint, enumfacing);
/*     */     }
/*     */     
/*  44 */     if (Reflector.ForgeHooksClient_fillNormal.exists())
/*     */     {
/*  46 */       Reflector.call(Reflector.ForgeHooksClient_fillNormal, new Object[] { aint, enumfacing });
/*     */     }
/*     */     
/*  49 */     return new BakedQuad(aint, p_makeBakedQuad_3_.tintIndex, enumfacing);
/*     */   }
/*     */ 
/*     */   
/*     */   private int[] makeQuadVertexData(BlockPartFace p_makeQuadVertexData_1_, TextureAtlasSprite p_makeQuadVertexData_2_, EnumFacing p_makeQuadVertexData_3_, float[] p_makeQuadVertexData_4_, ITransformation p_makeQuadVertexData_5_, BlockPartRotation p_makeQuadVertexData_6_, boolean p_makeQuadVertexData_7_, boolean p_makeQuadVertexData_8_) {
/*  54 */     int i = 28;
/*     */     
/*  56 */     if (Config.isShaders())
/*     */     {
/*  58 */       i = 56;
/*     */     }
/*     */     
/*  61 */     int[] aint = new int[i];
/*     */     
/*  63 */     for (int j = 0; j < 4; j++)
/*     */     {
/*  65 */       fillVertexData(aint, j, p_makeQuadVertexData_3_, p_makeQuadVertexData_1_, p_makeQuadVertexData_4_, p_makeQuadVertexData_2_, p_makeQuadVertexData_5_, p_makeQuadVertexData_6_, p_makeQuadVertexData_7_, p_makeQuadVertexData_8_);
/*     */     }
/*     */     
/*  68 */     return aint;
/*     */   }
/*     */ 
/*     */   
/*     */   private int getFaceShadeColor(EnumFacing facing) {
/*  73 */     float f = getFaceBrightness(facing);
/*  74 */     int i = MathHelper.clamp_int((int)(f * 255.0F), 0, 255);
/*  75 */     return 0xFF000000 | i << 16 | i << 8 | i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getFaceBrightness(EnumFacing p_178412_0_) {
/*  80 */     switch (p_178412_0_) {
/*     */       
/*     */       case X:
/*  83 */         if (Config.isShaders())
/*     */         {
/*  85 */           return Shaders.blockLightLevel05;
/*     */         }
/*     */         
/*  88 */         return 0.5F;
/*     */       
/*     */       case Y:
/*  91 */         return 1.0F;
/*     */       
/*     */       case Z:
/*     */       case null:
/*  95 */         if (Config.isShaders())
/*     */         {
/*  97 */           return Shaders.blockLightLevel08;
/*     */         }
/*     */         
/* 100 */         return 0.8F;
/*     */       
/*     */       case null:
/*     */       case null:
/* 104 */         if (Config.isShaders())
/*     */         {
/* 106 */           return Shaders.blockLightLevel06;
/*     */         }
/*     */         
/* 109 */         return 0.6F;
/*     */     } 
/*     */     
/* 112 */     return 1.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private float[] getPositionsDiv16(Vector3f pos1, Vector3f pos2) {
/* 118 */     float[] afloat = new float[(EnumFacing.values()).length];
/* 119 */     afloat[EnumFaceDirection.Constants.WEST_INDEX] = pos1.x / 16.0F;
/* 120 */     afloat[EnumFaceDirection.Constants.DOWN_INDEX] = pos1.y / 16.0F;
/* 121 */     afloat[EnumFaceDirection.Constants.NORTH_INDEX] = pos1.z / 16.0F;
/* 122 */     afloat[EnumFaceDirection.Constants.EAST_INDEX] = pos2.x / 16.0F;
/* 123 */     afloat[EnumFaceDirection.Constants.UP_INDEX] = pos2.y / 16.0F;
/* 124 */     afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = pos2.z / 16.0F;
/* 125 */     return afloat;
/*     */   }
/*     */ 
/*     */   
/*     */   private void fillVertexData(int[] p_fillVertexData_1_, int p_fillVertexData_2_, EnumFacing p_fillVertexData_3_, BlockPartFace p_fillVertexData_4_, float[] p_fillVertexData_5_, TextureAtlasSprite p_fillVertexData_6_, ITransformation p_fillVertexData_7_, BlockPartRotation p_fillVertexData_8_, boolean p_fillVertexData_9_, boolean p_fillVertexData_10_) {
/* 130 */     EnumFacing enumfacing = p_fillVertexData_7_.rotate(p_fillVertexData_3_);
/* 131 */     int i = p_fillVertexData_10_ ? getFaceShadeColor(enumfacing) : -1;
/* 132 */     EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = EnumFaceDirection.getFacing(p_fillVertexData_3_).getVertexInformation(p_fillVertexData_2_);
/* 133 */     Vector3f vector3f = new Vector3f(p_fillVertexData_5_[enumfacedirection$vertexinformation.xIndex], p_fillVertexData_5_[enumfacedirection$vertexinformation.yIndex], p_fillVertexData_5_[enumfacedirection$vertexinformation.zIndex]);
/* 134 */     rotatePart(vector3f, p_fillVertexData_8_);
/* 135 */     int j = rotateVertex(vector3f, p_fillVertexData_3_, p_fillVertexData_2_, p_fillVertexData_7_, p_fillVertexData_9_);
/* 136 */     BlockModelUtils.snapVertexPosition(vector3f);
/* 137 */     storeVertexData(p_fillVertexData_1_, j, p_fillVertexData_2_, vector3f, i, p_fillVertexData_6_, p_fillVertexData_4_.blockFaceUV);
/*     */   }
/*     */ 
/*     */   
/*     */   private void storeVertexData(int[] faceData, int storeIndex, int vertexIndex, Vector3f position, int shadeColor, TextureAtlasSprite sprite, BlockFaceUV faceUV) {
/* 142 */     int i = faceData.length / 4;
/* 143 */     int j = storeIndex * i;
/* 144 */     faceData[j] = Float.floatToRawIntBits(position.x);
/* 145 */     faceData[j + 1] = Float.floatToRawIntBits(position.y);
/* 146 */     faceData[j + 2] = Float.floatToRawIntBits(position.z);
/* 147 */     faceData[j + 3] = shadeColor;
/* 148 */     faceData[j + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU(faceUV.func_178348_a(vertexIndex) * 0.999D + faceUV.func_178348_a((vertexIndex + 2) % 4) * 0.001D));
/* 149 */     faceData[j + 4 + 1] = Float.floatToRawIntBits(sprite.getInterpolatedV(faceUV.func_178346_b(vertexIndex) * 0.999D + faceUV.func_178346_b((vertexIndex + 2) % 4) * 0.001D));
/*     */   }
/*     */ 
/*     */   
/*     */   private void rotatePart(Vector3f p_178407_1_, BlockPartRotation partRotation) {
/* 154 */     if (partRotation != null) {
/*     */       
/* 156 */       Matrix4f matrix4f = getMatrixIdentity();
/* 157 */       Vector3f vector3f = new Vector3f(0.0F, 0.0F, 0.0F);
/*     */       
/* 159 */       switch (partRotation.axis) {
/*     */         
/*     */         case X:
/* 162 */           Matrix4f.rotate(partRotation.angle * 0.017453292F, new Vector3f(1.0F, 0.0F, 0.0F), matrix4f, matrix4f);
/* 163 */           vector3f.set(0.0F, 1.0F, 1.0F);
/*     */           break;
/*     */         
/*     */         case Y:
/* 167 */           Matrix4f.rotate(partRotation.angle * 0.017453292F, new Vector3f(0.0F, 1.0F, 0.0F), matrix4f, matrix4f);
/* 168 */           vector3f.set(1.0F, 0.0F, 1.0F);
/*     */           break;
/*     */         
/*     */         case Z:
/* 172 */           Matrix4f.rotate(partRotation.angle * 0.017453292F, new Vector3f(0.0F, 0.0F, 1.0F), matrix4f, matrix4f);
/* 173 */           vector3f.set(1.0F, 1.0F, 0.0F);
/*     */           break;
/*     */       } 
/* 176 */       if (partRotation.rescale) {
/*     */         
/* 178 */         if (Math.abs(partRotation.angle) == 22.5F) {
/*     */           
/* 180 */           vector3f.scale(SCALE_ROTATION_22_5);
/*     */         }
/*     */         else {
/*     */           
/* 184 */           vector3f.scale(SCALE_ROTATION_GENERAL);
/*     */         } 
/*     */         
/* 187 */         Vector3f.add(vector3f, new Vector3f(1.0F, 1.0F, 1.0F), vector3f);
/*     */       }
/*     */       else {
/*     */         
/* 191 */         vector3f.set(1.0F, 1.0F, 1.0F);
/*     */       } 
/*     */       
/* 194 */       rotateScale(p_178407_1_, new Vector3f((ReadableVector3f)partRotation.origin), matrix4f, vector3f);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int rotateVertex(Vector3f position, EnumFacing facing, int vertexIndex, ModelRotation modelRotationIn, boolean uvLocked) {
/* 200 */     return rotateVertex(position, facing, vertexIndex, modelRotationIn, uvLocked);
/*     */   }
/*     */ 
/*     */   
/*     */   public int rotateVertex(Vector3f p_rotateVertex_1_, EnumFacing p_rotateVertex_2_, int p_rotateVertex_3_, ITransformation p_rotateVertex_4_, boolean p_rotateVertex_5_) {
/* 205 */     if (p_rotateVertex_4_ == ModelRotation.X0_Y0)
/*     */     {
/* 207 */       return p_rotateVertex_3_;
/*     */     }
/*     */ 
/*     */     
/* 211 */     if (Reflector.ForgeHooksClient_transform.exists()) {
/*     */       
/* 213 */       Reflector.call(Reflector.ForgeHooksClient_transform, new Object[] { p_rotateVertex_1_, p_rotateVertex_4_.getMatrix() });
/*     */     }
/*     */     else {
/*     */       
/* 217 */       rotateScale(p_rotateVertex_1_, new Vector3f(0.5F, 0.5F, 0.5F), ((ModelRotation)p_rotateVertex_4_).getMatrix4d(), new Vector3f(1.0F, 1.0F, 1.0F));
/*     */     } 
/*     */     
/* 220 */     return p_rotateVertex_4_.rotate(p_rotateVertex_2_, p_rotateVertex_3_);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void rotateScale(Vector3f position, Vector3f rotationOrigin, Matrix4f rotationMatrix, Vector3f scale) {
/* 226 */     Vector4f vector4f = new Vector4f(position.x - rotationOrigin.x, position.y - rotationOrigin.y, position.z - rotationOrigin.z, 1.0F);
/* 227 */     Matrix4f.transform(rotationMatrix, vector4f, vector4f);
/* 228 */     vector4f.x *= scale.x;
/* 229 */     vector4f.y *= scale.y;
/* 230 */     vector4f.z *= scale.z;
/* 231 */     position.set(vector4f.x + rotationOrigin.x, vector4f.y + rotationOrigin.y, vector4f.z + rotationOrigin.z);
/*     */   }
/*     */ 
/*     */   
/*     */   private Matrix4f getMatrixIdentity() {
/* 236 */     Matrix4f matrix4f = new Matrix4f();
/* 237 */     matrix4f.setIdentity();
/* 238 */     return matrix4f;
/*     */   }
/*     */ 
/*     */   
/*     */   public static EnumFacing getFacingFromVertexData(int[] faceData) {
/* 243 */     int i = faceData.length / 4;
/* 244 */     int j = i * 2;
/* 245 */     int k = i * 3;
/* 246 */     Vector3f vector3f = new Vector3f(Float.intBitsToFloat(faceData[0]), Float.intBitsToFloat(faceData[1]), Float.intBitsToFloat(faceData[2]));
/* 247 */     Vector3f vector3f1 = new Vector3f(Float.intBitsToFloat(faceData[i]), Float.intBitsToFloat(faceData[i + 1]), Float.intBitsToFloat(faceData[i + 2]));
/* 248 */     Vector3f vector3f2 = new Vector3f(Float.intBitsToFloat(faceData[j]), Float.intBitsToFloat(faceData[j + 1]), Float.intBitsToFloat(faceData[j + 2]));
/* 249 */     Vector3f vector3f3 = new Vector3f();
/* 250 */     Vector3f vector3f4 = new Vector3f();
/* 251 */     Vector3f vector3f5 = new Vector3f();
/* 252 */     Vector3f.sub(vector3f, vector3f1, vector3f3);
/* 253 */     Vector3f.sub(vector3f2, vector3f1, vector3f4);
/* 254 */     Vector3f.cross(vector3f4, vector3f3, vector3f5);
/* 255 */     float f = (float)Math.sqrt((vector3f5.x * vector3f5.x + vector3f5.y * vector3f5.y + vector3f5.z * vector3f5.z));
/* 256 */     vector3f5.x /= f;
/* 257 */     vector3f5.y /= f;
/* 258 */     vector3f5.z /= f;
/* 259 */     EnumFacing enumfacing = null;
/* 260 */     float f1 = 0.0F;
/*     */     
/* 262 */     for (EnumFacing enumfacing1 : EnumFacing.values()) {
/*     */       
/* 264 */       Vec3i vec3i = enumfacing1.getDirectionVec();
/* 265 */       Vector3f vector3f6 = new Vector3f(vec3i.getX(), vec3i.getY(), vec3i.getZ());
/* 266 */       float f2 = Vector3f.dot(vector3f5, vector3f6);
/*     */       
/* 268 */       if (f2 >= 0.0F && f2 > f1) {
/*     */         
/* 270 */         f1 = f2;
/* 271 */         enumfacing = enumfacing1;
/*     */       } 
/*     */     } 
/*     */     
/* 275 */     if (enumfacing == null)
/*     */     {
/* 277 */       return EnumFacing.UP;
/*     */     }
/*     */ 
/*     */     
/* 281 */     return enumfacing;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void lockUv(int[] p_178409_1_, EnumFacing facing, BlockFaceUV p_178409_3_, TextureAtlasSprite p_178409_4_) {
/* 287 */     for (int i = 0; i < 4; i++)
/*     */     {
/* 289 */       lockVertexUv(i, p_178409_1_, facing, p_178409_3_, p_178409_4_);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void applyFacing(int[] p_178408_1_, EnumFacing p_178408_2_) {
/* 295 */     int[] aint = new int[p_178408_1_.length];
/* 296 */     System.arraycopy(p_178408_1_, 0, aint, 0, p_178408_1_.length);
/* 297 */     float[] afloat = new float[(EnumFacing.values()).length];
/* 298 */     afloat[EnumFaceDirection.Constants.WEST_INDEX] = 999.0F;
/* 299 */     afloat[EnumFaceDirection.Constants.DOWN_INDEX] = 999.0F;
/* 300 */     afloat[EnumFaceDirection.Constants.NORTH_INDEX] = 999.0F;
/* 301 */     afloat[EnumFaceDirection.Constants.EAST_INDEX] = -999.0F;
/* 302 */     afloat[EnumFaceDirection.Constants.UP_INDEX] = -999.0F;
/* 303 */     afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = -999.0F;
/* 304 */     int i = p_178408_1_.length / 4;
/*     */     
/* 306 */     for (int j = 0; j < 4; j++) {
/*     */       
/* 308 */       int k = i * j;
/* 309 */       float f = Float.intBitsToFloat(aint[k]);
/* 310 */       float f1 = Float.intBitsToFloat(aint[k + 1]);
/* 311 */       float f2 = Float.intBitsToFloat(aint[k + 2]);
/*     */       
/* 313 */       if (f < afloat[EnumFaceDirection.Constants.WEST_INDEX])
/*     */       {
/* 315 */         afloat[EnumFaceDirection.Constants.WEST_INDEX] = f;
/*     */       }
/*     */       
/* 318 */       if (f1 < afloat[EnumFaceDirection.Constants.DOWN_INDEX])
/*     */       {
/* 320 */         afloat[EnumFaceDirection.Constants.DOWN_INDEX] = f1;
/*     */       }
/*     */       
/* 323 */       if (f2 < afloat[EnumFaceDirection.Constants.NORTH_INDEX])
/*     */       {
/* 325 */         afloat[EnumFaceDirection.Constants.NORTH_INDEX] = f2;
/*     */       }
/*     */       
/* 328 */       if (f > afloat[EnumFaceDirection.Constants.EAST_INDEX])
/*     */       {
/* 330 */         afloat[EnumFaceDirection.Constants.EAST_INDEX] = f;
/*     */       }
/*     */       
/* 333 */       if (f1 > afloat[EnumFaceDirection.Constants.UP_INDEX])
/*     */       {
/* 335 */         afloat[EnumFaceDirection.Constants.UP_INDEX] = f1;
/*     */       }
/*     */       
/* 338 */       if (f2 > afloat[EnumFaceDirection.Constants.SOUTH_INDEX])
/*     */       {
/* 340 */         afloat[EnumFaceDirection.Constants.SOUTH_INDEX] = f2;
/*     */       }
/*     */     } 
/*     */     
/* 344 */     EnumFaceDirection enumfacedirection = EnumFaceDirection.getFacing(p_178408_2_);
/*     */     
/* 346 */     for (int j1 = 0; j1 < 4; j1++) {
/*     */       
/* 348 */       int k1 = i * j1;
/* 349 */       EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = enumfacedirection.getVertexInformation(j1);
/* 350 */       float f8 = afloat[enumfacedirection$vertexinformation.xIndex];
/* 351 */       float f3 = afloat[enumfacedirection$vertexinformation.yIndex];
/* 352 */       float f4 = afloat[enumfacedirection$vertexinformation.zIndex];
/* 353 */       p_178408_1_[k1] = Float.floatToRawIntBits(f8);
/* 354 */       p_178408_1_[k1 + 1] = Float.floatToRawIntBits(f3);
/* 355 */       p_178408_1_[k1 + 2] = Float.floatToRawIntBits(f4);
/*     */       
/* 357 */       for (int l = 0; l < 4; l++) {
/*     */         
/* 359 */         int i1 = i * l;
/* 360 */         float f5 = Float.intBitsToFloat(aint[i1]);
/* 361 */         float f6 = Float.intBitsToFloat(aint[i1 + 1]);
/* 362 */         float f7 = Float.intBitsToFloat(aint[i1 + 2]);
/*     */         
/* 364 */         if (MathHelper.epsilonEquals(f8, f5) && MathHelper.epsilonEquals(f3, f6) && MathHelper.epsilonEquals(f4, f7)) {
/*     */           
/* 366 */           p_178408_1_[k1 + 4] = aint[i1 + 4];
/* 367 */           p_178408_1_[k1 + 4 + 1] = aint[i1 + 4 + 1];
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void lockVertexUv(int p_178401_1_, int[] p_178401_2_, EnumFacing facing, BlockFaceUV p_178401_4_, TextureAtlasSprite p_178401_5_) {
/* 375 */     int i = p_178401_2_.length / 4;
/* 376 */     int j = i * p_178401_1_;
/* 377 */     float f = Float.intBitsToFloat(p_178401_2_[j]);
/* 378 */     float f1 = Float.intBitsToFloat(p_178401_2_[j + 1]);
/* 379 */     float f2 = Float.intBitsToFloat(p_178401_2_[j + 2]);
/*     */     
/* 381 */     if (f < -0.1F || f >= 1.1F)
/*     */     {
/* 383 */       f -= MathHelper.floor_float(f);
/*     */     }
/*     */     
/* 386 */     if (f1 < -0.1F || f1 >= 1.1F)
/*     */     {
/* 388 */       f1 -= MathHelper.floor_float(f1);
/*     */     }
/*     */     
/* 391 */     if (f2 < -0.1F || f2 >= 1.1F)
/*     */     {
/* 393 */       f2 -= MathHelper.floor_float(f2);
/*     */     }
/*     */     
/* 396 */     float f3 = 0.0F;
/* 397 */     float f4 = 0.0F;
/*     */     
/* 399 */     switch (facing) {
/*     */       
/*     */       case X:
/* 402 */         f3 = f * 16.0F;
/* 403 */         f4 = (1.0F - f2) * 16.0F;
/*     */         break;
/*     */       
/*     */       case Y:
/* 407 */         f3 = f * 16.0F;
/* 408 */         f4 = f2 * 16.0F;
/*     */         break;
/*     */       
/*     */       case Z:
/* 412 */         f3 = (1.0F - f) * 16.0F;
/* 413 */         f4 = (1.0F - f1) * 16.0F;
/*     */         break;
/*     */       
/*     */       case null:
/* 417 */         f3 = f * 16.0F;
/* 418 */         f4 = (1.0F - f1) * 16.0F;
/*     */         break;
/*     */       
/*     */       case null:
/* 422 */         f3 = f2 * 16.0F;
/* 423 */         f4 = (1.0F - f1) * 16.0F;
/*     */         break;
/*     */       
/*     */       case null:
/* 427 */         f3 = (1.0F - f2) * 16.0F;
/* 428 */         f4 = (1.0F - f1) * 16.0F;
/*     */         break;
/*     */     } 
/* 431 */     int k = p_178401_4_.func_178345_c(p_178401_1_) * i;
/* 432 */     p_178401_2_[k + 4] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedU(f3));
/* 433 */     p_178401_2_[k + 4 + 1] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedV(f4));
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\block\model\FaceBakery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */