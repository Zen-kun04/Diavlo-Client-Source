/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ 
/*     */ public class S45PacketTitle
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private Type type;
/*     */   private IChatComponent message;
/*     */   private int fadeInTime;
/*     */   private int displayTime;
/*     */   private int fadeOutTime;
/*     */   
/*     */   public S45PacketTitle() {}
/*     */   
/*     */   public S45PacketTitle(Type type, IChatComponent message) {
/*  23 */     this(type, message, -1, -1, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public S45PacketTitle(int fadeInTime, int displayTime, int fadeOutTime) {
/*  28 */     this(Type.TIMES, (IChatComponent)null, fadeInTime, displayTime, fadeOutTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public S45PacketTitle(Type type, IChatComponent message, int fadeInTime, int displayTime, int fadeOutTime) {
/*  33 */     this.type = type;
/*  34 */     this.message = message;
/*  35 */     this.fadeInTime = fadeInTime;
/*  36 */     this.displayTime = displayTime;
/*  37 */     this.fadeOutTime = fadeOutTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readPacketData(PacketBuffer buf) throws IOException {
/*  42 */     this.type = (Type)buf.readEnumValue(Type.class);
/*     */     
/*  44 */     if (this.type == Type.TITLE || this.type == Type.SUBTITLE)
/*     */     {
/*  46 */       this.message = buf.readChatComponent();
/*     */     }
/*     */     
/*  49 */     if (this.type == Type.TIMES) {
/*     */       
/*  51 */       this.fadeInTime = buf.readInt();
/*  52 */       this.displayTime = buf.readInt();
/*  53 */       this.fadeOutTime = buf.readInt();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writePacketData(PacketBuffer buf) throws IOException {
/*  59 */     buf.writeEnumValue(this.type);
/*     */     
/*  61 */     if (this.type == Type.TITLE || this.type == Type.SUBTITLE)
/*     */     {
/*  63 */       buf.writeChatComponent(this.message);
/*     */     }
/*     */     
/*  66 */     if (this.type == Type.TIMES) {
/*     */       
/*  68 */       buf.writeInt(this.fadeInTime);
/*  69 */       buf.writeInt(this.displayTime);
/*  70 */       buf.writeInt(this.fadeOutTime);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void processPacket(INetHandlerPlayClient handler) {
/*  76 */     handler.handleTitle(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Type getType() {
/*  81 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public IChatComponent getMessage() {
/*  86 */     return this.message;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFadeInTime() {
/*  91 */     return this.fadeInTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDisplayTime() {
/*  96 */     return this.displayTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFadeOutTime() {
/* 101 */     return this.fadeOutTime;
/*     */   }
/*     */   
/*     */   public enum Type
/*     */   {
/* 106 */     TITLE,
/* 107 */     SUBTITLE,
/* 108 */     TIMES,
/* 109 */     CLEAR,
/* 110 */     RESET;
/*     */ 
/*     */     
/*     */     public static Type byName(String name) {
/* 114 */       for (Type s45packettitle$type : values()) {
/*     */         
/* 116 */         if (s45packettitle$type.name().equalsIgnoreCase(name))
/*     */         {
/* 118 */           return s45packettitle$type;
/*     */         }
/*     */       } 
/*     */       
/* 122 */       return TITLE;
/*     */     }
/*     */ 
/*     */     
/*     */     public static String[] getNames() {
/* 127 */       String[] astring = new String[(values()).length];
/* 128 */       int i = 0;
/*     */       
/* 130 */       for (Type s45packettitle$type : values())
/*     */       {
/* 132 */         astring[i++] = s45packettitle$type.name().toLowerCase();
/*     */       }
/*     */       
/* 135 */       return astring;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S45PacketTitle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */