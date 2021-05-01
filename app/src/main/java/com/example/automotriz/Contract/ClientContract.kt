package com.example.automotriz.Contract

import android.provider.BaseColumns

object ClientContract {
    object Entry: BaseColumns {
        const val TABLE_NAME = "CTL_CLIENTS"
        const val COLUMN_REMOTE_ID ="RemoteId"
    }
}