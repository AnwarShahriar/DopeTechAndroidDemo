package me.anwarshahriar.rxjavademo.di.component

import dagger.Component
import me.anwarshahriar.rxjavademo.di.module.GistModule
import me.anwarshahriar.rxjavademo.view.MainActivity

@Component(modules = arrayOf(GistModule::class))
interface GistComponent {
  fun inject(target: MainActivity)
}
