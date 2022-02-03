package com.malec.cheeselist.presentation.store.actionsource

import com.malec.cheeselist.presentation.store.CheeseListAction
import com.malec.domain.usecase.GetRecipesUseCase
import com.malec.interactor.EmptyParams
import com.malec.presentation.Resources
import com.malec.store.ActionSource
import kotlinx.coroutines.flow.flowOf

class UpdateCheeseTypesActionSource(
    private val useCase: GetRecipesUseCase,
    private val res: Resources
) : ActionSource<CheeseListAction>(
    source = {
        val cheeseTypes = listOf(res.stringCheeseTypeAny()) + (
                useCase.build(EmptyParams).map {
                    it.name
                }.takeIf {
                    it.isNotEmpty()
                } ?: res.recipes()
                )
        flowOf(CheeseListAction.SaveCheeseTypes(cheeseTypes))
    },
    error = {
        CheeseListAction.Error(it)
    }
)