package mas.com.health_diary.data.provider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import mas.com.health_diary.data.entity.Health
import mas.com.health_diary.data.entity.User
import mas.com.health_diary.data.errors.NoAuthException
import mas.com.health_diary.data.model.HealthResult
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class FireStoreProvider(val store: FirebaseFirestore, val auth: FirebaseAuth) : RemoteDataProvider {

    companion object {
        private const val HEALTH_COLLECTION = "health"
        private const val USER_COLLECTION = "users"

    }

    private val currentUser
        get() = auth.currentUser

    val userDataCollection
        get() = currentUser?.let {
            store.collection(USER_COLLECTION).document(it.uid).collection(HEALTH_COLLECTION)
        } ?: throw NoAuthException()

    override suspend fun getCurrentUser(): User? = suspendCoroutine { continuation ->
        currentUser?.let {
            User(it.displayName ?: "", it.email ?: "")
        }.let {
            continuation.resume(it)
        }
    }

    override fun subscribeToAllData(): ReceiveChannel<HealthResult> =
        Channel<HealthResult>(Channel.CONFLATED).apply {
            var registration: ListenerRegistration? = null

            try {
                registration = userDataCollection.addSnapshotListener { snapshot, e ->
                    val value = e?.let {
                        HealthResult.Error(it)
                    } ?: snapshot?.let {
                        val health = snapshot.documents.map { doc ->
                            doc.toObject(Health::class.java)
                        }
                        HealthResult.Success(health)
                    }

                    value?.let { offer(it) }
                }
            } catch (e: Throwable) {
                offer(HealthResult.Error(e))
            }

            invokeOnClose {
                registration?.remove()
            }
        }

    override suspend fun getDataById(id: String): Health = suspendCoroutine { continuation ->
        try {
            userDataCollection.document(id).get()
                .addOnSuccessListener { snapshot ->
                    continuation.resume(snapshot.toObject(Health::class.java)!!)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun saveData(health: Health): Health = suspendCoroutine { continuation ->
        try {
            userDataCollection.document(health.id).set(health)
                .addOnSuccessListener {
                    continuation.resume(health)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun deleteData(healthId: String): Unit = suspendCoroutine { continuation ->
        try {
            userDataCollection.document(healthId).delete()
                .addOnSuccessListener { snapshot ->
                    continuation.resume(Unit)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }


}
