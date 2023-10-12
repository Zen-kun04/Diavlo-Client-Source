/*    */ package com.mcping;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Socket;
/*    */ 
/*    */ public class MinecraftPing
/*    */ {
/*    */   public String[] getPingString(MinecraftPingOptions options) throws IOException {
/* 13 */     MinecraftPingUtil.validate(options.getHostname(), "Hostname cannot be null.");
/* 14 */     Socket socket = new Socket();
/* 15 */     socket.connect(new InetSocketAddress(options.getHostname(), options.getPort()), options.getTimeout());
/* 16 */     DataInputStream in = new DataInputStream(socket.getInputStream());
/* 17 */     DataOutputStream out = new DataOutputStream(socket.getOutputStream());
/* 18 */     ByteArrayOutputStream handshake_bytes = new ByteArrayOutputStream();
/* 19 */     DataOutputStream handshake = new DataOutputStream(handshake_bytes);
/* 20 */     handshake.writeByte(MinecraftPingUtil.PACKET_HANDSHAKE);
/* 21 */     MinecraftPingUtil.writeVarInt(handshake, MinecraftPingUtil.PROTOCOL_VERSION);
/* 22 */     MinecraftPingUtil.writeVarInt(handshake, options.getHostname().length());
/* 23 */     handshake.writeBytes(options.getHostname());
/* 24 */     handshake.writeShort(options.getPort());
/* 25 */     MinecraftPingUtil.writeVarInt(handshake, MinecraftPingUtil.STATUS_HANDSHAKE);
/* 26 */     MinecraftPingUtil.writeVarInt(out, handshake_bytes.size());
/* 27 */     out.write(handshake_bytes.toByteArray());
/* 28 */     out.writeByte(1);
/* 29 */     out.writeByte(MinecraftPingUtil.PACKET_STATUSREQUEST);
/* 30 */     MinecraftPingUtil.readVarInt(in);
/* 31 */     int id = MinecraftPingUtil.readVarInt(in);
/* 32 */     MinecraftPingUtil.io((id == -1), "Server prematurely ended stream.");
/* 33 */     MinecraftPingUtil.io((id != MinecraftPingUtil.PACKET_STATUSREQUEST), "Server returned invalid packet.");
/* 34 */     int length = MinecraftPingUtil.readVarInt(in);
/* 35 */     MinecraftPingUtil.io((length == -1), "Server prematurely ended stream.");
/* 36 */     MinecraftPingUtil.io((length == 0), "Server returned unexpected value.");
/* 37 */     byte[] data = new byte[length];
/* 38 */     in.readFully(data);
/* 39 */     String json = new String(data, options.getCharset());
/* 40 */     out.writeByte(9);
/* 41 */     out.writeByte(MinecraftPingUtil.PACKET_PING);
/* 42 */     out.writeLong(System.currentTimeMillis());
/* 43 */     MinecraftPingUtil.readVarInt(in);
/* 44 */     id = MinecraftPingUtil.readVarInt(in);
/* 45 */     MinecraftPingUtil.io((id == -1), "Server prematurely ended stream.");
/* 46 */     MinecraftPingUtil.io((id != MinecraftPingUtil.PACKET_PING), "Server returned invalid packet.");
/* 47 */     handshake.close();
/* 48 */     handshake_bytes.close();
/* 49 */     out.close();
/* 50 */     in.close();
/* 51 */     socket.close();
/* 52 */     String[] returnValue = new String[5];
/*    */     try {
/*    */       try {
/* 55 */         returnValue[0] = json.split("\"description\":\"")[1].split("\",")[0];
/* 56 */       } catch (Exception var13) {
/* 57 */         returnValue[0] = json.split("\"description\":")[1].split("\"text\":\"")[1].split("\"")[0];
/*    */       } 
/* 59 */       returnValue[1] = json.split("\"players\":")[1].split("\"max\":")[1].split(",")[0];
/* 60 */       returnValue[2] = json.split("\"players\":")[1].split("\"online\":")[1].split("}")[0].split(",")[0];
/* 61 */       returnValue[3] = json.split("\"version\":")[1].split("\"name\":\"")[1].split("\"")[0];
/* 62 */       returnValue[4] = json.split("\"version\":")[1].split("\"protocol\":")[1].split("}")[0].split(",")[0];
/* 63 */     } catch (Exception var14) {
/* 64 */       returnValue = null;
/*    */     } 
/* 66 */     return returnValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\mcping\MinecraftPing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */