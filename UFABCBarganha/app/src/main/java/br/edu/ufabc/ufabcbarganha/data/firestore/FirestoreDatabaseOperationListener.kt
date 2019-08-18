package br.edu.ufabc.ufabcbarganha.data.firestore

interface FirestoreDatabaseOperationListener<K>{

    fun onSuccess(result: K)
    fun onFailure(e: Exception)

}