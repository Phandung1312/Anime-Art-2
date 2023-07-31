package com.anime.art.ai.inject

import android.content.Context
import androidx.room.Room
import com.anime.art.ai.common.App
import com.anime.art.ai.common.Constraint
import com.anime.art.ai.data.Preferences
import com.anime.art.ai.data.db.Database
import com.anime.art.ai.data.db.query.GalleryDao
import com.anime.art.ai.data.db.query.PromptDao
import com.anime.art.ai.data.manager.NotificationManagerImpl
import com.anime.art.ai.data.repoository.AIApiRepositoryImpl
import com.anime.art.ai.data.repoository.ServerApiRepositoryImpl
import com.anime.art.ai.data.repoository.SyncRepositoryImpl
import com.anime.art.ai.domain.manager.NotificationManager
import com.anime.art.ai.domain.repository.AIApiRepository
import com.anime.art.ai.domain.repository.ServerApiRepository
import com.anime.art.ai.domain.repository.SyncRepository
import com.anime.art.ai.inject.sinkin.AIApi
import com.anime.art.ai.inject.sinkin.ServerApi
import com.anime.art.ai.inject.sinkin.SinkinApi
import com.basic.data.PreferencesConfig
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideContext(): Context = App.app

    @Provides
    @Singleton
    fun provideFirebaseAnalytic(): FirebaseAnalytics = Firebase.analytics

    @Provides
    @Singleton
    fun providePreferences(context: Context): Preferences {
        val sharedPreferences = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val rxSharedPreferences = RxSharedPreferences.create(sharedPreferences)
        return Preferences(rxSharedPreferences)
    }

    @Provides
    @Singleton
    fun providePreferencesConfig(context: Context): PreferencesConfig {
        val sharedPreferences = context.getSharedPreferences("PreferencesConfig", Context.MODE_PRIVATE)
        val rxSharedPreferences = RxSharedPreferences.create(sharedPreferences)
        return PreferencesConfig(context, rxSharedPreferences)
    }

    // Server

    @Provides
    @Singleton
    fun provideDezgoApi(): SinkinApi {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Timber.d(message)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val networkInterceptor = Interceptor {
            val request = it.request().newBuilder().build()
            it.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain
                    .request()
                    .newBuilder()

                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .hostnameVerifier { _, _ -> true }
            .retryOnConnectionFailure(false)
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constraint.Sinkin.URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(SinkinApi::class.java)
    }
    @Provides
    @Singleton
    @Named("CreditClient")
    fun providesCreditOkHttpClient() : OkHttpClient{
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Timber.d(message)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val networkInterceptor = Interceptor {
            val request = it.request().newBuilder().build()
            it.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain
                    .request()
                    .newBuilder()

                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .hostnameVerifier { _, _ -> true }
            .retryOnConnectionFailure(false)
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .build()

        return okHttpClient
    }
    @Provides
    @Singleton
    @Named("AIClient")
    fun providesAIOkHttpClient() : OkHttpClient{
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Timber.d(message)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val networkInterceptor = Interceptor {
            val request = it.request().newBuilder().build()
            it.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(object  : Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    val originalRequest = chain.request()
                    val authenticatedRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer ${Constraint.AIGeneration.KEY}")
                        .build()
                    return chain.proceed(authenticatedRequest)
                }

            })
            .addInterceptor { chain ->
                val requestBuilder = chain
                    .request()
                    .newBuilder()

                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .hostnameVerifier { _, _ -> true }
            .retryOnConnectionFailure(false)
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .build()

        return okHttpClient
    }

    @Provides
    @Singleton
    fun providesGsonConverterFactory() : GsonConverterFactory{
        val gson = GsonBuilder().setLenient().create()
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    @Named("CreditServer")
    fun providesRetrofitCreditServer(gsonConverterFactory: GsonConverterFactory,
    @Named("CreditClient") okHttpClient: OkHttpClient) : Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(Constraint.BASE_URL)
            .client(okHttpClient)
            .build()
    }
    @Provides
    @Singleton
    @Named("AIServer")
    fun providesRetrofitAIServer(gsonConverterFactory: GsonConverterFactory,
                                 @Named("AIClient") okHttpClient: OkHttpClient) : Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(Constraint.AIGeneration.URL)
            .client(okHttpClient)
            .build()
    }
    @Provides
    fun providesServerApi(@Named("CreditServer") retrofit: Retrofit) : ServerApi{
        return retrofit.create(ServerApi::class.java)
    }

    @Provides
    fun providesAIApi(@Named("AIServer") retrofit: Retrofit) : AIApi{
        return retrofit.create(AIApi::class.java)
    }
    // Database

    @Provides
    @Singleton
    fun provideDatabase(context: Context): Database = Room.databaseBuilder(
        context,
        Database::class.java,
        Database.DB_NAME
    )
        .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()

    @Provides
    @Singleton
    fun provideStyleDao(database: Database): GalleryDao = database.galleryDao()

    @Provides
    @Singleton
    fun providesPromptDao(database: Database) : PromptDao = database.promptDao()
    // Repository

    @Provides
    @Singleton
    fun provideSyncRepositoryImpl(repo: SyncRepositoryImpl): SyncRepository = repo

    @Provides
    @Singleton
    fun provideServerApiRepositoryImpl(repo: ServerApiRepositoryImpl): ServerApiRepository = repo

    @Provides
    @Singleton
    fun providesAIApiRepositoryImpl(repo: AIApiRepositoryImpl): AIApiRepository = repo
    // Manager

    @Provides
    @Singleton
    fun provideNotificationManagerImpl(manager: NotificationManagerImpl): NotificationManager = manager

}