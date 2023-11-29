package com.example.menuapp.di.modules

import com.example.menuapp.data.workers.ChildWorkerFactory
import com.example.menuapp.data.workers.LoadDataWorker
import com.example.menuapp.di.annotations.WorkerKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(LoadDataWorker::class)
    abstract fun bindWorkerFactory(worker: LoadDataWorker.Factory): ChildWorkerFactory
}