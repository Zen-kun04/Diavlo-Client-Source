/*     */ package net.minecraft.item;
/*     */ 
/*     */ import net.minecraft.block.BlockDispenser;
/*     */ import net.minecraft.block.BlockRailBase;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
/*     */ import net.minecraft.dispenser.IBehaviorDispenseItem;
/*     */ import net.minecraft.dispenser.IBlockSource;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemMinecart extends Item {
/*  19 */   private static final IBehaviorDispenseItem dispenserMinecartBehavior = (IBehaviorDispenseItem)new BehaviorDefaultDispenseItem()
/*     */     {
/*  21 */       private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();
/*     */       public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/*     */         double d3;
/*  24 */         EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
/*  25 */         World world = source.getWorld();
/*  26 */         double d0 = source.getX() + enumfacing.getFrontOffsetX() * 1.125D;
/*  27 */         double d1 = Math.floor(source.getY()) + enumfacing.getFrontOffsetY();
/*  28 */         double d2 = source.getZ() + enumfacing.getFrontOffsetZ() * 1.125D;
/*  29 */         BlockPos blockpos = source.getBlockPos().offset(enumfacing);
/*  30 */         IBlockState iblockstate = world.getBlockState(blockpos);
/*  31 */         BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (iblockstate.getBlock() instanceof BlockRailBase) ? (BlockRailBase.EnumRailDirection)iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
/*     */ 
/*     */         
/*  34 */         if (BlockRailBase.isRailBlock(iblockstate)) {
/*     */           
/*  36 */           if (blockrailbase$enumraildirection.isAscending())
/*     */           {
/*  38 */             d3 = 0.6D;
/*     */           }
/*     */           else
/*     */           {
/*  42 */             d3 = 0.1D;
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/*  47 */           if (iblockstate.getBlock().getMaterial() != Material.air || !BlockRailBase.isRailBlock(world.getBlockState(blockpos.down())))
/*     */           {
/*  49 */             return this.behaviourDefaultDispenseItem.dispense(source, stack);
/*     */           }
/*     */           
/*  52 */           IBlockState iblockstate1 = world.getBlockState(blockpos.down());
/*  53 */           BlockRailBase.EnumRailDirection blockrailbase$enumraildirection1 = (iblockstate1.getBlock() instanceof BlockRailBase) ? (BlockRailBase.EnumRailDirection)iblockstate1.getValue(((BlockRailBase)iblockstate1.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
/*     */           
/*  55 */           if (enumfacing != EnumFacing.DOWN && blockrailbase$enumraildirection1.isAscending()) {
/*     */             
/*  57 */             d3 = -0.4D;
/*     */           }
/*     */           else {
/*     */             
/*  61 */             d3 = -0.9D;
/*     */           } 
/*     */         } 
/*     */         
/*  65 */         EntityMinecart entityminecart = EntityMinecart.getMinecart(world, d0, d1 + d3, d2, ((ItemMinecart)stack.getItem()).minecartType);
/*     */         
/*  67 */         if (stack.hasDisplayName())
/*     */         {
/*  69 */           entityminecart.setCustomNameTag(stack.getDisplayName());
/*     */         }
/*     */         
/*  72 */         world.spawnEntityInWorld((Entity)entityminecart);
/*  73 */         stack.splitStack(1);
/*  74 */         return stack;
/*     */       }
/*     */       
/*     */       protected void playDispenseSound(IBlockSource source) {
/*  78 */         source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
/*     */       }
/*     */     };
/*     */   
/*     */   private final EntityMinecart.EnumMinecartType minecartType;
/*     */   
/*     */   public ItemMinecart(EntityMinecart.EnumMinecartType type) {
/*  85 */     this.maxStackSize = 1;
/*  86 */     this.minecartType = type;
/*  87 */     setCreativeTab(CreativeTabs.tabTransport);
/*  88 */     BlockDispenser.dispenseBehaviorRegistry.putObject(this, dispenserMinecartBehavior);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  93 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  95 */     if (BlockRailBase.isRailBlock(iblockstate)) {
/*     */       
/*  97 */       if (!worldIn.isRemote) {
/*     */         
/*  99 */         BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (iblockstate.getBlock() instanceof BlockRailBase) ? (BlockRailBase.EnumRailDirection)iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
/* 100 */         double d0 = 0.0D;
/*     */         
/* 102 */         if (blockrailbase$enumraildirection.isAscending())
/*     */         {
/* 104 */           d0 = 0.5D;
/*     */         }
/*     */         
/* 107 */         EntityMinecart entityminecart = EntityMinecart.getMinecart(worldIn, pos.getX() + 0.5D, pos.getY() + 0.0625D + d0, pos.getZ() + 0.5D, this.minecartType);
/*     */         
/* 109 */         if (stack.hasDisplayName())
/*     */         {
/* 111 */           entityminecart.setCustomNameTag(stack.getDisplayName());
/*     */         }
/*     */         
/* 114 */         worldIn.spawnEntityInWorld((Entity)entityminecart);
/*     */       } 
/*     */       
/* 117 */       stack.stackSize--;
/* 118 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 122 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemMinecart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */