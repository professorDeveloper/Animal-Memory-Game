package com.azamovhudstc.memorygame.navigator

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.azamovhudstc.memorygame.navigator.NavigationHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import com.azamovhudstc.memorygame.navigator.Navigator
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NavigationDispatcher @Inject constructor() : Navigator, NavigationHandler {
    override val navigationStack = MutableSharedFlow<(NavController) -> Unit>()

    private suspend fun navigator(args: NavController.() -> Unit) {
        navigationStack.emit(args)
    }

    override suspend fun navigateTo(direction: NavDirections) = navigator { navigate(direction) }

    override suspend fun back() = navigator { popBackStack() }

}