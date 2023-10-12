/*    */ package rip.diavlo.base.utils;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.FileReader;
/*    */ import java.io.FileWriter;
/*    */ 
/*    */ public class FileUtility
/*    */ {
/*    */   public static String readFirstLine(String filepath) {
/*    */     
/* 12 */     try { BufferedReader reader = new BufferedReader(new FileReader(filepath));
/* 13 */       String firstLine = reader.readLine();
/* 14 */       reader.close();
/* 15 */       return firstLine; }
/* 16 */     catch (Exception e) { return null; }
/*    */   
/*    */   }
/*    */   public static void overwriteFileContent(String filepath, String newContent) {
/*    */     try {
/* 21 */       BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
/* 22 */       writer.write(newContent);
/* 23 */       writer.close();
/* 24 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\FileUtility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */