/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import net.minecraft.src.Config;
/*    */ import net.minecraft.util.Vec3;
/*    */ import net.optifine.shaders.SVertexFormat;
/*    */ 
/*    */ 
/*    */ public class TexturedQuad
/*    */ {
/*    */   public PositionTextureVertex[] vertexPositions;
/*    */   public int nVertices;
/*    */   private boolean invertNormal;
/*    */   
/*    */   public TexturedQuad(PositionTextureVertex[] vertices) {
/* 18 */     this.vertexPositions = vertices;
/* 19 */     this.nVertices = vertices.length;
/*    */   }
/*    */ 
/*    */   
/*    */   public TexturedQuad(PositionTextureVertex[] vertices, int texcoordU1, int texcoordV1, int texcoordU2, int texcoordV2, float textureWidth, float textureHeight) {
/* 24 */     this(vertices);
/* 25 */     float f = 0.0F / textureWidth;
/* 26 */     float f1 = 0.0F / textureHeight;
/* 27 */     vertices[0] = vertices[0].setTexturePosition(texcoordU2 / textureWidth - f, texcoordV1 / textureHeight + f1);
/* 28 */     vertices[1] = vertices[1].setTexturePosition(texcoordU1 / textureWidth + f, texcoordV1 / textureHeight + f1);
/* 29 */     vertices[2] = vertices[2].setTexturePosition(texcoordU1 / textureWidth + f, texcoordV2 / textureHeight - f1);
/* 30 */     vertices[3] = vertices[3].setTexturePosition(texcoordU2 / textureWidth - f, texcoordV2 / textureHeight - f1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void flipFace() {
/* 35 */     PositionTextureVertex[] apositiontexturevertex = new PositionTextureVertex[this.vertexPositions.length];
/*    */     
/* 37 */     for (int i = 0; i < this.vertexPositions.length; i++)
/*    */     {
/* 39 */       apositiontexturevertex[i] = this.vertexPositions[this.vertexPositions.length - i - 1];
/*    */     }
/*    */     
/* 42 */     this.vertexPositions = apositiontexturevertex;
/*    */   }
/*    */ 
/*    */   
/*    */   public void draw(WorldRenderer renderer, float scale) {
/* 47 */     Vec3 vec3 = (this.vertexPositions[1]).vector3D.subtractReverse((this.vertexPositions[0]).vector3D);
/* 48 */     Vec3 vec31 = (this.vertexPositions[1]).vector3D.subtractReverse((this.vertexPositions[2]).vector3D);
/* 49 */     Vec3 vec32 = vec31.crossProduct(vec3).normalize();
/* 50 */     float f = (float)vec32.xCoord;
/* 51 */     float f1 = (float)vec32.yCoord;
/* 52 */     float f2 = (float)vec32.zCoord;
/*    */     
/* 54 */     if (this.invertNormal) {
/*    */       
/* 56 */       f = -f;
/* 57 */       f1 = -f1;
/* 58 */       f2 = -f2;
/*    */     } 
/*    */     
/* 61 */     if (Config.isShaders()) {
/*    */       
/* 63 */       renderer.begin(7, SVertexFormat.defVertexFormatTextured);
/*    */     }
/*    */     else {
/*    */       
/* 67 */       renderer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
/*    */     } 
/*    */     
/* 70 */     for (int i = 0; i < 4; i++) {
/*    */       
/* 72 */       PositionTextureVertex positiontexturevertex = this.vertexPositions[i];
/* 73 */       renderer.pos(positiontexturevertex.vector3D.xCoord * scale, positiontexturevertex.vector3D.yCoord * scale, positiontexturevertex.vector3D.zCoord * scale).tex(positiontexturevertex.texturePositionX, positiontexturevertex.texturePositionY).normal(f, f1, f2).endVertex();
/*    */     } 
/*    */     
/* 76 */     Tessellator.getInstance().draw();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\model\TexturedQuad.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */