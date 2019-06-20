package org.seniorlaguna.calculator.basic.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Calculation(
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo var title : String,
    @ColumnInfo var calculation: String
)