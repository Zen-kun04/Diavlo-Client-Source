/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.stats.StatList;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumWorldBlockLayer;
/*    */ import net.minecraft.world.EnumSkyBlock;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockIce
/*    */   extends BlockBreakable {
/*    */   public BlockIce() {
/* 22 */     super(Material.ice, false);
/* 23 */     this.slipperiness = 0.98F;
/* 24 */     setTickRandomly(true);
/* 25 */     setCreativeTab(CreativeTabs.tabBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   public EnumWorldBlockLayer getBlockLayer() {
/* 30 */     return EnumWorldBlockLayer.TRANSLUCENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
/* 35 */     player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
/* 36 */     player.addExhaustion(0.025F);
/*    */     
/* 38 */     if (canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier((EntityLivingBase)player)) {
/*    */       
/* 40 */       ItemStack itemstack = createStackedBlock(state);
/*    */       
/* 42 */       if (itemstack != null)
/*    */       {
/* 44 */         spawnAsEntity(worldIn, pos, itemstack);
/*    */       }
/*    */     }
/*    */     else {
/*    */       
/* 49 */       if (worldIn.provider.doesWaterVaporize()) {
/*    */         
/* 51 */         worldIn.setBlockToAir(pos);
/*    */         
/*    */         return;
/*    */       } 
/* 55 */       int i = EnchantmentHelper.getFortuneModifier((EntityLivingBase)player);
/* 56 */       dropBlockAsItem(worldIn, pos, state, i);
/* 57 */       Material material = worldIn.getBlockState(pos.down()).getBlock().getMaterial();
/*    */       
/* 59 */       if (material.blocksMovement() || material.isLiquid())
/*    */       {
/* 61 */         worldIn.setBlockState(pos, Blocks.flowing_water.getDefaultState());
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int quantityDropped(Random random) {
/* 68 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 73 */     if (worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 11 - getLightOpacity())
/*    */     {
/* 75 */       if (worldIn.provider.doesWaterVaporize()) {
/*    */         
/* 77 */         worldIn.setBlockToAir(pos);
/*    */       }
/*    */       else {
/*    */         
/* 81 */         dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
/* 82 */         worldIn.setBlockState(pos, Blocks.water.getDefaultState());
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMobilityFlag() {
/* 89 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockIce.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */