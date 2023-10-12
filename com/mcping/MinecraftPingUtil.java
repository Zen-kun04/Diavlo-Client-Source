/*    */ package com.mcping;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class MinecraftPingUtil
/*    */ {
/*  9 */   public static byte PACKET_HANDSHAKE = 0;
/*    */   
/* 11 */   public static byte PACKET_STATUSREQUEST = 0;
/*    */   
/* 13 */   public static byte PACKET_PING = 1;
/*    */   
/* 15 */   public static int PROTOCOL_VERSION = 4;
/*    */   
/* 17 */   public static int STATUS_HANDSHAKE = 1;
/*    */   
/*    */   public static void validate(Object o, String m) {
/* 20 */     if (o == null)
/* 21 */       throw new RuntimeException(m); 
/*    */   }
/*    */   
/*    */   public static void io(boolean b, String m) throws IOException {
/* 25 */     if (b) {
/* 26 */       throw new IOException(m);
/*    */     }
/*    */   }
/*    */   
/*    */   public static int readVarInt(DataInputStream in) throws IOException {
/* 31 */     int i = 0;
/* 32 */     int j = 0;
/*    */     while (true) {
/* 34 */       byte k = in.readByte();
/* 35 */       i |= (k & Byte.MAX_VALUE) << j++ * 7;
/* 36 */       if (j > 5)
/* 37 */         throw new RuntimeException("VarInt too big"); 
/* 38 */       if ((k & 0x80) != 128)
/* 39 */         return i; 
/*    */     } 
/*    */   }
/*    */   public static void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
/* 43 */     while ((paramInt & 0xFFFFFF80) != 0) {
/* 44 */       out.writeByte(paramInt & 0x7F | 0x80);
/* 45 */       paramInt >>>= 7;
/*    */     } 
/* 47 */     out.writeByte(paramInt);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\mcping\MinecraftPingUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */