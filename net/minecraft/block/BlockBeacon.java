/*     */ package net.minecraft.block;
/*     */ 
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityBeacon;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.HttpUtil;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ 
/*     */ public class BlockBeacon
/*     */   extends BlockContainer {
/*     */   public BlockBeacon() {
/*  26 */     super(Material.glass, MapColor.diamondColor);
/*  27 */     setHardness(3.0F);
/*  28 */     setCreativeTab(CreativeTabs.tabMisc);
/*     */   }
/*     */ 
/*     */   
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta) {
/*  33 */     return (TileEntity)new TileEntityBeacon();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  38 */     if (worldIn.isRemote)
/*     */     {
/*  40 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  44 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/*  46 */     if (tileentity instanceof TileEntityBeacon) {
/*     */       
/*  48 */       playerIn.displayGUIChest((IInventory)tileentity);
/*  49 */       playerIn.triggerAchievement(StatList.field_181730_N);
/*     */     } 
/*     */     
/*  52 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  58 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  63 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRenderType() {
/*  68 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
/*  73 */     super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
/*     */     
/*  75 */     if (stack.hasDisplayName()) {
/*     */       
/*  77 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/*  79 */       if (tileentity instanceof TileEntityBeacon)
/*     */       {
/*  81 */         ((TileEntityBeacon)tileentity).setName(stack.getDisplayName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/*  88 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/*  90 */     if (tileentity instanceof TileEntityBeacon) {
/*     */       
/*  92 */       ((TileEntityBeacon)tileentity).updateBeacon();
/*  93 */       worldIn.addBlockEvent(pos, this, 1, 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer() {
/*  99 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void updateColorAsync(final World worldIn, final BlockPos glassPos) {
/* 104 */     HttpUtil.field_180193_a.submit(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 108 */             Chunk chunk = worldIn.getChunkFromBlockCoords(glassPos);
/*     */             
/* 110 */             for (int i = glassPos.getY() - 1; i >= 0; i--) {
/*     */               
/* 112 */               final BlockPos blockpos = new BlockPos(glassPos.getX(), i, glassPos.getZ());
/*     */               
/* 114 */               if (!chunk.canSeeSky(blockpos)) {
/*     */                 break;
/*     */               }
/*     */ 
/*     */               
/* 119 */               IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*     */               
/* 121 */               if (iblockstate.getBlock() == Blocks.beacon)
/*     */               {
/* 123 */                 ((WorldServer)worldIn).addScheduledTask(new Runnable()
/*     */                     {
/*     */                       public void run()
/*     */                       {
/* 127 */                         TileEntity tileentity = worldIn.getTileEntity(blockpos);
/*     */                         
/* 129 */                         if (tileentity instanceof TileEntityBeacon) {
/*     */                           
/* 131 */                           ((TileEntityBeacon)tileentity).updateBeacon();
/* 132 */                           worldIn.addBlockEvent(blockpos, Blocks.beacon, 1, 0);
/*     */                         } 
/*     */                       }
/*     */                     });
/*     */               }
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockBeacon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */