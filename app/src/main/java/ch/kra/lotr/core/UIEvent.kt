package ch.kra.lotr.core

sealed class UIEvent {
    data class ShowSnackbar(val message: String) : UIEvent()
    object PopBackStack: UIEvent()
    data class Navigate(val route: String): UIEvent()

}
