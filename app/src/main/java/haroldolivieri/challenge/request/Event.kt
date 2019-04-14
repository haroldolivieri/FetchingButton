package haroldolivieri.challenge.request

data class Event<T>(val type: Type, val data: T?, val error: Throwable?) {
    companion object {
        @JvmStatic
        fun <T> loading() = Event<T>(Type.LOADING, null, null)

        @JvmStatic
        fun <T> data(data: T) = Event(Type.IDLE, data, null)

        @JvmStatic
        fun <T> error(error: Throwable): Event<T> = Event(Type.ERROR, null, error)
    }

    enum class Type {
        LOADING, IDLE, ERROR
    }
}

inline fun <T> Event<T>.reducer(
    onLoading: () -> Unit = {},
    onError: () -> Unit = {},
    onDataArrived: (data: T) -> Unit = {}
) {
    when (type) {
        Event.Type.LOADING -> {
            onLoading()
        }
        Event.Type.ERROR -> {
            onError()
        }
        Event.Type.IDLE -> {
            onDataArrived(data!!)
        }
    }
}