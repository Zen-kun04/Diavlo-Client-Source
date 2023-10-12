/*     */ package net.minecraft.item;
/*     */ 
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemFood
/*     */   extends Item {
/*     */   public final int itemUseDuration;
/*     */   private final int healAmount;
/*     */   private final float saturationModifier;
/*     */   private final boolean isWolfsFavoriteMeat;
/*     */   private boolean alwaysEdible;
/*     */   private int potionId;
/*     */   private int potionDuration;
/*     */   private int potionAmplifier;
/*     */   private float potionEffectProbability;
/*     */   
/*     */   public ItemFood(int amount, float saturation, boolean isWolfFood) {
/*  23 */     this.itemUseDuration = 32;
/*  24 */     this.healAmount = amount;
/*  25 */     this.isWolfsFavoriteMeat = isWolfFood;
/*  26 */     this.saturationModifier = saturation;
/*  27 */     setCreativeTab(CreativeTabs.tabFood);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemFood(int amount, boolean isWolfFood) {
/*  32 */     this(amount, 0.6F, isWolfFood);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
/*  37 */     stack.stackSize--;
/*  38 */     playerIn.getFoodStats().addStats(this, stack);
/*  39 */     worldIn.playSoundAtEntity((Entity)playerIn, "random.burp", 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
/*  40 */     onFoodEaten(stack, worldIn, playerIn);
/*  41 */     playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/*  42 */     return stack;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
/*  47 */     if (!worldIn.isRemote && this.potionId > 0 && worldIn.rand.nextFloat() < this.potionEffectProbability)
/*     */     {
/*  49 */       player.addPotionEffect(new PotionEffect(this.potionId, this.potionDuration * 20, this.potionAmplifier));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxItemUseDuration(ItemStack stack) {
/*  55 */     return 32;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumAction getItemUseAction(ItemStack stack) {
/*  60 */     return EnumAction.EAT;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/*  65 */     if (playerIn.canEat(this.alwaysEdible))
/*     */     {
/*  67 */       playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn));
/*     */     }
/*     */     
/*  70 */     return itemStackIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHealAmount(ItemStack stack) {
/*  75 */     return this.healAmount;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getSaturationModifier(ItemStack stack) {
/*  80 */     return this.saturationModifier;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWolfsFavoriteMeat() {
/*  85 */     return this.isWolfsFavoriteMeat;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemFood setPotionEffect(int id, int duration, int amplifier, float probability) {
/*  90 */     this.potionId = id;
/*  91 */     this.potionDuration = duration;
/*  92 */     this.potionAmplifier = amplifier;
/*  93 */     this.potionEffectProbability = probability;
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemFood setAlwaysEdible() {
/*  99 */     this.alwaysEdible = true;
/* 100 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemFood.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */