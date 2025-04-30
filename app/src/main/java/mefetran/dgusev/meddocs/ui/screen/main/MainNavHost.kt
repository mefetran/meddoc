package mefetran.dgusev.meddocs.ui.screen.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import mefetran.dgusev.meddocs.R
import mefetran.dgusev.meddocs.ui.components.model.BottomNavigationDataItem
import mefetran.dgusev.meddocs.ui.screen.documents.Documents
import mefetran.dgusev.meddocs.ui.screen.documents.documentsDestination
import mefetran.dgusev.meddocs.ui.screen.home.Home
import mefetran.dgusev.meddocs.ui.screen.home.homeDestination
import mefetran.dgusev.meddocs.ui.screen.settings.Settings
import mefetran.dgusev.meddocs.ui.screen.settings.settingsDestination

@Composable
internal fun MainNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val bottomNavigationDataItems = remember {
        listOf(
            BottomNavigationDataItem(
                name = context.getString(R.string.nav_home),
                route = Home,
                icon = Icons.Default.Home,
            ),
            BottomNavigationDataItem(
                name = context.getString(R.string.nav_documents),
                route = Documents,
                icon = Icons.Default.MedicalServices,
            ),
            BottomNavigationDataItem(
                name = context.getString(R.string.nav_settings),
                route = Settings,
                icon = Icons.Default.Settings,
            ),
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavigationDataItems.forEach { bottomNavigationDataItem ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(
                                bottomNavigationDataItem.route::class
                            )
                        } == true,
                        icon = {
                            Icon(
                                imageVector = bottomNavigationDataItem.icon,
                                contentDescription = bottomNavigationDataItem.name
                            )
                        },
                        label = { Text(text = bottomNavigationDataItem.name) },
                        onClick = {
                            navController.navigate(bottomNavigationDataItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            homeDestination()
            settingsDestination()
            documentsDestination()
        }
    }
}