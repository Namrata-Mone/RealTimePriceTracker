package com.multibank.realtimepricetracker.data.datasource.websocket

import com.multibank.realtimepricetracker.data.model.PriceUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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

    private val _events = MutableSharedFlow<WebSocketEvent>(
        extraBufferCapacity = 64
    )

    private var webSocket: WebSocket? = null

    @Volatile
    private var connected = false

    override fun events(): Flow<WebSocketEvent> = _events.asSharedFlow()

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
                    scope.launch {
                        _events.emit(WebSocketEvent.Connected)
                    }
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    scope.launch {
                        runCatching {
                            json.decodeFromString<PriceUpdate>(text)
                        }.onSuccess { priceUpdate ->
                            _events.emit(WebSocketEvent.PriceUpdateReceived(priceUpdate))
                        }.onFailure { throwable ->
                            _events.emit(WebSocketEvent.Failure(throwable))
                        }
                    }
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    webSocket.close(code, reason)
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    connected = false
                    this@WebSocketDataSourceImpl.webSocket = null
                    scope.launch {
                        _events.emit(WebSocketEvent.Disconnected)
                    }
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    connected = false
                    this@WebSocketDataSourceImpl.webSocket = null
                    scope.launch {
                        _events.emit(WebSocketEvent.Failure(t))
                        _events.emit(WebSocketEvent.Disconnected)
                    }
                }
            }
        )
    }

    override suspend fun disconnect() {
        webSocket?.close(1000, "Client disconnected")
        webSocket = null
        connected = false
        _events.emit(WebSocketEvent.Disconnected)
    }

    override suspend fun sendPriceUpdate(priceUpdate: PriceUpdate) {
        val message = json.encodeToString(priceUpdate)
        webSocket?.send(message)
    }

    override fun isConnected(): Boolean = connected

    private companion object {
        const val WEB_SOCKET_URL = "wss://ws.postman-echo.com/raw"
    }
}