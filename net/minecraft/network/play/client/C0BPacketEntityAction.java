/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ public class C0BPacketEntityAction
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private int entityID;
/*    */   private Action action;
/*    */   private int auxData;
/*    */   
/*    */   public C0BPacketEntityAction() {}
/*    */   
/*    */   public C0BPacketEntityAction(Entity entity, Action action) {
/* 21 */     this(entity, action, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public C0BPacketEntityAction(Entity entity, Action action, int auxData) {
/* 26 */     this.entityID = entity.getEntityId();
/* 27 */     this.action = action;
/* 28 */     this.auxData = auxData;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 33 */     this.entityID = buf.readVarIntFromBuffer();
/* 34 */     this.action = (Action)buf.readEnumValue(Action.class);
/* 35 */     this.auxData = buf.readVarIntFromBuffer();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 40 */     buf.writeVarIntToBuffer(this.entityID);
/* 41 */     buf.writeEnumValue(this.action);
/* 42 */     buf.writeVarIntToBuffer(this.auxData);
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayServer handler) {
/* 47 */     handler.processEntityAction(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public Action getAction() {
/* 52 */     return this.action;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getAuxData() {
/* 57 */     return this.auxData;
/*    */   }
/*    */   
/*    */   public enum Action
/*    */   {
/* 62 */     START_SNEAKING,
/* 63 */     STOP_SNEAKING,
/* 64 */     STOP_SLEEPING,
/* 65 */     START_SPRINTING,
/* 66 */     STOP_SPRINTING,
/* 67 */     RIDING_JUMP,
/* 68 */     OPEN_INVENTORY;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\client\C0BPacketEntityAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */