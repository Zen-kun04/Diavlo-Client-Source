/*    */ package rip.diavlo.base.utils.shader;
/*    */ 
/*    */ import java.nio.FloatBuffer;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import org.lwjgl.BufferUtils;
/*    */ import org.lwjgl.input.Mouse;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import org.lwjgl.opengl.GL20;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ShaderRenderer
/*    */ {
/* 15 */   private final Minecraft mc = Minecraft.getMinecraft();
/*    */   
/* 17 */   private int program = GL20.glCreateProgram(); public int getProgram() { return this.program; }
/*    */   
/* 19 */   private long startTime = System.currentTimeMillis();
/*    */   
/* 21 */   float mousemove = 0.01F;
/*    */   
/*    */   public ShaderRenderer(String fragment) {
/* 24 */     initShader(fragment);
/*    */   }
/*    */   
/*    */   private void initShader(String frag) {
/* 28 */     int vertex = GL20.glCreateShader(35633);
/* 29 */     int fragment = GL20.glCreateShader(35632);
/* 30 */     GL20.glShaderSource(vertex, "#version 130\n\nvoid main() {\n    gl_TexCoord[0] = gl_MultiTexCoord0;\n    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n}");
/* 31 */     GL20.glShaderSource(fragment, frag);
/* 32 */     GL20.glValidateProgram(this.program);
/* 33 */     GL20.glCompileShader(vertex);
/* 34 */     GL20.glCompileShader(fragment);
/* 35 */     GL20.glAttachShader(this.program, vertex);
/* 36 */     GL20.glAttachShader(this.program, fragment);
/* 37 */     GL20.glLinkProgram(this.program);
/*    */   }
/*    */   
/*    */   public void renderFirst() {
/* 41 */     GL11.glClear(16640);
/* 42 */     GL20.glUseProgram(0);
/*    */     
/* 44 */     GL20.glUseProgram(this.program);
/*    */   }
/*    */ 
/*    */   
/*    */   public void renderSecond() {
/* 49 */     GL11.glEnable(3042);
/* 50 */     GL11.glBlendFunc(770, 771);
/* 51 */     ScaledResolution sr = new ScaledResolution(this.mc);
/* 52 */     GL11.glBegin(7);
/* 53 */     GL11.glTexCoord2d(0.0D, 1.0D);
/* 54 */     GL11.glVertex2d(0.0D, 0.0D);
/* 55 */     GL11.glTexCoord2d(0.0D, 0.0D);
/* 56 */     GL11.glVertex2d(0.0D, sr.getScaledHeight());
/* 57 */     GL11.glTexCoord2d(1.0D, 0.0D);
/* 58 */     GL11.glVertex2d(sr.getScaledWidth(), sr.getScaledHeight());
/* 59 */     GL11.glTexCoord2d(1.0D, 1.0D);
/* 60 */     GL11.glVertex2d(sr.getScaledWidth(), 0.0D);
/* 61 */     GL11.glEnd();
/* 62 */     GL20.glUseProgram(0);
/*    */   }
/*    */   
/*    */   public void addDefaultUniforms(boolean detectmouse) {
/* 66 */     this.mousemove = (Mouse.getX() > 957) ? (this.mousemove - 0.002F) : (this.mousemove + 0.002F);
/* 67 */     float n3 = (Mouse.getX() / this.mc.displayWidth);
/* 68 */     float n4 = (Mouse.getY() / this.mc.displayHeight);
/* 69 */     FloatBuffer floatBuffer3 = BufferUtils.createFloatBuffer(2);
/* 70 */     floatBuffer3.position(0);
/* 71 */     floatBuffer3.put(n3);
/* 72 */     floatBuffer3.put(n4);
/* 73 */     floatBuffer3.flip();
/* 74 */     GL20.glUniform2f(GL20.glGetUniformLocation(this.program, "resolution"), this.mc.displayWidth, this.mc.displayHeight);
/* 75 */     float time = (float)(System.currentTimeMillis() - this.startTime) / 1000.0F;
/* 76 */     GL20.glUniform1f(GL20.glGetUniformLocation(this.program, "time"), time);
/* 77 */     if (detectmouse) {
/* 78 */       GL20.glUniform2f(GL20.glGetUniformLocation(this.program, "mouse"), this.mousemove, 0.0F);
/*    */     } else {
/* 80 */       GL20.glUniform2(GL20.glGetUniformLocation(this.program, "mouse"), floatBuffer3);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\shader\ShaderRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */