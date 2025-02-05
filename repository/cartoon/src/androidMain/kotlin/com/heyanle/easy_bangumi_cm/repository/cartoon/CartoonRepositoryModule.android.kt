package com.heyanle.easy_bangumi_cm.repository.cartoon

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.heyanle.easy_bangumi_cm.repository.cartoon.database.CartoonDatabase
import com.heyanle.lib.inject.api.InjectScope
import com.heyanle.lib.inject.api.get
import kotlinx.coroutines.Dispatchers

/**
 * Created by heyanlin on 2024/12/3.
 */
actual fun InjectScope.registerCartoonDatabase(): CartoonDatabase {
    val ctx = get<Context>()
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(CartoonDatabase.DB_FILE_NAME)
    return Room.databaseBuilder<CartoonDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .addMigrations(* Migrate.cartoonDatabaseMigration.toTypedArray())
        // .fallbackToDestructiveMigrationOnDowngrade(true)
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}