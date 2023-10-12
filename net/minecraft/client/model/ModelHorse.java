/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityHorse;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class ModelHorse
/*     */   extends ModelBase
/*     */ {
/*     */   private ModelRenderer head;
/*     */   private ModelRenderer field_178711_b;
/*     */   private ModelRenderer field_178712_c;
/*     */   private ModelRenderer horseLeftEar;
/*     */   private ModelRenderer horseRightEar;
/*     */   private ModelRenderer muleLeftEar;
/*     */   private ModelRenderer muleRightEar;
/*     */   private ModelRenderer neck;
/*     */   private ModelRenderer horseFaceRopes;
/*     */   private ModelRenderer mane;
/*     */   private ModelRenderer body;
/*     */   private ModelRenderer tailBase;
/*     */   private ModelRenderer tailMiddle;
/*     */   private ModelRenderer tailTip;
/*     */   private ModelRenderer backLeftLeg;
/*     */   private ModelRenderer backLeftShin;
/*     */   private ModelRenderer backLeftHoof;
/*     */   private ModelRenderer backRightLeg;
/*     */   private ModelRenderer backRightShin;
/*     */   private ModelRenderer backRightHoof;
/*     */   private ModelRenderer frontLeftLeg;
/*     */   private ModelRenderer frontLeftShin;
/*     */   private ModelRenderer frontLeftHoof;
/*     */   private ModelRenderer frontRightLeg;
/*     */   private ModelRenderer frontRightShin;
/*     */   private ModelRenderer frontRightHoof;
/*     */   private ModelRenderer muleLeftChest;
/*     */   private ModelRenderer muleRightChest;
/*     */   private ModelRenderer horseSaddleBottom;
/*     */   private ModelRenderer horseSaddleFront;
/*     */   private ModelRenderer horseSaddleBack;
/*     */   private ModelRenderer horseLeftSaddleRope;
/*     */   private ModelRenderer horseLeftSaddleMetal;
/*     */   private ModelRenderer horseRightSaddleRope;
/*     */   private ModelRenderer horseRightSaddleMetal;
/*     */   private ModelRenderer horseLeftFaceMetal;
/*     */   private ModelRenderer horseRightFaceMetal;
/*     */   private ModelRenderer horseLeftRein;
/*     */   private ModelRenderer horseRightRein;
/*     */   
/*     */   public ModelHorse() {
/*  53 */     this.textureWidth = 128;
/*  54 */     this.textureHeight = 128;
/*  55 */     this.body = new ModelRenderer(this, 0, 34);
/*  56 */     this.body.addBox(-5.0F, -8.0F, -19.0F, 10, 10, 24);
/*  57 */     this.body.setRotationPoint(0.0F, 11.0F, 9.0F);
/*  58 */     this.tailBase = new ModelRenderer(this, 44, 0);
/*  59 */     this.tailBase.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 3);
/*  60 */     this.tailBase.setRotationPoint(0.0F, 3.0F, 14.0F);
/*  61 */     setBoxRotation(this.tailBase, -1.134464F, 0.0F, 0.0F);
/*  62 */     this.tailMiddle = new ModelRenderer(this, 38, 7);
/*  63 */     this.tailMiddle.addBox(-1.5F, -2.0F, 3.0F, 3, 4, 7);
/*  64 */     this.tailMiddle.setRotationPoint(0.0F, 3.0F, 14.0F);
/*  65 */     setBoxRotation(this.tailMiddle, -1.134464F, 0.0F, 0.0F);
/*  66 */     this.tailTip = new ModelRenderer(this, 24, 3);
/*  67 */     this.tailTip.addBox(-1.5F, -4.5F, 9.0F, 3, 4, 7);
/*  68 */     this.tailTip.setRotationPoint(0.0F, 3.0F, 14.0F);
/*  69 */     setBoxRotation(this.tailTip, -1.40215F, 0.0F, 0.0F);
/*  70 */     this.backLeftLeg = new ModelRenderer(this, 78, 29);
/*  71 */     this.backLeftLeg.addBox(-2.5F, -2.0F, -2.5F, 4, 9, 5);
/*  72 */     this.backLeftLeg.setRotationPoint(4.0F, 9.0F, 11.0F);
/*  73 */     this.backLeftShin = new ModelRenderer(this, 78, 43);
/*  74 */     this.backLeftShin.addBox(-2.0F, 0.0F, -1.5F, 3, 5, 3);
/*  75 */     this.backLeftShin.setRotationPoint(4.0F, 16.0F, 11.0F);
/*  76 */     this.backLeftHoof = new ModelRenderer(this, 78, 51);
/*  77 */     this.backLeftHoof.addBox(-2.5F, 5.1F, -2.0F, 4, 3, 4);
/*  78 */     this.backLeftHoof.setRotationPoint(4.0F, 16.0F, 11.0F);
/*  79 */     this.backRightLeg = new ModelRenderer(this, 96, 29);
/*  80 */     this.backRightLeg.addBox(-1.5F, -2.0F, -2.5F, 4, 9, 5);
/*  81 */     this.backRightLeg.setRotationPoint(-4.0F, 9.0F, 11.0F);
/*  82 */     this.backRightShin = new ModelRenderer(this, 96, 43);
/*  83 */     this.backRightShin.addBox(-1.0F, 0.0F, -1.5F, 3, 5, 3);
/*  84 */     this.backRightShin.setRotationPoint(-4.0F, 16.0F, 11.0F);
/*  85 */     this.backRightHoof = new ModelRenderer(this, 96, 51);
/*  86 */     this.backRightHoof.addBox(-1.5F, 5.1F, -2.0F, 4, 3, 4);
/*  87 */     this.backRightHoof.setRotationPoint(-4.0F, 16.0F, 11.0F);
/*  88 */     this.frontLeftLeg = new ModelRenderer(this, 44, 29);
/*  89 */     this.frontLeftLeg.addBox(-1.9F, -1.0F, -2.1F, 3, 8, 4);
/*  90 */     this.frontLeftLeg.setRotationPoint(4.0F, 9.0F, -8.0F);
/*  91 */     this.frontLeftShin = new ModelRenderer(this, 44, 41);
/*  92 */     this.frontLeftShin.addBox(-1.9F, 0.0F, -1.6F, 3, 5, 3);
/*  93 */     this.frontLeftShin.setRotationPoint(4.0F, 16.0F, -8.0F);
/*  94 */     this.frontLeftHoof = new ModelRenderer(this, 44, 51);
/*  95 */     this.frontLeftHoof.addBox(-2.4F, 5.1F, -2.1F, 4, 3, 4);
/*  96 */     this.frontLeftHoof.setRotationPoint(4.0F, 16.0F, -8.0F);
/*  97 */     this.frontRightLeg = new ModelRenderer(this, 60, 29);
/*  98 */     this.frontRightLeg.addBox(-1.1F, -1.0F, -2.1F, 3, 8, 4);
/*  99 */     this.frontRightLeg.setRotationPoint(-4.0F, 9.0F, -8.0F);
/* 100 */     this.frontRightShin = new ModelRenderer(this, 60, 41);
/* 101 */     this.frontRightShin.addBox(-1.1F, 0.0F, -1.6F, 3, 5, 3);
/* 102 */     this.frontRightShin.setRotationPoint(-4.0F, 16.0F, -8.0F);
/* 103 */     this.frontRightHoof = new ModelRenderer(this, 60, 51);
/* 104 */     this.frontRightHoof.addBox(-1.6F, 5.1F, -2.1F, 4, 3, 4);
/* 105 */     this.frontRightHoof.setRotationPoint(-4.0F, 16.0F, -8.0F);
/* 106 */     this.head = new ModelRenderer(this, 0, 0);
/* 107 */     this.head.addBox(-2.5F, -10.0F, -1.5F, 5, 5, 7);
/* 108 */     this.head.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 109 */     setBoxRotation(this.head, 0.5235988F, 0.0F, 0.0F);
/* 110 */     this.field_178711_b = new ModelRenderer(this, 24, 18);
/* 111 */     this.field_178711_b.addBox(-2.0F, -10.0F, -7.0F, 4, 3, 6);
/* 112 */     this.field_178711_b.setRotationPoint(0.0F, 3.95F, -10.0F);
/* 113 */     setBoxRotation(this.field_178711_b, 0.5235988F, 0.0F, 0.0F);
/* 114 */     this.field_178712_c = new ModelRenderer(this, 24, 27);
/* 115 */     this.field_178712_c.addBox(-2.0F, -7.0F, -6.5F, 4, 2, 5);
/* 116 */     this.field_178712_c.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 117 */     setBoxRotation(this.field_178712_c, 0.5235988F, 0.0F, 0.0F);
/* 118 */     this.head.addChild(this.field_178711_b);
/* 119 */     this.head.addChild(this.field_178712_c);
/* 120 */     this.horseLeftEar = new ModelRenderer(this, 0, 0);
/* 121 */     this.horseLeftEar.addBox(0.45F, -12.0F, 4.0F, 2, 3, 1);
/* 122 */     this.horseLeftEar.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 123 */     setBoxRotation(this.horseLeftEar, 0.5235988F, 0.0F, 0.0F);
/* 124 */     this.horseRightEar = new ModelRenderer(this, 0, 0);
/* 125 */     this.horseRightEar.addBox(-2.45F, -12.0F, 4.0F, 2, 3, 1);
/* 126 */     this.horseRightEar.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 127 */     setBoxRotation(this.horseRightEar, 0.5235988F, 0.0F, 0.0F);
/* 128 */     this.muleLeftEar = new ModelRenderer(this, 0, 12);
/* 129 */     this.muleLeftEar.addBox(-2.0F, -16.0F, 4.0F, 2, 7, 1);
/* 130 */     this.muleLeftEar.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 131 */     setBoxRotation(this.muleLeftEar, 0.5235988F, 0.0F, 0.2617994F);
/* 132 */     this.muleRightEar = new ModelRenderer(this, 0, 12);
/* 133 */     this.muleRightEar.addBox(0.0F, -16.0F, 4.0F, 2, 7, 1);
/* 134 */     this.muleRightEar.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 135 */     setBoxRotation(this.muleRightEar, 0.5235988F, 0.0F, -0.2617994F);
/* 136 */     this.neck = new ModelRenderer(this, 0, 12);
/* 137 */     this.neck.addBox(-2.05F, -9.8F, -2.0F, 4, 14, 8);
/* 138 */     this.neck.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 139 */     setBoxRotation(this.neck, 0.5235988F, 0.0F, 0.0F);
/* 140 */     this.muleLeftChest = new ModelRenderer(this, 0, 34);
/* 141 */     this.muleLeftChest.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3);
/* 142 */     this.muleLeftChest.setRotationPoint(-7.5F, 3.0F, 10.0F);
/* 143 */     setBoxRotation(this.muleLeftChest, 0.0F, 1.5707964F, 0.0F);
/* 144 */     this.muleRightChest = new ModelRenderer(this, 0, 47);
/* 145 */     this.muleRightChest.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3);
/* 146 */     this.muleRightChest.setRotationPoint(4.5F, 3.0F, 10.0F);
/* 147 */     setBoxRotation(this.muleRightChest, 0.0F, 1.5707964F, 0.0F);
/* 148 */     this.horseSaddleBottom = new ModelRenderer(this, 80, 0);
/* 149 */     this.horseSaddleBottom.addBox(-5.0F, 0.0F, -3.0F, 10, 1, 8);
/* 150 */     this.horseSaddleBottom.setRotationPoint(0.0F, 2.0F, 2.0F);
/* 151 */     this.horseSaddleFront = new ModelRenderer(this, 106, 9);
/* 152 */     this.horseSaddleFront.addBox(-1.5F, -1.0F, -3.0F, 3, 1, 2);
/* 153 */     this.horseSaddleFront.setRotationPoint(0.0F, 2.0F, 2.0F);
/* 154 */     this.horseSaddleBack = new ModelRenderer(this, 80, 9);
/* 155 */     this.horseSaddleBack.addBox(-4.0F, -1.0F, 3.0F, 8, 1, 2);
/* 156 */     this.horseSaddleBack.setRotationPoint(0.0F, 2.0F, 2.0F);
/* 157 */     this.horseLeftSaddleMetal = new ModelRenderer(this, 74, 0);
/* 158 */     this.horseLeftSaddleMetal.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2);
/* 159 */     this.horseLeftSaddleMetal.setRotationPoint(5.0F, 3.0F, 2.0F);
/* 160 */     this.horseLeftSaddleRope = new ModelRenderer(this, 70, 0);
/* 161 */     this.horseLeftSaddleRope.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
/* 162 */     this.horseLeftSaddleRope.setRotationPoint(5.0F, 3.0F, 2.0F);
/* 163 */     this.horseRightSaddleMetal = new ModelRenderer(this, 74, 4);
/* 164 */     this.horseRightSaddleMetal.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2);
/* 165 */     this.horseRightSaddleMetal.setRotationPoint(-5.0F, 3.0F, 2.0F);
/* 166 */     this.horseRightSaddleRope = new ModelRenderer(this, 80, 0);
/* 167 */     this.horseRightSaddleRope.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
/* 168 */     this.horseRightSaddleRope.setRotationPoint(-5.0F, 3.0F, 2.0F);
/* 169 */     this.horseLeftFaceMetal = new ModelRenderer(this, 74, 13);
/* 170 */     this.horseLeftFaceMetal.addBox(1.5F, -8.0F, -4.0F, 1, 2, 2);
/* 171 */     this.horseLeftFaceMetal.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 172 */     setBoxRotation(this.horseLeftFaceMetal, 0.5235988F, 0.0F, 0.0F);
/* 173 */     this.horseRightFaceMetal = new ModelRenderer(this, 74, 13);
/* 174 */     this.horseRightFaceMetal.addBox(-2.5F, -8.0F, -4.0F, 1, 2, 2);
/* 175 */     this.horseRightFaceMetal.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 176 */     setBoxRotation(this.horseRightFaceMetal, 0.5235988F, 0.0F, 0.0F);
/* 177 */     this.horseLeftRein = new ModelRenderer(this, 44, 10);
/* 178 */     this.horseLeftRein.addBox(2.6F, -6.0F, -6.0F, 0, 3, 16);
/* 179 */     this.horseLeftRein.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 180 */     this.horseRightRein = new ModelRenderer(this, 44, 5);
/* 181 */     this.horseRightRein.addBox(-2.6F, -6.0F, -6.0F, 0, 3, 16);
/* 182 */     this.horseRightRein.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 183 */     this.mane = new ModelRenderer(this, 58, 0);
/* 184 */     this.mane.addBox(-1.0F, -11.5F, 5.0F, 2, 16, 4);
/* 185 */     this.mane.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 186 */     setBoxRotation(this.mane, 0.5235988F, 0.0F, 0.0F);
/* 187 */     this.horseFaceRopes = new ModelRenderer(this, 80, 12);
/* 188 */     this.horseFaceRopes.addBox(-2.5F, -10.1F, -7.0F, 5, 5, 12, 0.2F);
/* 189 */     this.horseFaceRopes.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 190 */     setBoxRotation(this.horseFaceRopes, 0.5235988F, 0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
/* 195 */     EntityHorse entityhorse = (EntityHorse)entityIn;
/* 196 */     int i = entityhorse.getHorseType();
/* 197 */     float f = entityhorse.getGrassEatingAmount(0.0F);
/* 198 */     boolean flag = entityhorse.isAdultHorse();
/* 199 */     boolean flag1 = (flag && entityhorse.isHorseSaddled());
/* 200 */     boolean flag2 = (flag && entityhorse.isChested());
/* 201 */     boolean flag3 = (i == 1 || i == 2);
/* 202 */     float f1 = entityhorse.getHorseSize();
/* 203 */     boolean flag4 = (entityhorse.riddenByEntity != null);
/*     */     
/* 205 */     if (flag1) {
/*     */       
/* 207 */       this.horseFaceRopes.render(scale);
/* 208 */       this.horseSaddleBottom.render(scale);
/* 209 */       this.horseSaddleFront.render(scale);
/* 210 */       this.horseSaddleBack.render(scale);
/* 211 */       this.horseLeftSaddleRope.render(scale);
/* 212 */       this.horseLeftSaddleMetal.render(scale);
/* 213 */       this.horseRightSaddleRope.render(scale);
/* 214 */       this.horseRightSaddleMetal.render(scale);
/* 215 */       this.horseLeftFaceMetal.render(scale);
/* 216 */       this.horseRightFaceMetal.render(scale);
/*     */       
/* 218 */       if (flag4) {
/*     */         
/* 220 */         this.horseLeftRein.render(scale);
/* 221 */         this.horseRightRein.render(scale);
/*     */       } 
/*     */     } 
/*     */     
/* 225 */     if (!flag) {
/*     */       
/* 227 */       GlStateManager.pushMatrix();
/* 228 */       GlStateManager.scale(f1, 0.5F + f1 * 0.5F, f1);
/* 229 */       GlStateManager.translate(0.0F, 0.95F * (1.0F - f1), 0.0F);
/*     */     } 
/*     */     
/* 232 */     this.backLeftLeg.render(scale);
/* 233 */     this.backLeftShin.render(scale);
/* 234 */     this.backLeftHoof.render(scale);
/* 235 */     this.backRightLeg.render(scale);
/* 236 */     this.backRightShin.render(scale);
/* 237 */     this.backRightHoof.render(scale);
/* 238 */     this.frontLeftLeg.render(scale);
/* 239 */     this.frontLeftShin.render(scale);
/* 240 */     this.frontLeftHoof.render(scale);
/* 241 */     this.frontRightLeg.render(scale);
/* 242 */     this.frontRightShin.render(scale);
/* 243 */     this.frontRightHoof.render(scale);
/*     */     
/* 245 */     if (!flag) {
/*     */       
/* 247 */       GlStateManager.popMatrix();
/* 248 */       GlStateManager.pushMatrix();
/* 249 */       GlStateManager.scale(f1, f1, f1);
/* 250 */       GlStateManager.translate(0.0F, 1.35F * (1.0F - f1), 0.0F);
/*     */     } 
/*     */     
/* 253 */     this.body.render(scale);
/* 254 */     this.tailBase.render(scale);
/* 255 */     this.tailMiddle.render(scale);
/* 256 */     this.tailTip.render(scale);
/* 257 */     this.neck.render(scale);
/* 258 */     this.mane.render(scale);
/*     */     
/* 260 */     if (!flag) {
/*     */       
/* 262 */       GlStateManager.popMatrix();
/* 263 */       GlStateManager.pushMatrix();
/* 264 */       float f2 = 0.5F + f1 * f1 * 0.5F;
/* 265 */       GlStateManager.scale(f2, f2, f2);
/*     */       
/* 267 */       if (f <= 0.0F) {
/*     */         
/* 269 */         GlStateManager.translate(0.0F, 1.35F * (1.0F - f1), 0.0F);
/*     */       }
/*     */       else {
/*     */         
/* 273 */         GlStateManager.translate(0.0F, 0.9F * (1.0F - f1) * f + 1.35F * (1.0F - f1) * (1.0F - f), 0.15F * (1.0F - f1) * f);
/*     */       } 
/*     */     } 
/*     */     
/* 277 */     if (flag3) {
/*     */       
/* 279 */       this.muleLeftEar.render(scale);
/* 280 */       this.muleRightEar.render(scale);
/*     */     }
/*     */     else {
/*     */       
/* 284 */       this.horseLeftEar.render(scale);
/* 285 */       this.horseRightEar.render(scale);
/*     */     } 
/*     */     
/* 288 */     this.head.render(scale);
/*     */     
/* 290 */     if (!flag)
/*     */     {
/* 292 */       GlStateManager.popMatrix();
/*     */     }
/*     */     
/* 295 */     if (flag2) {
/*     */       
/* 297 */       this.muleLeftChest.render(scale);
/* 298 */       this.muleRightChest.render(scale);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void setBoxRotation(ModelRenderer p_110682_1_, float p_110682_2_, float p_110682_3_, float p_110682_4_) {
/* 304 */     p_110682_1_.rotateAngleX = p_110682_2_;
/* 305 */     p_110682_1_.rotateAngleY = p_110682_3_;
/* 306 */     p_110682_1_.rotateAngleZ = p_110682_4_;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private float updateHorseRotation(float p_110683_1_, float p_110683_2_, float p_110683_3_) {
/*     */     float f;
/* 313 */     for (f = p_110683_2_ - p_110683_1_; f < -180.0F; f += 360.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 318 */     while (f >= 180.0F)
/*     */     {
/* 320 */       f -= 360.0F;
/*     */     }
/*     */     
/* 323 */     return p_110683_1_ + p_110683_3_ * f;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime) {
/* 328 */     super.setLivingAnimations(entitylivingbaseIn, p_78086_2_, p_78086_3_, partialTickTime);
/* 329 */     float f = updateHorseRotation(entitylivingbaseIn.prevRenderYawOffset, entitylivingbaseIn.renderYawOffset, partialTickTime);
/* 330 */     float f1 = updateHorseRotation(entitylivingbaseIn.prevRotationYawHead, entitylivingbaseIn.rotationYawHead, partialTickTime);
/* 331 */     float f2 = entitylivingbaseIn.prevRotationPitch + (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch) * partialTickTime;
/* 332 */     float f3 = f1 - f;
/* 333 */     float f4 = f2 / 57.295776F;
/*     */     
/* 335 */     if (f3 > 20.0F)
/*     */     {
/* 337 */       f3 = 20.0F;
/*     */     }
/*     */     
/* 340 */     if (f3 < -20.0F)
/*     */     {
/* 342 */       f3 = -20.0F;
/*     */     }
/*     */     
/* 345 */     if (p_78086_3_ > 0.2F)
/*     */     {
/* 347 */       f4 += MathHelper.cos(p_78086_2_ * 0.4F) * 0.15F * p_78086_3_;
/*     */     }
/*     */     
/* 350 */     EntityHorse entityhorse = (EntityHorse)entitylivingbaseIn;
/* 351 */     float f5 = entityhorse.getGrassEatingAmount(partialTickTime);
/* 352 */     float f6 = entityhorse.getRearingAmount(partialTickTime);
/* 353 */     float f7 = 1.0F - f6;
/* 354 */     float f8 = entityhorse.getMouthOpennessAngle(partialTickTime);
/* 355 */     boolean flag = (entityhorse.field_110278_bp != 0);
/* 356 */     boolean flag1 = entityhorse.isHorseSaddled();
/* 357 */     boolean flag2 = (entityhorse.riddenByEntity != null);
/* 358 */     float f9 = entitylivingbaseIn.ticksExisted + partialTickTime;
/* 359 */     float f10 = MathHelper.cos(p_78086_2_ * 0.6662F + 3.1415927F);
/* 360 */     float f11 = f10 * 0.8F * p_78086_3_;
/* 361 */     this.head.rotationPointY = 4.0F;
/* 362 */     this.head.rotationPointZ = -10.0F;
/* 363 */     this.tailBase.rotationPointY = 3.0F;
/* 364 */     this.tailMiddle.rotationPointZ = 14.0F;
/* 365 */     this.muleRightChest.rotationPointY = 3.0F;
/* 366 */     this.muleRightChest.rotationPointZ = 10.0F;
/* 367 */     this.body.rotateAngleX = 0.0F;
/* 368 */     this.head.rotateAngleX = 0.5235988F + f4;
/* 369 */     this.head.rotateAngleY = f3 / 57.295776F;
/* 370 */     this.head.rotateAngleX = f6 * (0.2617994F + f4) + f5 * 2.18166F + (1.0F - Math.max(f6, f5)) * this.head.rotateAngleX;
/* 371 */     this.head.rotateAngleY = f6 * f3 / 57.295776F + (1.0F - Math.max(f6, f5)) * this.head.rotateAngleY;
/* 372 */     this.head.rotationPointY = f6 * -6.0F + f5 * 11.0F + (1.0F - Math.max(f6, f5)) * this.head.rotationPointY;
/* 373 */     this.head.rotationPointZ = f6 * -1.0F + f5 * -10.0F + (1.0F - Math.max(f6, f5)) * this.head.rotationPointZ;
/* 374 */     this.tailBase.rotationPointY = f6 * 9.0F + f7 * this.tailBase.rotationPointY;
/* 375 */     this.tailMiddle.rotationPointZ = f6 * 18.0F + f7 * this.tailMiddle.rotationPointZ;
/* 376 */     this.muleRightChest.rotationPointY = f6 * 5.5F + f7 * this.muleRightChest.rotationPointY;
/* 377 */     this.muleRightChest.rotationPointZ = f6 * 15.0F + f7 * this.muleRightChest.rotationPointZ;
/* 378 */     this.body.rotateAngleX = f6 * -45.0F / 57.295776F + f7 * this.body.rotateAngleX;
/* 379 */     this.horseLeftEar.rotationPointY = this.head.rotationPointY;
/* 380 */     this.horseRightEar.rotationPointY = this.head.rotationPointY;
/* 381 */     this.muleLeftEar.rotationPointY = this.head.rotationPointY;
/* 382 */     this.muleRightEar.rotationPointY = this.head.rotationPointY;
/* 383 */     this.neck.rotationPointY = this.head.rotationPointY;
/* 384 */     this.field_178711_b.rotationPointY = 0.02F;
/* 385 */     this.field_178712_c.rotationPointY = 0.0F;
/* 386 */     this.mane.rotationPointY = this.head.rotationPointY;
/* 387 */     this.horseLeftEar.rotationPointZ = this.head.rotationPointZ;
/* 388 */     this.horseRightEar.rotationPointZ = this.head.rotationPointZ;
/* 389 */     this.muleLeftEar.rotationPointZ = this.head.rotationPointZ;
/* 390 */     this.muleRightEar.rotationPointZ = this.head.rotationPointZ;
/* 391 */     this.neck.rotationPointZ = this.head.rotationPointZ;
/* 392 */     this.field_178711_b.rotationPointZ = 0.02F - f8 * 1.0F;
/* 393 */     this.field_178712_c.rotationPointZ = 0.0F + f8 * 1.0F;
/* 394 */     this.mane.rotationPointZ = this.head.rotationPointZ;
/* 395 */     this.horseLeftEar.rotateAngleX = this.head.rotateAngleX;
/* 396 */     this.horseRightEar.rotateAngleX = this.head.rotateAngleX;
/* 397 */     this.muleLeftEar.rotateAngleX = this.head.rotateAngleX;
/* 398 */     this.muleRightEar.rotateAngleX = this.head.rotateAngleX;
/* 399 */     this.neck.rotateAngleX = this.head.rotateAngleX;
/* 400 */     this.field_178711_b.rotateAngleX = 0.0F - 0.09424778F * f8;
/* 401 */     this.field_178712_c.rotateAngleX = 0.0F + 0.15707964F * f8;
/* 402 */     this.mane.rotateAngleX = this.head.rotateAngleX;
/* 403 */     this.horseLeftEar.rotateAngleY = this.head.rotateAngleY;
/* 404 */     this.horseRightEar.rotateAngleY = this.head.rotateAngleY;
/* 405 */     this.muleLeftEar.rotateAngleY = this.head.rotateAngleY;
/* 406 */     this.muleRightEar.rotateAngleY = this.head.rotateAngleY;
/* 407 */     this.neck.rotateAngleY = this.head.rotateAngleY;
/* 408 */     this.field_178711_b.rotateAngleY = 0.0F;
/* 409 */     this.field_178712_c.rotateAngleY = 0.0F;
/* 410 */     this.mane.rotateAngleY = this.head.rotateAngleY;
/* 411 */     this.muleLeftChest.rotateAngleX = f11 / 5.0F;
/* 412 */     this.muleRightChest.rotateAngleX = -f11 / 5.0F;
/* 413 */     float f12 = 1.5707964F;
/* 414 */     float f13 = 4.712389F;
/* 415 */     float f14 = -1.0471976F;
/* 416 */     float f15 = 0.2617994F * f6;
/* 417 */     float f16 = MathHelper.cos(f9 * 0.6F + 3.1415927F);
/* 418 */     this.frontLeftLeg.rotationPointY = -2.0F * f6 + 9.0F * f7;
/* 419 */     this.frontLeftLeg.rotationPointZ = -2.0F * f6 + -8.0F * f7;
/* 420 */     this.frontRightLeg.rotationPointY = this.frontLeftLeg.rotationPointY;
/* 421 */     this.frontRightLeg.rotationPointZ = this.frontLeftLeg.rotationPointZ;
/* 422 */     this.backLeftLeg.rotationPointY += MathHelper.sin(1.5707964F + f15 + f7 * -f10 * 0.5F * p_78086_3_) * 7.0F;
/* 423 */     this.backLeftLeg.rotationPointZ += MathHelper.cos(4.712389F + f15 + f7 * -f10 * 0.5F * p_78086_3_) * 7.0F;
/* 424 */     this.backRightLeg.rotationPointY += MathHelper.sin(1.5707964F + f15 + f7 * f10 * 0.5F * p_78086_3_) * 7.0F;
/* 425 */     this.backRightLeg.rotationPointZ += MathHelper.cos(4.712389F + f15 + f7 * f10 * 0.5F * p_78086_3_) * 7.0F;
/* 426 */     float f17 = (-1.0471976F + f16) * f6 + f11 * f7;
/* 427 */     float f18 = (-1.0471976F + -f16) * f6 + -f11 * f7;
/* 428 */     this.frontLeftLeg.rotationPointY += MathHelper.sin(1.5707964F + f17) * 7.0F;
/* 429 */     this.frontLeftLeg.rotationPointZ += MathHelper.cos(4.712389F + f17) * 7.0F;
/* 430 */     this.frontRightLeg.rotationPointY += MathHelper.sin(1.5707964F + f18) * 7.0F;
/* 431 */     this.frontRightLeg.rotationPointZ += MathHelper.cos(4.712389F + f18) * 7.0F;
/* 432 */     this.backLeftLeg.rotateAngleX = f15 + -f10 * 0.5F * p_78086_3_ * f7;
/* 433 */     this.backLeftShin.rotateAngleX = -0.08726646F * f6 + (-f10 * 0.5F * p_78086_3_ - Math.max(0.0F, f10 * 0.5F * p_78086_3_)) * f7;
/* 434 */     this.backLeftHoof.rotateAngleX = this.backLeftShin.rotateAngleX;
/* 435 */     this.backRightLeg.rotateAngleX = f15 + f10 * 0.5F * p_78086_3_ * f7;
/* 436 */     this.backRightShin.rotateAngleX = -0.08726646F * f6 + (f10 * 0.5F * p_78086_3_ - Math.max(0.0F, -f10 * 0.5F * p_78086_3_)) * f7;
/* 437 */     this.backRightHoof.rotateAngleX = this.backRightShin.rotateAngleX;
/* 438 */     this.frontLeftLeg.rotateAngleX = f17;
/* 439 */     this.frontLeftShin.rotateAngleX = (this.frontLeftLeg.rotateAngleX + 3.1415927F * Math.max(0.0F, 0.2F + f16 * 0.2F)) * f6 + (f11 + Math.max(0.0F, f10 * 0.5F * p_78086_3_)) * f7;
/* 440 */     this.frontLeftHoof.rotateAngleX = this.frontLeftShin.rotateAngleX;
/* 441 */     this.frontRightLeg.rotateAngleX = f18;
/* 442 */     this.frontRightShin.rotateAngleX = (this.frontRightLeg.rotateAngleX + 3.1415927F * Math.max(0.0F, 0.2F - f16 * 0.2F)) * f6 + (-f11 + Math.max(0.0F, -f10 * 0.5F * p_78086_3_)) * f7;
/* 443 */     this.frontRightHoof.rotateAngleX = this.frontRightShin.rotateAngleX;
/* 444 */     this.backLeftHoof.rotationPointY = this.backLeftShin.rotationPointY;
/* 445 */     this.backLeftHoof.rotationPointZ = this.backLeftShin.rotationPointZ;
/* 446 */     this.backRightHoof.rotationPointY = this.backRightShin.rotationPointY;
/* 447 */     this.backRightHoof.rotationPointZ = this.backRightShin.rotationPointZ;
/* 448 */     this.frontLeftHoof.rotationPointY = this.frontLeftShin.rotationPointY;
/* 449 */     this.frontLeftHoof.rotationPointZ = this.frontLeftShin.rotationPointZ;
/* 450 */     this.frontRightHoof.rotationPointY = this.frontRightShin.rotationPointY;
/* 451 */     this.frontRightHoof.rotationPointZ = this.frontRightShin.rotationPointZ;
/*     */     
/* 453 */     if (flag1) {
/*     */       
/* 455 */       this.horseSaddleBottom.rotationPointY = f6 * 0.5F + f7 * 2.0F;
/* 456 */       this.horseSaddleBottom.rotationPointZ = f6 * 11.0F + f7 * 2.0F;
/* 457 */       this.horseSaddleFront.rotationPointY = this.horseSaddleBottom.rotationPointY;
/* 458 */       this.horseSaddleBack.rotationPointY = this.horseSaddleBottom.rotationPointY;
/* 459 */       this.horseLeftSaddleRope.rotationPointY = this.horseSaddleBottom.rotationPointY;
/* 460 */       this.horseRightSaddleRope.rotationPointY = this.horseSaddleBottom.rotationPointY;
/* 461 */       this.horseLeftSaddleMetal.rotationPointY = this.horseSaddleBottom.rotationPointY;
/* 462 */       this.horseRightSaddleMetal.rotationPointY = this.horseSaddleBottom.rotationPointY;
/* 463 */       this.muleLeftChest.rotationPointY = this.muleRightChest.rotationPointY;
/* 464 */       this.horseSaddleFront.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
/* 465 */       this.horseSaddleBack.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
/* 466 */       this.horseLeftSaddleRope.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
/* 467 */       this.horseRightSaddleRope.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
/* 468 */       this.horseLeftSaddleMetal.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
/* 469 */       this.horseRightSaddleMetal.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
/* 470 */       this.muleLeftChest.rotationPointZ = this.muleRightChest.rotationPointZ;
/* 471 */       this.horseSaddleBottom.rotateAngleX = this.body.rotateAngleX;
/* 472 */       this.horseSaddleFront.rotateAngleX = this.body.rotateAngleX;
/* 473 */       this.horseSaddleBack.rotateAngleX = this.body.rotateAngleX;
/* 474 */       this.horseLeftRein.rotationPointY = this.head.rotationPointY;
/* 475 */       this.horseRightRein.rotationPointY = this.head.rotationPointY;
/* 476 */       this.horseFaceRopes.rotationPointY = this.head.rotationPointY;
/* 477 */       this.horseLeftFaceMetal.rotationPointY = this.head.rotationPointY;
/* 478 */       this.horseRightFaceMetal.rotationPointY = this.head.rotationPointY;
/* 479 */       this.horseLeftRein.rotationPointZ = this.head.rotationPointZ;
/* 480 */       this.horseRightRein.rotationPointZ = this.head.rotationPointZ;
/* 481 */       this.horseFaceRopes.rotationPointZ = this.head.rotationPointZ;
/* 482 */       this.horseLeftFaceMetal.rotationPointZ = this.head.rotationPointZ;
/* 483 */       this.horseRightFaceMetal.rotationPointZ = this.head.rotationPointZ;
/* 484 */       this.horseLeftRein.rotateAngleX = f4;
/* 485 */       this.horseRightRein.rotateAngleX = f4;
/* 486 */       this.horseFaceRopes.rotateAngleX = this.head.rotateAngleX;
/* 487 */       this.horseLeftFaceMetal.rotateAngleX = this.head.rotateAngleX;
/* 488 */       this.horseRightFaceMetal.rotateAngleX = this.head.rotateAngleX;
/* 489 */       this.horseFaceRopes.rotateAngleY = this.head.rotateAngleY;
/* 490 */       this.horseLeftFaceMetal.rotateAngleY = this.head.rotateAngleY;
/* 491 */       this.horseLeftRein.rotateAngleY = this.head.rotateAngleY;
/* 492 */       this.horseRightFaceMetal.rotateAngleY = this.head.rotateAngleY;
/* 493 */       this.horseRightRein.rotateAngleY = this.head.rotateAngleY;
/*     */       
/* 495 */       if (flag2) {
/*     */         
/* 497 */         this.horseLeftSaddleRope.rotateAngleX = -1.0471976F;
/* 498 */         this.horseLeftSaddleMetal.rotateAngleX = -1.0471976F;
/* 499 */         this.horseRightSaddleRope.rotateAngleX = -1.0471976F;
/* 500 */         this.horseRightSaddleMetal.rotateAngleX = -1.0471976F;
/* 501 */         this.horseLeftSaddleRope.rotateAngleZ = 0.0F;
/* 502 */         this.horseLeftSaddleMetal.rotateAngleZ = 0.0F;
/* 503 */         this.horseRightSaddleRope.rotateAngleZ = 0.0F;
/* 504 */         this.horseRightSaddleMetal.rotateAngleZ = 0.0F;
/*     */       }
/*     */       else {
/*     */         
/* 508 */         this.horseLeftSaddleRope.rotateAngleX = f11 / 3.0F;
/* 509 */         this.horseLeftSaddleMetal.rotateAngleX = f11 / 3.0F;
/* 510 */         this.horseRightSaddleRope.rotateAngleX = f11 / 3.0F;
/* 511 */         this.horseRightSaddleMetal.rotateAngleX = f11 / 3.0F;
/* 512 */         this.horseLeftSaddleRope.rotateAngleZ = f11 / 5.0F;
/* 513 */         this.horseLeftSaddleMetal.rotateAngleZ = f11 / 5.0F;
/* 514 */         this.horseRightSaddleRope.rotateAngleZ = -f11 / 5.0F;
/* 515 */         this.horseRightSaddleMetal.rotateAngleZ = -f11 / 5.0F;
/*     */       } 
/*     */     } 
/*     */     
/* 519 */     f12 = -1.3089F + p_78086_3_ * 1.5F;
/*     */     
/* 521 */     if (f12 > 0.0F)
/*     */     {
/* 523 */       f12 = 0.0F;
/*     */     }
/*     */     
/* 526 */     if (flag) {
/*     */       
/* 528 */       this.tailBase.rotateAngleY = MathHelper.cos(f9 * 0.7F);
/* 529 */       f12 = 0.0F;
/*     */     }
/*     */     else {
/*     */       
/* 533 */       this.tailBase.rotateAngleY = 0.0F;
/*     */     } 
/*     */     
/* 536 */     this.tailMiddle.rotateAngleY = this.tailBase.rotateAngleY;
/* 537 */     this.tailTip.rotateAngleY = this.tailBase.rotateAngleY;
/* 538 */     this.tailMiddle.rotationPointY = this.tailBase.rotationPointY;
/* 539 */     this.tailTip.rotationPointY = this.tailBase.rotationPointY;
/* 540 */     this.tailMiddle.rotationPointZ = this.tailBase.rotationPointZ;
/* 541 */     this.tailTip.rotationPointZ = this.tailBase.rotationPointZ;
/* 542 */     this.tailBase.rotateAngleX = f12;
/* 543 */     this.tailMiddle.rotateAngleX = f12;
/* 544 */     this.tailTip.rotateAngleX = -0.2618F + f12;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\ModelHorse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */