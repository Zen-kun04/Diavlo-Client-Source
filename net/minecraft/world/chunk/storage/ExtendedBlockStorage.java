/*     */ package net.minecraft.world.chunk.storage;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.world.chunk.NibbleArray;
/*     */ import net.optifine.reflect.Reflector;
/*     */ 
/*     */ 
/*     */ public class ExtendedBlockStorage
/*     */ {
/*     */   private int yBase;
/*     */   private int blockRefCount;
/*     */   private int tickRefCount;
/*     */   private char[] data;
/*     */   private NibbleArray blocklightArray;
/*     */   private NibbleArray skylightArray;
/*     */   
/*     */   public ExtendedBlockStorage(int y, boolean storeSkylight) {
/*  20 */     this.yBase = y;
/*  21 */     this.data = new char[4096];
/*  22 */     this.blocklightArray = new NibbleArray();
/*     */     
/*  24 */     if (storeSkylight)
/*     */     {
/*  26 */       this.skylightArray = new NibbleArray();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState get(int x, int y, int z) {
/*  32 */     IBlockState iblockstate = (IBlockState)Block.BLOCK_STATE_IDS.getByValue(this.data[y << 8 | z << 4 | x]);
/*  33 */     return (iblockstate != null) ? iblockstate : Blocks.air.getDefaultState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(int x, int y, int z, IBlockState state) {
/*  38 */     if (Reflector.IExtendedBlockState.isInstance(state))
/*     */     {
/*  40 */       state = (IBlockState)Reflector.call(state, Reflector.IExtendedBlockState_getClean, new Object[0]);
/*     */     }
/*     */     
/*  43 */     IBlockState iblockstate = get(x, y, z);
/*  44 */     Block block = iblockstate.getBlock();
/*  45 */     Block block1 = state.getBlock();
/*     */     
/*  47 */     if (block != Blocks.air) {
/*     */       
/*  49 */       this.blockRefCount--;
/*     */       
/*  51 */       if (block.getTickRandomly())
/*     */       {
/*  53 */         this.tickRefCount--;
/*     */       }
/*     */     } 
/*     */     
/*  57 */     if (block1 != Blocks.air) {
/*     */       
/*  59 */       this.blockRefCount++;
/*     */       
/*  61 */       if (block1.getTickRandomly())
/*     */       {
/*  63 */         this.tickRefCount++;
/*     */       }
/*     */     } 
/*     */     
/*  67 */     this.data[y << 8 | z << 4 | x] = (char)Block.BLOCK_STATE_IDS.get(state);
/*     */   }
/*     */ 
/*     */   
/*     */   public Block getBlockByExtId(int x, int y, int z) {
/*  72 */     return get(x, y, z).getBlock();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getExtBlockMetadata(int x, int y, int z) {
/*  77 */     IBlockState iblockstate = get(x, y, z);
/*  78 */     return iblockstate.getBlock().getMetaFromState(iblockstate);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  83 */     return (this.blockRefCount == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getNeedsRandomTick() {
/*  88 */     return (this.tickRefCount > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getYLocation() {
/*  93 */     return this.yBase;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setExtSkylightValue(int x, int y, int z, int value) {
/*  98 */     this.skylightArray.set(x, y, z, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getExtSkylightValue(int x, int y, int z) {
/* 103 */     return this.skylightArray.get(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setExtBlocklightValue(int x, int y, int z, int value) {
/* 108 */     this.blocklightArray.set(x, y, z, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getExtBlocklightValue(int x, int y, int z) {
/* 113 */     return this.blocklightArray.get(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeInvalidBlocks() {
/* 118 */     IBlockState iblockstate = Blocks.air.getDefaultState();
/* 119 */     int i = 0;
/* 120 */     int j = 0;
/*     */     
/* 122 */     for (int k = 0; k < 16; k++) {
/*     */       
/* 124 */       for (int l = 0; l < 16; l++) {
/*     */         
/* 126 */         for (int i1 = 0; i1 < 16; i1++) {
/*     */           
/* 128 */           Block block = getBlockByExtId(i1, k, l);
/*     */           
/* 130 */           if (block != Blocks.air) {
/*     */             
/* 132 */             i++;
/*     */             
/* 134 */             if (block.getTickRandomly())
/*     */             {
/* 136 */               j++;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 143 */     this.blockRefCount = i;
/* 144 */     this.tickRefCount = j;
/*     */   }
/*     */ 
/*     */   
/*     */   public char[] getData() {
/* 149 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setData(char[] dataArray) {
/* 154 */     this.data = dataArray;
/*     */   }
/*     */ 
/*     */   
/*     */   public NibbleArray getBlocklightArray() {
/* 159 */     return this.blocklightArray;
/*     */   }
/*     */ 
/*     */   
/*     */   public NibbleArray getSkylightArray() {
/* 164 */     return this.skylightArray;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBlocklightArray(NibbleArray newBlocklightArray) {
/* 169 */     this.blocklightArray = newBlocklightArray;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSkylightArray(NibbleArray newSkylightArray) {
/* 174 */     this.skylightArray = newSkylightArray;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBlockRefCount() {
/* 179 */     return this.blockRefCount;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\chunk\storage\ExtendedBlockStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */