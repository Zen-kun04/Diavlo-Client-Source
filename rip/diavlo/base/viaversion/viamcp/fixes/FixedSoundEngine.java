/*     */ package rip.diavlo.base.viaversion.viamcp.fixes;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ import rip.diavlo.base.viaversion.vialoadingbase.ViaLoadingBase;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FixedSoundEngine
/*     */ {
/*  36 */   private static final Minecraft mc = Minecraft.getMinecraft();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean destroyBlock(World world, BlockPos pos, boolean dropBlock) {
/*  42 */     IBlockState iblockstate = world.getBlockState(pos);
/*  43 */     Block block = iblockstate.getBlock();
/*     */ 
/*     */ 
/*     */     
/*  47 */     world.playAuxSFX(2001, pos, Block.getStateId(iblockstate));
/*     */     
/*  49 */     if (block.getMaterial() == Material.air)
/*     */     {
/*  51 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  55 */     if (dropBlock)
/*     */     {
/*  57 */       block.dropBlockAsItem(world, pos, iblockstate, 0);
/*     */     }
/*     */     
/*  60 */     return world.setBlockState(pos, Blocks.air.getDefaultState(), 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean onItemUse(ItemBlock iblock, ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  70 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  71 */     Block block = iblockstate.getBlock();
/*     */     
/*  73 */     if (!block.isReplaceable(worldIn, pos))
/*     */     {
/*  75 */       pos = pos.offset(side);
/*     */     }
/*     */     
/*  78 */     if (stack.stackSize == 0)
/*     */     {
/*  80 */       return false;
/*     */     }
/*  82 */     if (!playerIn.canPlayerEdit(pos, side, stack))
/*     */     {
/*  84 */       return false;
/*     */     }
/*  86 */     if (worldIn.canBlockBePlaced(iblock.getBlock(), pos, false, side, (Entity)null, stack)) {
/*     */       
/*  88 */       int i = iblock.getMetadata(stack.getMetadata());
/*  89 */       IBlockState iblockstate1 = iblock.getBlock().onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, i, (EntityLivingBase)playerIn);
/*     */       
/*  91 */       if (worldIn.setBlockState(pos, iblockstate1, 3)) {
/*     */         
/*  93 */         iblockstate1 = worldIn.getBlockState(pos);
/*     */         
/*  95 */         if (iblockstate1.getBlock() == iblock.getBlock()) {
/*     */           
/*  97 */           ItemBlock.setTileEntityNBT(worldIn, playerIn, pos, stack);
/*  98 */           iblock.getBlock().onBlockPlacedBy(worldIn, pos, iblockstate1, (EntityLivingBase)playerIn, stack);
/*     */         } 
/*     */         
/* 101 */         if (ViaLoadingBase.getInstance().getTargetVersion().getOriginalVersion() != 47) {
/*     */ 
/*     */           
/* 104 */           mc.theWorld.playSoundAtPos(pos.add(0.5D, 0.5D, 0.5D), (iblock.getBlock()).stepSound.getPlaceSound(), ((iblock.getBlock()).stepSound.getVolume() + 1.0F) / 2.0F, (iblock.getBlock()).stepSound.getFrequency() * 0.8F, false);
/*     */         }
/*     */         else {
/*     */           
/* 108 */           worldIn.playSoundEffect((pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F), (iblock.getBlock()).stepSound.getPlaceSound(), ((iblock.getBlock()).stepSound.getVolume() + 1.0F) / 2.0F, (iblock.getBlock()).stepSound.getFrequency() * 0.8F);
/*     */         } 
/*     */         
/* 111 */         stack.stackSize--;
/*     */       } 
/*     */       
/* 114 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 118 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\viaversion\viamcp\fixes\FixedSoundEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */