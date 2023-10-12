/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.CraftingManager;
/*     */ import net.minecraft.item.crafting.FurnaceRecipes;
/*     */ import net.minecraft.item.crafting.IRecipe;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class StatList
/*     */ {
/*  23 */   protected static Map<String, StatBase> oneShotStats = Maps.newHashMap();
/*  24 */   public static List<StatBase> allStats = Lists.newArrayList();
/*  25 */   public static List<StatBase> generalStats = Lists.newArrayList();
/*  26 */   public static List<StatCrafting> itemStats = Lists.newArrayList();
/*  27 */   public static List<StatCrafting> objectMineStats = Lists.newArrayList();
/*  28 */   public static StatBase leaveGameStat = (new StatBasic("stat.leaveGame", (IChatComponent)new ChatComponentTranslation("stat.leaveGame", new Object[0]))).initIndependentStat().registerStat();
/*  29 */   public static StatBase minutesPlayedStat = (new StatBasic("stat.playOneMinute", (IChatComponent)new ChatComponentTranslation("stat.playOneMinute", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();
/*  30 */   public static StatBase timeSinceDeathStat = (new StatBasic("stat.timeSinceDeath", (IChatComponent)new ChatComponentTranslation("stat.timeSinceDeath", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();
/*  31 */   public static StatBase distanceWalkedStat = (new StatBasic("stat.walkOneCm", (IChatComponent)new ChatComponentTranslation("stat.walkOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
/*  32 */   public static StatBase distanceCrouchedStat = (new StatBasic("stat.crouchOneCm", (IChatComponent)new ChatComponentTranslation("stat.crouchOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
/*  33 */   public static StatBase distanceSprintedStat = (new StatBasic("stat.sprintOneCm", (IChatComponent)new ChatComponentTranslation("stat.sprintOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
/*  34 */   public static StatBase distanceSwumStat = (new StatBasic("stat.swimOneCm", (IChatComponent)new ChatComponentTranslation("stat.swimOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
/*  35 */   public static StatBase distanceFallenStat = (new StatBasic("stat.fallOneCm", (IChatComponent)new ChatComponentTranslation("stat.fallOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
/*  36 */   public static StatBase distanceClimbedStat = (new StatBasic("stat.climbOneCm", (IChatComponent)new ChatComponentTranslation("stat.climbOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
/*  37 */   public static StatBase distanceFlownStat = (new StatBasic("stat.flyOneCm", (IChatComponent)new ChatComponentTranslation("stat.flyOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
/*  38 */   public static StatBase distanceDoveStat = (new StatBasic("stat.diveOneCm", (IChatComponent)new ChatComponentTranslation("stat.diveOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
/*  39 */   public static StatBase distanceByMinecartStat = (new StatBasic("stat.minecartOneCm", (IChatComponent)new ChatComponentTranslation("stat.minecartOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
/*  40 */   public static StatBase distanceByBoatStat = (new StatBasic("stat.boatOneCm", (IChatComponent)new ChatComponentTranslation("stat.boatOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
/*  41 */   public static StatBase distanceByPigStat = (new StatBasic("stat.pigOneCm", (IChatComponent)new ChatComponentTranslation("stat.pigOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
/*  42 */   public static StatBase distanceByHorseStat = (new StatBasic("stat.horseOneCm", (IChatComponent)new ChatComponentTranslation("stat.horseOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
/*  43 */   public static StatBase jumpStat = (new StatBasic("stat.jump", (IChatComponent)new ChatComponentTranslation("stat.jump", new Object[0]))).initIndependentStat().registerStat();
/*  44 */   public static StatBase dropStat = (new StatBasic("stat.drop", (IChatComponent)new ChatComponentTranslation("stat.drop", new Object[0]))).initIndependentStat().registerStat();
/*  45 */   public static StatBase damageDealtStat = (new StatBasic("stat.damageDealt", (IChatComponent)new ChatComponentTranslation("stat.damageDealt", new Object[0]), StatBase.field_111202_k)).registerStat();
/*  46 */   public static StatBase damageTakenStat = (new StatBasic("stat.damageTaken", (IChatComponent)new ChatComponentTranslation("stat.damageTaken", new Object[0]), StatBase.field_111202_k)).registerStat();
/*  47 */   public static StatBase deathsStat = (new StatBasic("stat.deaths", (IChatComponent)new ChatComponentTranslation("stat.deaths", new Object[0]))).registerStat();
/*  48 */   public static StatBase mobKillsStat = (new StatBasic("stat.mobKills", (IChatComponent)new ChatComponentTranslation("stat.mobKills", new Object[0]))).registerStat();
/*  49 */   public static StatBase animalsBredStat = (new StatBasic("stat.animalsBred", (IChatComponent)new ChatComponentTranslation("stat.animalsBred", new Object[0]))).registerStat();
/*  50 */   public static StatBase playerKillsStat = (new StatBasic("stat.playerKills", (IChatComponent)new ChatComponentTranslation("stat.playerKills", new Object[0]))).registerStat();
/*  51 */   public static StatBase fishCaughtStat = (new StatBasic("stat.fishCaught", (IChatComponent)new ChatComponentTranslation("stat.fishCaught", new Object[0]))).registerStat();
/*  52 */   public static StatBase junkFishedStat = (new StatBasic("stat.junkFished", (IChatComponent)new ChatComponentTranslation("stat.junkFished", new Object[0]))).registerStat();
/*  53 */   public static StatBase treasureFishedStat = (new StatBasic("stat.treasureFished", (IChatComponent)new ChatComponentTranslation("stat.treasureFished", new Object[0]))).registerStat();
/*  54 */   public static StatBase timesTalkedToVillagerStat = (new StatBasic("stat.talkedToVillager", (IChatComponent)new ChatComponentTranslation("stat.talkedToVillager", new Object[0]))).registerStat();
/*  55 */   public static StatBase timesTradedWithVillagerStat = (new StatBasic("stat.tradedWithVillager", (IChatComponent)new ChatComponentTranslation("stat.tradedWithVillager", new Object[0]))).registerStat();
/*  56 */   public static StatBase field_181724_H = (new StatBasic("stat.cakeSlicesEaten", (IChatComponent)new ChatComponentTranslation("stat.cakeSlicesEaten", new Object[0]))).registerStat();
/*  57 */   public static StatBase field_181725_I = (new StatBasic("stat.cauldronFilled", (IChatComponent)new ChatComponentTranslation("stat.cauldronFilled", new Object[0]))).registerStat();
/*  58 */   public static StatBase field_181726_J = (new StatBasic("stat.cauldronUsed", (IChatComponent)new ChatComponentTranslation("stat.cauldronUsed", new Object[0]))).registerStat();
/*  59 */   public static StatBase field_181727_K = (new StatBasic("stat.armorCleaned", (IChatComponent)new ChatComponentTranslation("stat.armorCleaned", new Object[0]))).registerStat();
/*  60 */   public static StatBase field_181728_L = (new StatBasic("stat.bannerCleaned", (IChatComponent)new ChatComponentTranslation("stat.bannerCleaned", new Object[0]))).registerStat();
/*  61 */   public static StatBase field_181729_M = (new StatBasic("stat.brewingstandInteraction", (IChatComponent)new ChatComponentTranslation("stat.brewingstandInteraction", new Object[0]))).registerStat();
/*  62 */   public static StatBase field_181730_N = (new StatBasic("stat.beaconInteraction", (IChatComponent)new ChatComponentTranslation("stat.beaconInteraction", new Object[0]))).registerStat();
/*  63 */   public static StatBase field_181731_O = (new StatBasic("stat.dropperInspected", (IChatComponent)new ChatComponentTranslation("stat.dropperInspected", new Object[0]))).registerStat();
/*  64 */   public static StatBase field_181732_P = (new StatBasic("stat.hopperInspected", (IChatComponent)new ChatComponentTranslation("stat.hopperInspected", new Object[0]))).registerStat();
/*  65 */   public static StatBase field_181733_Q = (new StatBasic("stat.dispenserInspected", (IChatComponent)new ChatComponentTranslation("stat.dispenserInspected", new Object[0]))).registerStat();
/*  66 */   public static StatBase field_181734_R = (new StatBasic("stat.noteblockPlayed", (IChatComponent)new ChatComponentTranslation("stat.noteblockPlayed", new Object[0]))).registerStat();
/*  67 */   public static StatBase field_181735_S = (new StatBasic("stat.noteblockTuned", (IChatComponent)new ChatComponentTranslation("stat.noteblockTuned", new Object[0]))).registerStat();
/*  68 */   public static StatBase field_181736_T = (new StatBasic("stat.flowerPotted", (IChatComponent)new ChatComponentTranslation("stat.flowerPotted", new Object[0]))).registerStat();
/*  69 */   public static StatBase field_181737_U = (new StatBasic("stat.trappedChestTriggered", (IChatComponent)new ChatComponentTranslation("stat.trappedChestTriggered", new Object[0]))).registerStat();
/*  70 */   public static StatBase field_181738_V = (new StatBasic("stat.enderchestOpened", (IChatComponent)new ChatComponentTranslation("stat.enderchestOpened", new Object[0]))).registerStat();
/*  71 */   public static StatBase field_181739_W = (new StatBasic("stat.itemEnchanted", (IChatComponent)new ChatComponentTranslation("stat.itemEnchanted", new Object[0]))).registerStat();
/*  72 */   public static StatBase field_181740_X = (new StatBasic("stat.recordPlayed", (IChatComponent)new ChatComponentTranslation("stat.recordPlayed", new Object[0]))).registerStat();
/*  73 */   public static StatBase field_181741_Y = (new StatBasic("stat.furnaceInteraction", (IChatComponent)new ChatComponentTranslation("stat.furnaceInteraction", new Object[0]))).registerStat();
/*  74 */   public static StatBase field_181742_Z = (new StatBasic("stat.craftingTableInteraction", (IChatComponent)new ChatComponentTranslation("stat.workbenchInteraction", new Object[0]))).registerStat();
/*  75 */   public static StatBase field_181723_aa = (new StatBasic("stat.chestOpened", (IChatComponent)new ChatComponentTranslation("stat.chestOpened", new Object[0]))).registerStat();
/*  76 */   public static final StatBase[] mineBlockStatArray = new StatBase[4096];
/*  77 */   public static final StatBase[] objectCraftStats = new StatBase[32000];
/*  78 */   public static final StatBase[] objectUseStats = new StatBase[32000];
/*  79 */   public static final StatBase[] objectBreakStats = new StatBase[32000];
/*     */ 
/*     */   
/*     */   public static void init() {
/*  83 */     initMiningStats();
/*  84 */     initStats();
/*  85 */     initItemDepleteStats();
/*  86 */     initCraftableStats();
/*  87 */     AchievementList.init();
/*  88 */     EntityList.func_151514_a();
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initCraftableStats() {
/*  93 */     Set<Item> set = Sets.newHashSet();
/*     */     
/*  95 */     for (IRecipe irecipe : CraftingManager.getInstance().getRecipeList()) {
/*     */       
/*  97 */       if (irecipe.getRecipeOutput() != null)
/*     */       {
/*  99 */         set.add(irecipe.getRecipeOutput().getItem());
/*     */       }
/*     */     } 
/*     */     
/* 103 */     for (ItemStack itemstack : FurnaceRecipes.instance().getSmeltingList().values())
/*     */     {
/* 105 */       set.add(itemstack.getItem());
/*     */     }
/*     */     
/* 108 */     for (Item item : set) {
/*     */       
/* 110 */       if (item != null) {
/*     */         
/* 112 */         int i = Item.getIdFromItem(item);
/* 113 */         String s = func_180204_a(item);
/*     */         
/* 115 */         if (s != null)
/*     */         {
/* 117 */           objectCraftStats[i] = (new StatCrafting("stat.craftItem.", s, (IChatComponent)new ChatComponentTranslation("stat.craftItem", new Object[] { (new ItemStack(item)).getChatComponent() }), item)).registerStat();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 122 */     replaceAllSimilarBlocks(objectCraftStats);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initMiningStats() {
/* 127 */     for (Block block : Block.blockRegistry) {
/*     */       
/* 129 */       Item item = Item.getItemFromBlock(block);
/*     */       
/* 131 */       if (item != null) {
/*     */         
/* 133 */         int i = Block.getIdFromBlock(block);
/* 134 */         String s = func_180204_a(item);
/*     */         
/* 136 */         if (s != null && block.getEnableStats()) {
/*     */           
/* 138 */           mineBlockStatArray[i] = (new StatCrafting("stat.mineBlock.", s, (IChatComponent)new ChatComponentTranslation("stat.mineBlock", new Object[] { (new ItemStack(block)).getChatComponent() }), item)).registerStat();
/* 139 */           objectMineStats.add((StatCrafting)mineBlockStatArray[i]);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 144 */     replaceAllSimilarBlocks(mineBlockStatArray);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initStats() {
/* 149 */     for (Item item : Item.itemRegistry) {
/*     */       
/* 151 */       if (item != null) {
/*     */         
/* 153 */         int i = Item.getIdFromItem(item);
/* 154 */         String s = func_180204_a(item);
/*     */         
/* 156 */         if (s != null) {
/*     */           
/* 158 */           objectUseStats[i] = (new StatCrafting("stat.useItem.", s, (IChatComponent)new ChatComponentTranslation("stat.useItem", new Object[] { (new ItemStack(item)).getChatComponent() }), item)).registerStat();
/*     */           
/* 160 */           if (!(item instanceof net.minecraft.item.ItemBlock))
/*     */           {
/* 162 */             itemStats.add((StatCrafting)objectUseStats[i]);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 168 */     replaceAllSimilarBlocks(objectUseStats);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initItemDepleteStats() {
/* 173 */     for (Item item : Item.itemRegistry) {
/*     */       
/* 175 */       if (item != null) {
/*     */         
/* 177 */         int i = Item.getIdFromItem(item);
/* 178 */         String s = func_180204_a(item);
/*     */         
/* 180 */         if (s != null && item.isDamageable())
/*     */         {
/* 182 */           objectBreakStats[i] = (new StatCrafting("stat.breakItem.", s, (IChatComponent)new ChatComponentTranslation("stat.breakItem", new Object[] { (new ItemStack(item)).getChatComponent() }), item)).registerStat();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 187 */     replaceAllSimilarBlocks(objectBreakStats);
/*     */   }
/*     */ 
/*     */   
/*     */   private static String func_180204_a(Item p_180204_0_) {
/* 192 */     ResourceLocation resourcelocation = (ResourceLocation)Item.itemRegistry.getNameForObject(p_180204_0_);
/* 193 */     return (resourcelocation != null) ? resourcelocation.toString().replace(':', '.') : null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void replaceAllSimilarBlocks(StatBase[] p_75924_0_) {
/* 198 */     mergeStatBases(p_75924_0_, (Block)Blocks.water, (Block)Blocks.flowing_water);
/* 199 */     mergeStatBases(p_75924_0_, (Block)Blocks.lava, (Block)Blocks.flowing_lava);
/* 200 */     mergeStatBases(p_75924_0_, Blocks.lit_pumpkin, Blocks.pumpkin);
/* 201 */     mergeStatBases(p_75924_0_, Blocks.lit_furnace, Blocks.furnace);
/* 202 */     mergeStatBases(p_75924_0_, Blocks.lit_redstone_ore, Blocks.redstone_ore);
/* 203 */     mergeStatBases(p_75924_0_, (Block)Blocks.powered_repeater, (Block)Blocks.unpowered_repeater);
/* 204 */     mergeStatBases(p_75924_0_, (Block)Blocks.powered_comparator, (Block)Blocks.unpowered_comparator);
/* 205 */     mergeStatBases(p_75924_0_, Blocks.redstone_torch, Blocks.unlit_redstone_torch);
/* 206 */     mergeStatBases(p_75924_0_, Blocks.lit_redstone_lamp, Blocks.redstone_lamp);
/* 207 */     mergeStatBases(p_75924_0_, (Block)Blocks.double_stone_slab, (Block)Blocks.stone_slab);
/* 208 */     mergeStatBases(p_75924_0_, (Block)Blocks.double_wooden_slab, (Block)Blocks.wooden_slab);
/* 209 */     mergeStatBases(p_75924_0_, (Block)Blocks.double_stone_slab2, (Block)Blocks.stone_slab2);
/* 210 */     mergeStatBases(p_75924_0_, (Block)Blocks.grass, Blocks.dirt);
/* 211 */     mergeStatBases(p_75924_0_, Blocks.farmland, Blocks.dirt);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void mergeStatBases(StatBase[] statBaseIn, Block p_151180_1_, Block p_151180_2_) {
/* 216 */     int i = Block.getIdFromBlock(p_151180_1_);
/* 217 */     int j = Block.getIdFromBlock(p_151180_2_);
/*     */     
/* 219 */     if (statBaseIn[i] != null && statBaseIn[j] == null) {
/*     */       
/* 221 */       statBaseIn[j] = statBaseIn[i];
/*     */     }
/*     */     else {
/*     */       
/* 225 */       allStats.remove(statBaseIn[i]);
/* 226 */       objectMineStats.remove(statBaseIn[i]);
/* 227 */       generalStats.remove(statBaseIn[i]);
/* 228 */       statBaseIn[i] = statBaseIn[j];
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static StatBase getStatKillEntity(EntityList.EntityEggInfo eggInfo) {
/* 234 */     String s = EntityList.getStringFromID(eggInfo.spawnedID);
/* 235 */     return (s == null) ? null : (new StatBase("stat.killEntity." + s, (IChatComponent)new ChatComponentTranslation("stat.entityKill", new Object[] { new ChatComponentTranslation("entity." + s + ".name", new Object[0]) }))).registerStat();
/*     */   }
/*     */ 
/*     */   
/*     */   public static StatBase getStatEntityKilledBy(EntityList.EntityEggInfo eggInfo) {
/* 240 */     String s = EntityList.getStringFromID(eggInfo.spawnedID);
/* 241 */     return (s == null) ? null : (new StatBase("stat.entityKilledBy." + s, (IChatComponent)new ChatComponentTranslation("stat.entityKilledBy", new Object[] { new ChatComponentTranslation("entity." + s + ".name", new Object[0]) }))).registerStat();
/*     */   }
/*     */ 
/*     */   
/*     */   public static StatBase getOneShotStat(String p_151177_0_) {
/* 246 */     return oneShotStats.get(p_151177_0_);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\stats\StatList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */