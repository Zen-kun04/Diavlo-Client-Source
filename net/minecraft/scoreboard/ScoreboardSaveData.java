/*     */ package net.minecraft.scoreboard;
/*     */ 
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.nbt.NBTTagString;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.world.WorldSavedData;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ScoreboardSaveData extends WorldSavedData {
/*  13 */   private static final Logger logger = LogManager.getLogger();
/*     */   
/*     */   private Scoreboard theScoreboard;
/*     */   private NBTTagCompound delayedInitNbt;
/*     */   
/*     */   public ScoreboardSaveData() {
/*  19 */     this("scoreboard");
/*     */   }
/*     */ 
/*     */   
/*     */   public ScoreboardSaveData(String name) {
/*  24 */     super(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScoreboard(Scoreboard scoreboardIn) {
/*  29 */     this.theScoreboard = scoreboardIn;
/*     */     
/*  31 */     if (this.delayedInitNbt != null)
/*     */     {
/*  33 */       readFromNBT(this.delayedInitNbt);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound nbt) {
/*  39 */     if (this.theScoreboard == null) {
/*     */       
/*  41 */       this.delayedInitNbt = nbt;
/*     */     }
/*     */     else {
/*     */       
/*  45 */       readObjectives(nbt.getTagList("Objectives", 10));
/*  46 */       readScores(nbt.getTagList("PlayerScores", 10));
/*     */       
/*  48 */       if (nbt.hasKey("DisplaySlots", 10))
/*     */       {
/*  50 */         readDisplayConfig(nbt.getCompoundTag("DisplaySlots"));
/*     */       }
/*     */       
/*  53 */       if (nbt.hasKey("Teams", 9))
/*     */       {
/*  55 */         readTeams(nbt.getTagList("Teams", 10));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readTeams(NBTTagList p_96498_1_) {
/*  62 */     for (int i = 0; i < p_96498_1_.tagCount(); i++) {
/*     */       
/*  64 */       NBTTagCompound nbttagcompound = p_96498_1_.getCompoundTagAt(i);
/*  65 */       String s = nbttagcompound.getString("Name");
/*     */       
/*  67 */       if (s.length() > 16)
/*     */       {
/*  69 */         s = s.substring(0, 16);
/*     */       }
/*     */       
/*  72 */       ScorePlayerTeam scoreplayerteam = this.theScoreboard.createTeam(s);
/*  73 */       String s1 = nbttagcompound.getString("DisplayName");
/*     */       
/*  75 */       if (s1.length() > 32)
/*     */       {
/*  77 */         s1 = s1.substring(0, 32);
/*     */       }
/*     */       
/*  80 */       scoreplayerteam.setTeamName(s1);
/*     */       
/*  82 */       if (nbttagcompound.hasKey("TeamColor", 8))
/*     */       {
/*  84 */         scoreplayerteam.setChatFormat(EnumChatFormatting.getValueByName(nbttagcompound.getString("TeamColor")));
/*     */       }
/*     */       
/*  87 */       scoreplayerteam.setNamePrefix(nbttagcompound.getString("Prefix"));
/*  88 */       scoreplayerteam.setNameSuffix(nbttagcompound.getString("Suffix"));
/*     */       
/*  90 */       if (nbttagcompound.hasKey("AllowFriendlyFire", 99))
/*     */       {
/*  92 */         scoreplayerteam.setAllowFriendlyFire(nbttagcompound.getBoolean("AllowFriendlyFire"));
/*     */       }
/*     */       
/*  95 */       if (nbttagcompound.hasKey("SeeFriendlyInvisibles", 99))
/*     */       {
/*  97 */         scoreplayerteam.setSeeFriendlyInvisiblesEnabled(nbttagcompound.getBoolean("SeeFriendlyInvisibles"));
/*     */       }
/*     */       
/* 100 */       if (nbttagcompound.hasKey("NameTagVisibility", 8)) {
/*     */         
/* 102 */         Team.EnumVisible team$enumvisible = Team.EnumVisible.func_178824_a(nbttagcompound.getString("NameTagVisibility"));
/*     */         
/* 104 */         if (team$enumvisible != null)
/*     */         {
/* 106 */           scoreplayerteam.setNameTagVisibility(team$enumvisible);
/*     */         }
/*     */       } 
/*     */       
/* 110 */       if (nbttagcompound.hasKey("DeathMessageVisibility", 8)) {
/*     */         
/* 112 */         Team.EnumVisible team$enumvisible1 = Team.EnumVisible.func_178824_a(nbttagcompound.getString("DeathMessageVisibility"));
/*     */         
/* 114 */         if (team$enumvisible1 != null)
/*     */         {
/* 116 */           scoreplayerteam.setDeathMessageVisibility(team$enumvisible1);
/*     */         }
/*     */       } 
/*     */       
/* 120 */       func_96502_a(scoreplayerteam, nbttagcompound.getTagList("Players", 8));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_96502_a(ScorePlayerTeam p_96502_1_, NBTTagList p_96502_2_) {
/* 126 */     for (int i = 0; i < p_96502_2_.tagCount(); i++)
/*     */     {
/* 128 */       this.theScoreboard.addPlayerToTeam(p_96502_2_.getStringTagAt(i), p_96502_1_.getRegisteredName());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readDisplayConfig(NBTTagCompound p_96504_1_) {
/* 134 */     for (int i = 0; i < 19; i++) {
/*     */       
/* 136 */       if (p_96504_1_.hasKey("slot_" + i, 8)) {
/*     */         
/* 138 */         String s = p_96504_1_.getString("slot_" + i);
/* 139 */         ScoreObjective scoreobjective = this.theScoreboard.getObjective(s);
/* 140 */         this.theScoreboard.setObjectiveInDisplaySlot(i, scoreobjective);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readObjectives(NBTTagList nbt) {
/* 147 */     for (int i = 0; i < nbt.tagCount(); i++) {
/*     */       
/* 149 */       NBTTagCompound nbttagcompound = nbt.getCompoundTagAt(i);
/* 150 */       IScoreObjectiveCriteria iscoreobjectivecriteria = IScoreObjectiveCriteria.INSTANCES.get(nbttagcompound.getString("CriteriaName"));
/*     */       
/* 152 */       if (iscoreobjectivecriteria != null) {
/*     */         
/* 154 */         String s = nbttagcompound.getString("Name");
/*     */         
/* 156 */         if (s.length() > 16)
/*     */         {
/* 158 */           s = s.substring(0, 16);
/*     */         }
/*     */         
/* 161 */         ScoreObjective scoreobjective = this.theScoreboard.addScoreObjective(s, iscoreobjectivecriteria);
/* 162 */         scoreobjective.setDisplayName(nbttagcompound.getString("DisplayName"));
/* 163 */         scoreobjective.setRenderType(IScoreObjectiveCriteria.EnumRenderType.func_178795_a(nbttagcompound.getString("RenderType")));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readScores(NBTTagList nbt) {
/* 170 */     for (int i = 0; i < nbt.tagCount(); i++) {
/*     */       
/* 172 */       NBTTagCompound nbttagcompound = nbt.getCompoundTagAt(i);
/* 173 */       ScoreObjective scoreobjective = this.theScoreboard.getObjective(nbttagcompound.getString("Objective"));
/* 174 */       String s = nbttagcompound.getString("Name");
/*     */       
/* 176 */       if (s.length() > 40)
/*     */       {
/* 178 */         s = s.substring(0, 40);
/*     */       }
/*     */       
/* 181 */       Score score = this.theScoreboard.getValueFromObjective(s, scoreobjective);
/* 182 */       score.setScorePoints(nbttagcompound.getInteger("Score"));
/*     */       
/* 184 */       if (nbttagcompound.hasKey("Locked"))
/*     */       {
/* 186 */         score.setLocked(nbttagcompound.getBoolean("Locked"));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToNBT(NBTTagCompound nbt) {
/* 193 */     if (this.theScoreboard == null) {
/*     */       
/* 195 */       logger.warn("Tried to save scoreboard without having a scoreboard...");
/*     */     }
/*     */     else {
/*     */       
/* 199 */       nbt.setTag("Objectives", (NBTBase)objectivesToNbt());
/* 200 */       nbt.setTag("PlayerScores", (NBTBase)scoresToNbt());
/* 201 */       nbt.setTag("Teams", (NBTBase)func_96496_a());
/* 202 */       func_96497_d(nbt);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected NBTTagList func_96496_a() {
/* 208 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 210 */     for (ScorePlayerTeam scoreplayerteam : this.theScoreboard.getTeams()) {
/*     */       
/* 212 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 213 */       nbttagcompound.setString("Name", scoreplayerteam.getRegisteredName());
/* 214 */       nbttagcompound.setString("DisplayName", scoreplayerteam.getTeamName());
/*     */       
/* 216 */       if (scoreplayerteam.getChatFormat().getColorIndex() >= 0)
/*     */       {
/* 218 */         nbttagcompound.setString("TeamColor", scoreplayerteam.getChatFormat().getFriendlyName());
/*     */       }
/*     */       
/* 221 */       nbttagcompound.setString("Prefix", scoreplayerteam.getColorPrefix());
/* 222 */       nbttagcompound.setString("Suffix", scoreplayerteam.getColorSuffix());
/* 223 */       nbttagcompound.setBoolean("AllowFriendlyFire", scoreplayerteam.getAllowFriendlyFire());
/* 224 */       nbttagcompound.setBoolean("SeeFriendlyInvisibles", scoreplayerteam.getSeeFriendlyInvisiblesEnabled());
/* 225 */       nbttagcompound.setString("NameTagVisibility", (scoreplayerteam.getNameTagVisibility()).internalName);
/* 226 */       nbttagcompound.setString("DeathMessageVisibility", (scoreplayerteam.getDeathMessageVisibility()).internalName);
/* 227 */       NBTTagList nbttaglist1 = new NBTTagList();
/*     */       
/* 229 */       for (String s : scoreplayerteam.getMembershipCollection())
/*     */       {
/* 231 */         nbttaglist1.appendTag((NBTBase)new NBTTagString(s));
/*     */       }
/*     */       
/* 234 */       nbttagcompound.setTag("Players", (NBTBase)nbttaglist1);
/* 235 */       nbttaglist.appendTag((NBTBase)nbttagcompound);
/*     */     } 
/*     */     
/* 238 */     return nbttaglist;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_96497_d(NBTTagCompound p_96497_1_) {
/* 243 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 244 */     boolean flag = false;
/*     */     
/* 246 */     for (int i = 0; i < 19; i++) {
/*     */       
/* 248 */       ScoreObjective scoreobjective = this.theScoreboard.getObjectiveInDisplaySlot(i);
/*     */       
/* 250 */       if (scoreobjective != null) {
/*     */         
/* 252 */         nbttagcompound.setString("slot_" + i, scoreobjective.getName());
/* 253 */         flag = true;
/*     */       } 
/*     */     } 
/*     */     
/* 257 */     if (flag)
/*     */     {
/* 259 */       p_96497_1_.setTag("DisplaySlots", (NBTBase)nbttagcompound);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected NBTTagList objectivesToNbt() {
/* 265 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 267 */     for (ScoreObjective scoreobjective : this.theScoreboard.getScoreObjectives()) {
/*     */       
/* 269 */       if (scoreobjective.getCriteria() != null) {
/*     */         
/* 271 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 272 */         nbttagcompound.setString("Name", scoreobjective.getName());
/* 273 */         nbttagcompound.setString("CriteriaName", scoreobjective.getCriteria().getName());
/* 274 */         nbttagcompound.setString("DisplayName", scoreobjective.getDisplayName());
/* 275 */         nbttagcompound.setString("RenderType", scoreobjective.getRenderType().func_178796_a());
/* 276 */         nbttaglist.appendTag((NBTBase)nbttagcompound);
/*     */       } 
/*     */     } 
/*     */     
/* 280 */     return nbttaglist;
/*     */   }
/*     */ 
/*     */   
/*     */   protected NBTTagList scoresToNbt() {
/* 285 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 287 */     for (Score score : this.theScoreboard.getScores()) {
/*     */       
/* 289 */       if (score.getObjective() != null) {
/*     */         
/* 291 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 292 */         nbttagcompound.setString("Name", score.getPlayerName());
/* 293 */         nbttagcompound.setString("Objective", score.getObjective().getName());
/* 294 */         nbttagcompound.setInteger("Score", score.getScorePoints());
/* 295 */         nbttagcompound.setBoolean("Locked", score.isLocked());
/* 296 */         nbttaglist.appendTag((NBTBase)nbttagcompound);
/*     */       } 
/*     */     } 
/*     */     
/* 300 */     return nbttaglist;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\scoreboard\ScoreboardSaveData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */