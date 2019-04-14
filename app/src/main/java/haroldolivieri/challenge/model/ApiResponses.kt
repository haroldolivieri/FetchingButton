package haroldolivieri.challenge.model

import com.google.gson.annotations.SerializedName

data class ResponseCode(
        @SerializedName("path") val path: String,
        @SerializedName("response_code") val code: String
)

data class CorrectPath(
        @SerializedName("next_path") val nextPath: String
)