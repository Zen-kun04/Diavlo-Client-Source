/*    */ package net.minecraft.entity.boss;
/*    */ 
/*    */ 
/*    */ public final class BossStatus
/*    */ {
/*    */   public static float healthScale;
/*    */   public static int statusBarTime;
/*    */   public static String bossName;
/*    */   public static boolean hasColorModifier;
/*    */   
/*    */   public static void setBossStatus(IBossDisplayData displayData, boolean hasColorModifierIn) {
/* 12 */     healthScale = displayData.getHealth() / displayData.getMaxHealth();
/* 13 */     statusBarTime = 100;
/* 14 */     bossName = displayData.getDisplayName().getFormattedText();
/* 15 */     hasColorModifier = hasColorModifierIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\boss\BossStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */