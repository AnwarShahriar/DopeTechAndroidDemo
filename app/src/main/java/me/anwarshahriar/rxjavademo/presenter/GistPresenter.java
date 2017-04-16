package me.anwarshahriar.rxjavademo.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import me.anwarshahriar.rxjavademo.model.Gist;
import me.anwarshahriar.rxjavademo.view.GistView;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GistPresenter {
  private GistView view;
  private Disposable disposable;

  public GistPresenter() {}

  public void setView(GistView view) {
    this.view = view;

    view.hideLoading();
    view.showNoGistsStatus();
  }

  public void loadGists() {
    String username = view.getUsername();

    if (username == null || username.trim().length() == 0) {
      view.showEmptyUsernameError("Please enter an username");
      return;
    }

    view.hideNoGistsStatus();
    view.showLoading();

    view.showGists(new ArrayList<Gist>());

    disposable = getGistsObservalbe(username).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Gist>>() {
          @Override public void accept(@NonNull List<Gist> gists) throws Exception {
            updateStatus(gists);
            view.showGists(gists);
          }
        }, new Consumer<Throwable>() {
          @Override public void accept(@NonNull Throwable throwable) throws Exception {
            updateStatus(null);
          }
        });
  }

  private void updateStatus(List<Gist> gists) {
    if (view == null) return;

    view.hideLoading();
    if (gists == null || gists.size() == 0) {
      view.showNoGistsStatus();
    }
  }

  private Observable<List<Gist>> getGistsObservalbe(@NonNull final String username) {
    return Observable.defer(new Callable<ObservableSource<? extends List<Gist>>>() {
      @SuppressWarnings("ConstantConditions") @Override
      public ObservableSource<? extends List<Gist>> call() {
        try {
          return Observable.just(getGists(username));
        } catch (IOException e) {
          return Observable.error(e);
        }
      }
    });
  }

  private List<Gist> getGists(@NonNull String username) throws IOException {
    String url = "https://api.github.com/users/" + username + "/gists";
    OkHttpClient client = new OkHttpClient();
    Request gistsRequest = new Request.Builder().url(url).build();
    Call call = client.newCall(gistsRequest);
    Response response = call.execute();
    if (response.isSuccessful()) {
      return new Gson().fromJson(response.body().charStream(), new TypeToken<List<Gist>>() {
      }.getType());
    }
    return null;
  }

  public void cleanUp() {
    view = null;
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
    }
  }
}
