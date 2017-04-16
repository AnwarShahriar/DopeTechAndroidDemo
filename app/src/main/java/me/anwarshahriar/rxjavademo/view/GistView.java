package me.anwarshahriar.rxjavademo.view;

import java.util.List;
import me.anwarshahriar.rxjavademo.model.Gist;

public interface GistView {
  void showEmptyUsernameError(String message);
  void showNoGistsStatus();
  void hideNoGistsStatus();
  void showLoading();
  void hideLoading();
  String getUsername();
  void showGists(List<Gist> gists);
}
