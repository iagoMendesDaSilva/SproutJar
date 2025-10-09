package com.sproutjar.data.models

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BeachAccess
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.HomeRepairService
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.PregnantWoman
import androidx.compose.material.icons.outlined.School
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.sproutjar.R
import com.sproutjar.utils.JsonNavType

enum class PotCategory(@StringRes val categoryDesc: Int) {
    Emergency(R.string.emergency),
    Travel(R.string.travel),
    House(R.string.house),
    Automobile(R.string.automobile),
    Education(R.string.education),
    Retirement(R.string.retirement),
    Wedding(R.string.wedding),
    Pregnancy(R.string.pregnancy),
    Pet(R.string.pet)
}

@Entity(tableName = "pots")
data class Pot(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val potCategory: PotCategory,
    val title: String,
    val cdiPercent: Double = 1.0,
) {
    override fun toString(): String = Uri.encode(Gson().toJson(this))

}

class PotArgType : JsonNavType<Pot>() {

    companion object {
        val potArgType = PotArgType()
    }

    override fun fromJsonParse(value: String): Pot =
        Gson().fromJson(value, Pot::class.java)

    override fun Pot.getJsonParse(): String = Gson().toJson(this)

}


fun PotCategory.asImageVector(): ImageVector {
    return when (this) {
        PotCategory.Emergency -> Icons.Outlined.HomeRepairService
        PotCategory.Travel -> Icons.Outlined.Flight
        PotCategory.House -> Icons.Outlined.Home
        PotCategory.Automobile -> Icons.Outlined.DirectionsCar
        PotCategory.Education -> Icons.Outlined.School
        PotCategory.Retirement -> Icons.Outlined.BeachAccess
        PotCategory.Wedding -> Icons.Outlined.Celebration
        PotCategory.Pregnancy -> Icons.Outlined.PregnantWoman
        PotCategory.Pet -> Icons.Outlined.Pets
    }
}