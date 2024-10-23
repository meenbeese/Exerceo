package com.health.openworkout.core.datatypes

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity
class User {
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0

    @JvmField
    @ColumnInfo
    var trainingsPlanId: Long = 0

    @JvmField
    @ColumnInfo
    var isMale: Boolean = true
}
