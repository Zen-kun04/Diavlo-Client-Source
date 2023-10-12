/*    */ package net.minecraft.world;
/*    */ 
/*    */ public class ColorizerGrass
/*    */ {
/*  5 */   private static int[] grassBuffer = new int[65536];
/*    */ 
/*    */   
/*    */   public static void setGrassBiomeColorizer(int[] p_77479_0_) {
/*  9 */     grassBuffer = p_77479_0_;
/*    */   }
/*    */ 
/*    */   
/*    */   public static int getGrassColor(double p_77480_0_, double p_77480_2_) {
/* 14 */     p_77480_2_ *= p_77480_0_;
/* 15 */     int i = (int)((1.0D - p_77480_0_) * 255.0D);
/* 16 */     int j = (int)((1.0D - p_77480_2_) * 255.0D);
/* 17 */     int k = j << 8 | i;
/* 18 */     return (k > grassBuffer.length) ? -65281 : grassBuffer[k];
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\ColorizerGrass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */