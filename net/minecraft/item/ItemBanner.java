/*     */ package net.minecraft.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.BlockStandingSign;
/*     */ import net.minecraft.block.BlockWallSign;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityBanner;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemBanner
/*     */   extends ItemBlock {
/*     */   public ItemBanner() {
/*  24 */     super(Blocks.standing_banner);
/*  25 */     this.maxStackSize = 16;
/*  26 */     setCreativeTab(CreativeTabs.tabDecorations);
/*  27 */     setHasSubtypes(true);
/*  28 */     setMaxDamage(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  33 */     if (side == EnumFacing.DOWN)
/*     */     {
/*  35 */       return false;
/*     */     }
/*  37 */     if (!worldIn.getBlockState(pos).getBlock().getMaterial().isSolid())
/*     */     {
/*  39 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  43 */     pos = pos.offset(side);
/*     */     
/*  45 */     if (!playerIn.canPlayerEdit(pos, side, stack))
/*     */     {
/*  47 */       return false;
/*     */     }
/*  49 */     if (!Blocks.standing_banner.canPlaceBlockAt(worldIn, pos))
/*     */     {
/*  51 */       return false;
/*     */     }
/*  53 */     if (worldIn.isRemote)
/*     */     {
/*  55 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  59 */     if (side == EnumFacing.UP) {
/*     */       
/*  61 */       int i = MathHelper.floor_double(((playerIn.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 0xF;
/*  62 */       worldIn.setBlockState(pos, Blocks.standing_banner.getDefaultState().withProperty((IProperty)BlockStandingSign.ROTATION, Integer.valueOf(i)), 3);
/*     */     }
/*     */     else {
/*     */       
/*  66 */       worldIn.setBlockState(pos, Blocks.wall_banner.getDefaultState().withProperty((IProperty)BlockWallSign.FACING, (Comparable)side), 3);
/*     */     } 
/*     */     
/*  69 */     stack.stackSize--;
/*  70 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/*  72 */     if (tileentity instanceof TileEntityBanner)
/*     */     {
/*  74 */       ((TileEntityBanner)tileentity).setItemValues(stack);
/*     */     }
/*     */     
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getItemStackDisplayName(ItemStack stack) {
/*  84 */     String s = "item.banner.";
/*  85 */     EnumDyeColor enumdyecolor = getBaseColor(stack);
/*  86 */     s = s + enumdyecolor.getUnlocalizedName() + ".name";
/*  87 */     return StatCollector.translateToLocal(s);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
/*  92 */     NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
/*     */     
/*  94 */     if (nbttagcompound != null && nbttagcompound.hasKey("Patterns")) {
/*     */       
/*  96 */       NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);
/*     */       
/*  98 */       for (int i = 0; i < nbttaglist.tagCount() && i < 6; i++) {
/*     */         
/* 100 */         NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
/* 101 */         EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(nbttagcompound1.getInteger("Color"));
/* 102 */         TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern = TileEntityBanner.EnumBannerPattern.getPatternByID(nbttagcompound1.getString("Pattern"));
/*     */         
/* 104 */         if (tileentitybanner$enumbannerpattern != null)
/*     */         {
/* 106 */           tooltip.add(StatCollector.translateToLocal("item.banner." + tileentitybanner$enumbannerpattern.getPatternName() + "." + enumdyecolor.getUnlocalizedName()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColorFromItemStack(ItemStack stack, int renderPass) {
/* 114 */     if (renderPass == 0)
/*     */     {
/* 116 */       return 16777215;
/*     */     }
/*     */ 
/*     */     
/* 120 */     EnumDyeColor enumdyecolor = getBaseColor(stack);
/* 121 */     return (enumdyecolor.getMapColor()).colorValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
/* 127 */     for (EnumDyeColor enumdyecolor : EnumDyeColor.values()) {
/*     */       
/* 129 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 130 */       TileEntityBanner.setBaseColorAndPatterns(nbttagcompound, enumdyecolor.getDyeDamage(), (NBTTagList)null);
/* 131 */       NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 132 */       nbttagcompound1.setTag("BlockEntityTag", (NBTBase)nbttagcompound);
/* 133 */       ItemStack itemstack = new ItemStack(itemIn, 1, enumdyecolor.getDyeDamage());
/* 134 */       itemstack.setTagCompound(nbttagcompound1);
/* 135 */       subItems.add(itemstack);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public CreativeTabs getCreativeTab() {
/* 141 */     return CreativeTabs.tabDecorations;
/*     */   }
/*     */ 
/*     */   
/*     */   private EnumDyeColor getBaseColor(ItemStack stack) {
/* 146 */     NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
/* 147 */     EnumDyeColor enumdyecolor = null;
/*     */     
/* 149 */     if (nbttagcompound != null && nbttagcompound.hasKey("Base")) {
/*     */       
/* 151 */       enumdyecolor = EnumDyeColor.byDyeDamage(nbttagcompound.getInteger("Base"));
/*     */     }
/*     */     else {
/*     */       
/* 155 */       enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());
/*     */     } 
/*     */     
/* 158 */     return enumdyecolor;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemBanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */