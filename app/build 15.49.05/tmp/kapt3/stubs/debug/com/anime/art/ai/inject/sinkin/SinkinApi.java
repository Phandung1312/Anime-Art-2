package com.anime.art.ai.inject.sinkin;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\bf\u0018\u00002\u00020\u0001Jw\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0001\u0010\u0007\u001a\u00020\u00052\b\b\u0001\u0010\b\u001a\u00020\u00052\b\b\u0001\u0010\t\u001a\u00020\u00052\b\b\u0001\u0010\n\u001a\u00020\u00052\b\b\u0001\u0010\u000b\u001a\u00020\u00052\b\b\u0001\u0010\f\u001a\u00020\u00052\b\b\u0001\u0010\r\u001a\u00020\u00052\n\b\u0001\u0010\u000e\u001a\u0004\u0018\u00010\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000f\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0010"}, d2 = {"Lcom/anime/art/ai/inject/sinkin/SinkinApi;", "", "text2image", "Lokhttp3/ResponseBody;", "prompt", "Lokhttp3/RequestBody;", "negativePrompt", "guidance", "upscale", "sampler", "steps", "model", "width", "height", "seed", "(Lokhttp3/RequestBody;Lokhttp3/RequestBody;Lokhttp3/RequestBody;Lokhttp3/RequestBody;Lokhttp3/RequestBody;Lokhttp3/RequestBody;Lokhttp3/RequestBody;Lokhttp3/RequestBody;Lokhttp3/RequestBody;Lokhttp3/RequestBody;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface SinkinApi {
    
    @retrofit2.http.Multipart
    @retrofit2.http.POST(value = "")
    @retrofit2.http.Streaming
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object text2image(@retrofit2.http.Part(value = "prompt")
    @org.jetbrains.annotations.NotNull
    okhttp3.RequestBody prompt, @retrofit2.http.Part(value = "negative_prompt")
    @org.jetbrains.annotations.NotNull
    okhttp3.RequestBody negativePrompt, @retrofit2.http.Part(value = "guidance")
    @org.jetbrains.annotations.NotNull
    okhttp3.RequestBody guidance, @retrofit2.http.Part(value = "upscale")
    @org.jetbrains.annotations.NotNull
    okhttp3.RequestBody upscale, @retrofit2.http.Part(value = "sampler")
    @org.jetbrains.annotations.NotNull
    okhttp3.RequestBody sampler, @retrofit2.http.Part(value = "steps")
    @org.jetbrains.annotations.NotNull
    okhttp3.RequestBody steps, @retrofit2.http.Part(value = "model")
    @org.jetbrains.annotations.NotNull
    okhttp3.RequestBody model, @retrofit2.http.Part(value = "width")
    @org.jetbrains.annotations.NotNull
    okhttp3.RequestBody width, @retrofit2.http.Part(value = "height")
    @org.jetbrains.annotations.NotNull
    okhttp3.RequestBody height, @retrofit2.http.Part(value = "seed")
    @org.jetbrains.annotations.Nullable
    okhttp3.RequestBody seed, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super okhttp3.ResponseBody> $completion);
}