package me.anwarshahriar.rxjavademo.presenter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.anwarshahriar.rxjavademo.model.Gist
import me.anwarshahriar.rxjavademo.view.GistView
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.*
import java.util.concurrent.Callable

class GistPresenter {
  private var view: GistView? = null
  private var disposable: Disposable? = null

  fun setView(view: GistView) {
    this.view = view

    view.hideLoading()
    view.showNoGistsStatus()
  }

  fun loadGists() {
    val username = view?.getUsername()

    if (username == null || username.trim().isEmpty()) {
      view?.showEmptyUsernameError("Please enter an username")
      return
    }

    view?.hideNoGistsStatus()
    view?.showLoading()

    view?.showGists(ArrayList<Gist>())

    disposable = getGistsObservalbe(username)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ gists ->
          updateStatus(gists)
          view?.showGists(gists)
        }) { updateStatus(null) }
  }

  private fun updateStatus(gists: List<Gist>?) {
    if (view == null) return

    view?.hideLoading()
    if (gists == null || gists.isEmpty()) {
      view?.showNoGistsStatus()
    }
  }

  private fun getGistsObservalbe(@NonNull username: String): Observable<List<Gist>> {
    return Observable.defer(Callable<ObservableSource<out List<Gist>>> {
      try {
        return@Callable Observable.just(getGists(username)!!)
      } catch (e: IOException) {
        return@Callable Observable.error(e)
      }
    })
  }

  @Throws(IOException::class)
  private fun getGists(@NonNull username: String): List<Gist>? {
    val url = "https://api.github.com/users/$username/gists"
    val client = OkHttpClient()
    val gistsRequest = Request.Builder().url(url).build()
    val call = client.newCall(gistsRequest)
    val response = call.execute()
    if (response.isSuccessful) {
      return Gson().fromJson<List<Gist>>(response.body().charStream(),
          object : TypeToken<List<Gist>>() {

          }.type)
    }
    return null
  }

  fun cleanUp() {
    view = null
    if (disposable?.isDisposed ?: false) {
      disposable?.dispose()
    }
  }
}
