package br.edu.ufabc.ufabcbarganha.data.firestore

import com.google.firebase.firestore.FirebaseFirestore


object BarganhaFirebaseDatabase {



    private var _firestoreInstance: FirebaseFirestore? = null


    fun getInstance(): FirebaseFirestore{
        if(_firestoreInstance == null){
            _firestoreInstance = FirebaseFirestore.getInstance()
        }
        return _firestoreInstance!!
    }


}