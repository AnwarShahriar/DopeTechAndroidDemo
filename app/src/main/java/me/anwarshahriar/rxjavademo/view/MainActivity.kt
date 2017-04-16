package me.anwarshahriar.rxjavademo.view

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

class MainActivity : AppCompatActivity(), GistView {

  lateinit var gistList: RecyclerView
  lateinit var fieldUsername: EditText
  lateinit var progress: View
  lateinit var textGistStatus: View

  lateinit var gistPresenter: GistPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    gistPresenter = GistPresenter()

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

    val layoutManager = LinearLayoutManager(this)
    gistList.layoutManager = layoutManager
    val adapter = GistAdapter()
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

  override fun getUsername(): String {
    return fieldUsername.text.toString()
  }

  override fun showGists(gists: List<Gist>) {
    (gistList.adapter as GistAdapter).setData(gists)
  }
}
