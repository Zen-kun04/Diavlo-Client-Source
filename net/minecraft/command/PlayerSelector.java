/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.collect.ComparisonChain;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.scoreboard.Score;
/*     */ import net.minecraft.scoreboard.ScoreObjective;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.scoreboard.Team;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldSettings;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerSelector
/*     */ {
/*  39 */   private static final Pattern tokenPattern = Pattern.compile("^@([pare])(?:\\[([\\w=,!-]*)\\])?$");
/*  40 */   private static final Pattern intListPattern = Pattern.compile("\\G([-!]?[\\w-]*)(?:$|,)");
/*  41 */   private static final Pattern keyValueListPattern = Pattern.compile("\\G(\\w+)=([-!]?[\\w-]*)(?:$|,)");
/*  42 */   private static final Set<String> WORLD_BINDING_ARGS = Sets.newHashSet((Object[])new String[] { "x", "y", "z", "dx", "dy", "dz", "rm", "r" });
/*     */ 
/*     */   
/*     */   public static EntityPlayerMP matchOnePlayer(ICommandSender sender, String token) {
/*  46 */     return matchOneEntity(sender, token, EntityPlayerMP.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends Entity> T matchOneEntity(ICommandSender sender, String token, Class<? extends T> targetClass) {
/*  51 */     List<T> list = matchEntities(sender, token, targetClass);
/*  52 */     return (list.size() == 1) ? list.get(0) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static IChatComponent matchEntitiesToChatComponent(ICommandSender sender, String token) {
/*  57 */     List<Entity> list = matchEntities(sender, token, Entity.class);
/*     */     
/*  59 */     if (list.isEmpty())
/*     */     {
/*  61 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  65 */     List<IChatComponent> list1 = Lists.newArrayList();
/*     */     
/*  67 */     for (Entity entity : list)
/*     */     {
/*  69 */       list1.add(entity.getDisplayName());
/*     */     }
/*     */     
/*  72 */     return CommandBase.join(list1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Entity> List<T> matchEntities(ICommandSender sender, String token, Class<? extends T> targetClass) {
/*  78 */     Matcher matcher = tokenPattern.matcher(token);
/*     */     
/*  80 */     if (matcher.matches() && sender.canCommandSenderUseCommand(1, "@")) {
/*     */       
/*  82 */       Map<String, String> map = getArgumentMap(matcher.group(2));
/*     */       
/*  84 */       if (!isEntityTypeValid(sender, map))
/*     */       {
/*  86 */         return Collections.emptyList();
/*     */       }
/*     */ 
/*     */       
/*  90 */       String s = matcher.group(1);
/*  91 */       BlockPos blockpos = func_179664_b(map, sender.getPosition());
/*  92 */       List<World> list = getWorlds(sender, map);
/*  93 */       List<T> list1 = Lists.newArrayList();
/*     */       
/*  95 */       for (World world : list) {
/*     */         
/*  97 */         if (world != null) {
/*     */           
/*  99 */           List<Predicate<Entity>> list2 = Lists.newArrayList();
/* 100 */           list2.addAll(func_179663_a(map, s));
/* 101 */           list2.addAll(getXpLevelPredicates(map));
/* 102 */           list2.addAll(getGamemodePredicates(map));
/* 103 */           list2.addAll(getTeamPredicates(map));
/* 104 */           list2.addAll(getScorePredicates(map));
/* 105 */           list2.addAll(getNamePredicates(map));
/* 106 */           list2.addAll(func_180698_a(map, blockpos));
/* 107 */           list2.addAll(getRotationsPredicates(map));
/* 108 */           list1.addAll(filterResults(map, targetClass, list2, s, world, blockpos));
/*     */         } 
/*     */       } 
/*     */       
/* 112 */       return func_179658_a(list1, map, sender, targetClass, s, blockpos);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 117 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<World> getWorlds(ICommandSender sender, Map<String, String> argumentMap) {
/* 123 */     List<World> list = Lists.newArrayList();
/*     */     
/* 125 */     if (func_179665_h(argumentMap)) {
/*     */       
/* 127 */       list.add(sender.getEntityWorld());
/*     */     }
/*     */     else {
/*     */       
/* 131 */       Collections.addAll(list, (Object[])(MinecraftServer.getServer()).worldServers);
/*     */     } 
/*     */     
/* 134 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T extends Entity> boolean isEntityTypeValid(ICommandSender commandSender, Map<String, String> params) {
/* 139 */     String s = func_179651_b(params, "type");
/* 140 */     s = (s != null && s.startsWith("!")) ? s.substring(1) : s;
/*     */     
/* 142 */     if (s != null && !EntityList.isStringValidEntityName(s)) {
/*     */       
/* 144 */       ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.generic.entity.invalidType", new Object[] { s });
/* 145 */       chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.RED);
/* 146 */       commandSender.addChatMessage((IChatComponent)chatcomponenttranslation);
/* 147 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 151 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<Predicate<Entity>> func_179663_a(Map<String, String> p_179663_0_, String p_179663_1_) {
/* 157 */     List<Predicate<Entity>> list = Lists.newArrayList();
/* 158 */     String s = func_179651_b(p_179663_0_, "type");
/* 159 */     final boolean flag = (s != null && s.startsWith("!"));
/*     */     
/* 161 */     if (flag)
/*     */     {
/* 163 */       s = s.substring(1);
/*     */     }
/*     */     
/* 166 */     boolean flag1 = !p_179663_1_.equals("e");
/* 167 */     boolean flag2 = (p_179663_1_.equals("r") && s != null);
/*     */     
/* 169 */     if ((s == null || !p_179663_1_.equals("e")) && !flag2) {
/*     */       
/* 171 */       if (flag1)
/*     */       {
/* 173 */         list.add(new Predicate<Entity>()
/*     */             {
/*     */               public boolean apply(Entity p_apply_1_)
/*     */               {
/* 177 */                 return p_apply_1_ instanceof net.minecraft.entity.player.EntityPlayer;
/*     */               }
/*     */             });
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 184 */       final String s_f = s;
/* 185 */       list.add(new Predicate<Entity>()
/*     */           {
/*     */             public boolean apply(Entity p_apply_1_)
/*     */             {
/* 189 */               return (EntityList.isStringEntityName(p_apply_1_, s_f) != flag);
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/* 194 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Predicate<Entity>> getXpLevelPredicates(Map<String, String> p_179648_0_) {
/* 199 */     List<Predicate<Entity>> list = Lists.newArrayList();
/* 200 */     final int i = parseIntWithDefault(p_179648_0_, "lm", -1);
/* 201 */     final int j = parseIntWithDefault(p_179648_0_, "l", -1);
/*     */     
/* 203 */     if (i > -1 || j > -1)
/*     */     {
/* 205 */       list.add(new Predicate<Entity>()
/*     */           {
/*     */             public boolean apply(Entity p_apply_1_)
/*     */             {
/* 209 */               if (!(p_apply_1_ instanceof EntityPlayerMP))
/*     */               {
/* 211 */                 return false;
/*     */               }
/*     */ 
/*     */               
/* 215 */               EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
/* 216 */               return ((i <= -1 || entityplayermp.experienceLevel >= i) && (j <= -1 || entityplayermp.experienceLevel <= j));
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/* 222 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Predicate<Entity>> getGamemodePredicates(Map<String, String> p_179649_0_) {
/* 227 */     List<Predicate<Entity>> list = Lists.newArrayList();
/* 228 */     final int i = parseIntWithDefault(p_179649_0_, "m", WorldSettings.GameType.NOT_SET.getID());
/*     */     
/* 230 */     if (i != WorldSettings.GameType.NOT_SET.getID())
/*     */     {
/* 232 */       list.add(new Predicate<Entity>()
/*     */           {
/*     */             public boolean apply(Entity p_apply_1_)
/*     */             {
/* 236 */               if (!(p_apply_1_ instanceof EntityPlayerMP))
/*     */               {
/* 238 */                 return false;
/*     */               }
/*     */ 
/*     */               
/* 242 */               EntityPlayerMP entityplayermp = (EntityPlayerMP)p_apply_1_;
/* 243 */               return (entityplayermp.theItemInWorldManager.getGameType().getID() == i);
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/* 249 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Predicate<Entity>> getTeamPredicates(Map<String, String> p_179659_0_) {
/* 254 */     List<Predicate<Entity>> list = Lists.newArrayList();
/* 255 */     String s = func_179651_b(p_179659_0_, "team");
/* 256 */     final boolean flag = (s != null && s.startsWith("!"));
/*     */     
/* 258 */     if (flag)
/*     */     {
/* 260 */       s = s.substring(1);
/*     */     }
/*     */     
/* 263 */     if (s != null) {
/*     */       
/* 265 */       final String s_f = s;
/* 266 */       list.add(new Predicate<Entity>()
/*     */           {
/*     */             public boolean apply(Entity p_apply_1_)
/*     */             {
/* 270 */               if (!(p_apply_1_ instanceof EntityLivingBase))
/*     */               {
/* 272 */                 return false;
/*     */               }
/*     */ 
/*     */               
/* 276 */               EntityLivingBase entitylivingbase = (EntityLivingBase)p_apply_1_;
/* 277 */               Team team = entitylivingbase.getTeam();
/* 278 */               String s1 = (team == null) ? "" : team.getRegisteredName();
/* 279 */               return (s1.equals(s_f) != flag);
/*     */             }
/*     */           });
/*     */     } 
/*     */ 
/*     */     
/* 285 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Predicate<Entity>> getScorePredicates(Map<String, String> p_179657_0_) {
/* 290 */     List<Predicate<Entity>> list = Lists.newArrayList();
/* 291 */     final Map<String, Integer> map = func_96560_a(p_179657_0_);
/*     */     
/* 293 */     if (map != null && map.size() > 0)
/*     */     {
/* 295 */       list.add(new Predicate<Entity>()
/*     */           {
/*     */             public boolean apply(Entity p_apply_1_)
/*     */             {
/* 299 */               Scoreboard scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
/*     */               
/* 301 */               for (Map.Entry<String, Integer> entry : (Iterable<Map.Entry<String, Integer>>)map.entrySet()) {
/*     */                 
/* 303 */                 String s = entry.getKey();
/* 304 */                 boolean flag = false;
/*     */                 
/* 306 */                 if (s.endsWith("_min") && s.length() > 4) {
/*     */                   
/* 308 */                   flag = true;
/* 309 */                   s = s.substring(0, s.length() - 4);
/*     */                 } 
/*     */                 
/* 312 */                 ScoreObjective scoreobjective = scoreboard.getObjective(s);
/*     */                 
/* 314 */                 if (scoreobjective == null)
/*     */                 {
/* 316 */                   return false;
/*     */                 }
/*     */                 
/* 319 */                 String s1 = (p_apply_1_ instanceof EntityPlayerMP) ? p_apply_1_.getName() : p_apply_1_.getUniqueID().toString();
/*     */                 
/* 321 */                 if (!scoreboard.entityHasObjective(s1, scoreobjective))
/*     */                 {
/* 323 */                   return false;
/*     */                 }
/*     */                 
/* 326 */                 Score score = scoreboard.getValueFromObjective(s1, scoreobjective);
/* 327 */                 int i = score.getScorePoints();
/*     */                 
/* 329 */                 if (i < ((Integer)entry.getValue()).intValue() && flag)
/*     */                 {
/* 331 */                   return false;
/*     */                 }
/*     */                 
/* 334 */                 if (i > ((Integer)entry.getValue()).intValue() && !flag)
/*     */                 {
/* 336 */                   return false;
/*     */                 }
/*     */               } 
/*     */               
/* 340 */               return true;
/*     */             }
/*     */           });
/*     */     }
/*     */     
/* 345 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Predicate<Entity>> getNamePredicates(Map<String, String> p_179647_0_) {
/* 350 */     List<Predicate<Entity>> list = Lists.newArrayList();
/* 351 */     String s = func_179651_b(p_179647_0_, "name");
/* 352 */     final boolean flag = (s != null && s.startsWith("!"));
/*     */     
/* 354 */     if (flag)
/*     */     {
/* 356 */       s = s.substring(1);
/*     */     }
/*     */     
/* 359 */     if (s != null) {
/*     */       
/* 361 */       final String s_f = s;
/* 362 */       list.add(new Predicate<Entity>()
/*     */           {
/*     */             public boolean apply(Entity p_apply_1_)
/*     */             {
/* 366 */               return (p_apply_1_.getName().equals(s_f) != flag);
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/* 371 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Predicate<Entity>> func_180698_a(Map<String, String> p_180698_0_, final BlockPos p_180698_1_) {
/* 376 */     List<Predicate<Entity>> list = Lists.newArrayList();
/* 377 */     final int i = parseIntWithDefault(p_180698_0_, "rm", -1);
/* 378 */     final int j = parseIntWithDefault(p_180698_0_, "r", -1);
/*     */     
/* 380 */     if (p_180698_1_ != null && (i >= 0 || j >= 0)) {
/*     */       
/* 382 */       final int k = i * i;
/* 383 */       final int l = j * j;
/* 384 */       list.add(new Predicate<Entity>()
/*     */           {
/*     */             public boolean apply(Entity p_apply_1_)
/*     */             {
/* 388 */               int i1 = (int)p_apply_1_.getDistanceSqToCenter(p_180698_1_);
/* 389 */               return ((i < 0 || i1 >= k) && (j < 0 || i1 <= l));
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/* 394 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Predicate<Entity>> getRotationsPredicates(Map<String, String> p_179662_0_) {
/* 399 */     List<Predicate<Entity>> list = Lists.newArrayList();
/*     */     
/* 401 */     if (p_179662_0_.containsKey("rym") || p_179662_0_.containsKey("ry")) {
/*     */       
/* 403 */       final int i = func_179650_a(parseIntWithDefault(p_179662_0_, "rym", 0));
/* 404 */       final int j = func_179650_a(parseIntWithDefault(p_179662_0_, "ry", 359));
/* 405 */       list.add(new Predicate<Entity>()
/*     */           {
/*     */             public boolean apply(Entity p_apply_1_)
/*     */             {
/* 409 */               int i1 = PlayerSelector.func_179650_a((int)Math.floor(p_apply_1_.rotationYaw));
/* 410 */               return (i > j) ? ((i1 >= i || i1 <= j)) : ((i1 >= i && i1 <= j));
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/* 415 */     if (p_179662_0_.containsKey("rxm") || p_179662_0_.containsKey("rx")) {
/*     */       
/* 417 */       final int k = func_179650_a(parseIntWithDefault(p_179662_0_, "rxm", 0));
/* 418 */       final int l = func_179650_a(parseIntWithDefault(p_179662_0_, "rx", 359));
/* 419 */       list.add(new Predicate<Entity>()
/*     */           {
/*     */             public boolean apply(Entity p_apply_1_)
/*     */             {
/* 423 */               int i1 = PlayerSelector.func_179650_a((int)Math.floor(p_apply_1_.rotationPitch));
/* 424 */               return (k > l) ? ((i1 >= k || i1 <= l)) : ((i1 >= k && i1 <= l));
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/* 429 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T extends Entity> List<T> filterResults(Map<String, String> params, Class<? extends T> entityClass, List<Predicate<Entity>> inputList, String type, World worldIn, BlockPos position) {
/* 434 */     List<T> list = Lists.newArrayList();
/* 435 */     String s = func_179651_b(params, "type");
/* 436 */     s = (s != null && s.startsWith("!")) ? s.substring(1) : s;
/* 437 */     boolean flag = !type.equals("e");
/* 438 */     boolean flag1 = (type.equals("r") && s != null);
/* 439 */     int i = parseIntWithDefault(params, "dx", 0);
/* 440 */     int j = parseIntWithDefault(params, "dy", 0);
/* 441 */     int k = parseIntWithDefault(params, "dz", 0);
/* 442 */     int l = parseIntWithDefault(params, "r", -1);
/* 443 */     Predicate<Entity> predicate = Predicates.and(inputList);
/* 444 */     Predicate<Entity> predicate1 = Predicates.and(EntitySelectors.selectAnything, predicate);
/*     */     
/* 446 */     if (position != null) {
/*     */       
/* 448 */       int i1 = worldIn.playerEntities.size();
/* 449 */       int j1 = worldIn.loadedEntityList.size();
/* 450 */       boolean flag2 = (i1 < j1 / 16);
/*     */       
/* 452 */       if (!params.containsKey("dx") && !params.containsKey("dy") && !params.containsKey("dz")) {
/*     */         
/* 454 */         if (l >= 0) {
/*     */           
/* 456 */           AxisAlignedBB axisalignedbb1 = new AxisAlignedBB((position.getX() - l), (position.getY() - l), (position.getZ() - l), (position.getX() + l + 1), (position.getY() + l + 1), (position.getZ() + l + 1));
/*     */           
/* 458 */           if (flag && flag2 && !flag1)
/*     */           {
/* 460 */             list.addAll(worldIn.getPlayers(entityClass, predicate1));
/*     */           }
/*     */           else
/*     */           {
/* 464 */             list.addAll(worldIn.getEntitiesWithinAABB(entityClass, axisalignedbb1, predicate1));
/*     */           }
/*     */         
/* 467 */         } else if (type.equals("a")) {
/*     */           
/* 469 */           list.addAll(worldIn.getPlayers(entityClass, predicate));
/*     */         }
/* 471 */         else if (!type.equals("p") && (!type.equals("r") || flag1)) {
/*     */           
/* 473 */           list.addAll(worldIn.getEntities(entityClass, predicate1));
/*     */         }
/*     */         else {
/*     */           
/* 477 */           list.addAll(worldIn.getPlayers(entityClass, predicate1));
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 482 */         final AxisAlignedBB axisalignedbb = func_179661_a(position, i, j, k);
/*     */         
/* 484 */         if (flag && flag2 && !flag1)
/*     */         {
/* 486 */           Predicate<Entity> predicate2 = new Predicate<Entity>()
/*     */             {
/*     */               public boolean apply(Entity p_apply_1_)
/*     */               {
/* 490 */                 return (p_apply_1_.posX >= axisalignedbb.minX && p_apply_1_.posY >= axisalignedbb.minY && p_apply_1_.posZ >= axisalignedbb.minZ) ? ((p_apply_1_.posX < axisalignedbb.maxX && p_apply_1_.posY < axisalignedbb.maxY && p_apply_1_.posZ < axisalignedbb.maxZ)) : false;
/*     */               }
/*     */             };
/* 493 */           list.addAll(worldIn.getPlayers(entityClass, Predicates.and(predicate1, predicate2)));
/*     */         }
/*     */         else
/*     */         {
/* 497 */           list.addAll(worldIn.getEntitiesWithinAABB(entityClass, axisalignedbb, predicate1));
/*     */         }
/*     */       
/*     */       } 
/* 501 */     } else if (type.equals("a")) {
/*     */       
/* 503 */       list.addAll(worldIn.getPlayers(entityClass, predicate));
/*     */     }
/* 505 */     else if (!type.equals("p") && (!type.equals("r") || flag1)) {
/*     */       
/* 507 */       list.addAll(worldIn.getEntities(entityClass, predicate1));
/*     */     }
/*     */     else {
/*     */       
/* 511 */       list.addAll(worldIn.getPlayers(entityClass, predicate1));
/*     */     } 
/*     */     
/* 514 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T extends Entity> List<T> func_179658_a(List<T> p_179658_0_, Map<String, String> p_179658_1_, ICommandSender p_179658_2_, Class<? extends T> p_179658_3_, String p_179658_4_, final BlockPos p_179658_5_) {
/* 519 */     int i = parseIntWithDefault(p_179658_1_, "c", (!p_179658_4_.equals("a") && !p_179658_4_.equals("e")) ? 1 : 0);
/*     */     
/* 521 */     if (!p_179658_4_.equals("p") && !p_179658_4_.equals("a") && !p_179658_4_.equals("e")) {
/*     */       
/* 523 */       if (p_179658_4_.equals("r"))
/*     */       {
/* 525 */         Collections.shuffle(p_179658_0_);
/*     */       }
/*     */     }
/* 528 */     else if (p_179658_5_ != null) {
/*     */       
/* 530 */       Collections.sort(p_179658_0_, (Comparator)new Comparator<Entity>()
/*     */           {
/*     */             public int compare(Entity p_compare_1_, Entity p_compare_2_)
/*     */             {
/* 534 */               return ComparisonChain.start().compare(p_compare_1_.getDistanceSq(p_179658_5_), p_compare_2_.getDistanceSq(p_179658_5_)).result();
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/* 539 */     Entity entity = p_179658_2_.getCommandSenderEntity();
/*     */     
/* 541 */     if (entity != null && p_179658_3_.isAssignableFrom(entity.getClass()) && i == 1 && p_179658_0_.contains(entity) && !"r".equals(p_179658_4_))
/*     */     {
/* 543 */       p_179658_0_ = Lists.newArrayList((Object[])new Entity[] { entity });
/*     */     }
/*     */     
/* 546 */     if (i != 0) {
/*     */       
/* 548 */       if (i < 0)
/*     */       {
/* 550 */         Collections.reverse(p_179658_0_);
/*     */       }
/*     */       
/* 553 */       p_179658_0_ = p_179658_0_.subList(0, Math.min(Math.abs(i), p_179658_0_.size()));
/*     */     } 
/*     */     
/* 556 */     return p_179658_0_;
/*     */   }
/*     */ 
/*     */   
/*     */   private static AxisAlignedBB func_179661_a(BlockPos p_179661_0_, int p_179661_1_, int p_179661_2_, int p_179661_3_) {
/* 561 */     boolean flag = (p_179661_1_ < 0);
/* 562 */     boolean flag1 = (p_179661_2_ < 0);
/* 563 */     boolean flag2 = (p_179661_3_ < 0);
/* 564 */     int i = p_179661_0_.getX() + (flag ? p_179661_1_ : 0);
/* 565 */     int j = p_179661_0_.getY() + (flag1 ? p_179661_2_ : 0);
/* 566 */     int k = p_179661_0_.getZ() + (flag2 ? p_179661_3_ : 0);
/* 567 */     int l = p_179661_0_.getX() + (flag ? 0 : p_179661_1_) + 1;
/* 568 */     int i1 = p_179661_0_.getY() + (flag1 ? 0 : p_179661_2_) + 1;
/* 569 */     int j1 = p_179661_0_.getZ() + (flag2 ? 0 : p_179661_3_) + 1;
/* 570 */     return new AxisAlignedBB(i, j, k, l, i1, j1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int func_179650_a(int p_179650_0_) {
/* 575 */     p_179650_0_ %= 360;
/*     */     
/* 577 */     if (p_179650_0_ >= 160)
/*     */     {
/* 579 */       p_179650_0_ -= 360;
/*     */     }
/*     */     
/* 582 */     if (p_179650_0_ < 0)
/*     */     {
/* 584 */       p_179650_0_ += 360;
/*     */     }
/*     */     
/* 587 */     return p_179650_0_;
/*     */   }
/*     */ 
/*     */   
/*     */   private static BlockPos func_179664_b(Map<String, String> p_179664_0_, BlockPos p_179664_1_) {
/* 592 */     return new BlockPos(parseIntWithDefault(p_179664_0_, "x", p_179664_1_.getX()), parseIntWithDefault(p_179664_0_, "y", p_179664_1_.getY()), parseIntWithDefault(p_179664_0_, "z", p_179664_1_.getZ()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean func_179665_h(Map<String, String> p_179665_0_) {
/* 597 */     for (String s : WORLD_BINDING_ARGS) {
/*     */       
/* 599 */       if (p_179665_0_.containsKey(s))
/*     */       {
/* 601 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 605 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int parseIntWithDefault(Map<String, String> p_179653_0_, String p_179653_1_, int p_179653_2_) {
/* 610 */     return p_179653_0_.containsKey(p_179653_1_) ? MathHelper.parseIntWithDefault(p_179653_0_.get(p_179653_1_), p_179653_2_) : p_179653_2_;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String func_179651_b(Map<String, String> p_179651_0_, String p_179651_1_) {
/* 615 */     return p_179651_0_.get(p_179651_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Map<String, Integer> func_96560_a(Map<String, String> p_96560_0_) {
/* 620 */     Map<String, Integer> map = Maps.newHashMap();
/*     */     
/* 622 */     for (String s : p_96560_0_.keySet()) {
/*     */       
/* 624 */       if (s.startsWith("score_") && s.length() > "score_".length())
/*     */       {
/* 626 */         map.put(s.substring("score_".length()), Integer.valueOf(MathHelper.parseIntWithDefault(p_96560_0_.get(s), 1)));
/*     */       }
/*     */     } 
/*     */     
/* 630 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean matchesMultiplePlayers(String p_82377_0_) {
/* 635 */     Matcher matcher = tokenPattern.matcher(p_82377_0_);
/*     */     
/* 637 */     if (!matcher.matches())
/*     */     {
/* 639 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 643 */     Map<String, String> map = getArgumentMap(matcher.group(2));
/* 644 */     String s = matcher.group(1);
/* 645 */     int i = (!"a".equals(s) && !"e".equals(s)) ? 1 : 0;
/* 646 */     return (parseIntWithDefault(map, "c", i) != 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasArguments(String p_82378_0_) {
/* 652 */     return tokenPattern.matcher(p_82378_0_).matches();
/*     */   }
/*     */ 
/*     */   
/*     */   private static Map<String, String> getArgumentMap(String argumentString) {
/* 657 */     Map<String, String> map = Maps.newHashMap();
/*     */     
/* 659 */     if (argumentString == null)
/*     */     {
/* 661 */       return map;
/*     */     }
/*     */ 
/*     */     
/* 665 */     int i = 0;
/* 666 */     int j = -1;
/*     */     
/* 668 */     for (Matcher matcher = intListPattern.matcher(argumentString); matcher.find(); j = matcher.end()) {
/*     */       
/* 670 */       String s = null;
/*     */       
/* 672 */       switch (i++) {
/*     */         
/*     */         case 0:
/* 675 */           s = "x";
/*     */           break;
/*     */         
/*     */         case 1:
/* 679 */           s = "y";
/*     */           break;
/*     */         
/*     */         case 2:
/* 683 */           s = "z";
/*     */           break;
/*     */         
/*     */         case 3:
/* 687 */           s = "r";
/*     */           break;
/*     */       } 
/* 690 */       if (s != null && matcher.group(1).length() > 0)
/*     */       {
/* 692 */         map.put(s, matcher.group(1));
/*     */       }
/*     */     } 
/*     */     
/* 696 */     if (j < argumentString.length()) {
/*     */       
/* 698 */       Matcher matcher1 = keyValueListPattern.matcher((j == -1) ? argumentString : argumentString.substring(j));
/*     */       
/* 700 */       while (matcher1.find())
/*     */       {
/* 702 */         map.put(matcher1.group(1), matcher1.group(2));
/*     */       }
/*     */     } 
/*     */     
/* 706 */     return map;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\PlayerSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */