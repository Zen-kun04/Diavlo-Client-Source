/*    */ package net.optifine.config;
/*    */ 
/*    */ 
/*    */ public class GlVersion
/*    */ {
/*    */   private int major;
/*    */   private int minor;
/*    */   private int release;
/*    */   private String suffix;
/*    */   
/*    */   public GlVersion(int major, int minor) {
/* 12 */     this(major, minor, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public GlVersion(int major, int minor, int release) {
/* 17 */     this(major, minor, release, (String)null);
/*    */   }
/*    */ 
/*    */   
/*    */   public GlVersion(int major, int minor, int release, String suffix) {
/* 22 */     this.major = major;
/* 23 */     this.minor = minor;
/* 24 */     this.release = release;
/* 25 */     this.suffix = suffix;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMajor() {
/* 30 */     return this.major;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMinor() {
/* 35 */     return this.minor;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRelease() {
/* 40 */     return this.release;
/*    */   }
/*    */ 
/*    */   
/*    */   public int toInt() {
/* 45 */     return (this.minor > 9) ? (this.major * 100 + this.minor) : ((this.release > 9) ? (this.major * 100 + this.minor * 10 + 9) : (this.major * 100 + this.minor * 10 + this.release));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 50 */     return (this.suffix == null) ? ("" + this.major + "." + this.minor + "." + this.release) : ("" + this.major + "." + this.minor + "." + this.release + this.suffix);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\config\GlVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */