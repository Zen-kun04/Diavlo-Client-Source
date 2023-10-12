/*     */ package rip.diavlo.base.utils.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GL20;
/*     */ 
/*     */ public class Shaders
/*     */ {
/*  16 */   private final int programId = createShader("rq.frag", "vertex.vsh");
/*     */   
/*     */   public void draw(float x, float y, float width, float height, float radius, Color color) {
/*  19 */     start();
/*  20 */     UniformUtil.uniform2f(this.programId, "u_size", width, height);
/*  21 */     UniformUtil.uniform1f(this.programId, "u_radius", radius);
/*  22 */     UniformUtil.uniform4f(this.programId, "u_color", color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*  23 */     GlStateManager.enableBlend();
/*  24 */     GlStateManager.blendFunc(770, 771);
/*  25 */     GlStateManager.enableAlpha();
/*  26 */     GlStateManager.alphaFunc(516, 0.0F);
/*  27 */     drawQuad(x, y, width, height);
/*  28 */     GlStateManager.disableBlend();
/*  29 */     stop();
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
/*     */   public void draw(double x, double y, double width, double height, double radius, Color color) {
/*  41 */     draw((float)x, (float)y, (float)width, (float)height, (float)radius, color);
/*     */   }
/*     */   
/*     */   public void start() {
/*  45 */     GL20.glUseProgram(this.programId);
/*     */   }
/*     */   
/*     */   public void stop() {
/*  49 */     GL20.glUseProgram(0);
/*     */   }
/*     */   
/*     */   public static void drawQuad(double x, double y, double width, double height) {
/*  53 */     GL11.glBegin(7);
/*  54 */     GL11.glTexCoord2f(0.0F, 0.0F);
/*  55 */     GL11.glVertex2d(x, y + height);
/*  56 */     GL11.glTexCoord2f(1.0F, 0.0F);
/*  57 */     GL11.glVertex2d(x + width, y + height);
/*  58 */     GL11.glTexCoord2f(1.0F, 1.0F);
/*  59 */     GL11.glVertex2d(x + width, y);
/*  60 */     GL11.glTexCoord2f(0.0F, 1.0F);
/*  61 */     GL11.glVertex2d(x, y);
/*  62 */     GL11.glEnd();
/*     */   }
/*     */   
/*     */   public static int createShader(String fragmentResource, String vertexResource) {
/*  66 */     String fragmentSource = getShaderResource(fragmentResource);
/*  67 */     String vertexSource = getShaderResource(vertexResource);
/*     */     
/*  69 */     if (fragmentResource == null || vertexResource == null) {
/*  70 */       System.out.println(("Fragment: " + fragmentSource == null));
/*  71 */       System.out.println(("Vertex: " + vertexSource == null));
/*  72 */       return -1;
/*     */     } 
/*     */     
/*  75 */     int fragmentId = GL20.glCreateShader(35632);
/*  76 */     int vertexId = GL20.glCreateShader(35633);
/*     */     
/*  78 */     GL20.glShaderSource(fragmentId, fragmentSource);
/*  79 */     GL20.glShaderSource(vertexId, vertexSource);
/*  80 */     GL20.glCompileShader(fragmentId);
/*  81 */     GL20.glCompileShader(vertexId);
/*     */     
/*  83 */     if (compileShader(fragmentId)) return -1; 
/*  84 */     if (compileShader(vertexId)) return -1;
/*     */     
/*  86 */     int programId = GL20.glCreateProgram();
/*  87 */     GL20.glAttachShader(programId, fragmentId);
/*  88 */     GL20.glAttachShader(programId, vertexId);
/*  89 */     GL20.glValidateProgram(programId);
/*  90 */     GL20.glLinkProgram(programId);
/*  91 */     GL20.glDeleteShader(fragmentId);
/*  92 */     GL20.glDeleteShader(vertexId);
/*     */     
/*  94 */     return programId;
/*     */   }
/*     */   
/*     */   public static String getShaderResource(String resource) {
/*     */     try {
/*  99 */       InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/shader/" + resource)).getInputStream();
/* 100 */       InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
/* 101 */       BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
/* 102 */       String source = "";
/*     */       try {
/*     */         String s;
/* 105 */         for (; (s = bufferedReader.readLine()) != null; source = source + s + System.lineSeparator());
/* 106 */       } catch (IOException iOException) {}
/*     */ 
/*     */       
/* 109 */       return source;
/* 110 */     } catch (IOException|NullPointerException e) {
/* 111 */       e.printStackTrace();
/* 112 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean compileShader(int shaderId) {
/* 117 */     boolean compiled = (GL20.glGetShaderi(shaderId, 35713) == 1);
/* 118 */     if (compiled) return false;
/*     */     
/* 120 */     String shaderLog = GL20.glGetShaderInfoLog(shaderId, 8192);
/* 121 */     System.out.println(shaderLog);
/* 122 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\render\Shaders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */