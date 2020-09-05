package com.example.todo.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "todo_table")
@Parcelize
data class Todo(
    var title: String?,
    var priority: Priority?,
    var description: String?
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}