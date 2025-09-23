package com.sproutjar.data.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class PotIcon {
    Home,
    Add,
    Star,
}

@Entity(tableName = "pots")
data class Pot(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val icon: PotIcon,
    val title: String,
    val cdiPercent: Double = 100.0,
)

data class PotCard(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val icon: PotIcon,
    val title: String,
    val cdiPercent: Double = 100.0,
)

fun PotIcon.asImageVector(): ImageVector {
    return when (this) {
        PotIcon.Home -> Icons.Outlined.Home
        PotIcon.Add -> Icons.Outlined.Add
        PotIcon.Star -> Icons.Outlined.Star
    }
}