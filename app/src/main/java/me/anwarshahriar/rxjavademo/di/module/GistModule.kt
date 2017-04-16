@file:Suppress("unused")

package me.anwarshahriar.rxjavademo.di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import me.anwarshahriar.rxjavademo.presenter.GistPresenter
import okhttp3.OkHttpClient

@Module
class GistModule {

  @Provides
  internal fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient()
  }

  @Provides
  internal fun provideGson(): Gson {
    return Gson()
  }

  @Provides
  internal fun provideGistPresenter(gson: Gson, client: OkHttpClient): GistPresenter {
    return GistPresenter(gson, client)
  }
}
