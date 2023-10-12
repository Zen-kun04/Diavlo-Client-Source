/*    */ package com.viaversion.viaversion.libs.kyori.adventure.nbt;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class IOStreamUtil
/*    */ {
/*    */   static InputStream closeShield(final InputStream stream) {
/* 35 */     return new InputStream()
/*    */       {
/*    */         public int read() throws IOException {
/* 38 */           return stream.read();
/*    */         }
/*    */ 
/*    */         
/*    */         public int read(byte[] b) throws IOException {
/* 43 */           return stream.read(b);
/*    */         }
/*    */ 
/*    */         
/*    */         public int read(byte[] b, int off, int len) throws IOException {
/* 48 */           return stream.read(b, off, len);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static OutputStream closeShield(final OutputStream stream) {
/* 54 */     return new OutputStream()
/*    */       {
/*    */         public void write(int b) throws IOException {
/* 57 */           stream.write(b);
/*    */         }
/*    */ 
/*    */         
/*    */         public void write(byte[] b) throws IOException {
/* 62 */           stream.write(b);
/*    */         }
/*    */ 
/*    */         
/*    */         public void write(byte[] b, int off, int len) throws IOException {
/* 67 */           stream.write(b, off, len);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\nbt\IOStreamUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */