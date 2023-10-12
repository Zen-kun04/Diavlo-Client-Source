/*     */ package net.minecraft.item;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockSlab;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemSlab
/*     */   extends ItemBlock
/*     */ {
/*     */   private final BlockSlab singleSlab;
/*     */   private final BlockSlab doubleSlab;
/*     */   
/*     */   public ItemSlab(Block block, BlockSlab singleSlab, BlockSlab doubleSlab) {
/*  19 */     super(block);
/*  20 */     this.singleSlab = singleSlab;
/*  21 */     this.doubleSlab = doubleSlab;
/*  22 */     setMaxDamage(0);
/*  23 */     setHasSubtypes(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetadata(int damage) {
/*  28 */     return damage;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUnlocalizedName(ItemStack stack) {
/*  33 */     return this.singleSlab.getUnlocalizedName(stack.getMetadata());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  38 */     if (stack.stackSize == 0)
/*     */     {
/*  40 */       return false;
/*     */     }
/*  42 */     if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
/*     */     {
/*  44 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  48 */     Object object = this.singleSlab.getVariant(stack);
/*  49 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  51 */     if (iblockstate.getBlock() == this.singleSlab) {
/*     */       
/*  53 */       IProperty iproperty = this.singleSlab.getVariantProperty();
/*  54 */       Comparable comparable = iblockstate.getValue(iproperty);
/*  55 */       BlockSlab.EnumBlockHalf blockslab$enumblockhalf = (BlockSlab.EnumBlockHalf)iblockstate.getValue((IProperty)BlockSlab.HALF);
/*     */       
/*  57 */       if (((side == EnumFacing.UP && blockslab$enumblockhalf == BlockSlab.EnumBlockHalf.BOTTOM) || (side == EnumFacing.DOWN && blockslab$enumblockhalf == BlockSlab.EnumBlockHalf.TOP)) && comparable == object) {
/*     */         
/*  59 */         IBlockState iblockstate1 = this.doubleSlab.getDefaultState().withProperty(iproperty, comparable);
/*     */         
/*  61 */         if (worldIn.checkNoEntityCollision(this.doubleSlab.getCollisionBoundingBox(worldIn, pos, iblockstate1)) && worldIn.setBlockState(pos, iblockstate1, 3)) {
/*     */           
/*  63 */           worldIn.playSoundEffect((pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F), this.doubleSlab.stepSound.getPlaceSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getFrequency() * 0.8F);
/*  64 */           stack.stackSize--;
/*     */         } 
/*     */         
/*  67 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/*  71 */     return tryPlace(stack, worldIn, pos.offset(side), object) ? true : super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
/*  77 */     BlockPos blockpos = pos;
/*  78 */     IProperty iproperty = this.singleSlab.getVariantProperty();
/*  79 */     Object object = this.singleSlab.getVariant(stack);
/*  80 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  82 */     if (iblockstate.getBlock() == this.singleSlab) {
/*     */       
/*  84 */       boolean flag = (iblockstate.getValue((IProperty)BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP);
/*     */       
/*  86 */       if (((side == EnumFacing.UP && !flag) || (side == EnumFacing.DOWN && flag)) && object == iblockstate.getValue(iproperty))
/*     */       {
/*  88 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  92 */     pos = pos.offset(side);
/*  93 */     IBlockState iblockstate1 = worldIn.getBlockState(pos);
/*  94 */     return (iblockstate1.getBlock() == this.singleSlab && object == iblockstate1.getValue(iproperty)) ? true : super.canPlaceBlockOnSide(worldIn, blockpos, side, player, stack);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean tryPlace(ItemStack stack, World worldIn, BlockPos pos, Object variantInStack) {
/*  99 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/* 101 */     if (iblockstate.getBlock() == this.singleSlab) {
/*     */       
/* 103 */       Comparable comparable = iblockstate.getValue(this.singleSlab.getVariantProperty());
/*     */       
/* 105 */       if (comparable == variantInStack) {
/*     */         
/* 107 */         IBlockState iblockstate1 = this.doubleSlab.getDefaultState().withProperty(this.singleSlab.getVariantProperty(), comparable);
/*     */         
/* 109 */         if (worldIn.checkNoEntityCollision(this.doubleSlab.getCollisionBoundingBox(worldIn, pos, iblockstate1)) && worldIn.setBlockState(pos, iblockstate1, 3)) {
/*     */           
/* 111 */           worldIn.playSoundEffect((pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F), this.doubleSlab.stepSound.getPlaceSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getFrequency() * 0.8F);
/* 112 */           stack.stackSize--;
/*     */         } 
/*     */         
/* 115 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 119 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemSlab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */