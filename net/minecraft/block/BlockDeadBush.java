/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.stats.StatList;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockDeadBush
/*    */   extends BlockBush
/*    */ {
/*    */   protected BlockDeadBush() {
/* 21 */     super(Material.vine);
/* 22 */     float f = 0.4F;
/* 23 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
/*    */   }
/*    */ 
/*    */   
/*    */   public MapColor getMapColor(IBlockState state) {
/* 28 */     return MapColor.woodColor;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canPlaceBlockOn(Block ground) {
/* 33 */     return (ground == Blocks.sand || ground == Blocks.hardened_clay || ground == Blocks.stained_hardened_clay || ground == Blocks.dirt);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isReplaceable(World worldIn, BlockPos pos) {
/* 38 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 43 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
/* 48 */     if (!worldIn.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
/*    */       
/* 50 */       player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
/* 51 */       spawnAsEntity(worldIn, pos, new ItemStack(Blocks.deadbush, 1, 0));
/*    */     }
/*    */     else {
/*    */       
/* 55 */       super.harvestBlock(worldIn, player, pos, state, te);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockDeadBush.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */