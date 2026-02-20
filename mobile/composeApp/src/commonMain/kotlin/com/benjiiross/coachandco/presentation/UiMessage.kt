package com.benjiiross.coachandco.presentation

sealed class UiMessage {
    data class Success(val text: String) : UiMessage()
    data class Error(val text: String) : UiMessage()
}
