package com.karaskiewicz.scrapbook.di

import androidx.car.app.Screen
import androidx.room.Room
import com.karaskiewicz.scrapbook.add.viewmodel.ScrapAddViewModel
import com.karaskiewicz.scrapbook.car.viewmodel.ScrapbookCarViewModel
import com.karaskiewicz.scrapbook.database.MockDatabaseHelper
import com.karaskiewicz.scrapbook.database.ScrapDatabase
import com.karaskiewicz.scrapbook.database.repository.ScrapRepository
import com.karaskiewicz.scrapbook.list.viewmodel.ScrapListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val databaseModule = module {
  single {
    Room.databaseBuilder(get(), ScrapDatabase::class.java, "scraps")
      .fallbackToDestructiveMigration()
      .build()
  }
  single {
    ScrapRepository(get<ScrapDatabase>().scrapDao())
  }
}

private val viewModelsModule = module {
  viewModel { ScrapListViewModel(get()) }
  viewModel { ScrapAddViewModel(get(), get()) }
  single { (carScreen: Screen) -> ScrapbookCarViewModel(get(), carScreen) }
}

private val utilsModule = module {
  single {
    MockDatabaseHelper(get())
  }
}

val modules: List<Module> = listOf(databaseModule, viewModelsModule, utilsModule)
