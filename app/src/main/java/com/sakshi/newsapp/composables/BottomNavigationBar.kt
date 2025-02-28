package com.sakshi.newsapp.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sakshi.newsapp.R
import com.sakshi.newsapp.model.NewsArticle
import com.sakshi.newsapp.utils.HOME
import com.sakshi.newsapp.utils.HOME_LABEL
import com.sakshi.newsapp.utils.SAVED
import com.sakshi.newsapp.utils.SAVED_LABEL
import com.sakshi.newsapp.viewmodel.NewsViewModel

@Composable
fun BottomNavigationBar(
    bottomNavStartDestination: BottomNavItem,
    viewModel: NewsViewModel,
    onReadMoreClicked: (NewsArticle) -> Unit,
    onThemeChanged: (darkModeEnabled: Boolean) -> Unit,
    enableDarkTheme: Boolean
) {
    val navController = rememberNavController()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onThemeChanged(enableDarkTheme.not())
                },
                modifier = Modifier
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = if (enableDarkTheme) stringResource(R.string.light_theme) else stringResource(
                        R.string.dark_theme
                    ), modifier = Modifier
                        .padding(16.dp)
                )
            }
        },
        bottomBar = {
            NavigationBar {
                val items = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Saved
                )
                val currentRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = bottomNavStartDestination.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    onReadMoreClicked = onReadMoreClicked,
                    viewModel = viewModel
                )
            }
            composable(BottomNavItem.Saved.route) {
                SavedScreen(
                    onReadMoreClicked = onReadMoreClicked,
                    viewModel = viewModel
                )
            }
        }
    }
}


sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Home : BottomNavItem(HOME, Icons.Default.Home, HOME_LABEL)
    data object Saved : BottomNavItem(SAVED, Icons.Default.Favorite, SAVED_LABEL)
}