/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.INetHandler;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.world.border.WorldBorder;
/*     */ 
/*     */ 
/*     */ public class S44PacketWorldBorder
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private Action action;
/*     */   private int size;
/*     */   private double centerX;
/*     */   private double centerZ;
/*     */   private double targetSize;
/*     */   private double diameter;
/*     */   private long timeUntilTarget;
/*     */   private int warningTime;
/*     */   private int warningDistance;
/*     */   
/*     */   public S44PacketWorldBorder() {}
/*     */   
/*     */   public S44PacketWorldBorder(WorldBorder border, Action actionIn) {
/*  27 */     this.action = actionIn;
/*  28 */     this.centerX = border.getCenterX();
/*  29 */     this.centerZ = border.getCenterZ();
/*  30 */     this.diameter = border.getDiameter();
/*  31 */     this.targetSize = border.getTargetSize();
/*  32 */     this.timeUntilTarget = border.getTimeUntilTarget();
/*  33 */     this.size = border.getSize();
/*  34 */     this.warningDistance = border.getWarningDistance();
/*  35 */     this.warningTime = border.getWarningTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public void readPacketData(PacketBuffer buf) throws IOException {
/*  40 */     this.action = (Action)buf.readEnumValue(Action.class);
/*     */     
/*  42 */     switch (this.action) {
/*     */       
/*     */       case SET_SIZE:
/*  45 */         this.targetSize = buf.readDouble();
/*     */         break;
/*     */       
/*     */       case LERP_SIZE:
/*  49 */         this.diameter = buf.readDouble();
/*  50 */         this.targetSize = buf.readDouble();
/*  51 */         this.timeUntilTarget = buf.readVarLong();
/*     */         break;
/*     */       
/*     */       case SET_CENTER:
/*  55 */         this.centerX = buf.readDouble();
/*  56 */         this.centerZ = buf.readDouble();
/*     */         break;
/*     */       
/*     */       case SET_WARNING_BLOCKS:
/*  60 */         this.warningDistance = buf.readVarIntFromBuffer();
/*     */         break;
/*     */       
/*     */       case SET_WARNING_TIME:
/*  64 */         this.warningTime = buf.readVarIntFromBuffer();
/*     */         break;
/*     */       
/*     */       case INITIALIZE:
/*  68 */         this.centerX = buf.readDouble();
/*  69 */         this.centerZ = buf.readDouble();
/*  70 */         this.diameter = buf.readDouble();
/*  71 */         this.targetSize = buf.readDouble();
/*  72 */         this.timeUntilTarget = buf.readVarLong();
/*  73 */         this.size = buf.readVarIntFromBuffer();
/*  74 */         this.warningDistance = buf.readVarIntFromBuffer();
/*  75 */         this.warningTime = buf.readVarIntFromBuffer();
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void writePacketData(PacketBuffer buf) throws IOException {
/*  81 */     buf.writeEnumValue(this.action);
/*     */     
/*  83 */     switch (this.action) {
/*     */       
/*     */       case SET_SIZE:
/*  86 */         buf.writeDouble(this.targetSize);
/*     */         break;
/*     */       
/*     */       case LERP_SIZE:
/*  90 */         buf.writeDouble(this.diameter);
/*  91 */         buf.writeDouble(this.targetSize);
/*  92 */         buf.writeVarLong(this.timeUntilTarget);
/*     */         break;
/*     */       
/*     */       case SET_CENTER:
/*  96 */         buf.writeDouble(this.centerX);
/*  97 */         buf.writeDouble(this.centerZ);
/*     */         break;
/*     */       
/*     */       case SET_WARNING_BLOCKS:
/* 101 */         buf.writeVarIntToBuffer(this.warningDistance);
/*     */         break;
/*     */       
/*     */       case SET_WARNING_TIME:
/* 105 */         buf.writeVarIntToBuffer(this.warningTime);
/*     */         break;
/*     */       
/*     */       case INITIALIZE:
/* 109 */         buf.writeDouble(this.centerX);
/* 110 */         buf.writeDouble(this.centerZ);
/* 111 */         buf.writeDouble(this.diameter);
/* 112 */         buf.writeDouble(this.targetSize);
/* 113 */         buf.writeVarLong(this.timeUntilTarget);
/* 114 */         buf.writeVarIntToBuffer(this.size);
/* 115 */         buf.writeVarIntToBuffer(this.warningDistance);
/* 116 */         buf.writeVarIntToBuffer(this.warningTime);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void processPacket(INetHandlerPlayClient handler) {
/* 122 */     handler.handleWorldBorder(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_179788_a(WorldBorder border) {
/* 127 */     switch (this.action) {
/*     */       
/*     */       case SET_SIZE:
/* 130 */         border.setTransition(this.targetSize);
/*     */         break;
/*     */       
/*     */       case LERP_SIZE:
/* 134 */         border.setTransition(this.diameter, this.targetSize, this.timeUntilTarget);
/*     */         break;
/*     */       
/*     */       case SET_CENTER:
/* 138 */         border.setCenter(this.centerX, this.centerZ);
/*     */         break;
/*     */       
/*     */       case SET_WARNING_BLOCKS:
/* 142 */         border.setWarningDistance(this.warningDistance);
/*     */         break;
/*     */       
/*     */       case SET_WARNING_TIME:
/* 146 */         border.setWarningTime(this.warningTime);
/*     */         break;
/*     */       
/*     */       case INITIALIZE:
/* 150 */         border.setCenter(this.centerX, this.centerZ);
/*     */         
/* 152 */         if (this.timeUntilTarget > 0L) {
/*     */           
/* 154 */           border.setTransition(this.diameter, this.targetSize, this.timeUntilTarget);
/*     */         }
/*     */         else {
/*     */           
/* 158 */           border.setTransition(this.targetSize);
/*     */         } 
/*     */         
/* 161 */         border.setSize(this.size);
/* 162 */         border.setWarningDistance(this.warningDistance);
/* 163 */         border.setWarningTime(this.warningTime);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public enum Action {
/* 169 */     SET_SIZE,
/* 170 */     LERP_SIZE,
/* 171 */     SET_CENTER,
/* 172 */     INITIALIZE,
/* 173 */     SET_WARNING_TIME,
/* 174 */     SET_WARNING_BLOCKS;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S44PacketWorldBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */