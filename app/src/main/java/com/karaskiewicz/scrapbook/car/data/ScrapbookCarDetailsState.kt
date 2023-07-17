package com.karaskiewicz.scrapbook.car.data

sealed class ScrapbookCarDetailsState {
  object NavigateBackActionState : ScrapbookCarDetailsState()
  object InitialState : ScrapbookCarDetailsState()
}
