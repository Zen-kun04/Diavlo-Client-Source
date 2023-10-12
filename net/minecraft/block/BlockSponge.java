/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.util.Tuple;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockSponge extends Block {
/*  26 */   public static final PropertyBool WET = PropertyBool.create("wet");
/*     */ 
/*     */   
/*     */   protected BlockSponge() {
/*  30 */     super(Material.sponge);
/*  31 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)WET, Boolean.valueOf(false)));
/*  32 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLocalizedName() {
/*  37 */     return StatCollector.translateToLocal(getUnlocalizedName() + ".dry.name");
/*     */   }
/*     */ 
/*     */   
/*     */   public int damageDropped(IBlockState state) {
/*  42 */     return ((Boolean)state.getValue((IProperty)WET)).booleanValue() ? 1 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/*  47 */     tryAbsorb(worldIn, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/*  52 */     tryAbsorb(worldIn, pos, state);
/*  53 */     super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tryAbsorb(World worldIn, BlockPos pos, IBlockState state) {
/*  58 */     if (!((Boolean)state.getValue((IProperty)WET)).booleanValue() && absorb(worldIn, pos)) {
/*     */       
/*  60 */       worldIn.setBlockState(pos, state.withProperty((IProperty)WET, Boolean.valueOf(true)), 2);
/*  61 */       worldIn.playAuxSFX(2001, pos, Block.getIdFromBlock(Blocks.water));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean absorb(World worldIn, BlockPos pos) {
/*  67 */     Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
/*  68 */     ArrayList<BlockPos> arraylist = Lists.newArrayList();
/*  69 */     queue.add(new Tuple(pos, Integer.valueOf(0)));
/*  70 */     int i = 0;
/*     */     
/*  72 */     while (!queue.isEmpty()) {
/*     */       
/*  74 */       Tuple<BlockPos, Integer> tuple = queue.poll();
/*  75 */       BlockPos blockpos = (BlockPos)tuple.getFirst();
/*  76 */       int j = ((Integer)tuple.getSecond()).intValue();
/*     */       
/*  78 */       for (EnumFacing enumfacing : EnumFacing.values()) {
/*     */         
/*  80 */         BlockPos blockpos1 = blockpos.offset(enumfacing);
/*     */         
/*  82 */         if (worldIn.getBlockState(blockpos1).getBlock().getMaterial() == Material.water) {
/*     */           
/*  84 */           worldIn.setBlockState(blockpos1, Blocks.air.getDefaultState(), 2);
/*  85 */           arraylist.add(blockpos1);
/*  86 */           i++;
/*     */           
/*  88 */           if (j < 6)
/*     */           {
/*  90 */             queue.add(new Tuple(blockpos1, Integer.valueOf(j + 1)));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/*  95 */       if (i > 64) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 101 */     for (BlockPos blockpos2 : arraylist)
/*     */     {
/* 103 */       worldIn.notifyNeighborsOfStateChange(blockpos2, Blocks.air);
/*     */     }
/*     */     
/* 106 */     return (i > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
/* 111 */     list.add(new ItemStack(itemIn, 1, 0));
/* 112 */     list.add(new ItemStack(itemIn, 1, 1));
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getStateFromMeta(int meta) {
/* 117 */     return getDefaultState().withProperty((IProperty)WET, Boolean.valueOf(((meta & 0x1) == 1)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetaFromState(IBlockState state) {
/* 122 */     return ((Boolean)state.getValue((IProperty)WET)).booleanValue() ? 1 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState createBlockState() {
/* 127 */     return new BlockState(this, new IProperty[] { (IProperty)WET });
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 132 */     if (((Boolean)state.getValue((IProperty)WET)).booleanValue()) {
/*     */       
/* 134 */       EnumFacing enumfacing = EnumFacing.random(rand);
/*     */       
/* 136 */       if (enumfacing != EnumFacing.UP && !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, pos.offset(enumfacing))) {
/*     */         
/* 138 */         double d0 = pos.getX();
/* 139 */         double d1 = pos.getY();
/* 140 */         double d2 = pos.getZ();
/*     */         
/* 142 */         if (enumfacing == EnumFacing.DOWN) {
/*     */           
/* 144 */           d1 -= 0.05D;
/* 145 */           d0 += rand.nextDouble();
/* 146 */           d2 += rand.nextDouble();
/*     */         }
/*     */         else {
/*     */           
/* 150 */           d1 += rand.nextDouble() * 0.8D;
/*     */           
/* 152 */           if (enumfacing.getAxis() == EnumFacing.Axis.X) {
/*     */             
/* 154 */             d2 += rand.nextDouble();
/*     */             
/* 156 */             if (enumfacing == EnumFacing.EAST)
/*     */             {
/* 158 */               d0++;
/*     */             }
/*     */             else
/*     */             {
/* 162 */               d0 += 0.05D;
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 167 */             d0 += rand.nextDouble();
/*     */             
/* 169 */             if (enumfacing == EnumFacing.SOUTH) {
/*     */               
/* 171 */               d2++;
/*     */             }
/*     */             else {
/*     */               
/* 175 */               d2 += 0.05D;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 180 */         worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockSponge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */