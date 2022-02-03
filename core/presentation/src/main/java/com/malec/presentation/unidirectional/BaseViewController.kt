package com.malec.presentation.unidirectional

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.malec.store.BaseStore
import kotlinx.coroutines.*

abstract class BaseViewController<State, Action, View : BaseView<State>>(
    protected val store: BaseStore<State, Action>
) : ViewController, LifecycleEventObserver {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var isFirstAttach = true
    protected var isAttach = false

    private var createdView: LifecycleOwner? = null
    private var view: View? = null

    private var subscription: Job? = null

    override fun attach() {}

    override fun firstViewAttach() {}

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                createdView = source
            }
            Lifecycle.Event.ON_RESUME -> {
                isAttach = true
                view = source as View
                subscription = scope.launch {
                    store.state.collect {
                        view?.renderState(it)
                    }
                }
                attach()
                if (isFirstAttach) {
                    isFirstAttach = false
                    firstViewAttach()
                }
            }
            Lifecycle.Event.ON_PAUSE -> {
                isAttach = false
                subscription?.cancel()
            }
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
            scope.cancel()
        }
    }
}