package br.edu.ufabc.ufabcbarganha.user.data

import com.google.firebase.auth.FirebaseAuth

object FirebaseUserHelper{

    private var firebaseAuth: FirebaseAuth? = null

    fun getAuth(): FirebaseAuth{
        if(firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance()
        }
        return firebaseAuth!!
    }

    fun isLogged(): Boolean{
        return getAuth().currentUser != null
    }

    fun getUserId(): String?{
        return getAuth().currentUser?.uid
    }

    fun getUserName(): String{
        return getAuth().currentUser.let { if(it == null || it.displayName == null) "" else it.displayName!! }
    }

    fun getUserEmail(): String{
        return getAuth().currentUser.let { if(it == null || it.email == null) "" else it.email!! }
    }

}