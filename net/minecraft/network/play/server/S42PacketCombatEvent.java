/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.network.INetHandler;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.CombatTracker;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class S42PacketCombatEvent
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   public Event eventType;
/*    */   public int field_179774_b;
/*    */   public int field_179775_c;
/*    */   public int field_179772_d;
/*    */   public String deathMessage;
/*    */   
/*    */   public S42PacketCombatEvent() {}
/*    */   
/*    */   public S42PacketCombatEvent(CombatTracker combatTrackerIn, Event combatEventType) {
/* 25 */     this.eventType = combatEventType;
/* 26 */     EntityLivingBase entitylivingbase = combatTrackerIn.func_94550_c();
/*    */     
/* 28 */     switch (combatEventType) {
/*    */       
/*    */       case END_COMBAT:
/* 31 */         this.field_179772_d = combatTrackerIn.func_180134_f();
/* 32 */         this.field_179775_c = (entitylivingbase == null) ? -1 : entitylivingbase.getEntityId();
/*    */         break;
/*    */       
/*    */       case ENTITY_DIED:
/* 36 */         this.field_179774_b = combatTrackerIn.getFighter().getEntityId();
/* 37 */         this.field_179775_c = (entitylivingbase == null) ? -1 : entitylivingbase.getEntityId();
/* 38 */         this.deathMessage = combatTrackerIn.getDeathMessage().getUnformattedText();
/*    */         break;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void readPacketData(PacketBuffer buf) throws IOException {
/* 44 */     this.eventType = (Event)buf.readEnumValue(Event.class);
/*    */     
/* 46 */     if (this.eventType == Event.END_COMBAT) {
/*    */       
/* 48 */       this.field_179772_d = buf.readVarIntFromBuffer();
/* 49 */       this.field_179775_c = buf.readInt();
/*    */     }
/* 51 */     else if (this.eventType == Event.ENTITY_DIED) {
/*    */       
/* 53 */       this.field_179774_b = buf.readVarIntFromBuffer();
/* 54 */       this.field_179775_c = buf.readInt();
/* 55 */       this.deathMessage = buf.readStringFromBuffer(32767);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void writePacketData(PacketBuffer buf) throws IOException {
/* 61 */     buf.writeEnumValue(this.eventType);
/*    */     
/* 63 */     if (this.eventType == Event.END_COMBAT) {
/*    */       
/* 65 */       buf.writeVarIntToBuffer(this.field_179772_d);
/* 66 */       buf.writeInt(this.field_179775_c);
/*    */     }
/* 68 */     else if (this.eventType == Event.ENTITY_DIED) {
/*    */       
/* 70 */       buf.writeVarIntToBuffer(this.field_179774_b);
/* 71 */       buf.writeInt(this.field_179775_c);
/* 72 */       buf.writeString(this.deathMessage);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void processPacket(INetHandlerPlayClient handler) {
/* 78 */     handler.handleCombatEvent(this);
/*    */   }
/*    */   
/*    */   public enum Event
/*    */   {
/* 83 */     ENTER_COMBAT,
/* 84 */     END_COMBAT,
/* 85 */     ENTITY_DIED;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\network\play\server\S42PacketCombatEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */