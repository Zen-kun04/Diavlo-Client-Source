/*    */ package net.arikia.dev.drpc;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class OSUtil
/*    */ {
/*    */   public boolean isMac() {
/* 12 */     return getOS().toLowerCase()
/* 13 */       .startsWith("mac");
/*    */   }
/*    */   
/*    */   public boolean isWindows() {
/* 17 */     return getOS().toLowerCase()
/* 18 */       .startsWith("win");
/*    */   }
/*    */   
/*    */   public String getOS() {
/* 22 */     return System.getProperty("os.name").toLowerCase();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\arikia\dev\drpc\OSUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */