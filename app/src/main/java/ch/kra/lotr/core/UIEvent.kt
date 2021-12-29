package ch.kra.lotr.core

sealed class UIEvent {
    data class ShowSnackbar(val message: String) : UIEvent()
}
