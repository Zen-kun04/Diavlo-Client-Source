/*     */ package net.optifine.shaders;
/*     */ 
/*     */ import org.lwjgl.opengl.ARBShaderObjects;
/*     */ 
/*     */ 
/*     */ public class ShaderProgramData
/*     */ {
/*     */   public int programIDGL;
/*     */   public int uniform_texture;
/*     */   public int uniform_lightmap;
/*     */   public int uniform_normals;
/*     */   public int uniform_specular;
/*     */   public int uniform_shadow;
/*     */   public int uniform_watershadow;
/*     */   public int uniform_shadowtex0;
/*     */   public int uniform_shadowtex1;
/*     */   public int uniform_depthtex0;
/*     */   public int uniform_depthtex1;
/*     */   public int uniform_shadowcolor;
/*     */   public int uniform_shadowcolor0;
/*     */   public int uniform_shadowcolor1;
/*     */   public int uniform_noisetex;
/*     */   public int uniform_gcolor;
/*     */   public int uniform_gdepth;
/*     */   public int uniform_gnormal;
/*     */   public int uniform_composite;
/*     */   public int uniform_gaux1;
/*     */   public int uniform_gaux2;
/*     */   public int uniform_gaux3;
/*     */   public int uniform_gaux4;
/*     */   public int uniform_colortex0;
/*     */   public int uniform_colortex1;
/*     */   public int uniform_colortex2;
/*     */   public int uniform_colortex3;
/*     */   public int uniform_colortex4;
/*     */   public int uniform_colortex5;
/*     */   public int uniform_colortex6;
/*     */   public int uniform_colortex7;
/*     */   public int uniform_gdepthtex;
/*     */   public int uniform_depthtex2;
/*     */   public int uniform_tex;
/*     */   public int uniform_heldItemId;
/*     */   public int uniform_heldBlockLightValue;
/*     */   public int uniform_fogMode;
/*     */   public int uniform_fogColor;
/*     */   public int uniform_skyColor;
/*     */   public int uniform_worldTime;
/*     */   public int uniform_moonPhase;
/*     */   public int uniform_frameTimeCounter;
/*     */   public int uniform_sunAngle;
/*     */   public int uniform_shadowAngle;
/*     */   public int uniform_rainStrength;
/*     */   public int uniform_aspectRatio;
/*     */   public int uniform_viewWidth;
/*     */   public int uniform_viewHeight;
/*     */   public int uniform_near;
/*     */   public int uniform_far;
/*     */   public int uniform_sunPosition;
/*     */   public int uniform_moonPosition;
/*     */   public int uniform_upPosition;
/*     */   public int uniform_previousCameraPosition;
/*     */   public int uniform_cameraPosition;
/*     */   public int uniform_gbufferModelView;
/*     */   public int uniform_gbufferModelViewInverse;
/*     */   public int uniform_gbufferPreviousProjection;
/*     */   public int uniform_gbufferProjection;
/*     */   public int uniform_gbufferProjectionInverse;
/*     */   public int uniform_gbufferPreviousModelView;
/*     */   public int uniform_shadowProjection;
/*     */   public int uniform_shadowProjectionInverse;
/*     */   public int uniform_shadowModelView;
/*     */   public int uniform_shadowModelViewInverse;
/*     */   public int uniform_wetness;
/*     */   public int uniform_eyeAltitude;
/*     */   public int uniform_eyeBrightness;
/*     */   public int uniform_eyeBrightnessSmooth;
/*     */   public int uniform_terrainTextureSize;
/*     */   public int uniform_terrainIconSize;
/*     */   public int uniform_isEyeInWater;
/*     */   public int uniform_hideGUI;
/*     */   public int uniform_centerDepthSmooth;
/*     */   public int uniform_atlasSize;
/*     */   
/*     */   public ShaderProgramData(int programID) {
/*  85 */     this.programIDGL = programID;
/*  86 */     this.uniform_texture = ARBShaderObjects.glGetUniformLocationARB(programID, "texture");
/*  87 */     this.uniform_lightmap = ARBShaderObjects.glGetUniformLocationARB(programID, "lightmap");
/*  88 */     this.uniform_normals = ARBShaderObjects.glGetUniformLocationARB(programID, "normals");
/*  89 */     this.uniform_specular = ARBShaderObjects.glGetUniformLocationARB(programID, "specular");
/*  90 */     this.uniform_shadow = ARBShaderObjects.glGetUniformLocationARB(programID, "shadow");
/*  91 */     this.uniform_watershadow = ARBShaderObjects.glGetUniformLocationARB(programID, "watershadow");
/*  92 */     this.uniform_shadowtex0 = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowtex0");
/*  93 */     this.uniform_shadowtex1 = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowtex1");
/*  94 */     this.uniform_depthtex0 = ARBShaderObjects.glGetUniformLocationARB(programID, "depthtex0");
/*  95 */     this.uniform_depthtex1 = ARBShaderObjects.glGetUniformLocationARB(programID, "depthtex1");
/*  96 */     this.uniform_shadowcolor = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowcolor");
/*  97 */     this.uniform_shadowcolor0 = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowcolor0");
/*  98 */     this.uniform_shadowcolor1 = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowcolor1");
/*  99 */     this.uniform_noisetex = ARBShaderObjects.glGetUniformLocationARB(programID, "noisetex");
/* 100 */     this.uniform_gcolor = ARBShaderObjects.glGetUniformLocationARB(programID, "gcolor");
/* 101 */     this.uniform_gdepth = ARBShaderObjects.glGetUniformLocationARB(programID, "gdepth");
/* 102 */     this.uniform_gnormal = ARBShaderObjects.glGetUniformLocationARB(programID, "gnormal");
/* 103 */     this.uniform_composite = ARBShaderObjects.glGetUniformLocationARB(programID, "composite");
/* 104 */     this.uniform_gaux1 = ARBShaderObjects.glGetUniformLocationARB(programID, "gaux1");
/* 105 */     this.uniform_gaux2 = ARBShaderObjects.glGetUniformLocationARB(programID, "gaux2");
/* 106 */     this.uniform_gaux3 = ARBShaderObjects.glGetUniformLocationARB(programID, "gaux3");
/* 107 */     this.uniform_gaux4 = ARBShaderObjects.glGetUniformLocationARB(programID, "gaux4");
/* 108 */     this.uniform_colortex0 = ARBShaderObjects.glGetUniformLocationARB(programID, "colortex0");
/* 109 */     this.uniform_colortex1 = ARBShaderObjects.glGetUniformLocationARB(programID, "colortex1");
/* 110 */     this.uniform_colortex2 = ARBShaderObjects.glGetUniformLocationARB(programID, "colortex2");
/* 111 */     this.uniform_colortex3 = ARBShaderObjects.glGetUniformLocationARB(programID, "colortex3");
/* 112 */     this.uniform_colortex4 = ARBShaderObjects.glGetUniformLocationARB(programID, "colortex4");
/* 113 */     this.uniform_colortex5 = ARBShaderObjects.glGetUniformLocationARB(programID, "colortex5");
/* 114 */     this.uniform_colortex6 = ARBShaderObjects.glGetUniformLocationARB(programID, "colortex6");
/* 115 */     this.uniform_colortex7 = ARBShaderObjects.glGetUniformLocationARB(programID, "colortex7");
/* 116 */     this.uniform_gdepthtex = ARBShaderObjects.glGetUniformLocationARB(programID, "gdepthtex");
/* 117 */     this.uniform_depthtex2 = ARBShaderObjects.glGetUniformLocationARB(programID, "depthtex2");
/* 118 */     this.uniform_tex = ARBShaderObjects.glGetUniformLocationARB(programID, "tex");
/* 119 */     this.uniform_heldItemId = ARBShaderObjects.glGetUniformLocationARB(programID, "heldItemId");
/* 120 */     this.uniform_heldBlockLightValue = ARBShaderObjects.glGetUniformLocationARB(programID, "heldBlockLightValue");
/* 121 */     this.uniform_fogMode = ARBShaderObjects.glGetUniformLocationARB(programID, "fogMode");
/* 122 */     this.uniform_fogColor = ARBShaderObjects.glGetUniformLocationARB(programID, "fogColor");
/* 123 */     this.uniform_skyColor = ARBShaderObjects.glGetUniformLocationARB(programID, "skyColor");
/* 124 */     this.uniform_worldTime = ARBShaderObjects.glGetUniformLocationARB(programID, "worldTime");
/* 125 */     this.uniform_moonPhase = ARBShaderObjects.glGetUniformLocationARB(programID, "moonPhase");
/* 126 */     this.uniform_frameTimeCounter = ARBShaderObjects.glGetUniformLocationARB(programID, "frameTimeCounter");
/* 127 */     this.uniform_sunAngle = ARBShaderObjects.glGetUniformLocationARB(programID, "sunAngle");
/* 128 */     this.uniform_shadowAngle = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowAngle");
/* 129 */     this.uniform_rainStrength = ARBShaderObjects.glGetUniformLocationARB(programID, "rainStrength");
/* 130 */     this.uniform_aspectRatio = ARBShaderObjects.glGetUniformLocationARB(programID, "aspectRatio");
/* 131 */     this.uniform_viewWidth = ARBShaderObjects.glGetUniformLocationARB(programID, "viewWidth");
/* 132 */     this.uniform_viewHeight = ARBShaderObjects.glGetUniformLocationARB(programID, "viewHeight");
/* 133 */     this.uniform_near = ARBShaderObjects.glGetUniformLocationARB(programID, "near");
/* 134 */     this.uniform_far = ARBShaderObjects.glGetUniformLocationARB(programID, "far");
/* 135 */     this.uniform_sunPosition = ARBShaderObjects.glGetUniformLocationARB(programID, "sunPosition");
/* 136 */     this.uniform_moonPosition = ARBShaderObjects.glGetUniformLocationARB(programID, "moonPosition");
/* 137 */     this.uniform_upPosition = ARBShaderObjects.glGetUniformLocationARB(programID, "upPosition");
/* 138 */     this.uniform_previousCameraPosition = ARBShaderObjects.glGetUniformLocationARB(programID, "previousCameraPosition");
/* 139 */     this.uniform_cameraPosition = ARBShaderObjects.glGetUniformLocationARB(programID, "cameraPosition");
/* 140 */     this.uniform_gbufferModelView = ARBShaderObjects.glGetUniformLocationARB(programID, "gbufferModelView");
/* 141 */     this.uniform_gbufferModelViewInverse = ARBShaderObjects.glGetUniformLocationARB(programID, "gbufferModelViewInverse");
/* 142 */     this.uniform_gbufferPreviousProjection = ARBShaderObjects.glGetUniformLocationARB(programID, "gbufferPreviousProjection");
/* 143 */     this.uniform_gbufferProjection = ARBShaderObjects.glGetUniformLocationARB(programID, "gbufferProjection");
/* 144 */     this.uniform_gbufferProjectionInverse = ARBShaderObjects.glGetUniformLocationARB(programID, "gbufferProjectionInverse");
/* 145 */     this.uniform_gbufferPreviousModelView = ARBShaderObjects.glGetUniformLocationARB(programID, "gbufferPreviousModelView");
/* 146 */     this.uniform_shadowProjection = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowProjection");
/* 147 */     this.uniform_shadowProjectionInverse = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowProjectionInverse");
/* 148 */     this.uniform_shadowModelView = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowModelView");
/* 149 */     this.uniform_shadowModelViewInverse = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowModelViewInverse");
/* 150 */     this.uniform_wetness = ARBShaderObjects.glGetUniformLocationARB(programID, "wetness");
/* 151 */     this.uniform_eyeAltitude = ARBShaderObjects.glGetUniformLocationARB(programID, "eyeAltitude");
/* 152 */     this.uniform_eyeBrightness = ARBShaderObjects.glGetUniformLocationARB(programID, "eyeBrightness");
/* 153 */     this.uniform_eyeBrightnessSmooth = ARBShaderObjects.glGetUniformLocationARB(programID, "eyeBrightnessSmooth");
/* 154 */     this.uniform_terrainTextureSize = ARBShaderObjects.glGetUniformLocationARB(programID, "terrainTextureSize");
/* 155 */     this.uniform_terrainIconSize = ARBShaderObjects.glGetUniformLocationARB(programID, "terrainIconSize");
/* 156 */     this.uniform_isEyeInWater = ARBShaderObjects.glGetUniformLocationARB(programID, "isEyeInWater");
/* 157 */     this.uniform_hideGUI = ARBShaderObjects.glGetUniformLocationARB(programID, "hideGUI");
/* 158 */     this.uniform_centerDepthSmooth = ARBShaderObjects.glGetUniformLocationARB(programID, "centerDepthSmooth");
/* 159 */     this.uniform_atlasSize = ARBShaderObjects.glGetUniformLocationARB(programID, "atlasSize");
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\shaders\ShaderProgramData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */