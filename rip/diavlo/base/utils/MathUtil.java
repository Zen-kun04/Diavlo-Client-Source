/*    */ package rip.diavlo.base.utils;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import java.math.RoundingMode;
/*    */ import java.util.concurrent.ThreadLocalRandom;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public final class MathUtil {
/*    */   private static final float DEGREES_TO_RADIANS = 0.017453292F;
/*    */   
/*    */   private MathUtil() {
/* 12 */     throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
/*    */   }
/*    */ 
/*    */   
/*    */   public static double random(double min, double max) {
/* 17 */     if (min == max)
/* 18 */       return min; 
/* 19 */     if (min > max) {
/* 20 */       double d = min;
/* 21 */       min = max;
/* 22 */       max = d;
/*    */     } 
/* 24 */     return ThreadLocalRandom.current().nextDouble(min, max);
/*    */   }
/*    */   
/*    */   public static float randomFloat(float min, float max) {
/* 28 */     if (min == max)
/* 29 */       return min; 
/* 30 */     if (min > max) {
/* 31 */       float d = min;
/* 32 */       min = max;
/* 33 */       max = d;
/*    */     } 
/* 35 */     return (float)ThreadLocalRandom.current().nextDouble(min, max);
/*    */   }
/*    */   
/*    */   public static float toRadians(float angdeg) {
/* 39 */     return angdeg * 0.017453292F;
/*    */   }
/*    */   
/*    */   public static double round(double value, int places) {
/* 43 */     BigDecimal bigDecimal = BigDecimal.valueOf(value);
/*    */     
/* 45 */     return bigDecimal.setScale(places, RoundingMode.HALF_UP).doubleValue();
/*    */   }
/*    */   
/*    */   public static double round(double value, int scale, double inc) {
/* 49 */     double halfOfInc = inc / 2.0D;
/* 50 */     double floored = Math.floor(value / inc) * inc;
/*    */     
/* 52 */     if (value >= floored + halfOfInc) {
/* 53 */       return (new BigDecimal(Math.ceil(value / inc) * inc))
/* 54 */         .setScale(scale, RoundingMode.HALF_UP)
/* 55 */         .doubleValue();
/*    */     }
/* 57 */     return (new BigDecimal(floored))
/* 58 */       .setScale(scale, RoundingMode.HALF_UP)
/* 59 */       .doubleValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public static double roundWithSteps(double value, double steps) {
/* 64 */     double a = Math.round(value / steps) * steps;
/* 65 */     a *= 1000.0D;
/* 66 */     a = (int)a;
/* 67 */     a /= 1000.0D;
/* 68 */     return a;
/*    */   }
/*    */   
/*    */   public static double lerp(double a, double b, double c) {
/* 72 */     return a + c * (b - a);
/*    */   }
/*    */   
/*    */   public static float lerp(float a, float b, float c) {
/* 76 */     return a + c * (b - a);
/*    */   }
/*    */   
/*    */   public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
/* 80 */     double d0 = x2 - x1;
/* 81 */     double d1 = y2 - y1;
/* 82 */     double d2 = z2 - z1;
/* 83 */     return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
/*    */   }
/*    */   
/*    */   public static double getDistance(double x, double y) {
/* 87 */     return Math.sqrt(Math.pow(x, 2.0D) + Math.pow(y, 2.0D));
/*    */   }
/*    */   
/*    */   public static double clamp(double min, double max, double n) {
/* 91 */     return Math.max(min, Math.min(max, n));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\MathUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */