/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ChatComponentTranslation;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.IJsonSerializable;
/*     */ import net.minecraft.util.StatCollector;
/*     */ 
/*     */ public class Achievement
/*     */   extends StatBase
/*     */ {
/*     */   public final int displayColumn;
/*     */   public final int displayRow;
/*     */   public final Achievement parentAchievement;
/*     */   private final String achievementDescription;
/*     */   private IStatStringFormat statStringFormatter;
/*     */   public final ItemStack theItemStack;
/*     */   private boolean isSpecial;
/*     */   
/*     */   public Achievement(String statIdIn, String unlocalizedName, int column, int row, Item itemIn, Achievement parent) {
/*  24 */     this(statIdIn, unlocalizedName, column, row, new ItemStack(itemIn), parent);
/*     */   }
/*     */ 
/*     */   
/*     */   public Achievement(String statIdIn, String unlocalizedName, int column, int row, Block blockIn, Achievement parent) {
/*  29 */     this(statIdIn, unlocalizedName, column, row, new ItemStack(blockIn), parent);
/*     */   }
/*     */ 
/*     */   
/*     */   public Achievement(String statIdIn, String unlocalizedName, int column, int row, ItemStack stack, Achievement parent) {
/*  34 */     super(statIdIn, (IChatComponent)new ChatComponentTranslation("achievement." + unlocalizedName, new Object[0]));
/*  35 */     this.theItemStack = stack;
/*  36 */     this.achievementDescription = "achievement." + unlocalizedName + ".desc";
/*  37 */     this.displayColumn = column;
/*  38 */     this.displayRow = row;
/*     */     
/*  40 */     if (column < AchievementList.minDisplayColumn)
/*     */     {
/*  42 */       AchievementList.minDisplayColumn = column;
/*     */     }
/*     */     
/*  45 */     if (row < AchievementList.minDisplayRow)
/*     */     {
/*  47 */       AchievementList.minDisplayRow = row;
/*     */     }
/*     */     
/*  50 */     if (column > AchievementList.maxDisplayColumn)
/*     */     {
/*  52 */       AchievementList.maxDisplayColumn = column;
/*     */     }
/*     */     
/*  55 */     if (row > AchievementList.maxDisplayRow)
/*     */     {
/*  57 */       AchievementList.maxDisplayRow = row;
/*     */     }
/*     */     
/*  60 */     this.parentAchievement = parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public Achievement initIndependentStat() {
/*  65 */     this.isIndependent = true;
/*  66 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Achievement setSpecial() {
/*  71 */     this.isSpecial = true;
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Achievement registerStat() {
/*  77 */     super.registerStat();
/*  78 */     AchievementList.achievementList.add(this);
/*  79 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAchievement() {
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getStatName() {
/*  89 */     IChatComponent ichatcomponent = super.getStatName();
/*  90 */     ichatcomponent.getChatStyle().setColor(getSpecial() ? EnumChatFormatting.DARK_PURPLE : EnumChatFormatting.GREEN);
/*  91 */     return ichatcomponent;
/*     */   }
/*     */ 
/*     */   
/*     */   public Achievement func_150953_b(Class<? extends IJsonSerializable> p_150953_1_) {
/*  96 */     return (Achievement)super.func_150953_b(p_150953_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 101 */     return (this.statStringFormatter != null) ? this.statStringFormatter.formatString(StatCollector.translateToLocal(this.achievementDescription)) : StatCollector.translateToLocal(this.achievementDescription);
/*     */   }
/*     */ 
/*     */   
/*     */   public Achievement setStatStringFormatter(IStatStringFormat statStringFormatterIn) {
/* 106 */     this.statStringFormatter = statStringFormatterIn;
/* 107 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getSpecial() {
/* 112 */     return this.isSpecial;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\stats\Achievement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */