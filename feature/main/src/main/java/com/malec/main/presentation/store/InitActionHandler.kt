package com.malec.main.presentation.store

import com.malec.domain.repository.UserRepo
import com.malec.main.dependencies.MainOutput
import com.malec.store.ActionHandler

class InitActionHandler(
    private val repo: UserRepo,
    private val mainOutput: MainOutput
) : ActionHandler<MainAction>(
    requirement = { action -> action is MainAction.Init },
    handler = { _ ->
        if (repo.isUserAuthorized())
            mainOutput.main()
        else
            mainOutput.login()
    }
)