/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.command.server.CommandBlockLogic;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityCommandBlock;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockCommandBlock
/*     */   extends BlockContainer {
/*  22 */   public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");
/*     */ 
/*     */   
/*     */   public BlockCommandBlock() {
/*  26 */     super(Material.iron, MapColor.adobeColor);
/*  27 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)TRIGGERED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta) {
/*  32 */     return (TileEntity)new TileEntityCommandBlock();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/*  37 */     if (!worldIn.isRemote) {
/*     */       
/*  39 */       boolean flag = worldIn.isBlockPowered(pos);
/*  40 */       boolean flag1 = ((Boolean)state.getValue((IProperty)TRIGGERED)).booleanValue();
/*     */       
/*  42 */       if (flag && !flag1) {
/*     */         
/*  44 */         worldIn.setBlockState(pos, state.withProperty((IProperty)TRIGGERED, Boolean.valueOf(true)), 4);
/*  45 */         worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*     */       }
/*  47 */       else if (!flag && flag1) {
/*     */         
/*  49 */         worldIn.setBlockState(pos, state.withProperty((IProperty)TRIGGERED, Boolean.valueOf(false)), 4);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  56 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/*  58 */     if (tileentity instanceof TileEntityCommandBlock) {
/*     */       
/*  60 */       ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().trigger(worldIn);
/*  61 */       worldIn.updateComparatorOutputLevel(pos, this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int tickRate(World worldIn) {
/*  67 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  72 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*  73 */     return (tileentity instanceof TileEntityCommandBlock) ? ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().tryOpenEditCommandBlock(playerIn) : false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComparatorInputOverride() {
/*  78 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getComparatorInputOverride(World worldIn, BlockPos pos) {
/*  83 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*  84 */     return (tileentity instanceof TileEntityCommandBlock) ? ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().getSuccessCount() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
/*  89 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/*  91 */     if (tileentity instanceof TileEntityCommandBlock) {
/*     */       
/*  93 */       CommandBlockLogic commandblocklogic = ((TileEntityCommandBlock)tileentity).getCommandBlockLogic();
/*     */       
/*  95 */       if (stack.hasDisplayName())
/*     */       {
/*  97 */         commandblocklogic.setName(stack.getDisplayName());
/*     */       }
/*     */       
/* 100 */       if (!worldIn.isRemote)
/*     */       {
/* 102 */         commandblocklogic.setTrackOutput(worldIn.getGameRules().getBoolean("sendCommandFeedback"));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int quantityDropped(Random random) {
/* 109 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRenderType() {
/* 114 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 119 */     return getDefaultState().withProperty((IProperty)TRIGGERED, Boolean.valueOf(((meta & 0x1) > 0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 124 */     int i = 0;
/*     */     
/* 126 */     if (((Boolean)state.getValue((IProperty)TRIGGERED)).booleanValue())
/*     */     {
/* 128 */       i |= 0x1;
/*     */     }
/*     */     
/* 131 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 136 */     return new BlockState(this, new IProperty[] { (IProperty)TRIGGERED });
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/* 141 */     return getDefaultState().withProperty((IProperty)TRIGGERED, Boolean.valueOf(false));
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockCommandBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */