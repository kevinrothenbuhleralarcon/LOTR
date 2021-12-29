package ch.kra.lotr.di

import ch.kra.lotr.core.Constants.BASE_URL
import ch.kra.lotr.data.remote.dto.LotrApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object LotrModule {

    @Provides
    @Singleton
    fun provideLotrApi(): LotrApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LotrApi::class.java)
    }
}