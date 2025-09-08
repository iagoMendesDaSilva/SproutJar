package com.sproutjar.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light Phone",
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp",
)
@Preview(
    name = "Dark Phone",
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Tablet",
    showSystemUi = true,
    device =  "spec:width=800dp,height=1280dp",
)
@Preview(
    name = "Dark Tablet",
    showSystemUi = true,
    device = "spec:width=800dp,height=1280dp",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class DevicePreviews()