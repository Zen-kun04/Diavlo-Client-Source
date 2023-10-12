/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.boss.EntityDragon;
/*     */ 
/*     */ public class ModelDragon
/*     */   extends ModelBase
/*     */ {
/*     */   private ModelRenderer head;
/*     */   private ModelRenderer spine;
/*     */   private ModelRenderer jaw;
/*     */   private ModelRenderer body;
/*     */   private ModelRenderer rearLeg;
/*     */   private ModelRenderer frontLeg;
/*     */   private ModelRenderer rearLegTip;
/*     */   private ModelRenderer frontLegTip;
/*     */   private ModelRenderer rearFoot;
/*     */   private ModelRenderer frontFoot;
/*     */   private ModelRenderer wing;
/*     */   private ModelRenderer wingTip;
/*     */   private float partialTicks;
/*     */   
/*     */   public ModelDragon(float p_i46360_1_) {
/*  26 */     this.textureWidth = 256;
/*  27 */     this.textureHeight = 256;
/*  28 */     setTextureOffset("body.body", 0, 0);
/*  29 */     setTextureOffset("wing.skin", -56, 88);
/*  30 */     setTextureOffset("wingtip.skin", -56, 144);
/*  31 */     setTextureOffset("rearleg.main", 0, 0);
/*  32 */     setTextureOffset("rearfoot.main", 112, 0);
/*  33 */     setTextureOffset("rearlegtip.main", 196, 0);
/*  34 */     setTextureOffset("head.upperhead", 112, 30);
/*  35 */     setTextureOffset("wing.bone", 112, 88);
/*  36 */     setTextureOffset("head.upperlip", 176, 44);
/*  37 */     setTextureOffset("jaw.jaw", 176, 65);
/*  38 */     setTextureOffset("frontleg.main", 112, 104);
/*  39 */     setTextureOffset("wingtip.bone", 112, 136);
/*  40 */     setTextureOffset("frontfoot.main", 144, 104);
/*  41 */     setTextureOffset("neck.box", 192, 104);
/*  42 */     setTextureOffset("frontlegtip.main", 226, 138);
/*  43 */     setTextureOffset("body.scale", 220, 53);
/*  44 */     setTextureOffset("head.scale", 0, 0);
/*  45 */     setTextureOffset("neck.scale", 48, 0);
/*  46 */     setTextureOffset("head.nostril", 112, 0);
/*  47 */     float f = -16.0F;
/*  48 */     this.head = new ModelRenderer(this, "head");
/*  49 */     this.head.addBox("upperlip", -6.0F, -1.0F, -8.0F + f, 12, 5, 16);
/*  50 */     this.head.addBox("upperhead", -8.0F, -8.0F, 6.0F + f, 16, 16, 16);
/*  51 */     this.head.mirror = true;
/*  52 */     this.head.addBox("scale", -5.0F, -12.0F, 12.0F + f, 2, 4, 6);
/*  53 */     this.head.addBox("nostril", -5.0F, -3.0F, -6.0F + f, 2, 2, 4);
/*  54 */     this.head.mirror = false;
/*  55 */     this.head.addBox("scale", 3.0F, -12.0F, 12.0F + f, 2, 4, 6);
/*  56 */     this.head.addBox("nostril", 3.0F, -3.0F, -6.0F + f, 2, 2, 4);
/*  57 */     this.jaw = new ModelRenderer(this, "jaw");
/*  58 */     this.jaw.setRotationPoint(0.0F, 4.0F, 8.0F + f);
/*  59 */     this.jaw.addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16);
/*  60 */     this.head.addChild(this.jaw);
/*  61 */     this.spine = new ModelRenderer(this, "neck");
/*  62 */     this.spine.addBox("box", -5.0F, -5.0F, -5.0F, 10, 10, 10);
/*  63 */     this.spine.addBox("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6);
/*  64 */     this.body = new ModelRenderer(this, "body");
/*  65 */     this.body.setRotationPoint(0.0F, 4.0F, 8.0F);
/*  66 */     this.body.addBox("body", -12.0F, 0.0F, -16.0F, 24, 24, 64);
/*  67 */     this.body.addBox("scale", -1.0F, -6.0F, -10.0F, 2, 6, 12);
/*  68 */     this.body.addBox("scale", -1.0F, -6.0F, 10.0F, 2, 6, 12);
/*  69 */     this.body.addBox("scale", -1.0F, -6.0F, 30.0F, 2, 6, 12);
/*  70 */     this.wing = new ModelRenderer(this, "wing");
/*  71 */     this.wing.setRotationPoint(-12.0F, 5.0F, 2.0F);
/*  72 */     this.wing.addBox("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8);
/*  73 */     this.wing.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
/*  74 */     this.wingTip = new ModelRenderer(this, "wingtip");
/*  75 */     this.wingTip.setRotationPoint(-56.0F, 0.0F, 0.0F);
/*  76 */     this.wingTip.addBox("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4);
/*  77 */     this.wingTip.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
/*  78 */     this.wing.addChild(this.wingTip);
/*  79 */     this.frontLeg = new ModelRenderer(this, "frontleg");
/*  80 */     this.frontLeg.setRotationPoint(-12.0F, 20.0F, 2.0F);
/*  81 */     this.frontLeg.addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8);
/*  82 */     this.frontLegTip = new ModelRenderer(this, "frontlegtip");
/*  83 */     this.frontLegTip.setRotationPoint(0.0F, 20.0F, -1.0F);
/*  84 */     this.frontLegTip.addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6);
/*  85 */     this.frontLeg.addChild(this.frontLegTip);
/*  86 */     this.frontFoot = new ModelRenderer(this, "frontfoot");
/*  87 */     this.frontFoot.setRotationPoint(0.0F, 23.0F, 0.0F);
/*  88 */     this.frontFoot.addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16);
/*  89 */     this.frontLegTip.addChild(this.frontFoot);
/*  90 */     this.rearLeg = new ModelRenderer(this, "rearleg");
/*  91 */     this.rearLeg.setRotationPoint(-16.0F, 16.0F, 42.0F);
/*  92 */     this.rearLeg.addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16);
/*  93 */     this.rearLegTip = new ModelRenderer(this, "rearlegtip");
/*  94 */     this.rearLegTip.setRotationPoint(0.0F, 32.0F, -4.0F);
/*  95 */     this.rearLegTip.addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12);
/*  96 */     this.rearLeg.addChild(this.rearLegTip);
/*  97 */     this.rearFoot = new ModelRenderer(this, "rearfoot");
/*  98 */     this.rearFoot.setRotationPoint(0.0F, 31.0F, 4.0F);
/*  99 */     this.rearFoot.addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24);
/* 100 */     this.rearLegTip.addChild(this.rearFoot);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {
/* 105 */     this.partialTicks = partialTickTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/* 110 */     GlStateManager.pushMatrix();
/* 111 */     EntityDragon entitydragon = (EntityDragon)entityIn;
/* 112 */     float f = entitydragon.prevAnimTime + (entitydragon.animTime - entitydragon.prevAnimTime) * this.partialTicks;
/* 113 */     this.jaw.rotateAngleX = (float)(Math.sin((f * 3.1415927F * 2.0F)) + 1.0D) * 0.2F;
/* 114 */     float f1 = (float)(Math.sin((f * 3.1415927F * 2.0F - 1.0F)) + 1.0D);
/* 115 */     f1 = (f1 * f1 * 1.0F + f1 * 2.0F) * 0.05F;
/* 116 */     GlStateManager.translate(0.0F, f1 - 2.0F, -3.0F);
/* 117 */     GlStateManager.rotate(f1 * 2.0F, 1.0F, 0.0F, 0.0F);
/* 118 */     float f2 = -30.0F;
/* 119 */     float f4 = 0.0F;
/* 120 */     float f5 = 1.5F;
/* 121 */     double[] adouble = entitydragon.getMovementOffsets(6, this.partialTicks);
/* 122 */     float f6 = updateRotations(entitydragon.getMovementOffsets(5, this.partialTicks)[0] - entitydragon.getMovementOffsets(10, this.partialTicks)[0]);
/* 123 */     float f7 = updateRotations(entitydragon.getMovementOffsets(5, this.partialTicks)[0] + (f6 / 2.0F));
/* 124 */     f2 += 2.0F;
/* 125 */     float f8 = f * 3.1415927F * 2.0F;
/* 126 */     f2 = 20.0F;
/* 127 */     float f3 = -12.0F;
/*     */     
/* 129 */     for (int i = 0; i < 5; i++) {
/*     */       
/* 131 */       double[] adouble1 = entitydragon.getMovementOffsets(5 - i, this.partialTicks);
/* 132 */       float f9 = (float)Math.cos((i * 0.45F + f8)) * 0.15F;
/* 133 */       this.spine.rotateAngleY = updateRotations(adouble1[0] - adouble[0]) * 3.1415927F / 180.0F * f5;
/* 134 */       this.spine.rotateAngleX = f9 + (float)(adouble1[1] - adouble[1]) * 3.1415927F / 180.0F * f5 * 5.0F;
/* 135 */       this.spine.rotateAngleZ = -updateRotations(adouble1[0] - f7) * 3.1415927F / 180.0F * f5;
/* 136 */       this.spine.rotationPointY = f2;
/* 137 */       this.spine.rotationPointZ = f3;
/* 138 */       this.spine.rotationPointX = f4;
/* 139 */       f2 = (float)(f2 + Math.sin(this.spine.rotateAngleX) * 10.0D);
/* 140 */       f3 = (float)(f3 - Math.cos(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0D);
/* 141 */       f4 = (float)(f4 - Math.sin(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0D);
/* 142 */       this.spine.render(scale);
/*     */     } 
/*     */     
/* 145 */     this.head.rotationPointY = f2;
/* 146 */     this.head.rotationPointZ = f3;
/* 147 */     this.head.rotationPointX = f4;
/* 148 */     double[] adouble2 = entitydragon.getMovementOffsets(0, this.partialTicks);
/* 149 */     this.head.rotateAngleY = updateRotations(adouble2[0] - adouble[0]) * 3.1415927F / 180.0F * 1.0F;
/* 150 */     this.head.rotateAngleZ = -updateRotations(adouble2[0] - f7) * 3.1415927F / 180.0F * 1.0F;
/* 151 */     this.head.render(scale);
/* 152 */     GlStateManager.pushMatrix();
/* 153 */     GlStateManager.translate(0.0F, 1.0F, 0.0F);
/* 154 */     GlStateManager.rotate(-f6 * f5 * 1.0F, 0.0F, 0.0F, 1.0F);
/* 155 */     GlStateManager.translate(0.0F, -1.0F, 0.0F);
/* 156 */     this.body.rotateAngleZ = 0.0F;
/* 157 */     this.body.render(scale);
/*     */     
/* 159 */     for (int j = 0; j < 2; j++) {
/*     */       
/* 161 */       GlStateManager.enableCull();
/* 162 */       float f11 = f * 3.1415927F * 2.0F;
/* 163 */       this.wing.rotateAngleX = 0.125F - (float)Math.cos(f11) * 0.2F;
/* 164 */       this.wing.rotateAngleY = 0.25F;
/* 165 */       this.wing.rotateAngleZ = (float)(Math.sin(f11) + 0.125D) * 0.8F;
/* 166 */       this.wingTip.rotateAngleZ = -((float)(Math.sin((f11 + 2.0F)) + 0.5D)) * 0.75F;
/* 167 */       this.rearLeg.rotateAngleX = 1.0F + f1 * 0.1F;
/* 168 */       this.rearLegTip.rotateAngleX = 0.5F + f1 * 0.1F;
/* 169 */       this.rearFoot.rotateAngleX = 0.75F + f1 * 0.1F;
/* 170 */       this.frontLeg.rotateAngleX = 1.3F + f1 * 0.1F;
/* 171 */       this.frontLegTip.rotateAngleX = -0.5F - f1 * 0.1F;
/* 172 */       this.frontFoot.rotateAngleX = 0.75F + f1 * 0.1F;
/* 173 */       this.wing.render(scale);
/* 174 */       this.frontLeg.render(scale);
/* 175 */       this.rearLeg.render(scale);
/* 176 */       GlStateManager.scale(-1.0F, 1.0F, 1.0F);
/*     */       
/* 178 */       if (j == 0)
/*     */       {
/* 180 */         GlStateManager.cullFace(1028);
/*     */       }
/*     */     } 
/*     */     
/* 184 */     GlStateManager.popMatrix();
/* 185 */     GlStateManager.cullFace(1029);
/* 186 */     GlStateManager.disableCull();
/* 187 */     float f10 = -((float)Math.sin((f * 3.1415927F * 2.0F))) * 0.0F;
/* 188 */     f8 = f * 3.1415927F * 2.0F;
/* 189 */     f2 = 10.0F;
/* 190 */     f3 = 60.0F;
/* 191 */     f4 = 0.0F;
/* 192 */     adouble = entitydragon.getMovementOffsets(11, this.partialTicks);
/*     */     
/* 194 */     for (int k = 0; k < 12; k++) {
/*     */       
/* 196 */       adouble2 = entitydragon.getMovementOffsets(12 + k, this.partialTicks);
/* 197 */       f10 = (float)(f10 + Math.sin((k * 0.45F + f8)) * 0.05000000074505806D);
/* 198 */       this.spine.rotateAngleY = (updateRotations(adouble2[0] - adouble[0]) * f5 + 180.0F) * 3.1415927F / 180.0F;
/* 199 */       this.spine.rotateAngleX = f10 + (float)(adouble2[1] - adouble[1]) * 3.1415927F / 180.0F * f5 * 5.0F;
/* 200 */       this.spine.rotateAngleZ = updateRotations(adouble2[0] - f7) * 3.1415927F / 180.0F * f5;
/* 201 */       this.spine.rotationPointY = f2;
/* 202 */       this.spine.rotationPointZ = f3;
/* 203 */       this.spine.rotationPointX = f4;
/* 204 */       f2 = (float)(f2 + Math.sin(this.spine.rotateAngleX) * 10.0D);
/* 205 */       f3 = (float)(f3 - Math.cos(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0D);
/* 206 */       f4 = (float)(f4 - Math.sin(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0D);
/* 207 */       this.spine.render(scale);
/*     */     } 
/*     */     
/* 210 */     GlStateManager.popMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   private float updateRotations(double p_78214_1_) {
/* 215 */     while (p_78214_1_ >= 180.0D)
/*     */     {
/* 217 */       p_78214_1_ -= 360.0D;
/*     */     }
/*     */     
/* 220 */     while (p_78214_1_ < -180.0D)
/*     */     {
/* 222 */       p_78214_1_ += 360.0D;
/*     */     }
/*     */     
/* 225 */     return (float)p_78214_1_;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelDragon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */