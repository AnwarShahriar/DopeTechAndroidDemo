package me.anwarshahriar.rxjavademo;

import java.util.List;
import me.anwarshahriar.rxjavademo.model.Gist;

public interface GistView {
  void showEmptyUsernameError(String message);
  void showNoGistsStatus(String message);
  void showLoading();
  void hideLoading();
  void showGists(List<Gist> gists);
}
