/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.EnumDyeColor;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockOre
/*    */   extends Block
/*    */ {
/*    */   public BlockOre() {
/* 20 */     this(Material.rock.getMaterialMapColor());
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockOre(MapColor p_i46390_1_) {
/* 25 */     super(Material.rock, p_i46390_1_);
/* 26 */     setCreativeTab(CreativeTabs.tabBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 31 */     return (this == Blocks.coal_ore) ? Items.coal : ((this == Blocks.diamond_ore) ? Items.diamond : ((this == Blocks.lapis_ore) ? Items.dye : ((this == Blocks.emerald_ore) ? Items.emerald : ((this == Blocks.quartz_ore) ? Items.quartz : Item.getItemFromBlock(this)))));
/*    */   }
/*    */ 
/*    */   
/*    */   public int quantityDropped(Random random) {
/* 36 */     return (this == Blocks.lapis_ore) ? (4 + random.nextInt(5)) : 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public int quantityDroppedWithBonus(int fortune, Random random) {
/* 41 */     if (fortune > 0 && Item.getItemFromBlock(this) != getItemDropped((IBlockState)getBlockState().getValidStates().iterator().next(), random, fortune)) {
/*    */       
/* 43 */       int i = random.nextInt(fortune + 2) - 1;
/*    */       
/* 45 */       if (i < 0)
/*    */       {
/* 47 */         i = 0;
/*    */       }
/*    */       
/* 50 */       return quantityDropped(random) * (i + 1);
/*    */     } 
/*    */ 
/*    */     
/* 54 */     return quantityDropped(random);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
/* 60 */     super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
/*    */     
/* 62 */     if (getItemDropped(state, worldIn.rand, fortune) != Item.getItemFromBlock(this)) {
/*    */       
/* 64 */       int i = 0;
/*    */       
/* 66 */       if (this == Blocks.coal_ore) {
/*    */         
/* 68 */         i = MathHelper.getRandomIntegerInRange(worldIn.rand, 0, 2);
/*    */       }
/* 70 */       else if (this == Blocks.diamond_ore) {
/*    */         
/* 72 */         i = MathHelper.getRandomIntegerInRange(worldIn.rand, 3, 7);
/*    */       }
/* 74 */       else if (this == Blocks.emerald_ore) {
/*    */         
/* 76 */         i = MathHelper.getRandomIntegerInRange(worldIn.rand, 3, 7);
/*    */       }
/* 78 */       else if (this == Blocks.lapis_ore) {
/*    */         
/* 80 */         i = MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
/*    */       }
/* 82 */       else if (this == Blocks.quartz_ore) {
/*    */         
/* 84 */         i = MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
/*    */       } 
/*    */       
/* 87 */       dropXpOnBlockBreak(worldIn, pos, i);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDamageValue(World worldIn, BlockPos pos) {
/* 93 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public int damageDropped(IBlockState state) {
/* 98 */     return (this == Blocks.lapis_ore) ? EnumDyeColor.BLUE.getDyeDamage() : 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockOre.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */