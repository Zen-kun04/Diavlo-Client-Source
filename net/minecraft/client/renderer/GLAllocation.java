/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.ByteOrder;
/*    */ import java.nio.FloatBuffer;
/*    */ import java.nio.IntBuffer;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import org.lwjgl.util.glu.GLU;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GLAllocation
/*    */ {
/*    */   public static synchronized int generateDisplayLists(int range) {
/* 15 */     int i = GL11.glGenLists(range);
/*    */     
/* 17 */     if (i == 0) {
/*    */       
/* 19 */       int j = GL11.glGetError();
/* 20 */       String s = "No error code reported";
/*    */       
/* 22 */       if (j != 0)
/*    */       {
/* 24 */         s = GLU.gluErrorString(j);
/*    */       }
/*    */       
/* 27 */       throw new IllegalStateException("glGenLists returned an ID of 0 for a count of " + range + ", GL error (" + j + "): " + s);
/*    */     } 
/*    */ 
/*    */     
/* 31 */     return i;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static synchronized void deleteDisplayLists(int list, int range) {
/* 37 */     GL11.glDeleteLists(list, range);
/*    */   }
/*    */ 
/*    */   
/*    */   public static synchronized void deleteDisplayLists(int list) {
/* 42 */     GL11.glDeleteLists(list, 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public static synchronized ByteBuffer createDirectByteBuffer(int capacity) {
/* 47 */     return ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
/*    */   }
/*    */ 
/*    */   
/*    */   public static IntBuffer createDirectIntBuffer(int capacity) {
/* 52 */     return createDirectByteBuffer(capacity << 2).asIntBuffer();
/*    */   }
/*    */ 
/*    */   
/*    */   public static FloatBuffer createDirectFloatBuffer(int capacity) {
/* 57 */     return createDirectByteBuffer(capacity << 2).asFloatBuffer();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\GLAllocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */