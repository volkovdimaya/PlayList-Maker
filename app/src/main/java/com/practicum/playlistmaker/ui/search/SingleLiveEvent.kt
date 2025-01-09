package com.practicum.playlistmaker.ui.search

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.atomic.AtomicBoolean
import androidx.lifecycle.Observer

class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }
}