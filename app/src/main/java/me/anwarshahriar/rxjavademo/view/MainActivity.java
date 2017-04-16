package me.anwarshahriar.rxjavademo.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import me.anwarshahriar.rxjavademo.model.Gist;
import me.anwarshahriar.rxjavademo.presenter.GistPresenter;
import me.anwarshahriar.rxjavademo.R;

public class MainActivity extends AppCompatActivity implements GistView {

  RecyclerView gistList;
  EditText fieldUsername;
  View progress;
  View textGistStatus;

  GistPresenter gistPresenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    gistPresenter = new GistPresenter();

    init();

    fieldUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          gistPresenter.loadGists();
        }

        return false;
      }
    });
  }

  private void init() {
    gistList = (RecyclerView) findViewById(R.id.gist_list);
    fieldUsername = (EditText) findViewById(R.id.field_username);
    progress = findViewById(R.id.progress);
    textGistStatus = findViewById(R.id.text_gist_status);

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    gistList.setLayoutManager(layoutManager);
    final GistAdapter adapter = new GistAdapter();
    gistList.setAdapter(adapter);
    gistList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
  }

  @Override protected void onStart() {
    super.onStart();
    gistPresenter.setView(this);
  }

  @Override protected void onStop() {
    super.onStop();
    gistPresenter.cleanUp();
  }

  @Override public void showEmptyUsernameError(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  @Override public void showNoGistsStatus() {
    textGistStatus.setVisibility(View.VISIBLE);
  }

  @Override public void hideNoGistsStatus() {
    textGistStatus.setVisibility(View.GONE);
  }

  @Override public void showLoading() {
    progress.setVisibility(View.VISIBLE);
  }

  @Override public void hideLoading() {
    progress.setVisibility(View.GONE);
  }

  @Override public String getUsername() {
    return fieldUsername.getText().toString();
  }

  @Override public void showGists(List<Gist> gists) {
    ((GistAdapter) gistList.getAdapter()).setData(gists);
  }
}
