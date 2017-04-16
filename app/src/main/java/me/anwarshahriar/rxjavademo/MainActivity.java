package me.anwarshahriar.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

  RecyclerView gistList;
  EditText fieldUsername;
  View progress;
  View textGistStatus;

  Disposable disposable;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    gistList = (RecyclerView) findViewById(R.id.gist_list);
    fieldUsername = (EditText) findViewById(R.id.field_username);
    progress = findViewById(R.id.progress);
    textGistStatus = findViewById(R.id.text_gist_status);

    progress.setVisibility(View.GONE);
    textGistStatus.setVisibility(View.VISIBLE);

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    gistList.setLayoutManager(layoutManager);
    final GistAdapter adapter = new GistAdapter();
    gistList.setAdapter(adapter);
    gistList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    fieldUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          String username = textView.getText().toString().trim();

          if (username.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter an username", Toast.LENGTH_SHORT)
                .show();
            return false;
          }

          adapter.setData(new ArrayList<Gist>());
          textGistStatus.setVisibility(View.GONE);
          progress.setVisibility(View.VISIBLE);

          disposable = getGistsObservalbe(username).subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Consumer<List<Gist>>() {
                @Override public void accept(@NonNull List<Gist> gists) throws Exception {
                  progress.setVisibility(View.GONE);
                  if (gists.size() == 0) {
                    textGistStatus.setVisibility(View.VISIBLE);
                  }
                  adapter.setData(gists);
                }
              });
        }

        return false;
      }
    });
  }

  private Observable<List<Gist>> getGistsObservalbe(@NonNull final String username) {
    return Observable.defer(new Callable<ObservableSource<? extends List<Gist>>>() {
      @SuppressWarnings("ConstantConditions") @Override
      public ObservableSource<? extends List<Gist>> call() {
        try {
          return Observable.just(getGists(username));
        } catch (IOException e) {
          return null;
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

  @Override protected void onDestroy() {
    super.onDestroy();
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
    }
  }

  private static class GistAdapter extends RecyclerView.Adapter<GistViewHolder> {
    List<Gist> gists;

    GistAdapter() {
      gists = new ArrayList<>();
    }

    void setData(List<Gist> data) {
      this.gists = data;
      notifyDataSetChanged();
    }

    @Override public GistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(android.R.layout.simple_list_item_1, parent, false);
      return new GistViewHolder(view);
    }

    @Override public void onBindViewHolder(GistViewHolder holder, int position) {
      Gist gist = gists.get(position);
      holder.bind(gist);
    }

    @Override public int getItemCount() {
      return gists.size();
    }
  }

  private static class GistViewHolder extends RecyclerView.ViewHolder {
    TextView textGistName;

    GistViewHolder(View itemView) {
      super(itemView);
      textGistName = (TextView) itemView.findViewById(android.R.id.text1);
    }

    void bind(Gist data) {
      String gistName = data.getFiles().entrySet().iterator().next().getValue().getFileName();
      textGistName.setText(gistName);
    }
  }
}
