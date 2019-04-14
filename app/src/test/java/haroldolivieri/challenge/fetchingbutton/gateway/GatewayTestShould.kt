package haroldolivieri.challenge.fetchingbutton.gateway

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class GatewayTestShould {

    private val api: Gateway.Api = mock()
    private val gateway = Gateway(api)

    @Test
    fun `call fetchCorrectPath() on Api when fetchCorrectPath() called on gateway`() {
        gateway.fetchCorrectPath()

        verify(api).fetchCorrectPath()
    }

    @Test
    fun `call fetchUUID() on Api when fetchUUID() called on gateway`() {
        val url = "next_path"

        gateway.fetchUUID(url)

        verify(api).fetchUUID(url)
    }
}