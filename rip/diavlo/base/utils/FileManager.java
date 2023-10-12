/*    */ package rip.diavlo.base.utils;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileWriter;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileManager
/*    */ {
/*    */   public static void write(File file, String text) {
/*    */     try {
/* 12 */       if (!file.getParentFile().exists()) {
/* 13 */         file.getParentFile().mkdirs();
/* 14 */       } else if (!file.exists()) {
/* 15 */         file.createNewFile();
/*    */       } 
/* 17 */       FileWriter fw = new FileWriter(file, true);
/* 18 */       fw.write(String.valueOf(text) + System.getProperty("line.separator"));
/* 19 */       fw.close();
/*    */     }
/* 21 */     catch (Exception ex) {
/*    */       
/* 23 */       ex.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\FileManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */