/*    */ package net.optifine.util;
/*    */ 
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ 
/*    */ public class MathUtilsTest
/*    */ {
/*    */   public static void main(String[] args) throws Exception {
/*  9 */     OPER[] amathutilstest$oper = OPER.values();
/*    */     
/* 11 */     for (int i = 0; i < amathutilstest$oper.length; i++) {
/*    */       
/* 13 */       OPER mathutilstest$oper = amathutilstest$oper[i];
/* 14 */       dbg("******** " + mathutilstest$oper + " ***********");
/* 15 */       test(mathutilstest$oper, false);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void test(OPER oper, boolean fast) {
/*    */     double d0, d1;
/* 21 */     MathHelper.fastMath = fast;
/*    */ 
/*    */ 
/*    */     
/* 25 */     switch (oper) {
/*    */       
/*    */       case SIN:
/*    */       case COS:
/* 29 */         d0 = -MathHelper.PI;
/* 30 */         d1 = MathHelper.PI;
/*    */         break;
/*    */       
/*    */       case ASIN:
/*    */       case ACOS:
/* 35 */         d0 = -1.0D;
/* 36 */         d1 = 1.0D;
/*    */         break;
/*    */       
/*    */       default:
/*    */         return;
/*    */     } 
/*    */     
/* 43 */     int i = 10;
/*    */     
/* 45 */     for (int j = 0; j <= i; j++) {
/*    */       float f, f1;
/* 47 */       double d2 = d0 + j * (d1 - d0) / i;
/*    */ 
/*    */ 
/*    */       
/* 51 */       switch (oper) {
/*    */         
/*    */         case SIN:
/* 54 */           f = (float)Math.sin(d2);
/* 55 */           f1 = MathHelper.sin((float)d2);
/*    */           break;
/*    */         
/*    */         case COS:
/* 59 */           f = (float)Math.cos(d2);
/* 60 */           f1 = MathHelper.cos((float)d2);
/*    */           break;
/*    */         
/*    */         case ASIN:
/* 64 */           f = (float)Math.asin(d2);
/* 65 */           f1 = MathUtils.asin((float)d2);
/*    */           break;
/*    */         
/*    */         case ACOS:
/* 69 */           f = (float)Math.acos(d2);
/* 70 */           f1 = MathUtils.acos((float)d2);
/*    */           break;
/*    */         
/*    */         default:
/*    */           return;
/*    */       } 
/*    */       
/* 77 */       dbg(String.format("%.2f, Math: %f, Helper: %f, diff: %f", new Object[] { Double.valueOf(d2), Float.valueOf(f), Float.valueOf(f1), Float.valueOf(Math.abs(f - f1)) }));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static void dbg(String str) {
/* 83 */     System.out.println(str);
/*    */   }
/*    */   
/*    */   private enum OPER
/*    */   {
/* 88 */     SIN,
/* 89 */     COS,
/* 90 */     ASIN,
/* 91 */     ACOS;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifin\\util\MathUtilsTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */