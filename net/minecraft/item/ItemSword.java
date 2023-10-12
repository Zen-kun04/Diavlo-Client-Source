/*     */ package net.minecraft.item;
/*     */ 
/*     */ import com.google.common.collect.Multimap;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemSword
/*     */   extends Item
/*     */ {
/*     */   private float attackDamage;
/*     */   private final Item.ToolMaterial material;
/*     */   
/*     */   public ItemSword(Item.ToolMaterial material) {
/*  22 */     this.material = material;
/*  23 */     this.maxStackSize = 1;
/*  24 */     setMaxDamage(material.getMaxUses());
/*  25 */     setCreativeTab(CreativeTabs.tabCombat);
/*  26 */     this.attackDamage = 4.0F + material.getDamageVsEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getDamageVsEntity() {
/*  31 */     return this.material.getDamageVsEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getStrVsBlock(ItemStack stack, Block state) {
/*  36 */     if (state == Blocks.web)
/*     */     {
/*  38 */       return 15.0F;
/*     */     }
/*     */ 
/*     */     
/*  42 */     Material material = state.getMaterial();
/*  43 */     return (material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.gourd) ? 1.0F : 1.5F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
/*  49 */     stack.damageItem(1, attacker);
/*  50 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
/*  55 */     if (blockIn.getBlockHardness(worldIn, pos) != 0.0D)
/*     */     {
/*  57 */       stack.damageItem(2, playerIn);
/*     */     }
/*     */     
/*  60 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFull3D() {
/*  65 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumAction getItemUseAction(ItemStack stack) {
/*  70 */     return EnumAction.BLOCK;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxItemUseDuration(ItemStack stack) {
/*  75 */     return 72000;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/*  80 */     playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn));
/*  81 */     return itemStackIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canHarvestBlock(Block blockIn) {
/*  86 */     return (blockIn == Blocks.web);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getItemEnchantability() {
/*  91 */     return this.material.getEnchantability();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getToolMaterialName() {
/*  96 */     return this.material.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
/* 101 */     return (this.material.getRepairItem() == repair.getItem()) ? true : super.getIsRepairable(toRepair, repair);
/*     */   }
/*     */ 
/*     */   
/*     */   public Multimap<String, AttributeModifier> getItemAttributeModifiers() {
/* 106 */     Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
/* 107 */     multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", this.attackDamage, 0));
/* 108 */     return multimap;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemSword.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */