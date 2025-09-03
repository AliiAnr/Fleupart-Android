package com.course.fleupart.data.store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.course.fleupart.data.model.remote.Detail
//import com.course.fleura.data.model.remote.AddressItem
//import com.course.fleura.data.model.remote.Detail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class DataStoreManager(private val context: Context) {

    /** Menyimpan status onboarding */
    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED_KEY] = completed
        }
    }

    /** Mendapatkan status onboarding */
    val onboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[ONBOARDING_COMPLETED_KEY] ?: false }

    /** Menyimpan status login */
    suspend fun setUserLoggedIn(loggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USER_LOGGED_IN_KEY] = loggedIn
        }
    }

    /** Mendapatkan status login */
    val isUserLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[USER_LOGGED_IN_KEY] ?: false }

    /** Menyimpan token pengguna */
    suspend fun saveUserToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN_KEY] = token
        }
    }

    /** Mendapatkan token pengguna */
    val userToken: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_TOKEN_KEY] }

    suspend fun setPersonalizedFilled(filled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PERSONALIZED_KEY] = filled
        }
    }

    val personalizedFilled: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[PERSONALIZED_KEY] ?: false }

    suspend fun saveUserDetail(detail: Detail) {
        val gson = Gson()
        val detailJson = gson.toJson(detail)
        context.dataStore.edit { preferences ->
            preferences[USER_DATA_KEY] = detailJson
        }
    }

    suspend fun getUserDetail(): Detail? {
        val preferences = context.dataStore.data.first()
        val detailJson = preferences[USER_DATA_KEY] ?: return null
        return Gson().fromJson(detailJson, Detail::class.java)
    }

    suspend fun isPersonalizeCompleted(): Boolean {
        val detail = getUserDetail()
        return detail?.isProfileComplete() ?: false
    }

//    val addressListFlow: Flow<List<AddressItem>> = context.dataStore.data
//        .map { prefs ->
//            prefs[ADDRESSES_KEY]?.let { json ->
//                val type = object : TypeToken<List<AddressItem>>() {}.type
//                Gson().fromJson(json, type)
//            } ?: emptyList()
//        }
//
//    suspend fun saveAddressList(addresses: List<AddressItem>) {
//        val json = Gson().toJson(addresses)
//        context.dataStore.edit { prefs ->
//            prefs[ADDRESSES_KEY] = json
//        }
//    }

//    suspend fun getUserAddressList(): List<AddressItem> {
//        val prefs = context.dataStore.data.first()
//        val json = prefs[ADDRESSES_KEY] ?: return emptyList()
//        val type = object : TypeToken<List<AddressItem>>() {}.type
//        return Gson().fromJson(json, type)
//    }

    /** Menghapus semua data (Logout) */
    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // Keys untuk DataStore
    companion object {
        private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")
        private val USER_LOGGED_IN_KEY = booleanPreferencesKey("user_logged_in")
        private val USER_TOKEN_KEY = stringPreferencesKey("user_token")
        private val USER_DATA_KEY = stringPreferencesKey("user_data")
        private val PERSONALIZED_KEY = booleanPreferencesKey("personalized_filled")
        private val ADDRESSES_KEY = stringPreferencesKey("address_list")
    }
}