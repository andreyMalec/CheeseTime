package com.malec.presentation.unidirectional

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.malec.store.BaseStore
import kotlinx.coroutines.*

abstract class BaseViewController<State, Action, View : BaseView<State>>(
    private val store: BaseStore<State, Action>
) : ViewController, LifecycleEventObserver {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var createdView: LifecycleOwner? = null
    private var view: View? = null

    private var subscription: Job? = null

    override fun attach() {}

    override fun firstViewAttach() {}

    override fun detach() {
        scope.cancel()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                createdView = source
            }
            Lifecycle.Event.ON_RESUME -> {
                view = source as View
                subscription = scope.launch {
                    store.state.collect {
                        view?.renderState(it)
                    }
                }
            }
            Lifecycle.Event.ON_PAUSE -> subscription?.cancel()
            Lifecycle.Event.ON_DESTROY -> onDestroy(source)
            else -> {}
        }
    }

    private fun onDestroy(owner: LifecycleOwner) {
        if (owner is Fragment) {
            owner.activity?.let { activity ->
                if (activity.isFinishing) {
                    destroy(owner)
                } else {
                    var anyParentIsRemoving = false
                    var parent = owner.parentFragment
                    while (!anyParentIsRemoving && parent != null) {
                        anyParentIsRemoving = parent.isRemoving
                        parent = parent.parentFragment
                    }
                    if (owner.isRemoving || anyParentIsRemoving) {
                        destroy(owner)
                    }
                }
            }
        }
        if (owner is Activity) {
            if (owner.isFinishing) {
                destroy(owner)
            }
        }
    }

    protected fun dispatchAction(action: Action) {
        store.dispatchAction(action)
    }

    private fun destroy(owner: LifecycleOwner) {
        if (owner == createdView) {
            store.dispose()
        }
    }
}