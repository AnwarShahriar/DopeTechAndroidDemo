package me.anwarshahriar.rxjavademo.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import me.anwarshahriar.rxjavademo.model.Gist
import me.anwarshahriar.rxjavademo.presenter.GistPresenter
import me.anwarshahriar.rxjavademo.R
import me.anwarshahriar.rxjavademo.di.component.DaggerGistComponent
import me.anwarshahriar.rxjavademo.di.module.GistModule
import javax.inject.Inject

class MainActivity : AppCompatActivity(), GistView {

  lateinit var gistList: RecyclerView
  lateinit var fieldUsername: EditText
  lateinit var progress: View
  lateinit var textGistStatus: View

  @Inject lateinit var gistPresenter: GistPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    DaggerGistComponent.builder()
        .gistModule(GistModule())
        .build()
        .inject(this)

    init()

    fieldUsername.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        gistPresenter.loadGists()
      }

      false
    }
  }

  private fun init() {
    gistList = findViewById(R.id.gist_list) as RecyclerView
    fieldUsername = findViewById(R.id.field_username) as EditText
    progress = findViewById(R.id.progress)
    textGistStatus = findViewById(R.id.text_gist_status)

    val adapter = GistAdapter()
    adapter.setOnGistItemClickListener(object: GistAdapter.OnGistItemClickListener {
      override fun onGistItemClick(gist: Gist) {
        gistPresenter.gistSelected(gist)
      }
    })
    val layoutManager = LinearLayoutManager(this)
    gistList.layoutManager = layoutManager
    gistList.adapter = adapter
    gistList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
  }

  override fun onStart() {
    super.onStart()
    gistPresenter.setView(this)
  }

  override fun onStop() {
    super.onStop()
    gistPresenter.cleanUp()
  }

  override fun showEmptyUsernameError(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
  }

  override fun showNoGistsStatus() {
    textGistStatus.visibility = View.VISIBLE
  }

  override fun hideNoGistsStatus() {
    textGistStatus.visibility = View.GONE
  }

  override fun showLoading() {
    progress.visibility = View.VISIBLE
  }

  override fun hideLoading() {
    progress.visibility = View.GONE
  }

  override fun openGistHtmlUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    if (intent.resolveActivity(packageManager) != null) {
      startActivity(intent)
    } else {
      Toast.makeText(this, "No browser available to open the url", Toast.LENGTH_LONG).show()
    }
  }

  override fun showNoUrlExist() {
    Toast.makeText(this, "No web preview exist", Toast.LENGTH_SHORT).show()
  }

  override fun getUsername(): String? {
    return fieldUsername.text.toString()
  }

  override fun showGists(gists: List<Gist>) {
    (gistList.adapter as GistAdapter).setData(gists)
  }
}
