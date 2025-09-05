package com.sproutjar.data.models

data class GlobalDialogState(
    val dialogInfo: DialogInfo = DialogInfo(),
    val onSuccess: (() -> Unit)? = null,
    val onDismiss: () -> Unit = {},
)