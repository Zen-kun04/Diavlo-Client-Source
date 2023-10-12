/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.audio.MusicTicker;
/*     */ import net.minecraft.client.audio.SoundHandler;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.C16PacketClientStatus;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.commons.io.Charsets;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class GuiWinGame extends GuiScreen {
/*  25 */   private static final Logger logger = LogManager.getLogger();
/*  26 */   private static final ResourceLocation MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");
/*  27 */   private static final ResourceLocation VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");
/*     */   private int field_146581_h;
/*     */   private List<String> field_146582_i;
/*     */   private int field_146579_r;
/*  31 */   private float field_146578_s = 0.5F;
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/*  35 */     MusicTicker musicticker = this.mc.getMusicTicker();
/*  36 */     SoundHandler soundhandler = this.mc.getSoundHandler();
/*     */     
/*  38 */     if (this.field_146581_h == 0) {
/*     */       
/*  40 */       musicticker.func_181557_a();
/*  41 */       musicticker.func_181558_a(MusicTicker.MusicType.CREDITS);
/*  42 */       soundhandler.resumeSounds();
/*     */     } 
/*     */     
/*  45 */     soundhandler.update();
/*  46 */     this.field_146581_h++;
/*  47 */     float f = (this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;
/*     */     
/*  49 */     if (this.field_146581_h > f)
/*     */     {
/*  51 */       sendRespawnPacket();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/*  57 */     if (keyCode == 1)
/*     */     {
/*  59 */       sendRespawnPacket();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void sendRespawnPacket() {
/*  65 */     this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
/*  66 */     this.mc.displayGuiScreen((GuiScreen)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doesGuiPauseGame() {
/*  71 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  76 */     if (this.field_146582_i == null) {
/*     */       
/*  78 */       this.field_146582_i = Lists.newArrayList();
/*     */ 
/*     */       
/*     */       try {
/*  82 */         String s = "";
/*  83 */         String s1 = "" + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + EnumChatFormatting.GREEN + EnumChatFormatting.AQUA;
/*  84 */         int i = 274;
/*  85 */         InputStream inputstream = this.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt")).getInputStream();
/*  86 */         BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));
/*  87 */         Random random = new Random(8124371L);
/*     */         
/*  89 */         while ((s = bufferedreader.readLine()) != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  94 */           for (s = s.replaceAll("PLAYERNAME", this.mc.getSession().getUsername()); s.contains(s1); s = s2 + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + s3) {
/*     */             
/*  96 */             int j = s.indexOf(s1);
/*  97 */             String s2 = s.substring(0, j);
/*  98 */             String s3 = s.substring(j + s1.length());
/*     */           } 
/*     */           
/* 101 */           this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s, i));
/* 102 */           this.field_146582_i.add("");
/*     */         } 
/*     */         
/* 105 */         inputstream.close();
/*     */         
/* 107 */         for (int k = 0; k < 8; k++)
/*     */         {
/* 109 */           this.field_146582_i.add("");
/*     */         }
/*     */         
/* 112 */         inputstream = this.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream();
/* 113 */         bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));
/*     */         
/* 115 */         while ((s = bufferedreader.readLine()) != null) {
/*     */           
/* 117 */           s = s.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
/* 118 */           s = s.replaceAll("\t", "    ");
/* 119 */           this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s, i));
/* 120 */           this.field_146582_i.add("");
/*     */         } 
/*     */         
/* 123 */         inputstream.close();
/* 124 */         this.field_146579_r = this.field_146582_i.size() * 12;
/*     */       }
/* 126 */       catch (Exception exception) {
/*     */         
/* 128 */         logger.error("Couldn't load credits", exception);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawWinGameScreen(int p_146575_1_, int p_146575_2_, float p_146575_3_) {
/* 135 */     Tessellator tessellator = Tessellator.getInstance();
/* 136 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 137 */     this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
/* 138 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 139 */     int i = this.width;
/* 140 */     float f = 0.0F - (this.field_146581_h + p_146575_3_) * 0.5F * this.field_146578_s;
/* 141 */     float f1 = this.height - (this.field_146581_h + p_146575_3_) * 0.5F * this.field_146578_s;
/* 142 */     float f2 = 0.015625F;
/* 143 */     float f3 = (this.field_146581_h + p_146575_3_ - 0.0F) * 0.02F;
/* 144 */     float f4 = (this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;
/* 145 */     float f5 = (f4 - 20.0F - this.field_146581_h + p_146575_3_) * 0.005F;
/*     */     
/* 147 */     if (f5 < f3)
/*     */     {
/* 149 */       f3 = f5;
/*     */     }
/*     */     
/* 152 */     if (f3 > 1.0F)
/*     */     {
/* 154 */       f3 = 1.0F;
/*     */     }
/*     */     
/* 157 */     f3 *= f3;
/* 158 */     f3 = f3 * 96.0F / 255.0F;
/* 159 */     worldrenderer.pos(0.0D, this.height, this.zLevel).tex(0.0D, (f * f2)).color(f3, f3, f3, 1.0F).endVertex();
/* 160 */     worldrenderer.pos(i, this.height, this.zLevel).tex((i * f2), (f * f2)).color(f3, f3, f3, 1.0F).endVertex();
/* 161 */     worldrenderer.pos(i, 0.0D, this.zLevel).tex((i * f2), (f1 * f2)).color(f3, f3, f3, 1.0F).endVertex();
/* 162 */     worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(0.0D, (f1 * f2)).color(f3, f3, f3, 1.0F).endVertex();
/* 163 */     tessellator.draw();
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 168 */     drawWinGameScreen(mouseX, mouseY, partialTicks);
/* 169 */     Tessellator tessellator = Tessellator.getInstance();
/* 170 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 171 */     int i = 274;
/* 172 */     int j = this.width / 2 - i / 2;
/* 173 */     int k = this.height + 50;
/* 174 */     float f = -(this.field_146581_h + partialTicks) * this.field_146578_s;
/* 175 */     GlStateManager.pushMatrix();
/* 176 */     GlStateManager.translate(0.0F, f, 0.0F);
/* 177 */     this.mc.getTextureManager().bindTexture(MINECRAFT_LOGO);
/* 178 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 179 */     drawTexturedModalRect(j, k, 0, 0, 155, 44);
/* 180 */     drawTexturedModalRect(j + 155, k, 0, 45, 155, 44);
/* 181 */     int l = k + 200;
/*     */     
/* 183 */     for (int i1 = 0; i1 < this.field_146582_i.size(); i1++) {
/*     */       
/* 185 */       if (i1 == this.field_146582_i.size() - 1) {
/*     */         
/* 187 */         float f1 = l + f - (this.height / 2 - 6);
/*     */         
/* 189 */         if (f1 < 0.0F)
/*     */         {
/* 191 */           GlStateManager.translate(0.0F, -f1, 0.0F);
/*     */         }
/*     */       } 
/*     */       
/* 195 */       if (l + f + 12.0F + 8.0F > 0.0F && l + f < this.height) {
/*     */         
/* 197 */         String s = this.field_146582_i.get(i1);
/*     */         
/* 199 */         if (s.startsWith("[C]")) {
/*     */           
/* 201 */           this.fontRendererObj.drawStringWithShadow(s.substring(3), (j + (i - this.fontRendererObj.getStringWidth(s.substring(3))) / 2), l, 16777215);
/*     */         }
/*     */         else {
/*     */           
/* 205 */           this.fontRendererObj.fontRandom.setSeed(i1 * 4238972211L + (this.field_146581_h / 4));
/* 206 */           this.fontRendererObj.drawStringWithShadow(s, j, l, 16777215);
/*     */         } 
/*     */       } 
/*     */       
/* 210 */       l += 12;
/*     */     } 
/*     */     
/* 213 */     GlStateManager.popMatrix();
/* 214 */     this.mc.getTextureManager().bindTexture(VIGNETTE_TEXTURE);
/* 215 */     GlStateManager.enableBlend();
/* 216 */     GlStateManager.blendFunc(0, 769);
/* 217 */     int j1 = this.width;
/* 218 */     int k1 = this.height;
/* 219 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 220 */     worldrenderer.pos(0.0D, k1, this.zLevel).tex(0.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
/* 221 */     worldrenderer.pos(j1, k1, this.zLevel).tex(1.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
/* 222 */     worldrenderer.pos(j1, 0.0D, this.zLevel).tex(1.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
/* 223 */     worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(0.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
/* 224 */     tessellator.draw();
/* 225 */     GlStateManager.disableBlend();
/* 226 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiWinGame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */