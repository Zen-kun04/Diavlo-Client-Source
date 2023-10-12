/*     */ package net.minecraft.scoreboard;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Scoreboard
/*     */ {
/*  16 */   private final Map<String, ScoreObjective> scoreObjectives = Maps.newHashMap();
/*  17 */   private final Map<IScoreObjectiveCriteria, List<ScoreObjective>> scoreObjectiveCriterias = Maps.newHashMap();
/*  18 */   private final Map<String, Map<ScoreObjective, Score>> entitiesScoreObjectives = Maps.newHashMap();
/*  19 */   private final ScoreObjective[] objectiveDisplaySlots = new ScoreObjective[19];
/*  20 */   private final Map<String, ScorePlayerTeam> teams = Maps.newHashMap();
/*  21 */   private final Map<String, ScorePlayerTeam> teamMemberships = Maps.newHashMap();
/*  22 */   private static String[] field_178823_g = null;
/*     */ 
/*     */   
/*     */   public ScoreObjective getObjective(String name) {
/*  26 */     return this.scoreObjectives.get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public ScoreObjective addScoreObjective(String name, IScoreObjectiveCriteria criteria) {
/*  31 */     if (name.length() > 16)
/*     */     {
/*  33 */       throw new IllegalArgumentException("The objective name '" + name + "' is too long!");
/*     */     }
/*     */ 
/*     */     
/*  37 */     ScoreObjective scoreobjective = getObjective(name);
/*     */     
/*  39 */     if (scoreobjective != null)
/*     */     {
/*  41 */       throw new IllegalArgumentException("An objective with the name '" + name + "' already exists!");
/*     */     }
/*     */ 
/*     */     
/*  45 */     scoreobjective = new ScoreObjective(this, name, criteria);
/*  46 */     List<ScoreObjective> list = this.scoreObjectiveCriterias.get(criteria);
/*     */     
/*  48 */     if (list == null) {
/*     */       
/*  50 */       list = Lists.newArrayList();
/*  51 */       this.scoreObjectiveCriterias.put(criteria, list);
/*     */     } 
/*     */     
/*  54 */     list.add(scoreobjective);
/*  55 */     this.scoreObjectives.put(name, scoreobjective);
/*  56 */     onScoreObjectiveAdded(scoreobjective);
/*  57 */     return scoreobjective;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<ScoreObjective> getObjectivesFromCriteria(IScoreObjectiveCriteria criteria) {
/*  64 */     Collection<ScoreObjective> collection = this.scoreObjectiveCriterias.get(criteria);
/*  65 */     return (collection == null) ? Lists.newArrayList() : Lists.newArrayList(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean entityHasObjective(String name, ScoreObjective p_178819_2_) {
/*  70 */     Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.get(name);
/*     */     
/*  72 */     if (map == null)
/*     */     {
/*  74 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  78 */     Score score = map.get(p_178819_2_);
/*  79 */     return (score != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Score getValueFromObjective(String name, ScoreObjective objective) {
/*  85 */     if (name.length() > 40)
/*     */     {
/*  87 */       throw new IllegalArgumentException("The player name '" + name + "' is too long!");
/*     */     }
/*     */ 
/*     */     
/*  91 */     Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.get(name);
/*     */     
/*  93 */     if (map == null) {
/*     */       
/*  95 */       map = Maps.newHashMap();
/*  96 */       this.entitiesScoreObjectives.put(name, map);
/*     */     } 
/*     */     
/*  99 */     Score score = map.get(objective);
/*     */     
/* 101 */     if (score == null) {
/*     */       
/* 103 */       score = new Score(this, objective, name);
/* 104 */       map.put(objective, score);
/*     */     } 
/*     */     
/* 107 */     return score;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<Score> getSortedScores(ScoreObjective objective) {
/* 113 */     List<Score> list = Lists.newArrayList();
/*     */     
/* 115 */     for (Map<ScoreObjective, Score> map : this.entitiesScoreObjectives.values()) {
/*     */       
/* 117 */       Score score = map.get(objective);
/*     */       
/* 119 */       if (score != null)
/*     */       {
/* 121 */         list.add(score);
/*     */       }
/*     */     } 
/*     */     
/* 125 */     Collections.sort(list, Score.scoreComparator);
/* 126 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<ScoreObjective> getScoreObjectives() {
/* 131 */     return this.scoreObjectives.values();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getObjectiveNames() {
/* 136 */     return this.entitiesScoreObjectives.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeObjectiveFromEntity(String name, ScoreObjective objective) {
/* 141 */     if (objective == null) {
/*     */       
/* 143 */       Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.remove(name);
/*     */       
/* 145 */       if (map != null)
/*     */       {
/* 147 */         func_96516_a(name);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 152 */       Map<ScoreObjective, Score> map2 = this.entitiesScoreObjectives.get(name);
/*     */       
/* 154 */       if (map2 != null) {
/*     */         
/* 156 */         Score score = map2.remove(objective);
/*     */         
/* 158 */         if (map2.size() < 1) {
/*     */           
/* 160 */           Map<ScoreObjective, Score> map1 = this.entitiesScoreObjectives.remove(name);
/*     */           
/* 162 */           if (map1 != null)
/*     */           {
/* 164 */             func_96516_a(name);
/*     */           }
/*     */         }
/* 167 */         else if (score != null) {
/*     */           
/* 169 */           func_178820_a(name, objective);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<Score> getScores() {
/* 177 */     Collection<Map<ScoreObjective, Score>> collection = this.entitiesScoreObjectives.values();
/* 178 */     List<Score> list = Lists.newArrayList();
/*     */     
/* 180 */     for (Map<ScoreObjective, Score> map : collection)
/*     */     {
/* 182 */       list.addAll(map.values());
/*     */     }
/*     */     
/* 185 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<ScoreObjective, Score> getObjectivesForEntity(String name) {
/* 190 */     Map<ScoreObjective, Score> map = this.entitiesScoreObjectives.get(name);
/*     */     
/* 192 */     if (map == null)
/*     */     {
/* 194 */       map = Maps.newHashMap();
/*     */     }
/*     */     
/* 197 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeObjective(ScoreObjective p_96519_1_) {
/* 202 */     this.scoreObjectives.remove(p_96519_1_.getName());
/*     */     
/* 204 */     for (int i = 0; i < 19; i++) {
/*     */       
/* 206 */       if (getObjectiveInDisplaySlot(i) == p_96519_1_)
/*     */       {
/* 208 */         setObjectiveInDisplaySlot(i, (ScoreObjective)null);
/*     */       }
/*     */     } 
/*     */     
/* 212 */     List<ScoreObjective> list = this.scoreObjectiveCriterias.get(p_96519_1_.getCriteria());
/*     */     
/* 214 */     if (list != null)
/*     */     {
/* 216 */       list.remove(p_96519_1_);
/*     */     }
/*     */     
/* 219 */     for (Map<ScoreObjective, Score> map : this.entitiesScoreObjectives.values())
/*     */     {
/* 221 */       map.remove(p_96519_1_);
/*     */     }
/*     */     
/* 224 */     onScoreObjectiveRemoved(p_96519_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setObjectiveInDisplaySlot(int p_96530_1_, ScoreObjective p_96530_2_) {
/* 229 */     this.objectiveDisplaySlots[p_96530_1_] = p_96530_2_;
/*     */   }
/*     */ 
/*     */   
/*     */   public ScoreObjective getObjectiveInDisplaySlot(int p_96539_1_) {
/* 234 */     return this.objectiveDisplaySlots[p_96539_1_];
/*     */   }
/*     */ 
/*     */   
/*     */   public ScorePlayerTeam getTeam(String p_96508_1_) {
/* 239 */     return this.teams.get(p_96508_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public ScorePlayerTeam createTeam(String name) {
/* 244 */     if (name.length() > 16)
/*     */     {
/* 246 */       throw new IllegalArgumentException("The team name '" + name + "' is too long!");
/*     */     }
/*     */ 
/*     */     
/* 250 */     ScorePlayerTeam scoreplayerteam = getTeam(name);
/*     */     
/* 252 */     if (scoreplayerteam != null)
/*     */     {
/* 254 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 259 */     scoreplayerteam = new ScorePlayerTeam(this, name);
/* 260 */     this.teams.put(name, scoreplayerteam);
/* 261 */     broadcastTeamCreated(scoreplayerteam);
/* 262 */     return scoreplayerteam;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeTeam(ScorePlayerTeam p_96511_1_) {
/* 277 */     func_96513_c(p_96511_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addPlayerToTeam(String player, String newTeam) {
/* 282 */     if (player.length() > 40)
/*     */     {
/* 284 */       throw new IllegalArgumentException("The player name '" + player + "' is too long!");
/*     */     }
/* 286 */     if (!this.teams.containsKey(newTeam))
/*     */     {
/* 288 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 292 */     ScorePlayerTeam scoreplayerteam = getTeam(newTeam);
/*     */     
/* 294 */     if (getPlayersTeam(player) != null)
/*     */     {
/* 296 */       removePlayerFromTeams(player);
/*     */     }
/*     */     
/* 299 */     this.teamMemberships.put(player, scoreplayerteam);
/* 300 */     scoreplayerteam.getMembershipCollection().add(player);
/* 301 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removePlayerFromTeams(String p_96524_1_) {
/* 307 */     ScorePlayerTeam scoreplayerteam = getPlayersTeam(p_96524_1_);
/*     */     
/* 309 */     if (scoreplayerteam != null) {
/*     */       
/* 311 */       removePlayerFromTeam(p_96524_1_, scoreplayerteam);
/* 312 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 316 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removePlayerFromTeam(String p_96512_1_, ScorePlayerTeam p_96512_2_) {
/* 322 */     if (getPlayersTeam(p_96512_1_) == p_96512_2_) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 328 */       this.teamMemberships.remove(p_96512_1_);
/* 329 */       p_96512_2_.getMembershipCollection().remove(p_96512_1_);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<String> getTeamNames() {
/* 335 */     return this.teams.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<ScorePlayerTeam> getTeams() {
/* 340 */     return this.teams.values();
/*     */   }
/*     */ 
/*     */   
/*     */   public ScorePlayerTeam getPlayersTeam(String p_96509_1_) {
/* 345 */     return this.teamMemberships.get(p_96509_1_);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onScoreObjectiveAdded(ScoreObjective scoreObjectiveIn) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onObjectiveDisplayNameChanged(ScoreObjective p_96532_1_) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onScoreObjectiveRemoved(ScoreObjective p_96533_1_) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_96536_a(Score p_96536_1_) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_96516_a(String p_96516_1_) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_178820_a(String p_178820_1_, ScoreObjective p_178820_2_) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void broadcastTeamCreated(ScorePlayerTeam playerTeam) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendTeamUpdate(ScorePlayerTeam playerTeam) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_96513_c(ScorePlayerTeam playerTeam) {}
/*     */ 
/*     */   
/*     */   public static String getObjectiveDisplaySlot(int p_96517_0_) {
/* 386 */     switch (p_96517_0_) {
/*     */       
/*     */       case 0:
/* 389 */         return "list";
/*     */       
/*     */       case 1:
/* 392 */         return "sidebar";
/*     */       
/*     */       case 2:
/* 395 */         return "belowName";
/*     */     } 
/*     */     
/* 398 */     if (p_96517_0_ >= 3 && p_96517_0_ <= 18) {
/*     */       
/* 400 */       EnumChatFormatting enumchatformatting = EnumChatFormatting.func_175744_a(p_96517_0_ - 3);
/*     */       
/* 402 */       if (enumchatformatting != null && enumchatformatting != EnumChatFormatting.RESET)
/*     */       {
/* 404 */         return "sidebar.team." + enumchatformatting.getFriendlyName();
/*     */       }
/*     */     } 
/*     */     
/* 408 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getObjectiveDisplaySlotNumber(String p_96537_0_) {
/* 414 */     if (p_96537_0_.equalsIgnoreCase("list"))
/*     */     {
/* 416 */       return 0;
/*     */     }
/* 418 */     if (p_96537_0_.equalsIgnoreCase("sidebar"))
/*     */     {
/* 420 */       return 1;
/*     */     }
/* 422 */     if (p_96537_0_.equalsIgnoreCase("belowName"))
/*     */     {
/* 424 */       return 2;
/*     */     }
/*     */ 
/*     */     
/* 428 */     if (p_96537_0_.startsWith("sidebar.team.")) {
/*     */       
/* 430 */       String s = p_96537_0_.substring("sidebar.team.".length());
/* 431 */       EnumChatFormatting enumchatformatting = EnumChatFormatting.getValueByName(s);
/*     */       
/* 433 */       if (enumchatformatting != null && enumchatformatting.getColorIndex() >= 0)
/*     */       {
/* 435 */         return enumchatformatting.getColorIndex() + 3;
/*     */       }
/*     */     } 
/*     */     
/* 439 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] getDisplaySlotStrings() {
/* 445 */     if (field_178823_g == null) {
/*     */       
/* 447 */       field_178823_g = new String[19];
/*     */       
/* 449 */       for (int i = 0; i < 19; i++)
/*     */       {
/* 451 */         field_178823_g[i] = getObjectiveDisplaySlot(i);
/*     */       }
/*     */     } 
/*     */     
/* 455 */     return field_178823_g;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_181140_a(Entity p_181140_1_) {
/* 460 */     if (p_181140_1_ != null && !(p_181140_1_ instanceof net.minecraft.entity.player.EntityPlayer) && !p_181140_1_.isEntityAlive()) {
/*     */       
/* 462 */       String s = p_181140_1_.getUniqueID().toString();
/* 463 */       removeObjectiveFromEntity(s, (ScoreObjective)null);
/* 464 */       removePlayerFromTeams(s);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\scoreboard\Scoreboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */