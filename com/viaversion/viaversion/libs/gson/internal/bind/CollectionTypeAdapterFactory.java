/*    */ package com.viaversion.viaversion.libs.gson.internal.bind;
/*    */ 
/*    */ import com.viaversion.viaversion.libs.gson.Gson;
/*    */ import com.viaversion.viaversion.libs.gson.TypeAdapter;
/*    */ import com.viaversion.viaversion.libs.gson.TypeAdapterFactory;
/*    */ import com.viaversion.viaversion.libs.gson.internal.;
/*    */ import com.viaversion.viaversion.libs.gson.internal.ConstructorConstructor;
/*    */ import com.viaversion.viaversion.libs.gson.internal.ObjectConstructor;
/*    */ import com.viaversion.viaversion.libs.gson.reflect.TypeToken;
/*    */ import com.viaversion.viaversion.libs.gson.stream.JsonReader;
/*    */ import com.viaversion.viaversion.libs.gson.stream.JsonToken;
/*    */ import com.viaversion.viaversion.libs.gson.stream.JsonWriter;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Collection;
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
/*    */ public final class CollectionTypeAdapterFactory
/*    */   implements TypeAdapterFactory
/*    */ {
/*    */   private final ConstructorConstructor constructorConstructor;
/*    */   
/*    */   public CollectionTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
/* 40 */     this.constructorConstructor = constructorConstructor;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 45 */     Type type = typeToken.getType();
/*    */     
/* 47 */     Class<? super T> rawType = typeToken.getRawType();
/* 48 */     if (!Collection.class.isAssignableFrom(rawType)) {
/* 49 */       return null;
/*    */     }
/*    */     
/* 52 */     Type elementType = .Gson.Types.getCollectionElementType(type, rawType);
/* 53 */     TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));
/* 54 */     ObjectConstructor<T> constructor = this.constructorConstructor.get(typeToken);
/*    */ 
/*    */     
/* 57 */     TypeAdapter<T> result = new Adapter(gson, elementType, elementTypeAdapter, (ObjectConstructor)constructor);
/* 58 */     return result;
/*    */   }
/*    */   
/*    */   private static final class Adapter<E>
/*    */     extends TypeAdapter<Collection<E>>
/*    */   {
/*    */     private final TypeAdapter<E> elementTypeAdapter;
/*    */     private final ObjectConstructor<? extends Collection<E>> constructor;
/*    */     
/*    */     public Adapter(Gson context, Type elementType, TypeAdapter<E> elementTypeAdapter, ObjectConstructor<? extends Collection<E>> constructor) {
/* 68 */       this.elementTypeAdapter = new TypeAdapterRuntimeTypeWrapper<>(context, elementTypeAdapter, elementType);
/*    */       
/* 70 */       this.constructor = constructor;
/*    */     }
/*    */     
/*    */     public Collection<E> read(JsonReader in) throws IOException {
/* 74 */       if (in.peek() == JsonToken.NULL) {
/* 75 */         in.nextNull();
/* 76 */         return null;
/*    */       } 
/*    */       
/* 79 */       Collection<E> collection = (Collection<E>)this.constructor.construct();
/* 80 */       in.beginArray();
/* 81 */       while (in.hasNext()) {
/* 82 */         E instance = (E)this.elementTypeAdapter.read(in);
/* 83 */         collection.add(instance);
/*    */       } 
/* 85 */       in.endArray();
/* 86 */       return collection;
/*    */     }
/*    */     
/*    */     public void write(JsonWriter out, Collection<E> collection) throws IOException {
/* 90 */       if (collection == null) {
/* 91 */         out.nullValue();
/*    */         
/*    */         return;
/*    */       } 
/* 95 */       out.beginArray();
/* 96 */       for (E element : collection) {
/* 97 */         this.elementTypeAdapter.write(out, element);
/*    */       }
/* 99 */       out.endArray();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\gson\internal\bind\CollectionTypeAdapterFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */