package com.example.githubuser

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.data.local.room.UserDao
import com.example.githubuser.data.local.room.UserDatabase
import com.example.githubuser.utils.AppExecutors

class UserRepository(
    application: Application
) {
    private var userDao: UserDao
    private val appExecutors: AppExecutors = AppExecutors()

    init {
        val db = UserDatabase.getInstance(application)
        userDao = db.userDao()
    }



    fun getFavUser(): LiveData<List<FavoriteUser>> = userDao.getFavoriteUserByUsername()

    fun checkUser(username: String?): Int {
        return userDao.checkUser(username)
    }

    fun addToFav(username: String?, avatar: String?) {
        appExecutors?.diskIO?.execute {
            var user = username?.let {
                FavoriteUser(
                    it,
                    avatar
                )
            }
            if (user != null) {
                 userDao?.insert(user as FavoriteUser)
            }
        }
    }

    fun removeFromFav(username: String?){
        appExecutors.diskIO.execute {
            userDao.deleteNonFavoriteUserByUsername(username)
        }
    }

}