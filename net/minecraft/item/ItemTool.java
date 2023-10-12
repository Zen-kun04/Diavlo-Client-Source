/*    */ package net.minecraft.item;
/*    */ 
/*    */ import com.google.common.collect.Multimap;
/*    */ import java.util.Set;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.SharedMonsterAttributes;
/*    */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemTool
/*    */   extends Item
/*    */ {
/*    */   private Set<Block> effectiveBlocks;
/* 17 */   protected float efficiencyOnProperMaterial = 4.0F;
/*    */   
/*    */   private float damageVsEntity;
/*    */   protected Item.ToolMaterial toolMaterial;
/*    */   
/*    */   protected ItemTool(float attackDamage, Item.ToolMaterial material, Set<Block> effectiveBlocks) {
/* 23 */     this.toolMaterial = material;
/* 24 */     this.effectiveBlocks = effectiveBlocks;
/* 25 */     this.maxStackSize = 1;
/* 26 */     setMaxDamage(material.getMaxUses());
/* 27 */     this.efficiencyOnProperMaterial = material.getEfficiencyOnProperMaterial();
/* 28 */     this.damageVsEntity = attackDamage + material.getDamageVsEntity();
/* 29 */     setCreativeTab(CreativeTabs.tabTools);
/*    */   }
/*    */ 
/*    */   
/*    */   public float getStrVsBlock(ItemStack stack, Block state) {
/* 34 */     return this.effectiveBlocks.contains(state) ? this.efficiencyOnProperMaterial : 1.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
/* 39 */     stack.damageItem(2, attacker);
/* 40 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
/* 45 */     if (blockIn.getBlockHardness(worldIn, pos) != 0.0D)
/*    */     {
/* 47 */       stack.damageItem(1, playerIn);
/*    */     }
/*    */     
/* 50 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFull3D() {
/* 55 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Item.ToolMaterial getToolMaterial() {
/* 60 */     return this.toolMaterial;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getItemEnchantability() {
/* 65 */     return this.toolMaterial.getEnchantability();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getToolMaterialName() {
/* 70 */     return this.toolMaterial.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
/* 75 */     return (this.toolMaterial.getRepairItem() == repair.getItem()) ? true : super.getIsRepairable(toRepair, repair);
/*    */   }
/*    */ 
/*    */   
/*    */   public Multimap<String, AttributeModifier> getItemAttributeModifiers() {
/* 80 */     Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
/* 81 */     multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Tool modifier", this.damageVsEntity, 0));
/* 82 */     return multimap;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */