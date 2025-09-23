package com.sproutjar.ui.screens.splashScreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sproutjar.MainActivityViewModel
import com.sproutjar.R
import com.sproutjar.data.models.AppSettings
import com.sproutjar.data.models.GlobalDialogState
import com.sproutjar.data.models.Languages
import com.sproutjar.data.models.Theme
import com.sproutjar.navigation.Screens
import com.sproutjar.ui.composables.Logo
import com.sproutjar.ui.theme.SproutJarTheme
import com.sproutjar.utils.AnimationStates
import com.sproutjar.utils.ConnectionState
import com.sproutjar.utils.DevicePreviews
import com.sproutjar.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    connection: ConnectionState,
    navController: NavHostController,
    appSettings: AppSettings,
    saveAppSettings: (AppSettings) -> Unit,
    showGlobalDialog: (GlobalDialogState) -> Unit
) {

    val viewModel = hiltViewModel<MainActivityViewModel>()
    val cdiToday by viewModel.cdiToday.collectAsState()


    val scope = rememberCoroutineScope()

    val animationStart = remember { mutableStateOf(AnimationStates.UNDEFINED) }
    val alphaAnimation = animateFloatAsState(
        targetValue = if (animationStart.value == AnimationStates.START || animationStart.value == AnimationStates.FINISHED) 1f else 0f,
        animationSpec = tween(3000),
        label = stringResource(R.string.logo)
    )

    LaunchedEffect(key1 = Unit) {
        startAnimation(animationStart)
        viewModel.fetchCdiToday()
    }

    LaunchedEffect(cdiToday, animationStart.value) {
        if (animationStart.value == AnimationStates.FINISHED && cdiToday is Resource.Success) {
            configApp(appSettings.language)

            if (!appSettings.biometrics) {
                navController.navigate(Screens.PotsScreen.name) {
                    popUpTo(0)
                }
            }
        }
    }

    if (appSettings.biometrics && animationStart.value == AnimationStates.FINISHED) {
        BiometricAuthentication(onSuccess = {
            scope.launch {
                animationStart.value = AnimationStates.UNDEFINED
                navController.navigate(Screens.PotsScreen.name) {
                    popUpTo(0)
                }
            }
        }, onCancel = {

        })
    }

    SplashScreenUI(
        theme = appSettings.theme, alpha = alphaAnimation.value
    )
}

@Composable
fun SplashScreenUI(theme: Theme, alpha: Float) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Logo(theme = theme)
        Spacer(Modifier.height(10.dp))
        Text(
            color = when (theme) {
                Theme.DARK -> Color.White
                Theme.LIGHT -> Color.Black
                Theme.SPROUT_JAR -> MaterialTheme.colorScheme.primary
            },
            style = MaterialTheme.typography.displayMedium,
            text = stringResource(R.string.app_name),
        )
    }
}

@Composable
fun BiometricAuthentication(
    onSuccess: () -> Unit,
    onCancel: () -> Unit,
    onError: () -> Unit = {},
) {
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val biometricManager = BiometricManager.from(context)
    val isBiometricAvailable =
        biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
    val executor = ContextCompat.getMainExecutor(context)

    if (activity != null && isBiometricAvailable == BiometricManager.BIOMETRIC_SUCCESS) {

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onCancel()
                }

                @RequiresApi(Build.VERSION_CODES.R)
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onError()
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .setTitle(stringResource(id = R.string.biometric_authentication))
            .setSubtitle(stringResource(id = R.string.login_biometric_credential))
            .setNegativeButtonText(stringResource(id = R.string.login_password))
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}

private suspend fun startAnimation(animationStart: MutableState<AnimationStates>) {
    animationStart.value = AnimationStates.START
    delay(4000)
    animationStart.value = AnimationStates.FINISHED
}

private fun configApp(language: Languages) {
    AppCompatDelegate.setApplicationLocales(
        LocaleListCompat.forLanguageTags(
            language.code
        )
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@DevicePreviews
@Composable
fun SplashPreview() {
    SproutJarTheme {
        Scaffold {
            SplashScreenUI(
                theme = Theme.SPROUT_JAR,
                alpha = 1f,
            )
        }
    }
}