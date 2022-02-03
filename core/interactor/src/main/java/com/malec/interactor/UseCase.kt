package com.malec.interactor

abstract class UseCase<Params : UseCaseParams, ReturnType> {
    abstract suspend fun build(params: Params): ReturnType
}