package com.example.tv.pexels.di

import androidx.room.Room
import com.example.tv.pexels.core.Constants
import com.example.tv.pexels.data.source.local.AppDatabase
import com.example.tv.pexels.presentation.ui.activity.NotificationViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.example.tv.pexels.data.repositories.SearchRepositoryImpl
import com.example.tv.pexels.data.repositories.PhotoRepositoryImpl
import com.example.tv.pexels.domain.interactors.AddSearchQueryUseCase
import com.example.tv.pexels.domain.interactors.DeleteSearchQueryByQueryUseCase
import com.example.tv.pexels.domain.interactors.GetAllSearchUseCase
import com.example.tv.pexels.domain.interactors.AddPhotoUseCase
import com.example.tv.pexels.domain.interactors.GetAllLikedPhotosUseCase
import com.example.tv.pexels.domain.interactors.GetAllDislikedPhotosUseCase
import com.example.tv.pexels.domain.interactors.GetAllPhotosUseCase
import com.example.tv.pexels.domain.interfaces.IMainApi
import com.example.tv.pexels.domain.interfaces.SearchRepository
import com.example.tv.pexels.domain.interfaces.PhotoRepository
import com.example.tv.pexels.presentation.ui.home.HomeViewModel
import com.example.tv.pexels.presentation.ui.search.SearchViewModel
import com.example.tv.pexels.presentation.ui.favorite.FavoriteViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val viewModelModule = module {
    singleOf(::NotificationViewModel)
    factoryOf(::HomeViewModel)
    factoryOf(::SearchViewModel)
    factoryOf(::FavoriteViewModel)
}

val useCaseModule = module {
    factoryOf(::AddSearchQueryUseCase)
    factoryOf(::DeleteSearchQueryByQueryUseCase)
    factoryOf(::GetAllSearchUseCase)
    factoryOf(::AddPhotoUseCase)
    factoryOf(::GetAllLikedPhotosUseCase)
    factoryOf(::GetAllDislikedPhotosUseCase)
    factoryOf(::GetAllPhotosUseCase)
}

val repositoryModule = module {
    single<SearchRepository> { SearchRepositoryImpl(get()) }
    single<PhotoRepository> { PhotoRepositoryImpl(get()) }
}

val dispatcherModule = module {
    factory { Dispatchers.Default }
}

val networkModule = module {
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        urlApi: String,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(urlApi)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    fun provideOkHttpClient(): OkHttpClient {
        // Create a logging interceptor
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(CustomInterceptor())
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS) // <- PHẢI ĐỂ 0 để giữ kết nối stream mở!
            .retryOnConnectionFailure(true)
            .build()
    }

    fun provideMainApi(retrofit: Retrofit): IMainApi =
        retrofit.create(IMainApi::class.java)

    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    fun provideGsonConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create(provideGson())

    singleOf(::provideGsonConverterFactory)
    singleOf(::provideOkHttpClient)
    single(named(Constants.MainApi.NAME)) {
        provideRetrofit(get(), get(), Constants.MainApi.BASE_URL)
    }
    single { provideMainApi(get(named(Constants.MainApi.NAME))) }
}

val databaseModule = module {
    // Provide Room Database
    single {
        Room.databaseBuilder(
            get(),                      // Context
            AppDatabase::class.java,
            "pexels_app_database"             // Database Name
        ).build()
    }

    // Provide DAO
    single { get<AppDatabase>().searchQueryDao() }
    single { get<AppDatabase>().photoDao() }
}