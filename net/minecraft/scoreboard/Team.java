/*    */ package net.minecraft.scoreboard;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public abstract class Team
/*    */ {
/*    */   public boolean isSameTeam(Team other) {
/* 11 */     return (other == null) ? false : ((this == other));
/*    */   }
/*    */   
/*    */   public abstract String getRegisteredName();
/*    */   
/*    */   public abstract String formatString(String paramString);
/*    */   
/*    */   public abstract boolean getSeeFriendlyInvisiblesEnabled();
/*    */   
/*    */   public abstract boolean getAllowFriendlyFire();
/*    */   
/*    */   public abstract EnumVisible getNameTagVisibility();
/*    */   
/*    */   public abstract Collection<String> getMembershipCollection();
/*    */   
/*    */   public abstract EnumVisible getDeathMessageVisibility();
/*    */   
/*    */   public enum EnumVisible
/*    */   {
/* 30 */     ALWAYS("always", 0),
/* 31 */     NEVER("never", 1),
/* 32 */     HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
/* 33 */     HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);
/*    */     
/* 35 */     private static Map<String, EnumVisible> field_178828_g = Maps.newHashMap();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public final String internalName;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public final int id;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     static {
/* 56 */       for (EnumVisible team$enumvisible : values())
/*    */       {
/* 58 */         field_178828_g.put(team$enumvisible.internalName, team$enumvisible);
/*    */       }
/*    */     }
/*    */     
/*    */     public static String[] func_178825_a() {
/*    */       return (String[])field_178828_g.keySet().toArray((Object[])new String[field_178828_g.size()]);
/*    */     }
/*    */     
/*    */     public static EnumVisible func_178824_a(String p_178824_0_) {
/*    */       return field_178828_g.get(p_178824_0_);
/*    */     }
/*    */     
/*    */     EnumVisible(String p_i45550_3_, int p_i45550_4_) {
/*    */       this.internalName = p_i45550_3_;
/*    */       this.id = p_i45550_4_;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\scoreboard\Team.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */