package com.sproutjar.utils

import com.sproutjar.data.models.DialogInfo
import com.sproutjar.data.models.LoadingStates


sealed class Resource<T>(
    val data: T? = null,
    val dialogInfo: DialogInfo = DialogInfo(),
    val loadingState: LoadingStates = LoadingStates.LOADING
) {
    class Success<T>(data: T) : Resource<T>(data = data)

    class Error<T>(dialogInfo: DialogInfo = DialogInfo()) : Resource<T>(dialogInfo = dialogInfo)
    class Loading<T>(state: LoadingStates = LoadingStates.LOADING) :
        Resource<T>(loadingState = state)

    class Undefined<T> : Resource<T>()
}