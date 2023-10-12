/*     */ package net.minecraft.client.shader;
/*     */ 
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.texture.ITextureObject;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.client.util.JsonBlendingMode;
/*     */ import net.minecraft.client.util.JsonException;
/*     */ import net.minecraft.util.JsonUtils;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ShaderManager
/*     */ {
/*  28 */   private static final Logger logger = LogManager.getLogger();
/*  29 */   private static final ShaderDefault defaultShaderUniform = new ShaderDefault();
/*  30 */   private static ShaderManager staticShaderManager = null;
/*  31 */   private static int currentProgram = -1;
/*     */   private static boolean field_148000_e = true;
/*  33 */   private final Map<String, Object> shaderSamplers = Maps.newHashMap();
/*  34 */   private final List<String> samplerNames = Lists.newArrayList();
/*  35 */   private final List<Integer> shaderSamplerLocations = Lists.newArrayList();
/*  36 */   private final List<ShaderUniform> shaderUniforms = Lists.newArrayList();
/*  37 */   private final List<Integer> shaderUniformLocations = Lists.newArrayList();
/*  38 */   private final Map<String, ShaderUniform> mappedShaderUniforms = Maps.newHashMap();
/*     */   
/*     */   private final int program;
/*     */   private final String programFilename;
/*     */   private final boolean useFaceCulling;
/*     */   private boolean isDirty;
/*     */   private final JsonBlendingMode field_148016_p;
/*     */   private final List<Integer> attribLocations;
/*     */   private final List<String> attributes;
/*     */   private final ShaderLoader vertexShaderLoader;
/*     */   private final ShaderLoader fragmentShaderLoader;
/*     */   
/*     */   public ShaderManager(IResourceManager resourceManager, String programName) throws JsonException, IOException {
/*  51 */     JsonParser jsonparser = new JsonParser();
/*  52 */     ResourceLocation resourcelocation = new ResourceLocation("shaders/program/" + programName + ".json");
/*  53 */     this.programFilename = programName;
/*  54 */     InputStream inputstream = null;
/*     */ 
/*     */     
/*     */     try {
/*  58 */       inputstream = resourceManager.getResource(resourcelocation).getInputStream();
/*  59 */       JsonObject jsonobject = jsonparser.parse(IOUtils.toString(inputstream, Charsets.UTF_8)).getAsJsonObject();
/*  60 */       String s = JsonUtils.getString(jsonobject, "vertex");
/*  61 */       String s1 = JsonUtils.getString(jsonobject, "fragment");
/*  62 */       JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "samplers", (JsonArray)null);
/*     */       
/*  64 */       if (jsonarray != null) {
/*     */         
/*  66 */         int i = 0;
/*     */         
/*  68 */         for (JsonElement jsonelement : jsonarray) {
/*     */ 
/*     */           
/*     */           try {
/*  72 */             parseSampler(jsonelement);
/*     */           }
/*  74 */           catch (Exception exception2) {
/*     */             
/*  76 */             JsonException jsonexception1 = JsonException.func_151379_a(exception2);
/*  77 */             jsonexception1.func_151380_a("samplers[" + i + "]");
/*  78 */             throw jsonexception1;
/*     */           } 
/*     */           
/*  81 */           i++;
/*     */         } 
/*     */       } 
/*     */       
/*  85 */       JsonArray jsonarray1 = JsonUtils.getJsonArray(jsonobject, "attributes", (JsonArray)null);
/*     */       
/*  87 */       if (jsonarray1 != null) {
/*     */         
/*  89 */         int j = 0;
/*  90 */         this.attribLocations = Lists.newArrayListWithCapacity(jsonarray1.size());
/*  91 */         this.attributes = Lists.newArrayListWithCapacity(jsonarray1.size());
/*     */         
/*  93 */         for (JsonElement jsonelement1 : jsonarray1)
/*     */         {
/*     */           
/*     */           try {
/*  97 */             this.attributes.add(JsonUtils.getString(jsonelement1, "attribute"));
/*     */           }
/*  99 */           catch (Exception exception1) {
/*     */             
/* 101 */             JsonException jsonexception2 = JsonException.func_151379_a(exception1);
/* 102 */             jsonexception2.func_151380_a("attributes[" + j + "]");
/* 103 */             throw jsonexception2;
/*     */           } 
/*     */           
/* 106 */           j++;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 111 */         this.attribLocations = null;
/* 112 */         this.attributes = null;
/*     */       } 
/*     */       
/* 115 */       JsonArray jsonarray2 = JsonUtils.getJsonArray(jsonobject, "uniforms", (JsonArray)null);
/*     */       
/* 117 */       if (jsonarray2 != null) {
/*     */         
/* 119 */         int k = 0;
/*     */         
/* 121 */         for (JsonElement jsonelement2 : jsonarray2) {
/*     */ 
/*     */           
/*     */           try {
/* 125 */             parseUniform(jsonelement2);
/*     */           }
/* 127 */           catch (Exception exception) {
/*     */             
/* 129 */             JsonException jsonexception3 = JsonException.func_151379_a(exception);
/* 130 */             jsonexception3.func_151380_a("uniforms[" + k + "]");
/* 131 */             throw jsonexception3;
/*     */           } 
/*     */           
/* 134 */           k++;
/*     */         } 
/*     */       } 
/*     */       
/* 138 */       this.field_148016_p = JsonBlendingMode.func_148110_a(JsonUtils.getJsonObject(jsonobject, "blend", (JsonObject)null));
/* 139 */       this.useFaceCulling = JsonUtils.getBoolean(jsonobject, "cull", true);
/* 140 */       this.vertexShaderLoader = ShaderLoader.loadShader(resourceManager, ShaderLoader.ShaderType.VERTEX, s);
/* 141 */       this.fragmentShaderLoader = ShaderLoader.loadShader(resourceManager, ShaderLoader.ShaderType.FRAGMENT, s1);
/* 142 */       this.program = ShaderLinkHelper.getStaticShaderLinkHelper().createProgram();
/* 143 */       ShaderLinkHelper.getStaticShaderLinkHelper().linkProgram(this);
/* 144 */       setupUniforms();
/*     */       
/* 146 */       if (this.attributes != null)
/*     */       {
/* 148 */         for (String s2 : this.attributes)
/*     */         {
/* 150 */           int l = OpenGlHelper.glGetAttribLocation(this.program, s2);
/* 151 */           this.attribLocations.add(Integer.valueOf(l));
/*     */         }
/*     */       
/*     */       }
/* 155 */     } catch (Exception exception3) {
/*     */       
/* 157 */       JsonException jsonexception = JsonException.func_151379_a(exception3);
/* 158 */       jsonexception.func_151381_b(resourcelocation.getResourcePath());
/* 159 */       throw jsonexception;
/*     */     }
/*     */     finally {
/*     */       
/* 163 */       IOUtils.closeQuietly(inputstream);
/*     */     } 
/*     */     
/* 166 */     markDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteShader() {
/* 171 */     ShaderLinkHelper.getStaticShaderLinkHelper().deleteShader(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void endShader() {
/* 176 */     OpenGlHelper.glUseProgram(0);
/* 177 */     currentProgram = -1;
/* 178 */     staticShaderManager = null;
/* 179 */     field_148000_e = true;
/*     */     
/* 181 */     for (int i = 0; i < this.shaderSamplerLocations.size(); i++) {
/*     */       
/* 183 */       if (this.shaderSamplers.get(this.samplerNames.get(i)) != null) {
/*     */         
/* 185 */         GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + i);
/* 186 */         GlStateManager.bindTexture(0);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void useShader() {
/* 193 */     this.isDirty = false;
/* 194 */     staticShaderManager = this;
/* 195 */     this.field_148016_p.func_148109_a();
/*     */     
/* 197 */     if (this.program != currentProgram) {
/*     */       
/* 199 */       OpenGlHelper.glUseProgram(this.program);
/* 200 */       currentProgram = this.program;
/*     */     } 
/*     */     
/* 203 */     if (this.useFaceCulling) {
/*     */       
/* 205 */       GlStateManager.enableCull();
/*     */     }
/*     */     else {
/*     */       
/* 209 */       GlStateManager.disableCull();
/*     */     } 
/*     */     
/* 212 */     for (int i = 0; i < this.shaderSamplerLocations.size(); i++) {
/*     */       
/* 214 */       if (this.shaderSamplers.get(this.samplerNames.get(i)) != null) {
/*     */         
/* 216 */         GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + i);
/* 217 */         GlStateManager.enableTexture2D();
/* 218 */         Object object = this.shaderSamplers.get(this.samplerNames.get(i));
/* 219 */         int j = -1;
/*     */         
/* 221 */         if (object instanceof Framebuffer) {
/*     */           
/* 223 */           j = ((Framebuffer)object).framebufferTexture;
/*     */         }
/* 225 */         else if (object instanceof ITextureObject) {
/*     */           
/* 227 */           j = ((ITextureObject)object).getGlTextureId();
/*     */         }
/* 229 */         else if (object instanceof Integer) {
/*     */           
/* 231 */           j = ((Integer)object).intValue();
/*     */         } 
/*     */         
/* 234 */         if (j != -1) {
/*     */           
/* 236 */           GlStateManager.bindTexture(j);
/* 237 */           OpenGlHelper.glUniform1i(OpenGlHelper.glGetUniformLocation(this.program, this.samplerNames.get(i)), i);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 242 */     for (ShaderUniform shaderuniform : this.shaderUniforms)
/*     */     {
/* 244 */       shaderuniform.upload();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void markDirty() {
/* 250 */     this.isDirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ShaderUniform getShaderUniform(String p_147991_1_) {
/* 255 */     return this.mappedShaderUniforms.containsKey(p_147991_1_) ? this.mappedShaderUniforms.get(p_147991_1_) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ShaderUniform getShaderUniformOrDefault(String p_147984_1_) {
/* 260 */     return this.mappedShaderUniforms.containsKey(p_147984_1_) ? this.mappedShaderUniforms.get(p_147984_1_) : defaultShaderUniform;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupUniforms() {
/* 265 */     int i = 0;
/*     */     
/* 267 */     for (int j = 0; i < this.samplerNames.size(); j++) {
/*     */       
/* 269 */       String s = this.samplerNames.get(i);
/* 270 */       int k = OpenGlHelper.glGetUniformLocation(this.program, s);
/*     */       
/* 272 */       if (k == -1) {
/*     */         
/* 274 */         logger.warn("Shader " + this.programFilename + "could not find sampler named " + s + " in the specified shader program.");
/* 275 */         this.shaderSamplers.remove(s);
/* 276 */         this.samplerNames.remove(j);
/* 277 */         j--;
/*     */       }
/*     */       else {
/*     */         
/* 281 */         this.shaderSamplerLocations.add(Integer.valueOf(k));
/*     */       } 
/*     */       
/* 284 */       i++;
/*     */     } 
/*     */     
/* 287 */     for (ShaderUniform shaderuniform : this.shaderUniforms) {
/*     */       
/* 289 */       String s1 = shaderuniform.getShaderName();
/* 290 */       int l = OpenGlHelper.glGetUniformLocation(this.program, s1);
/*     */       
/* 292 */       if (l == -1) {
/*     */         
/* 294 */         logger.warn("Could not find uniform named " + s1 + " in the specified shader program.");
/*     */         
/*     */         continue;
/*     */       } 
/* 298 */       this.shaderUniformLocations.add(Integer.valueOf(l));
/* 299 */       shaderuniform.setUniformLocation(l);
/* 300 */       this.mappedShaderUniforms.put(s1, shaderuniform);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseSampler(JsonElement p_147996_1_) throws JsonException {
/* 307 */     JsonObject jsonobject = JsonUtils.getJsonObject(p_147996_1_, "sampler");
/* 308 */     String s = JsonUtils.getString(jsonobject, "name");
/*     */     
/* 310 */     if (!JsonUtils.isString(jsonobject, "file")) {
/*     */       
/* 312 */       this.shaderSamplers.put(s, null);
/* 313 */       this.samplerNames.add(s);
/*     */     }
/*     */     else {
/*     */       
/* 317 */       this.samplerNames.add(s);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addSamplerTexture(String p_147992_1_, Object p_147992_2_) {
/* 323 */     if (this.shaderSamplers.containsKey(p_147992_1_))
/*     */     {
/* 325 */       this.shaderSamplers.remove(p_147992_1_);
/*     */     }
/*     */     
/* 328 */     this.shaderSamplers.put(p_147992_1_, p_147992_2_);
/* 329 */     markDirty();
/*     */   }
/*     */ 
/*     */   
/*     */   private void parseUniform(JsonElement p_147987_1_) throws JsonException {
/* 334 */     JsonObject jsonobject = JsonUtils.getJsonObject(p_147987_1_, "uniform");
/* 335 */     String s = JsonUtils.getString(jsonobject, "name");
/* 336 */     int i = ShaderUniform.parseType(JsonUtils.getString(jsonobject, "type"));
/* 337 */     int j = JsonUtils.getInt(jsonobject, "count");
/* 338 */     float[] afloat = new float[Math.max(j, 16)];
/* 339 */     JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "values");
/*     */     
/* 341 */     if (jsonarray.size() != j && jsonarray.size() > 1)
/*     */     {
/* 343 */       throw new JsonException("Invalid amount of values specified (expected " + j + ", found " + jsonarray.size() + ")");
/*     */     }
/*     */ 
/*     */     
/* 347 */     int k = 0;
/*     */     
/* 349 */     for (JsonElement jsonelement : jsonarray) {
/*     */ 
/*     */       
/*     */       try {
/* 353 */         afloat[k] = JsonUtils.getFloat(jsonelement, "value");
/*     */       }
/* 355 */       catch (Exception exception) {
/*     */         
/* 357 */         JsonException jsonexception = JsonException.func_151379_a(exception);
/* 358 */         jsonexception.func_151380_a("values[" + k + "]");
/* 359 */         throw jsonexception;
/*     */       } 
/*     */       
/* 362 */       k++;
/*     */     } 
/*     */     
/* 365 */     if (j > 1 && jsonarray.size() == 1)
/*     */     {
/* 367 */       while (k < j) {
/*     */         
/* 369 */         afloat[k] = afloat[0];
/* 370 */         k++;
/*     */       } 
/*     */     }
/*     */     
/* 374 */     int l = (j > 1 && j <= 4 && i < 8) ? (j - 1) : 0;
/* 375 */     ShaderUniform shaderuniform = new ShaderUniform(s, i + l, j, this);
/*     */     
/* 377 */     if (i <= 3) {
/*     */       
/* 379 */       shaderuniform.set((int)afloat[0], (int)afloat[1], (int)afloat[2], (int)afloat[3]);
/*     */     }
/* 381 */     else if (i <= 7) {
/*     */       
/* 383 */       shaderuniform.func_148092_b(afloat[0], afloat[1], afloat[2], afloat[3]);
/*     */     }
/*     */     else {
/*     */       
/* 387 */       shaderuniform.set(afloat);
/*     */     } 
/*     */     
/* 390 */     this.shaderUniforms.add(shaderuniform);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ShaderLoader getVertexShaderLoader() {
/* 396 */     return this.vertexShaderLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public ShaderLoader getFragmentShaderLoader() {
/* 401 */     return this.fragmentShaderLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getProgram() {
/* 406 */     return this.program;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\shader\ShaderManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */