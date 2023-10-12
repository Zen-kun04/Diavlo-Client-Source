/*     */ package net.minecraft.scoreboard;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ 
/*     */ public class ScorePlayerTeam
/*     */   extends Team {
/*     */   private final Scoreboard theScoreboard;
/*     */   private final String registeredName;
/*  12 */   private final Set<String> membershipSet = Sets.newHashSet();
/*     */   private String teamNameSPT;
/*  14 */   private String namePrefixSPT = "";
/*  15 */   private String colorSuffix = "";
/*     */   private boolean allowFriendlyFire = true;
/*     */   private boolean canSeeFriendlyInvisibles = true;
/*  18 */   private Team.EnumVisible nameTagVisibility = Team.EnumVisible.ALWAYS;
/*  19 */   private Team.EnumVisible deathMessageVisibility = Team.EnumVisible.ALWAYS;
/*  20 */   private EnumChatFormatting chatFormat = EnumChatFormatting.RESET;
/*     */ 
/*     */   
/*     */   public ScorePlayerTeam(Scoreboard theScoreboardIn, String name) {
/*  24 */     this.theScoreboard = theScoreboardIn;
/*  25 */     this.registeredName = name;
/*  26 */     this.teamNameSPT = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRegisteredName() {
/*  31 */     return this.registeredName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTeamName() {
/*  36 */     return this.teamNameSPT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTeamName(String name) {
/*  41 */     if (name == null)
/*     */     {
/*  43 */       throw new IllegalArgumentException("Name cannot be null");
/*     */     }
/*     */ 
/*     */     
/*  47 */     this.teamNameSPT = name;
/*  48 */     this.theScoreboard.sendTeamUpdate(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getMembershipCollection() {
/*  54 */     return this.membershipSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getColorPrefix() {
/*  59 */     return this.namePrefixSPT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNamePrefix(String prefix) {
/*  64 */     if (prefix == null)
/*     */     {
/*  66 */       throw new IllegalArgumentException("Prefix cannot be null");
/*     */     }
/*     */ 
/*     */     
/*  70 */     this.namePrefixSPT = prefix;
/*  71 */     this.theScoreboard.sendTeamUpdate(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getColorSuffix() {
/*  77 */     return this.colorSuffix;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNameSuffix(String suffix) {
/*  82 */     this.colorSuffix = suffix;
/*  83 */     this.theScoreboard.sendTeamUpdate(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public String formatString(String input) {
/*  88 */     return getColorPrefix() + input + getColorSuffix();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String formatPlayerName(Team p_96667_0_, String p_96667_1_) {
/*  93 */     return (p_96667_0_ == null) ? p_96667_1_ : p_96667_0_.formatString(p_96667_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getAllowFriendlyFire() {
/*  98 */     return this.allowFriendlyFire;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAllowFriendlyFire(boolean friendlyFire) {
/* 103 */     this.allowFriendlyFire = friendlyFire;
/* 104 */     this.theScoreboard.sendTeamUpdate(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getSeeFriendlyInvisiblesEnabled() {
/* 109 */     return this.canSeeFriendlyInvisibles;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSeeFriendlyInvisiblesEnabled(boolean friendlyInvisibles) {
/* 114 */     this.canSeeFriendlyInvisibles = friendlyInvisibles;
/* 115 */     this.theScoreboard.sendTeamUpdate(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Team.EnumVisible getNameTagVisibility() {
/* 120 */     return this.nameTagVisibility;
/*     */   }
/*     */ 
/*     */   
/*     */   public Team.EnumVisible getDeathMessageVisibility() {
/* 125 */     return this.deathMessageVisibility;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNameTagVisibility(Team.EnumVisible p_178772_1_) {
/* 130 */     this.nameTagVisibility = p_178772_1_;
/* 131 */     this.theScoreboard.sendTeamUpdate(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDeathMessageVisibility(Team.EnumVisible p_178773_1_) {
/* 136 */     this.deathMessageVisibility = p_178773_1_;
/* 137 */     this.theScoreboard.sendTeamUpdate(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int func_98299_i() {
/* 142 */     int i = 0;
/*     */     
/* 144 */     if (getAllowFriendlyFire())
/*     */     {
/* 146 */       i |= 0x1;
/*     */     }
/*     */     
/* 149 */     if (getSeeFriendlyInvisiblesEnabled())
/*     */     {
/* 151 */       i |= 0x2;
/*     */     }
/*     */     
/* 154 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_98298_a(int p_98298_1_) {
/* 159 */     setAllowFriendlyFire(((p_98298_1_ & 0x1) > 0));
/* 160 */     setSeeFriendlyInvisiblesEnabled(((p_98298_1_ & 0x2) > 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChatFormat(EnumChatFormatting p_178774_1_) {
/* 165 */     this.chatFormat = p_178774_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumChatFormatting getChatFormat() {
/* 170 */     return this.chatFormat;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\scoreboard\ScorePlayerTeam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */