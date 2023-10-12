/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.client.renderer.block.model.BreakingFour;
/*     */ import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
/*     */ import net.minecraft.client.renderer.block.model.ModelBlock;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ 
/*     */ public class SimpleBakedModel
/*     */   implements IBakedModel
/*     */ {
/*     */   protected final List<BakedQuad> generalQuads;
/*     */   protected final List<List<BakedQuad>> faceQuads;
/*     */   protected final boolean ambientOcclusion;
/*     */   protected final boolean gui3d;
/*     */   protected final TextureAtlasSprite texture;
/*     */   protected final ItemCameraTransforms cameraTransforms;
/*     */   
/*     */   public SimpleBakedModel(List<BakedQuad> generalQuadsIn, List<List<BakedQuad>> faceQuadsIn, boolean ambientOcclusionIn, boolean gui3dIn, TextureAtlasSprite textureIn, ItemCameraTransforms cameraTransformsIn) {
/*  23 */     this.generalQuads = generalQuadsIn;
/*  24 */     this.faceQuads = faceQuadsIn;
/*  25 */     this.ambientOcclusion = ambientOcclusionIn;
/*  26 */     this.gui3d = gui3dIn;
/*  27 */     this.texture = textureIn;
/*  28 */     this.cameraTransforms = cameraTransformsIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BakedQuad> getFaceQuads(EnumFacing facing) {
/*  33 */     return this.faceQuads.get(facing.ordinal());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BakedQuad> getGeneralQuads() {
/*  38 */     return this.generalQuads;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAmbientOcclusion() {
/*  43 */     return this.ambientOcclusion;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isGui3d() {
/*  48 */     return this.gui3d;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBuiltInRenderer() {
/*  53 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureAtlasSprite getParticleTexture() {
/*  58 */     return this.texture;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemCameraTransforms getItemCameraTransforms() {
/*  63 */     return this.cameraTransforms;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final List<BakedQuad> builderGeneralQuads;
/*     */     private final List<List<BakedQuad>> builderFaceQuads;
/*     */     private final boolean builderAmbientOcclusion;
/*     */     private TextureAtlasSprite builderTexture;
/*     */     private boolean builderGui3d;
/*     */     private ItemCameraTransforms builderCameraTransforms;
/*     */     
/*     */     public Builder(ModelBlock model) {
/*  77 */       this(model.isAmbientOcclusion(), model.isGui3d(), model.getAllTransforms());
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder(IBakedModel bakedModel, TextureAtlasSprite texture) {
/*  82 */       this(bakedModel.isAmbientOcclusion(), bakedModel.isGui3d(), bakedModel.getItemCameraTransforms());
/*  83 */       this.builderTexture = bakedModel.getParticleTexture();
/*     */       
/*  85 */       for (EnumFacing enumfacing : EnumFacing.values())
/*     */       {
/*  87 */         addFaceBreakingFours(bakedModel, texture, enumfacing);
/*     */       }
/*     */       
/*  90 */       addGeneralBreakingFours(bakedModel, texture);
/*     */     }
/*     */ 
/*     */     
/*     */     private void addFaceBreakingFours(IBakedModel bakedModel, TextureAtlasSprite texture, EnumFacing facing) {
/*  95 */       for (BakedQuad bakedquad : bakedModel.getFaceQuads(facing))
/*     */       {
/*  97 */         addFaceQuad(facing, (BakedQuad)new BreakingFour(bakedquad, texture));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     private void addGeneralBreakingFours(IBakedModel p_177647_1_, TextureAtlasSprite texture) {
/* 103 */       for (BakedQuad bakedquad : p_177647_1_.getGeneralQuads())
/*     */       {
/* 105 */         addGeneralQuad((BakedQuad)new BreakingFour(bakedquad, texture));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     private Builder(boolean ambientOcclusion, boolean gui3d, ItemCameraTransforms cameraTransforms) {
/* 111 */       this.builderGeneralQuads = Lists.newArrayList();
/* 112 */       this.builderFaceQuads = Lists.newArrayListWithCapacity(6);
/*     */       
/* 114 */       for (EnumFacing enumfacing : EnumFacing.values())
/*     */       {
/* 116 */         this.builderFaceQuads.add(Lists.newArrayList());
/*     */       }
/*     */       
/* 119 */       this.builderAmbientOcclusion = ambientOcclusion;
/* 120 */       this.builderGui3d = gui3d;
/* 121 */       this.builderCameraTransforms = cameraTransforms;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder addFaceQuad(EnumFacing facing, BakedQuad quad) {
/* 126 */       ((List<BakedQuad>)this.builderFaceQuads.get(facing.ordinal())).add(quad);
/* 127 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder addGeneralQuad(BakedQuad quad) {
/* 132 */       this.builderGeneralQuads.add(quad);
/* 133 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder setTexture(TextureAtlasSprite texture) {
/* 138 */       this.builderTexture = texture;
/* 139 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public IBakedModel makeBakedModel() {
/* 144 */       if (this.builderTexture == null)
/*     */       {
/* 146 */         throw new RuntimeException("Missing particle!");
/*     */       }
/*     */ 
/*     */       
/* 150 */       return new SimpleBakedModel(this.builderGeneralQuads, this.builderFaceQuads, this.builderAmbientOcclusion, this.builderGui3d, this.builderTexture, this.builderCameraTransforms);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\resources\model\SimpleBakedModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */