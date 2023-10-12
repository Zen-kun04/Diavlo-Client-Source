/*    */ package rip.diavlo.base.utils.render;
/*    */ 
/*    */ import java.nio.FloatBuffer;
/*    */ import org.lwjgl.opengl.GL20;
/*    */ 
/*    */ 
/*    */ public class UniformUtil
/*    */ {
/*    */   public static void uniformFB(int programId, String name, FloatBuffer floatBuffer) {
/* 10 */     GL20.glUniform1(getLocation(programId, name), floatBuffer);
/*    */   }
/*    */   
/*    */   public static void uniform1i(int programId, String name, int i) {
/* 14 */     GL20.glUniform1i(getLocation(programId, name), i);
/*    */   }
/*    */   
/*    */   public static void uniform2i(int programId, String name, int i, int j) {
/* 18 */     GL20.glUniform2i(getLocation(programId, name), i, j);
/*    */   }
/*    */   
/*    */   public static void uniform1f(int programId, String name, float f) {
/* 22 */     GL20.glUniform1f(getLocation(programId, name), f);
/*    */   }
/*    */   
/*    */   public static void uniform2f(int programId, String name, float f, float g) {
/* 26 */     GL20.glUniform2f(getLocation(programId, name), f, g);
/*    */   }
/*    */   
/*    */   public static void uniform3f(int programId, String name, float f, float g, float h) {
/* 30 */     GL20.glUniform3f(getLocation(programId, name), f, g, h);
/*    */   }
/*    */   
/*    */   public static void uniform4f(int programId, String name, float f, float g, float h, float i) {
/* 34 */     GL20.glUniform4f(getLocation(programId, name), f, g, h, i);
/*    */   }
/*    */   
/*    */   private static int getLocation(int programId, String name) {
/* 38 */     return GL20.glGetUniformLocation(programId, name);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\render\UniformUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */