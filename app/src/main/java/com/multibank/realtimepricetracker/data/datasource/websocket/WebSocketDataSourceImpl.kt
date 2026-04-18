package com.multibank.realtimepricetracker.data.datasource.websocket

import android.util.Log
import com.multibank.realtimepricetracker.core.common.NetworkConstants.WEB_SOCKET_URL
import com.multibank.realtimepricetracker.data.model.PriceUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketDataSourceImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val json: Json
) : WebSocketDataSource {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val eventFlow = MutableSharedFlow<WebSocketEvent>(extraBufferCapacity = 64)

    private var webSocket: WebSocket? = null

    @Volatile
    private var connected = false

    override fun events(): Flow<WebSocketEvent> = eventFlow.asSharedFlow()

    override suspend fun connect() {
        if (connected || webSocket != null) return

        val request = Request.Builder()
            .url(WEB_SOCKET_URL)
            .build()

        webSocket = okHttpClient.newWebSocket(
            request,
            object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    connected = true
                    Log.d(TAG, "WebSocket connected")
                    scope.launch {
                        eventFlow.emit(WebSocketEvent.Connected)
                    }
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    Log.d(TAG, "Raw echo response: $text")

                    scope.launch {
                        runCatching {
                            json.decodeFromString<PriceUpdate>(text)
                        }.onSuccess { priceUpdate ->
                            Log.d(TAG, "Parsed echo response: $priceUpdate")
                            eventFlow.emit(WebSocketEvent.PriceUpdateReceived(priceUpdate))
                        }.onFailure { throwable ->
                            Log.e(TAG, "Failed to parse echo response", throwable)
                            eventFlow.emit(WebSocketEvent.Failure(throwable))
                        }
                    }
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    Log.d(TAG, "Closing: $code / $reason")
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    connected = false
                    this@WebSocketDataSourceImpl.webSocket = null

                    Log.d(TAG, "WebSocket closed: $reason")

                    scope.launch {
                        eventFlow.emit(WebSocketEvent.Disconnected)
                    }

                    scope.launch {
                        delay(2000)
                        connect()
                    }
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    Log.e(TAG, "WebSocket failure: ${t.message}", t)

                    connected = false
                    this@WebSocketDataSourceImpl.webSocket = null

                    scope.launch {
                        eventFlow.emit(WebSocketEvent.Failure(t))
                        eventFlow.emit(WebSocketEvent.Disconnected)
                    }

                    scope.launch {
                        delay(2000)
                        Log.d(TAG, "Reconnecting WebSocket...")
                        connect()
                    }
                }
            }
        )
    }

    override suspend fun disconnect() {
        webSocket?.close(1000, "Client disconnected")
        webSocket = null
        connected = false
        eventFlow.emit(WebSocketEvent.Disconnected)
    }

    override suspend fun sendPriceUpdate(priceUpdate: PriceUpdate) {
        if (!connected || webSocket == null) {
            Log.d(TAG, "Skipping send, socket is not connected yet")
            return
        }

        val message = json.encodeToString(priceUpdate)
        Log.d(TAG, "Sending message: $message")
        webSocket?.send(message)
    }

    override fun isConnected(): Boolean = connected

    private companion object {
        const val TAG = "WebSocketDataSource"
    }
}