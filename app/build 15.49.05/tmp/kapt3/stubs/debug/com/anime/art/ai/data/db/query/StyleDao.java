package com.anime.art.ai.data.db.query;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\'J\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\'J\u000e\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\tH\'J\u0014\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\u000bH\'J\'\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00070\t2\u0012\u0010\r\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\u000e\"\u00020\u0005H\'\u00a2\u0006\u0002\u0010\u000f\u00a8\u0006\u0010"}, d2 = {"Lcom/anime/art/ai/data/db/query/StyleDao;", "", "deleteAll", "", "findById", "Lcom/anime/art/ai/domain/model/config/Style;", "id", "", "getAll", "", "getAllLive", "Landroidx/lifecycle/LiveData;", "inserts", "objects", "", "([Lcom/anime/art/ai/domain/model/config/Style;)Ljava/util/List;", "app_debug"})
@androidx.room.Dao
public abstract interface StyleDao {
    
    @androidx.room.Query(value = "SELECT * FROM Styles")
    @org.jetbrains.annotations.NotNull
    public abstract java.util.List<com.anime.art.ai.domain.model.config.Style> getAll();
    
    @androidx.room.Query(value = "SELECT * FROM Styles")
    @org.jetbrains.annotations.NotNull
    public abstract androidx.lifecycle.LiveData<java.util.List<com.anime.art.ai.domain.model.config.Style>> getAllLive();
    
    @androidx.room.Insert
    @org.jetbrains.annotations.NotNull
    public abstract java.util.List<java.lang.Long> inserts(@org.jetbrains.annotations.NotNull
    com.anime.art.ai.domain.model.config.Style... objects);
    
    @androidx.room.Query(value = "DELETE FROM Styles")
    public abstract void deleteAll();
    
    @androidx.room.Query(value = "SELECT * FROM Styles WHERE id =:id LIMIT 1")
    @org.jetbrains.annotations.Nullable
    public abstract com.anime.art.ai.domain.model.config.Style findById(long id);
}