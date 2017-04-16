package me.anwarshahriar.rxjavademo.view

import me.anwarshahriar.rxjavademo.model.Gist

interface GistView {
  fun showEmptyUsernameError(message: String)
  fun showNoGistsStatus()
  fun hideNoGistsStatus()
  fun showLoading()
  fun hideLoading()
  fun openGistHtmlUrl(url: String)
  fun showNoUrlExist()
  fun getUsername(): String?
  fun showGists(gists: List<Gist>)
}
