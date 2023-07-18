package com.anime.art.ai.domain.model.config;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0010 \n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001e\u0010\u000f\u001a\u00020\u00048\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\bR$\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00040\u00138\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017\u00a8\u0006\u0018"}, d2 = {"Lcom/anime/art/ai/domain/model/config/Style;", "", "()V", "display", "", "getDisplay", "()Ljava/lang/String;", "setDisplay", "(Ljava/lang/String;)V", "id", "", "getId", "()J", "setId", "(J)V", "preview", "getPreview", "setPreview", "prompts", "", "getPrompts", "()Ljava/util/List;", "setPrompts", "(Ljava/util/List;)V", "app_debug"})
@androidx.annotation.Keep
@androidx.room.Entity(tableName = "Styles")
public final class Style {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private long id = 0L;
    @com.google.gson.annotations.SerializedName(value = "preview")
    @com.google.gson.annotations.Expose
    @org.jetbrains.annotations.NotNull
    private java.lang.String preview = "";
    @com.google.gson.annotations.SerializedName(value = "display")
    @com.google.gson.annotations.Expose
    @org.jetbrains.annotations.NotNull
    private java.lang.String display = "";
    @com.google.gson.annotations.SerializedName(value = "prompts")
    @com.google.gson.annotations.Expose
    @org.jetbrains.annotations.NotNull
    private java.util.List<java.lang.String> prompts;
    
    public Style() {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    public final void setId(long p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getPreview() {
        return null;
    }
    
    public final void setPreview(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDisplay() {
        return null;
    }
    
    public final void setDisplay(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.String> getPrompts() {
        return null;
    }
    
    public final void setPrompts(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> p0) {
    }
}