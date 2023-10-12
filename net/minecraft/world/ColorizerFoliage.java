/*    */ package net.minecraft.world;
/*    */ 
/*    */ public class ColorizerFoliage
/*    */ {
/*  5 */   private static int[] foliageBuffer = new int[65536];
/*    */ 
/*    */   
/*    */   public static void setFoliageBiomeColorizer(int[] p_77467_0_) {
/*  9 */     foliageBuffer = p_77467_0_;
/*    */   }
/*    */ 
/*    */   
/*    */   public static int getFoliageColor(double p_77470_0_, double p_77470_2_) {
/* 14 */     p_77470_2_ *= p_77470_0_;
/* 15 */     int i = (int)((1.0D - p_77470_0_) * 255.0D);
/* 16 */     int j = (int)((1.0D - p_77470_2_) * 255.0D);
/* 17 */     return foliageBuffer[j << 8 | i];
/*    */   }
/*    */ 
/*    */   
/*    */   public static int getFoliageColorPine() {
/* 22 */     return 6396257;
/*    */   }
/*    */ 
/*    */   
/*    */   public static int getFoliageColorBirch() {
/* 27 */     return 8431445;
/*    */   }
/*    */ 
/*    */   
/*    */   public static int getFoliageColorBasic() {
/* 32 */     return 4764952;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\ColorizerFoliage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */