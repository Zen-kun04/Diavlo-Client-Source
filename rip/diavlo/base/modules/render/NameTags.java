/*     */ package rip.diavlo.base.modules.render;
/*     */ 
/*     */ import com.google.common.eventbus.Subscribe;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import rip.diavlo.base.api.module.Category;
/*     */ import rip.diavlo.base.api.module.Module;
/*     */ import rip.diavlo.base.api.value.impl.ModeValue;
/*     */ import rip.diavlo.base.events.render.RenderGuiEvent;
/*     */ 
/*     */ 
/*     */ public class NameTags
/*     */   extends Module
/*     */ {
/*     */   ModeValue scale;
/*     */   
/*     */   public NameTags() {
/*  23 */     super("NameTags", 0, Category.RENDER);
/*     */ 
/*     */     
/*  26 */     this.scale = new ModeValue("Escala", this, new String[] { "500.0D", "10.0D", "200.0D", "1" });
/*     */   }
/*     */   
/*     */   @Subscribe
/*     */   public void onRender(RenderGuiEvent event) {
/*  31 */     for (Entity entity : mc.theWorld.loadedEntityList) {
/*  32 */       if (entity instanceof net.minecraft.entity.player.EntityPlayer) {
/*  33 */         System.out.println(20);
/*  34 */         render((EntityLivingBase)entity, 20.0F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  41 */     super.onEnable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  46 */     super.onDisable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(EntityLivingBase entity, float partialTicks) {
/*  55 */     String string = entity.getDisplayName().getFormattedText() + " " + ((entity.getHealth() > entity.getMaxHealth() / 3.0F) ? "§6" : ((entity.getHealth() > entity.getMaxHealth() / 3.0F * 2.0F) ? "§a" : "§4")) + ((int)(entity.getHealth() * 2.0F) / 2.0F) + "h";
/*  56 */     int i = mc.fontRendererObj.getStringWidth(string);
/*  57 */     GlStateManager.pushMatrix();
/*     */     
/*  59 */     rotate(entity, partialTicks);
/*  60 */     renderHealth(entity, i);
/*     */     
/*  62 */     mc.fontRendererObj.drawString(string, (-i / 2), 0.0D, -1);
/*     */     
/*  64 */     GlStateManager.enableDepth();
/*  65 */     GlStateManager.disableBlend();
/*  66 */     GlStateManager.popMatrix();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void rotate(EntityLivingBase entity, float partialTicks) {
/*  72 */     double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - (mc.getRenderManager()).renderPosX;
/*     */     
/*  74 */     double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - (mc.getRenderManager()).renderPosY + entity.height + 0.2D + Double.parseDouble((String)this.scale.get()) / 200.0D;
/*     */     
/*  76 */     double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - (mc.getRenderManager()).renderPosZ;
/*  77 */     GlStateManager.translate(x, y, z);
/*  78 */     GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/*  79 */     GlStateManager.rotate(-(mc.getRenderManager()).playerViewY, 0.0F, 1.0F, 0.0F);
/*  80 */     GlStateManager.rotate((mc.getRenderManager()).playerViewX, 1.0F, 0.0F, 0.0F);
/*  81 */     GlStateManager.scale(-(Double.parseDouble((String)this.scale.get()) / 2000.0D), -Double.parseDouble((String)this.scale.get()) / 2000.0D, Double.parseDouble((String)this.scale.get()) / 2000.0D);
/*  82 */     GlStateManager.disableDepth();
/*     */     
/*  84 */     GlStateManager.enableBlend();
/*  85 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderHealth(EntityLivingBase entity, int i) {
/*  90 */     GlStateManager.disableTexture2D();
/*  91 */     Tessellator tessellator = Tessellator.getInstance();
/*  92 */     WorldRenderer vertexbuffer = tessellator.getWorldRenderer();
/*  93 */     vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/*  94 */     vertexbuffer.pos((-i / 2), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/*  95 */     vertexbuffer.pos((-i / 2), 10.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/*  96 */     vertexbuffer.pos((i / 2), 10.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/*  97 */     vertexbuffer.pos((i / 2), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/*  98 */     tessellator.draw();
/*     */     
/* 100 */     double health = (entity.getHealth() / entity.getMaxHealth());
/* 101 */     vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 102 */     vertexbuffer.pos((-i / 2), 8.0D, 0.0D).color(1.0F, 0.0F, 0.0F, 0.75F).endVertex();
/* 103 */     vertexbuffer.pos((-i / 2), 10.0D, 0.0D).color(1.0F, 0.0F, 0.0F, 0.75F).endVertex();
/* 104 */     vertexbuffer.pos(i * health - (i / 2), 10.0D, 0.0D).color(1.0F, 0.0F, 0.0F, 0.75F).endVertex();
/* 105 */     vertexbuffer.pos(i * health - (i / 2), 8.0D, 0.0D).color(1.0F, 0.0F, 0.0F, 0.75F).endVertex();
/* 106 */     tessellator.draw();
/* 107 */     GlStateManager.enableTexture2D();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\render\NameTags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */