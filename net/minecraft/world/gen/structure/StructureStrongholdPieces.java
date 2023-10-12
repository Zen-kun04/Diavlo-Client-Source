/*      */ package net.minecraft.world.gen.structure;
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import net.minecraft.block.BlockEndPortalFrame;
/*      */ import net.minecraft.block.BlockSilverfish;
/*      */ import net.minecraft.block.BlockStoneBrick;
/*      */ import net.minecraft.block.BlockStoneSlab;
/*      */ import net.minecraft.block.properties.IProperty;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.tileentity.TileEntityMobSpawner;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.Vec3i;
/*      */ import net.minecraft.util.WeightedRandomChestContent;
/*      */ import net.minecraft.world.World;
/*      */ 
/*      */ public class StructureStrongholdPieces {
/*   23 */   private static final PieceWeight[] pieceWeightArray = new PieceWeight[] { new PieceWeight((Class)Straight.class, 40, 0), new PieceWeight((Class)Prison.class, 5, 5), new PieceWeight((Class)LeftTurn.class, 20, 0), new PieceWeight((Class)RightTurn.class, 20, 0), new PieceWeight((Class)RoomCrossing.class, 10, 6), new PieceWeight((Class)StairsStraight.class, 5, 5), new PieceWeight((Class)Stairs.class, 5, 5), new PieceWeight((Class)Crossing.class, 5, 4), new PieceWeight((Class)ChestCorridor.class, 5, 4), new PieceWeight(Library.class, 10, 2)
/*      */       {
/*      */         public boolean canSpawnMoreStructuresOfType(int p_75189_1_)
/*      */         {
/*   27 */           return (super.canSpawnMoreStructuresOfType(p_75189_1_) && p_75189_1_ > 4);
/*      */         }
/*      */       }, 
/*      */       new PieceWeight(PortalRoom.class, 20, 1)
/*      */       {
/*      */         public boolean canSpawnMoreStructuresOfType(int p_75189_1_) {
/*   33 */           return (super.canSpawnMoreStructuresOfType(p_75189_1_) && p_75189_1_ > 5);
/*      */         }
/*      */       } };
/*      */   
/*      */   private static List<PieceWeight> structurePieceList;
/*      */   private static Class<? extends Stronghold> strongComponentType;
/*      */   static int totalWeight;
/*   40 */   private static final Stones strongholdStones = new Stones();
/*      */ 
/*      */   
/*      */   public static void registerStrongholdPieces() {
/*   44 */     MapGenStructureIO.registerStructureComponent((Class)ChestCorridor.class, "SHCC");
/*   45 */     MapGenStructureIO.registerStructureComponent((Class)Corridor.class, "SHFC");
/*   46 */     MapGenStructureIO.registerStructureComponent((Class)Crossing.class, "SH5C");
/*   47 */     MapGenStructureIO.registerStructureComponent((Class)LeftTurn.class, "SHLT");
/*   48 */     MapGenStructureIO.registerStructureComponent((Class)Library.class, "SHLi");
/*   49 */     MapGenStructureIO.registerStructureComponent((Class)PortalRoom.class, "SHPR");
/*   50 */     MapGenStructureIO.registerStructureComponent((Class)Prison.class, "SHPH");
/*   51 */     MapGenStructureIO.registerStructureComponent((Class)RightTurn.class, "SHRT");
/*   52 */     MapGenStructureIO.registerStructureComponent((Class)RoomCrossing.class, "SHRC");
/*   53 */     MapGenStructureIO.registerStructureComponent((Class)Stairs.class, "SHSD");
/*   54 */     MapGenStructureIO.registerStructureComponent((Class)Stairs2.class, "SHStart");
/*   55 */     MapGenStructureIO.registerStructureComponent((Class)Straight.class, "SHS");
/*   56 */     MapGenStructureIO.registerStructureComponent((Class)StairsStraight.class, "SHSSD");
/*      */   }
/*      */ 
/*      */   
/*      */   public static void prepareStructurePieces() {
/*   61 */     structurePieceList = Lists.newArrayList();
/*      */     
/*   63 */     for (PieceWeight structurestrongholdpieces$pieceweight : pieceWeightArray) {
/*      */       
/*   65 */       structurestrongholdpieces$pieceweight.instancesSpawned = 0;
/*   66 */       structurePieceList.add(structurestrongholdpieces$pieceweight);
/*      */     } 
/*      */     
/*   69 */     strongComponentType = null;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean canAddStructurePieces() {
/*   74 */     boolean flag = false;
/*   75 */     totalWeight = 0;
/*      */     
/*   77 */     for (PieceWeight structurestrongholdpieces$pieceweight : structurePieceList) {
/*      */       
/*   79 */       if (structurestrongholdpieces$pieceweight.instancesLimit > 0 && structurestrongholdpieces$pieceweight.instancesSpawned < structurestrongholdpieces$pieceweight.instancesLimit)
/*      */       {
/*   81 */         flag = true;
/*      */       }
/*      */       
/*   84 */       totalWeight += structurestrongholdpieces$pieceweight.pieceWeight;
/*      */     } 
/*      */     
/*   87 */     return flag;
/*      */   }
/*      */ 
/*      */   
/*      */   private static Stronghold func_175954_a(Class<? extends Stronghold> p_175954_0_, List<StructureComponent> p_175954_1_, Random p_175954_2_, int p_175954_3_, int p_175954_4_, int p_175954_5_, EnumFacing p_175954_6_, int p_175954_7_) {
/*   92 */     Stronghold structurestrongholdpieces$stronghold = null;
/*      */     
/*   94 */     if (p_175954_0_ == Straight.class) {
/*      */       
/*   96 */       structurestrongholdpieces$stronghold = Straight.func_175862_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*   98 */     else if (p_175954_0_ == Prison.class) {
/*      */       
/*  100 */       structurestrongholdpieces$stronghold = Prison.func_175860_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  102 */     else if (p_175954_0_ == LeftTurn.class) {
/*      */       
/*  104 */       structurestrongholdpieces$stronghold = LeftTurn.func_175867_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  106 */     else if (p_175954_0_ == RightTurn.class) {
/*      */       
/*  108 */       structurestrongholdpieces$stronghold = RightTurn.func_175867_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  110 */     else if (p_175954_0_ == RoomCrossing.class) {
/*      */       
/*  112 */       structurestrongholdpieces$stronghold = RoomCrossing.func_175859_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  114 */     else if (p_175954_0_ == StairsStraight.class) {
/*      */       
/*  116 */       structurestrongholdpieces$stronghold = StairsStraight.func_175861_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  118 */     else if (p_175954_0_ == Stairs.class) {
/*      */       
/*  120 */       structurestrongholdpieces$stronghold = Stairs.func_175863_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  122 */     else if (p_175954_0_ == Crossing.class) {
/*      */       
/*  124 */       structurestrongholdpieces$stronghold = Crossing.func_175866_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  126 */     else if (p_175954_0_ == ChestCorridor.class) {
/*      */       
/*  128 */       structurestrongholdpieces$stronghold = ChestCorridor.func_175868_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  130 */     else if (p_175954_0_ == Library.class) {
/*      */       
/*  132 */       structurestrongholdpieces$stronghold = Library.func_175864_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     }
/*  134 */     else if (p_175954_0_ == PortalRoom.class) {
/*      */       
/*  136 */       structurestrongholdpieces$stronghold = PortalRoom.func_175865_a(p_175954_1_, p_175954_2_, p_175954_3_, p_175954_4_, p_175954_5_, p_175954_6_, p_175954_7_);
/*      */     } 
/*      */     
/*  139 */     return structurestrongholdpieces$stronghold;
/*      */   }
/*      */ 
/*      */   
/*      */   private static Stronghold func_175955_b(Stairs2 p_175955_0_, List<StructureComponent> p_175955_1_, Random p_175955_2_, int p_175955_3_, int p_175955_4_, int p_175955_5_, EnumFacing p_175955_6_, int p_175955_7_) {
/*  144 */     if (!canAddStructurePieces())
/*      */     {
/*  146 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  150 */     if (strongComponentType != null) {
/*      */       
/*  152 */       Stronghold structurestrongholdpieces$stronghold = func_175954_a(strongComponentType, p_175955_1_, p_175955_2_, p_175955_3_, p_175955_4_, p_175955_5_, p_175955_6_, p_175955_7_);
/*  153 */       strongComponentType = null;
/*      */       
/*  155 */       if (structurestrongholdpieces$stronghold != null)
/*      */       {
/*  157 */         return structurestrongholdpieces$stronghold;
/*      */       }
/*      */     } 
/*      */     
/*  161 */     int j = 0;
/*      */     
/*  163 */     while (j < 5) {
/*      */       
/*  165 */       j++;
/*  166 */       int i = p_175955_2_.nextInt(totalWeight);
/*      */       
/*  168 */       for (PieceWeight structurestrongholdpieces$pieceweight : structurePieceList) {
/*      */         
/*  170 */         i -= structurestrongholdpieces$pieceweight.pieceWeight;
/*      */         
/*  172 */         if (i < 0) {
/*      */           
/*  174 */           if (!structurestrongholdpieces$pieceweight.canSpawnMoreStructuresOfType(p_175955_7_) || structurestrongholdpieces$pieceweight == p_175955_0_.strongholdPieceWeight) {
/*      */             break;
/*      */           }
/*      */ 
/*      */           
/*  179 */           Stronghold structurestrongholdpieces$stronghold1 = func_175954_a(structurestrongholdpieces$pieceweight.pieceClass, p_175955_1_, p_175955_2_, p_175955_3_, p_175955_4_, p_175955_5_, p_175955_6_, p_175955_7_);
/*      */           
/*  181 */           if (structurestrongholdpieces$stronghold1 != null) {
/*      */             
/*  183 */             structurestrongholdpieces$pieceweight.instancesSpawned++;
/*  184 */             p_175955_0_.strongholdPieceWeight = structurestrongholdpieces$pieceweight;
/*      */             
/*  186 */             if (!structurestrongholdpieces$pieceweight.canSpawnMoreStructures())
/*      */             {
/*  188 */               structurePieceList.remove(structurestrongholdpieces$pieceweight);
/*      */             }
/*      */             
/*  191 */             return structurestrongholdpieces$stronghold1;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  197 */     StructureBoundingBox structureboundingbox = Corridor.func_175869_a(p_175955_1_, p_175955_2_, p_175955_3_, p_175955_4_, p_175955_5_, p_175955_6_);
/*      */     
/*  199 */     if (structureboundingbox != null && structureboundingbox.minY > 1)
/*      */     {
/*  201 */       return new Corridor(p_175955_7_, p_175955_2_, structureboundingbox, p_175955_6_);
/*      */     }
/*      */ 
/*      */     
/*  205 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static StructureComponent func_175953_c(Stairs2 p_175953_0_, List<StructureComponent> p_175953_1_, Random p_175953_2_, int p_175953_3_, int p_175953_4_, int p_175953_5_, EnumFacing p_175953_6_, int p_175953_7_) {
/*  212 */     if (p_175953_7_ > 50)
/*      */     {
/*  214 */       return null;
/*      */     }
/*  216 */     if (Math.abs(p_175953_3_ - (p_175953_0_.getBoundingBox()).minX) <= 112 && Math.abs(p_175953_5_ - (p_175953_0_.getBoundingBox()).minZ) <= 112) {
/*      */       
/*  218 */       StructureComponent structurecomponent = func_175955_b(p_175953_0_, p_175953_1_, p_175953_2_, p_175953_3_, p_175953_4_, p_175953_5_, p_175953_6_, p_175953_7_ + 1);
/*      */       
/*  220 */       if (structurecomponent != null) {
/*      */         
/*  222 */         p_175953_1_.add(structurecomponent);
/*  223 */         p_175953_0_.field_75026_c.add(structurecomponent);
/*      */       } 
/*      */       
/*  226 */       return structurecomponent;
/*      */     } 
/*      */ 
/*      */     
/*  230 */     return null;
/*      */   }
/*      */   
/*      */   public static class ChestCorridor
/*      */     extends Stronghold
/*      */   {
/*  236 */     private static final List<WeightedRandomChestContent> strongholdChestContents = Lists.newArrayList((Object[])new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.ender_pearl, 0, 1, 1, 10), new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 5), new WeightedRandomChestContent((Item)Items.iron_chestplate, 0, 1, 1, 5), new WeightedRandomChestContent((Item)Items.iron_helmet, 0, 1, 1, 5), new WeightedRandomChestContent((Item)Items.iron_leggings, 0, 1, 1, 5), new WeightedRandomChestContent((Item)Items.iron_boots, 0, 1, 1, 5), new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 1), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1) });
/*      */     
/*      */     private boolean hasMadeChest;
/*      */ 
/*      */     
/*      */     public ChestCorridor() {}
/*      */ 
/*      */     
/*      */     public ChestCorridor(int p_i45582_1_, Random p_i45582_2_, StructureBoundingBox p_i45582_3_, EnumFacing p_i45582_4_) {
/*  245 */       super(p_i45582_1_);
/*  246 */       this.coordBaseMode = p_i45582_4_;
/*  247 */       this.field_143013_d = getRandomDoor(p_i45582_2_);
/*  248 */       this.boundingBox = p_i45582_3_;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound) {
/*  253 */       super.writeStructureToNBT(tagCompound);
/*  254 */       tagCompound.setBoolean("Chest", this.hasMadeChest);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound) {
/*  259 */       super.readStructureFromNBT(tagCompound);
/*  260 */       this.hasMadeChest = tagCompound.getBoolean("Chest");
/*      */     }
/*      */ 
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
/*  265 */       getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public static ChestCorridor func_175868_a(List<StructureComponent> p_175868_0_, Random p_175868_1_, int p_175868_2_, int p_175868_3_, int p_175868_4_, EnumFacing p_175868_5_, int p_175868_6_) {
/*  270 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175868_2_, p_175868_3_, p_175868_4_, -1, -1, 0, 5, 5, 7, p_175868_5_);
/*  271 */       return (canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175868_0_, structureboundingbox) == null) ? new ChestCorridor(p_175868_6_, p_175868_1_, structureboundingbox, p_175868_5_) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/*  276 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/*  278 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  282 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 4, 6, true, randomIn, StructureStrongholdPieces.strongholdStones);
/*  283 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 1, 0);
/*  284 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 6);
/*  285 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 2, 3, 1, 4, Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
/*  286 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 1, structureBoundingBoxIn);
/*  287 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 5, structureBoundingBoxIn);
/*  288 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 2, structureBoundingBoxIn);
/*  289 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 4, structureBoundingBoxIn);
/*      */       
/*  291 */       for (int i = 2; i <= 4; i++)
/*      */       {
/*  293 */         setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 2, 1, i, structureBoundingBoxIn);
/*      */       }
/*      */       
/*  296 */       if (!this.hasMadeChest && structureBoundingBoxIn.isVecInside((Vec3i)new BlockPos(getXWithOffset(3, 3), getYWithOffset(2), getZWithOffset(3, 3)))) {
/*      */         
/*  298 */         this.hasMadeChest = true;
/*  299 */         generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 3, 2, 3, WeightedRandomChestContent.func_177629_a(strongholdChestContents, new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(randomIn) }), 2 + randomIn.nextInt(2));
/*      */       } 
/*      */       
/*  302 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class Corridor
/*      */     extends Stronghold
/*      */   {
/*      */     private int field_74993_a;
/*      */ 
/*      */     
/*      */     public Corridor() {}
/*      */ 
/*      */     
/*      */     public Corridor(int p_i45581_1_, Random p_i45581_2_, StructureBoundingBox p_i45581_3_, EnumFacing p_i45581_4_) {
/*  317 */       super(p_i45581_1_);
/*  318 */       this.coordBaseMode = p_i45581_4_;
/*  319 */       this.boundingBox = p_i45581_3_;
/*  320 */       this.field_74993_a = (p_i45581_4_ != EnumFacing.NORTH && p_i45581_4_ != EnumFacing.SOUTH) ? p_i45581_3_.getXSize() : p_i45581_3_.getZSize();
/*      */     }
/*      */ 
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound) {
/*  325 */       super.writeStructureToNBT(tagCompound);
/*  326 */       tagCompound.setInteger("Steps", this.field_74993_a);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound) {
/*  331 */       super.readStructureFromNBT(tagCompound);
/*  332 */       this.field_74993_a = tagCompound.getInteger("Steps");
/*      */     }
/*      */ 
/*      */     
/*      */     public static StructureBoundingBox func_175869_a(List<StructureComponent> p_175869_0_, Random p_175869_1_, int p_175869_2_, int p_175869_3_, int p_175869_4_, EnumFacing p_175869_5_) {
/*  337 */       int i = 3;
/*  338 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175869_2_, p_175869_3_, p_175869_4_, -1, -1, 0, 5, 5, 4, p_175869_5_);
/*  339 */       StructureComponent structurecomponent = StructureComponent.findIntersecting(p_175869_0_, structureboundingbox);
/*      */       
/*  341 */       if (structurecomponent == null)
/*      */       {
/*  343 */         return null;
/*      */       }
/*      */ 
/*      */       
/*  347 */       if ((structurecomponent.getBoundingBox()).minY == structureboundingbox.minY)
/*      */       {
/*  349 */         for (int j = 3; j >= 1; j--) {
/*      */           
/*  351 */           structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175869_2_, p_175869_3_, p_175869_4_, -1, -1, 0, 5, 5, j - 1, p_175869_5_);
/*      */           
/*  353 */           if (!structurecomponent.getBoundingBox().intersectsWith(structureboundingbox))
/*      */           {
/*  355 */             return StructureBoundingBox.getComponentToAddBoundingBox(p_175869_2_, p_175869_3_, p_175869_4_, -1, -1, 0, 5, 5, j, p_175869_5_);
/*      */           }
/*      */         } 
/*      */       }
/*      */       
/*  360 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/*  366 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/*  368 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  372 */       for (int i = 0; i < this.field_74993_a; i++) {
/*      */         
/*  374 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 0, 0, i, structureBoundingBoxIn);
/*  375 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 0, i, structureBoundingBoxIn);
/*  376 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 0, i, structureBoundingBoxIn);
/*  377 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 0, i, structureBoundingBoxIn);
/*  378 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 4, 0, i, structureBoundingBoxIn);
/*      */         
/*  380 */         for (int j = 1; j <= 3; j++) {
/*      */           
/*  382 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 0, j, i, structureBoundingBoxIn);
/*  383 */           setBlockState(worldIn, Blocks.air.getDefaultState(), 1, j, i, structureBoundingBoxIn);
/*  384 */           setBlockState(worldIn, Blocks.air.getDefaultState(), 2, j, i, structureBoundingBoxIn);
/*  385 */           setBlockState(worldIn, Blocks.air.getDefaultState(), 3, j, i, structureBoundingBoxIn);
/*  386 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 4, j, i, structureBoundingBoxIn);
/*      */         } 
/*      */         
/*  389 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 0, 4, i, structureBoundingBoxIn);
/*  390 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 4, i, structureBoundingBoxIn);
/*  391 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 4, i, structureBoundingBoxIn);
/*  392 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 4, i, structureBoundingBoxIn);
/*  393 */         setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 4, 4, i, structureBoundingBoxIn);
/*      */       } 
/*      */       
/*  396 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class Crossing
/*      */     extends Stronghold
/*      */   {
/*      */     private boolean field_74996_b;
/*      */     
/*      */     private boolean field_74997_c;
/*      */     
/*      */     private boolean field_74995_d;
/*      */     private boolean field_74999_h;
/*      */     
/*      */     public Crossing() {}
/*      */     
/*      */     public Crossing(int p_i45580_1_, Random p_i45580_2_, StructureBoundingBox p_i45580_3_, EnumFacing p_i45580_4_) {
/*  414 */       super(p_i45580_1_);
/*  415 */       this.coordBaseMode = p_i45580_4_;
/*  416 */       this.field_143013_d = getRandomDoor(p_i45580_2_);
/*  417 */       this.boundingBox = p_i45580_3_;
/*  418 */       this.field_74996_b = p_i45580_2_.nextBoolean();
/*  419 */       this.field_74997_c = p_i45580_2_.nextBoolean();
/*  420 */       this.field_74995_d = p_i45580_2_.nextBoolean();
/*  421 */       this.field_74999_h = (p_i45580_2_.nextInt(3) > 0);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound) {
/*  426 */       super.writeStructureToNBT(tagCompound);
/*  427 */       tagCompound.setBoolean("leftLow", this.field_74996_b);
/*  428 */       tagCompound.setBoolean("leftHigh", this.field_74997_c);
/*  429 */       tagCompound.setBoolean("rightLow", this.field_74995_d);
/*  430 */       tagCompound.setBoolean("rightHigh", this.field_74999_h);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound) {
/*  435 */       super.readStructureFromNBT(tagCompound);
/*  436 */       this.field_74996_b = tagCompound.getBoolean("leftLow");
/*  437 */       this.field_74997_c = tagCompound.getBoolean("leftHigh");
/*  438 */       this.field_74995_d = tagCompound.getBoolean("rightLow");
/*  439 */       this.field_74999_h = tagCompound.getBoolean("rightHigh");
/*      */     }
/*      */ 
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
/*  444 */       int i = 3;
/*  445 */       int j = 5;
/*      */       
/*  447 */       if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.NORTH) {
/*      */         
/*  449 */         i = 8 - i;
/*  450 */         j = 8 - j;
/*      */       } 
/*      */       
/*  453 */       getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 5, 1);
/*      */       
/*  455 */       if (this.field_74996_b)
/*      */       {
/*  457 */         getNextComponentX((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, i, 1);
/*      */       }
/*      */       
/*  460 */       if (this.field_74997_c)
/*      */       {
/*  462 */         getNextComponentX((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, j, 7);
/*      */       }
/*      */       
/*  465 */       if (this.field_74995_d)
/*      */       {
/*  467 */         getNextComponentZ((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, i, 1);
/*      */       }
/*      */       
/*  470 */       if (this.field_74999_h)
/*      */       {
/*  472 */         getNextComponentZ((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, j, 7);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public static Crossing func_175866_a(List<StructureComponent> p_175866_0_, Random p_175866_1_, int p_175866_2_, int p_175866_3_, int p_175866_4_, EnumFacing p_175866_5_, int p_175866_6_) {
/*  478 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175866_2_, p_175866_3_, p_175866_4_, -4, -3, 0, 10, 9, 11, p_175866_5_);
/*  479 */       return (canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175866_0_, structureboundingbox) == null) ? new Crossing(p_175866_6_, p_175866_1_, structureboundingbox, p_175866_5_) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/*  484 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/*  486 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  490 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 9, 8, 10, true, randomIn, StructureStrongholdPieces.strongholdStones);
/*  491 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 4, 3, 0);
/*      */       
/*  493 */       if (this.field_74996_b)
/*      */       {
/*  495 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 1, 0, 5, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       
/*  498 */       if (this.field_74995_d)
/*      */       {
/*  500 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 3, 1, 9, 5, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       
/*  503 */       if (this.field_74997_c)
/*      */       {
/*  505 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 7, 0, 7, 9, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       
/*  508 */       if (this.field_74999_h)
/*      */       {
/*  510 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 5, 7, 9, 7, 9, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       
/*  513 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 10, 7, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  514 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 2, 1, 8, 2, 6, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  515 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 5, 4, 4, 9, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  516 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 8, 1, 5, 8, 4, 9, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  517 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 4, 7, 3, 4, 9, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  518 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 3, 5, 3, 3, 6, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  519 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 4, 3, 3, 4, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
/*  520 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 6, 3, 4, 6, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
/*  521 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 5, 1, 7, 7, 1, 8, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  522 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 9, 7, 1, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
/*  523 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 7, 7, 2, 7, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
/*  524 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 7, 4, 5, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
/*  525 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 7, 8, 5, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
/*  526 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 7, 7, 5, 9, Blocks.double_stone_slab.getDefaultState(), Blocks.double_stone_slab.getDefaultState(), false);
/*  527 */       setBlockState(worldIn, Blocks.torch.getDefaultState(), 6, 5, 6, structureBoundingBoxIn);
/*  528 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class LeftTurn
/*      */     extends Stronghold
/*      */   {
/*      */     public LeftTurn() {}
/*      */ 
/*      */     
/*      */     public LeftTurn(int p_i45579_1_, Random p_i45579_2_, StructureBoundingBox p_i45579_3_, EnumFacing p_i45579_4_) {
/*  541 */       super(p_i45579_1_);
/*  542 */       this.coordBaseMode = p_i45579_4_;
/*  543 */       this.field_143013_d = getRandomDoor(p_i45579_2_);
/*  544 */       this.boundingBox = p_i45579_3_;
/*      */     }
/*      */ 
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
/*  549 */       if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST) {
/*      */         
/*  551 */         getNextComponentZ((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */       }
/*      */       else {
/*      */         
/*  555 */         getNextComponentX((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public static LeftTurn func_175867_a(List<StructureComponent> p_175867_0_, Random p_175867_1_, int p_175867_2_, int p_175867_3_, int p_175867_4_, EnumFacing p_175867_5_, int p_175867_6_) {
/*  561 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175867_2_, p_175867_3_, p_175867_4_, -1, -1, 0, 5, 5, 5, p_175867_5_);
/*  562 */       return (canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175867_0_, structureboundingbox) == null) ? new LeftTurn(p_175867_6_, p_175867_1_, structureboundingbox, p_175867_5_) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/*  567 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/*  569 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  573 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 4, 4, true, randomIn, StructureStrongholdPieces.strongholdStones);
/*  574 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 1, 0);
/*      */       
/*  576 */       if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST) {
/*      */         
/*  578 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 4, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       else {
/*      */         
/*  582 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       } 
/*      */       
/*  585 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Library
/*      */     extends Stronghold
/*      */   {
/*  592 */     private static final List<WeightedRandomChestContent> strongholdLibraryChestContents = Lists.newArrayList((Object[])new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.book, 0, 1, 3, 20), new WeightedRandomChestContent(Items.paper, 0, 2, 7, 20), new WeightedRandomChestContent((Item)Items.map, 0, 1, 1, 1), new WeightedRandomChestContent(Items.compass, 0, 1, 1, 1) });
/*      */     
/*      */     private boolean isLargeRoom;
/*      */ 
/*      */     
/*      */     public Library() {}
/*      */ 
/*      */     
/*      */     public Library(int p_i45578_1_, Random p_i45578_2_, StructureBoundingBox p_i45578_3_, EnumFacing p_i45578_4_) {
/*  601 */       super(p_i45578_1_);
/*  602 */       this.coordBaseMode = p_i45578_4_;
/*  603 */       this.field_143013_d = getRandomDoor(p_i45578_2_);
/*  604 */       this.boundingBox = p_i45578_3_;
/*  605 */       this.isLargeRoom = (p_i45578_3_.getYSize() > 6);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound) {
/*  610 */       super.writeStructureToNBT(tagCompound);
/*  611 */       tagCompound.setBoolean("Tall", this.isLargeRoom);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound) {
/*  616 */       super.readStructureFromNBT(tagCompound);
/*  617 */       this.isLargeRoom = tagCompound.getBoolean("Tall");
/*      */     }
/*      */ 
/*      */     
/*      */     public static Library func_175864_a(List<StructureComponent> p_175864_0_, Random p_175864_1_, int p_175864_2_, int p_175864_3_, int p_175864_4_, EnumFacing p_175864_5_, int p_175864_6_) {
/*  622 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175864_2_, p_175864_3_, p_175864_4_, -4, -1, 0, 14, 11, 15, p_175864_5_);
/*      */       
/*  624 */       if (!canStrongholdGoDeeper(structureboundingbox) || StructureComponent.findIntersecting(p_175864_0_, structureboundingbox) != null) {
/*      */         
/*  626 */         structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175864_2_, p_175864_3_, p_175864_4_, -4, -1, 0, 14, 6, 15, p_175864_5_);
/*      */         
/*  628 */         if (!canStrongholdGoDeeper(structureboundingbox) || StructureComponent.findIntersecting(p_175864_0_, structureboundingbox) != null)
/*      */         {
/*  630 */           return null;
/*      */         }
/*      */       } 
/*      */       
/*  634 */       return new Library(p_175864_6_, p_175864_1_, structureboundingbox, p_175864_5_);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/*  639 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/*  641 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  645 */       int i = 11;
/*      */       
/*  647 */       if (!this.isLargeRoom)
/*      */       {
/*  649 */         i = 6;
/*      */       }
/*      */       
/*  652 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 13, i - 1, 14, true, randomIn, StructureStrongholdPieces.strongholdStones);
/*  653 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 4, 1, 0);
/*  654 */       func_175805_a(worldIn, structureBoundingBoxIn, randomIn, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.web.getDefaultState(), Blocks.web.getDefaultState(), false);
/*  655 */       int j = 1;
/*  656 */       int k = 12;
/*      */       
/*  658 */       for (int l = 1; l <= 13; l++) {
/*      */         
/*  660 */         if ((l - 1) % 4 == 0) {
/*      */           
/*  662 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, l, 1, 4, l, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  663 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, l, 12, 4, l, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  664 */           setBlockState(worldIn, Blocks.torch.getDefaultState(), 2, 3, l, structureBoundingBoxIn);
/*  665 */           setBlockState(worldIn, Blocks.torch.getDefaultState(), 11, 3, l, structureBoundingBoxIn);
/*      */           
/*  667 */           if (this.isLargeRoom)
/*      */           {
/*  669 */             fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, l, 1, 9, l, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  670 */             fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 6, l, 12, 9, l, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/*  675 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, l, 1, 4, l, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
/*  676 */           fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, l, 12, 4, l, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
/*      */           
/*  678 */           if (this.isLargeRoom) {
/*      */             
/*  680 */             fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, l, 1, 9, l, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
/*  681 */             fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 6, l, 12, 9, l, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  686 */       for (int k1 = 3; k1 < 12; k1 += 2) {
/*      */         
/*  688 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, k1, 4, 3, k1, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
/*  689 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, k1, 7, 3, k1, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
/*  690 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, k1, 10, 3, k1, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
/*      */       } 
/*      */       
/*  693 */       if (this.isLargeRoom) {
/*      */         
/*  695 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 1, 3, 5, 13, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  696 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 5, 1, 12, 5, 13, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  697 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 1, 9, 5, 2, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  698 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 12, 9, 5, 13, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
/*  699 */         setBlockState(worldIn, Blocks.planks.getDefaultState(), 9, 5, 11, structureBoundingBoxIn);
/*  700 */         setBlockState(worldIn, Blocks.planks.getDefaultState(), 8, 5, 11, structureBoundingBoxIn);
/*  701 */         setBlockState(worldIn, Blocks.planks.getDefaultState(), 9, 5, 10, structureBoundingBoxIn);
/*  702 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 6, 2, 3, 6, 12, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
/*  703 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 6, 2, 10, 6, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
/*  704 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 6, 2, 9, 6, 2, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
/*  705 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 6, 12, 8, 6, 12, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
/*  706 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 9, 6, 11, structureBoundingBoxIn);
/*  707 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 8, 6, 11, structureBoundingBoxIn);
/*  708 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 9, 6, 10, structureBoundingBoxIn);
/*  709 */         int l1 = getMetadataWithOffset(Blocks.ladder, 3);
/*  710 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(l1), 10, 1, 13, structureBoundingBoxIn);
/*  711 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(l1), 10, 2, 13, structureBoundingBoxIn);
/*  712 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(l1), 10, 3, 13, structureBoundingBoxIn);
/*  713 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(l1), 10, 4, 13, structureBoundingBoxIn);
/*  714 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(l1), 10, 5, 13, structureBoundingBoxIn);
/*  715 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(l1), 10, 6, 13, structureBoundingBoxIn);
/*  716 */         setBlockState(worldIn, Blocks.ladder.getStateFromMeta(l1), 10, 7, 13, structureBoundingBoxIn);
/*  717 */         int i1 = 7;
/*  718 */         int j1 = 7;
/*  719 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1 - 1, 9, j1, structureBoundingBoxIn);
/*  720 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1, 9, j1, structureBoundingBoxIn);
/*  721 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1 - 1, 8, j1, structureBoundingBoxIn);
/*  722 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1, 8, j1, structureBoundingBoxIn);
/*  723 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1 - 1, 7, j1, structureBoundingBoxIn);
/*  724 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1, 7, j1, structureBoundingBoxIn);
/*  725 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1 - 2, 7, j1, structureBoundingBoxIn);
/*  726 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1 + 1, 7, j1, structureBoundingBoxIn);
/*  727 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1 - 1, 7, j1 - 1, structureBoundingBoxIn);
/*  728 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1 - 1, 7, j1 + 1, structureBoundingBoxIn);
/*  729 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1, 7, j1 - 1, structureBoundingBoxIn);
/*  730 */         setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), i1, 7, j1 + 1, structureBoundingBoxIn);
/*  731 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), i1 - 2, 8, j1, structureBoundingBoxIn);
/*  732 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), i1 + 1, 8, j1, structureBoundingBoxIn);
/*  733 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), i1 - 1, 8, j1 - 1, structureBoundingBoxIn);
/*  734 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), i1 - 1, 8, j1 + 1, structureBoundingBoxIn);
/*  735 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), i1, 8, j1 - 1, structureBoundingBoxIn);
/*  736 */         setBlockState(worldIn, Blocks.torch.getDefaultState(), i1, 8, j1 + 1, structureBoundingBoxIn);
/*      */       } 
/*      */       
/*  739 */       generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 3, 3, 5, WeightedRandomChestContent.func_177629_a(strongholdLibraryChestContents, new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(randomIn, 1, 5, 2) }), 1 + randomIn.nextInt(4));
/*      */       
/*  741 */       if (this.isLargeRoom) {
/*      */         
/*  743 */         setBlockState(worldIn, Blocks.air.getDefaultState(), 12, 9, 1, structureBoundingBoxIn);
/*  744 */         generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 12, 8, 1, WeightedRandomChestContent.func_177629_a(strongholdLibraryChestContents, new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(randomIn, 1, 5, 2) }), 1 + randomIn.nextInt(4));
/*      */       } 
/*      */       
/*  747 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class PieceWeight
/*      */   {
/*      */     public Class<? extends StructureStrongholdPieces.Stronghold> pieceClass;
/*      */     
/*      */     public final int pieceWeight;
/*      */     public int instancesSpawned;
/*      */     public int instancesLimit;
/*      */     
/*      */     public PieceWeight(Class<? extends StructureStrongholdPieces.Stronghold> p_i2076_1_, int p_i2076_2_, int p_i2076_3_) {
/*  761 */       this.pieceClass = p_i2076_1_;
/*  762 */       this.pieceWeight = p_i2076_2_;
/*  763 */       this.instancesLimit = p_i2076_3_;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canSpawnMoreStructuresOfType(int p_75189_1_) {
/*  768 */       return (this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canSpawnMoreStructures() {
/*  773 */       return (this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class PortalRoom
/*      */     extends Stronghold
/*      */   {
/*      */     private boolean hasSpawner;
/*      */ 
/*      */     
/*      */     public PortalRoom() {}
/*      */     
/*      */     public PortalRoom(int p_i45577_1_, Random p_i45577_2_, StructureBoundingBox p_i45577_3_, EnumFacing p_i45577_4_) {
/*  787 */       super(p_i45577_1_);
/*  788 */       this.coordBaseMode = p_i45577_4_;
/*  789 */       this.boundingBox = p_i45577_3_;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound) {
/*  794 */       super.writeStructureToNBT(tagCompound);
/*  795 */       tagCompound.setBoolean("Mob", this.hasSpawner);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound) {
/*  800 */       super.readStructureFromNBT(tagCompound);
/*  801 */       this.hasSpawner = tagCompound.getBoolean("Mob");
/*      */     }
/*      */ 
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
/*  806 */       if (componentIn != null)
/*      */       {
/*  808 */         ((StructureStrongholdPieces.Stairs2)componentIn).strongholdPortalRoom = this;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public static PortalRoom func_175865_a(List<StructureComponent> p_175865_0_, Random p_175865_1_, int p_175865_2_, int p_175865_3_, int p_175865_4_, EnumFacing p_175865_5_, int p_175865_6_) {
/*  814 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175865_2_, p_175865_3_, p_175865_4_, -4, -1, 0, 11, 8, 16, p_175865_5_);
/*  815 */       return (canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175865_0_, structureboundingbox) == null) ? new PortalRoom(p_175865_6_, p_175865_1_, structureboundingbox, p_175865_5_) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/*  820 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 10, 7, 15, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  821 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, StructureStrongholdPieces.Stronghold.Door.GRATES, 4, 1, 0);
/*  822 */       int i = 6;
/*  823 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, i, 1, 1, i, 14, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  824 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, i, 1, 9, i, 14, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  825 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, i, 1, 8, i, 2, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  826 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, i, 14, 8, i, 14, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  827 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 2, 1, 4, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  828 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 8, 1, 1, 9, 1, 4, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  829 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 1, 1, 3, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
/*  830 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 1, 9, 1, 3, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
/*  831 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 3, 1, 8, 7, 1, 12, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  832 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 9, 6, 1, 11, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
/*      */       
/*  834 */       for (int j = 3; j < 14; j += 2) {
/*      */         
/*  836 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, j, 0, 4, j, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
/*  837 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 3, j, 10, 4, j, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
/*      */       } 
/*      */       
/*  840 */       for (int k1 = 2; k1 < 9; k1 += 2)
/*      */       {
/*  842 */         fillWithBlocks(worldIn, structureBoundingBoxIn, k1, 3, 15, k1, 4, 15, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
/*      */       }
/*      */       
/*  845 */       int l1 = getMetadataWithOffset(Blocks.stone_brick_stairs, 3);
/*  846 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 5, 6, 1, 7, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  847 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 2, 6, 6, 2, 7, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  848 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 3, 7, 6, 3, 7, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*      */       
/*  850 */       for (int k = 4; k <= 6; k++) {
/*      */         
/*  852 */         setBlockState(worldIn, Blocks.stone_brick_stairs.getStateFromMeta(l1), k, 1, 4, structureBoundingBoxIn);
/*  853 */         setBlockState(worldIn, Blocks.stone_brick_stairs.getStateFromMeta(l1), k, 2, 5, structureBoundingBoxIn);
/*  854 */         setBlockState(worldIn, Blocks.stone_brick_stairs.getStateFromMeta(l1), k, 3, 6, structureBoundingBoxIn);
/*      */       } 
/*      */       
/*  857 */       int i2 = EnumFacing.NORTH.getHorizontalIndex();
/*  858 */       int l = EnumFacing.SOUTH.getHorizontalIndex();
/*  859 */       int i1 = EnumFacing.EAST.getHorizontalIndex();
/*  860 */       int j1 = EnumFacing.WEST.getHorizontalIndex();
/*      */       
/*  862 */       if (this.coordBaseMode != null)
/*      */       {
/*  864 */         switch (this.coordBaseMode) {
/*      */           
/*      */           case OPENING:
/*  867 */             i2 = EnumFacing.SOUTH.getHorizontalIndex();
/*  868 */             l = EnumFacing.NORTH.getHorizontalIndex();
/*      */             break;
/*      */           
/*      */           case WOOD_DOOR:
/*  872 */             i2 = EnumFacing.WEST.getHorizontalIndex();
/*  873 */             l = EnumFacing.EAST.getHorizontalIndex();
/*  874 */             i1 = EnumFacing.SOUTH.getHorizontalIndex();
/*  875 */             j1 = EnumFacing.NORTH.getHorizontalIndex();
/*      */             break;
/*      */           
/*      */           case GRATES:
/*  879 */             i2 = EnumFacing.EAST.getHorizontalIndex();
/*  880 */             l = EnumFacing.WEST.getHorizontalIndex();
/*  881 */             i1 = EnumFacing.SOUTH.getHorizontalIndex();
/*  882 */             j1 = EnumFacing.NORTH.getHorizontalIndex();
/*      */             break;
/*      */         } 
/*      */       }
/*  886 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i2).withProperty((IProperty)BlockEndPortalFrame.EYE, Boolean.valueOf((randomIn.nextFloat() > 0.9F))), 4, 3, 8, structureBoundingBoxIn);
/*  887 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i2).withProperty((IProperty)BlockEndPortalFrame.EYE, Boolean.valueOf((randomIn.nextFloat() > 0.9F))), 5, 3, 8, structureBoundingBoxIn);
/*  888 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i2).withProperty((IProperty)BlockEndPortalFrame.EYE, Boolean.valueOf((randomIn.nextFloat() > 0.9F))), 6, 3, 8, structureBoundingBoxIn);
/*  889 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(l).withProperty((IProperty)BlockEndPortalFrame.EYE, Boolean.valueOf((randomIn.nextFloat() > 0.9F))), 4, 3, 12, structureBoundingBoxIn);
/*  890 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(l).withProperty((IProperty)BlockEndPortalFrame.EYE, Boolean.valueOf((randomIn.nextFloat() > 0.9F))), 5, 3, 12, structureBoundingBoxIn);
/*  891 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(l).withProperty((IProperty)BlockEndPortalFrame.EYE, Boolean.valueOf((randomIn.nextFloat() > 0.9F))), 6, 3, 12, structureBoundingBoxIn);
/*  892 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i1).withProperty((IProperty)BlockEndPortalFrame.EYE, Boolean.valueOf((randomIn.nextFloat() > 0.9F))), 3, 3, 9, structureBoundingBoxIn);
/*  893 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i1).withProperty((IProperty)BlockEndPortalFrame.EYE, Boolean.valueOf((randomIn.nextFloat() > 0.9F))), 3, 3, 10, structureBoundingBoxIn);
/*  894 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(i1).withProperty((IProperty)BlockEndPortalFrame.EYE, Boolean.valueOf((randomIn.nextFloat() > 0.9F))), 3, 3, 11, structureBoundingBoxIn);
/*  895 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(j1).withProperty((IProperty)BlockEndPortalFrame.EYE, Boolean.valueOf((randomIn.nextFloat() > 0.9F))), 7, 3, 9, structureBoundingBoxIn);
/*  896 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(j1).withProperty((IProperty)BlockEndPortalFrame.EYE, Boolean.valueOf((randomIn.nextFloat() > 0.9F))), 7, 3, 10, structureBoundingBoxIn);
/*  897 */       setBlockState(worldIn, Blocks.end_portal_frame.getStateFromMeta(j1).withProperty((IProperty)BlockEndPortalFrame.EYE, Boolean.valueOf((randomIn.nextFloat() > 0.9F))), 7, 3, 11, structureBoundingBoxIn);
/*      */       
/*  899 */       if (!this.hasSpawner) {
/*      */         
/*  901 */         i = getYWithOffset(3);
/*  902 */         BlockPos blockpos = new BlockPos(getXWithOffset(5, 6), i, getZWithOffset(5, 6));
/*      */         
/*  904 */         if (structureBoundingBoxIn.isVecInside((Vec3i)blockpos)) {
/*      */           
/*  906 */           this.hasSpawner = true;
/*  907 */           worldIn.setBlockState(blockpos, Blocks.mob_spawner.getDefaultState(), 2);
/*  908 */           TileEntity tileentity = worldIn.getTileEntity(blockpos);
/*      */           
/*  910 */           if (tileentity instanceof TileEntityMobSpawner)
/*      */           {
/*  912 */             ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().setEntityName("Silverfish");
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  917 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class Prison
/*      */     extends Stronghold
/*      */   {
/*      */     public Prison() {}
/*      */ 
/*      */     
/*      */     public Prison(int p_i45576_1_, Random p_i45576_2_, StructureBoundingBox p_i45576_3_, EnumFacing p_i45576_4_) {
/*  929 */       super(p_i45576_1_);
/*  930 */       this.coordBaseMode = p_i45576_4_;
/*  931 */       this.field_143013_d = getRandomDoor(p_i45576_2_);
/*  932 */       this.boundingBox = p_i45576_3_;
/*      */     }
/*      */ 
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
/*  937 */       getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public static Prison func_175860_a(List<StructureComponent> p_175860_0_, Random p_175860_1_, int p_175860_2_, int p_175860_3_, int p_175860_4_, EnumFacing p_175860_5_, int p_175860_6_) {
/*  942 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175860_2_, p_175860_3_, p_175860_4_, -1, -1, 0, 9, 5, 11, p_175860_5_);
/*  943 */       return (canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175860_0_, structureboundingbox) == null) ? new Prison(p_175860_6_, p_175860_1_, structureboundingbox, p_175860_5_) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/*  948 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/*  950 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  954 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 8, 4, 10, true, randomIn, StructureStrongholdPieces.strongholdStones);
/*  955 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 1, 0);
/*  956 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 10, 3, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*  957 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 4, 3, 1, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  958 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 3, 4, 3, 3, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  959 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 7, 4, 3, 7, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  960 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 9, 4, 3, 9, false, randomIn, StructureStrongholdPieces.strongholdStones);
/*  961 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 4, 4, 3, 6, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
/*  962 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 5, 7, 3, 5, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
/*  963 */       setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), 4, 3, 2, structureBoundingBoxIn);
/*  964 */       setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), 4, 3, 8, structureBoundingBoxIn);
/*  965 */       setBlockState(worldIn, Blocks.iron_door.getStateFromMeta(getMetadataWithOffset(Blocks.iron_door, 3)), 4, 1, 2, structureBoundingBoxIn);
/*  966 */       setBlockState(worldIn, Blocks.iron_door.getStateFromMeta(getMetadataWithOffset(Blocks.iron_door, 3) + 8), 4, 2, 2, structureBoundingBoxIn);
/*  967 */       setBlockState(worldIn, Blocks.iron_door.getStateFromMeta(getMetadataWithOffset(Blocks.iron_door, 3)), 4, 1, 8, structureBoundingBoxIn);
/*  968 */       setBlockState(worldIn, Blocks.iron_door.getStateFromMeta(getMetadataWithOffset(Blocks.iron_door, 3) + 8), 4, 2, 8, structureBoundingBoxIn);
/*  969 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class RightTurn
/*      */     extends LeftTurn
/*      */   {
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
/*  978 */       if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST) {
/*      */         
/*  980 */         getNextComponentX((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */       }
/*      */       else {
/*      */         
/*  984 */         getNextComponentZ((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/*  990 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/*  992 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  996 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 4, 4, true, randomIn, StructureStrongholdPieces.strongholdStones);
/*  997 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 1, 0);
/*      */       
/*  999 */       if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST) {
/*      */         
/* 1001 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       else {
/*      */         
/* 1005 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 4, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       } 
/*      */       
/* 1008 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class RoomCrossing
/*      */     extends Stronghold
/*      */   {
/* 1015 */     private static final List<WeightedRandomChestContent> strongholdRoomCrossingChestContents = Lists.newArrayList((Object[])new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1) });
/*      */     
/*      */     protected int roomType;
/*      */ 
/*      */     
/*      */     public RoomCrossing() {}
/*      */ 
/*      */     
/*      */     public RoomCrossing(int p_i45575_1_, Random p_i45575_2_, StructureBoundingBox p_i45575_3_, EnumFacing p_i45575_4_) {
/* 1024 */       super(p_i45575_1_);
/* 1025 */       this.coordBaseMode = p_i45575_4_;
/* 1026 */       this.field_143013_d = getRandomDoor(p_i45575_2_);
/* 1027 */       this.boundingBox = p_i45575_3_;
/* 1028 */       this.roomType = p_i45575_2_.nextInt(5);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound) {
/* 1033 */       super.writeStructureToNBT(tagCompound);
/* 1034 */       tagCompound.setInteger("Type", this.roomType);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound) {
/* 1039 */       super.readStructureFromNBT(tagCompound);
/* 1040 */       this.roomType = tagCompound.getInteger("Type");
/*      */     }
/*      */ 
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
/* 1045 */       getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 4, 1);
/* 1046 */       getNextComponentX((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 4);
/* 1047 */       getNextComponentZ((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 4);
/*      */     }
/*      */ 
/*      */     
/*      */     public static RoomCrossing func_175859_a(List<StructureComponent> p_175859_0_, Random p_175859_1_, int p_175859_2_, int p_175859_3_, int p_175859_4_, EnumFacing p_175859_5_, int p_175859_6_) {
/* 1052 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175859_2_, p_175859_3_, p_175859_4_, -4, -1, 0, 11, 7, 11, p_175859_5_);
/* 1053 */       return (canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175859_0_, structureboundingbox) == null) ? new RoomCrossing(p_175859_6_, p_175859_1_, structureboundingbox, p_175859_5_) : null;
/*      */     }
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/*      */       int i1, i, j, k, l;
/* 1058 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/* 1060 */         return false;
/*      */       }
/*      */ 
/*      */       
/* 1064 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 10, 6, 10, true, randomIn, StructureStrongholdPieces.strongholdStones);
/* 1065 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 4, 1, 0);
/* 1066 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 10, 6, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 1067 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 4, 0, 3, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/* 1068 */       fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 4, 10, 3, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       
/* 1070 */       switch (this.roomType) {
/*      */         
/*      */         case 0:
/* 1073 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 1, 5, structureBoundingBoxIn);
/* 1074 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 2, 5, structureBoundingBoxIn);
/* 1075 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 3, 5, structureBoundingBoxIn);
/* 1076 */           setBlockState(worldIn, Blocks.torch.getDefaultState(), 4, 3, 5, structureBoundingBoxIn);
/* 1077 */           setBlockState(worldIn, Blocks.torch.getDefaultState(), 6, 3, 5, structureBoundingBoxIn);
/* 1078 */           setBlockState(worldIn, Blocks.torch.getDefaultState(), 5, 3, 4, structureBoundingBoxIn);
/* 1079 */           setBlockState(worldIn, Blocks.torch.getDefaultState(), 5, 3, 6, structureBoundingBoxIn);
/* 1080 */           setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 4, 1, 4, structureBoundingBoxIn);
/* 1081 */           setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 4, 1, 5, structureBoundingBoxIn);
/* 1082 */           setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 4, 1, 6, structureBoundingBoxIn);
/* 1083 */           setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 6, 1, 4, structureBoundingBoxIn);
/* 1084 */           setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 6, 1, 5, structureBoundingBoxIn);
/* 1085 */           setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 6, 1, 6, structureBoundingBoxIn);
/* 1086 */           setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 5, 1, 4, structureBoundingBoxIn);
/* 1087 */           setBlockState(worldIn, Blocks.stone_slab.getDefaultState(), 5, 1, 6, structureBoundingBoxIn);
/*      */           break;
/*      */         
/*      */         case 1:
/* 1091 */           for (i1 = 0; i1 < 5; i1++) {
/*      */             
/* 1093 */             setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 1, 3 + i1, structureBoundingBoxIn);
/* 1094 */             setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 7, 1, 3 + i1, structureBoundingBoxIn);
/* 1095 */             setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3 + i1, 1, 3, structureBoundingBoxIn);
/* 1096 */             setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3 + i1, 1, 7, structureBoundingBoxIn);
/*      */           } 
/*      */           
/* 1099 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 1, 5, structureBoundingBoxIn);
/* 1100 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 2, 5, structureBoundingBoxIn);
/* 1101 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 5, 3, 5, structureBoundingBoxIn);
/* 1102 */           setBlockState(worldIn, Blocks.flowing_water.getDefaultState(), 5, 4, 5, structureBoundingBoxIn);
/*      */           break;
/*      */         
/*      */         case 2:
/* 1106 */           for (i = 1; i <= 9; i++) {
/*      */             
/* 1108 */             setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 1, 3, i, structureBoundingBoxIn);
/* 1109 */             setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 9, 3, i, structureBoundingBoxIn);
/*      */           } 
/*      */           
/* 1112 */           for (j = 1; j <= 9; j++) {
/*      */             
/* 1114 */             setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), j, 3, 1, structureBoundingBoxIn);
/* 1115 */             setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), j, 3, 9, structureBoundingBoxIn);
/*      */           } 
/*      */           
/* 1118 */           setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 5, 1, 4, structureBoundingBoxIn);
/* 1119 */           setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 5, 1, 6, structureBoundingBoxIn);
/* 1120 */           setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 5, 3, 4, structureBoundingBoxIn);
/* 1121 */           setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 5, 3, 6, structureBoundingBoxIn);
/* 1122 */           setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 1, 5, structureBoundingBoxIn);
/* 1123 */           setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 6, 1, 5, structureBoundingBoxIn);
/* 1124 */           setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 3, 5, structureBoundingBoxIn);
/* 1125 */           setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 6, 3, 5, structureBoundingBoxIn);
/*      */           
/* 1127 */           for (k = 1; k <= 3; k++) {
/*      */             
/* 1129 */             setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, k, 4, structureBoundingBoxIn);
/* 1130 */             setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 6, k, 4, structureBoundingBoxIn);
/* 1131 */             setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, k, 6, structureBoundingBoxIn);
/* 1132 */             setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 6, k, 6, structureBoundingBoxIn);
/*      */           } 
/*      */           
/* 1135 */           setBlockState(worldIn, Blocks.torch.getDefaultState(), 5, 3, 5, structureBoundingBoxIn);
/*      */           
/* 1137 */           for (l = 2; l <= 8; l++) {
/*      */             
/* 1139 */             setBlockState(worldIn, Blocks.planks.getDefaultState(), 2, 3, l, structureBoundingBoxIn);
/* 1140 */             setBlockState(worldIn, Blocks.planks.getDefaultState(), 3, 3, l, structureBoundingBoxIn);
/*      */             
/* 1142 */             if (l <= 3 || l >= 7) {
/*      */               
/* 1144 */               setBlockState(worldIn, Blocks.planks.getDefaultState(), 4, 3, l, structureBoundingBoxIn);
/* 1145 */               setBlockState(worldIn, Blocks.planks.getDefaultState(), 5, 3, l, structureBoundingBoxIn);
/* 1146 */               setBlockState(worldIn, Blocks.planks.getDefaultState(), 6, 3, l, structureBoundingBoxIn);
/*      */             } 
/*      */             
/* 1149 */             setBlockState(worldIn, Blocks.planks.getDefaultState(), 7, 3, l, structureBoundingBoxIn);
/* 1150 */             setBlockState(worldIn, Blocks.planks.getDefaultState(), 8, 3, l, structureBoundingBoxIn);
/*      */           } 
/*      */           
/* 1153 */           setBlockState(worldIn, Blocks.ladder.getStateFromMeta(getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 1, 3, structureBoundingBoxIn);
/* 1154 */           setBlockState(worldIn, Blocks.ladder.getStateFromMeta(getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 2, 3, structureBoundingBoxIn);
/* 1155 */           setBlockState(worldIn, Blocks.ladder.getStateFromMeta(getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 3, 3, structureBoundingBoxIn);
/* 1156 */           generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 3, 4, 8, WeightedRandomChestContent.func_177629_a(strongholdRoomCrossingChestContents, new WeightedRandomChestContent[] { Items.enchanted_book.getRandom(randomIn) }), 1 + randomIn.nextInt(4));
/*      */           break;
/*      */       } 
/* 1159 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class Stairs
/*      */     extends Stronghold
/*      */   {
/*      */     private boolean field_75024_a;
/*      */ 
/*      */     
/*      */     public Stairs() {}
/*      */ 
/*      */     
/*      */     public Stairs(int p_i2081_1_, Random p_i2081_2_, int p_i2081_3_, int p_i2081_4_) {
/* 1174 */       super(p_i2081_1_);
/* 1175 */       this.field_75024_a = true;
/* 1176 */       this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(p_i2081_2_);
/* 1177 */       this.field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;
/*      */       
/* 1179 */       switch (this.coordBaseMode) {
/*      */         
/*      */         case OPENING:
/*      */         case IRON_DOOR:
/* 1183 */           this.boundingBox = new StructureBoundingBox(p_i2081_3_, 64, p_i2081_4_, p_i2081_3_ + 5 - 1, 74, p_i2081_4_ + 5 - 1);
/*      */           return;
/*      */       } 
/*      */       
/* 1187 */       this.boundingBox = new StructureBoundingBox(p_i2081_3_, 64, p_i2081_4_, p_i2081_3_ + 5 - 1, 74, p_i2081_4_ + 5 - 1);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Stairs(int p_i45574_1_, Random p_i45574_2_, StructureBoundingBox p_i45574_3_, EnumFacing p_i45574_4_) {
/* 1193 */       super(p_i45574_1_);
/* 1194 */       this.field_75024_a = false;
/* 1195 */       this.coordBaseMode = p_i45574_4_;
/* 1196 */       this.field_143013_d = getRandomDoor(p_i45574_2_);
/* 1197 */       this.boundingBox = p_i45574_3_;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound) {
/* 1202 */       super.writeStructureToNBT(tagCompound);
/* 1203 */       tagCompound.setBoolean("Source", this.field_75024_a);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound) {
/* 1208 */       super.readStructureFromNBT(tagCompound);
/* 1209 */       this.field_75024_a = tagCompound.getBoolean("Source");
/*      */     }
/*      */ 
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
/* 1214 */       if (this.field_75024_a)
/*      */       {
/* 1216 */         StructureStrongholdPieces.strongComponentType = (Class)StructureStrongholdPieces.Crossing.class;
/*      */       }
/*      */       
/* 1219 */       getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public static Stairs func_175863_a(List<StructureComponent> p_175863_0_, Random p_175863_1_, int p_175863_2_, int p_175863_3_, int p_175863_4_, EnumFacing p_175863_5_, int p_175863_6_) {
/* 1224 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175863_2_, p_175863_3_, p_175863_4_, -1, -7, 0, 5, 11, 5, p_175863_5_);
/* 1225 */       return (canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175863_0_, structureboundingbox) == null) ? new Stairs(p_175863_6_, p_175863_1_, structureboundingbox, p_175863_5_) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/* 1230 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/* 1232 */         return false;
/*      */       }
/*      */ 
/*      */       
/* 1236 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 10, 4, true, randomIn, StructureStrongholdPieces.strongholdStones);
/* 1237 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 7, 0);
/* 1238 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 4);
/* 1239 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 6, 1, structureBoundingBoxIn);
/* 1240 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 5, 1, structureBoundingBoxIn);
/* 1241 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 6, 1, structureBoundingBoxIn);
/* 1242 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 5, 2, structureBoundingBoxIn);
/* 1243 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 4, 3, structureBoundingBoxIn);
/* 1244 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 5, 3, structureBoundingBoxIn);
/* 1245 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 4, 3, structureBoundingBoxIn);
/* 1246 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 3, 3, structureBoundingBoxIn);
/* 1247 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 4, 3, structureBoundingBoxIn);
/* 1248 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 3, 2, structureBoundingBoxIn);
/* 1249 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 2, 1, structureBoundingBoxIn);
/* 1250 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 3, 1, structureBoundingBoxIn);
/* 1251 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 2, 1, structureBoundingBoxIn);
/* 1252 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 1, 1, structureBoundingBoxIn);
/* 1253 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 2, 1, structureBoundingBoxIn);
/* 1254 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 1, 2, structureBoundingBoxIn);
/* 1255 */       setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 1, 3, structureBoundingBoxIn);
/* 1256 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Stairs2
/*      */     extends Stairs
/*      */   {
/*      */     public StructureStrongholdPieces.PieceWeight strongholdPieceWeight;
/*      */     public StructureStrongholdPieces.PortalRoom strongholdPortalRoom;
/* 1265 */     public List<StructureComponent> field_75026_c = Lists.newArrayList();
/*      */ 
/*      */ 
/*      */     
/*      */     public Stairs2() {}
/*      */ 
/*      */     
/*      */     public Stairs2(int p_i2083_1_, Random p_i2083_2_, int p_i2083_3_, int p_i2083_4_) {
/* 1273 */       super(0, p_i2083_2_, p_i2083_3_, p_i2083_4_);
/*      */     }
/*      */ 
/*      */     
/*      */     public BlockPos getBoundingBoxCenter() {
/* 1278 */       return (this.strongholdPortalRoom != null) ? this.strongholdPortalRoom.getBoundingBoxCenter() : super.getBoundingBoxCenter();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class StairsStraight
/*      */     extends Stronghold
/*      */   {
/*      */     public StairsStraight() {}
/*      */ 
/*      */     
/*      */     public StairsStraight(int p_i45572_1_, Random p_i45572_2_, StructureBoundingBox p_i45572_3_, EnumFacing p_i45572_4_) {
/* 1290 */       super(p_i45572_1_);
/* 1291 */       this.coordBaseMode = p_i45572_4_;
/* 1292 */       this.field_143013_d = getRandomDoor(p_i45572_2_);
/* 1293 */       this.boundingBox = p_i45572_3_;
/*      */     }
/*      */ 
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
/* 1298 */       getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public static StairsStraight func_175861_a(List<StructureComponent> p_175861_0_, Random p_175861_1_, int p_175861_2_, int p_175861_3_, int p_175861_4_, EnumFacing p_175861_5_, int p_175861_6_) {
/* 1303 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175861_2_, p_175861_3_, p_175861_4_, -1, -7, 0, 5, 11, 8, p_175861_5_);
/* 1304 */       return (canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175861_0_, structureboundingbox) == null) ? new StairsStraight(p_175861_6_, p_175861_1_, structureboundingbox, p_175861_5_) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/* 1309 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/* 1311 */         return false;
/*      */       }
/*      */ 
/*      */       
/* 1315 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 10, 7, true, randomIn, StructureStrongholdPieces.strongholdStones);
/* 1316 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 7, 0);
/* 1317 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 7);
/* 1318 */       int i = getMetadataWithOffset(Blocks.stone_stairs, 2);
/*      */       
/* 1320 */       for (int j = 0; j < 6; j++) {
/*      */         
/* 1322 */         setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 1, 6 - j, 1 + j, structureBoundingBoxIn);
/* 1323 */         setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 2, 6 - j, 1 + j, structureBoundingBoxIn);
/* 1324 */         setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 3, 6 - j, 1 + j, structureBoundingBoxIn);
/*      */         
/* 1326 */         if (j < 5) {
/*      */           
/* 1328 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 5 - j, 1 + j, structureBoundingBoxIn);
/* 1329 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 5 - j, 1 + j, structureBoundingBoxIn);
/* 1330 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 5 - j, 1 + j, structureBoundingBoxIn);
/*      */         } 
/*      */       } 
/*      */       
/* 1334 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class Stones
/*      */     extends StructureComponent.BlockSelector
/*      */   {
/*      */     private Stones() {}
/*      */ 
/*      */     
/*      */     public void selectBlocks(Random rand, int x, int y, int z, boolean p_75062_5_) {
/* 1347 */       if (p_75062_5_) {
/*      */         
/* 1349 */         float f = rand.nextFloat();
/*      */         
/* 1351 */         if (f < 0.2F)
/*      */         {
/* 1353 */           this.blockstate = Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CRACKED_META);
/*      */         }
/* 1355 */         else if (f < 0.5F)
/*      */         {
/* 1357 */           this.blockstate = Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.MOSSY_META);
/*      */         }
/* 1359 */         else if (f < 0.55F)
/*      */         {
/* 1361 */           this.blockstate = Blocks.monster_egg.getStateFromMeta(BlockSilverfish.EnumType.STONEBRICK.getMetadata());
/*      */         }
/*      */         else
/*      */         {
/* 1365 */           this.blockstate = Blocks.stonebrick.getDefaultState();
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/* 1370 */         this.blockstate = Blocks.air.getDefaultState();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class Straight
/*      */     extends Stronghold
/*      */   {
/*      */     private boolean expandsX;
/*      */     
/*      */     private boolean expandsZ;
/*      */     
/*      */     public Straight() {}
/*      */     
/*      */     public Straight(int p_i45573_1_, Random p_i45573_2_, StructureBoundingBox p_i45573_3_, EnumFacing p_i45573_4_) {
/* 1386 */       super(p_i45573_1_);
/* 1387 */       this.coordBaseMode = p_i45573_4_;
/* 1388 */       this.field_143013_d = getRandomDoor(p_i45573_2_);
/* 1389 */       this.boundingBox = p_i45573_3_;
/* 1390 */       this.expandsX = (p_i45573_2_.nextInt(2) == 0);
/* 1391 */       this.expandsZ = (p_i45573_2_.nextInt(2) == 0);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound) {
/* 1396 */       super.writeStructureToNBT(tagCompound);
/* 1397 */       tagCompound.setBoolean("Left", this.expandsX);
/* 1398 */       tagCompound.setBoolean("Right", this.expandsZ);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound) {
/* 1403 */       super.readStructureFromNBT(tagCompound);
/* 1404 */       this.expandsX = tagCompound.getBoolean("Left");
/* 1405 */       this.expandsZ = tagCompound.getBoolean("Right");
/*      */     }
/*      */ 
/*      */     
/*      */     public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
/* 1410 */       getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
/*      */       
/* 1412 */       if (this.expandsX)
/*      */       {
/* 1414 */         getNextComponentX((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 2);
/*      */       }
/*      */       
/* 1417 */       if (this.expandsZ)
/*      */       {
/* 1419 */         getNextComponentZ((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 2);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public static Straight func_175862_a(List<StructureComponent> p_175862_0_, Random p_175862_1_, int p_175862_2_, int p_175862_3_, int p_175862_4_, EnumFacing p_175862_5_, int p_175862_6_) {
/* 1425 */       StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175862_2_, p_175862_3_, p_175862_4_, -1, -1, 0, 5, 5, 7, p_175862_5_);
/* 1426 */       return (canStrongholdGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175862_0_, structureboundingbox) == null) ? new Straight(p_175862_6_, p_175862_1_, structureboundingbox, p_175862_5_) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/* 1431 */       if (isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn))
/*      */       {
/* 1433 */         return false;
/*      */       }
/*      */ 
/*      */       
/* 1437 */       fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 4, 6, true, randomIn, StructureStrongholdPieces.strongholdStones);
/* 1438 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 1, 0);
/* 1439 */       placeDoor(worldIn, randomIn, structureBoundingBoxIn, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 6);
/* 1440 */       randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1F, 1, 2, 1, Blocks.torch.getDefaultState());
/* 1441 */       randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1F, 3, 2, 1, Blocks.torch.getDefaultState());
/* 1442 */       randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1F, 1, 2, 5, Blocks.torch.getDefaultState());
/* 1443 */       randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1F, 3, 2, 5, Blocks.torch.getDefaultState());
/*      */       
/* 1445 */       if (this.expandsX)
/*      */       {
/* 1447 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 2, 0, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       
/* 1450 */       if (this.expandsZ)
/*      */       {
/* 1452 */         fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 2, 4, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */       }
/*      */       
/* 1455 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class Stronghold
/*      */     extends StructureComponent
/*      */   {
/* 1462 */     protected Door field_143013_d = Door.OPENING;
/*      */ 
/*      */ 
/*      */     
/*      */     public Stronghold() {}
/*      */ 
/*      */     
/*      */     protected Stronghold(int p_i2087_1_) {
/* 1470 */       super(p_i2087_1_);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void writeStructureToNBT(NBTTagCompound tagCompound) {
/* 1475 */       tagCompound.setString("EntryDoor", this.field_143013_d.name());
/*      */     }
/*      */ 
/*      */     
/*      */     protected void readStructureFromNBT(NBTTagCompound tagCompound) {
/* 1480 */       this.field_143013_d = Door.valueOf(tagCompound.getString("EntryDoor"));
/*      */     }
/*      */ 
/*      */     
/*      */     protected void placeDoor(World worldIn, Random p_74990_2_, StructureBoundingBox p_74990_3_, Door p_74990_4_, int p_74990_5_, int p_74990_6_, int p_74990_7_) {
/* 1485 */       switch (p_74990_4_) {
/*      */ 
/*      */         
/*      */         default:
/* 1489 */           fillWithBlocks(worldIn, p_74990_3_, p_74990_5_, p_74990_6_, p_74990_7_, p_74990_5_ + 3 - 1, p_74990_6_ + 3 - 1, p_74990_7_, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
/*      */           return;
/*      */         
/*      */         case WOOD_DOOR:
/* 1493 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1494 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1495 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1496 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 1, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1497 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1498 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1499 */           setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1500 */           setBlockState(worldIn, Blocks.oak_door.getDefaultState(), p_74990_5_ + 1, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1501 */           setBlockState(worldIn, Blocks.oak_door.getStateFromMeta(8), p_74990_5_ + 1, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/*      */           return;
/*      */         
/*      */         case GRATES:
/* 1505 */           setBlockState(worldIn, Blocks.air.getDefaultState(), p_74990_5_ + 1, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1506 */           setBlockState(worldIn, Blocks.air.getDefaultState(), p_74990_5_ + 1, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1507 */           setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1508 */           setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1509 */           setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1510 */           setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_ + 1, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1511 */           setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1512 */           setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1513 */           setBlockState(worldIn, Blocks.iron_bars.getDefaultState(), p_74990_5_ + 2, p_74990_6_, p_74990_7_, p_74990_3_); return;
/*      */         case IRON_DOOR:
/*      */           break;
/*      */       } 
/* 1517 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1518 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1519 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1520 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 1, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1521 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 2, p_74990_7_, p_74990_3_);
/* 1522 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1523 */       setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), p_74990_5_ + 2, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1524 */       setBlockState(worldIn, Blocks.iron_door.getDefaultState(), p_74990_5_ + 1, p_74990_6_, p_74990_7_, p_74990_3_);
/* 1525 */       setBlockState(worldIn, Blocks.iron_door.getStateFromMeta(8), p_74990_5_ + 1, p_74990_6_ + 1, p_74990_7_, p_74990_3_);
/* 1526 */       setBlockState(worldIn, Blocks.stone_button.getStateFromMeta(getMetadataWithOffset(Blocks.stone_button, 4)), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_ + 1, p_74990_3_);
/* 1527 */       setBlockState(worldIn, Blocks.stone_button.getStateFromMeta(getMetadataWithOffset(Blocks.stone_button, 3)), p_74990_5_ + 2, p_74990_6_ + 1, p_74990_7_ - 1, p_74990_3_);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     protected Door getRandomDoor(Random p_74988_1_) {
/* 1533 */       int i = p_74988_1_.nextInt(5);
/*      */       
/* 1535 */       switch (i) {
/*      */ 
/*      */ 
/*      */         
/*      */         default:
/* 1540 */           return Door.OPENING;
/*      */         
/*      */         case 2:
/* 1543 */           return Door.WOOD_DOOR;
/*      */         
/*      */         case 3:
/* 1546 */           return Door.GRATES;
/*      */         case 4:
/*      */           break;
/* 1549 */       }  return Door.IRON_DOOR;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     protected StructureComponent getNextComponentNormal(StructureStrongholdPieces.Stairs2 p_74986_1_, List<StructureComponent> p_74986_2_, Random p_74986_3_, int p_74986_4_, int p_74986_5_) {
/* 1555 */       if (this.coordBaseMode != null)
/*      */       {
/* 1557 */         switch (this.coordBaseMode) {
/*      */           
/*      */           case IRON_DOOR:
/* 1560 */             return StructureStrongholdPieces.func_175953_c(p_74986_1_, p_74986_2_, p_74986_3_, this.boundingBox.minX + p_74986_4_, this.boundingBox.minY + p_74986_5_, this.boundingBox.minZ - 1, this.coordBaseMode, getComponentType());
/*      */           
/*      */           case OPENING:
/* 1563 */             return StructureStrongholdPieces.func_175953_c(p_74986_1_, p_74986_2_, p_74986_3_, this.boundingBox.minX + p_74986_4_, this.boundingBox.minY + p_74986_5_, this.boundingBox.maxZ + 1, this.coordBaseMode, getComponentType());
/*      */           
/*      */           case WOOD_DOOR:
/* 1566 */             return StructureStrongholdPieces.func_175953_c(p_74986_1_, p_74986_2_, p_74986_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74986_5_, this.boundingBox.minZ + p_74986_4_, this.coordBaseMode, getComponentType());
/*      */           
/*      */           case GRATES:
/* 1569 */             return StructureStrongholdPieces.func_175953_c(p_74986_1_, p_74986_2_, p_74986_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74986_5_, this.boundingBox.minZ + p_74986_4_, this.coordBaseMode, getComponentType());
/*      */         } 
/*      */       
/*      */       }
/* 1573 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     protected StructureComponent getNextComponentX(StructureStrongholdPieces.Stairs2 p_74989_1_, List<StructureComponent> p_74989_2_, Random p_74989_3_, int p_74989_4_, int p_74989_5_) {
/* 1578 */       if (this.coordBaseMode != null)
/*      */       {
/* 1580 */         switch (this.coordBaseMode) {
/*      */           
/*      */           case IRON_DOOR:
/* 1583 */             return StructureStrongholdPieces.func_175953_c(p_74989_1_, p_74989_2_, p_74989_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74989_4_, this.boundingBox.minZ + p_74989_5_, EnumFacing.WEST, getComponentType());
/*      */           
/*      */           case OPENING:
/* 1586 */             return StructureStrongholdPieces.func_175953_c(p_74989_1_, p_74989_2_, p_74989_3_, this.boundingBox.minX - 1, this.boundingBox.minY + p_74989_4_, this.boundingBox.minZ + p_74989_5_, EnumFacing.WEST, getComponentType());
/*      */           
/*      */           case WOOD_DOOR:
/* 1589 */             return StructureStrongholdPieces.func_175953_c(p_74989_1_, p_74989_2_, p_74989_3_, this.boundingBox.minX + p_74989_5_, this.boundingBox.minY + p_74989_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, getComponentType());
/*      */           
/*      */           case GRATES:
/* 1592 */             return StructureStrongholdPieces.func_175953_c(p_74989_1_, p_74989_2_, p_74989_3_, this.boundingBox.minX + p_74989_5_, this.boundingBox.minY + p_74989_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, getComponentType());
/*      */         } 
/*      */       
/*      */       }
/* 1596 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     protected StructureComponent getNextComponentZ(StructureStrongholdPieces.Stairs2 p_74987_1_, List<StructureComponent> p_74987_2_, Random p_74987_3_, int p_74987_4_, int p_74987_5_) {
/* 1601 */       if (this.coordBaseMode != null)
/*      */       {
/* 1603 */         switch (this.coordBaseMode) {
/*      */           
/*      */           case IRON_DOOR:
/* 1606 */             return StructureStrongholdPieces.func_175953_c(p_74987_1_, p_74987_2_, p_74987_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74987_4_, this.boundingBox.minZ + p_74987_5_, EnumFacing.EAST, getComponentType());
/*      */           
/*      */           case OPENING:
/* 1609 */             return StructureStrongholdPieces.func_175953_c(p_74987_1_, p_74987_2_, p_74987_3_, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74987_4_, this.boundingBox.minZ + p_74987_5_, EnumFacing.EAST, getComponentType());
/*      */           
/*      */           case WOOD_DOOR:
/* 1612 */             return StructureStrongholdPieces.func_175953_c(p_74987_1_, p_74987_2_, p_74987_3_, this.boundingBox.minX + p_74987_5_, this.boundingBox.minY + p_74987_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, getComponentType());
/*      */           
/*      */           case GRATES:
/* 1615 */             return StructureStrongholdPieces.func_175953_c(p_74987_1_, p_74987_2_, p_74987_3_, this.boundingBox.minX + p_74987_5_, this.boundingBox.minY + p_74987_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, getComponentType());
/*      */         } 
/*      */       
/*      */       }
/* 1619 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     protected static boolean canStrongholdGoDeeper(StructureBoundingBox p_74991_0_) {
/* 1624 */       return (p_74991_0_ != null && p_74991_0_.minY > 10);
/*      */     }
/*      */     
/*      */     public enum Door
/*      */     {
/* 1629 */       OPENING,
/* 1630 */       WOOD_DOOR,
/* 1631 */       GRATES,
/* 1632 */       IRON_DOOR;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\structure\StructureStrongholdPieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */