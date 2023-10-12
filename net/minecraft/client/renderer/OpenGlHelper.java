/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.src.Config;
/*     */ import org.lwjgl.opengl.ARBCopyBuffer;
/*     */ import org.lwjgl.opengl.ARBFramebufferObject;
/*     */ import org.lwjgl.opengl.ARBMultitexture;
/*     */ import org.lwjgl.opengl.ARBShaderObjects;
/*     */ import org.lwjgl.opengl.ARBVertexBufferObject;
/*     */ import org.lwjgl.opengl.ARBVertexShader;
/*     */ import org.lwjgl.opengl.ContextCapabilities;
/*     */ import org.lwjgl.opengl.EXTBlendFuncSeparate;
/*     */ import org.lwjgl.opengl.EXTFramebufferObject;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL13;
/*     */ import org.lwjgl.opengl.GL14;
/*     */ import org.lwjgl.opengl.GL15;
/*     */ import org.lwjgl.opengl.GL20;
/*     */ import org.lwjgl.opengl.GL30;
/*     */ import org.lwjgl.opengl.GL31;
/*     */ import org.lwjgl.opengl.GLContext;
/*     */ import oshi.SystemInfo;
/*     */ import oshi.hardware.Processor;
/*     */ 
/*     */ public class OpenGlHelper
/*     */ {
/*     */   public static boolean nvidia;
/*     */   public static boolean ati;
/*     */   public static int GL_FRAMEBUFFER;
/*     */   public static int GL_RENDERBUFFER;
/*     */   public static int GL_COLOR_ATTACHMENT0;
/*     */   public static int GL_DEPTH_ATTACHMENT;
/*     */   public static int GL_FRAMEBUFFER_COMPLETE;
/*     */   public static int GL_FB_INCOMPLETE_ATTACHMENT;
/*     */   public static int GL_FB_INCOMPLETE_MISS_ATTACH;
/*     */   public static int GL_FB_INCOMPLETE_DRAW_BUFFER;
/*     */   public static int GL_FB_INCOMPLETE_READ_BUFFER;
/*     */   private static int framebufferType;
/*     */   public static boolean framebufferSupported;
/*     */   private static boolean shadersAvailable;
/*     */   private static boolean arbShaders;
/*     */   public static int GL_LINK_STATUS;
/*     */   public static int GL_COMPILE_STATUS;
/*     */   public static int GL_VERTEX_SHADER;
/*     */   public static int GL_FRAGMENT_SHADER;
/*     */   private static boolean arbMultitexture;
/*     */   public static int defaultTexUnit;
/*     */   public static int lightmapTexUnit;
/*     */   public static int GL_TEXTURE2;
/*     */   private static boolean arbTextureEnvCombine;
/*     */   public static int GL_COMBINE;
/*     */   public static int GL_INTERPOLATE;
/*     */   public static int GL_PRIMARY_COLOR;
/*     */   public static int GL_CONSTANT;
/*     */   public static int GL_PREVIOUS;
/*     */   public static int GL_COMBINE_RGB;
/*     */   public static int GL_SOURCE0_RGB;
/*     */   public static int GL_SOURCE1_RGB;
/*     */   public static int GL_SOURCE2_RGB;
/*     */   public static int GL_OPERAND0_RGB;
/*     */   public static int GL_OPERAND1_RGB;
/*     */   public static int GL_OPERAND2_RGB;
/*     */   public static int GL_COMBINE_ALPHA;
/*     */   public static int GL_SOURCE0_ALPHA;
/*     */   public static int GL_SOURCE1_ALPHA;
/*     */   public static int GL_SOURCE2_ALPHA;
/*     */   public static int GL_OPERAND0_ALPHA;
/*     */   public static int GL_OPERAND1_ALPHA;
/*     */   public static int GL_OPERAND2_ALPHA;
/*     */   private static boolean openGL14;
/*     */   public static boolean extBlendFuncSeparate;
/*     */   public static boolean openGL21;
/*     */   public static boolean shadersSupported;
/*  80 */   private static String logText = "";
/*     */   private static String cpu;
/*     */   public static boolean vboSupported;
/*     */   public static boolean vboSupportedAti;
/*     */   private static boolean arbVbo;
/*     */   public static int GL_ARRAY_BUFFER;
/*     */   public static int GL_STATIC_DRAW;
/*  87 */   public static float lastBrightnessX = 0.0F;
/*  88 */   public static float lastBrightnessY = 0.0F;
/*     */   
/*     */   public static boolean openGL31;
/*     */   public static boolean vboRegions;
/*     */   public static int GL_COPY_READ_BUFFER;
/*     */   public static int GL_COPY_WRITE_BUFFER;
/*     */   public static final int GL_QUADS = 7;
/*     */   public static final int GL_TRIANGLES = 4;
/*     */   
/*     */   public static void initializeTextures() {
/*  98 */     Config.initDisplay();
/*  99 */     ContextCapabilities contextcapabilities = GLContext.getCapabilities();
/* 100 */     arbMultitexture = (contextcapabilities.GL_ARB_multitexture && !contextcapabilities.OpenGL13);
/* 101 */     arbTextureEnvCombine = (contextcapabilities.GL_ARB_texture_env_combine && !contextcapabilities.OpenGL13);
/* 102 */     openGL31 = contextcapabilities.OpenGL31;
/*     */     
/* 104 */     if (openGL31) {
/*     */       
/* 106 */       GL_COPY_READ_BUFFER = 36662;
/* 107 */       GL_COPY_WRITE_BUFFER = 36663;
/*     */     }
/*     */     else {
/*     */       
/* 111 */       GL_COPY_READ_BUFFER = 36662;
/* 112 */       GL_COPY_WRITE_BUFFER = 36663;
/*     */     } 
/*     */     
/* 115 */     boolean flag = (openGL31 || contextcapabilities.GL_ARB_copy_buffer);
/* 116 */     boolean flag1 = contextcapabilities.OpenGL14;
/* 117 */     vboRegions = (flag && flag1);
/*     */     
/* 119 */     if (!vboRegions) {
/*     */       
/* 121 */       List<String> list = new ArrayList<>();
/*     */       
/* 123 */       if (!flag)
/*     */       {
/* 125 */         list.add("OpenGL 1.3, ARB_copy_buffer");
/*     */       }
/*     */       
/* 128 */       if (!flag1)
/*     */       {
/* 130 */         list.add("OpenGL 1.4");
/*     */       }
/*     */       
/* 133 */       String s = "VboRegions not supported, missing: " + Config.listToString(list);
/* 134 */       Config.dbg(s);
/* 135 */       logText += s + "\n";
/*     */     } 
/*     */     
/* 138 */     if (arbMultitexture) {
/*     */       
/* 140 */       logText += "Using ARB_multitexture.\n";
/* 141 */       defaultTexUnit = 33984;
/* 142 */       lightmapTexUnit = 33985;
/* 143 */       GL_TEXTURE2 = 33986;
/*     */     }
/*     */     else {
/*     */       
/* 147 */       logText += "Using GL 1.3 multitexturing.\n";
/* 148 */       defaultTexUnit = 33984;
/* 149 */       lightmapTexUnit = 33985;
/* 150 */       GL_TEXTURE2 = 33986;
/*     */     } 
/*     */     
/* 153 */     if (arbTextureEnvCombine) {
/*     */       
/* 155 */       logText += "Using ARB_texture_env_combine.\n";
/* 156 */       GL_COMBINE = 34160;
/* 157 */       GL_INTERPOLATE = 34165;
/* 158 */       GL_PRIMARY_COLOR = 34167;
/* 159 */       GL_CONSTANT = 34166;
/* 160 */       GL_PREVIOUS = 34168;
/* 161 */       GL_COMBINE_RGB = 34161;
/* 162 */       GL_SOURCE0_RGB = 34176;
/* 163 */       GL_SOURCE1_RGB = 34177;
/* 164 */       GL_SOURCE2_RGB = 34178;
/* 165 */       GL_OPERAND0_RGB = 34192;
/* 166 */       GL_OPERAND1_RGB = 34193;
/* 167 */       GL_OPERAND2_RGB = 34194;
/* 168 */       GL_COMBINE_ALPHA = 34162;
/* 169 */       GL_SOURCE0_ALPHA = 34184;
/* 170 */       GL_SOURCE1_ALPHA = 34185;
/* 171 */       GL_SOURCE2_ALPHA = 34186;
/* 172 */       GL_OPERAND0_ALPHA = 34200;
/* 173 */       GL_OPERAND1_ALPHA = 34201;
/* 174 */       GL_OPERAND2_ALPHA = 34202;
/*     */     }
/*     */     else {
/*     */       
/* 178 */       logText += "Using GL 1.3 texture combiners.\n";
/* 179 */       GL_COMBINE = 34160;
/* 180 */       GL_INTERPOLATE = 34165;
/* 181 */       GL_PRIMARY_COLOR = 34167;
/* 182 */       GL_CONSTANT = 34166;
/* 183 */       GL_PREVIOUS = 34168;
/* 184 */       GL_COMBINE_RGB = 34161;
/* 185 */       GL_SOURCE0_RGB = 34176;
/* 186 */       GL_SOURCE1_RGB = 34177;
/* 187 */       GL_SOURCE2_RGB = 34178;
/* 188 */       GL_OPERAND0_RGB = 34192;
/* 189 */       GL_OPERAND1_RGB = 34193;
/* 190 */       GL_OPERAND2_RGB = 34194;
/* 191 */       GL_COMBINE_ALPHA = 34162;
/* 192 */       GL_SOURCE0_ALPHA = 34184;
/* 193 */       GL_SOURCE1_ALPHA = 34185;
/* 194 */       GL_SOURCE2_ALPHA = 34186;
/* 195 */       GL_OPERAND0_ALPHA = 34200;
/* 196 */       GL_OPERAND1_ALPHA = 34201;
/* 197 */       GL_OPERAND2_ALPHA = 34202;
/*     */     } 
/*     */     
/* 200 */     extBlendFuncSeparate = (contextcapabilities.GL_EXT_blend_func_separate && !contextcapabilities.OpenGL14);
/* 201 */     openGL14 = (contextcapabilities.OpenGL14 || contextcapabilities.GL_EXT_blend_func_separate);
/* 202 */     framebufferSupported = (openGL14 && (contextcapabilities.GL_ARB_framebuffer_object || contextcapabilities.GL_EXT_framebuffer_object || contextcapabilities.OpenGL30));
/*     */     
/* 204 */     if (framebufferSupported) {
/*     */       
/* 206 */       logText += "Using framebuffer objects because ";
/*     */       
/* 208 */       if (contextcapabilities.OpenGL30)
/*     */       {
/* 210 */         logText += "OpenGL 3.0 is supported and separate blending is supported.\n";
/* 211 */         framebufferType = 0;
/* 212 */         GL_FRAMEBUFFER = 36160;
/* 213 */         GL_RENDERBUFFER = 36161;
/* 214 */         GL_COLOR_ATTACHMENT0 = 36064;
/* 215 */         GL_DEPTH_ATTACHMENT = 36096;
/* 216 */         GL_FRAMEBUFFER_COMPLETE = 36053;
/* 217 */         GL_FB_INCOMPLETE_ATTACHMENT = 36054;
/* 218 */         GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
/* 219 */         GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
/* 220 */         GL_FB_INCOMPLETE_READ_BUFFER = 36060;
/*     */       }
/* 222 */       else if (contextcapabilities.GL_ARB_framebuffer_object)
/*     */       {
/* 224 */         logText += "ARB_framebuffer_object is supported and separate blending is supported.\n";
/* 225 */         framebufferType = 1;
/* 226 */         GL_FRAMEBUFFER = 36160;
/* 227 */         GL_RENDERBUFFER = 36161;
/* 228 */         GL_COLOR_ATTACHMENT0 = 36064;
/* 229 */         GL_DEPTH_ATTACHMENT = 36096;
/* 230 */         GL_FRAMEBUFFER_COMPLETE = 36053;
/* 231 */         GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
/* 232 */         GL_FB_INCOMPLETE_ATTACHMENT = 36054;
/* 233 */         GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
/* 234 */         GL_FB_INCOMPLETE_READ_BUFFER = 36060;
/*     */       }
/* 236 */       else if (contextcapabilities.GL_EXT_framebuffer_object)
/*     */       {
/* 238 */         logText += "EXT_framebuffer_object is supported.\n";
/* 239 */         framebufferType = 2;
/* 240 */         GL_FRAMEBUFFER = 36160;
/* 241 */         GL_RENDERBUFFER = 36161;
/* 242 */         GL_COLOR_ATTACHMENT0 = 36064;
/* 243 */         GL_DEPTH_ATTACHMENT = 36096;
/* 244 */         GL_FRAMEBUFFER_COMPLETE = 36053;
/* 245 */         GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
/* 246 */         GL_FB_INCOMPLETE_ATTACHMENT = 36054;
/* 247 */         GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
/* 248 */         GL_FB_INCOMPLETE_READ_BUFFER = 36060;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 253 */       logText += "Not using framebuffer objects because ";
/* 254 */       logText += "OpenGL 1.4 is " + (contextcapabilities.OpenGL14 ? "" : "not ") + "supported, ";
/* 255 */       logText += "EXT_blend_func_separate is " + (contextcapabilities.GL_EXT_blend_func_separate ? "" : "not ") + "supported, ";
/* 256 */       logText += "OpenGL 3.0 is " + (contextcapabilities.OpenGL30 ? "" : "not ") + "supported, ";
/* 257 */       logText += "ARB_framebuffer_object is " + (contextcapabilities.GL_ARB_framebuffer_object ? "" : "not ") + "supported, and ";
/* 258 */       logText += "EXT_framebuffer_object is " + (contextcapabilities.GL_EXT_framebuffer_object ? "" : "not ") + "supported.\n";
/*     */     } 
/*     */     
/* 261 */     openGL21 = contextcapabilities.OpenGL21;
/* 262 */     shadersAvailable = (openGL21 || (contextcapabilities.GL_ARB_vertex_shader && contextcapabilities.GL_ARB_fragment_shader && contextcapabilities.GL_ARB_shader_objects));
/* 263 */     logText += "Shaders are " + (shadersAvailable ? "" : "not ") + "available because ";
/*     */     
/* 265 */     if (shadersAvailable) {
/*     */       
/* 267 */       if (contextcapabilities.OpenGL21)
/*     */       {
/* 269 */         logText += "OpenGL 2.1 is supported.\n";
/* 270 */         arbShaders = false;
/* 271 */         GL_LINK_STATUS = 35714;
/* 272 */         GL_COMPILE_STATUS = 35713;
/* 273 */         GL_VERTEX_SHADER = 35633;
/* 274 */         GL_FRAGMENT_SHADER = 35632;
/*     */       }
/*     */       else
/*     */       {
/* 278 */         logText += "ARB_shader_objects, ARB_vertex_shader, and ARB_fragment_shader are supported.\n";
/* 279 */         arbShaders = true;
/* 280 */         GL_LINK_STATUS = 35714;
/* 281 */         GL_COMPILE_STATUS = 35713;
/* 282 */         GL_VERTEX_SHADER = 35633;
/* 283 */         GL_FRAGMENT_SHADER = 35632;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 288 */       logText += "OpenGL 2.1 is " + (contextcapabilities.OpenGL21 ? "" : "not ") + "supported, ";
/* 289 */       logText += "ARB_shader_objects is " + (contextcapabilities.GL_ARB_shader_objects ? "" : "not ") + "supported, ";
/* 290 */       logText += "ARB_vertex_shader is " + (contextcapabilities.GL_ARB_vertex_shader ? "" : "not ") + "supported, and ";
/* 291 */       logText += "ARB_fragment_shader is " + (contextcapabilities.GL_ARB_fragment_shader ? "" : "not ") + "supported.\n";
/*     */     } 
/*     */     
/* 294 */     shadersSupported = (framebufferSupported && shadersAvailable);
/* 295 */     String s1 = GL11.glGetString(7936).toLowerCase();
/* 296 */     nvidia = s1.contains("nvidia");
/* 297 */     arbVbo = (!contextcapabilities.OpenGL15 && contextcapabilities.GL_ARB_vertex_buffer_object);
/* 298 */     vboSupported = (contextcapabilities.OpenGL15 || arbVbo);
/* 299 */     logText += "VBOs are " + (vboSupported ? "" : "not ") + "available because ";
/*     */     
/* 301 */     if (vboSupported)
/*     */     {
/* 303 */       if (arbVbo) {
/*     */         
/* 305 */         logText += "ARB_vertex_buffer_object is supported.\n";
/* 306 */         GL_STATIC_DRAW = 35044;
/* 307 */         GL_ARRAY_BUFFER = 34962;
/*     */       }
/*     */       else {
/*     */         
/* 311 */         logText += "OpenGL 1.5 is supported.\n";
/* 312 */         GL_STATIC_DRAW = 35044;
/* 313 */         GL_ARRAY_BUFFER = 34962;
/*     */       } 
/*     */     }
/*     */     
/* 317 */     ati = s1.contains("ati");
/*     */     
/* 319 */     if (ati)
/*     */     {
/* 321 */       if (vboSupported) {
/*     */         
/* 323 */         vboSupportedAti = true;
/*     */       }
/*     */       else {
/*     */         
/* 327 */         GameSettings.Options.RENDER_DISTANCE.setValueMax(16.0F);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 333 */       Processor[] aprocessor = (new SystemInfo()).getHardware().getProcessors();
/* 334 */       cpu = String.format("%dx %s", new Object[] { Integer.valueOf(aprocessor.length), aprocessor[0] }).replaceAll("\\s+", " ");
/*     */     }
/* 336 */     catch (Throwable throwable) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean areShadersSupported() {
/* 344 */     return shadersSupported;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getLogText() {
/* 349 */     return logText;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetProgrami(int program, int pname) {
/* 354 */     return arbShaders ? ARBShaderObjects.glGetObjectParameteriARB(program, pname) : GL20.glGetProgrami(program, pname);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glAttachShader(int program, int shaderIn) {
/* 359 */     if (arbShaders) {
/*     */       
/* 361 */       ARBShaderObjects.glAttachObjectARB(program, shaderIn);
/*     */     }
/*     */     else {
/*     */       
/* 365 */       GL20.glAttachShader(program, shaderIn);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glDeleteShader(int p_153180_0_) {
/* 371 */     if (arbShaders) {
/*     */       
/* 373 */       ARBShaderObjects.glDeleteObjectARB(p_153180_0_);
/*     */     }
/*     */     else {
/*     */       
/* 377 */       GL20.glDeleteShader(p_153180_0_);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glCreateShader(int type) {
/* 383 */     return arbShaders ? ARBShaderObjects.glCreateShaderObjectARB(type) : GL20.glCreateShader(type);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glShaderSource(int shaderIn, ByteBuffer string) {
/* 388 */     if (arbShaders) {
/*     */       
/* 390 */       ARBShaderObjects.glShaderSourceARB(shaderIn, string);
/*     */     }
/*     */     else {
/*     */       
/* 394 */       GL20.glShaderSource(shaderIn, string);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glCompileShader(int shaderIn) {
/* 400 */     if (arbShaders) {
/*     */       
/* 402 */       ARBShaderObjects.glCompileShaderARB(shaderIn);
/*     */     }
/*     */     else {
/*     */       
/* 406 */       GL20.glCompileShader(shaderIn);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetShaderi(int shaderIn, int pname) {
/* 412 */     return arbShaders ? ARBShaderObjects.glGetObjectParameteriARB(shaderIn, pname) : GL20.glGetShaderi(shaderIn, pname);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String glGetShaderInfoLog(int shaderIn, int maxLength) {
/* 417 */     return arbShaders ? ARBShaderObjects.glGetInfoLogARB(shaderIn, maxLength) : GL20.glGetShaderInfoLog(shaderIn, maxLength);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String glGetProgramInfoLog(int program, int maxLength) {
/* 422 */     return arbShaders ? ARBShaderObjects.glGetInfoLogARB(program, maxLength) : GL20.glGetProgramInfoLog(program, maxLength);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glUseProgram(int program) {
/* 427 */     if (arbShaders) {
/*     */       
/* 429 */       ARBShaderObjects.glUseProgramObjectARB(program);
/*     */     }
/*     */     else {
/*     */       
/* 433 */       GL20.glUseProgram(program);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glCreateProgram() {
/* 439 */     return arbShaders ? ARBShaderObjects.glCreateProgramObjectARB() : GL20.glCreateProgram();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glDeleteProgram(int program) {
/* 444 */     if (arbShaders) {
/*     */       
/* 446 */       ARBShaderObjects.glDeleteObjectARB(program);
/*     */     }
/*     */     else {
/*     */       
/* 450 */       GL20.glDeleteProgram(program);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glLinkProgram(int program) {
/* 456 */     if (arbShaders) {
/*     */       
/* 458 */       ARBShaderObjects.glLinkProgramARB(program);
/*     */     }
/*     */     else {
/*     */       
/* 462 */       GL20.glLinkProgram(program);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetUniformLocation(int programObj, CharSequence name) {
/* 468 */     return arbShaders ? ARBShaderObjects.glGetUniformLocationARB(programObj, name) : GL20.glGetUniformLocation(programObj, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glUniform1(int location, IntBuffer values) {
/* 473 */     if (arbShaders) {
/*     */       
/* 475 */       ARBShaderObjects.glUniform1ARB(location, values);
/*     */     }
/*     */     else {
/*     */       
/* 479 */       GL20.glUniform1(location, values);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glUniform1i(int location, int v0) {
/* 485 */     if (arbShaders) {
/*     */       
/* 487 */       ARBShaderObjects.glUniform1iARB(location, v0);
/*     */     }
/*     */     else {
/*     */       
/* 491 */       GL20.glUniform1i(location, v0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glUniform1(int location, FloatBuffer values) {
/* 497 */     if (arbShaders) {
/*     */       
/* 499 */       ARBShaderObjects.glUniform1ARB(location, values);
/*     */     }
/*     */     else {
/*     */       
/* 503 */       GL20.glUniform1(location, values);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glUniform2(int location, IntBuffer values) {
/* 509 */     if (arbShaders) {
/*     */       
/* 511 */       ARBShaderObjects.glUniform2ARB(location, values);
/*     */     }
/*     */     else {
/*     */       
/* 515 */       GL20.glUniform2(location, values);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glUniform2(int location, FloatBuffer values) {
/* 521 */     if (arbShaders) {
/*     */       
/* 523 */       ARBShaderObjects.glUniform2ARB(location, values);
/*     */     }
/*     */     else {
/*     */       
/* 527 */       GL20.glUniform2(location, values);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glUniform3(int location, IntBuffer values) {
/* 533 */     if (arbShaders) {
/*     */       
/* 535 */       ARBShaderObjects.glUniform3ARB(location, values);
/*     */     }
/*     */     else {
/*     */       
/* 539 */       GL20.glUniform3(location, values);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glUniform3(int location, FloatBuffer values) {
/* 545 */     if (arbShaders) {
/*     */       
/* 547 */       ARBShaderObjects.glUniform3ARB(location, values);
/*     */     }
/*     */     else {
/*     */       
/* 551 */       GL20.glUniform3(location, values);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glUniform4(int location, IntBuffer values) {
/* 557 */     if (arbShaders) {
/*     */       
/* 559 */       ARBShaderObjects.glUniform4ARB(location, values);
/*     */     }
/*     */     else {
/*     */       
/* 563 */       GL20.glUniform4(location, values);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glUniform4(int location, FloatBuffer values) {
/* 569 */     if (arbShaders) {
/*     */       
/* 571 */       ARBShaderObjects.glUniform4ARB(location, values);
/*     */     }
/*     */     else {
/*     */       
/* 575 */       GL20.glUniform4(location, values);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glUniformMatrix2(int location, boolean transpose, FloatBuffer matrices) {
/* 581 */     if (arbShaders) {
/*     */       
/* 583 */       ARBShaderObjects.glUniformMatrix2ARB(location, transpose, matrices);
/*     */     }
/*     */     else {
/*     */       
/* 587 */       GL20.glUniformMatrix2(location, transpose, matrices);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glUniformMatrix3(int location, boolean transpose, FloatBuffer matrices) {
/* 593 */     if (arbShaders) {
/*     */       
/* 595 */       ARBShaderObjects.glUniformMatrix3ARB(location, transpose, matrices);
/*     */     }
/*     */     else {
/*     */       
/* 599 */       GL20.glUniformMatrix3(location, transpose, matrices);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glUniformMatrix4(int location, boolean transpose, FloatBuffer matrices) {
/* 605 */     if (arbShaders) {
/*     */       
/* 607 */       ARBShaderObjects.glUniformMatrix4ARB(location, transpose, matrices);
/*     */     }
/*     */     else {
/*     */       
/* 611 */       GL20.glUniformMatrix4(location, transpose, matrices);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGetAttribLocation(int p_153164_0_, CharSequence p_153164_1_) {
/* 617 */     return arbShaders ? ARBVertexShader.glGetAttribLocationARB(p_153164_0_, p_153164_1_) : GL20.glGetAttribLocation(p_153164_0_, p_153164_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int glGenBuffers() {
/* 622 */     return arbVbo ? ARBVertexBufferObject.glGenBuffersARB() : GL15.glGenBuffers();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glBindBuffer(int target, int buffer) {
/* 627 */     if (arbVbo) {
/*     */       
/* 629 */       ARBVertexBufferObject.glBindBufferARB(target, buffer);
/*     */     }
/*     */     else {
/*     */       
/* 633 */       GL15.glBindBuffer(target, buffer);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glBufferData(int target, ByteBuffer data, int usage) {
/* 639 */     if (arbVbo) {
/*     */       
/* 641 */       ARBVertexBufferObject.glBufferDataARB(target, data, usage);
/*     */     }
/*     */     else {
/*     */       
/* 645 */       GL15.glBufferData(target, data, usage);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glDeleteBuffers(int buffer) {
/* 651 */     if (arbVbo) {
/*     */       
/* 653 */       ARBVertexBufferObject.glDeleteBuffersARB(buffer);
/*     */     }
/*     */     else {
/*     */       
/* 657 */       GL15.glDeleteBuffers(buffer);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean useVbo() {
/* 663 */     return Config.isMultiTexture() ? false : ((Config.isRenderRegions() && !vboRegions) ? false : ((vboSupported && (Minecraft.getMinecraft()).gameSettings.useVbo)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glBindFramebuffer(int target, int framebufferIn) {
/* 668 */     if (framebufferSupported)
/*     */     {
/* 670 */       switch (framebufferType) {
/*     */         
/*     */         case 0:
/* 673 */           GL30.glBindFramebuffer(target, framebufferIn);
/*     */           break;
/*     */         
/*     */         case 1:
/* 677 */           ARBFramebufferObject.glBindFramebuffer(target, framebufferIn);
/*     */           break;
/*     */         
/*     */         case 2:
/* 681 */           EXTFramebufferObject.glBindFramebufferEXT(target, framebufferIn);
/*     */           break;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glBindRenderbuffer(int target, int renderbuffer) {
/* 688 */     if (framebufferSupported)
/*     */     {
/* 690 */       switch (framebufferType) {
/*     */         
/*     */         case 0:
/* 693 */           GL30.glBindRenderbuffer(target, renderbuffer);
/*     */           break;
/*     */         
/*     */         case 1:
/* 697 */           ARBFramebufferObject.glBindRenderbuffer(target, renderbuffer);
/*     */           break;
/*     */         
/*     */         case 2:
/* 701 */           EXTFramebufferObject.glBindRenderbufferEXT(target, renderbuffer);
/*     */           break;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glDeleteRenderbuffers(int renderbuffer) {
/* 708 */     if (framebufferSupported)
/*     */     {
/* 710 */       switch (framebufferType) {
/*     */         
/*     */         case 0:
/* 713 */           GL30.glDeleteRenderbuffers(renderbuffer);
/*     */           break;
/*     */         
/*     */         case 1:
/* 717 */           ARBFramebufferObject.glDeleteRenderbuffers(renderbuffer);
/*     */           break;
/*     */         
/*     */         case 2:
/* 721 */           EXTFramebufferObject.glDeleteRenderbuffersEXT(renderbuffer);
/*     */           break;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glDeleteFramebuffers(int framebufferIn) {
/* 728 */     if (framebufferSupported)
/*     */     {
/* 730 */       switch (framebufferType) {
/*     */         
/*     */         case 0:
/* 733 */           GL30.glDeleteFramebuffers(framebufferIn);
/*     */           break;
/*     */         
/*     */         case 1:
/* 737 */           ARBFramebufferObject.glDeleteFramebuffers(framebufferIn);
/*     */           break;
/*     */         
/*     */         case 2:
/* 741 */           EXTFramebufferObject.glDeleteFramebuffersEXT(framebufferIn);
/*     */           break;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static int glGenFramebuffers() {
/* 748 */     if (!framebufferSupported)
/*     */     {
/* 750 */       return -1;
/*     */     }
/*     */ 
/*     */     
/* 754 */     switch (framebufferType) {
/*     */       
/*     */       case 0:
/* 757 */         return GL30.glGenFramebuffers();
/*     */       
/*     */       case 1:
/* 760 */         return ARBFramebufferObject.glGenFramebuffers();
/*     */       
/*     */       case 2:
/* 763 */         return EXTFramebufferObject.glGenFramebuffersEXT();
/*     */     } 
/*     */     
/* 766 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int glGenRenderbuffers() {
/* 773 */     if (!framebufferSupported)
/*     */     {
/* 775 */       return -1;
/*     */     }
/*     */ 
/*     */     
/* 779 */     switch (framebufferType) {
/*     */       
/*     */       case 0:
/* 782 */         return GL30.glGenRenderbuffers();
/*     */       
/*     */       case 1:
/* 785 */         return ARBFramebufferObject.glGenRenderbuffers();
/*     */       
/*     */       case 2:
/* 788 */         return EXTFramebufferObject.glGenRenderbuffersEXT();
/*     */     } 
/*     */     
/* 791 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void glRenderbufferStorage(int target, int internalFormat, int width, int height) {
/* 798 */     if (framebufferSupported)
/*     */     {
/* 800 */       switch (framebufferType) {
/*     */         
/*     */         case 0:
/* 803 */           GL30.glRenderbufferStorage(target, internalFormat, width, height);
/*     */           break;
/*     */         
/*     */         case 1:
/* 807 */           ARBFramebufferObject.glRenderbufferStorage(target, internalFormat, width, height);
/*     */           break;
/*     */         
/*     */         case 2:
/* 811 */           EXTFramebufferObject.glRenderbufferStorageEXT(target, internalFormat, width, height);
/*     */           break;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static void glFramebufferRenderbuffer(int target, int attachment, int renderBufferTarget, int renderBuffer) {
/* 818 */     if (framebufferSupported)
/*     */     {
/* 820 */       switch (framebufferType) {
/*     */         
/*     */         case 0:
/* 823 */           GL30.glFramebufferRenderbuffer(target, attachment, renderBufferTarget, renderBuffer);
/*     */           break;
/*     */         
/*     */         case 1:
/* 827 */           ARBFramebufferObject.glFramebufferRenderbuffer(target, attachment, renderBufferTarget, renderBuffer);
/*     */           break;
/*     */         
/*     */         case 2:
/* 831 */           EXTFramebufferObject.glFramebufferRenderbufferEXT(target, attachment, renderBufferTarget, renderBuffer);
/*     */           break;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static int glCheckFramebufferStatus(int target) {
/* 838 */     if (!framebufferSupported)
/*     */     {
/* 840 */       return -1;
/*     */     }
/*     */ 
/*     */     
/* 844 */     switch (framebufferType) {
/*     */       
/*     */       case 0:
/* 847 */         return GL30.glCheckFramebufferStatus(target);
/*     */       
/*     */       case 1:
/* 850 */         return ARBFramebufferObject.glCheckFramebufferStatus(target);
/*     */       
/*     */       case 2:
/* 853 */         return EXTFramebufferObject.glCheckFramebufferStatusEXT(target);
/*     */     } 
/*     */     
/* 856 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
/* 863 */     if (framebufferSupported)
/*     */     {
/* 865 */       switch (framebufferType) {
/*     */         
/*     */         case 0:
/* 868 */           GL30.glFramebufferTexture2D(target, attachment, textarget, texture, level);
/*     */           break;
/*     */         
/*     */         case 1:
/* 872 */           ARBFramebufferObject.glFramebufferTexture2D(target, attachment, textarget, texture, level);
/*     */           break;
/*     */         
/*     */         case 2:
/* 876 */           EXTFramebufferObject.glFramebufferTexture2DEXT(target, attachment, textarget, texture, level);
/*     */           break;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static void setActiveTexture(int texture) {
/* 883 */     if (arbMultitexture) {
/*     */       
/* 885 */       ARBMultitexture.glActiveTextureARB(texture);
/*     */     }
/*     */     else {
/*     */       
/* 889 */       GL13.glActiveTexture(texture);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setClientActiveTexture(int texture) {
/* 895 */     if (arbMultitexture) {
/*     */       
/* 897 */       ARBMultitexture.glClientActiveTextureARB(texture);
/*     */     }
/*     */     else {
/*     */       
/* 901 */       GL13.glClientActiveTexture(texture);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setLightmapTextureCoords(int target, float p_77475_1_, float p_77475_2_) {
/* 907 */     if (arbMultitexture) {
/*     */       
/* 909 */       ARBMultitexture.glMultiTexCoord2fARB(target, p_77475_1_, p_77475_2_);
/*     */     }
/*     */     else {
/*     */       
/* 913 */       GL13.glMultiTexCoord2f(target, p_77475_1_, p_77475_2_);
/*     */     } 
/*     */     
/* 916 */     if (target == lightmapTexUnit) {
/*     */       
/* 918 */       lastBrightnessX = p_77475_1_;
/* 919 */       lastBrightnessY = p_77475_2_;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glBlendFunc(int sFactorRGB, int dFactorRGB, int sfactorAlpha, int dfactorAlpha) {
/* 925 */     if (openGL14) {
/*     */       
/* 927 */       if (extBlendFuncSeparate)
/*     */       {
/* 929 */         EXTBlendFuncSeparate.glBlendFuncSeparateEXT(sFactorRGB, dFactorRGB, sfactorAlpha, dfactorAlpha);
/*     */       }
/*     */       else
/*     */       {
/* 933 */         GL14.glBlendFuncSeparate(sFactorRGB, dFactorRGB, sfactorAlpha, dfactorAlpha);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 938 */       GL11.glBlendFunc(sFactorRGB, dFactorRGB);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isFramebufferEnabled() {
/* 944 */     return Config.isFastRender() ? false : (Config.isAntialiasing() ? false : ((framebufferSupported && (Minecraft.getMinecraft()).gameSettings.fboEnable)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glBufferData(int p_glBufferData_0_, long p_glBufferData_1_, int p_glBufferData_3_) {
/* 949 */     if (arbVbo) {
/*     */       
/* 951 */       ARBVertexBufferObject.glBufferDataARB(p_glBufferData_0_, p_glBufferData_1_, p_glBufferData_3_);
/*     */     }
/*     */     else {
/*     */       
/* 955 */       GL15.glBufferData(p_glBufferData_0_, p_glBufferData_1_, p_glBufferData_3_);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glBufferSubData(int p_glBufferSubData_0_, long p_glBufferSubData_1_, ByteBuffer p_glBufferSubData_3_) {
/* 961 */     if (arbVbo) {
/*     */       
/* 963 */       ARBVertexBufferObject.glBufferSubDataARB(p_glBufferSubData_0_, p_glBufferSubData_1_, p_glBufferSubData_3_);
/*     */     }
/*     */     else {
/*     */       
/* 967 */       GL15.glBufferSubData(p_glBufferSubData_0_, p_glBufferSubData_1_, p_glBufferSubData_3_);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void glCopyBufferSubData(int p_glCopyBufferSubData_0_, int p_glCopyBufferSubData_1_, long p_glCopyBufferSubData_2_, long p_glCopyBufferSubData_4_, long p_glCopyBufferSubData_6_) {
/* 973 */     if (openGL31) {
/*     */       
/* 975 */       GL31.glCopyBufferSubData(p_glCopyBufferSubData_0_, p_glCopyBufferSubData_1_, p_glCopyBufferSubData_2_, p_glCopyBufferSubData_4_, p_glCopyBufferSubData_6_);
/*     */     }
/*     */     else {
/*     */       
/* 979 */       ARBCopyBuffer.glCopyBufferSubData(p_glCopyBufferSubData_0_, p_glCopyBufferSubData_1_, p_glCopyBufferSubData_2_, p_glCopyBufferSubData_4_, p_glCopyBufferSubData_6_);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getCpu() {
/* 985 */     return (cpu == null) ? "<unknown>" : cpu;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\OpenGlHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */