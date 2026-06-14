package com.getredeemcode.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "rc_prefs")

object AppDataStore {
    private val COINS_KEY = doublePreferencesKey("rc_coins")
    private val BONUS_CLAIMED_KEY = booleanPreferencesKey("rc_bonusClaimed")
    private val VISITED_KEY = booleanPreferencesKey("rc_visited")
    private val MY_REFER_CODE_KEY = stringPreferencesKey("rc_my_refer_code")
    private val REFER_STATS_KEY = stringPreferencesKey("rc_refer_stats")
    private val CAPTCHA_DATA_KEY = stringPreferencesKey("captcha_data")
    private val CODE_IDX_1000_KEY = intPreferencesKey("rc_codeIdx_1000")
    private val CODE_IDX_1650_KEY = intPreferencesKey("rc_codeIdx_1650")
    private val CODE_IDX_2000_KEY = intPreferencesKey("rc_codeIdx_2000")
    private val DEVICE_OFFSET_KEY = intPreferencesKey("rc_device_offset")

    fun getCoins(context: Context): Flow<Double> = context.dataStore.data.map { it[COINS_KEY] ?: 0.0 }
    suspend fun setCoins(context: Context, v: Double) { context.dataStore.edit { it[COINS_KEY] = v } }

    fun getBonusClaimed(context: Context): Flow<Boolean> = context.dataStore.data.map { it[BONUS_CLAIMED_KEY] ?: false }
    suspend fun setBonusClaimed(context: Context, v: Boolean) { context.dataStore.edit { it[BONUS_CLAIMED_KEY] = v } }

    fun getVisited(context: Context): Flow<Boolean> = context.dataStore.data.map { it[VISITED_KEY] ?: false }
    suspend fun setVisited(context: Context, v: Boolean) { context.dataStore.edit { it[VISITED_KEY] = v } }

    fun getMyReferCode(context: Context): Flow<String> = context.dataStore.data.map { it[MY_REFER_CODE_KEY] ?: "" }
    suspend fun setMyReferCode(context: Context, v: String) { context.dataStore.edit { it[MY_REFER_CODE_KEY] = v } }

    fun getReferStats(context: Context): Flow<String> = context.dataStore.data.map { it[REFER_STATS_KEY] ?: "" }
    suspend fun setReferStats(context: Context, v: String) { context.dataStore.edit { it[REFER_STATS_KEY] = v } }

    fun getCaptchaData(context: Context): Flow<String> = context.dataStore.data.map { it[CAPTCHA_DATA_KEY] ?: "" }
    suspend fun setCaptchaData(context: Context, v: String) { context.dataStore.edit { it[CAPTCHA_DATA_KEY] = v } }

    fun getCodeIdx(context: Context, tier: Int): Flow<Int> {
        val key = when(tier) { 1650 -> CODE_IDX_1650_KEY; 2000 -> CODE_IDX_2000_KEY; else -> CODE_IDX_1000_KEY }
        return context.dataStore.data.map { it[key] ?: 0 }
    }
    suspend fun setCodeIdx(context: Context, tier: Int, v: Int) {
        val key = when(tier) { 1650 -> CODE_IDX_1650_KEY; 2000 -> CODE_IDX_2000_KEY; else -> CODE_IDX_1000_KEY }
        context.dataStore.edit { it[key] = v }
    }

    fun getDeviceOffset(context: Context): Flow<Int> = context.dataStore.data.map { it[DEVICE_OFFSET_KEY] ?: -1 }
    suspend fun setDeviceOffset(context: Context, v: Int) { context.dataStore.edit { it[DEVICE_OFFSET_KEY] = v } }
}
