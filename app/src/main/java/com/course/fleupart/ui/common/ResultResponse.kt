package com.course.fleupart.ui.common

sealed class ResultResponse<out R> private constructor() {
    data class Success<out T>(val data: T) : ResultResponse<T>()
    data class Error(val error: String) : ResultResponse<Nothing>()
    data object Loading : ResultResponse<Nothing>()
    data object None : ResultResponse<Nothing>()
}