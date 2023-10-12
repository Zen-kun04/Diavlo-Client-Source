/*     */ package net.optifine.render;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.BlockStateBase;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.renderer.BlockModelRenderer;
/*     */ import net.minecraft.client.renderer.RegionRenderCacheBuilder;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.optifine.BlockPosM;
/*     */ import net.optifine.model.ListQuadsOverlay;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RenderEnv
/*     */ {
/*     */   private IBlockState blockState;
/*     */   private BlockPos blockPos;
/*  26 */   private int blockId = -1;
/*  27 */   private int metadata = -1;
/*  28 */   private int breakingAnimation = -1;
/*  29 */   private int smartLeaves = -1;
/*  30 */   private float[] quadBounds = new float[EnumFacing.VALUES.length * 2];
/*  31 */   private BitSet boundsFlags = new BitSet(3);
/*  32 */   private BlockModelRenderer.AmbientOcclusionFace aoFace = new BlockModelRenderer.AmbientOcclusionFace();
/*  33 */   private BlockPosM colorizerBlockPosM = null;
/*  34 */   private boolean[] borderFlags = null;
/*  35 */   private boolean[] borderFlags2 = null;
/*  36 */   private boolean[] borderFlags3 = null;
/*  37 */   private EnumFacing[] borderDirections = null;
/*  38 */   private List<BakedQuad> listQuadsCustomizer = new ArrayList<>();
/*  39 */   private List<BakedQuad> listQuadsCtmMultipass = new ArrayList<>();
/*  40 */   private BakedQuad[] arrayQuadsCtm1 = new BakedQuad[1];
/*  41 */   private BakedQuad[] arrayQuadsCtm2 = new BakedQuad[2];
/*  42 */   private BakedQuad[] arrayQuadsCtm3 = new BakedQuad[3];
/*  43 */   private BakedQuad[] arrayQuadsCtm4 = new BakedQuad[4];
/*  44 */   private RegionRenderCacheBuilder regionRenderCacheBuilder = null;
/*  45 */   private ListQuadsOverlay[] listsQuadsOverlay = new ListQuadsOverlay[(EnumWorldBlockLayer.values()).length];
/*     */   
/*     */   private boolean overlaysRendered = false;
/*     */   private static final int UNKNOWN = -1;
/*     */   private static final int FALSE = 0;
/*     */   private static final int TRUE = 1;
/*     */   
/*     */   public RenderEnv(IBlockState blockState, BlockPos blockPos) {
/*  53 */     this.blockState = blockState;
/*  54 */     this.blockPos = blockPos;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset(IBlockState blockStateIn, BlockPos blockPosIn) {
/*  59 */     if (this.blockState != blockStateIn || this.blockPos != blockPosIn) {
/*     */       
/*  61 */       this.blockState = blockStateIn;
/*  62 */       this.blockPos = blockPosIn;
/*  63 */       this.blockId = -1;
/*  64 */       this.metadata = -1;
/*  65 */       this.breakingAnimation = -1;
/*  66 */       this.smartLeaves = -1;
/*  67 */       this.boundsFlags.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBlockId() {
/*  73 */     if (this.blockId < 0)
/*     */     {
/*  75 */       if (this.blockState instanceof BlockStateBase) {
/*     */         
/*  77 */         BlockStateBase blockstatebase = (BlockStateBase)this.blockState;
/*  78 */         this.blockId = blockstatebase.getBlockId();
/*     */       }
/*     */       else {
/*     */         
/*  82 */         this.blockId = Block.getIdFromBlock(this.blockState.getBlock());
/*     */       } 
/*     */     }
/*     */     
/*  86 */     return this.blockId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMetadata() {
/*  91 */     if (this.metadata < 0)
/*     */     {
/*  93 */       if (this.blockState instanceof BlockStateBase) {
/*     */         
/*  95 */         BlockStateBase blockstatebase = (BlockStateBase)this.blockState;
/*  96 */         this.metadata = blockstatebase.getMetadata();
/*     */       }
/*     */       else {
/*     */         
/* 100 */         this.metadata = this.blockState.getBlock().getMetaFromState(this.blockState);
/*     */       } 
/*     */     }
/*     */     
/* 104 */     return this.metadata;
/*     */   }
/*     */ 
/*     */   
/*     */   public float[] getQuadBounds() {
/* 109 */     return this.quadBounds;
/*     */   }
/*     */ 
/*     */   
/*     */   public BitSet getBoundsFlags() {
/* 114 */     return this.boundsFlags;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockModelRenderer.AmbientOcclusionFace getAoFace() {
/* 119 */     return this.aoFace;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBreakingAnimation(List listQuads) {
/* 124 */     if (this.breakingAnimation == -1 && listQuads.size() > 0)
/*     */     {
/* 126 */       if (listQuads.get(0) instanceof net.minecraft.client.renderer.block.model.BreakingFour) {
/*     */         
/* 128 */         this.breakingAnimation = 1;
/*     */       }
/*     */       else {
/*     */         
/* 132 */         this.breakingAnimation = 0;
/*     */       } 
/*     */     }
/*     */     
/* 136 */     return (this.breakingAnimation == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBreakingAnimation(BakedQuad quad) {
/* 141 */     if (this.breakingAnimation < 0)
/*     */     {
/* 143 */       if (quad instanceof net.minecraft.client.renderer.block.model.BreakingFour) {
/*     */         
/* 145 */         this.breakingAnimation = 1;
/*     */       }
/*     */       else {
/*     */         
/* 149 */         this.breakingAnimation = 0;
/*     */       } 
/*     */     }
/*     */     
/* 153 */     return (this.breakingAnimation == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBreakingAnimation() {
/* 158 */     return (this.breakingAnimation == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getBlockState() {
/* 163 */     return this.blockState;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPosM getColorizerBlockPosM() {
/* 168 */     if (this.colorizerBlockPosM == null)
/*     */     {
/* 170 */       this.colorizerBlockPosM = new BlockPosM(0, 0, 0);
/*     */     }
/*     */     
/* 173 */     return this.colorizerBlockPosM;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean[] getBorderFlags() {
/* 178 */     if (this.borderFlags == null)
/*     */     {
/* 180 */       this.borderFlags = new boolean[4];
/*     */     }
/*     */     
/* 183 */     return this.borderFlags;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean[] getBorderFlags2() {
/* 188 */     if (this.borderFlags2 == null)
/*     */     {
/* 190 */       this.borderFlags2 = new boolean[4];
/*     */     }
/*     */     
/* 193 */     return this.borderFlags2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean[] getBorderFlags3() {
/* 198 */     if (this.borderFlags3 == null)
/*     */     {
/* 200 */       this.borderFlags3 = new boolean[4];
/*     */     }
/*     */     
/* 203 */     return this.borderFlags3;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumFacing[] getBorderDirections() {
/* 208 */     if (this.borderDirections == null)
/*     */     {
/* 210 */       this.borderDirections = new EnumFacing[4];
/*     */     }
/*     */     
/* 213 */     return this.borderDirections;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumFacing[] getBorderDirections(EnumFacing dir0, EnumFacing dir1, EnumFacing dir2, EnumFacing dir3) {
/* 218 */     EnumFacing[] aenumfacing = getBorderDirections();
/* 219 */     aenumfacing[0] = dir0;
/* 220 */     aenumfacing[1] = dir1;
/* 221 */     aenumfacing[2] = dir2;
/* 222 */     aenumfacing[3] = dir3;
/* 223 */     return aenumfacing;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSmartLeaves() {
/* 228 */     if (this.smartLeaves == -1)
/*     */     {
/* 230 */       if (Config.isTreesSmart() && this.blockState.getBlock() instanceof net.minecraft.block.BlockLeaves) {
/*     */         
/* 232 */         this.smartLeaves = 1;
/*     */       }
/*     */       else {
/*     */         
/* 236 */         this.smartLeaves = 0;
/*     */       } 
/*     */     }
/*     */     
/* 240 */     return (this.smartLeaves == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BakedQuad> getListQuadsCustomizer() {
/* 245 */     return this.listQuadsCustomizer;
/*     */   }
/*     */ 
/*     */   
/*     */   public BakedQuad[] getArrayQuadsCtm(BakedQuad quad) {
/* 250 */     this.arrayQuadsCtm1[0] = quad;
/* 251 */     return this.arrayQuadsCtm1;
/*     */   }
/*     */ 
/*     */   
/*     */   public BakedQuad[] getArrayQuadsCtm(BakedQuad quad0, BakedQuad quad1) {
/* 256 */     this.arrayQuadsCtm2[0] = quad0;
/* 257 */     this.arrayQuadsCtm2[1] = quad1;
/* 258 */     return this.arrayQuadsCtm2;
/*     */   }
/*     */ 
/*     */   
/*     */   public BakedQuad[] getArrayQuadsCtm(BakedQuad quad0, BakedQuad quad1, BakedQuad quad2) {
/* 263 */     this.arrayQuadsCtm3[0] = quad0;
/* 264 */     this.arrayQuadsCtm3[1] = quad1;
/* 265 */     this.arrayQuadsCtm3[2] = quad2;
/* 266 */     return this.arrayQuadsCtm3;
/*     */   }
/*     */ 
/*     */   
/*     */   public BakedQuad[] getArrayQuadsCtm(BakedQuad quad0, BakedQuad quad1, BakedQuad quad2, BakedQuad quad3) {
/* 271 */     this.arrayQuadsCtm4[0] = quad0;
/* 272 */     this.arrayQuadsCtm4[1] = quad1;
/* 273 */     this.arrayQuadsCtm4[2] = quad2;
/* 274 */     this.arrayQuadsCtm4[3] = quad3;
/* 275 */     return this.arrayQuadsCtm4;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BakedQuad> getListQuadsCtmMultipass(BakedQuad[] quads) {
/* 280 */     this.listQuadsCtmMultipass.clear();
/*     */     
/* 282 */     if (quads != null)
/*     */     {
/* 284 */       for (int i = 0; i < quads.length; i++) {
/*     */         
/* 286 */         BakedQuad bakedquad = quads[i];
/* 287 */         this.listQuadsCtmMultipass.add(bakedquad);
/*     */       } 
/*     */     }
/*     */     
/* 291 */     return this.listQuadsCtmMultipass;
/*     */   }
/*     */ 
/*     */   
/*     */   public RegionRenderCacheBuilder getRegionRenderCacheBuilder() {
/* 296 */     return this.regionRenderCacheBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRegionRenderCacheBuilder(RegionRenderCacheBuilder regionRenderCacheBuilder) {
/* 301 */     this.regionRenderCacheBuilder = regionRenderCacheBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   public ListQuadsOverlay getListQuadsOverlay(EnumWorldBlockLayer layer) {
/* 306 */     ListQuadsOverlay listquadsoverlay = this.listsQuadsOverlay[layer.ordinal()];
/*     */     
/* 308 */     if (listquadsoverlay == null) {
/*     */       
/* 310 */       listquadsoverlay = new ListQuadsOverlay();
/* 311 */       this.listsQuadsOverlay[layer.ordinal()] = listquadsoverlay;
/*     */     } 
/*     */     
/* 314 */     return listquadsoverlay;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOverlaysRendered() {
/* 319 */     return this.overlaysRendered;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOverlaysRendered(boolean overlaysRendered) {
/* 324 */     this.overlaysRendered = overlaysRendered;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\render\RenderEnv.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */