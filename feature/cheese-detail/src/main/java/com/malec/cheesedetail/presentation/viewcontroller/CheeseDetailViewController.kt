package com.malec.cheesedetail.presentation.viewcontroller

import com.malec.cheesedetail.presentation.store.CheeseDetailAction
import com.malec.cheesedetail.presentation.store.CheeseDetailState
import com.malec.cheesedetail.presentation.store.CheeseDetailStore
import com.malec.presentation.unidirectional.BaseView
import com.malec.presentation.unidirectional.BaseViewController

class CheeseDetailViewController(
    store: CheeseDetailStore
) : BaseViewController<CheeseDetailState, CheeseDetailAction, BaseView<CheeseDetailState>>(store) {
    fun loadCheese(cheeseId: Long) {
        dispatchAction(CheeseDetailAction.LoadCheese(cheeseId))
    }

    fun setName(name: String) {
        dispatchAction(CheeseDetailAction.SetName(name))
    }

    fun setDate(date: String) {
        dispatchAction(CheeseDetailAction.SetDate(date))
    }

    fun selectRecipeAtPosition(position: Int) {
        dispatchAction(CheeseDetailAction.SelectRecipeAtPosition(position))
    }

    fun setComment(comment: String) {
        dispatchAction(CheeseDetailAction.SetComment(comment))
    }

    fun setMilkType(type: String) {
        dispatchAction(CheeseDetailAction.SetMilkType(type))
    }

    fun setMilkVolume(volume: String) {
        dispatchAction(CheeseDetailAction.SetMilkVolume(volume))
    }

    fun setMilkAge(age: String) {
        dispatchAction(CheeseDetailAction.SetMilkAge(age))
    }

    fun setComposition(composition: String) {
        dispatchAction(CheeseDetailAction.SetComposition(composition))
    }

    fun addStage() {
        dispatchAction(CheeseDetailAction.AddStage)
    }

    fun setVolume(volume: String) {
        dispatchAction(CheeseDetailAction.SetVolume(volume))
    }

    fun setVolumeMax(volumeMax: String) {
        dispatchAction(CheeseDetailAction.SetVolumeMax(volumeMax))
    }

    fun setBadgeColor(color: Int) {
        dispatchAction(CheeseDetailAction.SetBadgeColor(color))
    }

    fun saveCheese() {
        dispatchAction(CheeseDetailAction.SaveCheese)
    }

    fun deleteCheese() {
        dispatchAction(CheeseDetailAction.DeleteCheese)
    }

    fun onBackClick() {
        dispatchAction(CheeseDetailAction.Back)
    }
}