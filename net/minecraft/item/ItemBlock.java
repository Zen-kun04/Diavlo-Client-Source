/*     */ package net.minecraft.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ import rip.diavlo.base.viaversion.viamcp.fixes.FixedSoundEngine;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ItemBlock
/*     */   extends Item
/*     */ {
/*     */   protected final Block block;
/*     */   
/*     */   public ItemBlock(Block block) {
/*  25 */     this.block = block;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemBlock setUnlocalizedName(String unlocalizedName) {
/*  30 */     super.setUnlocalizedName(unlocalizedName);
/*  31 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  37 */     return FixedSoundEngine.onItemUse(this, stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
/*     */   }
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
/*     */   public static boolean setTileEntityNBT(World worldIn, EntityPlayer pos, BlockPos stack, ItemStack p_179224_3_) {
/*  87 */     MinecraftServer minecraftserver = MinecraftServer.getServer();
/*     */     
/*  89 */     if (minecraftserver == null)
/*     */     {
/*  91 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  95 */     if (p_179224_3_.hasTagCompound() && p_179224_3_.getTagCompound().hasKey("BlockEntityTag", 10)) {
/*     */       
/*  97 */       TileEntity tileentity = worldIn.getTileEntity(stack);
/*     */       
/*  99 */       if (tileentity != null) {
/*     */         
/* 101 */         if (!worldIn.isRemote && tileentity.func_183000_F() && !minecraftserver.getConfigurationManager().canSendCommands(pos.getGameProfile()))
/*     */         {
/* 103 */           return false;
/*     */         }
/*     */         
/* 106 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 107 */         NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttagcompound.copy();
/* 108 */         tileentity.writeToNBT(nbttagcompound);
/* 109 */         NBTTagCompound nbttagcompound2 = (NBTTagCompound)p_179224_3_.getTagCompound().getTag("BlockEntityTag");
/* 110 */         nbttagcompound.merge(nbttagcompound2);
/* 111 */         nbttagcompound.setInteger("x", stack.getX());
/* 112 */         nbttagcompound.setInteger("y", stack.getY());
/* 113 */         nbttagcompound.setInteger("z", stack.getZ());
/*     */         
/* 115 */         if (!nbttagcompound.equals(nbttagcompound1)) {
/*     */           
/* 117 */           tileentity.readFromNBT(nbttagcompound);
/* 118 */           tileentity.markDirty();
/* 119 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
/* 130 */     Block block = worldIn.getBlockState(pos).getBlock();
/*     */     
/* 132 */     if (block == Blocks.snow_layer) {
/*     */       
/* 134 */       side = EnumFacing.UP;
/*     */     }
/* 136 */     else if (!block.isReplaceable(worldIn, pos)) {
/*     */       
/* 138 */       pos = pos.offset(side);
/*     */     } 
/*     */     
/* 141 */     return worldIn.canBlockBePlaced(this.block, pos, false, side, (Entity)null, stack);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUnlocalizedName(ItemStack stack) {
/* 146 */     return this.block.getUnlocalizedName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUnlocalizedName() {
/* 151 */     return this.block.getUnlocalizedName();
/*     */   }
/*     */ 
/*     */   
/*     */   public CreativeTabs getCreativeTab() {
/* 156 */     return this.block.getCreativeTabToDisplayOn();
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
/* 161 */     this.block.getSubBlocks(itemIn, tab, subItems);
/*     */   }
/*     */ 
/*     */   
/*     */   public Block getBlock() {
/* 166 */     return this.block;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */