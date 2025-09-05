package com.sproutjar

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sproutjar.data.models.AppSettings
import com.sproutjar.data.models.DialogInfo
import com.sproutjar.data.models.GlobalDialogState
import com.sproutjar.data.models.MessageDialog
import com.sproutjar.navigation.Navigation
import com.sproutjar.navigation.NavigationBarItem
import com.sproutjar.navigation.Screens
import com.sproutjar.ui.composables.ConnectivityStatus
import com.sproutjar.ui.composables.GlobalDialog
import com.sproutjar.ui.composables.connectivityState
import com.sproutjar.ui.theme.Red
import com.sproutjar.ui.theme.SproutJarTheme
import com.sproutjar.utils.AppIcons
import com.sproutjar.utils.ConnectionState
import com.sproutjar.utils.ErrorService
import androidx.compose.ui.graphics.Color
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        AppIcons.initialize(packageInfo)

        enableEdgeToEdge()
        setContent {
            App(intent.extras)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(extras: Bundle?) {

    val viewModel = hiltViewModel<MainActivityViewModel>()
    val appSettings: AppSettings by viewModel.appSettings.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val connection by connectivityState()
    val globalDialog = remember { mutableStateOf<GlobalDialogState?>(null) }

    val showBottomBar = navController.currentBackStackEntryAsState().value.let { backStackEntry ->
        val currentRoute = backStackEntry?.destination?.route
        NavigationBarItem.items.any { navigationBarItem ->
            navigationBarItem.route.let {
                currentRoute?.startsWith(
                    it
                )
            } ?: false
        }
    }

    fun onLogout() {
        viewModel.saveAppSettings(AppSettings())
        navController.navigate(Screens.SplashScreen.name)
    }


    SproutJarTheme(theme = appSettings.theme) {
        globalDialog.value?.let {
            GlobalDialog(it) {
                if (it.dialogInfo.error == ErrorService.HTTP_401_UNAUTHORIZED)
                    onLogout()
                globalDialog.value = null
            }
        }

        Scaffold(bottomBar = {
            if (showBottomBar)
                NavigationBar {
                    NavigationBarItem.items.forEach { item ->
                        val selected = item.route.let {
                            navBackStackEntry?.destination?.route?.startsWith(
                                it
                            )
                        } ?: false
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.iconSelected else item.iconUnSelected,
                                    contentDescription = stringResource(id = item.title),
                                )
                            },
                            alwaysShowLabel = false,
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
        }) {
            Column {
                ConnectivityStatus(connection)
                Navigation(
                    connection = connection,
                    extras = extras,
                    navController = navController,
                    appSettings = appSettings,
                    saveAppSettings = { viewModel.saveAppSettings(it) },
                ) {
                    if (connection == ConnectionState.Unavailable)
                        globalDialog.value = GlobalDialogState(
                            DialogInfo(
                                MessageDialog(
                                    R.string.no_internet_connection,
                                    R.string.check_internet_connection
                                )
                            )
                        )
                    else
                        globalDialog.value = it
                }
            }
        }
    }
}

